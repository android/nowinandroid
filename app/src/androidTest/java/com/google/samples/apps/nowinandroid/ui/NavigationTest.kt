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

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests all the navigation flows that are handled by the navigation library.
 */
class NavigationTest {

    /**
     * Using an empty activity to have control of the content that is set.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        // Using targetContext as the Context of the instrumentation code
        composeTestRule.setContent {
            NiaApp()
        }
    }

    @Test
    fun firstScreenIsForYou() {
        composeTestRule.forYouDestinationTopMatcher()
            .assertExists("Could not find FOR YOU text on first screen after app open")
    }

    // TODO: implement tests related to navigation & resetting of destinations (b/213307564)
    /**
     * As per guidelines:
     *
     * When you select a navigation bar item (one that’s not currently selected), the app navigates
     * to that destination’s screen.
     *
     * Any prior user interactions and temporary screen states are reset, such as scroll position,
     * tab selection, and inline search.
     *
     * This default behavior can be overridden when needed to improve the user experience. For
     * example, an Android app that requires frequent switching between sections can preserve each
     * section’s state.
     */
//    @Test
//    fun navigateToUnselectedTabResetsContent1() {
//        // GIVEN the user was previously on the Topics destination
//        composeTestRule.topicsDestinationTopMatcher().performClick()
//        // and scrolled down
//        [IMPLEMENT] Match the root scrollable container and scroll down to an item below the fold
//        composeTestRule.topicsDestinationTopMatcher()
//            .assertDoesNotExist() // verify we scrolled beyond the top
//        // and then navigated back to the For You destination
//        composeTestRule.forYouDestinationTopMatcher().performClick()
//        // WHEN the user presses the Topic navigation bar item
//        composeTestRule.topicsDestinationTopMatcher().performClick()
//        // THEN the Topics destination shows at the top.
//        composeTestRule.topicsDestinationTopMatcher()
//            .assertExists("Screen did not correctly reset to the top after re-navigating to it")
//    }

//    @Test
//    fun navigateToUnselectedTabResetsContent2() {
//        // GIVEN the user was previously on the Topics destination
//        composeTestRule.topicsDestinationTopMatcher().performClick()
//        // and navigated to the Topic detail destination
//        [IMPLEMENT] Navigate to topic detail destination
//        composeTestRule.topicsDestinationTopMatcher()
//            .assertDoesNotExist() // verify we are not on topics overview destination any more
//        // and then navigated back to the For You destination
//        composeTestRule.forYouDestinationTopMatcher().performClick()
//        // WHEN the user presses the Topic navigation bar item
//        composeTestRule.topicsDestinationTopMatcher().performClick()
//        // THEN the Topics destination shows at the top.
//        composeTestRule.topicsDestinationTopMatcher()
//            .assertExists("Screen did not correctly reset to the top after re-navigating to it")
//    }

//    @Test
//    fun reselectingTabResetsContent1() {
//        // GIVEN the user is on the For You destination
//        // and has scrolled down
//        // WHEN the user taps the For You navigation bar item
//        // THEN the For You destination shows at the top of the destination
//    }

//    @Test
//    fun reselectingTabResetsContent2() {
//        // GIVEN the user is on the Topics destination
//        // and navigates to the Topic Detail destination
//        // WHEN the user taps the Topics navigation bar item
//        // THEN the Topics destination shows at the top of the destination
//    }

    /*
     * Top level destinations should never show an up affordance.
     */
    @Test
    fun topLevelDestinationsDoNotShowUpArrow() {
        // GIVEN the user is on any of the top level destinations, THEN the Up arrow is not shown.
        composeTestRule.onNodeWithContentDescription("Navigate up").assertDoesNotExist()
        composeTestRule.onNodeWithText("Episodes").performClick()
        composeTestRule.onNodeWithContentDescription("Navigate up").assertDoesNotExist()
        composeTestRule.onNodeWithText("Saved").performClick()
        composeTestRule.onNodeWithContentDescription("Navigate up").assertDoesNotExist()
        composeTestRule.onNodeWithText("Topics").performClick()
        composeTestRule.onNodeWithContentDescription("Navigate up").assertDoesNotExist()
    }

    /*
     * There should always be at most one instance of a top-level destination at the same time.
     */
    @Test(expected = NoActivityResumedException::class)
    fun backFromHomeDestinationQuitsApp() {
        // GIVEN the user navigates to the Episodes destination
        composeTestRule.onNodeWithText("Episodes").performClick()
        // and then navigates to the For you destination
        composeTestRule.onNodeWithText("For you").performClick()
        // WHEN the user uses the system button/gesture to go back
        Espresso.pressBack()
        // THEN the app quits
    }

    /*
     * When pressing back from any top level destination except "For you", the app navigates back
     * to the "For you" destination, no matter which destinations you visited in between.
     */
    @Test
    fun backFromDestinationReturnsToForYou() {
        // GIVEN the user navigated to the Episodes destination
        composeTestRule.onNodeWithText("Episodes").performClick()
        // and then navigated to the Topics destination
        composeTestRule.onNodeWithText("Topics").performClick()
        // WHEN the user uses the system button/gesture to go back,
        Espresso.pressBack()
        // THEN the app shows the For You destination
        composeTestRule.forYouDestinationTopMatcher().assertExists()
    }

    /*
     * Matches an element at the top of the For You destination. Should be updated when the
     * destination is implemented.
     */
    private fun ComposeTestRule.forYouDestinationTopMatcher() = onNodeWithText("FOR YOU")

    /*
     * Matches an element at the top of the Topics destination. Should be updated when the
     * destination is implemented.
     */
    private fun ComposeTestRule.topicsDestinationTopMatcher() = onNodeWithText("TOPICS")
}
