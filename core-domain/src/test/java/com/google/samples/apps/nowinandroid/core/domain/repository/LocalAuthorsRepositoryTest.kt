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

import com.google.samples.apps.nowinandroid.core.database.dao.AuthorDao
import com.google.samples.apps.nowinandroid.core.database.model.AuthorEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.domain.model.asEntity
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestAuthorDao
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestNiaNetwork
import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.model.NetworkAuthor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LocalAuthorsRepositoryTest {

    private lateinit var subject: LocalAuthorsRepository

    private lateinit var authorDao: AuthorDao

    private lateinit var network: NiANetwork

    @Before
    fun setup() {
        authorDao = TestAuthorDao()
        network = TestNiaNetwork()

        subject = LocalAuthorsRepository(
            authorDao = authorDao,
            network = network,
        )
    }

    @Test
    fun localAuthorsRepository_Authors_stream_is_backed_by_Authors_dao() =
        runTest {
            Assert.assertEquals(
                authorDao.getAuthorEntitiesStream()
                    .first()
                    .map(AuthorEntity::asExternalModel),
                subject.getAuthorsStream()
                    .first()
            )
        }

    @Test
    fun localAuthorsRepository_sync_pulls_from_network() =
        runTest {
            subject.sync()

            val network = network.getAuthors()
                .map(NetworkAuthor::asEntity)

            val db = authorDao.getAuthorEntitiesStream()
                .first()

            Assert.assertEquals(
                network.map(AuthorEntity::id),
                db.map(AuthorEntity::id)
            )
        }
}
