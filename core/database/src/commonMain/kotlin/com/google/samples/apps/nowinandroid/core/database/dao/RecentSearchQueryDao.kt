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

package com.google.samples.apps.nowinandroid.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.Recent_search_query
import com.google.samples.apps.nowinandroid.core.database.model.RecentSearchQueryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

/**
 * DAO for [RecentSearchQueryEntity] access
 */
class RecentSearchQueryDao(db: NiaDatabase, private val dispatcher: CoroutineDispatcher) {

    private val query = db.recentSearchQueryQueries

    fun getRecentSearchQueryEntities(limit: Int): Flow<List<RecentSearchQueryEntity>> {
        return query.getRecentSearchQueryEntities(limit.toLong()) { query, timestamp ->
            RecentSearchQueryEntity(
                query = query,
                queriedDate = Instant.fromEpochMilliseconds(timestamp ?: 0L),
            )
        }
            .asFlow()
            .mapToList(dispatcher)
    }

    suspend fun insertOrReplaceRecentSearchQuery(recentSearchQuery: RecentSearchQueryEntity) {
        query.insertOrReplaceRecentSearchQuery(
            recent_search_query = Recent_search_query(
                query = recentSearchQuery.query,
                queried_date = recentSearchQuery.queriedDate.toEpochMilliseconds(),
            )
        )
    }

    suspend fun clearRecentSearchQueries() {
        query.clearRecentSearchQueries()
    }
}
