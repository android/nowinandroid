/*
 * Copyright 2026 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.ads_impl

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * Stores one-shot callbacks for a single in-flight interstitial request.
 * Guarantees that exactly one terminal callback is called.
 */
internal class InterstitialPendingActions {

    private val inFlight = AtomicBoolean(false)

    private val onShownRef = AtomicReference<(() -> Unit)?>(null)
    private val onDismissRef = AtomicReference<(() -> Unit)?>(null)
    private val onFailedRef = AtomicReference<((Throwable?) -> Unit)?>(null)

    fun trySet(
        onShown: () -> Unit,
        onDismiss: () -> Unit,
        onFailed: (Throwable?) -> Unit,
    ): Boolean {
        if (!inFlight.compareAndSet(false, true)) return false
        onShownRef.set(onShown)
        onDismissRef.set(onDismiss)
        onFailedRef.set(onFailed)
        return true
    }

    fun notifyShown() {
        // Not terminal; can happen before close.
        onShownRef.getAndSet(null)?.invoke()
    }

    fun completeDismissed() {
        if (!inFlight.compareAndSet(true, false)) return
        onFailedRef.set(null)
        onShownRef.set(null)
        onDismissRef.getAndSet(null)?.invoke()
    }

    fun completeFailed(error: Throwable?) {
        if (!inFlight.compareAndSet(true, false)) return
        onDismissRef.set(null)
        onShownRef.set(null)
        onFailedRef.getAndSet(null)?.invoke(error)
    }
}
