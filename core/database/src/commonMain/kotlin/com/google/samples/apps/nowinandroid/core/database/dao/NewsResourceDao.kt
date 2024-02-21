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
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceTopicCrossRef
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

/**
 * DAO for [NewsResource] and [NewsResourceEntity] access
 */
class NewsResourceDao(db: NiaDatabase, private val dispatcher: CoroutineDispatcher) {
    private val query = db.newsResourceQueries

    /**
     * Fetches news resources that match the query parameters
     */
    fun getNewsResources(
        useFilterTopicIds: Boolean = false,
        filterTopicIds: Set<String> = emptySet(),
        useFilterNewsIds: Boolean = false,
        filterNewsIds: Set<String> = emptySet(),
    ): Flow<List<PopulatedNewsResource>> {
        return query.getNewsResources(
            useFilterTopicIds = useFilterTopicIds,
            filterTopicIds = filterTopicIds,
            useFilterNewsIds = useFilterNewsIds,
            filterNewsIds = filterNewsIds,
        ) { id, title, content, url, headerImageUrl, publishDate, type ->
            PopulatedNewsResource(
                entity = NewsResourceEntity(
                    id = id,
                    title = title,
                    content = content,
                    url = url,
                    headerImageUrl = headerImageUrl,
                    publishDate = Instant.fromEpochMilliseconds(publishDate),
                    type = type,
                ),
                // TODO Dealing with NewsResources <-> Topics relationship
                topics = emptyList(),
            )
        }
            .asFlow()
            .mapToList(dispatcher)
    }

    /**
     * Fetches ids of news resources that match the query parameters
     */
    fun getNewsResourceIds(
        useFilterTopicIds: Boolean = false,
        filterTopicIds: Set<String> = emptySet(),
        useFilterNewsIds: Boolean = false,
        filterNewsIds: Set<String> = emptySet(),
    ): Flow<List<String>> {
        return query.getNewsResourceIds(
            useFilterTopicIds = useFilterTopicIds,
            filterTopicIds = filterTopicIds,
            useFilterNewsIds = useFilterNewsIds,
            filterNewsIds = filterNewsIds,
        )
            .asFlow()
            .mapToList(dispatcher)
    }

    /**
     * Inserts [entities] into the db if they don't exist, and ignores those that do
     */
    suspend fun insertOrIgnoreNewsResources(entities: List<NewsResourceEntity>): List<Long> {
        entities.forEach {
            query.insertOrIgnoreNewsResource(
                id = it.id,
                title = it.title,
                content = it.content,
                url = it.url,
                header_image_url = it.headerImageUrl,
                publish_date = it.publishDate.toEpochMilliseconds(),
                type = it.type,
            )
        }
        // TODO Return the inserted ids
        return entities.mapNotNull {
            it.id.toLongOrNull()
        }
    }

    /**
     * Inserts or updates [newsResourceEntities] in the db under the specified primary keys
     */
    suspend fun upsertNewsResources(newsResourceEntities: List<NewsResourceEntity>) {
        newsResourceEntities.forEach {
            query.upsertNewsResource(
                id = it.id,
                title = it.title,
                content = it.content,
                url = it.url,
                header_image_url = it.headerImageUrl,
                publish_date = it.publishDate.toEpochMilliseconds(),
                type = it.type,
            )
        }
    }

    suspend fun insertOrIgnoreTopicCrossRefEntities(
        newsResourceTopicCrossReferences: List<NewsResourceTopicCrossRef>,
    ) {
        newsResourceTopicCrossReferences.forEach {
            query.insertOrIgnoreTopicCrossRefEntitiy(
                news_resource_id = it.newsResourceId,
                topic_id = it.topicId,
            )
        }
    }

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    suspend fun deleteNewsResources(ids: List<String>) {
        query.deleteNewsResources(ids)
    }
}
