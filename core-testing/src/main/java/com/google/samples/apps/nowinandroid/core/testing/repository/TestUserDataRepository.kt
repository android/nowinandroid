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

package com.google.samples.apps.nowinandroid.core.testing.repository

import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.model.data.UserData
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

private val emptyUserData = UserData(
    bookmarkedNewsResources = emptySet(),
    followedTopics = emptySet(),
    followedAuthors = emptySet()
)

class TestUserDataRepository : UserDataRepository {
    /**
     * The backing hot flow for the list of followed topic ids for testing.
     */
    private val _userData = MutableSharedFlow<UserData>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val currentUserData get() = _userData.replayCache.firstOrNull() ?: emptyUserData

    override val userDataStream: Flow<UserData> = _userData.filterNotNull()

    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) {
        _userData.tryEmit(currentUserData.copy(followedTopics = followedTopicIds))
    }

    override suspend fun toggleFollowedTopicId(followedTopicId: String, followed: Boolean) {
        currentUserData.let { current ->
            val followedTopics = if (followed) current.followedTopics + followedTopicId
            else current.followedTopics - followedTopicId

            _userData.tryEmit(current.copy(followedTopics = followedTopics))
        }
    }

    override suspend fun setFollowedAuthorIds(followedAuthorIds: Set<String>) {
        _userData.tryEmit(currentUserData.copy(followedAuthors = followedAuthorIds))
    }

    override suspend fun toggleFollowedAuthorId(followedAuthorId: String, followed: Boolean) {
        currentUserData.let { current ->
            val followedAuthors = if (followed) current.followedAuthors + followedAuthorId
            else current.followedAuthors - followedAuthorId

            _userData.tryEmit(current.copy(followedAuthors = followedAuthors))
        }
    }

    override suspend fun updateNewsResourceBookmark(newsResourceId: String, bookmarked: Boolean) {
        currentUserData.let { current ->
            val bookmarkedNews = if (bookmarked) current.bookmarkedNewsResources + newsResourceId
            else current.bookmarkedNewsResources - newsResourceId

            _userData.tryEmit(current.copy(bookmarkedNewsResources = bookmarkedNews))
        }
    }

    /**
     * A test-only API to allow querying the current followed topics.
     */
    fun getCurrentFollowedTopics(): Set<String>? =
        _userData.replayCache.firstOrNull()?.followedTopics

    /**
     * A test-only API to allow querying the current followed authors.
     */
    fun getCurrentFollowedAuthors(): Set<String>? =
        _userData.replayCache.firstOrNull()?.followedAuthors
}
