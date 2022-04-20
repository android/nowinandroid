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
import com.google.samples.apps.nowinandroid.core.domain.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.domain.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val authorsRepository: AuthorsRepository,
    private val topicsRepository: TopicsRepository
) : ViewModel() {

    private val _tabState = MutableStateFlow(
        FollowingTabState(
            titles = listOf(R.string.following_topics, R.string.following_people),
            currentIndex = 0
        )
    )
    val tabState: StateFlow<FollowingTabState> = _tabState.asStateFlow()

    val uiState: StateFlow<FollowingUiState> = combine(
        authorsRepository.getAuthorsStream(),
        authorsRepository.getFollowedAuthorIdsStream(),
        topicsRepository.getTopicsStream(),
        topicsRepository.getFollowedTopicIdsStream(),
    ) { availableAuthors, followedAuthorIdsState, availableTopics, followedTopicIdsState ->

        FollowingUiState.Interests(
            authors = availableAuthors
                .map { author ->
                    FollowableAuthor(
                        author = author,
                        isFollowed = author.id in followedAuthorIdsState
                    )
                }
                .sortedBy { it.author.name },
            topics = availableTopics
                .map { topic ->
                    FollowableTopic(
                        topic = topic,
                        isFollowed = topic.id in followedTopicIdsState
                    )
                }
                .sortedBy { it.topic.name }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FollowingUiState.Loading
        )

    fun followTopic(followedTopicId: String, followed: Boolean) {
        viewModelScope.launch {
            topicsRepository.toggleFollowedTopicId(followedTopicId, followed)
        }
    }

    fun followAuthor(followedAuthorId: String, followed: Boolean) {
        viewModelScope.launch {
            authorsRepository.toggleFollowedAuthorId(followedAuthorId, followed)
        }
    }

    fun switchTab(newIndex: Int) {
        if (newIndex != tabState.value.currentIndex) {
            _tabState.update {
                it.copy(currentIndex = newIndex)
            }
        }
    }
}

data class FollowingTabState(
    val titles: List<Int>,
    val currentIndex: Int
)

sealed interface FollowingUiState {
    object Loading : FollowingUiState

    data class Interests(
        val authors: List<FollowableAuthor>,
        val topics: List<FollowableTopic>
    ) : FollowingUiState

    object Empty : FollowingUiState
}
