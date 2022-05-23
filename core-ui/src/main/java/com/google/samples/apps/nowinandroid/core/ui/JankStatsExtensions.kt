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

package com.google.samples.apps.nowinandroid.core.ui

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.metrics.performance.PerformanceMetricsState

fun View.addPerformanceMetricsState(stateName: String, state: String) {
    PerformanceMetricsState.getForHierarchy(this).state?.addState(stateName, state)
}

/**
 * Retrieves [PerformanceMetricsState.MetricsStateHolder] from current [LocalView] and
 * remembers it until the View changes.
 * @see PerformanceMetricsState.getForHierarchy
 */
@Composable
fun rememberMetricsStateHolder(): PerformanceMetricsState.MetricsStateHolder {
    val localView = LocalView.current

    return remember(localView) {
        PerformanceMetricsState.getForHierarchy(localView)
    }
}
