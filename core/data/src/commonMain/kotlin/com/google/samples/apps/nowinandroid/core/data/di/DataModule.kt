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

package com.google.samples.apps.nowinandroid.core.data.di

import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.repository.DefaultRecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.repository.DefaultSearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstNewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstTopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstUserDataRepository
import com.google.samples.apps.nowinandroid.core.data.repository.RecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val repositoryModule = module {
    singleOf(::OfflineFirstTopicsRepository) bind TopicsRepository::class
    singleOf(::OfflineFirstNewsRepository) bind NewsRepository::class
    singleOf(::OfflineFirstUserDataRepository) bind UserDataRepository::class
    singleOf(::DefaultRecentSearchRepository) bind RecentSearchRepository::class
    singleOf(::CompositeUserNewsResourceRepository) bind UserNewsResourceRepository::class
    singleOf(::DefaultSearchContentsRepository) bind SearchContentsRepository::class
}

internal expect val networkMonitorModule: Module
internal expect val timeZoneMonitorModule: Module

val dataModule: Module get() = module {
    includes(
        repositoryModule,
        networkMonitorModule,
        timeZoneMonitorModule,
    )
}
