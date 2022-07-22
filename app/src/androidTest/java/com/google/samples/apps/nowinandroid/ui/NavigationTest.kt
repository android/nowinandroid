/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.ui

import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import com.google.samples.apps.nowinandroid.MainActivity
import com.google.samples.apps.nowinandroid.R
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Tests all the navigation flows that are handled by the navigation library.
 */
@HiltAndroidTest
class NavigationTest {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // The strings used for matching in these tests
    private lateinit var done: String
    private lateinit var navigateUp: String
    private lateinit var forYouLoading: String
    private lateinit var forYou: String
    private lateinit var interests: String
    private lateinit var sampleTopic: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            done = getString(R.string.done)
            navigateUp = getString(R.string.navigate_up)
            forYouLoading = getString(R.string.for_you_loading)
            forYou = getString(R.string.for_you)
            interests = getString(R.string.interests)
            sampleTopic = "Headlines"
        }
    }

    @Test
    fun firstScreen_isForYou() {
        composeTestRule.apply {
            // VERIFY for you is selected
            onNodeWithText(forYou).assertIsSelected()
        }
    }

    // TODO: implement tests related to navigation & resetting of destinations (b/213307564)
    // Restoring content should be tested with another tab than the For You one, as that will
    // still succeed even when restoring state is turned off.
    /**
     * When navigating between the different top level destinations, we should restore the state
     * of previously visited destinations.
     */
    @Test
    fun navigationBar_navigateToPreviouslySelectedTab_restoresContent() {
        composeTestRule.apply {
            // GIVEN the user follows a topic
            onNodeWithText(sampleTopic).performClick()
            // WHEN the user navigates to the Interests destination
            onNodeWithText(interests).performClick()
            // AND the user navigates to the For You destination
            onNodeWithText(forYou).performClick()
            // THEN the state of the For You destination is restored
            onNodeWithContentDescription(sampleTopic).assertIsOn()
        }
    }

    /**
     * When reselecting a tab, it should show that tab's start destination and restore its state.
     */
    @Test
    fun navigationBar_reselectTab_keepsState() {
        composeTestRule.apply {
            // GIVEN the user follows a topic
            onNodeWithText(sampleTopic).performClick()
            // WHEN the user taps the For You navigation bar item
            onNodeWithText(forYou).performClick()
            // THEN the state of the For You destination is restored
            onNodeWithContentDescription(sampleTopic).assertIsOn()
        }
    }

//    @Test
//    fun navigationBar_reselectTab_resetsToStartDestination() {
//        // GIVEN the user is on the Topics destination and scrolls
//        // and navigates to the Topic Detail destination
//        // WHEN the user taps the Topics navigation bar item
//        // THEN the Topics destination shows in the same scrolled state
//    }

    /*
     * Top level destinations should never show an up affordance.
     */
    @Test
    fun topLevelDestinations_doNotShowUpArrow() {
        composeTestRule.apply {
            // GIVEN the user is on any of the top level destinations, THEN the Up arrow is not shown.
            onNodeWithContentDescription(navigateUp).assertDoesNotExist()
            // TODO: Add top level destinations here, see b/226357686.
            onNodeWithText(interests).performClick()
            onNodeWithContentDescription(navigateUp).assertDoesNotExist()
        }
    }

    /*
     * There should always be at most one instance of a top-level destination at the same time.
     */
    @Test(expected = NoActivityResumedException::class)
    fun homeDestination_back_quitsApp() {
        composeTestRule.apply {
            // GIVEN the user navigates to the Interests destination
            onNodeWithText(interests).performClick()
            // and then navigates to the For you destination
            onNodeWithText(forYou).performClick()
            // WHEN the user uses the system button/gesture to go back
            Espresso.pressBack()
            // THEN the app quits
        }
    }

    /*
     * When pressing back from any top level destination except "For you", the app navigates back
     * to the "For you" destination, no matter which destinations you visited in between.
     */
    @Test
    fun navigationBar_backFromAnyDestination_returnsToForYou() {
        composeTestRule.apply {
            // GIVEN the user navigated to the Interests destination
            onNodeWithText(interests).performClick()
            // TODO: Add another destination here to increase test coverage, see b/226357686.
            // WHEN the user uses the system button/gesture to go back,
            Espresso.pressBack()
            // THEN the app shows the For You destination
            onNodeWithText(forYou).assertExists()
        }
    }

    @Test
    fun navigationBar_multipleBackStackInterests() {
        composeTestRule.apply {
            onNodeWithText(interests).performClick()
            onNodeWithText("Android Studio").performClick() // TODO: Grab string from fake data

            // Switch tab
            onNodeWithText(forYou).performClick()

            // Come back to Interests
            onNodeWithText(interests).performClick()

            // Verify we're not in the list of interests
            onNodeWithText("Android Auto").assertDoesNotExist() // TODO: Grab string from fake data
        }
    }
}
