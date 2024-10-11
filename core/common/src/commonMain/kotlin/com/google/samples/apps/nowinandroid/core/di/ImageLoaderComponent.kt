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

package com.google.samples.apps.nowinandroid.core.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import org.koin.dsl.module

val imageLoaderModule = module {
    single<ImageLoader> {
        val platformContext: PlatformContext = get()
        ImageLoader.Builder(platformContext)
            .memoryCache {
                MemoryCache.Builder()
                    // Set the max size to 25% of the app's available memory.
                    .maxSizePercent(platformContext, percent = 0.25)
                    .build()
            }
            // Show a short crossfade when loading images asynchronously.
            .crossfade(true)
            // Enable logging if this is a debug build.
            .apply {
                logger(DebugLogger())
            }
            .build()
    }
}
