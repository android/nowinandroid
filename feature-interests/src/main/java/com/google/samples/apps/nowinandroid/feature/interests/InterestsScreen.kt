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

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.component.*
import kotlinx.coroutines.launch

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
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            // TODO: Replace with windowInsetsTopHeight after
            //       https://issuetracker.google.com/issues/230383055
            Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
            )
        )

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


@OptIn(ExperimentalPagerApi::class)
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
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()
        NiaTabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            tabState.titles.forEachIndexed { index, titleId ->
                NiaTab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        switchTab(index)
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                    text = { Text(text = stringResource(id = titleId)) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            count = tabState.titles.size,
            verticalAlignment = Alignment.Top,
            contentPadding = PaddingValues(top = 12.dp),
        ) { index ->
            when (index) {
                0 -> TopicsTabContent(
                    topics = uiState.topics,
                    onTopicClick = navigateToTopic,
                    onFollowButtonClick = followTopic,
                    modifier = Modifier.padding(top = 8.dp)
                )
                1 -> AuthorsTabContent(
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
