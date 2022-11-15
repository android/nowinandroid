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

package com.google.samples.apps.nowinandroid.core.data.repository

import com.google.samples.apps.nowinandroid.core.data.Synchronizer
import com.google.samples.apps.nowinandroid.core.data.model.asEntity
import com.google.samples.apps.nowinandroid.core.data.testdoubles.CollectionType
import com.google.samples.apps.nowinandroid.core.data.testdoubles.TestAuthorDao
import com.google.samples.apps.nowinandroid.core.data.testdoubles.TestNiaNetworkDataSource
import com.google.samples.apps.nowinandroid.core.database.dao.AuthorDao
import com.google.samples.apps.nowinandroid.core.database.model.AuthorEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferencesDataSource
import com.google.samples.apps.nowinandroid.core.datastore.test.testUserPreferencesDataStore
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.network.model.NetworkAuthor
import com.google.samples.apps.nowinandroid.core.network.model.NetworkChangeList
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class OfflineFirstAuthorsRepositoryTest {

    private lateinit var subject: OfflineFirstAuthorsRepository

    private lateinit var authorDao: AuthorDao

    private lateinit var network: TestNiaNetworkDataSource

    private lateinit var synchronizer: Synchronizer

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        authorDao = TestAuthorDao()
        network = TestNiaNetworkDataSource()
        val niaPreferencesDataSource = NiaPreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore()
        )
        synchronizer = TestSynchronizer(niaPreferencesDataSource)

        subject = OfflineFirstAuthorsRepository(
            authorDao = authorDao,
            network = network,
        )
    }

    @Test
    fun offlineFirstAuthorsRepository_Authors_stream_is_backed_by_Authors_dao() =
        runTest {
            assertEquals(
                authorDao.getAuthorEntitiesStream()
                    .first()
                    .map(AuthorEntity::asExternalModel),
                subject.getAuthorsStream()
                    .first()
            )
        }

    @Test
    fun offlineFirstAuthorsRepository_sync_pulls_from_network() =
        runTest {
            subject.syncWith(synchronizer)

            val networkAuthors = network.getAuthors()
                .map(NetworkAuthor::asEntity)

            val dbAuthors = authorDao.getAuthorEntitiesStream()
                .first()

            assertEquals(
                networkAuthors.map(AuthorEntity::id),
                dbAuthors.map(AuthorEntity::id)
            )

            // After sync version should be updated
            assertEquals(
                network.latestChangeListVersion(CollectionType.Authors),
                synchronizer.getChangeListVersions().authorVersion
            )
        }

    @Test
    fun offlineFirstAuthorsRepository_incremental_sync_pulls_from_network() =
        runTest {
            // Set author version to 5
            synchronizer.updateChangeListVersions {
                copy(authorVersion = 5)
            }

            subject.syncWith(synchronizer)

            val changeList = network.changeListsAfter(
                CollectionType.Authors,
                version = 5
            )
            val changeListIds = changeList
                .map(NetworkChangeList::id)
                .toSet()

            val network = network.getAuthors()
                .map(NetworkAuthor::asEntity)
                .filter { it.id in changeListIds }

            val db = authorDao.getAuthorEntitiesStream()
                .first()

            assertEquals(
                network.map(AuthorEntity::id),
                db.map(AuthorEntity::id)
            )

            // After sync version should be updated
            assertEquals(
                changeList.last().changeListVersion,
                synchronizer.getChangeListVersions().authorVersion
            )
        }

    @Test
    fun offlineFirstAuthorsRepository_sync_deletes_items_marked_deleted_on_network() =
        runTest {
            val networkAuthors = network.getAuthors()
                .map(NetworkAuthor::asEntity)
                .map(AuthorEntity::asExternalModel)

            // Delete half of the items on the network
            val deletedItems = networkAuthors
                .map(Author::id)
                .partition { it.chars().sum() % 2 == 0 }
                .first
                .toSet()

            deletedItems.forEach {
                network.editCollection(
                    collectionType = CollectionType.Authors,
                    id = it,
                    isDelete = true
                )
            }

            subject.syncWith(synchronizer)

            val dbAuthors = authorDao.getAuthorEntitiesStream()
                .first()
                .map(AuthorEntity::asExternalModel)

            // Assert that items marked deleted on the network have been deleted locally
            assertEquals(
                networkAuthors.map(Author::id) - deletedItems,
                dbAuthors.map(Author::id)
            )

            // After sync version should be updated
            assertEquals(
                network.latestChangeListVersion(CollectionType.Authors),
                synchronizer.getChangeListVersions().authorVersion
            )
        }
}
