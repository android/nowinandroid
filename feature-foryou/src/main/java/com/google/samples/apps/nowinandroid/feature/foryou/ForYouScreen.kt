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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.ui.NewsResourceCardExpanded
import com.google.samples.apps.nowinandroid.core.ui.NiaLoadingIndicator
import com.google.samples.apps.nowinandroid.core.ui.component.NiaToggleButton
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar
import com.google.samples.apps.nowinandroid.core.ui.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTypography
import com.google.samples.apps.nowinandroid.feature.foryou.ForYouFeedUiState.PopulatedFeed
import com.google.samples.apps.nowinandroid.feature.foryou.ForYouFeedUiState.PopulatedFeed.FeedWithInterestsSelection
import com.google.samples.apps.nowinandroid.feature.foryou.ForYouFeedUiState.PopulatedFeed.FeedWithoutTopicSelection
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
        onAuthorCheckedChanged = viewModel::updateAuthorSelection,
        saveFollowedTopics = viewModel::saveFollowedInterests,
        onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved
    )
}

@Composable
fun ForYouScreen(
    uiState: ForYouFeedUiState,
    onTopicCheckedChanged: (Int, Boolean) -> Unit,
    onAuthorCheckedChanged: (Int, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit,
    onNewsResourcesCheckedChanged: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            NiaTopAppBar(
                titleRes = R.string.top_app_bar_title,
                navigationIcon = Icons.Filled.Search,
                navigationIconContentDescription = stringResource(
                    id = R.string.top_app_bar_navigation_button_content_desc
                ),
                actionIcon = Icons.Outlined.AccountCircle,
                actionIconContentDescription = stringResource(
                    id = R.string.top_app_bar_navigation_button_content_desc
                )
            )
        }
        when (uiState) {
            is ForYouFeedUiState.Loading -> {
                item {
                    NiaLoadingIndicator(
                        modifier = modifier,
                        contentDesc = stringResource(id = R.string.for_you_loading),
                    )
                }
            }
            is PopulatedFeed -> {
                when (uiState) {
                    is FeedWithInterestsSelection -> {
                        item {
                            Text(
                                text = stringResource(R.string.onboarding_guidance_title),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                style = NiaTypography.titleMedium
                            )
                        }
                        item {
                            Text(
                                text = stringResource(R.string.onboarding_guidance_subtitle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                                textAlign = TextAlign.Center,
                                style = NiaTypography.bodyMedium
                            )
                        }
                        item {
                            AuthorsCarousel(
                                authors = uiState.authors,
                                onAuthorClick = onAuthorCheckedChanged,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        item {
                            TopicSelection(
                                uiState,
                                onTopicCheckedChanged,
                                Modifier.padding(bottom = 8.dp)
                            )
                        }
                        item {
                            // Done button
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = saveFollowedTopics,
                                    enabled = uiState.canSaveInterests,
                                    modifier = Modifier
                                        .padding(horizontal = 40.dp)
                                        .width(364.dp)
                                ) {
                                    Text(text = stringResource(R.string.done))
                                }
                            }
                        }
                    }
                    is FeedWithoutTopicSelection -> Unit
                }

                items(uiState.feed) { (newsResource: NewsResource, isBookmarked: Boolean) ->
                    val launchResourceIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(newsResource.url))
                    val context = LocalContext.current

                    NewsResourceCardExpanded(
                        newsResource = newsResource,
                        isBookmarked = isBookmarked,
                        onClick = { startActivity(context, launchResourceIntent, null) },
                        onToggleBookmark = {
                            onNewsResourcesCheckedChanged(newsResource.id, !isBookmarked)
                        },
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopicSelection(
    uiState: FeedWithInterestsSelection,
    onTopicCheckedChanged: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(24.dp),
        modifier = modifier
            // LazyHorizontalGrid has to be constrained in height.
            // However, we can't set a fixed height because the horizontal grid contains
            // vertical text that can be rescaled.
            // When the fontScale is at most 1, we know that the horizontal grid will be at most
            // 240dp tall, so this is an upper bound for when the font scale is at most 1.
            // When the fontScale is greater than 1, the height required by the text inside the
            // horizontal grid will increase by at most the same factor, so 240sp is a valid
            // upper bound for how much space we need in that case.
            // The maximum of these two bounds is therefore a valid upper bound in all cases.
            .heightIn(max = max(240.dp, with(LocalDensity.current) { 240.sp.toDp() }))
            .fillMaxWidth()
    ) {
        items(uiState.topics) {
            SingleTopicButton(
                name = it.topic.name,
                topicId = it.topic.id,
                isSelected = it.isFollowed,
                onClick = onTopicCheckedChanged
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleTopicButton(
    name: String,
    topicId: Int,
    isSelected: Boolean,
    onClick: (Int, Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(264.dp)
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        selected = isSelected,
        onClick = {
            onClick(topicId, !isSelected)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp)
        ) {
            Text(
                text = name,
                style = NiaTypography.titleSmall,
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            NiaToggleButton(
                checked = isSelected,
                onCheckedChange = { checked -> onClick(topicId, !isSelected) },
                icon = {
                    Icon(imageVector = NiaIcons.Add, contentDescription = name)
                },
                checkedIcon = {
                    Icon(imageVector = NiaIcons.Check, contentDescription = name)
                }
            )
        }
    }
}

@Preview
@Composable
fun ForYouScreenLoading() {
    MaterialTheme {
        Surface {
            ForYouScreen(
                uiState = ForYouFeedUiState.Loading,
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}

@Preview
@Composable
fun ForYouScreenTopicSelection() {
    ForYouScreen(
        uiState = FeedWithInterestsSelection(
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
            ),
            authors = listOf(
                FollowableAuthor(
                    author = Author(
                        id = 0,
                        name = "Android Dev",
                        imageUrl = "",
                        twitter = "",
                        mediumPage = ""
                    ),
                    isFollowed = false
                ),
                FollowableAuthor(
                    author = Author(
                        id = 1,
                        name = "Android Dev 2",
                        imageUrl = "",
                        twitter = "",
                        mediumPage = ""
                    ),
                    isFollowed = false
                ),
                FollowableAuthor(
                    author = Author(
                        id = 2,
                        name = "Android Dev 3",
                        imageUrl = "",
                        twitter = "",
                        mediumPage = ""
                    ),
                    isFollowed = false
                )
            ),
        ),
        onAuthorCheckedChanged = { _, _ -> },
        onTopicCheckedChanged = { _, _ -> },
        saveFollowedTopics = {},
        onNewsResourcesCheckedChanged = { _, _ -> }
    )
}

@Preview
@Composable
fun PopulatedFeed() {
    MaterialTheme {
        Surface {
            ForYouScreen(
                uiState = FeedWithoutTopicSelection(
                    feed = emptyList()
                ),
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}
