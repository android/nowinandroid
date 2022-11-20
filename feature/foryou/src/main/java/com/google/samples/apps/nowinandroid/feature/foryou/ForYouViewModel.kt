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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.util.SyncStatusMonitor
import com.google.samples.apps.nowinandroid.core.domain.GetFollowableTopicsStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.GetSaveableNewsResourcesStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.GetSortedFollowableAuthorsStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.stateInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class ForYouViewModel @Inject constructor(
    syncStatusMonitor: SyncStatusMonitor,
    private val userDataRepository: UserDataRepository,
    private val getSaveableNewsResourcesStream: GetSaveableNewsResourcesStreamUseCase,
    getSortedFollowableAuthorsStream: GetSortedFollowableAuthorsStreamUseCase,
    getFollowableTopicsStream: GetFollowableTopicsStreamUseCase
) : ViewModel() {

    private val shouldShowOnboarding: Flow<Boolean> =
        userDataRepository.userDataStream.map { !it.shouldHideOnboarding }

    val isSyncing = syncStatusMonitor
        .isSyncing
        .stateInViewModelScope(viewModelScope, initialValue = false)

    val feedState: StateFlow<NewsFeedUiState> =
        userDataRepository.userDataStream
            .map { userData ->
                // If the user hasn't completed the onboarding and hasn't selected any interests
                // show an empty news list to clearly demonstrate that their selections affect the
                // news articles they will see.
                if (!userData.shouldHideOnboarding &&
                    userData.followedAuthors.isEmpty() &&
                    userData.followedTopics.isEmpty()
                ) {
                    flowOf(NewsFeedUiState.Success(emptyList()))
                } else {
                    getSaveableNewsResourcesStream(
                        filterTopicIds = userData.followedTopics,
                        filterAuthorIds = userData.followedAuthors
                    ).mapToFeedState()
                }
            }
            // Flatten the feed flows.
            // As the selected topics and topic state changes, this will cancel the old feed
            // monitoring and start the new one.
            .flatMapLatest { it }
            .stateInViewModelScope(viewModelScope, initialValue = NewsFeedUiState.Loading)

    val onboardingUiState: StateFlow<OnboardingUiState> =
        combine(
            shouldShowOnboarding,
            getFollowableTopicsStream(),
            getSortedFollowableAuthorsStream()
        ) { shouldShowOnboarding, topics, authors ->
            if (shouldShowOnboarding) {
                OnboardingUiState.Shown(
                    topics = topics,
                    authors = authors
                )
            } else {
                OnboardingUiState.NotShown
            }
        }.stateInViewModelScope(viewModelScope, initialValue = OnboardingUiState.Loading)

    fun updateTopicSelection(topicId: String, isChecked: Boolean) = viewModelScope.launch {
        userDataRepository.toggleFollowedTopicId(topicId, isChecked)
    }

    fun updateAuthorSelection(authorId: String, isChecked: Boolean) = viewModelScope.launch {
        userDataRepository.toggleFollowedAuthorId(authorId, isChecked)
    }

    fun updateNewsResourceSaved(newsResourceId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, isChecked)
        }
    }

    fun dismissOnboarding() = viewModelScope.launch {
        userDataRepository.setShouldHideOnboarding(true)
    }
}

private fun Flow<List<SaveableNewsResource>>.mapToFeedState(): Flow<NewsFeedUiState> =
    map<List<SaveableNewsResource>, NewsFeedUiState>(NewsFeedUiState::Success)
        .onStart { emit(NewsFeedUiState.Loading) }
