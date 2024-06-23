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

package com.google.samples.apps.nowinandroid.core.testing.di

import androidx.datastore.core.DataStore
import com.google.samples.apps.nowinandroid.core.data.di.DataModule
import com.google.samples.apps.nowinandroid.core.data.di.TestDataModule
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferences
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferencesSerializer
import com.google.samples.apps.nowinandroid.core.datastore.di.DataStoreModule
import com.google.samples.apps.nowinandroid.core.datastore.di.TestDataStoreModule
import com.google.samples.apps.nowinandroid.core.network.di.ApplicationScope
import com.google.samples.apps.nowinandroid.core.sync.di.TestSyncModule
import com.google.samples.apps.nowinandroid.sync.di.SyncModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton

/**
 * KSP is currently not supported on Android testFixtures ([issuetracker](https://issuetracker.google.com/issues/259523353#comment32)).
 *
 * Including [TestDataStoreModule] in [Module.includes] leads to an unexpected compilation error (maybe due to datastore-proto and KSP ordering, certainly related to the initial issue).
 *
 * ```
 * > Task :app:hiltJavaCompileDemoDebugAndroidTest FAILED
 * nowinandroid\app\build\generated\hilt\component_sources\demoDebugAndroidTest\dagger\hilt\android\internal\testing\root\DaggerNavigationTest_HiltComponents_SingletonC.java:38: error: cannot find symbol
 * import com.google.samples.apps.nowinandroid.core.datastore.di.TestDataStoreModule_ProvidesUserPreferencesDataStoreFactory;
 *                                                              ^
 *   symbol:   class TestDataStoreModule_ProvidesUserPreferencesDataStoreFactory
 *   location: package com.google.samples.apps.nowinandroid.core.datastore.di
 * ```
 *
 * Therefore, a [providesUserPreferencesDataStore] delegate is added in this module.
 */
@Module(includes = [TestDataModule::class, TestSyncModule::class])
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class, SyncModule::class, DataStoreModule::class],
)
internal object TestingModule {
    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<UserPreferences> = TestDataStoreModule.providesUserPreferencesDataStore(
        scope,
        userPreferencesSerializer,
        tmpFolder,
    )
}
