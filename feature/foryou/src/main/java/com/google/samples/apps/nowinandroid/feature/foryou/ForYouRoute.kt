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

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.previewAuthors
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources
import com.google.samples.apps.nowinandroid.core.model.data.previewTopics
import com.google.samples.apps.nowinandroid.core.ui.DevicePreviews
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState.Success
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.Loading
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.NotShown
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.Shown

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
        onAuthorCheckedChanged = viewModel::updateAuthorSelection,
        saveFollowedTopics = viewModel::dismissOnboarding,
        onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved
    )
}

@DevicePreviews
@Composable
fun ForYouScreenPopulatedFeed() {
    BoxWithConstraints {
        NiaTheme {
            ForYouScreen(
                isSyncing = false,
                onboardingUiState = NotShown,
                feedState = Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                isOffline = false,
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
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
                onboardingUiState = Shown(
                    topics = previewTopics.map { FollowableTopic(it, false) },
                    authors = previewAuthors.map { FollowableAuthor(it, false) }
                ),
                feedState = Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                isOffline = false,
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
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
                onAuthorCheckedChanged = { _, _ -> },
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
                feedState = Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                isOffline = false,
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}
