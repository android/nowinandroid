/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.database.di

import app.cash.sqldelight.db.SqlDriver
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceFtsDao
import com.google.samples.apps.nowinandroid.core.database.dao.RecentSearchQueryDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicFtsDao
import com.google.samples.apps.nowinandroid.core.di.IODispatcher
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
internal abstract class DatabaseModule {
    @Provides
    fun providesNiaDatabase(driver: SqlDriver): NiaDatabase = NiaDatabase(driver)

    @Provides
    fun providesTopicsDao(
        database: NiaDatabase,
        dispatcher: IODispatcher,
    ): TopicDao = TopicDao(database, dispatcher)

    @Provides
    fun providesNewsResourceDao(
        database: NiaDatabase,
        dispatcher: IODispatcher,
    ): NewsResourceDao = NewsResourceDao(database, dispatcher)

    @Provides
    fun providesTopicFtsDao(
        database: NiaDatabase,
        dispatcher: IODispatcher,
    ): TopicFtsDao = TopicFtsDao(database, dispatcher)

    @Provides
    fun providesNewsResourceFtsDao(
        database: NiaDatabase,
        dispatcher: IODispatcher,
    ): NewsResourceFtsDao = NewsResourceFtsDao(database, dispatcher)

    @Provides
    fun providesRecentSearchQueryDao(
        database: NiaDatabase,
        dispatcher: IODispatcher,
    ): RecentSearchQueryDao = RecentSearchQueryDao(database, dispatcher)
}
