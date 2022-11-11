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
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource

internal class TopicRobot(
    private val composeTestRule: AndroidComposeTestRule<
        ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    private val topicLoading = composeTestRule.activity.resources.getString(R.string.topic_loading)

    fun setContent(topicUiState: TopicUiState, newsUiState: NewsUiState) {
        composeTestRule.setContent {
            TopicScreen(
                topicUiState = topicUiState,
                newsUiState = newsUiState,
                onBackClick = { },
                onFollowClick = { },
                onBookmarkChanged = { _, _ -> },
            )
        }
    }

    fun loadingIndicatorExists() {
        composeTestRule
            .onNodeWithContentDescription(topicLoading)
            .assertExists()
    }

    fun topicExists(topic: FollowableTopic) {
        composeTestRule
            .onNodeWithText(topic.topic.name)
            .assertExists()

        composeTestRule
            .onNodeWithText(topic.topic.longDescription)
            .assertExists()
    }

    fun scrollToNewsResource(newsResource: NewsResource) {
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(hasText(newsResource.title))
    }
}
