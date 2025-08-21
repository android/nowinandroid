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
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TestRecentSearchRepository : RecentSearchRepository {

    private val cachedRecentSearches = MutableSharedFlow<MutableList<RecentSearchQuery>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    ).apply { tryEmit(mutableListOf()) }

    override fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> =
        cachedRecentSearches.map { it.sortedByDescending { it.queriedDate }.take(limit) }

    override suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
        val searchQueries = cachedRecentSearches.map { it.apply { add(RecentSearchQuery(searchQuery)) } }.first()
        cachedRecentSearches.emit(searchQueries)
    }

    override suspend fun clearRecentSearches() = cachedRecentSearches.emit(mutableListOf())
}
