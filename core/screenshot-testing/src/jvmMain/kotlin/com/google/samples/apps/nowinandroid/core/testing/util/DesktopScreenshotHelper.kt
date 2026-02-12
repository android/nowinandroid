/*
 * Copyright 2026 The Android Open Source Project
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

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziOptions.CompareOptions
import com.github.takahirom.roborazzi.RoborazziOptions.RecordOptions
import com.github.takahirom.roborazzi.captureRoboImage

val DefaultRoborazziOptions =
    RoborazziOptions(
        // Pixel-perfect matching
        compareOptions = CompareOptions(changeThreshold = 0f),
        // Reduce the size of the PNGs
        recordOptions = RecordOptions(resizeScale = 0.5),
    )

enum class DefaultDesktopTestSizes(val description: String, val width: Int, val height: Int) {
    COMPACT("compact", 400, 800),
    MEDIUM("medium", 700, 900),
    EXPANDED("expanded", 1200, 800),
}

@OptIn(ExperimentalTestApi::class)
fun captureDesktopScreenshot(
    screenshotName: String,
    width: Int = 800,
    height: Int = 600,
    roborazziOptions: RoborazziOptions = DefaultRoborazziOptions,
    content: @Composable () -> Unit,
) = runDesktopComposeUiTest(width = width, height = height) {
    setContent { content() }
    onRoot().captureRoboImage(
        filePath = "src/jvmTest/screenshots/$screenshotName.png",
        roborazziOptions = roborazziOptions,
    )
}

@OptIn(ExperimentalTestApi::class)
fun captureMultiSize(
    screenshotName: String,
    roborazziOptions: RoborazziOptions = DefaultRoborazziOptions,
    content: @Composable () -> Unit,
) {
    DefaultDesktopTestSizes.entries.forEach { size ->
        runDesktopComposeUiTest(width = size.width, height = size.height) {
            setContent { content() }
            onRoot().captureRoboImage(
                filePath = "src/jvmTest/screenshots/${screenshotName}_${size.description}.png",
                roborazziOptions = roborazziOptions,
            )
        }
    }
}
