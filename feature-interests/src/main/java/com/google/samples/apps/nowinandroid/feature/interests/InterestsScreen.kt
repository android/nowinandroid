/*
 * Copyright 2021 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.feature.interests

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.component.NiaGradientBackground
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTab
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTabRow
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar

@Composable
fun InterestsRoute(
    modifier: Modifier = Modifier,
    navigateToAuthor: (String) -> Unit,
    navigateToTopic: (String) -> Unit,
    viewModel: InterestsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabState by viewModel.tabState.collectAsState()

    InterestsScreen(
        uiState = uiState,
        tabState = tabState,
        followTopic = viewModel::followTopic,
        followAuthor = viewModel::followAuthor,
        navigateToAuthor = navigateToAuthor,
        navigateToTopic = navigateToTopic,
        switchTab = viewModel::switchTab,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun InterestsScreen(
    uiState: InterestsUiState,
    tabState: InterestsTabState,
    followAuthor: (String, Boolean) -> Unit,
    followTopic: (String, Boolean) -> Unit,
    navigateToAuthor: (String) -> Unit,
    navigateToTopic: (String) -> Unit,
    switchTab: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NiaGradientBackground {
        Scaffold(
            topBar = {
                NiaTopAppBar(
                    titleRes = R.string.interests,
                    navigationIcon = Icons.Filled.Search,
                    navigationIconContentDescription = stringResource(
                        id = R.string.top_app_bar_navigation_button_content_desc
                    ),
                    actionIcon = Icons.Filled.MoreVert,
                    actionIconContentDescription = stringResource(
                        id = R.string.top_app_bar_navigation_button_content_desc
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
                when (uiState) {
                    InterestsUiState.Loading ->
                        LoadingWheel(
                            modifier = modifier,
                            contentDesc = stringResource(id = R.string.interests_loading),
                        )
                    is InterestsUiState.Interests ->
                        InterestsContent(
                            tabState = tabState,
                            switchTab = switchTab,
                            uiState = uiState,
                            navigateToTopic = navigateToTopic,
                            followTopic = followTopic,
                            navigateToAuthor = navigateToAuthor,
                            followAuthor = followAuthor
                        )
                    is InterestsUiState.Empty -> InterestsEmptyScreen()
                }
            }
        }
    }
}

@Composable
private fun InterestsContent(
    tabState: InterestsTabState,
    switchTab: (Int) -> Unit,
    uiState: InterestsUiState.Interests,
    navigateToTopic: (String) -> Unit,
    followTopic: (String, Boolean) -> Unit,
    navigateToAuthor: (String) -> Unit,
    followAuthor: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        NiaTabRow(selectedTabIndex = tabState.currentIndex) {
            tabState.titles.forEachIndexed { index, titleId ->
                NiaTab(
                    selected = index == tabState.currentIndex,
                    onClick = { switchTab(index) },
                    text = { Text(text = stringResource(id = titleId)) }
                )
            }
        }
        when (tabState.currentIndex) {
            0 -> {
                TopicsTabContent(
                    topics = uiState.topics,
                    onTopicClick = navigateToTopic,
                    onFollowButtonClick = followTopic,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            1 -> {
                AuthorsTabContent(
                    authors = uiState.authors,
                    onAuthorClick = navigateToAuthor,
                    onFollowButtonClick = followAuthor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun InterestsEmptyScreen() {
    Text(text = stringResource(id = R.string.interests_empty_header))
}
