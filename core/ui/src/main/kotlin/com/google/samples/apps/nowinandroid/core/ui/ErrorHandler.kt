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

import androidx.compose.material3.SnackbarHostState
import com.google.samples.apps.nowinandroid.core.ui.HandledError.Default
import com.google.samples.apps.nowinandroid.core.ui.HandledError.Exception
import com.google.samples.apps.nowinandroid.core.ui.HandledError.Offline
import com.google.samples.apps.nowinandroid.core.ui.HandledError.Specific

class ErrorHandler(private val snackbarHostState: SnackbarHostState) {

    suspend fun handleError(error: HandledError<*>) {
        // Log the error or show a generic error message
        when (error) {
            is Specific -> {
                snackbarHostState.showSnackbar(error.data.toString())
            }
            is Exception -> {
                snackbarHostState.showSnackbar("Exception: ${error.exception.message}")
            }
            is Offline -> {
                snackbarHostState.showSnackbar("No internet connection")
            }
            is Default -> {
                snackbarHostState.showSnackbar("An error occurred")
            }
            is UnknownError -> {
                snackbarHostState.showSnackbar("An unknown error occurred")
            }
        }
    }
}
