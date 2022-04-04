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
import com.google.samples.apps.nowinandroid.core.database.model.AuthorEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.datastore.ChangeListVersions
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferences
import com.google.samples.apps.nowinandroid.core.domain.changeListSync
import com.google.samples.apps.nowinandroid.core.domain.model.asEntity
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.model.NetworkAuthor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room database backed implementation of the [AuthorsRepository].
 */
class LocalAuthorsRepository @Inject constructor(
    private val authorDao: AuthorDao,
    private val network: NiANetwork,
    private val niaPreferences: NiaPreferences,
) : AuthorsRepository {

    override fun getAuthorsStream(): Flow<List<Author>> =
        authorDao.getAuthorEntitiesStream()
            .map { it.map(AuthorEntity::asExternalModel) }

    override suspend fun setFollowedAuthorIds(followedAuthorIds: Set<Int>) =
        niaPreferences.setFollowedAuthorIds(followedAuthorIds)

    override suspend fun toggleFollowedAuthorId(followedAuthorId: Int, followed: Boolean) =
        niaPreferences.toggleFollowedAuthorId(followedAuthorId, followed)

    override fun getFollowedAuthorIdsStream(): Flow<Set<Int>> = niaPreferences.followedAuthorIds

    override suspend fun sync(): Boolean = changeListSync(
        niaPreferences = niaPreferences,
        versionReader = ChangeListVersions::authorVersion,
        changeListFetcher = { currentVersion ->
            network.getAuthorChangeList(after = currentVersion)
        },
        versionUpdater = { latestVersion ->
            copy(authorVersion = latestVersion)
        },
        modelUpdater = { changedIds ->
            val networkAuthors = network.getAuthors(ids = changedIds)
            authorDao.upsertAuthors(
                entities = networkAuthors.map(NetworkAuthor::asEntity)
            )
        }
    )
}
