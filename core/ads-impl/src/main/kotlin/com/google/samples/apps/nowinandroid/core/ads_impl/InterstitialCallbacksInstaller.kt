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

import com.appodeal.ads.InterstitialCallbacks
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Installs Appodeal interstitial callbacks once.
 * Callbacks are global in Appodeal, so we must not re-install per instance.
 */
internal class InterstitialCallbacksInstaller(
    private val pending: InterstitialPendingActions,
    private val appodeal: InterstitialAppodealFacade,
) {
    private val installed = AtomicBoolean(false)

    fun installIfNeeded() {
        if (installed.get()) return
        synchronized(this) {
            if (installed.get()) return

            appodeal.setCallbacks(object : InterstitialCallbacks {
                override fun onInterstitialShown() {
                    pending.notifyShown()
                }

                override fun onInterstitialClosed() {
                    pending.completeDismissed()
                }

                override fun onInterstitialFailedToLoad() {
                    pending.completeFailed(null)
                }

                override fun onInterstitialShowFailed() {
                    pending.completeFailed(null)
                }

                override fun onInterstitialLoaded(isPrecache: Boolean) = Unit
                override fun onInterstitialClicked() = Unit
                override fun onInterstitialExpired() = Unit
            })

            installed.set(true)
        }
    }
}