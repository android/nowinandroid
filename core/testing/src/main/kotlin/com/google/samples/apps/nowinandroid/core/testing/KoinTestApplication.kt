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

package com.google.samples.apps.nowinandroid.core.testing

import android.app.Application
import com.google.samples.apps.nowinandroid.core.testing.di.testDispatchersModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

/**
 * A test application that uses Koin for dependency injection.
 * Does not start Koin automatically - tests manage their own Koin lifecycle via SafeKoinTestRule.
 */
class KoinTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Do not start Koin here - let individual tests manage their own Koin lifecycle
        // This prevents conflicts with SafeKoinTestRule
    }
}