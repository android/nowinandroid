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
import androidx.room.Transaction
import androidx.room.Update
import com.google.samples.apps.nowinandroid.core.database.model.EpisodeEntity
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedEpisode
import com.google.samples.apps.nowinandroid.core.model.data.Episode
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [EpisodeEntity] and [Episode] access
 */
@Dao
interface EpisodeDao {
    @Query(value = "SELECT * FROM episodes")
    fun getEpisodesStream(): Flow<List<PopulatedEpisode>>

    /**
     * Inserts [episodeEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreEpisodes(episodeEntities: List<EpisodeEntity>): List<Long>

    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun updateEpisodes(entities: List<EpisodeEntity>)

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Transaction
    suspend fun upsertEpisodes(entities: List<EpisodeEntity>) = upsert(
        items = entities,
        insertMany = ::insertOrIgnoreEpisodes,
        updateMany = ::updateEpisodes
    )

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(
        value = """
            DELETE FROM episodes
            WHERE id in (:ids)
        """
    )
    suspend fun deleteEpisodes(ids: List<String>)
}
