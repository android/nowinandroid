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

package com.google.samples.apps.nowinandroid.feature.foryou

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.ui.NewsResourceCardExpanded
import com.google.samples.apps.nowinandroid.core.ui.NiaLoadingIndicator
import kotlinx.datetime.Instant

@Composable
fun ForYouRoute(
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ForYouScreen(
        modifier = modifier,
        uiState = uiState,
        onTopicCheckedChanged = viewModel::updateTopicSelection,
        saveFollowedTopics = viewModel::saveFollowedTopics,
        onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved
    )
}

@Composable
fun ForYouScreen(
    uiState: ForYouFeedUiState,
    onTopicCheckedChanged: (Int, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit,
    onNewsResourcesCheckedChanged: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            ForYouFeedUiState.Loading -> {
                NiaLoadingIndicator(
                    modifier = modifier,
                    contentDesc = stringResource(id = R.string.for_you_loading),
                )
            }
            is ForYouFeedUiState.PopulatedFeed -> {
                LazyColumn {
                    when (uiState) {
                        is ForYouFeedUiState.PopulatedFeed.FeedWithTopicSelection -> {
                            TopicSelection(uiState, onTopicCheckedChanged, saveFollowedTopics)
                        }
                        is ForYouFeedUiState.PopulatedFeed.FeedWithoutTopicSelection -> Unit
                    }

                    items(uiState.feed) { (newsResource: NewsResource, isBookmarked: Boolean) ->
                        val launchResourceIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(newsResource.url))
                        val context = LocalContext.current

                        NewsResourceCardExpanded(
                            newsResource = newsResource,
                            isBookmarked = isBookmarked,
                            onToggleBookmark = {
                                onNewsResourcesCheckedChanged(newsResource.id, !isBookmarked)
                            },
                            onClick = {
                                startActivity(context, launchResourceIntent, null)
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
 * The topic selection items
 */
private fun LazyListScope.TopicSelection(
    uiState: ForYouFeedUiState.PopulatedFeed.FeedWithTopicSelection,
    onTopicCheckedChanged: (Int, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit
) {
    item {
        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            uiState.topics.forEach { (topic, isSelected) ->
                key(topic.id) {
                    // TODO: Add toggleable semantics
                    OutlinedButton(
                        onClick = {
                            onTopicCheckedChanged(topic.id, !isSelected)
                        },
                        shape = RoundedCornerShape(50),
                        colors = if (isSelected) {
                            ButtonDefaults.buttonColors()
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        },
                        modifier = Modifier.toggleable(
                            value = isSelected, role = Role.Button, onValueChange = {}
                        )
                    ) {
                        Text(
                            text = topic.name.uppercase(),
                        )
                    }
                }
            }
        }
    }

    item {
        Button(
            onClick = saveFollowedTopics,
            enabled = uiState.canSaveSelectedTopics,
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.done))
        }
    }
}

@Preview
@Composable
fun ForYouScreenLoading() {
    ForYouScreen(
        uiState = ForYouFeedUiState.Loading,
        onTopicCheckedChanged = { _, _ -> },
        saveFollowedTopics = {},
        onNewsResourcesCheckedChanged = { _, _ -> }
    )
}

@Preview
@Composable
fun ForYouScreenTopicSelection() {
    ForYouScreen(
        uiState = ForYouFeedUiState.PopulatedFeed.FeedWithTopicSelection(
            topics = listOf(
                FollowableTopic(
                    topic = Topic(
                        id = 0,
                        name = "Headlines",
                        shortDescription = "",
                        longDescription = "",
                        url = "",
                        imageUrl = ""
                    ),
                    isFollowed = false
                ),
                FollowableTopic(
                    topic = Topic(
                        id = 1,
                        name = "UI",
                        shortDescription = "",
                        longDescription = "",
                        url = "",
                        imageUrl = ""
                    ),
                    isFollowed = false
                ),
                FollowableTopic(
                    topic = Topic(
                        id = 2,
                        name = "Tools",
                        shortDescription = "",
                        longDescription = "",
                        url = "",
                        imageUrl = ""
                    ),
                    isFollowed = false
                ),
            ),
            feed = listOf(
                SaveableNewsResource(
                    newsResource = NewsResource(
                        id = 1,
                        episodeId = 52,
                        title = "Thanks for helping us reach 1M YouTube Subscribers",
                        content = "Thank you everyone for following the Now in Android series " +
                            "and everything the Android Developers YouTube channel has to offer. " +
                            "During the Android Developer Summit, our YouTube channel reached 1 " +
                            "million subscribers! Here’s a small video to thank you all.",
                        url = "https://youtu.be/-fJ6poHQrjM",
                        headerImageUrl = "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
                        publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
                        type = Video,
                        topics = listOf(
                            Topic(
                                id = 0,
                                name = "Headlines",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            )
                        ),
                        authors = emptyList()
                    ),
                    isSaved = false
                ),
                SaveableNewsResource(
                    newsResource = NewsResource(
                        id = 2,
                        episodeId = 52,
                        title = "Transformations and customisations in the Paging Library",
                        content = "A demonstration of different operations that can be performed " +
                            "with Paging. Transformations like inserting separators, when to " +
                            "create a new pager, and customisation options for consuming " +
                            "PagingData.",
                        url = "https://youtu.be/ZARz0pjm5YM",
                        headerImageUrl = "https://i.ytimg.com/vi/ZARz0pjm5YM/maxresdefault.jpg",
                        publishDate = Instant.parse("2021-11-01T00:00:00.000Z"),
                        type = Video,
                        topics = listOf(
                            Topic(
                                id = 1,
                                name = "UI",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                        ),
                        authors = emptyList()
                    ),
                    isSaved = false
                ),
                SaveableNewsResource(
                    newsResource = NewsResource(
                        id = 3,
                        episodeId = 52,
                        title = "Community tip on Paging",
                        content = "Tips for using the Paging library from the developer community",
                        url = "https://youtu.be/r5JgIyS3t3s",
                        headerImageUrl = "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
                        publishDate = Instant.parse("2021-11-08T00:00:00.000Z"),
                        type = Video,
                        topics = listOf(
                            Topic(
                                id = 1,
                                name = "UI",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                        ),
                        authors = emptyList()
                    ),
                    isSaved = false
                ),
            )
        ),
        onTopicCheckedChanged = { _, _ -> },
        saveFollowedTopics = {},
        onNewsResourcesCheckedChanged = { _, _ -> }
    )
}

@Preview
@Composable
fun PopulatedFeed() {
    ForYouScreen(
        uiState = ForYouFeedUiState.PopulatedFeed.FeedWithoutTopicSelection(
            feed = emptyList()
        ),
        onTopicCheckedChanged = { _, _ -> },
        saveFollowedTopics = {},
        onNewsResourcesCheckedChanged = { _, _ -> }
    )
}
