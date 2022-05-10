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

package com.google.samples.apps.nowinandroid.feature.topic

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.component.NiaFilterChip
import com.google.samples.apps.nowinandroid.core.ui.newsResourceCardItems
import com.google.samples.apps.nowinandroid.feature.topic.R.string
import com.google.samples.apps.nowinandroid.feature.topic.TopicUiState.Loading

@Composable
fun TopicRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicViewModel = hiltViewModel(),
) {
    val uiState: TopicScreenUiState by viewModel.uiState.collectAsState()

    TopicScreen(
        topicState = uiState.topicState,
        newsState = uiState.newsState,
        modifier = modifier,
        onBackClick = onBackClick,
        onFollowClick = viewModel::followTopicToggle,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@VisibleForTesting
@Composable
internal fun TopicScreen(
    topicState: TopicUiState,
    newsState: NewsUiState,
    onBackClick: () -> Unit,
    onFollowClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(
                // TODO: Replace with windowInsetsTopHeight after
                //       https://issuetracker.google.com/issues/230383055
                Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                )
            )
        }
        when (topicState) {
            Loading -> item {
                LoadingWheel(
                    modifier = modifier,
                    contentDesc = stringResource(id = string.topic_loading),
                )
            }
            TopicUiState.Error -> TODO()
            is TopicUiState.Success -> {
                item {
                    TopicToolbar(
                        onBackClick = onBackClick,
                        onFollowClick = onFollowClick,
                        uiState = topicState.followableTopic,
                    )
                }
                TopicBody(
                    name = topicState.followableTopic.topic.name,
                    description = topicState.followableTopic.topic.longDescription,
                    news = newsState,
                    imageUrl = topicState.followableTopic.topic.imageUrl
                )
            }
        }
        item {
            Spacer(
                // TODO: Replace with windowInsetsBottomHeight after
                //       https://issuetracker.google.com/issues/230383055
                Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                )
            )
        }
    }
}

private fun LazyListScope.TopicBody(
    name: String,
    description: String,
    news: NewsUiState,
    imageUrl: String
) {
    // TODO: Show icon if available
    item {
        TopicHeader(name, description, imageUrl)
    }

    TopicCards(news)
}

@Composable
private fun TopicHeader(name: String, description: String, imageUrl: String) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .size(216.dp)
                .padding(bottom = 12.dp)
        )
        Text(name, style = MaterialTheme.typography.displayMedium)
        if (description.isNotEmpty()) {
            Text(
                description,
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun LazyListScope.TopicCards(news: NewsUiState) {
    when (news) {
        is NewsUiState.Success -> {
            newsResourceCardItems(
                items = news.news,
                newsResourceMapper = { it },
                isBookmarkedMapper = { /* TODO */ false },
                onToggleBookmark = { /* TODO */ },
                itemModifier = Modifier.padding(24.dp)
            )
        }
        is NewsUiState.Loading -> item {
            LoadingWheel(contentDesc = "Loading news") // TODO
        }
        else -> item {
            Text("Error") // TODO
        }
    }
}

@Preview
@Composable
private fun TopicBodyPreview() {
    MaterialTheme {
        LazyColumn {
            TopicBody(
                "Jetpack Compose", "Lorem ipsum maximum",
                NewsUiState.Success(emptyList()), ""
            )
        }
    }
}

@Composable
private fun TopicToolbar(
    uiState: FollowableTopic,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFollowClick: (Boolean) -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }
        val selected = uiState.isFollowed
        NiaFilterChip(
            checked = selected,
            onCheckedChange = onFollowClick,
        ) {
            if (selected) {
                Text("FOLLOWING")
            } else {
                Text("NOT FOLLOWING")
            }
        }
    }
}
