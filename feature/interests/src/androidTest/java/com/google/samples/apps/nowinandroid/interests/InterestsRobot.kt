/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.google.samples.apps.nowinandroid.interests

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.samples.apps.nowinandroid.feature.interests.InterestsScreen
import com.google.samples.apps.nowinandroid.feature.interests.InterestsTabState
import com.google.samples.apps.nowinandroid.feature.interests.InterestsUiState
import com.google.samples.apps.nowinandroid.feature.interests.R.string

internal class InterestsRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    private val interestsLoading = getString(string.interests_loading)
    private val interestsEmptyHeader = getString(string.interests_empty_header)
    private val interestsTopicCardFollowButton =
        getString(string.interests_card_follow_button_content_desc)
    private val interestsTopicCardUnfollowButton =
        getString(string.interests_card_unfollow_button_content_desc)

    fun setContent(uiState: InterestsUiState, tabIndex: Int = 0) {
        composeTestRule.setContent {
            InterestsScreen(
                uiState = uiState,
                tabState = InterestsTabState(
                    titles = listOf(string.interests_topics, string.interests_people),
                    currentIndex = tabIndex
                ),
                followAuthor = { _, _ -> },
                followTopic = { _, _ -> },
                navigateToAuthor = {},
                navigateToTopic = {},
                switchTab = {},
            )
        }
    }

    fun interestsLoadingExists() {
        composeTestRule
            .onNodeWithContentDescription(interestsLoading)
            .assertExists()
    }

    fun nodeWithTextDisplayed(text: String) {
        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()
    }

    fun nodesWithTextCountEquals(text: String, count: Int) {
        composeTestRule
            .onAllNodesWithText(text)
            .assertCountEquals(count)
    }

    fun interestsTopicCardFollowButtonCountEquals(count: Int) {
        nodesWithContentDescriptionCountEquals(interestsTopicCardFollowButton, count)
    }

    fun interestsTopicCardUnfollowButtonCountEquals(count: Int) {
        nodesWithContentDescriptionCountEquals(interestsTopicCardUnfollowButton, count)
    }

    private fun nodesWithContentDescriptionCountEquals(text: String, count: Int) {
        composeTestRule
            .onAllNodesWithContentDescription(text)
            .assertCountEquals(count)
    }

    fun interestsEmptyHeaderDisplayed() {
        composeTestRule
            .onNodeWithText(interestsEmptyHeader)
            .assertIsDisplayed()
    }

    private fun getString(@StringRes stringId: Int) =
        composeTestRule.activity.resources.getString(stringId)
}