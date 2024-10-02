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

package com.google.samples.apps.nowinandroid.core.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

/**
 * Interface implementation for handling general errors.
 */

class SnackbarErrorMonitor @Inject constructor(val networkMonitor: NetworkMonitor) : ErrorMonitor {
    /**
     * List of [ErrorMessage] to be shown to the user, via Snackbar.
     */
    private val errorMessages = MutableStateFlow<List<ErrorMessage>>(emptyList())

    override val isOffline = networkMonitor.isOnline
        .map(Boolean::not)

    override var offlineMessage: String? = null

    override val errorMessage: Flow<ErrorMessage?> = combine(errorMessages, isOffline) { messages, isOffline ->
        // Offline Error Message takes precedence over other messages
        if (isOffline) {
            ErrorMessage(offlineMessage ?: "You are offline", duration = MessageDuration.Indefinite)
        } else {
            messages.firstOrNull()
        }
    }

    /**
     * Creates an [ErrorMessage] from String value and adds it to the list.
     *
     * @param error: String value of the error message.
     *
     * Returns the ID of the new [ErrorMessage] if success
     * Returns null if [error] is Blank
     */
    private fun addErrorMessage(error: String, label: String?, duration: MessageDuration?, actionPerformed: (() -> Unit)?, actionNotPerformed: (() -> Unit)?): String? {
        if (error.isNotBlank()) {
            val newError = ErrorMessage(error, label = label, duration = duration, actionPerformed = actionPerformed, actionNotPerformed = actionNotPerformed)
            errorMessages.update { it + newError }
            return newError.id
        }
        return null
    }

    override fun addShortErrorMessage(error: String, label: String?, successAction: (() -> Unit)?, failureAction: (() -> Unit)?): String? {
        return addErrorMessage(error, label, MessageDuration.Short, successAction, failureAction)
    }

    override fun addLongErrorMessage(error: String, label: String?, successAction: (() -> Unit)?, failureAction: (() -> Unit)?): String? {
        return addErrorMessage(error, label, MessageDuration.Long, successAction, failureAction)
    }

    override fun addIndefiniteErrorMessage(error: String, label: String?, successAction: (() -> Unit)?, failureAction: (() -> Unit)?): String? {
        return addErrorMessage(error, label, MessageDuration.Indefinite, successAction, failureAction)
    }

    /**
     * Removes the [ErrorMessage] with the specified [id] from the list.
     */
    override fun clearErrorMessage(id: String) {
        errorMessages.update { it.filter { item -> item.id != id } }
    }
}

/**
 * Models the data needed for an error message to be displayed and tracked.
 */
data class ErrorMessage(
    val message: String,
    val id: String = UUID.randomUUID().toString(),
    val label: String? = null,
    val duration: MessageDuration? = MessageDuration.Short,
    val actionPerformed: (() -> Unit)? = null,
    val actionNotPerformed: (() -> Unit)? = null,
)

enum class MessageDuration {
    Short,
    Long,
    Indefinite,
}
