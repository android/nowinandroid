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
import android.view.ViewGroup

/**
 * Entry point for ads.
 * Lazy init: safe to call multiple times; initializes only once in impl.
 */
interface AdsClient {
    fun ensureInitialized(context: Context)
    val banner: BannerAds
}

interface BannerAds {

    fun onResume(activity: Activity, placement: String)
    fun preload(
        activity: Activity,
        container: ViewGroup,
        placement: String,
        onLoaded: () -> Unit,
        onFailed: () -> Unit,
    )

    fun attach(
        activity: Activity,
        container: ViewGroup,
        placement: String,
    )

    fun detach(activity: Activity)
}


