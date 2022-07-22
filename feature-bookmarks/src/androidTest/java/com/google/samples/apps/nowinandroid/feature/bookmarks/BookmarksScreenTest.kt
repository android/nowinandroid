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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import androidx.compose.ui.unit.DpSize
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for [BookmarksScreen] composable.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class BookmarksScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_showsLoadingSpinner() {
        lateinit var windowSizeClass: WindowSizeClass
        composeTestRule.setContent {
            BoxWithConstraints {
                windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(maxWidth, maxHeight)
                )
                BookmarksScreen(
                    windowSizeClass = windowSizeClass,
                    feedState = NewsFeedUiState.Loading,
                    removeFromBookmarks = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.saved_loading)
            )
            .assertExists()
    }

    @Test
    fun feed_whenHasBookmarks_showsBookmarks() {
        lateinit var windowSizeClass: WindowSizeClass

        composeTestRule.setContent {
            BoxWithConstraints {
                windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(maxWidth, maxHeight)
                )

                BookmarksScreen(
                    windowSizeClass = windowSizeClass,
                    feedState = NewsFeedUiState.Success(
                        previewNewsResources.take(2)
                            .map { SaveableNewsResource(it, true) }
                    ),
                    removeFromBookmarks = { }
                )
            }
        }

        composeTestRule
            .onNodeWithText(
                previewNewsResources[0].title,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(
                hasText(
                    previewNewsResources[1].title,
                    substring = true
                )
            )

        composeTestRule
            .onNodeWithText(
                previewNewsResources[1].title,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun feed_whenRemovingBookmark_removesBookmark() {
        lateinit var windowSizeClass: WindowSizeClass

        var removeFromBookmarksCalled = false

        composeTestRule.setContent {
            BoxWithConstraints {
                windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(maxWidth, maxHeight)
                )

                BookmarksScreen(
                    windowSizeClass = windowSizeClass,
                    feedState = NewsFeedUiState.Success(
                        previewNewsResources.take(2)
                            .map { SaveableNewsResource(it, true) }
                    ),
                    removeFromBookmarks = { newsResourceId ->
                        assertEquals(previewNewsResources[0].id, newsResourceId)
                        removeFromBookmarksCalled = true
                    }
                )
            }
        }

        composeTestRule
            .onAllNodesWithContentDescription(
                composeTestRule.activity.getString(
                    com.google.samples.apps.nowinandroid.core.ui.R.string.unbookmark
                )
            ).filter(
                hasAnyAncestor(
                    hasText(
                        previewNewsResources[0].title,
                        substring = true
                    )
                )
            )
            .assertCountEquals(1)
            .onFirst()
            .performClick()

        assertTrue(removeFromBookmarksCalled)
    }
}
