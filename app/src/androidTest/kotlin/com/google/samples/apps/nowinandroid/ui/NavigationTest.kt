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

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import com.google.samples.apps.nowinandroid.MainActivity
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.rules.GrantPostNotificationsPermissionRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty
import com.google.samples.apps.nowinandroid.feature.bookmarks.R as BookmarksR
import com.google.samples.apps.nowinandroid.feature.foryou.R as FeatureForyouR
import com.google.samples.apps.nowinandroid.feature.search.R as FeatureSearchR
import com.google.samples.apps.nowinandroid.feature.settings.R as SettingsR

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
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Grant [android.Manifest.permission.POST_NOTIFICATIONS] permission.
     */
    @get:Rule(order = 2)
    val postNotificationsPermission = GrantPostNotificationsPermissionRule()

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var topicsRepository: TopicsRepository

    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val navigateUp by composeTestRule.stringResource(FeatureForyouR.string.feature_foryou_navigate_up)
    private val forYou by composeTestRule.stringResource(FeatureForyouR.string.feature_foryou_title)
    private val interests by composeTestRule.stringResource(FeatureSearchR.string.feature_search_interests)
    private val sampleTopic = "Headlines"
    private val appName by composeTestRule.stringResource(R.string.app_name)
    private val saved by composeTestRule.stringResource(BookmarksR.string.feature_bookmarks_title)
    private val settings by composeTestRule.stringResource(SettingsR.string.feature_settings_top_app_bar_action_icon_description)
    private val brand by composeTestRule.stringResource(SettingsR.string.feature_settings_brand_android)
    private val ok by composeTestRule.stringResource(SettingsR.string.feature_settings_dismiss_dialog_button_text)

    @Before
    fun setup() = hiltRule.inject()

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

            onNodeWithText(saved).performClick()
            onNodeWithContentDescription(navigateUp).assertDoesNotExist()

            onNodeWithText(interests).performClick()
            onNodeWithContentDescription(navigateUp).assertDoesNotExist()
        }
    }

    @Test
    fun topLevelDestinations_showTopBarWithTitle() {
        composeTestRule.apply {
            // Verify that the top bar contains the app name on the first screen.
            onNodeWithText(appName).assertExists()

            // Go to the saved tab, verify that the top bar contains "saved". This means
            // we'll have 2 elements with the text "saved" on screen. One in the top bar, and
            // one in the bottom navigation.
            onNodeWithText(saved).performClick()
            onAllNodesWithText(saved).assertCountEquals(2)

            // As above but for the interests tab.
            onNodeWithText(interests).performClick()
            onAllNodesWithText(interests).assertCountEquals(2)
        }
    }

    @Test
    fun topLevelDestinations_showSettingsIcon() {
        composeTestRule.apply {
            onNodeWithContentDescription(settings).assertExists()

            onNodeWithText(saved).performClick()
            onNodeWithContentDescription(settings).assertExists()

            onNodeWithText(interests).performClick()
            onNodeWithContentDescription(settings).assertExists()
        }
    }

    @Test
    fun whenSettingsIconIsClicked_settingsDialogIsShown() {
        composeTestRule.apply {
            onNodeWithContentDescription(settings).performClick()

            // Check that one of the settings is actually displayed.
            onNodeWithText(brand).assertExists()
        }
    }

    @Test
    fun whenSettingsDialogDismissed_previousScreenIsDisplayed() {
        composeTestRule.apply {
            // Navigate to the saved screen, open the settings dialog, then close it.
            onNodeWithText(saved).performClick()
            onNodeWithContentDescription(settings).performClick()
            onNodeWithText(ok).performClick()

            // Check that the saved screen is still visible and selected.
            onNode(
                hasText(saved) and
                    hasAnyAncestor(
                        hasTestTag("NiaBottomBar") or hasTestTag("NiaNavRail"),
                    ),
            ).assertIsSelected()
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

            // Select the last topic
            val topic = runBlocking {
                topicsRepository.getTopics().first().sortedBy(Topic::name).last().name
            }
            onNodeWithTag("interests:topics").performScrollToNode(hasText(topic))
            onNodeWithText(topic).performClick()

            // Switch tab
            onNodeWithText(forYou).performClick()

            // Come back to Interests
            onNodeWithText(interests).performClick()

            // Verify we're not in the list of interests
            onNodeWithTag("interests:topics").assertDoesNotExist()
        }
    }
}
