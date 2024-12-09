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

package com.google.samples.apps.nowinandroid.interests

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import com.google.samples.apps.nowinandroid.PACKAGE_NAME
import com.google.samples.apps.nowinandroid.startActivityAndAllowNotifications
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopicsScreenRecompositionBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun benchmarkStateChangeCompilationBaselineProfile() =
        benchmarkStateChange(CompilationMode.Partial())

    private fun benchmarkStateChange(compilationMode: CompilationMode) =
        benchmarkRule.measureRepeated(
            packageName = PACKAGE_NAME,
            metrics = listOf(FrameTimingMetric()),
            compilationMode = compilationMode,
            iterations = 10,
            startupMode = StartupMode.WARM,
            setupBlock = {
                // Start the app
                pressHome()
                startActivityAndAllowNotifications()
                // Navigate to interests screen
                device.findObject(By.text("Interests")).click()
                device.waitForIdle()
            },
        ) {
            interestsWaitForTopics()
            repeat(3) {
                interestsToggleBookmarked()
            }
        }
}
