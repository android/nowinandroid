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

package com.google.samples.apps.nowinandroid.ui.foryou

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.data.model.NewsResource
import com.google.samples.apps.nowinandroid.data.model.Topic

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
        saveFollowedTopics = viewModel::saveFollowedTopics
    )
}

@Composable
fun ForYouScreen(
    uiState: ForYouFeedUiState,
    onTopicCheckedChanged: (Int, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            ForYouFeedUiState.Loading -> {
                val forYouLoading = stringResource(id = R.string.for_you_loading)

                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .semantics {
                            contentDescription = forYouLoading
                        },
                    color = MaterialTheme.colorScheme.primary
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

                    items(uiState.feed) { _: NewsResource ->
                        // TODO: News item
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
            uiState.selectedTopics.forEach { (topic, isSelected) ->
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
                        }
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
        saveFollowedTopics = {}
    )
}

@Preview
@Composable
fun ForYouScreenTopicSelection() {
    ForYouScreen(
        uiState = ForYouFeedUiState.PopulatedFeed.FeedWithTopicSelection(
            selectedTopics = listOf(
                Topic(
                    id = 0,
                    name = "Headlines",
                    description = ""
                ) to false,
                Topic(
                    id = 1,
                    name = "UI",
                    description = ""
                ) to true,
                Topic(
                    id = 2,
                    name = "Tools",
                    description = ""
                ) to false
            ),
            feed = emptyList()
        ),
        onTopicCheckedChanged = { _, _ -> },
        saveFollowedTopics = {}
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
        saveFollowedTopics = {}
    )
}
