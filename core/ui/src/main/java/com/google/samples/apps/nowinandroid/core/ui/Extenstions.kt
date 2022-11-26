/*
 * Copyright 2022 The Android Open Source Project
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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * This is a helper extension function for making it easier to use [Flow.stateIn] within a ViewModel or etc.
 * @Returns a [StateFlow] that shares the latest value emitted by the original [Flow] and starts
 * with the [initialValue].
 */
fun <T> Flow<T>.stateInScope(
    coroutineScope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
    initialValue: T
): StateFlow<T> = stateIn(coroutineScope, started, initialValue)
