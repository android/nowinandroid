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

import com.google.samples.apps.nowinandroid.core.domain.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestTopicsRepository : TopicsRepository {
    /**
     * The backing hot flow for the list of followed topic ids for testing.
     */
    private val _followedTopicIds: MutableSharedFlow<Set<Int>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * The backing hot flow for the list of topics ids for testing.
     */
    private val topicsFlow: MutableSharedFlow<List<Topic>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getTopicsStream(): Flow<List<Topic>> = topicsFlow

    override suspend fun setFollowedTopicIds(followedTopicIds: Set<Int>) {
        _followedTopicIds.tryEmit(followedTopicIds)
    }

    override suspend fun toggleFollowedTopicId(followedTopicId: Int, followed: Boolean) {
        getCurrentFollowedTopics()?.let { current ->
            _followedTopicIds.tryEmit(
                if (followed) current.plus(followedTopicId)
                else current.minus(followedTopicId)
            )
        }
    }

    override fun getFollowedTopicIdsStream(): Flow<Set<Int>> = _followedTopicIds

    /**
     * A test-only API to allow controlling the list of topics from tests.
     */
    fun sendTopics(topics: List<Topic>) {
        topicsFlow.tryEmit(topics)
    }

    /**
     * A test-only API to allow querying the current followed topics.
     */
    fun getCurrentFollowedTopics(): Set<Int>? = _followedTopicIds.replayCache.firstOrNull()

    override suspend fun sync(): Boolean = true
}
