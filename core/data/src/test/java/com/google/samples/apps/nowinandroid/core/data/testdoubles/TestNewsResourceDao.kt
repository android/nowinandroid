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

import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceTopicCrossRef
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedNewsResource
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Instant

val filteredInterestsIds = setOf("1")
val nonPresentInterestsIds = setOf("2")

/**
 * Test double for [NewsResourceDao]
 */
class TestNewsResourceDao : NewsResourceDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            NewsResourceEntity(
                id = "1",
                title = "news",
                content = "Hilt",
                url = "url",
                headerImageUrl = "headerImageUrl",
                type = Video,
                publishDate = Instant.fromEpochMilliseconds(1),
            ),
        ),
    )

    internal var topicCrossReferences: List<NewsResourceTopicCrossRef> = listOf()

    override fun getNewsResources(): Flow<List<PopulatedNewsResource>> =
        entitiesStateFlow.map {
            it.map(NewsResourceEntity::asPopulatedNewsResource)
        }

    override fun getNewsResources(
        filterTopicIds: Set<String>,
    ): Flow<List<PopulatedNewsResource>> =
        getNewsResources()
            .map { resources ->
                resources.filter { resource ->
                    resource.topics.any { it.id in filterTopicIds }
                }
            }

    override suspend fun insertOrIgnoreNewsResources(
        entities: List<NewsResourceEntity>,
    ): List<Long> {
        entitiesStateFlow.value = entities
        // Assume no conflicts on insert
        return entities.map { it.id.toLong() }
    }

    override suspend fun updateNewsResources(entities: List<NewsResourceEntity>) {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun upsertNewsResources(newsResourceEntities: List<NewsResourceEntity>) {
        entitiesStateFlow.value = newsResourceEntities
    }

    override suspend fun insertOrIgnoreTopicCrossRefEntities(
        newsResourceTopicCrossReferences: List<NewsResourceTopicCrossRef>,
    ) {
        topicCrossReferences = newsResourceTopicCrossReferences
    }

    override suspend fun deleteNewsResources(ids: List<String>) {
        val idSet = ids.toSet()
        entitiesStateFlow.update { entities ->
            entities.filterNot { idSet.contains(it.id) }
        }
    }
}

private fun NewsResourceEntity.asPopulatedNewsResource() = PopulatedNewsResource(
    entity = this,
    topics = listOf(
        TopicEntity(
            id = filteredInterestsIds.random(),
            name = "name",
            shortDescription = "short description",
            longDescription = "long description",
            url = "URL",
            imageUrl = "image URL",
        ),
    ),
)
