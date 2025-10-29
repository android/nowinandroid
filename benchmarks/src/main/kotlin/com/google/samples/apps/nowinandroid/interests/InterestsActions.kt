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

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString
import com.google.samples.apps.nowinandroid.textVisibleOnTopAppBar

fun UiAutomatorTestScope.goToInterestsScreen() {
    onElement { textAsString() == "Interests" }.click()
    textVisibleOnTopAppBar("Interests")
    // Wait until content is loaded by checking if interests are loaded
    assert(onElementOrNull { viewIdResourceName == "loadingWheel" && !isVisibleToUser } == null)
}

fun UiAutomatorTestScope.interestsScrollTopicsDownUp() {
    onElement { viewIdResourceName == "interests:topics" }.run {
        // Set some margin from the sides to prevent triggering system navigation
        setGestureMargin(device.displayWidth / 5)
        fling(Direction.DOWN)
        fling(Direction.UP)
    }
}

fun UiAutomatorTestScope.interestsWaitForTopics() {
    onElement(30_000) { textAsString() == "Accessibility" }
}
