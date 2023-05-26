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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic

@Composable
fun TopicsTabContent(
    selectedTopicId: String?,
    topics: List<FollowableTopic>,
    onTopicClick: (String) -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyGridState = rememberLazyGridState(),
    withBottomSpacer: Boolean = true,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        state = listState,
        modifier = modifier
            .selectableGroup()
            .testTag("interests:topics"),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(
            items = topics,
            key = { item -> item.topic.id },
        ) { followableTopic ->
            val topicId = followableTopic.topic.id
            InterestsItem(
                isSelected = selectedTopicId == topicId,
                name = followableTopic.topic.name,
                following = followableTopic.isFollowed,
                description = followableTopic.topic.shortDescription,
                topicImageUrl = followableTopic.topic.imageUrl,
                onClick = { onTopicClick(topicId) },
                onFollowButtonClick = { onFollowButtonClick(topicId, it) },
            )
        }

        if (withBottomSpacer) {
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}
