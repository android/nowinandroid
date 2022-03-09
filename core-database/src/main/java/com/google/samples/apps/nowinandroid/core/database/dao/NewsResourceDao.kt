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
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceTopicCrossRef
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [NewsResource] and [NewsResourceEntity] access
 */
@Dao
interface NewsResourceDao {
    @Query(
        value = """
    SELECT * FROM news_resources
    ORDER BY publish_date
    """
    )
    fun getNewsResourcesStream(): Flow<List<PopulatedNewsResource>>

    @Query(
        value = """
        SELECT * FROM news_resources
        WHERE id in
        (
            SELECT news_resource_id FROM news_resources_topics
            WHERE topic_id IN (:filterTopicIds)
        )
        ORDER BY publish_date
    """
    )
    fun getNewsResourcesStream(filterTopicIds: Set<Int>): Flow<List<PopulatedNewsResource>>

    // TODO: Perform a proper upsert. See: b/226916817
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewsResourceEntities(entities: List<NewsResourceEntity>)

    // TODO: Perform a proper upsert. See: b/226916817
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopicCrossRefEntities(entities: List<NewsResourceTopicCrossRef>)
}
