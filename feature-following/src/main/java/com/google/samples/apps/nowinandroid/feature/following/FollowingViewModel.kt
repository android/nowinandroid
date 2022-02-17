/*
 * Copyright 2021 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.feature.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.domain.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val topicsRepository: TopicsRepository
) : ViewModel() {

    private val followedTopicIdsStream = topicsRepository.getFollowedTopicIdsStream()
        .map<Set<Int>, FollowingState> { followedTopics ->
            FollowingState.Topics(topics = followedTopics)
        }
        .catch { emit(FollowingState.Error) }

    val uiState: StateFlow<FollowingUiState> = combine(
        followedTopicIdsStream,
        topicsRepository.getTopicsStream(),
    ) { followedTopicIdsState, topics ->
        if (followedTopicIdsState is FollowingState.Topics) {
            mapFollowedAndUnfollowedTopics(topics)
        } else {
            flowOf(FollowingUiState.Error)
        }
    }
        .flatMapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FollowingUiState.Loading
        )

    fun followTopic(followedTopicId: Int, followed: Boolean) {
        viewModelScope.launch {
            topicsRepository.toggleFollowedTopicId(followedTopicId, followed)
        }
    }

    private fun mapFollowedAndUnfollowedTopics(topics: List<Topic>): Flow<FollowingUiState.Topics> =
        topicsRepository.getFollowedTopicIdsStream().map { followedTopicIds ->
            FollowingUiState.Topics(
                topics = topics
                    .map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = topic.id in followedTopicIds,
                        )
                    }
                    .sortedBy { it.topic.name }
            )
        }
}

private sealed interface FollowingState {
    data class Topics(val topics: Set<Int>) : FollowingState
    object Error : FollowingState
}

sealed interface FollowingUiState {
    object Loading : FollowingUiState
    data class Topics(val topics: List<FollowableTopic>) : FollowingUiState
    object Error : FollowingUiState
}
