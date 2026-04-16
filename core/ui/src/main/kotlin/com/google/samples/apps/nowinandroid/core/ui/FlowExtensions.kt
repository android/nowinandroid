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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Converts a [Flow] to a [StateFlow] scoped to the [ViewModel]'s lifecycle, using
 * [SharingStarted.WhileSubscribed] with a 5-second stop timeout.
 *
 * Shorthand for:
 * ```
 * stateIn(
 *     scope = viewModelScope,
 *     started = SharingStarted.WhileSubscribed(5_000),
 *     initialValue = initialValue,
 * )
 * ```
 *
 * The [ViewModel] context is resolved implicitly from `this` when called inside a [ViewModel].
 */
context(viewModel: ViewModel)
fun <T> Flow<T>.stateInUi(initialValue: T): StateFlow<T> = stateIn(
    scope = viewModel.viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = initialValue,
)
