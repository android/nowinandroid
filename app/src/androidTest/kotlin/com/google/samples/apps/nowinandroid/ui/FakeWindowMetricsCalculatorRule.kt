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

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.toIntSize
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import androidx.window.layout.WindowMetricsCalculatorDecorator
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A test rule which allows overriding the reported WindowMetrics with an arbitrary size, defaulting
 * to [Size.Zero]. The size is not persisted and will be reset after each test.
 */
class FakeWindowMetricsCalculatorRule : TestRule {

    private val calculator = FakeWindowMetricsCalculator()

    fun setWindowSize(size: Size) {
        calculator.windowSize = size
    }

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                WindowMetricsCalculator.overrideDecorator(
                    object : WindowMetricsCalculatorDecorator {
                        override fun decorate(calculator: WindowMetricsCalculator): WindowMetricsCalculator =
                            calculator
                    },
                )
                try {
                    base?.evaluate()
                } finally {
                    WindowMetricsCalculator.reset()
                    calculator.resetWindowSize()
                }
            }
        }
    }
}

internal class FakeWindowMetricsCalculator : WindowMetricsCalculator {

    var windowSize = Size.Zero

    fun resetWindowSize() {
        windowSize = Size.Zero
    }

    override fun computeCurrentWindowMetrics(context: Context) = compute()

    override fun computeMaximumWindowMetrics(context: Context) = compute()

    override fun computeCurrentWindowMetrics(activity: Activity) = compute()

    override fun computeMaximumWindowMetrics(activity: Activity) = compute()

    private fun compute(): WindowMetrics = windowSize.toIntSize().run {
        WindowMetrics(Rect(0, 0, width, height))
    }
}
