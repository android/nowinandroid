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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.feature.foryou.R.string

internal class ForYouRobot(
    private val composeTestRule: AndroidComposeTestRule<
        ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    private val doneButtonMatcher by lazy {
        hasText(composeTestRule.activity.resources.getString(string.done))
    }

    fun setContent(
        isSyncing: Boolean,
        interestsSelectionState: ForYouInterestsSelectionUiState,
        feedState: NewsFeedUiState,
    ) {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    isSyncing = isSyncing,
                    interestsSelectionState = interestsSelectionState,
                    feedState = feedState,
                    onTopicCheckedChanged = { _, _ -> },
                    onAuthorCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }
    }

    fun loadingIndicatorExists() {
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(string.for_you_loading)
            )
            .assertExists()
    }

    fun clickableAuthorExists(author: FollowableAuthor) {
        composeTestRule
            .onNodeWithText(author.author.name)
            .assertExists()
            .assertHasClickAction()
    }

    fun clickableTopicExists(topic: FollowableTopic) {
        composeTestRule
            .onNodeWithText(topic.topic.name)
            .assertExists()
            .assertHasClickAction()
    }

    fun scrollToDoneButton() {
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)
    }

    fun clickableDoneButtonExists(isEnabled: Boolean) {
        composeTestRule
            .onNode(doneButtonMatcher)
            .assertExists()
            .assertHasClickAction()
            .apply {
                if (isEnabled) assertIsEnabled() else assertIsNotEnabled()
            }
    }

    fun clickableNewsResourceExists(newsResource: NewsResource) {
        composeTestRule
            .onNodeWithText(
                newsResource.title,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }

    fun scrollToNewsResource(newsResource: NewsResource) {
        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(
                hasText(
                    newsResource.title,
                    substring = true
                )
            )
    }
}
