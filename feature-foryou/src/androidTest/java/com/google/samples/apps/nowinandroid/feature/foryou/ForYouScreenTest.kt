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

package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import org.junit.Rule
import org.junit.Test

class ForYouScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun circularProgressIndicator_whenScreenIsLoading_exists() {
        composeTestRule.setContent {
            ForYouScreen(
                uiState = ForYouFeedUiState.Loading,
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.for_you_loading)
            )
            .assertExists()
    }

    @Test
    fun topicSelector_whenNoTopicsSelected_showsTopicChipsAndDisabledDoneButton() {
        composeTestRule.setContent {
            ForYouScreen(
                uiState = ForYouFeedUiState.PopulatedFeed.FeedWithTopicSelection(
                    selectedTopics = listOf(
                        Topic(
                            id = 0,
                            name = "Headlines",
                            description = ""
                        ) to false,
                        Topic(
                            id = 1,
                            name = "UI",
                            description = ""
                        ) to false,
                        Topic(
                            id = 2,
                            name = "Tools",
                            description = ""
                        ) to false
                    ),
                    feed = emptyList()
                ),
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {}
            )
        }

        composeTestRule
            .onNodeWithText("HEADLINES")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("UI")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("TOOLS")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.done))
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()
    }

    @Test
    fun topicSelector_whenSomeTopicsSelected_showsTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            ForYouScreen(
                uiState = ForYouFeedUiState.PopulatedFeed.FeedWithTopicSelection(
                    selectedTopics = listOf(
                        Topic(
                            id = 0,
                            name = "Headlines",
                            description = ""
                        ) to false,
                        Topic(
                            id = 1,
                            name = "UI",
                            description = ""
                        ) to true,
                        Topic(
                            id = 2,
                            name = "Tools",
                            description = ""
                        ) to false
                    ),
                    feed = emptyList()
                ),
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {}
            )
        }

        composeTestRule
            .onNodeWithText("HEADLINES")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("UI")
            .assertIsDisplayed()
            .assertIsOn()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("TOOLS")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.resources.getString(R.string.done))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }
}
