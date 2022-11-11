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

package com.google.samples.apps.nowinandroid.feature.bookmarks

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState

internal class BookmarksRobot(
    private val composeTestRule: AndroidComposeTestRule<
        ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    private val removedBookmarks = mutableSetOf<String>()

    fun setContent(newsFeedUiState: NewsFeedUiState) {
        composeTestRule.setContent {
            BookmarksScreen(feedState = newsFeedUiState, removeFromBookmarks = {
                removedBookmarks.add(it)
            })
        }
    }

    fun loadingIndicatorShown() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.resources.getString(R.string.saved_loading)
        ).assertExists()
    }

    fun clickableNewsResourceExists(newsResource: NewsResource) {
        composeTestRule.onNodeWithText(
            newsResource.title, substring = true
        ).assertExists().assertHasClickAction()
    }

    fun scrollToNewsResource(newsResource: NewsResource) {
        composeTestRule.onNode(hasScrollToNodeAction()).performScrollToNode(
            hasText(
                newsResource.title, substring = true
            )
        )
    }

    fun clickNewsResourceBookmark(newsResource: NewsResource) {
        composeTestRule.onAllNodesWithContentDescription(
            composeTestRule.activity.getString(
                com.google.samples.apps.nowinandroid.core.ui.R.string.unbookmark
            )
        ).filter(
            hasAnyAncestor(
                hasText(
                    newsResource.title, substring = true
                )
            )
        ).assertCountEquals(1).onFirst().performClick()
    }

    fun removedNewsResourceBookmark(newsResource: NewsResource) =
        removedBookmarks.contains(newsResource.id)
}
