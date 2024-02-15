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

package com.google.samples.apps.nowinandroid.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DAO for [TopicEntity] access
 */

class TopicDao(db: NiaDatabase, private val dispatcher: CoroutineDispatcher) {

    private val query = db.topicsQueries

    fun getTopicEntity(topicId: String): Flow<TopicEntity> {
        return query.getTopicEntity(topicId) { id, name, shortDescription, longDescription, url, imageUrl ->
            TopicEntity(
                id = id,
                name = name,
                shortDescription = shortDescription,
                longDescription = longDescription,
                url = url,
                imageUrl = imageUrl,
            )
        }
            .asFlow()
            .mapToOne(dispatcher)
    }

    fun getTopicEntities(): Flow<List<TopicEntity>> {
        return query.getOneOffTopicEntities { id, name, shortDescription, longDescription, url, imageUrl ->
            TopicEntity(
                id = id,
                name = name,
                shortDescription = shortDescription,
                longDescription = longDescription,
                url = url,
                imageUrl = imageUrl,
            )
        }
            .asFlow()
            .mapToList(dispatcher)
    }

    suspend fun getOneOffTopicEntities(): List<TopicEntity> {
        // TODO: Use flow?
        return query.getOneOffTopicEntities { id, name, shortDescription, longDescription, url, imageUrl ->
            TopicEntity(
                id = id,
                name = name,
                shortDescription = shortDescription,
                longDescription = longDescription,
                url = url,
                imageUrl = imageUrl,
            )
        }.executeAsList()
    }

    fun getTopicEntities(ids: Set<String>): Flow<List<TopicEntity>> {
        return query.getTopicEntities { id, name, shortDescription, longDescription, url, imageUrl ->
            TopicEntity(
                id = id,
                name = name,
                shortDescription = shortDescription,
                longDescription = longDescription,
                url = url,
                imageUrl = imageUrl,
            )
        }
            .asFlow()
            .mapToList(dispatcher)
            .map {
                it.filter { topic -> topic.id in ids }
            }
    }

    /**
     * Inserts [topicEntities] into the db if they don't exist, and ignores those that do
     */
    suspend fun insertOrIgnoreTopics(topicEntities: List<TopicEntity>): List<Long> {
        topicEntities.map {
            query.insertOrIgnoreTopic(
                id = it.id,
                name = it.name,
                short_description = it.shortDescription,
                long_description = it.longDescription,
                url = it.url,
                image_url = it.imageUrl,
            )
        }
        // TODO return the ids of the inserted topics
        return topicEntities.mapNotNull { it.id.toLongOrNull() }
    }

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    suspend fun upsertTopics(entities: List<TopicEntity>) {
        entities.forEach {
            query.upsertTopic(
                id = it.id,
                name = it.name,
                short_description = it.shortDescription,
                long_description = it.longDescription,
                url = it.url,
                image_url = it.imageUrl,
            )
        }
    }

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    suspend fun deleteTopics(ids: List<String>) {
        query.deleteTopics(ids)
    }
}
