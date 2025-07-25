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

import com.google.samples.apps.nowinandroid.core.model.data.MessageData
import com.google.samples.apps.nowinandroid.core.model.data.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Interface implementation for handling general errors.
 */

class StateErrorMonitor @Inject constructor() : ErrorMonitor {
    /**
     * List of [MessageData] to be shown
     */
    override val messages = MutableStateFlow<List<MessageData>>(emptyList())

    /**
     * Creates a [MessageData] and adds it to the list.
     * @param message: String value for message to add.
     */
    override fun addMessageByString(message: String): MessageData {
        val data = MessageData(type = MessageType.MESSAGE(message))
        messages.update { it + data }

        return data
    }

    /**
     * Add a [MessageData] to the list.
     * @param message: [MessageData] to add.
     */
    override fun addMessageByData(message: MessageData) {
        messages.update { it + message }
    }

    /**
     * Removes the [MessageData] from the list.
     */
    override fun clearMessage(message: MessageData) {
        messages.update { list -> list.filterNot { it == message } }
    }

    /**
     * Remove all from list, reset to empty list
     */
    override fun clearAllMessages() {
        messages.update { emptyList() }
    }
}
