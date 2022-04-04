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

package com.google.samples.apps.nowinandroid.core.domain.testdoubles

import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.fake.FakeDataSource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkAuthor
import com.google.samples.apps.nowinandroid.core.network.model.NetworkChangeList
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Test double for [NiANetwork]
 */
class TestNiaNetwork : NiANetwork {

    private val networkJson = Json

    override suspend fun getTopics(ids: List<Int>?): List<NetworkTopic> =
        networkJson.decodeFromString<List<NetworkTopic>>(FakeDataSource.topicsData)
            .matchIds(
                ids = ids,
                idGetter = NetworkTopic::id
            )

    override suspend fun getAuthors(ids: List<Int>?): List<NetworkAuthor> =
        networkJson.decodeFromString<List<NetworkAuthor>>(FakeDataSource.authors)
            .matchIds(
                ids = ids,
                idGetter = NetworkAuthor::id
            )

    override suspend fun getNewsResources(ids: List<Int>?): List<NetworkNewsResource> =
        networkJson.decodeFromString<List<NetworkNewsResource>>(FakeDataSource.data)
            .matchIds(
                ids = ids,
                idGetter = NetworkNewsResource::id
            )

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        getTopics(ids = null).mapToChangeList(
            after = after,
            idGetter = NetworkTopic::id
        )

    override suspend fun getAuthorChangeList(after: Int?): List<NetworkChangeList> =
        getAuthors(ids = null).mapToChangeList(
            after = after,
            idGetter = NetworkAuthor::id
        )

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        getNewsResources(ids = null).mapToChangeList(
            after = after,
            idGetter = NetworkNewsResource::id
        )
}

/**
 * Return items from [this] whose id defined by [idGetter] is in [ids] if [ids] is not null
 */
private fun <T> List<T>.matchIds(
    ids: List<Int>?,
    idGetter: (T) -> Int
) = when (ids) {
    null -> this
    else -> ids.toSet().let { idSet -> this.filter { idSet.contains(idGetter(it)) } }
}

/**
 * Maps items to a change list where the change list version is denoted by the index of each item.
 * [after] simulates which models have changed by excluding items before it
 */
private fun <T> List<T>.mapToChangeList(
    after: Int?,
    idGetter: (T) -> Int
) = mapIndexed { index, item ->
    NetworkChangeList(
        id = idGetter(item),
        changeListVersion = index,
        isDelete = false,
    )
}.drop(after ?: 0)
