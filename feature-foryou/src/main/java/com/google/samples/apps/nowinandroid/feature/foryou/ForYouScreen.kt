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

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import androidx.core.view.doOnPreDraw
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaFilledButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaGradientBackground
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaToggleButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTopAppBar
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.previewAuthors
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources
import com.google.samples.apps.nowinandroid.core.model.data.previewTopics
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank
import com.google.samples.apps.nowinandroid.core.ui.newsFeed

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ForYouRoute(
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel()
) {
    val interestsSelectionState by viewModel.interestsSelectionState.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    ForYouScreen(
        interestsSelectionState = interestsSelectionState,
        feedState = feedState,
        onTopicCheckedChanged = viewModel::updateTopicSelection,
        onAuthorCheckedChanged = viewModel::updateAuthorSelection,
        saveFollowedTopics = viewModel::saveFollowedInterests,
        onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForYouScreen(
    interestsSelectionState: ForYouInterestsSelectionUiState,
    feedState: NewsFeedUiState,
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
                    navigationIcon = NiaIcons.Search,
                    navigationIconContentDescription = stringResource(
                        id = R.string.for_you_top_app_bar_action_search
                    ),
                    actionIcon = NiaIcons.AccountCircle,
                    actionIconContentDescription = stringResource(
                        id = R.string.for_you_top_app_bar_action_my_account
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
            // Workaround to call Activity.reportFullyDrawn from Jetpack Compose.
            // This code should be called when the UI is ready for use
            // and relates to Time To Full Display.
            val interestsLoaded =
                interestsSelectionState !is ForYouInterestsSelectionUiState.Loading
            val feedLoaded = feedState !is NewsFeedUiState.Loading

            if (interestsLoaded && feedLoaded) {
                val localView = LocalView.current
                // We use Unit to call reportFullyDrawn only on the first recomposition,
                // however it will be called again if this composable goes out of scope.
                // Activity.reportFullyDrawn() has its own check for this
                // and is safe to call multiple times though.
                LaunchedEffect(Unit) {
                    // We're leveraging the fact, that the current view is directly set as content of Activity.
                    val activity = localView.context as? Activity ?: return@LaunchedEffect
                    // To be sure not to call in the middle of a frame draw.
                    localView.doOnPreDraw { activity.reportFullyDrawn() }
                }
            }

            val tag = "forYou:feed"

            val lazyGridState = rememberLazyGridState()
            TrackScrollJank(scrollableState = lazyGridState, stateName = tag)

            LazyVerticalGrid(
                columns = Adaptive(300.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = modifier
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
                    .fillMaxSize()
                    .testTag("forYou:feed"),
                state = lazyGridState
            ) {
                interestsSelection(
                    interestsSelectionState = interestsSelectionState,
                    onAuthorCheckedChanged = onAuthorCheckedChanged,
                    onTopicCheckedChanged = onTopicCheckedChanged,
                    saveFollowedTopics = saveFollowedTopics
                )

                newsFeed(
                    feedState = feedState,
                    // Avoid showing a second loading wheel if we already are for the interests
                    // selection
                    showLoadingUIIfLoading =
                    interestsSelectionState !is ForYouInterestsSelectionUiState.Loading,
                    onNewsResourcesCheckedChanged = onNewsResourcesCheckedChanged,
                    loadingContentDescription = R.string.for_you_loading
                )

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }
}

/**
 * An extension on [LazyListScope] defining the interests selection portion of the for you screen.
 * Depending on the [interestsSelectionState], this might emit no items.
 *
 * @param showLoaderWhenLoading if true, show a visual indication of loading if the
 * [interestsSelectionState] is loading. This is controllable to permit du-duplicating loading
 * states.
 */
private fun LazyGridScope.interestsSelection(
    interestsSelectionState: ForYouInterestsSelectionUiState,
    onAuthorCheckedChanged: (String, Boolean) -> Unit,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit
) {
    when (interestsSelectionState) {
        ForYouInterestsSelectionUiState.Loading -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                NiaLoadingWheel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .testTag("forYou:loading"),
                    contentDesc = stringResource(id = R.string.for_you_loading),
                )
            }
        }
        ForYouInterestsSelectionUiState.NoInterestsSelection -> Unit
        is ForYouInterestsSelectionUiState.WithInterestsSelection -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Text(
                        text = stringResource(R.string.onboarding_guidance_title),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.onboarding_guidance_subtitle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    AuthorsCarousel(
                        authors = interestsSelectionState.authors,
                        onAuthorClick = onAuthorCheckedChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    TopicSelection(
                        interestsSelectionState,
                        onTopicCheckedChanged,
                        Modifier.padding(bottom = 8.dp)
                    )
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
}

@Composable
private fun TopicSelection(
    interestsSelectionState: ForYouInterestsSelectionUiState.WithInterestsSelection,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) = trace("TopicSelection") {
    val lazyGridState = rememberLazyGridState()
    TrackScrollJank(scrollableState = lazyGridState, stateName = "forYou:TopicSelection")

    LazyHorizontalGrid(
        state = lazyGridState,
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
) = trace("SingleTopicButton") {
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
                style = MaterialTheme.typography.titleSmall,
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
                        imageVector = NiaIcons.Add,
                        contentDescription = name
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = NiaIcons.Check,
                        contentDescription = name
                    )
                }
            )
        }
    }
}

@Composable
fun TopicIcon(
    imageUrl: String,
    modifier: Modifier = Modifier
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

@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun ForYouScreenPopulatedFeed() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
                feedState = NewsFeedUiState.Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}

@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun ForYouScreenTopicSelection() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                interestsSelectionState = ForYouInterestsSelectionUiState.WithInterestsSelection(
                    topics = previewTopics.map { FollowableTopic(it, false) },
                    authors = previewAuthors.map { FollowableAuthor(it, false) }
                ),
                feedState = NewsFeedUiState.Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}

@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun ForYouScreenLoading() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                interestsSelectionState = ForYouInterestsSelectionUiState.Loading,
                feedState = NewsFeedUiState.Loading,
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}
