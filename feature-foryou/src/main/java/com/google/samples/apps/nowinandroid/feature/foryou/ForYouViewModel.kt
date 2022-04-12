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
import com.google.samples.apps.nowinandroid.core.domain.combine
import com.google.samples.apps.nowinandroid.core.domain.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.domain.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.domain.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
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
    private val authorsRepository: AuthorsRepository,
    private val topicsRepository: TopicsRepository,
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val followedInterestsStateFlow =
        combine(
            authorsRepository.getFollowedAuthorIdsStream(),
            topicsRepository.getFollowedTopicIdsStream(),
        ) { followedAuthors, followedTopics ->
            if (followedAuthors.isEmpty() && followedTopics.isEmpty()) {
                FollowedInterestsState.None
            } else {
                FollowedInterestsState.FollowedInterests(
                    authorIds = followedAuthors,
                    topicIds = followedTopics
                )
            }
        }
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FollowedInterestsState.Unknown
            )

    /**
     * TODO: Temporary saving of news resources persisted through process death with a
     *       [SavedStateHandle].
     *
     * This should be persisted to disk instead.
     */
    private var savedNewsResources by savedStateHandle.saveable {
        mutableStateOf<Set<Int>>(emptySet())
    }

    /**
     * The in-progress set of topics to be selected, persisted through process death with a
     * [SavedStateHandle].
     */
    private var inProgressTopicSelection by savedStateHandle.saveable {
        mutableStateOf<Set<Int>>(emptySet())
    }

    /**
     * The in-progress set of authors to be selected, persisted through process death with a
     * [SavedStateHandle].
     */
    private var inProgressAuthorSelection by savedStateHandle.saveable {
        mutableStateOf<Set<Int>>(emptySet())
    }

    val uiState: StateFlow<ForYouFeedUiState> = combine(
        followedInterestsStateFlow,
        topicsRepository.getTopicsStream(),
        snapshotFlow { inProgressTopicSelection },
        authorsRepository.getAuthorsStream(),
        snapshotFlow { inProgressAuthorSelection },
        snapshotFlow { savedNewsResources }
    ) { followedInterestsUserState, availableTopics, inProgressTopicSelection,
        availableAuthors, inProgressAuthorSelection, savedNewsResources ->

        fun mapToSaveableFeed(feed: List<NewsResource>): List<SaveableNewsResource> =
            feed.map { newsResource ->
                SaveableNewsResource(
                    newsResource = newsResource,
                    isSaved = savedNewsResources.contains(newsResource.id)
                )
            }

        when (followedInterestsUserState) {
            // If we don't know the current selection state, just emit loading.
            FollowedInterestsState.Unknown -> flowOf<ForYouFeedUiState>(ForYouFeedUiState.Loading)
            // If the user has followed topics, use those followed topics to populate the feed
            is FollowedInterestsState.FollowedInterests -> {
                newsRepository.getNewsResourcesStream(
                    filterTopicIds = followedInterestsUserState.topicIds,
                    filterAuthorIds = followedInterestsUserState.authorIds
                )
                    .map(::mapToSaveableFeed)
                    .map { feed ->
                        ForYouFeedUiState.PopulatedFeed.FeedWithoutTopicSelection(
                            feed = feed
                        )
                    }
            }
            // If the user hasn't followed topics yet, show the topic selection, as well as a
            // realtime populated feed based on those in-progress topic selections.
            FollowedInterestsState.None -> {
                newsRepository.getNewsResourcesStream(
                    filterTopicIds = inProgressTopicSelection,
                    filterAuthorIds = inProgressAuthorSelection
                )
                    .map(::mapToSaveableFeed)
                    .map { feed ->
                        ForYouFeedUiState.PopulatedFeed.FeedWithInterestsSelection(
                            topics = availableTopics.map { topic ->
                                FollowableTopic(
                                    topic = topic,
                                    isFollowed = topic.id in inProgressTopicSelection
                                )
                            },
                            authors = availableAuthors.map { author ->
                                FollowableAuthor(
                                    author = author,
                                    isFollowed = author.id in inProgressAuthorSelection
                                )
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

    fun updateAuthorSelection(authorId: Int, isChecked: Boolean) {
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

    fun updateNewsResourceSaved(newsResourceId: Int, isChecked: Boolean) {
        withMutableSnapshot {
            savedNewsResources =
                if (isChecked) {
                    savedNewsResources + newsResourceId
                } else {
                    savedNewsResources - newsResourceId
                }
        }
    }

    fun saveFollowedInterests() {
        // Don't attempt to save anything if nothing is selected
        if (inProgressTopicSelection.isEmpty() && inProgressAuthorSelection.isEmpty()) {
            return
        }

        viewModelScope.launch {
            topicsRepository.setFollowedTopicIds(inProgressTopicSelection)
            authorsRepository.setFollowedAuthorIds(inProgressAuthorSelection)
            // Clear out the old selection, in case we return to onboarding
            withMutableSnapshot {
                inProgressTopicSelection = emptySet()
                inProgressAuthorSelection = emptySet()
            }
        }
    }
}

/**
 * A sealed hierarchy for the user's current followed interests state.
 */
private sealed interface FollowedInterestsState {

    /**
     * The current state is unknown (hasn't loaded yet)
     */
    object Unknown : FollowedInterestsState

    /**
     * The user hasn't followed any interests yet.
     */
    object None : FollowedInterestsState

    /**
     * The user has followed the given (non-empty) set of [topicIds] or [authorIds].
     */
    data class FollowedInterests(
        val topicIds: Set<Int>,
        val authorIds: Set<Int>
    ) : FollowedInterestsState
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
        val feed: List<SaveableNewsResource>

        /**
         * The feed, along with a list of interests that can be selected.
         */
        data class FeedWithInterestsSelection(
            val topics: List<FollowableTopic>,
            val authors: List<FollowableAuthor>,
            override val feed: List<SaveableNewsResource>
        ) : PopulatedFeed {
            val canSaveInterests: Boolean =
                topics.any { it.isFollowed } || authors.any { it.isFollowed }
        }

        /**
         * Just the feed.
         */
        data class FeedWithoutTopicSelection(
            override val feed: List<SaveableNewsResource>
        ) : PopulatedFeed
    }
}
