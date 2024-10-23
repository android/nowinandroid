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

package com.google.samples.apps.nowinandroid.di

import com.google.samples.apps.nowinandroid.MainScreenViewModel
import com.google.samples.apps.nowinandroid.core.analytics.di.AnalyticsModule
import com.google.samples.apps.nowinandroid.core.data.di.dataModule
import com.google.samples.apps.nowinandroid.core.database.di.databaseModule
import com.google.samples.apps.nowinandroid.core.datastore.di.dataStoreModule
import com.google.samples.apps.nowinandroid.core.di.commonModule
import com.google.samples.apps.nowinandroid.core.network.di.networkModule
import com.google.samples.apps.nowinandroid.feature.bookmarks.di.bookmarksModule
import com.google.samples.apps.nowinandroid.feature.foryou.di.forYouModule
import com.google.samples.apps.nowinandroid.feature.interests.di.interestModule
import com.google.samples.apps.nowinandroid.feature.search.di.searchModule
import com.google.samples.apps.nowinandroid.feature.settings.di.settingsModule
import com.google.samples.apps.nowinandroid.feature.topic.di.topicModule
import com.google.samples.apps.nowinandroid.ui.interests2pane.Interests2PaneViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
@ComponentScan
class AppModule

internal val appViewModelModule = module {
    viewModelOf(::MainScreenViewModel)
    viewModelOf(::Interests2PaneViewModel)
}

internal val featureModules = module {
    includes(
        forYouModule,
        interestModule,
        topicModule,
        bookmarksModule,
        searchModule,
        settingsModule,
    )
}

internal val appModules = module {
    includes(
        *commonModule().toTypedArray(),
        AnalyticsModule().module,
        *databaseModule().toTypedArray(),
        dataModule,
        dataStoreModule(),
        *networkModule().toTypedArray(),
    )
    includes(featureModules)
    includes(appViewModelModule)
    includes(AppModule().module)
}
