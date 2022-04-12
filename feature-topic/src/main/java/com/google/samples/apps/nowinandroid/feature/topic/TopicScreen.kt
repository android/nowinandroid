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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.component.NiaFilterChip
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

@VisibleForTesting
@Composable
internal fun TopicScreen(
    topicState: TopicUiState,
    newsState: NewsUiState,
    onBackClick: () -> Unit,
    onFollowClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (topicState) {
            Loading ->
                LoadingWheel(
                    modifier = modifier,
                    contentDesc = stringResource(id = string.topic_loading),
                )
            TopicUiState.Error -> TODO()
            is TopicUiState.Success -> {
                TopicToolbar(
                    onBackClick = onBackClick,
                    onFollowClick = onFollowClick,
                    uiState = topicState.followableTopic
                )
                TopicBody(
                    name = topicState.followableTopic.topic.name,
                    description = topicState.followableTopic.topic.longDescription,
                    news = newsState
                )
            }
        }
    }
}

@Composable
private fun TopicBody(name: String, description: String, news: NewsUiState) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        // TODO: Show icon if available
        Box(
            modifier = Modifier
                .size(216.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Black, Color.White)
                    )
                )
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
        TopicList(news, Modifier.padding(top = 24.dp))
    }
}

@Composable
private fun TopicList(news: NewsUiState, modifier: Modifier = Modifier) {
    when (news) {
        is NewsUiState.Success -> {
            LazyColumn(modifier = modifier) {
                items(news.news.size) { index ->
                    Text(news.news[index].title)
                }
            }
        }
        is NewsUiState.Loading -> {
            LoadingWheel(contentDesc = "Loading news") // TODO
        }
        else -> {
            Text("Error") // TODO
        }
    }
}

@Preview
@Composable
private fun TopicBodyPreview() {
    MaterialTheme {
        TopicBody("Jetpack Compose", "Lorem ipsum maximum", NewsUiState.Success(emptyList()))
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
