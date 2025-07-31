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
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceFtsEntity
import com.google.samples.apps.nowinandroid.core.database.model.asFtsEntity
import com.google.samples.apps.nowinandroid.core.testing.database.newsResourceEntitiesTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Instrumentation tests for [NewsResourceFtsDao].
 */
class NewsResourceFtsDaoTest {

    private lateinit var newsResourceFtsDao: NewsResourceFtsDao
    private lateinit var db: NiaDatabase

    private lateinit var newsResourceFtsEntities: List<NewsResourceFtsEntity>

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NiaDatabase::class.java,
        ).build()
        newsResourceFtsDao = db.newsResourceFtsDao()

        newsResourceFtsEntities = newsResourceEntitiesTestData.map {
            it.asFtsEntity()
        }
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun newsResourceFts_insertAllOnce_countMatches() = runTest {
        insertAllNewsResourceFtsEntities()
        assertEquals(newsResourceFtsEntities.size, newsResourceFtsDao.getCount().first())
    }

    @Test
    fun newsResourceFts_insertAllTwice_countMatches() = runTest {
        repeat(2) {
            newsResourceFtsDao.insertAll(newsResources = newsResourceFtsEntities)
        }
        assertEquals(newsResourceFtsEntities.size * 2, newsResourceFtsDao.getCount().first())
    }

    @Test
    fun newsResourceFts_insertAllThreeTimes_countMatches() = runTest {
        repeat(3) {
            newsResourceFtsDao.deleteAllAndInsertAll(newsResources = newsResourceFtsEntities)
        }
        assertEquals(newsResourceFtsEntities.size, newsResourceFtsDao.getCount().first())
    }

    private suspend fun insertAllNewsResourceFtsEntities() =
        newsResourceFtsDao.insertAll(newsResources = newsResourceFtsEntities)
}
