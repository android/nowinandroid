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

package com.google.samples.apps.nowinandroid.core.testing.util

import com.google.samples.apps.nowinandroid.core.data.util.ErrorMessage
import com.google.samples.apps.nowinandroid.core.data.util.ErrorMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TestErrorMonitor(networkMonitor: TestNetworkMonitor) : ErrorMonitor {
    override var offlineMessage: String? = "offline"
    override val isOffline: Flow<Boolean> = networkMonitor.isOnline.map { !it }
    override fun addShortErrorMessage(
        error: String,
        label: String?,
        successAction: (() -> Unit)?,
        failureAction: (() -> Unit)?,
    ): String? {
        return "1"
    }

    override fun addLongErrorMessage(
        error: String,
        label: String?,
        successAction: (() -> Unit)?,
        failureAction: (() -> Unit)?,
    ): String? {
        return "2"
    }

    override fun addIndefiniteErrorMessage(
        error: String,
        label: String?,
        successAction: (() -> Unit)?,
        failureAction: (() -> Unit)?,
    ): String? {
        return "3"
    }

    override fun clearErrorMessage(id: String) {
        // Do nothing
    }

    override val errorMessage: Flow<ErrorMessage?>
        get() = flowOf(ErrorMessage("Error Message", "1"))
}
