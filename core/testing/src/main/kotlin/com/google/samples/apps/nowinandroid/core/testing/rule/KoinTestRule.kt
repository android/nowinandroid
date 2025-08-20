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

package com.google.samples.apps.nowinandroid.core.testing.rule

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * Custom test rule for managing Koin lifecycle in tests.
 * Ensures clean state between tests to prevent context conflicts.
 */
class SafeKoinTestRule(
    private val modules: List<Module>
) : TestRule {
    
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                // Clean up any existing Koin context
                stopKoinSafely()
                
                // Start fresh Koin context with Android context
                startKoin {
                    androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
                    modules(this@SafeKoinTestRule.modules)
                }
                
                try {
                    base.evaluate()
                } finally {
                    // Clean up after test
                    stopKoinSafely()
                }
            }
        }
    }
    
    private fun stopKoinSafely() {
        try {
            if (GlobalContext.getOrNull() != null) {
                GlobalContext.stopKoin()
            }
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
    }
    
    companion object {
        fun create(modules: List<Module>) = SafeKoinTestRule(modules)
    }
}