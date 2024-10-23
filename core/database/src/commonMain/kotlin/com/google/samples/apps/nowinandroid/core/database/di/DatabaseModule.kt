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
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module

internal val driverModule = module {
    single { NiaDatabase.Schema }
    single<SqlDriver> {
        val driverProvider: DriverProvider = get()
        // TODO check if we can remove runBlocking
        runBlocking {
            driverProvider.provideDbDriver(get())
        }
    }
}

internal val daoModule = module {
    single { (driver: SqlDriver) -> NiaDatabase(driver) }

    factory { (database: NiaDatabase) -> TopicDao(database, get()) }

    factory { (database: NiaDatabase) -> NewsResourceDao(database, get()) }

    factory { (database: NiaDatabase) -> TopicFtsDao(database, get()) }

    factory { (database: NiaDatabase) -> NewsResourceFtsDao(database, get()) }

    factory { (database: NiaDatabase) ->
        RecentSearchQueryDao(
            database,
            get(),
        )
    }
}

fun databaseModule() = listOf(
    driverModule,
    DatabaseModule().module,
    daoModule,
)

@Module
@ComponentScan
class DatabaseModule
