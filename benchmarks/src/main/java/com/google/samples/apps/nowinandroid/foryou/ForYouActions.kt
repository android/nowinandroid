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

package com.google.samples.apps.nowinandroid.foryou

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import com.google.samples.apps.nowinandroid.flingElementDownUp

fun MacrobenchmarkScope.forYouWaitForContent() {
    // Wait until content is loaded
    device.wait(Until.hasObject(By.text("What are you interested in?")), 30_000)
}

fun MacrobenchmarkScope.forYouSelectAuthors() {
    val authors = device.findObject(By.res("forYou:authors"))
    // select some authors to show some feed content
    repeat(3) { index ->
        val author = authors.children[index % authors.childCount]
        author.click()
        device.waitForIdle()
    }
}

fun MacrobenchmarkScope.forYouScrollFeedDownUp() {
    val feedList = device.findObject(By.res("forYou:feed"))
    device.flingElementDownUp(feedList)
}
