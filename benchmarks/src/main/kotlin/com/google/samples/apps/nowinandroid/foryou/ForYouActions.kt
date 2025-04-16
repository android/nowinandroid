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

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString
import com.google.samples.apps.nowinandroid.textVisibleOnTopAppBar
import org.junit.Assert.fail

fun UiAutomatorTestScope.forYouWaitForContent() {
    onViewOrNull(timeoutMs = 500) { viewIdResourceName == "loadingWheel" && !isVisibleToUser }
    // Sometimes, the loading wheel is gone, but the content is not loaded yet
    // So we'll wait here for topics to be sure
    // Timeout here is quite big, because sometimes data loading takes a long time!
    onView(timeoutMs = 30_000) {
        viewIdResourceName == "forYou:topicSelection" && childCount > 0 && isVisibleToUser
    }
}

/**
 * Selects some topics, which will show the feed content for them.
 * [recheckTopicsIfChecked] Topics may be already checked from the previous iteration.
 */
fun UiAutomatorTestScope.forYouSelectTopics(recheckTopicsIfChecked: Boolean = false) {
    onView { viewIdResourceName == "forYou:topicSelection" }.run {
        if (children.isEmpty()) {
            fail("No topics found, can't generate profile for ForYou page.")
        }

        val horizontalMargin = visibleBounds.width() / 10
        setGestureMargins(horizontalMargin, 0, horizontalMargin, 0)

        // Filter for any checkable and unchecked children unless recheck is enabled
        children.filter { child ->
            child.isCheckable and (recheckTopicsIfChecked or !child.isChecked)
        }.forEach { child ->
            // recheck check to uncheck before recheck
            if (child.isChecked) {
                child.click()
            }
            child.click()
        }
    }
}

fun UiAutomatorTestScope.forYouScrollFeedDownUp() {
    onView { viewIdResourceName == "forYou:feed" }.run {
        fling(Direction.DOWN)
        fling(Direction.UP)
    }
}

fun UiAutomatorTestScope.setAppTheme(isDark: Boolean) {
    onView { textAsString == if (isDark) "Dark" else "Light" }.click()
    onView { textAsString == "OK" }.click()
    textVisibleOnTopAppBar("Now in Android")
}
