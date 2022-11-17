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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaLoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank
import com.google.samples.apps.nowinandroid.core.ui.newsFeed

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun BookmarksRoute(
    modifier: Modifier = Modifier,
    navigateToTopic: (String) -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedUiState.collectAsStateWithLifecycle()
    BookmarksScreen(
        feedState = feedState,
        removeFromBookmarks = viewModel::removeFromSavedResources,
        modifier = modifier,
        onBrowseTopic = navigateToTopic,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookmarksScreen(
    feedState: NewsFeedUiState,
    removeFromBookmarks: (String) -> Unit,
    onBrowseTopic: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollableState = rememberLazyGridState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "bookmarks:grid")
    LazyVerticalGrid(
        columns = Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        state = scrollableState,
        modifier = modifier
            .fillMaxSize()
            .testTag("bookmarks:feed")
    ) {
        if (feedState is NewsFeedUiState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                NiaLoadingWheel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .testTag("forYou:loading"),
                    contentDesc = stringResource(id = R.string.saved_loading),
                )
            }
        }

        newsFeed(
            feedState = feedState,
            onNewsResourcesCheckedChanged = { id, _ -> removeFromBookmarks(id) },
            onBrowseTopic = onBrowseTopic,
        )

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}
