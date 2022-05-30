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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.previewAuthors
import com.google.samples.apps.nowinandroid.core.model.data.previewTopics
import com.google.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTab
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTabRow
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar
import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTheme

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun InterestsScreenPopulated() {
    NiaTheme {
        NiaBackground {
            Scaffold(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ) { padding ->
                InterestsScreen(
                    uiState = InterestsUiState.Interests(
                        authors = previewAuthors.map { FollowableAuthor(it, false) },
                        topics = previewTopics.map { FollowableTopic(it, false) }
                    ),
                    tabState = InterestsTabState(
                        titles = listOf(R.string.interests_topics, R.string.interests_people),
                        currentIndex = 0
                    ),
                    followAuthor = { _, _ -> },
                    followTopic = { _, _ -> },
                    navigateToAuthor = {},
                    navigateToTopic = {},
                    switchTab = {},
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun InterestsScreenLoading() {
    NiaTheme {
        NiaBackground {
            Scaffold(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ) { padding ->
                InterestsScreen(
                    uiState = InterestsUiState.Loading,
                    tabState = InterestsTabState(
                        titles = listOf(R.string.interests_topics, R.string.interests_people),
                        currentIndex = 0
                    ),
                    followAuthor = { _, _ -> },
                    followTopic = { _, _ -> },
                    navigateToAuthor = {},
                    navigateToTopic = {},
                    switchTab = {},
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun InterestsScreenEmpty() {
    NiaTheme {
        NiaBackground {
            Scaffold(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ) { padding ->
                InterestsScreen(
                    uiState = InterestsUiState.Empty,
                    tabState = InterestsTabState(
                        titles = listOf(R.string.interests_topics, R.string.interests_people),
                        currentIndex = 0
                    ),
                    followAuthor = { _, _ -> },
                    followTopic = { _, _ -> },
                    navigateToAuthor = {},
                    navigateToTopic = {},
                    switchTab = {},
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}
