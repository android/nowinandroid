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

package com.google.samples.apps.nowinandroid.feature.interests

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.DraggableScrollbar
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.rememberDraggableScroller
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.scrollbarState
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TopicsTabContent(
    topics: List<FollowableTopic>,
    selectedTopicId: String?,
    onTopicClick: (String) -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    detailsPane: @Composable (String) -> Unit,
) {
    val listDetailPaneState = rememberListDetailPaneScaffoldState()

    BackHandler(enabled = listDetailPaneState.canNavigateBack()) {
        listDetailPaneState.navigateBack()
    }

    LaunchedEffect(selectedTopicId) {
        if (selectedTopicId != null) {
            listDetailPaneState.navigateTo(ListDetailPaneScaffoldRole.Detail)
        }
    }

    ListDetailPaneScaffold(
        scaffoldState = listDetailPaneState,
        listPane = {
            ListPane(
                topics = topics,
                onTopicClick = onTopicClick,
                onFollowButtonClick = onFollowButtonClick,
            )
        },
        detailPane = {
            if (selectedTopicId != null) {
                detailsPane(selectedTopicId)
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun ListPane(
    topics: List<FollowableTopic>,
    onTopicClick: (String) -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        val scrollableState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.testTag("interests:topics"),
            state = scrollableState,
        ) {
            items(
                items = topics,
                key = { followableTopic -> followableTopic.topic.id },
            ) { followableTopic ->
                val topicId = followableTopic.topic.id
                InterestsItem(
                    name = followableTopic.topic.name,
                    following = followableTopic.isFollowed,
                    description = followableTopic.topic.shortDescription,
                    topicImageUrl = followableTopic.topic.imageUrl,
                    onClick = { onTopicClick(topicId) },
                    onFollowButtonClick = { onFollowButtonClick(topicId, it) },
                )
            }

            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
        val scrollbarState = scrollableState.scrollbarState(
            itemsAvailable = topics.size,
        )
        scrollableState.DraggableScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 2.dp)
                .align(Alignment.CenterEnd),
            state = scrollbarState,
            orientation = Orientation.Vertical,
            onThumbMoved = scrollableState.rememberDraggableScroller(
                itemsAvailable = topics.size,
            ),
        )
    }
}
