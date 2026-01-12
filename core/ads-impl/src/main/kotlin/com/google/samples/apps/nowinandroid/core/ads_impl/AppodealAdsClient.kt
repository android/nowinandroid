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

import android.content.Context
import com.appodeal.ads.Appodeal
import com.google.samples.apps.nowinandroid.core.ads_api.AdsClient
import com.google.samples.apps.nowinandroid.core.ads_api.AdsConfigProvider
import com.google.samples.apps.nowinandroid.core.ads_api.BannerAds
import jakarta.inject.Inject
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Singleton

@Singleton
class AppodealAdsClient @Inject constructor(
    private val configProvider: AdsConfigProvider,
) : AdsClient {

    private val initialized = AtomicBoolean(false)

    override val banner: BannerAds = AppodealBannerAds(
        ensureInit = ::ensureInitialized
    )

    override fun ensureInitialized(context: Context) {
        if (initialized.get()) return
        synchronized(this) {
            if (initialized.get()) return

            val cfg = configProvider.getConfig()
            val appContext = context.applicationContext
            Appodeal.setTesting(configProvider.getConfig().isTesting)
            Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.verbose)
            Appodeal.setSharedAdsInstanceAcrossActivities(true)
            Appodeal.initialize(appContext, cfg.appKey, Appodeal.BANNER_VIEW)

            initialized.set(true)
        }
    }
}
