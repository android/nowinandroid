/*
 * Copyright 2026 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.feature.search

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultRoborazziOptions
import com.google.samples.apps.nowinandroid.core.ui.FollowableTopicPreviewParameterProvider
import com.google.samples.apps.nowinandroid.core.ui.UserNewsResourcePreviewParameterProvider
import org.junit.Test

/**
 * JVM Desktop screenshot tests for the [SearchScreen].
 */
class SearchScreenDesktopScreenshotTests {

    private val userNewsResources = UserNewsResourcePreviewParameterProvider().values.first()
    private val topics = FollowableTopicPreviewParameterProvider().values.first()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchScreen_compact() = runDesktopComposeUiTest(width = 400, height = 800) {
        setContent {
            NiaTheme {
                SearchScreen(
                    searchQuery = "android",
                    searchResultUiState = SearchResultUiState.Success(
                        topics = topics,
                        newsResources = userNewsResources,
                    ),
                    recentSearchesUiState = RecentSearchQueriesUiState.Success(),
                )
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/jvmTest/screenshots/SearchScreen_compact.png",
            roborazziOptions = DefaultRoborazziOptions,
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchScreen_medium() = runDesktopComposeUiTest(width = 700, height = 900) {
        setContent {
            NiaTheme {
                SearchScreen(
                    searchQuery = "android",
                    searchResultUiState = SearchResultUiState.Success(
                        topics = topics,
                        newsResources = userNewsResources,
                    ),
                    recentSearchesUiState = RecentSearchQueriesUiState.Success(),
                )
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/jvmTest/screenshots/SearchScreen_medium.png",
            roborazziOptions = DefaultRoborazziOptions,
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchScreen_expanded() = runDesktopComposeUiTest(width = 1200, height = 800) {
        setContent {
            NiaTheme {
                SearchScreen(
                    searchQuery = "android",
                    searchResultUiState = SearchResultUiState.Success(
                        topics = topics,
                        newsResources = userNewsResources,
                    ),
                    recentSearchesUiState = RecentSearchQueriesUiState.Success(),
                )
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/jvmTest/screenshots/SearchScreen_expanded.png",
            roborazziOptions = DefaultRoborazziOptions,
        )
    }
}
