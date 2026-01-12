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

import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.samples.apps.nowinandroid.core.ads_api.AdsClient

@Composable
fun BannerItem(
    ads: AdsClient,
    placement: String,
    modifier: Modifier = Modifier,
) {
    val activity = LocalContext.current.findActivity() ?: return

    LifecycleResumeEffect(activity, placement) {
        ads.banner.onResume(activity, placement)
        onPauseOrDispose {  }
    }

    AndroidView(
        modifier = modifier.fillMaxWidth().height(50.dp),
        factory = { FrameLayout(it) },
        update = { container ->
            ads.banner.attach(activity, container, placement)
        }
    )

    DisposableEffect(activity) {
        onDispose { ads.banner.detach(activity) }
    }
}



