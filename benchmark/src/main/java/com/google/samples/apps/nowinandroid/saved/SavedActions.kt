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

package com.google.samples.apps.nowinandroid.saved

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until

fun MacrobenchmarkScope.savedWaitForContent() {
    // Wait until content is loaded
    device.wait(Until.hasObject(By.res("saved:feed")), 30_000)
}

fun MacrobenchmarkScope.savedScrollFeedDownUp() {
    val feedList = device.findObject(By.res("saved:feed"))
    feedList.fling(Direction.DOWN)
    device.waitForIdle()
    feedList.fling(Direction.UP)
}
