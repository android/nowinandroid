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

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalView
import androidx.metrics.performance.PerformanceMetricsState
import androidx.metrics.performance.PerformanceMetricsState.MetricsStateHolder
import kotlinx.coroutines.CoroutineScope

/**
 * Retrieves [PerformanceMetricsState.MetricsStateHolder] from current [LocalView] and
 * remembers it until the View changes.
 * @see PerformanceMetricsState.getForHierarchy
 */
@Composable
fun rememberMetricsStateHolder(): MetricsStateHolder {
    val localView = LocalView.current

    return remember(localView) {
        PerformanceMetricsState.getForHierarchy(localView)
    }
}

/**
 * Convenience function to work with [PerformanceMetricsState] state. The side effect is
 * re-launched if any of the [keys] value is not equal to the previous composition.
 * @see JankMetricDisposableEffect if you need to work with DisposableEffect to cleanup added state.
 */
@Composable
fun JankMetricEffect(
    vararg keys: Any?,
    reportMetric: suspend CoroutineScope.(state: MetricsStateHolder) -> Unit
) {
    val metrics = rememberMetricsStateHolder()
    LaunchedEffect(metrics, *keys) {
        reportMetric(metrics)
    }
}

/**
 * Convenience function to work with [PerformanceMetricsState] state that needs to be cleaned up.
 * The side effect is re-launched if any of the [keys] value is not equal to the previous composition.
 */
@Composable
fun JankMetricDisposableEffect(
    vararg keys: Any?,
    reportMetric: DisposableEffectScope.(state: MetricsStateHolder) -> DisposableEffectResult
) {
    val metrics = rememberMetricsStateHolder()
    DisposableEffect(metrics, *keys) {
        reportMetric(this, metrics)
    }
}

@Composable
fun TrackScrollJank(scrollableState: ScrollableState, stateName: String) {
    JankMetricEffect(scrollableState) { metricsHolder ->
        snapshotFlow { scrollableState.isScrollInProgress }.collect { isScrollInProgress ->
            metricsHolder.state?.apply {
                if (isScrollInProgress) {
                    addState(stateName, "Scrolling=true")
                } else {
                    removeState(stateName)
                }
            }
        }
    }
}
