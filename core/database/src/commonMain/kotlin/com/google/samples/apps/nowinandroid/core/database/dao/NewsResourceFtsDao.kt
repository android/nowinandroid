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
import app.cash.sqldelight.coroutines.mapToOneNotNull
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceFtsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DAO for [NewsResourceFtsEntity] access.
 */
class NewsResourceFtsDao(db: NiaDatabase, private val dispatcher: CoroutineDispatcher) {
    private val dbQuery = db.newsResourceFtsQueries
    suspend fun insertAll(newsResources: List<NewsResourceFtsEntity>) {
        newsResources.forEach {
            dbQuery.insert(
                news_resource_id = it.newsResourceId,
                title = it.title,
                content = it.content,
            )
        }
    }

    fun searchAllNewsResources(query: String): Flow<List<String>> {
        return dbQuery.searchAllNewsResources(query)
            .asFlow()
            .mapToList(dispatcher)
    }

    fun getCount(): Flow<Int> {
        return dbQuery.getCount()
            .asFlow()
            .mapToOneNotNull(dispatcher)
            .map { it.toInt() }
    }
}
