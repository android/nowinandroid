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

package com.google.samples.apps.nowinandroid.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.google.samples.apps.nowinandroid.data.UserPreferences
import com.google.samples.apps.nowinandroid.data.UserPreferencesSerializer
import com.google.samples.apps.nowinandroid.data.fake.FakeNewsRepository
import com.google.samples.apps.nowinandroid.data.fake.FakeTopicsRepository
import com.google.samples.apps.nowinandroid.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.data.repository.TopicsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import org.junit.rules.TemporaryFolder

/**
 * The [TestAppModule] replaces [AppModule] during instrumentation tests. It creates test doubles
 * where necessary. It also includes logic to prevent multiple data stores with the same file name
 * from being created during one test execution context.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
interface TestAppModule {

    // Use a fake repository as a test double, so we don't have a network dependency.
    @Binds
    fun bindsTopicRepository(fakeTopicsRepository: FakeTopicsRepository): TopicsRepository

    // Use a fake repository as a test double, so we don't have a network dependency.
    @Binds
    fun bindsNewsResourceRepository(fakeNewsRepository: FakeNewsRepository): NewsRepository

    // Use the default dispatchers. For the high-level UI tests, we don't want to override these.
    @Binds
    fun bindsNiaDispatchers(defaultNiaDispatchers: DefaultNiaDispatchers): NiaDispatchers

    companion object {

        @Provides
        @Singleton
        fun providesUserPreferencesDataStore(
            userPreferencesSerializer: UserPreferencesSerializer,
            tmpFolder: TemporaryFolder
        ): DataStore<UserPreferences> {
            return DataStoreFactory.create(
                serializer = userPreferencesSerializer,
            ) {
                tmpFolder.newFile("user_preferences_test.pb")
            }
        }

        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            ignoreUnknownKeys = true
        }
    }
}
