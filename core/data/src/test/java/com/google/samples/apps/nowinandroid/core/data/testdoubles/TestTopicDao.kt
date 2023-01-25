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

package com.google.samples.apps.nowinandroid.core.data.testdoubles

import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Test double for [TopicDao]
 */
class TestTopicDao : TopicDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            TopicEntity(
                id = "1",
                name = "Topic",
                shortDescription = "short description",
                longDescription = "long description",
                url = "URL",
                imageUrl = "image URL",
            ),
        ),
    )

    override fun getTopicEntity(topicId: String): Flow<TopicEntity> {
        throw NotImplementedError("Unused in tests")
    }

    override fun getTopicEntities(): Flow<List<TopicEntity>> =
        entitiesStateFlow

    override fun getTopicEntities(ids: Set<String>): Flow<List<TopicEntity>> =
        getTopicEntities()
            .map { topics -> topics.filter { it.id in ids } }

    override suspend fun insertOrIgnoreTopics(topicEntities: List<TopicEntity>): List<Long> {
        entitiesStateFlow.value = topicEntities
        // Assume no conflicts on insert
        return topicEntities.map { it.id.toLong() }
    }

    override suspend fun updateTopics(entities: List<TopicEntity>) {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun upsertTopics(entities: List<TopicEntity>) {
        entitiesStateFlow.value = entities
    }

    override suspend fun deleteTopics(ids: List<String>) {
        val idSet = ids.toSet()
        entitiesStateFlow.update { entities ->
            entities.filterNot { idSet.contains(it.id) }
        }
    }
}
