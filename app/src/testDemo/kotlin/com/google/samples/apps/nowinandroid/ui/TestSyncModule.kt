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

package com.google.samples.apps.nowinandroid.ui

import com.google.samples.apps.nowinandroid.core.data.util.SyncManager
import com.google.samples.apps.nowinandroid.core.testing.util.TestSyncManager
import com.google.samples.apps.nowinandroid.sync.di.SyncModule
import com.google.samples.apps.nowinandroid.sync.status.StubSyncSubscriber
import com.google.samples.apps.nowinandroid.sync.status.SyncSubscriber
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

/**
 * Test Module to avoid WorkManager initialization in tests.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SyncModule::class],
)
abstract class TestSyncModule {
    @Binds
    internal abstract fun bindsSyncSubscriber(
        syncSubscriber: StubSyncSubscriber,
    ): SyncSubscriber

    companion object {
        @Provides
        fun providesSyncManager(): SyncManager = TestSyncManager()
    }
}
