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

package com.google.samples.apps.nowinandroid.bookmarks

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import com.google.samples.apps.nowinandroid.flingElementDownUp

fun MacrobenchmarkScope.goToBookmarksScreen() {
    device.findObject(By.text("Saved")).click()
    device.waitForIdle()
    // Wait until saved title are shown on screen
    device.wait(Until.hasObject(By.res("niaTopAppBar")), 2_000)
    val topAppBar = device.findObject(By.res("niaTopAppBar"))
    topAppBar.wait(Until.hasObject(By.text("Saved")), 2_000)
}

fun MacrobenchmarkScope.bookmarksScrollFeedDownUp() {
    val feedList = device.findObject(By.res("bookmarks:feed"))
    device.flingElementDownUp(feedList)
}
