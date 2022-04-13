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

import com.google.samples.apps.nowinandroid.core.database.dao.AuthorDao
import com.google.samples.apps.nowinandroid.core.database.dao.EpisodeDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.model.AuthorEntity
import com.google.samples.apps.nowinandroid.core.database.model.EpisodeEntity
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedNewsResource
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.datastore.ChangeListVersions
import com.google.samples.apps.nowinandroid.core.domain.Synchronizer
import com.google.samples.apps.nowinandroid.core.domain.changeListSync
import com.google.samples.apps.nowinandroid.core.domain.model.asEntity
import com.google.samples.apps.nowinandroid.core.domain.model.authorCrossReferences
import com.google.samples.apps.nowinandroid.core.domain.model.authorEntityShells
import com.google.samples.apps.nowinandroid.core.domain.model.episodeEntityShell
import com.google.samples.apps.nowinandroid.core.domain.model.topicCrossReferences
import com.google.samples.apps.nowinandroid.core.domain.model.topicEntityShells
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
    private val authorDao: AuthorDao,
    private val topicDao: TopicDao,
    private val network: NiANetwork,
) : NewsRepository {

    override fun getNewsResourcesStream(): Flow<List<NewsResource>> =
        newsResourceDao.getNewsResourcesStream()
            .map { it.map(PopulatedNewsResource::asExternalModel) }

    override fun getNewsResourcesStream(
        filterAuthorIds: Set<Int>,
        filterTopicIds: Set<Int>
    ): Flow<List<NewsResource>> = newsResourceDao.getNewsResourcesStream(
        filterAuthorIds = filterAuthorIds,
        filterTopicIds = filterTopicIds
    )
        .map { it.map(PopulatedNewsResource::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer) =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::newsResourceVersion,
            changeListFetcher = { currentVersion ->
                network.getNewsResourceChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(newsResourceVersion = latestVersion)
            },
            modelDeleter = newsResourceDao::deleteNewsResources,
            modelUpdater = { changedIds ->
                val networkNewsResources = network.getNewsResources(ids = changedIds)

                // Order of invocation matters to satisfy id and foreign key constraints!

                topicDao.insertOrIgnoreTopics(
                    topicEntities = networkNewsResources
                        .map(NetworkNewsResource::topicEntityShells)
                        .flatten()
                        .distinctBy(TopicEntity::id)
                )
                authorDao.insertOrIgnoreAuthors(
                    authorEntities = networkNewsResources
                        .map(NetworkNewsResource::authorEntityShells)
                        .flatten()
                        .distinctBy(AuthorEntity::id)
                )
                episodeDao.insertOrIgnoreEpisodes(
                    episodeEntities = networkNewsResources
                        .map(NetworkNewsResource::episodeEntityShell)
                        .distinctBy(EpisodeEntity::id)
                )
                newsResourceDao.upsertNewsResources(
                    newsResourceEntities = networkNewsResources
                        .map(NetworkNewsResource::asEntity)
                )
                newsResourceDao.insertOrIgnoreTopicCrossRefEntities(
                    newsResourceTopicCrossReferences = networkNewsResources
                        .map(NetworkNewsResource::topicCrossReferences)
                        .distinct()
                        .flatten()
                )
                newsResourceDao.insertOrIgnoreAuthorCrossRefEntities(
                    newsResourceAuthorCrossReferences = networkNewsResources
                        .map(NetworkNewsResource::authorCrossReferences)
                        .distinct()
                        .flatten()
                )
            }
        )
}
