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

package com.google.samples.apps.nowinandroid.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.samples.apps.nowinandroid.core.data.repository.DefaultSearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceFtsDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicFtsDao
import com.google.samples.apps.nowinandroid.core.testing.database.newsResourceEntitiesTestData
import com.google.samples.apps.nowinandroid.core.testing.database.topicEntitiesTestData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Instrumentation tests for [SearchContentsRepository].
 */
class SearchContentsRepositoryTest {

    private lateinit var newsResourceDao: NewsResourceDao
    private lateinit var topicDao: TopicDao
    private lateinit var newsResourceFtsDao: NewsResourceFtsDao
    private lateinit var topicFtsDao: TopicFtsDao
    private lateinit var db: NiaDatabase
    private lateinit var searchContentsRepository: SearchContentsRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NiaDatabase::class.java,
        ).build()
        newsResourceDao = db.newsResourceDao()
        topicDao = db.topicDao()
        newsResourceFtsDao = db.newsResourceFtsDao()
        topicFtsDao = db.topicFtsDao()

        searchContentsRepository = DefaultSearchContentsRepository(
            newsResourceDao = newsResourceDao,
            newsResourceFtsDao = newsResourceFtsDao,
            topicDao = topicDao,
            topicFtsDao = topicFtsDao,
            ioDispatcher = Dispatchers.IO,
        )
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun whenPopulateFtsDataTwice_PopulateFtsOnce() = runTest {
        allDataPreSetting()
        repeat(2) {
            searchContentsRepository.populateFtsData()
        }
        advanceUntilIdle()
        assertEquals(7, searchContentsRepository.getSearchContentsCount().first())
    }

    @Test
    fun whenSearchAndroid_ReturnResult() = runTest {
        allDataPreSetting()
        searchContentsRepository.populateFtsData()

        val searchQuery = "Android"
        val topicIds = listOf("2")
        val newsResourceIds = listOf("1", "2")
        assertEquals(
            topicIds,
            searchContentsRepository
                .searchContents(searchQuery = searchQuery)
                .first()
                .topics
                .map { it.id },
        )
        assertEquals(
            newsResourceIds,
            searchContentsRepository
                .searchContents(searchQuery = searchQuery)
                .first()
                .newsResources
                .map { it.id },
        )
    }

    private suspend fun allDataPreSetting() {
        newsResourceDao.upsertNewsResources(newsResourceEntities = newsResourceEntitiesTestData)
        topicDao.upsertTopics(entities = topicEntitiesTestData)
    }
}
