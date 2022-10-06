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

package com.google.samples.apps.nowinandroid.feature.author

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.decoder.StringDecoder
import com.google.samples.apps.nowinandroid.core.domain.GetSaveableNewsResourcesStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.result.Result
import com.google.samples.apps.nowinandroid.core.result.asResult
import com.google.samples.apps.nowinandroid.feature.author.navigation.AuthorArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AuthorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stringDecoder: StringDecoder,
    private val userDataRepository: UserDataRepository,
    authorsRepository: AuthorsRepository,
    getSaveableNewsResourcesStream: GetSaveableNewsResourcesStreamUseCase
) : ViewModel() {

    private val authorArgs: AuthorArgs = AuthorArgs(savedStateHandle, stringDecoder)

    val authorUiState: StateFlow<AuthorUiState> = authorUiStateStream(
        authorId = authorArgs.authorId,
        userDataRepository = userDataRepository,
        authorsRepository = authorsRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthorUiState.Loading
        )

    val newsUiState: StateFlow<NewsUiState> =
        getSaveableNewsResourcesStream.newsUiStateStream(authorId = authorArgs.authorId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NewsUiState.Loading
            )

    fun followAuthorToggle(followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedAuthorId(authorArgs.authorId, followed)
        }
    }

    fun bookmarkNews(newsResourceId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, bookmarked)
        }
    }
}

private fun authorUiStateStream(
    authorId: String,
    userDataRepository: UserDataRepository,
    authorsRepository: AuthorsRepository,
): Flow<AuthorUiState> {
    // Observe the followed authors, as they could change over time.
    val followedAuthorIdsStream: Flow<Set<String>> =
        userDataRepository.userDataStream
            .map { it.followedAuthors }

    // Observe author information
    val authorStream: Flow<Author> = authorsRepository.getAuthorStream(
        id = authorId
    )

    return combine(
        followedAuthorIdsStream,
        authorStream,
        ::Pair
    )
        .asResult()
        .map { followedAuthorToAuthorResult ->
            when (followedAuthorToAuthorResult) {
                is Result.Success -> {
                    val (followedAuthors, author) = followedAuthorToAuthorResult.data
                    val followed = followedAuthors.contains(authorId)
                    AuthorUiState.Success(
                        followableAuthor = FollowableAuthor(
                            author = author,
                            isFollowed = followed
                        )
                    )
                }
                is Result.Loading -> {
                    AuthorUiState.Loading
                }
                is Result.Error -> {
                    AuthorUiState.Error
                }
            }
        }
}

private fun GetSaveableNewsResourcesStreamUseCase.newsUiStateStream(
    authorId: String
): Flow<NewsUiState> {
    // Observe news
    return this(
        filterAuthorIds = setOf(element = authorId)
    ).asResult()
        .map { newsResult ->
            when (newsResult) {
                is Result.Success -> NewsUiState.Success(newsResult.data)
                is Result.Loading -> NewsUiState.Loading
                is Result.Error -> NewsUiState.Error
            }
        }
}

sealed interface AuthorUiState {
    data class Success(val followableAuthor: FollowableAuthor) : AuthorUiState
    object Error : AuthorUiState
    object Loading : AuthorUiState
}

sealed interface NewsUiState {
    data class Success(val news: List<SaveableNewsResource>) : NewsUiState
    object Error : NewsUiState
    object Loading : NewsUiState
}
