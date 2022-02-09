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

package com.google.samples.apps.nowinandroid.data.fake

import com.google.samples.apps.nowinandroid.data.NiaPreferences
import com.google.samples.apps.nowinandroid.data.model.Topic
import com.google.samples.apps.nowinandroid.data.network.NetworkTopic
import com.google.samples.apps.nowinandroid.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.di.NiaDispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FakeTopicsRepository @Inject constructor(
    private val dispatchers: NiaDispatchers,
    private val networkJson: Json,
    private val niaPreferences: NiaPreferences
) : TopicsRepository {
    override fun getTopicsStream(): Flow<List<Topic>> = flow<List<Topic>> {
        emit(
            networkJson.decodeFromString<List<NetworkTopic>>(FakeDataSource.topicsData).map {
                Topic(
                    id = it.id,
                    name = it.name,
                    description = it.description
                )
            }
        )
    }
        .flowOn(dispatchers.IO)

    override suspend fun setFollowedTopicIds(followedTopicIds: Set<Int>) =
        niaPreferences.setFollowedTopicIds(followedTopicIds)

    override suspend fun toggleFollowedTopicId(followedTopicId: Int, followed: Boolean) =
        niaPreferences.toggleFollowedTopicId(followedTopicId, followed)

    override fun getFollowedTopicIdsStream() = niaPreferences.followedTopicIds
}
