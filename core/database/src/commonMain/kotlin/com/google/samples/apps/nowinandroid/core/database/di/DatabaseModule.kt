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

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.google.samples.apps.nowinandroid.core.database.NiaDatabase
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDaoInterface
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceFtsDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceFtsDaoInterface
import com.google.samples.apps.nowinandroid.core.database.dao.RecentSearchQueryDao
import com.google.samples.apps.nowinandroid.core.database.dao.RecentSearchQueryDaoInterface
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDaoInterface
import com.google.samples.apps.nowinandroid.core.database.dao.TopicFtsDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicFtsDaoInterface
import com.google.samples.apps.nowinandroid.core.di.coroutineDispatcherModule
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val driverModule: Module

internal val schemaModule = module {
    single<SqlSchema<QueryResult.AsyncValue<Unit>>> { NiaDatabase.Schema }
}

internal val dbModule = module {
    single { (driver: SqlDriver) ->
        NiaDatabase(driver)
    }
}

internal val daoModule = module {
    factory<TopicDaoInterface> { (database: NiaDatabase) ->
        TopicDao(database, get())
    }

    factory <NewsResourceDaoInterface> { (database: NiaDatabase) ->
        NewsResourceDao(database, get())
    }

    factory<TopicFtsDaoInterface> { (database: NiaDatabase) ->
        TopicFtsDao(database, get())
    }

    factory<NewsResourceFtsDaoInterface> { (database: NiaDatabase) ->
        NewsResourceFtsDao(database, get())
    }

    factory<RecentSearchQueryDaoInterface> { (database: NiaDatabase) ->
        RecentSearchQueryDao(
            database,
            get(),
        )
    }
}

val databaseModule: Module get() = module {
    includes(coroutineDispatcherModule)
    includes(
        schemaModule,
        driverModule,
        dbModule,
        daoModule,
    )
}