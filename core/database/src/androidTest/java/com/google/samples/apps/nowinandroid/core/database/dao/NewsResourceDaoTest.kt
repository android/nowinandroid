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
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.model.AuthorEntity
import com.google.samples.apps.nowinandroid.core.database.model.EpisodeEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceAuthorCrossRef
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
    private lateinit var authorDao: AuthorDao
    private lateinit var db: NiaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NiaDatabase::class.java
        ).build()
        newsResourceDao = db.newsResourceDao()
        episodeDao = db.episodeDao()
        topicDao = db.topicDao()
        authorDao = db.authorDao()
    }

    @Test
    fun newsResourceDao_fetches_items_by_descending_publish_date() = runTest {
        val newsResourceEntities = listOf(
            testNewsResource(
                id = "0",
                millisSinceEpoch = 0,
            ),
            testNewsResource(
                id = "1",
                millisSinceEpoch = 3,
            ),
            testNewsResource(
                id = "2",
                millisSinceEpoch = 1,
            ),
            testNewsResource(
                id = "3",
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
                id = "1",
                name = "1"
            ),
            testTopicEntity(
                id = "2",
                name = "2"
            ),
        )
        val newsResourceEntities = listOf(
            testNewsResource(
                id = "0",
                millisSinceEpoch = 0,
            ),
            testNewsResource(
                id = "1",
                millisSinceEpoch = 3,
            ),
            testNewsResource(
                id = "2",
                millisSinceEpoch = 1,
            ),
            testNewsResource(
                id = "3",
                millisSinceEpoch = 2,
            ),
        )
        val episodeEntityShells = newsResourceEntities
            .map(NewsResourceEntity::episodeEntityShell)
            .distinct()
        val newsResourceTopicCrossRefEntities = topicEntities.mapIndexed { index, topicEntity ->
            NewsResourceTopicCrossRef(
                newsResourceId = index.toString(),
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
                .toSet(),
        ).first()

        assertEquals(
            listOf("1", "0"),
            filteredNewsResources.map { it.entity.id }
        )
    }

    @Test
    fun newsResourceDao_filters_items_by_author_ids_by_descending_publish_date() = runTest {
        val authorEntities = listOf(
            testAuthorEntity(
                id = "1",
                name = "1"
            ),
            testAuthorEntity(
                id = "2",
                name = "2"
            ),
        )
        val newsResourceEntities = listOf(
            testNewsResource(
                id = "0",
                millisSinceEpoch = 0,
            ),
            testNewsResource(
                id = "1",
                millisSinceEpoch = 3,
            ),
            testNewsResource(
                id = "2",
                millisSinceEpoch = 1,
            ),
            testNewsResource(
                id = "3",
                millisSinceEpoch = 2,
            ),
        )
        val episodeEntityShells = newsResourceEntities
            .map(NewsResourceEntity::episodeEntityShell)
            .distinct()
        val newsResourceAuthorCrossRefEntities = authorEntities.mapIndexed { index, authorEntity ->
            NewsResourceAuthorCrossRef(
                newsResourceId = index.toString(),
                authorId = authorEntity.id
            )
        }

        authorDao.upsertAuthors(authorEntities)
        episodeDao.upsertEpisodes(episodeEntityShells)
        newsResourceDao.upsertNewsResources(newsResourceEntities)
        newsResourceDao.insertOrIgnoreAuthorCrossRefEntities(newsResourceAuthorCrossRefEntities)

        val filteredNewsResources = newsResourceDao.getNewsResourcesStream(
            filterAuthorIds = authorEntities
                .map(AuthorEntity::id)
                .toSet()
        ).first()

        assertEquals(
            listOf("1", "0"),
            filteredNewsResources.map { it.entity.id }
        )
    }

    @Test
    fun newsResourceDao_filters_items_by_topic_ids_or_author_ids_by_descending_publish_date() =
        runTest {
            val topicEntities = listOf(
                testTopicEntity(
                    id = "1",
                    name = "1"
                ),
                testTopicEntity(
                    id = "2",
                    name = "2"
                ),
            )
            val authorEntities = listOf(
                testAuthorEntity(
                    id = "1",
                    name = "1"
                ),
                testAuthorEntity(
                    id = "2",
                    name = "2"
                ),
            )
            val newsResourceEntities = listOf(
                testNewsResource(
                    id = "0",
                    millisSinceEpoch = 0,
                ),
                testNewsResource(
                    id = "1",
                    millisSinceEpoch = 3,
                ),
                testNewsResource(
                    id = "2",
                    millisSinceEpoch = 1,
                ),
                testNewsResource(
                    id = "3",
                    millisSinceEpoch = 2,
                ),
                // Should be missing as no topics or authors match it
                testNewsResource(
                    id = "4",
                    millisSinceEpoch = 10,
                ),
            )
            val episodeEntityShells = newsResourceEntities
                .map(NewsResourceEntity::episodeEntityShell)
                .distinct()
            val newsResourceTopicCrossRefEntities = topicEntities.mapIndexed { index, topicEntity ->
                NewsResourceTopicCrossRef(
                    newsResourceId = index.toString(),
                    topicId = topicEntity.id
                )
            }
            val newsResourceAuthorCrossRefEntities =
                authorEntities.mapIndexed { index, authorEntity ->
                    NewsResourceAuthorCrossRef(
                        // Offset news resources by two
                        newsResourceId = (index + 2).toString(),
                        authorId = authorEntity.id
                    )
                }

            topicDao.upsertTopics(topicEntities)
            authorDao.upsertAuthors(authorEntities)
            episodeDao.upsertEpisodes(episodeEntityShells)
            newsResourceDao.upsertNewsResources(newsResourceEntities)
            newsResourceDao.insertOrIgnoreTopicCrossRefEntities(newsResourceTopicCrossRefEntities)
            newsResourceDao.insertOrIgnoreAuthorCrossRefEntities(newsResourceAuthorCrossRefEntities)

            val filteredNewsResources = newsResourceDao.getNewsResourcesStream(
                filterTopicIds = topicEntities
                    .map(TopicEntity::id)
                    .toSet(),
                filterAuthorIds = authorEntities
                    .map(AuthorEntity::id)
                    .toSet()
            ).first()

            assertEquals(
                listOf("1", "3", "2", "0"),
                filteredNewsResources.map { it.entity.id }
            )
        }

    @Test
    fun newsResourceDao_deletes_items_by_ids() =
        runTest {
            val newsResourceEntities = listOf(
                testNewsResource(
                    id = "0",
                    millisSinceEpoch = 0,
                ),
                testNewsResource(
                    id = "1",
                    millisSinceEpoch = 3,
                ),
                testNewsResource(
                    id = "2",
                    millisSinceEpoch = 1,
                ),
                testNewsResource(
                    id = "3",
                    millisSinceEpoch = 2,
                ),
            )
            val episodeEntityShells = newsResourceEntities
                .map(NewsResourceEntity::episodeEntityShell)
                .distinct()

            episodeDao.upsertEpisodes(episodeEntityShells)
            newsResourceDao.upsertNewsResources(newsResourceEntities)

            val (toDelete, toKeep) = newsResourceEntities.partition { it.id.toInt() % 2 == 0 }

            newsResourceDao.deleteNewsResources(
                toDelete.map(NewsResourceEntity::id)
            )

            assertEquals(
                toKeep.map(NewsResourceEntity::id)
                    .toSet(),
                newsResourceDao.getNewsResourcesStream().first()
                    .map { it.entity.id }
                    .toSet()
            )
        }
}

private fun testAuthorEntity(
    id: String = "0",
    name: String
) = AuthorEntity(
    id = id,
    name = name,
    imageUrl = "",
    twitter = "",
    mediumPage = "",
    bio = "",
)

private fun testTopicEntity(
    id: String = "0",
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
    id: String = "0",
    millisSinceEpoch: Long = 0
) = NewsResourceEntity(
    id = id,
    episodeId = "0",
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
