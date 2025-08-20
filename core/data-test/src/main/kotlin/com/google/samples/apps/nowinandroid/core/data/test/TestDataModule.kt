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

package com.google.samples.apps.nowinandroid.core.data.test

import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.RecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeNewsRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeRecentSearchRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeSearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeTopicsRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeUserDataRepository
import com.google.samples.apps.nowinandroid.core.data.test.AlwaysOnlineNetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.test.DefaultZoneIdTimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import org.koin.core.qualifier.named
import org.koin.dsl.module

val testDataModule = module {
    single<TopicsRepository> { FakeTopicsRepository(get(named("IO")), get()) }
    single<NewsRepository> { FakeNewsRepository(get(named("IO")), get()) }
    single<UserDataRepository> { FakeUserDataRepository(get()) }
    single<RecentSearchRepository> { FakeRecentSearchRepository() }
    single<SearchContentsRepository> { FakeSearchContentsRepository() }
    single<NetworkMonitor> { AlwaysOnlineNetworkMonitor() }
    single<TimeZoneMonitor> { DefaultZoneIdTimeZoneMonitor() }
    single<UserNewsResourceRepository> { CompositeUserNewsResourceRepository(get(), get()) }
}

val testScopeModule = module {
    single<kotlinx.coroutines.CoroutineScope>(named("ApplicationScope")) { kotlinx.coroutines.test.TestScope() }
}
