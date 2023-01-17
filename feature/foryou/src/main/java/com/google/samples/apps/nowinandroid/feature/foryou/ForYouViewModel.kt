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
import com.google.samples.apps.nowinandroid.core.data.repository.NewsResourceQuery
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.util.SyncStatusMonitor
import com.google.samples.apps.nowinandroid.core.domain.GetFollowableTopicsUseCase
import com.google.samples.apps.nowinandroid.core.domain.GetUserNewsResourcesUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ForYouViewModel @Inject constructor(
    syncStatusMonitor: SyncStatusMonitor,
    private val userDataRepository: UserDataRepository,
    getUserNewsResources: GetUserNewsResourcesUseCase,
    getFollowableTopics: GetFollowableTopicsUseCase
) : ViewModel() {

    private val userData = userDataRepository.userData
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    private val shouldShowOnboarding: Flow<Boolean> =
        userData.map { !it.shouldHideOnboarding }

    val isSyncing = syncStatusMonitor.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val onboardingItems =
        combine(
            shouldShowOnboarding,
            getFollowableTopics()
        ) { shouldShowOnboarding, topics ->
            if (shouldShowOnboarding) OnboardingUiState.Shown(topics = topics)
            else OnboardingUiState.NotShown
        }
            .map { onboardingUiState ->
                // Add onboarding item if it should show
                if (onboardingUiState is OnboardingUiState.NotShown) emptyList()
                else listOf<ForYouItem>(ForYouItem.OnBoarding(onboardingUiState))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf(
                    ForYouItem.OnBoarding(OnboardingUiState.Loading),
                )
            )

    private val newsItems = userData
        .flatMapLatest { userData ->
            // If the user hasn't completed the onboarding and hasn't selected any interests
            // show an empty news list to clearly demonstrate that their selections affect the
            // news articles they will see.
            when {
                !userData.shouldHideOnboarding && userData.followedTopics.isEmpty() -> flowOf(
                    emptyList()
                )
                else -> getUserNewsResources(
                    NewsResourceQuery(filterTopicIds = userData.followedTopics)
                )
            }
        }
        .map { userNewsResources ->
            userNewsResources.map<UserNewsResource, ForYouItem>(ForYouItem.News::Loaded)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf(
                ForYouItem.News.Loading,
            )
        )

    val forYouItems: StateFlow<List<ForYouItem>> =
        combine(
            onboardingItems,
            newsItems,
            List<ForYouItem>::plus
        )
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf(
                    ForYouItem.OnBoarding(OnboardingUiState.Loading),
                    ForYouItem.News.Loading,
                )
            )

    fun updateTopicSelection(topicId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedTopicId(topicId, isChecked)
        }
    }

    fun updateNewsResourceSaved(newsResourceId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, isChecked)
        }
    }

    fun dismissOnboarding() {
        viewModelScope.launch {
            userDataRepository.setShouldHideOnboarding(true)
        }
    }
}
