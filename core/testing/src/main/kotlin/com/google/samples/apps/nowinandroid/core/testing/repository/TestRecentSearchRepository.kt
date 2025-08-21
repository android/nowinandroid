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

package com.google.samples.apps.nowinandroid.core.testing.repository

import com.google.samples.apps.nowinandroid.core.data.model.RecentSearchQuery
import com.google.samples.apps.nowinandroid.core.data.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestRecentSearchRepository : RecentSearchRepository {

    private val cachedRecentSearches: MutableList<RecentSearchQuery> = mutableListOf()

    override fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> =
        flowOf(cachedRecentSearches.sortedByDescending { it.queriedDate }.take(limit))

    override suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
        cachedRecentSearches.add(RecentSearchQuery(searchQuery))
    }

    override suspend fun clearRecentSearches() = cachedRecentSearches.clear()
}
