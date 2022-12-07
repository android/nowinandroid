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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.doOnPreDraw
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaFilledButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaOverlayLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources
import com.google.samples.apps.nowinandroid.core.model.data.previewTopics
import com.google.samples.apps.nowinandroid.core.ui.DevicePreviews
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank
import com.google.samples.apps.nowinandroid.core.ui.newsFeed
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.LoadFailed
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.Loading
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.NotShown
import com.google.samples.apps.nowinandroid.feature.foryou.components.TopicSelection

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun ForYouRoute(
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel()
) {
    val onboardingUiState by viewModel.onboardingUiState.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()

    ForYouScreen(
        modifier = modifier,
        isSyncing = isSyncing,
        onboardingUiState = onboardingUiState,
        feedState = feedState,
        isOffline = isOffline,
        onTopicCheckedChanged = viewModel::updateTopicSelection,
        saveFollowedTopics = viewModel::dismissOnboarding,
        onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved
    )
}

@Composable
internal fun ForYouScreen(
    modifier: Modifier = Modifier,
    isSyncing: Boolean,
    onboardingUiState: OnboardingUiState,
    feedState: NewsFeedUiState,
    isOffline: Boolean,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit
) {
    val isOnboardingLoading = onboardingUiState is Loading
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
        state = state
    ) {
        if (!isOffline) {
            onboarding(
                onboardingUiState = onboardingUiState,
                onTopicCheckedChanged = onTopicCheckedChanged,
                saveFollowedTopics = saveFollowedTopics,
                // Custom LayoutModifier to remove the enforced parent 16.dp contentPadding
                // from the LazyVerticalGrid and enable edge-to-edge scrolling for this section
                interestsItemModifier = Modifier.layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(
                            maxWidth = constraints.maxWidth + 32.dp.roundToPx()
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                }
            )

            newsFeed(
                feedState = feedState,
                onNewsResourcesCheckedChanged = onNewsResourcesCheckedChanged,
            )

            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    // TODO: Check that the Scaffold handles this correctly in NiaApp
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }

    // Show the Offline Screen if there is no internet connection.
    if (isOffline) {
        OfflineScreen()
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
        Box(modifier = modifier.fillMaxWidth()) {
            NiaOverlayLoadingWheel(
                modifier = modifier
                    .align(Alignment.Center)
                    .testTag("forYou:loadingWheel"),
                contentDesc = loadingContentDescription
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
    interestsItemModifier: Modifier = Modifier
) = when (onboardingUiState) {
    Loading,
    LoadFailed,
    NotShown -> Unit

    is OnboardingUiState.Shown -> {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = interestsItemModifier) {
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
                TopicSelection(
                    onboardingUiState,
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
                        enabled = onboardingUiState.isDismissable,
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

@DevicePreviews
@Composable
fun ForYouScreenPopulatedFeed() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                isSyncing = false,
                onboardingUiState = NotShown,
                feedState = NewsFeedUiState.Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                isOffline = false,
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
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
                onboardingUiState = NotShown,
                feedState = NewsFeedUiState.Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                isOffline = true,
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
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
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                isOffline = false,
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
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
                onboardingUiState = Loading,
                feedState = NewsFeedUiState.Loading,
                isOffline = false,
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
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
                onboardingUiState = Loading,
                feedState = NewsFeedUiState.Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                isOffline = false,
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}
