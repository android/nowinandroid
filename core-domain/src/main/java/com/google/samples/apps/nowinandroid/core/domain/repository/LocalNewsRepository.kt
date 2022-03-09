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

package com.google.samples.apps.nowinandroid.core.domain.repository

import com.google.samples.apps.nowinandroid.core.database.dao.EpisodeDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.model.EpisodeEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedNewsResource
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.database.model.episodeEntityShell
import com.google.samples.apps.nowinandroid.core.domain.model.asEntity
import com.google.samples.apps.nowinandroid.core.domain.model.topicCrossReferences
import com.google.samples.apps.nowinandroid.core.domain.suspendRunCatching
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room database backed implementation of the [NewsRepository].
 */
class LocalNewsRepository @Inject constructor(
    private val newsResourceDao: NewsResourceDao,
    private val episodeDao: EpisodeDao,
    private val network: NiANetwork,
) : NewsRepository {

    override fun getNewsResourcesStream(): Flow<List<NewsResource>> =
        newsResourceDao.getNewsResourcesStream()
            .map { it.map(PopulatedNewsResource::asExternalModel) }

    override fun getNewsResourcesStream(filterTopicIds: Set<Int>): Flow<List<NewsResource>> =
        newsResourceDao.getNewsResourcesStream(filterTopicIds = filterTopicIds)
            .map { it.map(PopulatedNewsResource::asExternalModel) }

    override suspend fun sync() = suspendRunCatching {
        val networkNewsResources = network.getNewsResources()

        val newsResourceEntities = networkNewsResources
            .map(NetworkNewsResource::asEntity)

        val episodeEntityShells = newsResourceEntities
            .map(NewsResourceEntity::episodeEntityShell)
            .distinctBy(EpisodeEntity::id)

        val topicCrossReferences = networkNewsResources
            .map(NetworkNewsResource::topicCrossReferences)
            .distinct()
            .flatten()

        // Order of invocation matters to satisfy id and foreign key constraints!

        // TODO: Create a separate method for saving shells with proper conflict resolution
        //  See: b/226919874
        episodeDao.saveEpisodeEntities(
            episodeEntityShells
        )
        newsResourceDao.saveNewsResourceEntities(
            newsResourceEntities
        )
        newsResourceDao.saveTopicCrossRefEntities(
            topicCrossReferences
        )

        // TODO: Save author as well
    }.isSuccess
}
