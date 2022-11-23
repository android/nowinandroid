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
import androidx.test.uiautomator.Direction.RIGHT
import androidx.test.uiautomator.Until
import androidx.test.uiautomator.untilHasChildren
import com.google.samples.apps.nowinandroid.flingElementDownUp
import kotlin.random.Random.Default.nextInt

fun MacrobenchmarkScope.forYouWaitForContent() {
    // Wait until content is loaded by checking if authors are loaded
    device.wait(Until.gone(By.res("forYou:loadingWheel")), 5_000)
    // Sometimes, the loading wheel is gone, but the content is not loaded yet
    // So we'll wait here for authors to be sure
    val obj = device.findObject(By.res("forYou:authors"))
    obj.wait(untilHasChildren(), 30_000)
}

fun MacrobenchmarkScope.forYouSelectAuthors() {
    val authors = device.findObject(By.res("forYou:authors"))

    // set gesture margin from sides not to trigger system gesture navigation
    val horizontalMargin = 10 * authors.visibleBounds.width() / 100
    authors.setGestureMargins(horizontalMargin, 0, horizontalMargin, 0)

    // select some random authors to show some feed content
    repeat(3) {
        // picks randomly from visible authors
        val randomChild = nextInt(0, authors.childCount)
        val author = authors.children[randomChild]
        author.click()
        device.waitForIdle()
        authors.fling(RIGHT)
    }
}

fun MacrobenchmarkScope.forYouScrollFeedDownUp() {
    val feedList = device.findObject(By.res("forYou:feed"))
    device.flingElementDownUp(feedList)
}
