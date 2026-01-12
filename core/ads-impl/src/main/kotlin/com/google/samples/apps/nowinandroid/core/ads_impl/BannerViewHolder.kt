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
import android.view.ViewGroup
import com.appodeal.ads.Appodeal
import com.appodeal.ads.BannerView
import java.util.concurrent.atomic.AtomicReference

/**
 * Owns the single BannerView instance and provides safe re-parenting.
 */
internal class BannerViewHolder {

    private val bannerViewRef = AtomicReference<BannerView?>(null)

    fun obtain(context: Context): BannerView {
        bannerViewRef.get()?.let { return it }

        synchronized(this) {
            bannerViewRef.get()?.let { return it }

            // Use applicationContext to avoid leaking Activity.
            val created = Appodeal.getBannerView(context.applicationContext)
            bannerViewRef.set(created)
            return created
        }
    }

    fun moveTo(container: ViewGroup, bannerView: BannerView) {
        val parent = bannerView.parent
        if (parent is ViewGroup && parent !== container) {
            parent.removeView(bannerView)
        }

        // Keep container clean: only the banner view should be inside.
        if (container.childCount != 1 || container.getChildAt(0) !== bannerView) {
            container.removeAllViews()
            container.addView(
                bannerView,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                ),
            )
        }
    }

    fun detachFromParent() {
        bannerViewRef.get()?.let { bannerView ->
            (bannerView.parent as? ViewGroup)?.removeView(bannerView)
        }
    }
}