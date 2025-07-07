/*
 * Copyright 2023 The Android Open Source Project
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

import android.view.accessibility.AccessibilityNodeInfo
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.onElement
import androidx.test.uiautomator.textAsString
import androidx.test.uiautomator.watcher.PermissionDialog

/**
 * Wraps starting the default activity, waiting for it to start and then allowing permissions in
 * one convenient call.
 */
fun UiAutomatorTestScope.startAppAndAllowPermission() {
    startApp(PACKAGE_NAME)
    watchFor(PermissionDialog) {
        clickAllow()
    }
}

fun UiAutomatorTestScope.textVisibleOnTopAppBar(text: String) =
    onTopAppBar { textAsString() == text && isVisibleToUser }

fun UiAutomatorTestScope.onTopAppBar(
    timeoutMs: Long = 10000,
    pollIntervalMs: Long = 100,
    block: AccessibilityNodeInfo.() -> (Boolean),
) {
    onElement { viewIdResourceName == "niaTopAppBar" }.onElement(timeoutMs, pollIntervalMs, block)
}
