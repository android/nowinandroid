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

import java.util.concurrent.atomic.AtomicReference

/**
 * Stores callbacks for the current preload attempt and guarantees one-shot delivery.
 */
internal class PreloadCallbacks {
    private data class OneShot(
        val onLoaded: () -> Unit,
        val onFailed: () -> Unit,
    )

    private val ref = AtomicReference<OneShot?>(null)

    fun set(onLoaded: () -> Unit, onFailed: () -> Unit) {
        ref.set(OneShot(onLoaded, onFailed))
    }

    fun clear() {
        ref.set(null)
    }

    fun consumeLoaded() {
        ref.getAndSet(null)?.onLoaded?.invoke()
    }

    fun consumeFailed() {
        ref.getAndSet(null)?.onFailed?.invoke()
    }
}