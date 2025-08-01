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

package com.example.mylibrary

import android.util.Log
import kotlin.annotation.AnnotationRetention.RUNTIME

// In your library:
@Retention(RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class OnEvent

class EventBus {
    fun dispatch(listener: Any) {
        Log.e("EventBus", "Dispatching event to $listener")
        // Find all methods annotated with @OnEvent and invoke them
        listener::class.java.declaredMethods.forEach { method ->
            if (method.isAnnotationPresent(OnEvent::class.java)) {
                try {
                    method.invoke(listener)
                } catch (e: Exception) { /* ... */ }
            }
        }
    }
}


