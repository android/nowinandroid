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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Done
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
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.ui.NiaLoadingIndicator
import com.google.samples.apps.nowinandroid.core.ui.NiaToolbar
import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTheme

@Composable
fun FollowingRoute(
    modifier: Modifier = Modifier,
    navigateToTopic: () -> Unit,
    viewModel: FollowingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FollowingScreen(
        modifier = modifier,
        uiState = uiState,
        followTopic = viewModel::followTopic,
        navigateToTopic = navigateToTopic
    )
}

@Composable
fun FollowingScreen(
    uiState: FollowingUiState,
    followTopic: (Int, Boolean) -> Unit,
    navigateToTopic: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NiaToolbar(titleRes = R.string.following)
        when (uiState) {
            FollowingUiState.Loading ->
                NiaLoadingIndicator(
                    modifier = modifier,
                    contentDesc = stringResource(id = R.string.following_loading),
                )
            is FollowingUiState.Topics ->
                FollowingWithTopicsScreen(
                    uiState = uiState,
                    onTopicClick = { navigateToTopic() },
                    onFollowButtonClick = followTopic,
                )
            is FollowingUiState.Error -> FollowingErrorScreen()
        }
    }
}

@Composable
fun FollowingWithTopicsScreen(
    modifier: Modifier = Modifier,
    uiState: FollowingUiState.Topics,
    onTopicClick: () -> Unit,
    onFollowButtonClick: (Int, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        uiState.topics.forEach { followableTopic ->
            item {
                FollowingTopicCard(
                    followableTopic = followableTopic,
                    onTopicClick = onTopicClick,
                    onFollowButtonClick = onFollowButtonClick
                )
            }
        }
    }
}

@Composable
fun FollowingErrorScreen() {
    Text(text = stringResource(id = R.string.following_error_header))
}

@Composable
fun FollowingTopicCard(
    followableTopic: FollowableTopic,
    onTopicClick: () -> Unit,
    onFollowButtonClick: (Int, Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier =
        Modifier.padding(
            start = 24.dp,
            end = 8.dp,
            bottom = 24.dp
        )
    ) {
        TopicIcon(
            modifier = Modifier.padding(end = 24.dp),
            topicImageUrl = followableTopic.topic.imageUrl,
            onClick = onTopicClick
        )
        Column(
            Modifier
                .wrapContentSize(Alignment.CenterStart)
                .weight(1f)
                .clickable { onTopicClick() }
        ) {
            TopicTitle(topicName = followableTopic.topic.name)
            TopicDescription(topicDescription = followableTopic.topic.shortDescription)
        }
        FollowButton(
            topicId = followableTopic.topic.id,
            onClick = onFollowButtonClick,
            isFollowed = followableTopic.isFollowed
        )
    }
}

@Composable
fun TopicTitle(
    topicName: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = topicName,
        style = MaterialTheme.typography.h5,
        modifier = modifier.padding(top = 12.dp, bottom = 8.dp)
    )
}

@Composable
fun TopicDescription(topicDescription: String) {
    Text(
        text = topicDescription,
        style = MaterialTheme.typography.body2,
        modifier = Modifier.wrapContentSize(Alignment.CenterStart)
    )
}

@Composable
fun TopicIcon(
    modifier: Modifier = Modifier,
    topicImageUrl: String,
    onClick: () -> Unit
) {

    val iconModifier = modifier.size(64.dp)
        .clickable { onClick() }
    val contentDescription = stringResource(id = R.string.following_topic_card_icon_content_desc)

    if (topicImageUrl.isEmpty()) {
        Icon(
            imageVector = Icons.Filled.Android,
            tint = Color.Magenta,
            contentDescription = contentDescription,
            modifier = iconModifier
        )
    } else {
        AsyncImage(
            model = topicImageUrl,
            contentDescription = contentDescription,
            modifier = iconModifier
        )
    }
}

@Composable
fun FollowButton(
    topicId: Int,
    isFollowed: Boolean,
    onClick: (Int, Boolean) -> Unit,
) {
    IconToggleButton(
        checked = isFollowed,
        onCheckedChange = { onClick(topicId, !isFollowed) }
    ) {
        if (isFollowed) {
            FollowedTopicIcon()
        } else {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription =
                stringResource(id = R.string.following_topic_card_follow_button_content_desc),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun FollowedTopicIcon() {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(
                color = Color.Magenta.copy(alpha = 0.5f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription =
            stringResource(id = R.string.following_topic_card_unfollow_button_content_desc),
            modifier = Modifier
                .size(14.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview("Topic card")
@Composable
fun TopicCardPreview() {
    NiaTheme {
        Surface {
            FollowingTopicCard(
                FollowableTopic(
                    Topic(
                        id = 0,
                        name = "Compose",
                        shortDescription = "Short description",
                        longDescription = "Long description",
                        url = "URL",
                        imageUrl = "imageUrl"
                    ),
                    isFollowed = false
                ),
                onTopicClick = {},
                onFollowButtonClick = { _, _ -> }
            )
        }
    }
}
