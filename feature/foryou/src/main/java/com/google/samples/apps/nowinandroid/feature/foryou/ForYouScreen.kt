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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import androidx.core.view.doOnPreDraw
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.component.DynamicAsyncImage
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaIconToggleButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaOverlayLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.previewUserNewsResources
import com.google.samples.apps.nowinandroid.core.model.data.previewTopics
import com.google.samples.apps.nowinandroid.core.ui.DevicePreviews
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank
import com.google.samples.apps.nowinandroid.core.ui.newsFeed

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun ForYouRoute(
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel(),
) {
    val onboardingUiState by viewModel.onboardingUiState.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    ForYouScreen(
        isSyncing = isSyncing,
        onboardingUiState = onboardingUiState,
        feedState = feedState,
        onTopicCheckedChanged = viewModel::updateTopicSelection,
        saveFollowedTopics = viewModel::dismissOnboarding,
        onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved,
        modifier = modifier,
    )
}

@Composable
internal fun ForYouScreen(
    isSyncing: Boolean,
    onboardingUiState: OnboardingUiState,
    feedState: NewsFeedUiState,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOnboardingLoading = onboardingUiState is OnboardingUiState.Loading
    val isFeedLoading = feedState is NewsFeedUiState.Loading

    // Workaround to call Activity.reportFullyDrawn from Jetpack Compose.
    // This code should be called when the UI is ready for use
    // and relates to Time To Full Display.
    // TODO replace with ReportDrawnWhen { } once androidx.activity-compose 1.7.0 is used (currently alpha)
    if (!isSyncing && !isOnboardingLoading && !isFeedLoading) {
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

    val state = rememberLazyGridState()
    TrackScrollJank(scrollableState = state, stateName = "forYou:feed")

    LazyVerticalGrid(
        columns = Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("forYou:feed"),
        state = state,
    ) {
        onboarding(
            onboardingUiState = onboardingUiState,
            onTopicCheckedChanged = onTopicCheckedChanged,
            saveFollowedTopics = saveFollowedTopics,
            // Custom LayoutModifier to remove the enforced parent 16.dp contentPadding
            // from the LazyVerticalGrid and enable edge-to-edge scrolling for this section
            interestsItemModifier = Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                    ),
                )
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            },
        )

        newsFeed(
            feedState = feedState,
            onNewsResourcesCheckedChanged = onNewsResourcesCheckedChanged,
        )

        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                // Add space for the content to clear the "offline" snackbar.
                // TODO: Check that the Scaffold handles this correctly in NiaApp
                // if (isOffline) Spacer(modifier = Modifier.height(48.dp))
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
    AnimatedVisibility(
        visible = isSyncing || isFeedLoading || isOnboardingLoading,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        val loadingContentDescription = stringResource(id = R.string.for_you_loading)
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            NiaOverlayLoadingWheel(
                modifier = Modifier
                    .align(Alignment.Center),
                contentDesc = loadingContentDescription,
            )
        }
    }
}

/**
 * An extension on [LazyListScope] defining the onboarding portion of the for you screen.
 * Depending on the [onboardingUiState], this might emit no items.
 *
 */
private fun LazyGridScope.onboarding(
    onboardingUiState: OnboardingUiState,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit,
    interestsItemModifier: Modifier = Modifier,
) {
    when (onboardingUiState) {
        OnboardingUiState.Loading,
        OnboardingUiState.LoadFailed,
        OnboardingUiState.NotShown,
        -> Unit

        is OnboardingUiState.Shown -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(modifier = interestsItemModifier) {
                    Text(
                        text = stringResource(R.string.onboarding_guidance_title),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(R.string.onboarding_guidance_subtitle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    TopicSelection(
                        onboardingUiState,
                        onTopicCheckedChanged,
                        Modifier.padding(bottom = 8.dp),
                    )
                    // Done button
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        NiaButton(
                            onClick = saveFollowedTopics,
                            enabled = onboardingUiState.isDismissable,
                            modifier = Modifier
                                .padding(horizontal = 40.dp)
                                .width(364.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.done),
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
    onboardingUiState: OnboardingUiState.Shown,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = trace("TopicSelection") {
    val lazyGridState = rememberLazyGridState()
    val topicSelectionTestTag = "forYou:topicSelection"

    TrackScrollJank(scrollableState = lazyGridState, stateName = topicSelectionTestTag)

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
            .testTag(topicSelectionTestTag),
    ) {
        items(
            items = onboardingUiState.topics,
            key = { it.topic.id },
        ) {
            SingleTopicButton(
                name = it.topic.name,
                topicId = it.topic.id,
                imageUrl = it.topic.imageUrl,
                isSelected = it.isFollowed,
                onClick = onTopicCheckedChanged,
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
    onClick: (String, Boolean) -> Unit,
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
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp),
        ) {
            TopicIcon(
                imageUrl = imageUrl,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
            )
            NiaIconToggleButton(
                checked = isSelected,
                onCheckedChange = { checked -> onClick(topicId, checked) },
                icon = {
                    Icon(
                        imageVector = NiaIcons.Add,
                        contentDescription = name,
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = NiaIcons.Check,
                        contentDescription = name,
                    )
                },
            )
        }
    }
}

@Composable
fun TopicIcon(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    DynamicAsyncImage(
        // TODO b/228077205, show loading image visual instead of static placeholder
        placeholder = painterResource(R.drawable.ic_icon_placeholder),
        imageUrl = imageUrl,
        contentDescription = null, // decorative
        modifier = modifier
            .padding(10.dp)
            .size(32.dp),
    )
}

@DevicePreviews
@Composable
fun ForYouScreenPopulatedFeed() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                isSyncing = false,
                onboardingUiState = OnboardingUiState.NotShown,
                feedState = NewsFeedUiState.Success(
                    feed = previewUserNewsResources,
                ),
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> },
            )
        }
    }
}

@DevicePreviews
@Composable
fun ForYouScreenOfflinePopulatedFeed() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                isSyncing = false,
                onboardingUiState = OnboardingUiState.NotShown,
                feedState = NewsFeedUiState.Success(
                    feed = previewUserNewsResources,
                ),
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> },
            )
        }
    }
}

@DevicePreviews
@Composable
fun ForYouScreenTopicSelection() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                isSyncing = false,
                onboardingUiState = OnboardingUiState.Shown(
                    topics = previewTopics.map { FollowableTopic(it, false) },
                ),
                feedState = NewsFeedUiState.Success(
                    feed = previewUserNewsResources,
                ),
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> },
            )
        }
    }
}

@DevicePreviews
@Composable
fun ForYouScreenLoading() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                isSyncing = false,
                onboardingUiState = OnboardingUiState.Loading,
                feedState = NewsFeedUiState.Loading,
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> },
            )
        }
    }
}

@DevicePreviews
@Composable
fun ForYouScreenPopulatedAndLoading() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                isSyncing = true,
                onboardingUiState = OnboardingUiState.Loading,
                feedState = NewsFeedUiState.Success(
                    feed = previewUserNewsResources,
                ),
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> },
            )
        }
    }
}
