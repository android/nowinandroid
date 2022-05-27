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
import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.ui.LoadingWheel
import com.google.samples.apps.nowinandroid.core.ui.NewsResourceCardExpanded
import com.google.samples.apps.nowinandroid.core.ui.component.NiaFilledButton
import com.google.samples.apps.nowinandroid.core.ui.component.NiaGradientBackground
import com.google.samples.apps.nowinandroid.core.ui.component.NiaToggleButton
import com.google.samples.apps.nowinandroid.core.ui.component.NiaTopAppBar
import com.google.samples.apps.nowinandroid.core.ui.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTypography
import kotlin.math.floor
import kotlinx.datetime.Instant

@Composable
fun ForYouRoute(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel()
) {
    val interestsSelectionState by viewModel.interestsSelectionState.collectAsState()
    val feedState by viewModel.feedState.collectAsState()
    ForYouScreen(
        windowSizeClass = windowSizeClass,
        modifier = modifier,
        interestsSelectionState = interestsSelectionState,
        feedState = feedState,
        onTopicCheckedChanged = viewModel::updateTopicSelection,
        onAuthorCheckedChanged = viewModel::updateAuthorSelection,
        saveFollowedTopics = viewModel::saveFollowedInterests,
        onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForYouScreen(
    windowSizeClass: WindowSizeClass,
    interestsSelectionState: ForYouInterestsSelectionUiState,
    feedState: ForYouFeedUiState,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    onAuthorCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    NiaGradientBackground {
        Scaffold(
            topBar = {
                NiaTopAppBar(
                    titleRes = R.string.top_app_bar_title,
                    navigationIcon = Icons.Filled.Search,
                    navigationIconContentDescription = stringResource(
                        id = R.string.top_app_bar_navigation_button_content_desc
                    ),
                    actionIcon = Icons.Outlined.AccountCircle,
                    actionIconContentDescription = stringResource(
                        id = R.string.top_app_bar_navigation_button_content_desc
                    ),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            // TODO: Replace with `LazyVerticalGrid` when blocking bugs are fixed:
            //       https://issuetracker.google.com/issues/230514914
            //       https://issuetracker.google.com/issues/231320714
            BoxWithConstraints(
                modifier = modifier
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
            ) {
                val numberOfColumns = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> 1
                    else -> floor(maxWidth / 300.dp).toInt().coerceAtLeast(1)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    InterestsSelection(
                        interestsSelectionState = interestsSelectionState,
                        showLoadingUIIfLoading = true,
                        onAuthorCheckedChanged = onAuthorCheckedChanged,
                        onTopicCheckedChanged = onTopicCheckedChanged,
                        saveFollowedTopics = saveFollowedTopics
                    )

                    Feed(
                        feedState = feedState,
                        // Avoid showing a second loading wheel if we already are for the interests
                        // selection
                        showLoadingUIIfLoading =
                        interestsSelectionState !is ForYouInterestsSelectionUiState.Loading,
                        numberOfColumns = numberOfColumns,
                        onNewsResourcesCheckedChanged = onNewsResourcesCheckedChanged
                    )

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
        }
    }
}

/**
 * An extension on [LazyListScope] defining the interests selection portion of the for you screen.
 * Depending on the [interestsSelectionState], this might emit no items.
 *
 * @param showLoadingUIIfLoading if true, show a visual indication of loading if the
 * [interestsSelectionState] is loading. This is controllable to permit du-duplicating loading
 * states.
 */
private fun LazyListScope.InterestsSelection(
    interestsSelectionState: ForYouInterestsSelectionUiState,
    showLoadingUIIfLoading: Boolean,
    onAuthorCheckedChanged: (String, Boolean) -> Unit,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit
) {
    when (interestsSelectionState) {
        ForYouInterestsSelectionUiState.Loading -> {
            if (showLoadingUIIfLoading) {
                item {
                    LoadingWheel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        contentDesc = stringResource(id = R.string.for_you_loading),
                    )
                }
            }
        }
        ForYouInterestsSelectionUiState.NoInterestsSelection -> Unit
        is ForYouInterestsSelectionUiState.WithInterestsSelection -> {
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
                    authors = interestsSelectionState.authors,
                    onAuthorClick = onAuthorCheckedChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            item {
                TopicSelection(
                    interestsSelectionState,
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
                    NiaFilledButton(
                        onClick = saveFollowedTopics,
                        enabled = interestsSelectionState.canSaveInterests,
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .width(364.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.done)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicSelection(
    interestsSelectionState: ForYouInterestsSelectionUiState.WithInterestsSelection,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
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
        items(interestsSelectionState.topics) {
            SingleTopicButton(
                name = it.topic.name,
                topicId = it.topic.id,
                imageUrl = it.topic.imageUrl,
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
    topicId: String,
    imageUrl: String,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(312.dp)
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        color = MaterialTheme.colorScheme.surface,
        selected = isSelected,
        onClick = {
            onClick(topicId, !isSelected)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp)
        ) {
            TopicIcon(
                imageUrl = imageUrl
            )
            Text(
                text = name,
                style = NiaTypography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            NiaToggleButton(
                checked = isSelected,
                onCheckedChange = { checked -> onClick(topicId, checked) },
                icon = {
                    Icon(
                        imageVector = NiaIcons.Add, contentDescription = name,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = NiaIcons.Check, contentDescription = name,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}

@Composable
fun TopicIcon(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    AsyncImage(
        // TODO b/228077205, show loading image visual instead of static placeholder
        placeholder = painterResource(R.drawable.ic_icon_placeholder),
        model = imageUrl,
        contentDescription = null, // decorative
        modifier = modifier
            .padding(10.dp)
            .size(32.dp)
    )
}

/**
 * An extension on [LazyListScope] defining the feed portion of the for you screen.
 * Depending on the [feedState], this might emit no items.
 *
 * @param showLoadingUIIfLoading if true, show a visual indication of loading if the
 * [feedState] is loading. This is controllable to permit du-duplicating loading
 * states.
 */
private fun LazyListScope.Feed(
    feedState: ForYouFeedUiState,
    showLoadingUIIfLoading: Boolean,
    @IntRange(from = 1) numberOfColumns: Int,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit
) {
    when (feedState) {
        ForYouFeedUiState.Loading -> {
            if (showLoadingUIIfLoading) {
                item {
                    LoadingWheel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        contentDesc = stringResource(id = R.string.for_you_loading),
                    )
                }
            }
        }
        is ForYouFeedUiState.Success -> {
            items(
                feedState.feed.chunked(numberOfColumns)
            ) { saveableNewsResources ->
                Row(
                    modifier = Modifier.padding(
                        top = 32.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // The last row may not be complete, but for a consistent grid
                    // structure we still want an element taking up the empty space.
                    // Therefore, the last row may have empty boxes.
                    repeat(numberOfColumns) { index ->
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            val saveableNewsResource =
                                saveableNewsResources.getOrNull(index)

                            if (saveableNewsResource != null) {
                                val launchResourceIntent =
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(saveableNewsResource.newsResource.url)
                                    )
                                val context = LocalContext.current

                                NewsResourceCardExpanded(
                                    newsResource = saveableNewsResource.newsResource,
                                    isBookmarked = saveableNewsResource.isSaved,
                                    onClick = {
                                        ContextCompat.startActivity(
                                            context,
                                            launchResourceIntent,
                                            null
                                        )
                                    },
                                    onToggleBookmark = {
                                        onNewsResourcesCheckedChanged(
                                            saveableNewsResource.newsResource.id,
                                            !saveableNewsResource.isSaved
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun ForYouScreenLoading() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
                interestsSelectionState = ForYouInterestsSelectionUiState.Loading,
                feedState = ForYouFeedUiState.Loading,
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun ForYouScreenTopicSelection() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
                interestsSelectionState = ForYouInterestsSelectionUiState.WithInterestsSelection(
                    topics = listOf(
                        FollowableTopic(
                            topic = Topic(
                                id = "0",
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
                                id = "1",
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
                                id = "2",
                                name = "Publishing and Distribution",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                    ),
                    authors = listOf(
                        FollowableAuthor(
                            author = Author(
                                id = "0",
                                name = "Android Dev",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = "",
                                bio = "",
                            ),
                            isFollowed = false
                        ),
                        FollowableAuthor(
                            author = Author(
                                id = "1",
                                name = "Android Dev 2",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = "",
                                bio = "",
                            ),
                            isFollowed = false
                        ),
                        FollowableAuthor(
                            author = Author(
                                id = "2",
                                name = "Android Dev 3",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = "",
                                bio = "",
                            ),
                            isFollowed = false
                        )
                    )
                ),
                feedState = ForYouFeedUiState.Success(
                    feed = saveableNewsResource,
                ),
                onAuthorCheckedChanged = { _, _ -> },
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun PopulatedFeed() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
                interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
                feedState = ForYouFeedUiState.Success(
                    feed = saveableNewsResource
                ),
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}

private val saveableNewsResource = listOf(
    SaveableNewsResource(
        newsResource = NewsResource(
            id = "1",
            episodeId = "52",
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
                    id = "0",
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
            id = "2",
            episodeId = "52",
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
                    id = "1",
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
            id = "3",
            episodeId = "52",
            title = "Community tip on Paging",
            content = "Tips for using the Paging library from the developer community",
            url = "https://youtu.be/r5JgIyS3t3s",
            headerImageUrl = "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
            publishDate = Instant.parse("2021-11-08T00:00:00.000Z"),
            type = Video,
            topics = listOf(
                Topic(
                    id = "1",
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
