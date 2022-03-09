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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [TopicEntity] access
 */
@Dao
interface TopicDao {
    @Query(value = "SELECT * FROM topics")
    fun getTopicEntitiesStream(): Flow<List<TopicEntity>>

    @Query(
        value = """
        SELECT * FROM topics
        WHERE id IN (:ids)
    """
    )
    fun getTopicEntitiesStream(ids: Set<Int>): Flow<List<TopicEntity>>

    // TODO: Perform a proper upsert. See: b/226916817
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopics(entities: List<TopicEntity>)
}
