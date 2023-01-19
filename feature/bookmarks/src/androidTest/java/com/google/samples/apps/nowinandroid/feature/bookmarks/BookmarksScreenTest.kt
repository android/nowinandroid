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
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.google.samples.apps.nowinandroid.core.domain.model.previewUserNewsResources
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * UI tests for [BookmarksScreen] composable.
 */
class BookmarksScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_showsLoadingSpinner() {
        composeTestRule.setContent {
            BookmarksScreen(
                feedState = NewsFeedUiState.Loading,
                removeFromBookmarks = { },
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.saved_loading),
            )
            .assertExists()
    }

    @Test
    fun feed_whenHasBookmarks_showsBookmarks() {
        composeTestRule.setContent {
            BookmarksScreen(
                feedState = NewsFeedUiState.Success(
                    previewUserNewsResources.take(2),
                ),
                removeFromBookmarks = { },
            )
        }

        composeTestRule
            .onNodeWithText(
                previewUserNewsResources[0].title,
                substring = true,
            )
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(
                hasText(
                    previewUserNewsResources[1].title,
                    substring = true,
                ),
            )

        composeTestRule
            .onNodeWithText(
                previewUserNewsResources[1].title,
                substring = true,
            )
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun feed_whenRemovingBookmark_removesBookmark() {
        var removeFromBookmarksCalled = false

        composeTestRule.setContent {
            BookmarksScreen(
                feedState = NewsFeedUiState.Success(
                    previewUserNewsResources.take(2),
                ),
                removeFromBookmarks = { newsResourceId ->
                    assertEquals(previewUserNewsResources[0].id, newsResourceId)
                    removeFromBookmarksCalled = true
                },
            )
        }

        composeTestRule
            .onAllNodesWithContentDescription(
                composeTestRule.activity.getString(
                    com.google.samples.apps.nowinandroid.core.ui.R.string.unbookmark,
                ),
            ).filter(
                hasAnyAncestor(
                    hasText(
                        previewUserNewsResources[0].title,
                        substring = true,
                    ),
                ),
            )
            .assertCountEquals(1)
            .onFirst()
            .performClick()

        assertTrue(removeFromBookmarksCalled)
    }

    @Test
    fun feed_whenHasNoBookmarks_showsEmptyState() {
        composeTestRule.setContent {
            BookmarksScreen(
                feedState = NewsFeedUiState.Success(emptyList()),
                removeFromBookmarks = { },
            )
        }

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.bookmarks_empty_error),
            )
            .assertExists()

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.bookmarks_empty_description),
            )
            .assertExists()
    }
}
