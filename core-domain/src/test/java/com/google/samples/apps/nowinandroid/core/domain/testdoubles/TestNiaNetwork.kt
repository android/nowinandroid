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

enum class CollectionType {
    Topics,
    Authors,
    Episodes,
    NewsResources
}

/**
 * Test double for [NiANetwork]
 */
class TestNiaNetwork : NiANetwork {

    private val networkJson = Json

    private val allTopics =
        networkJson.decodeFromString<List<NetworkTopic>>(FakeDataSource.topicsData)

    private val allAuthors =
        networkJson.decodeFromString<List<NetworkAuthor>>(FakeDataSource.authors)

    private val allNewsResources =
        networkJson.decodeFromString<List<NetworkNewsResource>>(FakeDataSource.data)

    private val changeLists: MutableMap<CollectionType, List<NetworkChangeList>> = mutableMapOf(
        CollectionType.Topics to allTopics
            .mapToChangeList(idGetter = NetworkTopic::id),
        CollectionType.Authors to allAuthors
            .mapToChangeList(idGetter = NetworkAuthor::id),
        CollectionType.Episodes to listOf(),
        CollectionType.NewsResources to allNewsResources
            .mapToChangeList(idGetter = NetworkNewsResource::id),
    )

    override suspend fun getTopics(ids: List<Int>?): List<NetworkTopic> =
        allTopics.matchIds(
            ids = ids,
            idGetter = NetworkTopic::id
        )

    override suspend fun getAuthors(ids: List<Int>?): List<NetworkAuthor> =
        allAuthors.matchIds(
            ids = ids,
            idGetter = NetworkAuthor::id
        )

    override suspend fun getNewsResources(ids: List<Int>?): List<NetworkNewsResource> =
        allNewsResources.matchIds(
            ids = ids,
            idGetter = NetworkNewsResource::id
        )

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        changeLists.getValue(CollectionType.Topics).after(after)

    override suspend fun getAuthorChangeList(after: Int?): List<NetworkChangeList> =
        changeLists.getValue(CollectionType.Authors).after(after)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        changeLists.getValue(CollectionType.NewsResources).after(after)

    fun latestChangeListVersion(collectionType: CollectionType) =
        changeLists.getValue(collectionType).last().changeListVersion

    fun changeListsAfter(collectionType: CollectionType, version: Int) =
        changeLists.getValue(collectionType).after(version)

    /**
     * Edits the change list for the backing [collectionType] for the given [id] mimicking
     * the server's change list registry
     */
    fun editCollection(collectionType: CollectionType, id: Int, isDelete: Boolean) {
        val changeList = changeLists.getValue(collectionType)
        val latestVersion = changeList.lastOrNull()?.changeListVersion ?: 0
        val change = NetworkChangeList(
            id = id,
            isDelete = isDelete,
            changeListVersion = latestVersion + 1,
        )
        changeLists[collectionType] = changeList.filterNot { it.id == id } + change
    }
}

fun List<NetworkChangeList>.after(version: Int?): List<NetworkChangeList> =
    when (version) {
        null -> this
        else -> this.filter { it.changeListVersion > version }
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
    idGetter: (T) -> Int
) = mapIndexed { index, item ->
    NetworkChangeList(
        id = idGetter(item),
        changeListVersion = index + 1,
        isDelete = false,
    )
}
