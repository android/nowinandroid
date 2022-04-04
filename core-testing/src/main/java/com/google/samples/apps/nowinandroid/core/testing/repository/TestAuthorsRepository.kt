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

import com.google.samples.apps.nowinandroid.core.domain.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.model.data.Author
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestAuthorsRepository : AuthorsRepository {
    /**
     * The backing hot flow for the list of followed author ids for testing.
     */
    private val _followedAuthorIds: MutableSharedFlow<Set<Int>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * The backing hot flow for the list of author ids for testing.
     */
    private val authorsFlow: MutableSharedFlow<List<Author>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getAuthorsStream(): Flow<List<Author>> = authorsFlow

    override fun getFollowedAuthorIdsStream(): Flow<Set<Int>> = _followedAuthorIds

    override suspend fun setFollowedAuthorIds(followedAuthorIds: Set<Int>) {
        _followedAuthorIds.tryEmit(followedAuthorIds)
    }

    override suspend fun toggleFollowedAuthorId(followedAuthorId: Int, followed: Boolean) {
        getCurrentFollowedAuthors()?.let { current ->
            _followedAuthorIds.tryEmit(
                if (followed) current.plus(followedAuthorId)
                else current.minus(followedAuthorId)
            )
        }
    }

    override suspend fun sync(): Boolean = true

    /**
     * A test-only API to allow controlling the list of topics from tests.
     */
    fun sendAuthors(authors: List<Author>) {
        authorsFlow.tryEmit(authors)
    }

    /**
     * A test-only API to allow querying the current followed topics.
     */
    fun getCurrentFollowedAuthors(): Set<Int>? = _followedAuthorIds.replayCache.firstOrNull()
}
