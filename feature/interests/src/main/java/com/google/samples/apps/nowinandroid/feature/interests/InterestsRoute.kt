/*
 * Copyright 2023 The Android Open Source Project
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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun InterestsRoute(
    listState: LazyGridState,
    shouldShowTwoPane: Boolean,
    onTopicClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InterestsViewModel = hiltViewModel(),
) {
    val interestUiState by viewModel.interestUiState.collectAsStateWithLifecycle()
    val topicUiState by viewModel.topicUiState.collectAsStateWithLifecycle()

    Row(modifier = modifier.fillMaxSize()) {
        if (shouldShowTwoPane || topicUiState == null) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .then(
                        if (topicUiState != null) {
                            Modifier.widthIn(min = 350.dp)
                        } else {
                            Modifier.weight(1f)
                        },
                    ),
            ) {
                InterestsScreen(
                    uiState = interestUiState,
                    listState = listState,
                    followTopic = viewModel::followTopic,
                    onTopicClick = onTopicClick,
                    modifier = Modifier.matchParentSize(),
                )
            }
        }
        AnimatedVisibility(
            visible = topicUiState != null,
            enter = slideInHorizontally(initialOffsetX = { it / 2 }),
            exit = slideOutHorizontally(targetOffsetX = { it / 2 }),
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .run {
                    if (!shouldShowTwoPane) {
                        safeDrawingPadding()
                    } else {
                        this
                    }
                },
        ) {
            topicUiState?.let { state ->
                TopicScreen(
                    topicUiState = state,
                    onBackClick = onBackClick,
                    onFollowClick = viewModel::followTopic,
                    onTopicClick = onTopicClick,
                    onBookmarkChanged = viewModel::bookmarkNews,
                    onNewsResourceViewed = viewModel::newsViewed,
                )
            }
        }
    }
}
