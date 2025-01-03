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

package com.google.samples.apps.nowinandroid.core.testing.util

import com.google.samples.apps.nowinandroid.core.data.util.ErrorMonitor
import com.google.samples.apps.nowinandroid.core.model.data.MessageData
import com.google.samples.apps.nowinandroid.core.model.data.MessageType
import com.google.samples.apps.nowinandroid.core.model.data.MessageType.MESSAGE
import com.google.samples.apps.nowinandroid.core.model.data.MessageType.OFFLINE
import com.google.samples.apps.nowinandroid.core.model.data.MessageType.UNKNOWN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class TestErrorMonitor : ErrorMonitor {

    private val _messages = MutableStateFlow<List<MessageData>>(emptyList())

    override fun addMessageByString(message: String): MessageData {
        TODO("Not yet implemented")
    }

    override fun addMessageByData(message: MessageData) {
        TODO("Not yet implemented")
    }

    override fun clearMessage(message: MessageData) {
        TODO("Not yet implemented")
    }

    override fun clearAllMessages() {
        _messages.value = emptyList()
    }

    override val messages: Flow<List<MessageData?>>
        get() = _messages

    /**
     * Test-only API to add message types
     */

    fun setOfflineMessage(){
        _messages.value = listOf(OFFLINE_MESSAGE)
    }

    fun addMessage(){
        _messages.value.plus(MESSAGE_MESSAGE)
    }

    fun addUnknownMessage(){
        _messages.value.plus(UNKNOWN_MESSAGE)
    }

    companion object{
        val OFFLINE_MESSAGE = MessageData(OFFLINE)
        val MESSAGE_MESSAGE = MessageData(MESSAGE("Message"), "Title", {}, {})
        val UNKNOWN_MESSAGE = MessageData(UNKNOWN)
    }
}