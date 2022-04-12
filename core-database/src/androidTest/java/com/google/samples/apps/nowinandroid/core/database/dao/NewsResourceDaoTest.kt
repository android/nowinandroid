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

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.samples.apps.nowinandroid.core.database.NiADatabase
import com.google.samples.apps.nowinandroid.core.database.model.EpisodeEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceTopicCrossRef
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NewsResourceDaoTest {

    private lateinit var newsResourceDao: NewsResourceDao
    private lateinit var episodeDao: EpisodeDao
    private lateinit var topicDao: TopicDao
    private lateinit var db: NiADatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NiADatabase::class.java
        ).build()
        newsResourceDao = db.newsResourceDao()
        episodeDao = db.episodeDao()
        topicDao = db.topicDao()
    }

    @Test
    fun newsResourceDao_fetches_items_by_descending_publish_date() = runTest {
        val newsResourceEntities = listOf(
            testNewsResource(
                id = 0,
                millisSinceEpoch = 0,
            ),
            testNewsResource(
                id = 1,
                millisSinceEpoch = 3,
            ),
            testNewsResource(
                id = 2,
                millisSinceEpoch = 1,
            ),
            testNewsResource(
                id = 3,
                millisSinceEpoch = 2,
            ),
        )
        val episodeEntityShells = newsResourceEntities
            .map(NewsResourceEntity::episodeEntityShell)
            .distinct()

        episodeDao.insertOrIgnoreEpisodes(
            episodeEntityShells
        )
        newsResourceDao.upsertNewsResources(
            newsResourceEntities
        )

        val savedNewsResourceEntities = newsResourceDao.getNewsResourcesStream()
            .first()

        assertEquals(
            listOf(3L, 2L, 1L, 0L),
            savedNewsResourceEntities.map {
                it.asExternalModel().publishDate.toEpochMilliseconds()
            }
        )
    }

    @Test
    fun newsResourceDao_filters_items_by_topic_ids_by_descending_publish_date() = runTest {
        val topicEntities = listOf(
            testTopicEntity(
                id = 1,
                name = "1"
            ),
            testTopicEntity(
                id = 2,
                name = "2"
            ),
        )
        val newsResourceEntities = listOf(
            testNewsResource(
                id = 0,
                millisSinceEpoch = 0,
            ),
            testNewsResource(
                id = 1,
                millisSinceEpoch = 3,
            ),
            testNewsResource(
                id = 2,
                millisSinceEpoch = 1,
            ),
            testNewsResource(
                id = 3,
                millisSinceEpoch = 2,
            ),
        )
        val episodeEntityShells = newsResourceEntities
            .map(NewsResourceEntity::episodeEntityShell)
            .distinct()
        val newsResourceTopicCrossRefEntities = topicEntities.mapIndexed { index, topicEntity ->
            NewsResourceTopicCrossRef(
                newsResourceId = index,
                topicId = topicEntity.id
            )
        }

        topicDao.insertOrIgnoreTopics(
            topicEntities = topicEntities
        )
        episodeDao.insertOrIgnoreEpisodes(
            episodeEntities = episodeEntityShells
        )
        newsResourceDao.upsertNewsResources(
            newsResourceEntities
        )
        newsResourceDao.insertOrIgnoreTopicCrossRefEntities(
            newsResourceTopicCrossRefEntities
        )

        val filteredNewsResources = newsResourceDao.getNewsResourcesStream(
            filterTopicIds = topicEntities
                .map(TopicEntity::id)
                .toSet()
        ).first()

        assertEquals(
            listOf(1, 0),
            filteredNewsResources.map { it.entity.id }
        )
    }
}

private fun testTopicEntity(
    id: Int = 0,
    name: String
) = TopicEntity(
    id = id,
    name = name,
    shortDescription = "",
    longDescription = "",
    url = "",
    imageUrl = ""
)

private fun testNewsResource(
    id: Int = 0,
    millisSinceEpoch: Long = 0
) = NewsResourceEntity(
    id = id,
    episodeId = 0,
    title = "",
    content = "",
    url = "",
    headerImageUrl = "",
    publishDate = Instant.fromEpochMilliseconds(millisSinceEpoch),
    type = NewsResourceType.DAC,
)

private fun NewsResourceEntity.episodeEntityShell() = EpisodeEntity(
    id = episodeId,
    name = "",
    publishDate = Instant.fromEpochMilliseconds(0),
    alternateVideo = null,
    alternateAudio = null,
)
