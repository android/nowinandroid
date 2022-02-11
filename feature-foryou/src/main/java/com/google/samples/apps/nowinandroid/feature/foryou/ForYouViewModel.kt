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

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.domain.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.domain.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.feature.foryou.util.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val followedTopicsStateFlow = topicsRepository.getFollowedTopicIdsStream()
        .map { followedTopics ->
            if (followedTopics.isEmpty()) {
                FollowedTopicsState.None
            } else {
                FollowedTopicsState.FollowedTopics(
                    topics = followedTopics
                )
            }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FollowedTopicsState.Unknown
        )

    /**
     * The in-progress set of topics to be selected, persisted through process death with a
     * [SavedStateHandle].
     */
    private var inProgressTopicSelection by savedStateHandle.saveable {
        mutableStateOf<Set<Int>>(emptySet())
    }

    val uiState: StateFlow<ForYouFeedUiState> = combine(
        followedTopicsStateFlow,
        topicsRepository.getTopicsStream(),
        snapshotFlow { inProgressTopicSelection },
    ) { followedTopicsUserState, availableTopics, inProgressTopicSelection ->
        when (followedTopicsUserState) {
            // If we don't know the current selection state, just emit loading.
            FollowedTopicsState.Unknown -> flowOf<ForYouFeedUiState>(ForYouFeedUiState.Loading)
            // If the user has followed topics, use those followed topics to populate the feed
            is FollowedTopicsState.FollowedTopics -> {
                newsRepository.getNewsResourcesStream(
                    filterTopicIds = followedTopicsUserState.topics
                )
                    .map { feed ->
                        ForYouFeedUiState.PopulatedFeed.FeedWithoutTopicSelection(
                            feed = feed
                        )
                    }
            }
            // If the user hasn't followed topics yet, show the topic selection, as well as a
            // realtime populated feed based on those in-progress topic selections.
            FollowedTopicsState.None -> {
                newsRepository.getNewsResourcesStream(
                    filterTopicIds = inProgressTopicSelection
                )
                    .map { feed ->
                        ForYouFeedUiState.PopulatedFeed.FeedWithTopicSelection(
                            selectedTopics = availableTopics.map { topic ->
                                topic to (topic.id in inProgressTopicSelection)
                            },
                            feed = feed
                        )
                    }
            }
        }
    }
        // Flatten the feed flows.
        // As the selected topics and topic state changes, this will cancel the old feed monitoring
        // and start the new one.
        .flatMapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ForYouFeedUiState.Loading
        )

    fun updateTopicSelection(topicId: Int, isChecked: Boolean) {
        withMutableSnapshot {
            inProgressTopicSelection =
                // Update the in-progress selection based on whether the topic id was checked
                if (isChecked) {
                    inProgressTopicSelection + topicId
                } else {
                    inProgressTopicSelection - topicId
                }
        }
    }

    fun saveFollowedTopics() {
        if (inProgressTopicSelection.isEmpty()) return

        viewModelScope.launch {
            topicsRepository.setFollowedTopicIds(inProgressTopicSelection)
        }
    }
}

/**
 * A sealed hierarchy for the user's current followed topics state.
 */
private sealed interface FollowedTopicsState {

    /**
     * The current state is unknown (hasn't loaded yet)
     */
    object Unknown : FollowedTopicsState

    /**
     * The user hasn't followed any topics yet.
     */
    object None : FollowedTopicsState

    /**
     * The user has followed the given (non-empty) set of [topics].
     */
    data class FollowedTopics(
        val topics: Set<Int>,
    ) : FollowedTopicsState
}

/**
 * A sealed hierarchy describing the for you screen state.
 */
sealed interface ForYouFeedUiState {

    /**
     * The screen is still loading.
     */
    object Loading : ForYouFeedUiState

    /**
     * Loaded with a populated [feed] of [NewsResource]s.
     */
    sealed interface PopulatedFeed : ForYouFeedUiState {

        /**
         * The list of news resources contained in this [PopulatedFeed].
         */
        val feed: List<NewsResource>

        /**
         * The feed, along with a list of topics that can be selected.
         */
        data class FeedWithTopicSelection(
            val selectedTopics: List<Pair<Topic, Boolean>>,
            override val feed: List<NewsResource>
        ) : PopulatedFeed {
            val canSaveSelectedTopics: Boolean = selectedTopics.any { it.second }
        }

        /**
         * Just the feed.
         */
        data class FeedWithoutTopicSelection(
            override val feed: List<NewsResource>
        ) : PopulatedFeed
    }
}
