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

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaGradientBackground
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTopAppBar
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.ui.NewsFeed
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import kotlin.math.floor

@Composable
fun BookmarksRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState.collectAsState()
    BookmarksScreen(
        windowSizeClass = windowSizeClass,
        feedState = feedState,
        removeFromBookmarks = viewModel::removeFromSavedResources,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookmarksScreen(
    windowSizeClass: WindowSizeClass,
    feedState: NewsFeedUiState,
    removeFromBookmarks: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NiaGradientBackground {
        Scaffold(
            topBar = {
                NiaTopAppBar(
                    titleRes = R.string.top_app_bar_title_saved,
                    navigationIcon = NiaIcons.Search,
                    navigationIconContentDescription = stringResource(
                        id = R.string.top_app_bar_action_search
                    ),
                    actionIcon = NiaIcons.AccountCircle,
                    actionIconContentDescription = stringResource(
                        id = R.string.top_app_bar_action_menu
                    ),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            // TODO: Replace with `LazyVerticalGrid` when blocking bugs are fixed:
            //       https://issuetracker.google.com/issues/230514914
            //       https://issuetracker.google.com/issues/231320714
            BoxWithConstraints(
                modifier = modifier
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
            ) {
                val numberOfColumns = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> 1
                    else -> floor(maxWidth / 300.dp).toInt().coerceAtLeast(1)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("saved:feed"),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    NewsFeed(
                        feedState = feedState,
                        numberOfColumns = numberOfColumns,
                        onNewsResourcesCheckedChanged = { id, _ -> removeFromBookmarks(id) },
                        showLoadingUIIfLoading = true,
                        loadingContentDescription = R.string.saved_loading
                    )

                    item {
                        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                    }
                }
            }
        }
    }
}
