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

package com.google.samples.apps.nowinandroid

import android.util.Log

val TAG: String = "R8"

/**
 * A class with private members to demonstrate reflection.
 */
class LibraryClass {
    private val secretMessage: Message = Message("R8 will remove me")
}

data class Message(
    val message: String,
    val id: Int =  0
)

// In your app code:
fun accessSecretMessage(instance: LibraryClass) {
    // Use Java reflection from Kotlin to access the private field
    val secretField = instance::class.java.getDeclaredField("secretMessage")
    secretField.isAccessible = true
    val message = secretField.get(instance) as Message
    Log.d(TAG, message.toString())
}
