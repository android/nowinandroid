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

package com.google.samples.apps.nowinandroid.feature.interests.impl

import android.content.Intent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.DraggableScrollbar
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.rememberDraggableScroller
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.scrollbarState
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.ui.InterestsItem
import com.google.samples.apps.nowinandroid.feature.interests.impl.filter.FilterActivity
import com.google.samples.apps.nowinandroid.feature.interests.impl.filter.IO_CONNECT_WORKSHOP_TOPIC_NAME

@Composable
fun TopicsTabContent(
    topics: List<FollowableTopic>,
    onTopicClick: (String) -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    withBottomSpacer: Boolean = true,
    selectedTopicId: String? = null,
    shouldHighlightSelectedTopic: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val scrollableState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .testTag(LIST_PANE_TEST_TAG),
            contentPadding = PaddingValues(vertical = 16.dp),
            state = scrollableState,
        ) {

            item(key = IO_CONNECT_WORKSHOP_TOPIC_NAME) {
                val context = LocalContext.current
                InterestsItem(
                    name = IO_CONNECT_WORKSHOP_TOPIC_NAME,
                    following = false,
                    description = "",
                    topicImageUrl = "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Performance.svg?alt=media&token=558fdf02-1918-4527-b13f-323db67e31cc",
                    onClick = {
                        context.startActivity(Intent(context, FilterActivity::class.java))
                    },
                    onFollowButtonClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            topics.forEach { followableTopic ->
                val topicId = followableTopic.topic.id
                item(key = topicId) {
                    val isSelected = shouldHighlightSelectedTopic && topicId == selectedTopicId
                    InterestsItem(
                        name = followableTopic.topic.name,
                        following = followableTopic.isFollowed,
                        description = followableTopic.topic.shortDescription,
                        topicImageUrl = followableTopic.topic.imageUrl,
                        onClick = { onTopicClick(topicId) },
                        onFollowButtonClick = { onFollowButtonClick(topicId, it) },
                        isSelected = isSelected,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            if (withBottomSpacer) {
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }

        val itemsAvailable = topics.size + 1
        val scrollbarState = scrollableState.scrollbarState(
            itemsAvailable = itemsAvailable,
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
                itemsAvailable = itemsAvailable,
            ),
        )
    }
}

val LIST_PANE_TEST_TAG = "interests:topics"
