/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.ui

import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children

/**
 * A [DeviceConfigurationOverride] that allows overriding the [windowInsets] available
 * to the content under test.
 */
@Suppress("ktlint:standard:function-naming")
fun DeviceConfigurationOverride.Companion.WindowInsets(
    windowInsets: WindowInsetsCompat,
): DeviceConfigurationOverride = DeviceConfigurationOverride { contentUnderTest ->
    val currentContentUnderTest by rememberUpdatedState(contentUnderTest)
    val currentWindowInsets by rememberUpdatedState(windowInsets)
    AndroidView(
        factory = { context ->
            object : FrameLayout(context) {
                override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
                    children.forEach {
                        it.dispatchApplyWindowInsets(currentWindowInsets.toWindowInsets())
                    }
                    return WindowInsetsCompat.CONSUMED.toWindowInsets()!!
                }

                /**
                 * Deprecated, but intercept the `requestApplyInsets` call via the deprecated
                 * method.
                 */
                @Deprecated("Deprecated in Java")
                override fun requestFitSystemWindows() {
                    dispatchApplyWindowInsets(currentWindowInsets.toWindowInsets()!!)
                }
            }.apply {
                addView(
                    ComposeView(context).apply {
                        setContent {
                            currentContentUnderTest()
                        }
                    },
                )
            }
        },
    )
}
