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
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.domain.GetFollowableTopicsStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.GetSortedFollowableAuthorsStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.TopicSortField
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.ui.stateInScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class InterestsViewModel @Inject constructor(
    val userDataRepository: UserDataRepository,
    getFollowableTopicsStream: GetFollowableTopicsStreamUseCase,
    getSortedFollowableAuthorsStream: GetSortedFollowableAuthorsStreamUseCase
) : ViewModel() {

    private val _tabState = MutableStateFlow(
        InterestsTabState(
            titles = listOf(R.string.interests_topics, R.string.interests_people),
            currentIndex = 0
        )
    )
    val tabState: StateFlow<InterestsTabState> = _tabState.asStateFlow()

    val uiState: StateFlow<InterestsUiState> = combine(
        getSortedFollowableAuthorsStream(),
        getFollowableTopicsStream(sortBy = TopicSortField.NAME),
        InterestsUiState::Interests
    ).stateInScope(viewModelScope, initialValue = InterestsUiState.Loading)

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
