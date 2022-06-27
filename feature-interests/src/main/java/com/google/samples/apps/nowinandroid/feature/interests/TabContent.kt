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
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic

@Composable
fun TopicsTabContent(
    topics: List<FollowableTopic>,
    onTopicClick: (String) -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .testTag("interests:topics"),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        topics.forEach { followableTopic ->
            item {
                InterestsItem(
                    name = followableTopic.topic.name,
                    following = followableTopic.isFollowed,
                    description = followableTopic.topic.shortDescription,
                    topicImageUrl = followableTopic.topic.imageUrl,
                    onClick = { onTopicClick(followableTopic.topic.id) },
                    onFollowButtonClick = { onFollowButtonClick(followableTopic.topic.id, it) }
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

@Composable
fun AuthorsTabContent(
    authors: List<FollowableAuthor>,
    onAuthorClick: (String) -> Unit,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .testTag("interests:people"),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        authors.forEach { followableAuthor ->
            item {
                InterestsItem(
                    name = followableAuthor.author.name,
                    following = followableAuthor.isFollowed,
                    topicImageUrl = followableAuthor.author.imageUrl,
                    onClick = { onAuthorClick(followableAuthor.author.id) },
                    onFollowButtonClick = { onFollowButtonClick(followableAuthor.author.id, it) },
                    iconModifier = Modifier.clip(CircleShape)
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
