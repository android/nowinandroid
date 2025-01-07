/*
 * Copyright 2025 The Android Open Source Project
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
import org.junit.After
import org.junit.Before

internal abstract class DatabaseTest {

    private lateinit var db: NiaDatabase
    protected lateinit var newsResourceDao: NewsResourceDao
    protected lateinit var topicDao: TopicDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                NiaDatabase::class.java,
            ).build()
        }
        newsResourceDao = db.newsResourceDao()
        topicDao = db.topicDao()
    }

    @After
    fun teardown() = db.close()
}
