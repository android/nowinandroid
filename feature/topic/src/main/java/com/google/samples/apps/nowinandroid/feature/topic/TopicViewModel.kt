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

package com.google.samples.apps.nowinandroid.feature.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.decoder.StringDecoder
import com.google.samples.apps.nowinandroid.core.domain.GetSaveableNewsResourcesStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.result.Result
import com.google.samples.apps.nowinandroid.core.result.Result.Error
import com.google.samples.apps.nowinandroid.core.result.Result.Loading
import com.google.samples.apps.nowinandroid.core.result.Result.Success
import com.google.samples.apps.nowinandroid.core.result.asResult
import com.google.samples.apps.nowinandroid.core.ui.stateInScope
import com.google.samples.apps.nowinandroid.feature.topic.navigation.TopicArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
    private val userDataRepository: UserDataRepository,
    topicsRepository: TopicsRepository,
    getSaveableNewsResourcesStream: GetSaveableNewsResourcesStreamUseCase
) : ViewModel() {

    private val topicArgs: TopicArgs = TopicArgs(savedStateHandle, stringDecoder)

    val topicUiState: StateFlow<TopicUiState> = topicUiStateStream(
        topicId = topicArgs.topicId,
        userDataRepository = userDataRepository,
        topicsRepository = topicsRepository
    ).stateInScope(viewModelScope, initialValue = TopicUiState.Loading)

    val newUiState: StateFlow<NewsUiState> = newsUiStateStream(
        topicId = topicArgs.topicId,
        userDataRepository = userDataRepository,
        getSaveableNewsResourcesStream = getSaveableNewsResourcesStream
    ).stateInScope(viewModelScope, initialValue = NewsUiState.Loading)

    fun followTopicToggle(followed: Boolean) = viewModelScope.launch {
        userDataRepository.toggleFollowedTopicId(topicArgs.topicId, followed)
    }

    fun bookmarkNews(newsResourceId: String, bookmarked: Boolean) = viewModelScope.launch {
        userDataRepository.updateNewsResourceBookmark(newsResourceId, bookmarked)
    }
}

private fun topicUiStateStream(
    topicId: String,
    userDataRepository: UserDataRepository,
    topicsRepository: TopicsRepository,
): Flow<TopicUiState> {
    // Observe the followed topics, as they could change over time.
    val followedTopicIdsStream: Flow<Set<String>> = userDataRepository
        .userDataStream
        .map { it.followedTopics }

    // Observe topic information
    val topicStream: Flow<Topic> = topicsRepository.getTopic(
        id = topicId
    )

    return combine(
        followedTopicIdsStream,
        topicStream,
        ::Pair
    )
        .asResult()
        .map { followedTopicToTopicResult ->
            handleTopicResult(followedTopicToTopicResult, topicId)
        }
}

private fun handleTopicResult(
    followedTopicToTopicResult: Result<Pair<Set<String>, Topic>>,
    topicId: String
) = when (followedTopicToTopicResult) {
    is Success -> {
        val (followedTopics, topic) = followedTopicToTopicResult.data
        val followed = followedTopics.contains(topicId)
        TopicUiState.Success(
            followableTopic = FollowableTopic(
                topic = topic,
                isFollowed = followed
            )
        )
    }
    is Loading -> TopicUiState.Loading
    is Error -> TopicUiState.Error
}

private fun newsUiStateStream(
    topicId: String,
    getSaveableNewsResourcesStream: GetSaveableNewsResourcesStreamUseCase,
    userDataRepository: UserDataRepository,
): Flow<NewsUiState> {
    // Observe news
    val newsStream: Flow<List<SaveableNewsResource>> = getSaveableNewsResourcesStream(
        filterAuthorIds = emptySet(),
        filterTopicIds = setOf(element = topicId),
    )

    // Observe bookmarks
    val bookmarkStream: Flow<Set<String>> = userDataRepository.userDataStream
        .map { it.bookmarkedNewsResources }

    return combine(
        newsStream,
        bookmarkStream,
        ::Pair
    )
        .asResult()
        .map { newsToBookmarksResult ->
            when (newsToBookmarksResult) {
                is Success -> {
                    val (news, bookmarks) = newsToBookmarksResult.data
                    NewsUiState.Success(news)
                }
                is Loading -> NewsUiState.Loading
                is Error -> NewsUiState.Error
            }
        }
}

sealed interface TopicUiState {
    data class Success(val followableTopic: FollowableTopic) : TopicUiState
    object Error : TopicUiState
    object Loading : TopicUiState
}

sealed interface NewsUiState {
    data class Success(val news: List<SaveableNewsResource>) : NewsUiState
    object Error : NewsUiState
    object Loading : NewsUiState
}
