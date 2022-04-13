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

package com.google.samples.apps.nowinandroid.core.domain.repository

import com.google.samples.apps.nowinandroid.core.database.model.AuthorEntity
import com.google.samples.apps.nowinandroid.core.database.model.EpisodeEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedEpisode
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedNewsResource
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferences
import com.google.samples.apps.nowinandroid.core.datastore.test.testUserPreferencesDataStore
import com.google.samples.apps.nowinandroid.core.domain.Synchronizer
import com.google.samples.apps.nowinandroid.core.domain.model.asEntity
import com.google.samples.apps.nowinandroid.core.domain.model.authorCrossReferences
import com.google.samples.apps.nowinandroid.core.domain.model.authorEntityShells
import com.google.samples.apps.nowinandroid.core.domain.model.episodeEntityShell
import com.google.samples.apps.nowinandroid.core.domain.model.topicCrossReferences
import com.google.samples.apps.nowinandroid.core.domain.model.topicEntityShells
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.CollectionType
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestAuthorDao
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestEpisodeDao
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestNewsResourceDao
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestNiaNetwork
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestTopicDao
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.filteredInterestsIds
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.nonPresentInterestsIds
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkChangeList
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class LocalNewsRepositoryTest {

    private lateinit var subject: LocalNewsRepository

    private lateinit var newsResourceDao: TestNewsResourceDao

    private lateinit var episodeDao: TestEpisodeDao

    private lateinit var authorDao: TestAuthorDao

    private lateinit var topicDao: TestTopicDao

    private lateinit var network: TestNiaNetwork

    private lateinit var synchronizer: Synchronizer

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        newsResourceDao = TestNewsResourceDao()
        episodeDao = TestEpisodeDao()
        authorDao = TestAuthorDao()
        topicDao = TestTopicDao()
        network = TestNiaNetwork()
        synchronizer = TestSynchronizer(
            NiaPreferences(
                tmpFolder.testUserPreferencesDataStore()
            )
        )

        subject = LocalNewsRepository(
            newsResourceDao = newsResourceDao,
            episodeDao = episodeDao,
            authorDao = authorDao,
            topicDao = topicDao,
            network = network,
        )
    }

    @Test
    fun localNewsRepository_news_resources_stream_is_backed_by_news_resource_dao() =
        runTest {
            assertEquals(
                newsResourceDao.getNewsResourcesStream()
                    .first()
                    .map(PopulatedNewsResource::asExternalModel),
                subject.getNewsResourcesStream()
                    .first()
            )
        }

    @Test
    fun localNewsRepository_news_resources_topic_filtered_stream_is_backed_by_news_resource_dao() =
        runTest {
            assertEquals(
                newsResourceDao.getNewsResourcesStream(
                    filterTopicIds = filteredInterestsIds,
                )
                    .first()
                    .map(PopulatedNewsResource::asExternalModel),
                subject.getNewsResourcesStream(
                    filterTopicIds = filteredInterestsIds,
                )
                    .first()
            )

            assertEquals(
                emptyList<NewsResource>(),
                subject.getNewsResourcesStream(
                    filterTopicIds = nonPresentInterestsIds,
                )
                    .first()
            )
        }

    @Test
    fun localNewsRepository_news_resources_author_filtered_stream_is_backed_by_news_resource_dao() =
        runTest {
            assertEquals(
                newsResourceDao.getNewsResourcesStream(
                    filterAuthorIds = filteredInterestsIds
                )
                    .first()
                    .map(PopulatedNewsResource::asExternalModel),
                subject.getNewsResourcesStream(
                    filterAuthorIds = filteredInterestsIds
                )
                    .first()
            )

            assertEquals(
                emptyList<NewsResource>(),
                subject.getNewsResourcesStream(
                    filterAuthorIds = nonPresentInterestsIds
                )
                    .first()
            )
        }

    @Test
    fun localNewsRepository_sync_pulls_from_network() =
        runTest {
            subject.syncWith(synchronizer)

            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)

            val newsResourcesFromDb = newsResourceDao.getNewsResourcesStream()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            assertEquals(
                newsResourcesFromNetwork.map(NewsResource::id),
                newsResourcesFromDb.map(NewsResource::id)
            )

            // After sync version should be updated
            assertEquals(
                network.latestChangeListVersion(CollectionType.NewsResources),
                synchronizer.getChangeListVersions().newsResourceVersion
            )
        }

    @Test
    fun localNewsRepository_sync_deletes_items_marked_deleted_on_network() =
        runTest {
            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)

            val deletedItems = newsResourcesFromNetwork
                .map(NewsResource::id)
                .partition { it % 2 == 0 }
                .first
                .toSet()

            deletedItems.forEach {
                network.editCollection(
                    collectionType = CollectionType.NewsResources,
                    id = it,
                    isDelete = true
                )
            }

            subject.syncWith(synchronizer)

            val newsResourcesFromDb = newsResourceDao.getNewsResourcesStream()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            assertEquals(
                newsResourcesFromNetwork.map(NewsResource::id) - deletedItems,
                newsResourcesFromDb.map(NewsResource::id)
            )

            // After sync version should be updated
            assertEquals(
                network.latestChangeListVersion(CollectionType.NewsResources),
                synchronizer.getChangeListVersions().newsResourceVersion
            )
        }

    @Test
    fun localNewsRepository_incremental_sync_pulls_from_network() =
        runTest {
            // Set news version to 7
            synchronizer.updateChangeListVersions {
                copy(newsResourceVersion = 7)
            }

            subject.syncWith(synchronizer)

            val changeList = network.changeListsAfter(
                CollectionType.NewsResources,
                version = 7
            )
            val changeListIds = changeList
                .map(NetworkChangeList::id)
                .toSet()

            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)
                .filter { it.id in changeListIds }

            val newsResourcesFromDb = newsResourceDao.getNewsResourcesStream()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            assertEquals(
                newsResourcesFromNetwork.map(NewsResource::id),
                newsResourcesFromDb.map(NewsResource::id)
            )

            // After sync version should be updated
            assertEquals(
                changeList.last().changeListVersion,
                synchronizer.getChangeListVersions().newsResourceVersion
            )
        }

    @Test
    fun localNewsRepository_sync_saves_shell_topic_entities() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::topicEntityShells)
                    .flatten()
                    .distinctBy(TopicEntity::id),
                topicDao.getTopicEntitiesStream()
                    .first()
            )
        }

    @Test
    fun localNewsRepository_sync_saves_shell_author_entities() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::authorEntityShells)
                    .flatten()
                    .distinctBy(AuthorEntity::id),
                authorDao.getAuthorEntitiesStream()
                    .first()
            )
        }

    @Test
    fun localNewsRepository_sync_saves_shell_episode_entities() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::episodeEntityShell)
                    .distinctBy(EpisodeEntity::id),
                episodeDao.getEpisodesStream()
                    .first()
                    .map(PopulatedEpisode::entity)
            )
        }

    @Test
    fun localNewsRepository_sync_saves_topic_cross_references() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::topicCrossReferences)
                    .distinct()
                    .flatten(),
                newsResourceDao.topicCrossReferences
            )
        }

    @Test
    fun localNewsRepository_sync_saves_author_cross_references() =
        runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources()
                    .map(NetworkNewsResource::authorCrossReferences)
                    .distinct()
                    .flatten(),
                newsResourceDao.authorCrossReferences
            )
        }
}
