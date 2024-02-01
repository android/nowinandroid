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

package com.google.samples.apps.nowinandroid.util

import androidx.tracing.trace
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers.Default
import com.google.samples.apps.nowinandroid.core.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * This class asynchronously loads the Coil's image loader on a [ApplicationScope], which uses Default dispatcher.
 * Reason for this is to prevent initializing Coil (and thus OkHttp internally) with the first image loading
 * to prevent skipping frames and performance issues.
 *
 * Usage:
 * - Init creates an async initialization of the image loader.
 * - delegate to [newImageLoader] so that Coil can automatically reach for its loader.
 */
class ImageLoaderAsyncFactory @Inject constructor(
    @ApplicationScope
    appScope: CoroutineScope,
    private val imageLoader: dagger.Lazy<ImageLoader>,
) : ImageLoaderFactory {

    /**
     * Initialize immediately, but need a Deferred for callers
     * [ApplicationScope] already uses [Default] dispatcher, so we don't have to switch it here.
     */
    private val asyncNewImageLoader: Deferred<ImageLoader> = appScope.async { imageLoader.get() }

    /**
     * This runBlocking here is on purpose to prevent any unfinished Coil initialization.
     * Most likely this will be already initialized by the time we want to show an image on the screen.
     */
    override fun newImageLoader() =
        trace("NiaImageLoader.runBlocking") {
            if (asyncNewImageLoader.isCompleted) asyncNewImageLoader.getCompleted() 
            else runBlocking { asyncNewImageLoader.await() }
        }
}
