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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.repository.NewsResourceQuery
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.domain.GetFollowableTopicsUseCase
import com.google.samples.apps.nowinandroid.core.domain.TopicSortField
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.model.data.UserNewsResource
import com.google.samples.apps.nowinandroid.feature.interests.navigation.topicIdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(
    val userDataRepository: UserDataRepository,
    getFollowableTopics: GetFollowableTopicsUseCase,
    userNewsResourceRepository: UserNewsResourceRepository,
    topicsRepository: TopicsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val topicId: StateFlow<String?> =
        savedStateHandle.getStateFlow(topicIdArg, null)

    val interestsUiState: StateFlow<InterestsUiState> = combine(
        getFollowableTopics(sortBy = TopicSortField.NAME),
        topicId,
    ) { topics, selectedTopicId ->
        InterestsUiState.Interests(
            topics = topics,
            selectedTopicId = selectedTopicId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = InterestsUiState.Loading,
    )

    val topicUiState: StateFlow<TopicUiState?> = topicId.flatMapLatest { topicId ->
        topicUiState(
            topicId,
            userDataRepository,
            userNewsResourceRepository,
            topicsRepository,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    fun followTopic(followedTopicId: String, isFollowed: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedTopicId(followedTopicId, isFollowed)
        }
    }

    fun bookmarkNews(newsResourceId: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, isBookmarked)
        }
    }

    fun newsViewed(newsResourceId: String) {
        viewModelScope.launch {
            userDataRepository.setNewsResourceViewed(newsResourceId, true)
        }
    }
}

private fun topicUiState(
    topicId: String?,
    userDataRepository: UserDataRepository,
    userNewsResourceRepository: UserNewsResourceRepository,
    topicsRepository: TopicsRepository,
): Flow<TopicUiState?> {
    if (topicId == null) {
        return flowOf(null)
    }

    // Observe the followed topics, as they could change over time.
    val followedTopicIds: Flow<Set<String>> =
        userDataRepository.userData
            .map { it.followedTopics }

    // Observe topic information
    val topicStream: Flow<Topic> = topicsRepository.getTopic(id = topicId)

    val newsResourcesStream: Flow<List<UserNewsResource>> = userNewsResourceRepository.observeAll(
        NewsResourceQuery(filterTopicIds = setOf(element = topicId)),
    )

    return combine<_, _, _, TopicUiState>(
        followedTopicIds,
        topicStream,
        newsResourcesStream,
    ) { followedTopics, topic, newsResources ->
        val followed = followedTopics.contains(topicId)
        TopicUiState.Success(
            followableTopic = FollowableTopic(
                topic = topic,
                isFollowed = followed,
            ),
            newsResources = newsResources,
        )
    }.onStart {
        emit(TopicUiState.Loading)
    }.catch {
        emit(TopicUiState.Error)
    }
}
