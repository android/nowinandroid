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

package com.google.samples.apps.nowinandroid.core.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.FrameLayout
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.samples.apps.nowinandroid.core.ads_api.AdsClient

@Composable
fun BannerPreloadItem(
    ads: AdsClient,
    placement: String,
    onLoaded: () -> Unit,
    onFailed: () -> Unit,
) {
    val activity = LocalContext.current.findActivity() ?: return

    AndroidView(
        modifier = Modifier.size(0.dp),
        factory = { FrameLayout(it) },
        update = { container ->
            ads.ensureInitialized(container.context)
            ads.banner.preload(
                activity = activity,
                container = container,
                placement = placement,
                onLoaded = onLoaded,
                onFailed = onFailed,
            )
        }
    )
}