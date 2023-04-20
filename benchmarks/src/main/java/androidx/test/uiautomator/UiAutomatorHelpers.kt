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

package androidx.test.uiautomator

import android.util.Log
import androidx.test.uiautomator.HasChildrenOp.AT_LEAST
import androidx.test.uiautomator.HasChildrenOp.AT_MOST
import androidx.test.uiautomator.HasChildrenOp.EXACTLY
import java.io.ByteArrayOutputStream

// These helpers need to be in the androidx.test.uiautomator package,
// because the abstract class has package local method that needs to be implemented.

/**
 * Condition will be satisfied if given element has specified count of children
 */
fun untilHasChildren(
    childCount: Int = 1,
    op: HasChildrenOp = AT_LEAST,
): UiObject2Condition<Boolean> {
    return object : UiObject2Condition<Boolean>() {
        override fun apply(element: UiObject2): Boolean {
            return when (op) {
                AT_LEAST -> element.childCount >= childCount
                EXACTLY -> element.childCount == childCount
                AT_MOST -> element.childCount <= childCount
            }
        }
    }
}

enum class HasChildrenOp {
    AT_LEAST,
    EXACTLY,
    AT_MOST,
}

/**
 * Finds an element by [selector].
 * If not found, fails with [AssertionError] and dumps the window hierarchy.
 */
fun UiDevice.findOrFail(
    selector: BySelector,
    message: String? = null,
): UiObject2 {
    val element = findObject(selector)
    if (element == null) {
        val hierarchy = getWindowHierarchy()
        Log.d("Benchmark", hierarchy)
        throw AssertionError((message ?: "Element not on screen ($selector)") + "\n$hierarchy")
    }
    return element
}

/**
 * Waits until a [searchCondition] evaluates to true.
 * If not found within [timeout], fails with [AssertionError] and dumps the window hierarchy.
 * For example, wait [Until.hasObject] to wait until an element is present on screen.
 */
fun UiDevice.waitOrFail(
    searchCondition: SearchCondition<Boolean>,
    timeout: Long,
    message: String? = null,
) {
    // If successfully found, just return
    if (wait(searchCondition, timeout)) return

    val hierarchy = getWindowHierarchy()
    Log.d("Benchmark", hierarchy)
    throw AssertionError((message ?: "Element not on screen") + "\n$hierarchy")
}

/**
 * Combines waiting for an element and returning it.
 * If an object is not present, it throws [AssertionError] and dumps the window hierarchy.
 */
fun UiDevice.waitAndFind(
    selector: BySelector,
    timeout: Long = 5_000,
    message: String? = null,
): UiObject2 {
    waitOrFail(Until.hasObject(selector), timeout, message)
    return findOrFail(selector, message)
}

/**
 * Simplifies dumping window hierarchy
 */
fun UiDevice.getWindowHierarchy(): String {
    val output = ByteArrayOutputStream()
    dumpWindowHierarchy(output)
    return output.toString()
}
