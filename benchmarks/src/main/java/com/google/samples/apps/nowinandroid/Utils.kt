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

package com.google.samples.apps.nowinandroid

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.google.samples.apps.nowinandroid.benchmarks.BuildConfig

/**
 * Convenience parameter to use proper package name with regards to build type and build flavor.
 */
val PACKAGE_NAME = buildString {
    append("com.google.samples.apps.nowinandroid")
    append(BuildConfig.APP_FLAVOR_SUFFIX)
    append(BuildConfig.APP_BUILD_TYPE_SUFFIX)
}

fun UiDevice.flingElementDownUp(element: UiObject2) {
    // Set some margin from the sides to prevent triggering system navigation
    element.setGestureMargin(displayWidth / 5)

    element.fling(Direction.DOWN)
    waitForIdle()
    element.fling(Direction.UP)
}
