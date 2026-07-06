/*
 * Copyright 2022 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.samples.apps.nowinandroid.sync.initializers.Sync
import com.google.samples.apps.nowinandroid.util.ProfileVerifierLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.example.mylibrary.StartupTask
import com.example.mylibrary.TaskRunner
import android.util.Log

class NiaStartupTask : StartupTask {
    override fun run() {
        Log.d("NiaStartupTask", "Running startup task from the app")
    }
}

/**
 * [Application] class for NiA
 */
@HiltAndroidApp
class NiaApplication : Application(), ImageLoaderFactory, ComponentCallbacks2 {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    lateinit var profileVerifierLogger: ProfileVerifierLogger

    override fun onCreate() {
        super.onCreate()

        // Initialize Sync; the system responsible for keeping data in the app up to date.
        Sync.initialize(context = this)
        profileVerifierLogger()
        TaskRunner.execute("com.google.samples.apps.nowinandroid.NiaStartupTask")
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()

    /**
     * Return true if the application is debuggable.
     */
    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    override fun onTrimMemory(level: Int) {
        super<Application>.onTrimMemory(level)
        // 1. Explicitly clear or trim our ImageLoader's memory cache
        // (Note: Coil automatically listens to ComponentCallbacks2, but this serves as a good 
        // example of how to manually trim caches based on system memory signals).
        if (level == TRIM_MEMORY_UI_HIDDEN){
            // The user has navigated away from the app and the UI is no longer visible.
            // This is a good place to release large UI-related resources that you
            // won't need while in the background
            imageLoader.get().memoryCache?.trimMemory(level)
        }

        if(level == TRIM_MEMORY_BACKGROUND) {
            // The app is in the background and the system is running low on memory.
            // As the level increases to COMPLETE, the likelihood of the app process
            // being killed increases. Aggressively clear caches here.
            imageLoader.get().memoryCache?.clear()
        }

    }

    /**
     * Set a thread policy that detects all potential problems on the main thread, such as network
     * and disk access.
     *
     * If a problem is found, the offending call will be logged and the application will be killed.
     */
    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            StrictMode.setThreadPolicy(
                Builder().detectAll().penaltyLog().build(),
            )
        }
    }
}
