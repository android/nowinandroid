/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.di

import android.app.Activity
import android.util.Log
import android.view.Window
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.JankStats.OnFrameListener
import coil.ImageLoader
import coil.util.DebugLogger
import com.google.samples.apps.nowinandroid.util.ProfileVerifierLogger
import com.google.samples.apps.nowinandroid.MainActivityViewModel
import com.google.samples.apps.nowinandroid.ui.interests2pane.Interests2PaneViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // JankStats dependencies
    single<OnFrameListener> {
        OnFrameListener { frameData ->
            // Make sure to only log janky frames.
            if (frameData.isJank) {
                // We're currently logging this but would better report it to a backend.
                Log.v("NiA Jank", frameData.toString())
            }
        }
    }
    
    single<Window> { (activity: Activity) ->
        activity.window
    }
    
    single<JankStats> { (window: Window) ->
        JankStats.createAndTrack(window, get<OnFrameListener>())
    }
    
    // ImageLoader
    single<ImageLoader> {
        ImageLoader.Builder(get())
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
    
    // ProfileVerifierLogger
    single { ProfileVerifierLogger(get<CoroutineScope>(named("ApplicationScope"))) }
    
    // ViewModels
    viewModel { MainActivityViewModel(get()) }
    viewModel { Interests2PaneViewModel(get()) }
}
