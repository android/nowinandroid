/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.google.samples.apps.nowinandroid.foryou

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.MemoryUsageMetric.Mode.Max
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.TraceSectionMetric.Mode.Sum
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.samples.apps.nowinandroid.PACKAGE_NAME
import com.google.samples.apps.nowinandroid.allowNotifications
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@OptIn(ExperimentalMetricApi::class)
class SelectTopicBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun benchmarkCompilationNone() = benchmark(CompilationMode.None())

    @Test
    fun benchmarkCompilationBaselineProfile() = benchmark(CompilationMode.Partial())

    @Test
    fun benchmarkCompilationFull() = benchmark(CompilationMode.Full())

    private fun benchmark(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = listOf(
            FrameTimingMetric(),
            MemoryUsageMetric(Max),
            TraceSectionMetric("SingleTopicButton", Sum),
        ),
        compilationMode = compilationMode,
        iterations = 10,
        setupBlock = {
            // kill process to simulate COLD startup. [StartupMode.COLD] should be just used for startup benchmarks.
            killProcess()
            startActivityAndWait()
            allowNotifications()
            forYouWaitForContent()
            // Clear any previously selected topics in setupBlock, so we have stable number of topics.
            forYouClearSelectedTopics()
            forYouSelectTopics(false, 1)
        },
    ) {
        // Select just one topic to see what recomposes and what's the impact
        forYouSelectTopics(true, 1, 1)
    }
}
