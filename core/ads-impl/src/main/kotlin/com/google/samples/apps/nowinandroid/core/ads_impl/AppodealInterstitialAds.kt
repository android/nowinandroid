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
import com.google.samples.apps.nowinandroid.core.ads_api.InterstitialAds

internal class AppodealInterstitialAds(
    private val ensureInit: (Context) -> Unit,
    private val appodeal: InterstitialAppodealFacade = InterstitialAppodealFacade.Real,
) : InterstitialAds {

    private val pending = InterstitialPendingActions()
    private val callbacksInstaller = InterstitialCallbacksInstaller(
        pending = pending,
        appodeal = appodeal,
    )

    override fun preload(activity: Activity, placement: String) {
        ensureInit(activity)
        callbacksInstaller.installIfNeeded()
        // placement is kept for symmetry; Appodeal caches by ad type.
        appodeal.cache(activity)
    }

    override fun canShow(placement: String): Boolean {
        return appodeal.canShow(placement)
    }

    override fun show(
        activity: Activity,
        placement: String,
        onShown: () -> Unit,
        onDismiss: () -> Unit,
        onFailed: (Throwable?) -> Unit,
    ) {
        ensureInit(activity)
        callbacksInstaller.installIfNeeded()

        if (!appodeal.canShow(placement)) {
            onDismiss()
            return
        }

        // If something is already pending, we fail fast to avoid callback clobbering.
        if (!pending.trySet(onShown = onShown, onDismiss = onDismiss, onFailed = onFailed)) {
            onFailed(IllegalStateException("Interstitial show already in-flight"))
            return
        }

        appodeal.show(activity, placement)
    }
}






