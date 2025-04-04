/*
 * Copyright 2024 The Android Open Source Project
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
import com.google.samples.apps.nowinandroid.core.database.model.TopicFtsEntity
import com.google.samples.apps.nowinandroid.core.database.model.asFtsEntity
import com.google.samples.apps.nowinandroid.core.testing.database.topicEntitiesTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Instrumentation tests for [TopicFtsDao].
 */
class TopicFtsDaoTest {

    private lateinit var topicFtsDao: TopicFtsDao
    private lateinit var db: NiaDatabase

    private lateinit var topicFtsEntities: List<TopicFtsEntity>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NiaDatabase::class.java,
        ).build()
        topicFtsDao = db.topicFtsDao()

        topicFtsEntities = topicEntitiesTestData.map {
            it.asFtsEntity()
        }
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun topicFts_insertAllOnce_countMatches() = runTest {
        insertAllNewsResourceFtsEntities()
        assertEquals(topicFtsEntities.size, topicFtsDao.getCount().first())
    }

    @Test
    fun topicFts_insertAllTwice_countMatches() = runTest {
        repeat(2) {
            topicFtsDao.insertAll(topics = topicFtsEntities)
        }
        assertEquals(topicFtsEntities.size * 2, topicFtsDao.getCount().first())
    }

    @Test
    fun topicFts_insertAllThreeTimes_countMatches() = runTest {
        repeat(3) {
            topicFtsDao.deleteAllAndInsertAll(topics = topicFtsEntities)
        }
        assertEquals(topicFtsEntities.size, topicFtsDao.getCount().first())
    }

    private suspend fun insertAllNewsResourceFtsEntities() =
        topicFtsDao.insertAll(topics = topicFtsEntities)
}
