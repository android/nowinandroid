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

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.appodeal.ads.Appodeal
import com.appodeal.ads.BannerCallbacks
import com.appodeal.ads.BannerView
import com.google.samples.apps.nowinandroid.core.ads_api.BannerAds
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

internal class AppodealBannerAds(
    private val ensureInit: (Context) -> Unit,
) : BannerAds {

    private val callbacksInstalled = AtomicBoolean(false)
    private val preloadInFlight = AtomicBoolean(false)
    private val isLoaded = AtomicBoolean(false)
    private val lastShownToken = AtomicReference<String?>(null)

    private val viewHolder = BannerViewHolder()
    private val preloadCallbacks = PreloadCallbacks()

    override fun preload(
        activity: Activity,
        container: ViewGroup,
        placement: String,
        onLoaded: () -> Unit,
        onFailed: () -> Unit,
    ) {
        ensureInit(activity)
        installCallbacksIfNeeded()

        // If banner is already loaded, report immediately.
        if (isLoaded.get()) {
            onLoaded()
            return
        }

        // Avoid re-triggering preload on recompositions.
        if (!preloadInFlight.compareAndSet(false, true)) return

        preloadCallbacks.set(onLoaded, onFailed)

        val bannerView = viewHolder.obtain(activity)
        viewHolder.moveTo(container, bannerView)

        // Explicitly start loading.
        runCatching { Appodeal.cache(activity, Appodeal.BANNER) }

        // Optional: attempt to show if it is already allowed.
        tryShowIfPossible(activity, placement)
    }

    override fun attach(
        activity: Activity,
        container: ViewGroup,
        placement: String,
    ) {
        ensureInit(activity)
        installCallbacksIfNeeded()

        val bannerView = viewHolder.obtain(activity)
        viewHolder.moveTo(container, bannerView)

        // If not loaded yet, cache again (cache is idempotent).
        if (!isLoaded.get()) {
            runCatching { Appodeal.cache(activity, Appodeal.BANNER) }
        }
        Log.d("ADS", "onAttachCall")
        showOncePerContainerAndPlacement(container, placement, activity)
    }

    override fun detach(activity: Activity) {
        ensureInit(activity)

        preloadCallbacks.clear()
        preloadInFlight.set(false)

        // hide() can vary between SDK versions; be defensive.
        runCatching { Appodeal.hide(activity, Appodeal.BANNER) }

        viewHolder.detachFromParent()
    }

    override fun onResume(activity: Activity, placement: String) {
        ensureInit(activity)
        installCallbacksIfNeeded()
        Log.d("ADS", "onResumeCall")

        // Re-issue show on resume to survive rotation / window recreation.
        tryShowIfPossible(activity, placement)
    }

    private fun installCallbacksIfNeeded() {
        if (callbacksInstalled.get()) return
        synchronized(this) {
            if (callbacksInstalled.get()) return

            Appodeal.setBannerCallbacks(
                object : BannerCallbacks {

                    override fun onBannerLoaded(height: Int, isPrecache: Boolean) {
                        isLoaded.set(true)
                        preloadInFlight.set(false)
                        preloadCallbacks.consumeLoaded()
                    }

                    override fun onBannerFailedToLoad() {
                        preloadInFlight.set(false)
                        preloadCallbacks.consumeFailed()
                    }

                    override fun onBannerExpired() {
                        // Treat expired as "not loaded" so next preload/attach can reload it.
                        isLoaded.set(false)
                    }

                    override fun onBannerShown() = Unit
                    override fun onBannerShowFailed() = Unit
                    override fun onBannerClicked() = Unit
                },
            )

            callbacksInstalled.set(true)
        }
    }

    private fun tryShowIfPossible(activity: Activity, placement: String) {
        val canShow = runCatching { Appodeal.canShow(Appodeal.BANNER, placement) }
            .getOrDefault(false)
        Log.d("ADS", "tryShowIfPossible CANSHOW == $canShow")
        if (canShow) {
            val isShown = Appodeal.show(activity, Appodeal.BANNER_VIEW, placement)
            Log.d("ADS", "tryShowIfPossible isShown == $isShown")
        }
    }

    private fun showOncePerContainerAndPlacement(
        container: ViewGroup,
        placement: String,
        activity: Activity,
    ) {
        val token = "${System.identityHashCode(container)}:$placement"
        if (lastShownToken.getAndSet(token) == token) return

        if (Appodeal.canShow(Appodeal.BANNER, placement)) {
            Appodeal.show(activity, Appodeal.BANNER_VIEW, placement)
        }
    }
}

