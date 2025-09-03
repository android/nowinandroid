/*
 * Copyright 2025 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.userjourney

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.scrollToElement
import androidx.test.uiautomator.textAsString
import androidx.test.uiautomator.uiAutomator
import androidx.test.uiautomator.waitForStable
import androidx.test.uiautomator.watcher.PermissionDialog
import com.google.samples.apps.nowinandroid.BaselineProfileMetrics
import com.google.samples.apps.nowinandroid.ITERATIONS
import com.google.samples.apps.nowinandroid.PACKAGE_NAME
import com.google.samples.apps.nowinandroid.foryou.forYouSelectTopics
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewUserJourneyBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun measureJourney() = benchmarkRule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = BaselineProfileMetrics.allMetrics,
        compilationMode = CompilationMode.DEFAULT,
        startupMode = StartupMode.COLD,
        iterations = ITERATIONS,
    ) {
        uiAutomator {
            startApp(PACKAGE_NAME)
            watchFor(PermissionDialog) {
                clickAllow()
            }
            // Select some topics
            forYouSelectTopics()
            onElement { textAsString() == "Done" }.click()
            onElement { isScrollable }.scroll(Direction.DOWN, 0.25f)
            onElement { textAsString() == "Interests" }.click()

            activeWindowRoot().waitForStable()

            onElement { isScrollable }.scrollToElement(
                direction = Direction.DOWN,
                block = {
                    textAsString() == "Performance"
                },
            ).click()

            // Enable dynamic color in settings
            onElement { contentDescription == "Settings" }.click()
            onElement { textAsString() == "Yes" }.click()
            pressBack()

            // Search for "AndroidX releases"
            onElement { contentDescription == "Search" }.click()
            // Wait for animations to finish
            activeWindowRoot().waitForStable()
            type("AndroidX releases")
            pressEnter()
        }
    }
}
