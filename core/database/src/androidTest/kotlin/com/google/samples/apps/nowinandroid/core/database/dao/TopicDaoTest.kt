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
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TopicDaoTest {
    private lateinit var topicDao: TopicDao
    private lateinit var db: NiaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NiaDatabase::class.java,
        ).build()
        topicDao = db.topicDao()
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun topicDao_fetchTopics() = runTest {
        insertTopics()

        val savedTopics = topicDao.getTopicEntities().first()

        assertEquals(
            listOf("1", "2", "3"), savedTopics.map { it.id },
        )
    }

    @Test
    fun topicDao_getSingleTopicEntity() = runTest {
        insertTopics()

        val savedTopicEntity = topicDao.getTopicEntity("2").first()

        assertEquals("performance", savedTopicEntity.name)
    }

    @Test
    fun topicDao_getOneOffTopicEntity() = runTest {
        insertTopics()

        val savedTopics = topicDao.getOneOffTopicEntities()

        assertEquals(
            listOf("1", "2", "3"), savedTopics.map { it.id },
        )
    }

    @Test
    fun topicDao_getTopicEntities_ByIds() = runTest {
        insertTopics()

        val savedTopics = topicDao.getTopicEntities(setOf("1", "2"))
            .first()

        assertEquals(listOf("compose", "performance"), savedTopics.map { it.name })
    }

    @Test
    fun topicDao_IgnoreNewEntry_If_EntityExists() = runTest {
        insertTopics()
        topicDao.insertOrIgnoreTopics(
            listOf(testTopicEntity("1", "compose")),
        )

        val savedTopics = topicDao.getOneOffTopicEntities()

        assertEquals(3, savedTopics.size)
    }

    @Test
    fun topicDao_Upsert_Entities() = runTest {
        insertTopics()
        topicDao.upsertTopics(
            listOf(testTopicEntity("1", "newName")),
        )

        val savedTopics = topicDao.getOneOffTopicEntities()

        assertEquals(3, savedTopics.size)
        assertEquals("newName", savedTopics.first().name)
    }

    @Test
    fun topicDao_Delete_Entities() = runTest {
        insertTopics()
        topicDao.deleteTopics(listOf("1", "2"))

        val savedTopics = topicDao.getOneOffTopicEntities()

        assertEquals(1, savedTopics.size)
        assertEquals("3", savedTopics.first().id)
    }

    private suspend fun insertTopics() {
        val topicEntities = listOf(
            testTopicEntity("1", "compose"),
            testTopicEntity("2", "performance"),
            testTopicEntity("3", "headline"),
        )
        topicDao.insertOrIgnoreTopics(topicEntities)
    }
}

private fun testTopicEntity(
    id: String = "0",
    name: String,
) = TopicEntity(
    id = id,
    name = name,
    shortDescription = "",
    longDescription = "",
    url = "",
    imageUrl = "",
)
