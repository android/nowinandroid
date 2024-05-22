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

package com.google.samples.apps.nowinandroid.core.ui

// Generic error types
sealed interface SnackbarError<out T> {
    data class Custom<T>(val data: T) : SnackbarError<T>
    data object Exception: SnackbarError<Nothing>
    data object Default : SnackbarError<Nothing>
    data object Unknown : SnackbarError<Nothing>
    data object Offline : SnackbarError<Nothing>
}
