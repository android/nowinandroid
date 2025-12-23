/*
 * Copyright 2025 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.ui

import android.widget.FrameLayout
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appodeal.ads.Appodeal
import com.google.samples.apps.nowinandroid.AppodealViewModel

private val BANNER_HEIGHT = 50.dp

@Composable
fun AppodealBanner(
    modifier: Modifier = Modifier,
    viewModel: AppodealViewModel = viewModel()
) {
    val isReady by viewModel.isAppodealReady.collectAsStateWithLifecycle()
    val activity = LocalActivity.current

    if (!isReady || activity == null) return

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(BANNER_HEIGHT),
        factory = { context -> FrameLayout(context) },
        update = {
            Appodeal.show(activity, Appodeal.BANNER_LEFT)
        }
    )
}
