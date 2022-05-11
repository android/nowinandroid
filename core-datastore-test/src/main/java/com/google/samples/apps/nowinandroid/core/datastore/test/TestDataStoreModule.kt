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

package com.google.samples.apps.nowinandroid.core.datastore.test

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferences
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferencesSerializer
import com.google.samples.apps.nowinandroid.core.datastore.di.DataStoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import org.junit.rules.TemporaryFolder

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        userPreferencesSerializer: UserPreferencesSerializer,
        tmpFolder: TemporaryFolder
    ): DataStore<UserPreferences> =
        tmpFolder.testUserPreferencesDataStore(userPreferencesSerializer)
}

fun TemporaryFolder.testUserPreferencesDataStore(
    userPreferencesSerializer: UserPreferencesSerializer = UserPreferencesSerializer()
) = DataStoreFactory.create(
    serializer = userPreferencesSerializer,
) {
    newFile("user_preferences_test.pb")
}
