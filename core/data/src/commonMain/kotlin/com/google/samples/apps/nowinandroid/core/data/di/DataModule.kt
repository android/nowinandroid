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
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstNewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstTopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.OfflineFirstUserDataRepository
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module

internal val repositoryModule = module {
    single { OfflineFirstTopicsRepository(get(), get()) }
    single { OfflineFirstNewsRepository(get(), get(), get(), get(), get()) }
    single { OfflineFirstUserDataRepository(get(), get()) }
    single { DefaultRecentSearchRepository(get()) }
    single {
        DefaultSearchContentsRepository(
            get(),
            get(),
            get(),
            get(),
            get(named("IoDispatcher")),
        )
    }
    single { CompositeUserNewsResourceRepository(get(), get()) }
}

internal val networkMonitorModule = module {
    single {
        val networkMonitorProvider: NetworkMonitorProvider = get()
        networkMonitorProvider.provideNetworkMonitor()
    }
}

internal val timeZoneMonitorProviderModule = module {
    single {
        val timeZoneMonitorProvider: TimeZoneMonitorProvider = get()
        timeZoneMonitorProvider.provideTimeZoneMonitor()
    }
}

val dataModule = listOf(
    DataModule().module,
    repositoryModule,
    networkMonitorModule,
    timeZoneMonitorProviderModule,
)

@Module
@ComponentScan
class DataModule
