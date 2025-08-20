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

package com.google.samples.apps.nowinandroid.core.testing.util

import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * Utility to safely manage Koin context in tests.
 * Ensures clean state between test runs.
 */
object KoinTestUtil {
    
    /**
     * Safely stops Koin context if it exists.
     * Useful for cleanup between tests.
     */
    fun stopKoinSafely() {
        try {
            if (GlobalContext.getOrNull() != null) {
                GlobalContext.stopKoin()
            }
        } catch (e: Exception) {
            // Ignore errors when stopping Koin
        }
    }
    
    /**
     * Starts Koin with given modules after ensuring clean state.
     */
    fun startKoinSafely(modules: List<Module>) {
        stopKoinSafely()
        startKoin {
            modules(modules)
        }
    }
}