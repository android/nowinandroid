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

package com.google.samples.apps.nowinandroid.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.samples.apps.nowinandroid.core.datastore.IntToStringIdsMigration
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferencesDataSource
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferences
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferencesSerializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataStoreModule = module {

    // UserPreferencesSerializer
    singleOf(::UserPreferencesSerializer)
    
    // NiaPreferencesDataSource
    singleOf(::NiaPreferencesDataSource)

    single<DataStore<UserPreferences>> {
        DataStoreFactory.create(
            serializer = get<UserPreferencesSerializer>(),
            scope = CoroutineScope(get<CoroutineScope>(named("ApplicationScope")).coroutineContext + get<CoroutineDispatcher>(named("IO"))),
            migrations = listOf(
                IntToStringIdsMigration,
            ),
        ) {
            androidContext().dataStoreFile("user_preferences.pb")
        }
    }
}