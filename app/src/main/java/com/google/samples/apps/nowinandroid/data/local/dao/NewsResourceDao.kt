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

package com.google.samples.apps.nowinandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.google.samples.apps.nowinandroid.data.local.entities.NewsResourceEntity
import com.google.samples.apps.nowinandroid.data.model.NewsResource
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [NewsResource] and [NewsResourceEntity] access
 */
@Dao
interface NewsResourceDao {
    @Query(value = "SELECT * FROM news_resources")
    fun getNewsResourcesStream(): Flow<List<NewsResource>>

    @Query(
        value = """
        SELECT * FROM news_resources
        WHERE id IN (:filterTopicIds)
    """
    )
    fun getNewsResourcesStream(filterTopicIds: Set<Int>): Flow<List<NewsResource>>

    @Insert
    suspend fun saveNewsResourceEntities(entities: List<NewsResourceEntity>)
}
