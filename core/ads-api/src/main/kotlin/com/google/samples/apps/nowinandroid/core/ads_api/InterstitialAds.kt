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

package com.google.samples.apps.nowinandroid.core.ads_api

import android.app.Activity
import android.content.Context

interface InterstitialAds {
    /** Warm-up / caching. Safe to call multiple times. */
    fun preload(activity: Activity, placement: String)

    /** True if SDK says interstitial can be shown right now. */
    fun canShow(placement: String): Boolean

    /**
     * Show interstitial. Must call exactly one of callbacks.
     * If cannot show -> calls onDismiss immediately.
     */
    fun show(
        activity: Activity,
        placement: String,
        onShown: () -> Unit = {},
        onDismiss: () -> Unit,
        onFailed: (Throwable?) -> Unit = {},
    )
}
