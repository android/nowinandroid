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

package com.google.samples.apps.nowinandroid.core.database

import app.cash.sqldelight.db.SqlDriver
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceFtsDao
import com.google.samples.apps.nowinandroid.core.database.dao.RecentSearchQueryDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicFtsDao
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Provides

internal object DatabaseModule {
    @Provides
    fun providesNiaDatabase(driver: SqlDriver): NiaDatabase = NiaDatabase(driver)

    @Provides
    fun providesTopicsDao(
        database: NiaDatabase,
    ): TopicDao = TopicDao(database, Dispatchers.Default)

    @Provides
    fun providesNewsResourceDao(
        database: NiaDatabase,
    ): NewsResourceDao = NewsResourceDao(database, Dispatchers.Default)

    @Provides
    fun providesTopicFtsDao(
        database: NiaDatabase,
    ): TopicFtsDao = TopicFtsDao(database, Dispatchers.Default)

    @Provides
    fun providesNewsResourceFtsDao(
        database: NiaDatabase,
    ): NewsResourceFtsDao = NewsResourceFtsDao(database, Dispatchers.Default)

    @Provides
    fun providesRecentSearchQueryDao(
        database: NiaDatabase,
    ): RecentSearchQueryDao = RecentSearchQueryDao(database, Dispatchers.Default)
}
