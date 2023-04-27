/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.google.samples.apps.nowinandroid.core.ui

import android.app.Activity
import android.util.Log
import androidx.activity.FullyDrawnReporter
import androidx.activity.compose.LocalFullyDrawnReporterOwner
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.snapshots.SnapshotStateObserver

/**
 * Copy of activity-compose that includes a try..catch to ignore an exception in Robolectric tests.
 *
 * TODO:
 * - Remove whole file when fixed: https://issuetracker.google.com/260506820
 * - Remove implementation(libs.androidx.activity.compose) from core:ui gradle file
 */
@Composable
fun ReportDrawnWhen(
    predicate: () -> Boolean
) {
    val fullyDrawnReporter =
        LocalFullyDrawnReporterOwner.current?.fullyDrawnReporter ?: return
    DisposableEffect(fullyDrawnReporter, predicate) {
        if (fullyDrawnReporter.isFullyDrawnReported) {
            onDispose {}
        } else {
            val compositionDrawn = ReportDrawnComposition(fullyDrawnReporter, predicate)
            onDispose {
                try {
                    compositionDrawn.removeReporter()
                } catch (e: IllegalStateException) {
                    Log.e("ReportDrawnWhen", e.message, e)
                }

            }
        }
    }
}


/**
 * Manages the composition callback for [ReportDrawnWhen].
 */
private class ReportDrawnComposition(
    private val fullyDrawnReporter: FullyDrawnReporter,
    private val predicate: () -> Boolean
) : () -> Unit {

    private val snapshotStateObserver = SnapshotStateObserver { command ->
        command()
    }.apply {
        start()
    }

    /**
     * Called whenever the values read in the lambda parameter has changed.
     */
    private val checkReporter: (() -> Boolean) -> Unit = ::observeReporter

    init {
        fullyDrawnReporter.addOnReportDrawnListener(this)
        if (!fullyDrawnReporter.isFullyDrawnReported) {
            fullyDrawnReporter.addReporter()
            observeReporter(predicate)
        }
    }

    /**
     * Called when the [FullyDrawnReporter] has called [Activity.reportFullyDrawn]. This
     * stops watching for changes to the snapshot.
     */
    override fun invoke() {
        snapshotStateObserver.clear()
        snapshotStateObserver.stop()
    }

    /**
     * Stops observing [predicate] and marks the [fullyDrawnReporter] as ready for it.
     */
    fun removeReporter() {
        snapshotStateObserver.clear(predicate)
        if (!fullyDrawnReporter.isFullyDrawnReported) {
            fullyDrawnReporter.removeReporter()
        }
        this.invoke() // stop the snapshotStateObserver.
    }

    private fun observeReporter(predicate: () -> Boolean) {
        var reporterPassed = false
        snapshotStateObserver.observeReads(predicate, checkReporter) {
            reporterPassed = predicate()
        }
        if (reporterPassed) {
            removeReporter()
        }
    }
}
