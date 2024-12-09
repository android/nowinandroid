/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.data.repository

import com.google.samples.apps.nowinandroid.core.data.model.RecentSearchQuery
import com.google.samples.apps.nowinandroid.core.data.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.database.dao.RecentSearchQueryDao
import com.google.samples.apps.nowinandroid.core.database.model.RecentSearchQueryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

internal class DefaultRecentSearchRepository @Inject constructor(
    private val recentSearchQueryDao: RecentSearchQueryDao,
) : RecentSearchRepository {
    override suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
        recentSearchQueryDao.insertOrReplaceRecentSearchQuery(
            RecentSearchQueryEntity(
                query = searchQuery,
                queriedDate = Clock.System.now(),
            ),
        )
    }

    override fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> =
        recentSearchQueryDao.getRecentSearchQueryEntities(limit).map { searchQueries ->
            searchQueries.map { it.asExternalModel() }
        }

    override suspend fun clearRecentSearches() = recentSearchQueryDao.clearRecentSearchQueries()
}
