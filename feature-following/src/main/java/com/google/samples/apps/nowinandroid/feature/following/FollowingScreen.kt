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

package com.google.samples.apps.nowinandroid.feature.following

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.ui.NiaLoadingIndicator
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTab
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTabRow
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar

@Composable
fun InterestsRoute(
    modifier: Modifier = Modifier,
    navigateToAuthor: () -> Unit,
    navigateToTopic: (Int) -> Unit,
    viewModel: FollowingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabState by viewModel.tabState.collectAsState()

    FollowingScreen(
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

@Composable
fun FollowingScreen(
    uiState: FollowingUiState,
    tabState: FollowingTabState,
    followAuthor: (Int, Boolean) -> Unit,
    followTopic: (Int, Boolean) -> Unit,
    navigateToAuthor: () -> Unit,
    navigateToTopic: (Int) -> Unit,
    switchTab: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NiaTopAppBar(
            titleRes = R.string.interests,
            navigationIcon = Icons.Filled.Search,
            navigationIconContentDescription = stringResource(
                id = R.string.top_app_bar_navigation_button_content_desc
            ),
            actionIcon = Icons.Filled.MoreVert,
            actionIconContentDescription = stringResource(
                id = R.string.top_app_bar_navigation_button_content_desc
            )
        )
        when (uiState) {
            FollowingUiState.Loading ->
                NiaLoadingIndicator(
                    modifier = modifier,
                    contentDesc = stringResource(id = R.string.following_loading),
                )
            is FollowingUiState.Interests ->
                FollowingContent(
                    tabState, switchTab, uiState, navigateToTopic, followTopic,
                    navigateToAuthor, followAuthor
                )
            is FollowingUiState.Empty -> InterestsEmptyScreen()
        }
    }
}

@Composable
private fun FollowingContent(
    tabState: FollowingTabState,
    switchTab: (Int) -> Unit,
    uiState: FollowingUiState.Interests,
    navigateToTopic: (Int) -> Unit,
    followTopic: (Int, Boolean) -> Unit,
    navigateToAuthor: () -> Unit,
    followAuthor: (Int, Boolean) -> Unit,
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
                    onAuthorClick = { navigateToAuthor() },
                    onFollowButtonClick = followAuthor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun InterestsEmptyScreen() {
    Text(text = stringResource(id = R.string.following_empty_header))
}
