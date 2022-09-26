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
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.google.samples.apps.nowinandroid.core.data.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.SyncStatusMonitor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.feature.foryou.FollowedInterestsUiState.FollowedInterests
import com.google.samples.apps.nowinandroid.feature.foryou.FollowedInterestsUiState.None
import com.google.samples.apps.nowinandroid.feature.foryou.FollowedInterestsUiState.Unknown
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ForYouViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    syncStatusMonitor: SyncStatusMonitor,
    authorsRepository: AuthorsRepository,
    topicsRepository: TopicsRepository,
    private val newsRepository: NewsRepository,
    private val userDataRepository: UserDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val followedInterestsUiState: StateFlow<FollowedInterestsUiState> =
        userDataRepository.userDataStream
            .map { userData ->
                if (userData.followedAuthors.isEmpty() && userData.followedTopics.isEmpty()) {
                    None
                } else {
                    FollowedInterests(
                        authorIds = userData.followedAuthors,
                        topicIds = userData.followedTopics
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Unknown
            )

    private val savedNewsResourcesState: StateFlow<Set<String>> =
        userDataRepository.userDataStream
            .map { userData ->
                userData.bookmarkedNewsResources
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    /**
     * The in-progress set of topics to be selected, persisted through process death with a
     * [SavedStateHandle].
     */
    private var inProgressTopicSelection by savedStateHandle.saveable {
        mutableStateOf<Set<String>>(emptySet())
    }

    /**
     * The in-progress set of authors to be selected, persisted through process death with a
     * [SavedStateHandle].
     */
    private var inProgressAuthorSelection by savedStateHandle.saveable {
        mutableStateOf<Set<String>>(emptySet())
    }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val isSyncing = syncStatusMonitor.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val feedState: StateFlow<NewsFeedUiState> =
        combine(
            followedInterestsUiState,
            snapshotFlow { inProgressTopicSelection },
            snapshotFlow { inProgressAuthorSelection }
        ) { followedInterestsUserState, inProgressTopicSelection, inProgressAuthorSelection ->
            when (followedInterestsUserState) {
                // If we don't know the current selection state, emit loading.
                Unknown -> flowOf<NewsFeedUiState>(NewsFeedUiState.Loading)
                // If the user has followed topics, use those followed topics to populate the feed
                is FollowedInterests -> {

                    newsRepository.getNewsResourcesStream(
                        filterTopicIds = followedInterestsUserState.topicIds,
                        filterAuthorIds = followedInterestsUserState.authorIds
                    ).mapToFeedState(savedNewsResourcesState)
                }
                // If the user hasn't followed interests yet, show a realtime populated feed based
                // on the in-progress interests selections, if there are any.
                None -> {
                    if (inProgressTopicSelection.isEmpty() && inProgressAuthorSelection.isEmpty()) {
                        flowOf<NewsFeedUiState>(NewsFeedUiState.Success(emptyList()))
                    } else {
                        newsRepository.getNewsResourcesStream(
                            filterTopicIds = inProgressTopicSelection,
                            filterAuthorIds = inProgressAuthorSelection
                        ).mapToFeedState(savedNewsResourcesState)
                    }
                }
            }
        }
            // Flatten the feed flows.
            // As the selected topics and topic state changes, this will cancel the old feed
            // monitoring and start the new one.
            .flatMapLatest { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NewsFeedUiState.Loading
            )

    val interestsSelectionState: StateFlow<ForYouInterestsSelectionUiState> =
        combine(
            followedInterestsUiState,
            topicsRepository.getTopicsStream(),
            authorsRepository.getAuthorsStream(),
            snapshotFlow { inProgressTopicSelection },
            snapshotFlow { inProgressAuthorSelection },
        ) { followedInterestsUserState, availableTopics, availableAuthors, inProgressTopicSelection,
            inProgressAuthorSelection ->

            when (followedInterestsUserState) {
                Unknown -> ForYouInterestsSelectionUiState.Loading
                is FollowedInterests -> ForYouInterestsSelectionUiState.NoInterestsSelection
                None -> {
                    val topics = availableTopics.map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = topic.id in inProgressTopicSelection
                        )
                    }
                    val authors = availableAuthors.map { author ->
                        FollowableAuthor(
                            author = author,
                            isFollowed = author.id in inProgressAuthorSelection
                        )
                    }

                    if (topics.isEmpty() && authors.isEmpty()) {
                        ForYouInterestsSelectionUiState.LoadFailed
                    } else {
                        ForYouInterestsSelectionUiState.WithInterestsSelection(
                            topics = topics,
                            authors = authors
                        )
                    }
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ForYouInterestsSelectionUiState.Loading
            )

    fun updateTopicSelection(topicId: String, isChecked: Boolean) {
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

    fun updateAuthorSelection(authorId: String, isChecked: Boolean) {
        withMutableSnapshot {
            inProgressAuthorSelection =
                // Update the in-progress selection based on whether the author id was checked
                if (isChecked) {
                    inProgressAuthorSelection + authorId
                } else {
                    inProgressAuthorSelection - authorId
                }
        }
    }

    fun updateNewsResourceSaved(newsResourceId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, isChecked)
        }
    }

    fun saveFollowedInterests() {
        // Don't attempt to save anything if nothing is selected
        if (inProgressTopicSelection.isEmpty() && inProgressAuthorSelection.isEmpty()) {
            return
        }

        viewModelScope.launch {
            userDataRepository.setFollowedTopicIds(inProgressTopicSelection)
            userDataRepository.setFollowedAuthorIds(inProgressAuthorSelection)
            // Clear out the old selection, in case we return to onboarding
            withMutableSnapshot {
                inProgressTopicSelection = emptySet()
                inProgressAuthorSelection = emptySet()
            }
        }
    }
}

private fun Flow<List<NewsResource>>.mapToFeedState(
    savedNewsResourcesState: Flow<Set<String>>
): Flow<NewsFeedUiState> =
    filterNot { it.isEmpty() }
        .combine(savedNewsResourcesState) { newsResources, savedNewsResources ->
            newsResources.map { newsResource ->
                SaveableNewsResource(
                    newsResource = newsResource,
                    isSaved = savedNewsResources.contains(newsResource.id)
                )
            }
        }
        .map<List<SaveableNewsResource>, NewsFeedUiState>(NewsFeedUiState::Success)
        .onStart { emit(NewsFeedUiState.Loading) }
