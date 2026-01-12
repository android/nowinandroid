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
import com.appodeal.ads.Appodeal
import com.appodeal.ads.InterstitialCallbacks

/**
 * Small facade around Appodeal static calls to simplify testing and keep orchestration clean.
 */
internal interface InterstitialAppodealFacade {
    fun cache(activity: Activity)
    fun canShow(placement: String): Boolean
    fun show(activity: Activity, placement: String)
    fun setCallbacks(callbacks: InterstitialCallbacks)

    object Real : InterstitialAppodealFacade {
        override fun cache(activity: Activity) {
            runCatching { Appodeal.cache(activity, Appodeal.INTERSTITIAL) }
        }

        override fun canShow(placement: String): Boolean {
            return runCatching { Appodeal.canShow(Appodeal.INTERSTITIAL, placement) }
                .getOrDefault(false)
        }

        override fun show(activity: Activity, placement: String) {
            Appodeal.show(activity, Appodeal.INTERSTITIAL, placement)
        }

        override fun setCallbacks(callbacks: InterstitialCallbacks) {
            Appodeal.setInterstitialCallbacks(callbacks)
        }
    }
}