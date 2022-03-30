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

package com.google.samples.apps.nowinandroid.core.domain.testdoubles

import com.google.samples.apps.nowinandroid.core.database.dao.EpisodeDao
import com.google.samples.apps.nowinandroid.core.database.model.EpisodeEntity
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedEpisode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

/**
 * Test double for [EpisodeDao]
 */
class TestEpisodeDao : EpisodeDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            EpisodeEntity(
                id = 1,
                name = "Episode",
                publishDate = Instant.fromEpochMilliseconds(0),
                alternateVideo = null,
                alternateAudio = null,
            )
        )
    )

    override fun getEpisodesStream(): Flow<List<PopulatedEpisode>> =
        entitiesStateFlow.map {
            it.map(EpisodeEntity::asPopulatedEpisode)
        }

    override suspend fun insertOrIgnoreEpisodes(episodeEntities: List<EpisodeEntity>): List<Long> {
        entitiesStateFlow.value = episodeEntities
        // Assume no conflicts on insert
        return episodeEntities.map { it.id.toLong() }
    }

    override suspend fun updateEpisodes(entities: List<EpisodeEntity>) {
        throw NotImplementedError("Unused in tests")
    }
}

private fun EpisodeEntity.asPopulatedEpisode() = PopulatedEpisode(
    entity = this,
    newsResources = emptyList(),
    authors = emptyList(),
)
