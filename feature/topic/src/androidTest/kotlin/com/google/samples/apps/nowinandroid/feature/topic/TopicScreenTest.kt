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

package com.google.samples.apps.nowinandroid.feature.topic

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.google.samples.apps.nowinandroid.core.testing.data.followableTopicTestData
import com.google.samples.apps.nowinandroid.core.testing.data.userNewsResourcesTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Topic screen;
 * Verifies that, when a specific UiState is set, the corresponding
 * composables and details are shown
 */
class TopicScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var topicLoading: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            topicLoading = getString(R.string.feature_topic_loading)
        }
    }

    @Test
    fun niaLoadingWheel_whenScreenIsLoading_showLoading() {
        composeTestRule.setContent {
            TopicScreen(
                topicUiState = TopicUiState.Loading,
                newsUiState = NewsUiState.Loading,
                onBackClick = {},
                onFollowClick = {},
                onTopicClick = {},
                onBookmarkChanged = { _, _ -> },
                onNewsResourceViewed = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(topicLoading)
            .assertExists()
    }

    @Test
    fun topicTitle_whenTopicIsSuccess_isShown() {
        val testTopic = followableTopicTestData.first()
        composeTestRule.setContent {
            TopicScreen(
                topicUiState = TopicUiState.Success(testTopic),
                newsUiState = NewsUiState.Loading,
                onBackClick = {},
                onFollowClick = {},
                onTopicClick = {},
                onBookmarkChanged = { _, _ -> },
                onNewsResourceViewed = {},
            )
        }

        // Name is shown
        composeTestRule
            .onNodeWithText(testTopic.topic.name)
            .assertExists()

        // Description is shown
        composeTestRule
            .onNodeWithText(testTopic.topic.longDescription)
            .assertExists()
    }

    @Test
    fun news_whenTopicIsLoading_isNotShown() {
        composeTestRule.setContent {
            TopicScreen(
                topicUiState = TopicUiState.Loading,
                newsUiState = NewsUiState.Success(userNewsResourcesTestData),
                onBackClick = {},
                onFollowClick = {},
                onTopicClick = {},
                onBookmarkChanged = { _, _ -> },
                onNewsResourceViewed = {},
            )
        }

        // Loading indicator shown
        composeTestRule
            .onNodeWithContentDescription(topicLoading)
            .assertExists()
    }

    @Test
    fun news_whenSuccessAndTopicIsSuccess_isShown() {
        val testTopic = followableTopicTestData.first()
        composeTestRule.setContent {
            TopicScreen(
                topicUiState = TopicUiState.Success(testTopic),
                newsUiState = NewsUiState.Success(
                    userNewsResourcesTestData,
                ),
                onBackClick = {},
                onFollowClick = {},
                onTopicClick = {},
                onBookmarkChanged = { _, _ -> },
                onNewsResourceViewed = {},
            )
        }

        // Scroll to first news title if available
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(hasText(userNewsResourcesTestData.first().title))
    }
}
