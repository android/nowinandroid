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

package com.google.samples.apps.nowinandroid.feature.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
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
class InterestsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    authorsRepository: AuthorsRepository,
    topicsRepository: TopicsRepository
) : ViewModel() {

    private val _tabState = MutableStateFlow(
        InterestsTabState(
            titles = listOf(R.string.interests_topics, R.string.interests_people),
            currentIndex = 0
        )
    )
    val tabState: StateFlow<InterestsTabState> = _tabState.asStateFlow()

    val uiState: StateFlow<InterestsUiState> = combine(
        userDataRepository.userDataStream,
        authorsRepository.getAuthorsStream(),
        topicsRepository.getTopicsStream(),
    ) { userData, availableAuthors, availableTopics ->

        InterestsUiState.Interests(
            authors = availableAuthors
                .map { author ->
                    FollowableAuthor(
                        author = author,
                        isFollowed = author.id in userData.followedAuthors
                    )
                }
                .sortedBy { it.author.name },
            topics = availableTopics
                .map { topic ->
                    FollowableTopic(
                        topic = topic,
                        isFollowed = topic.id in userData.followedTopics
                    )
                }
                .sortedBy { it.topic.name }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InterestsUiState.Loading
        )

    fun followTopic(followedTopicId: String, followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedTopicId(followedTopicId, followed)
        }
    }

    fun followAuthor(followedAuthorId: String, followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedAuthorId(followedAuthorId, followed)
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

data class InterestsTabState(
    val titles: List<Int>,
    val currentIndex: Int
)

sealed interface InterestsUiState {
    object Loading : InterestsUiState

    data class Interests(
        val authors: List<FollowableAuthor>,
        val topics: List<FollowableTopic>
    ) : InterestsUiState

    object Empty : InterestsUiState
}
