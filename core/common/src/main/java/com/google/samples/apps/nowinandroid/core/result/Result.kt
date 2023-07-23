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

package com.google.samples.apps.nowinandroid.core.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun <T, R> Flow<T>.mapToResultUiState(
    onSuccess: (T) -> R,
    onLoading: () -> R,
    onError: (Throwable?) -> R,
): Flow<R> {
    return this
        .map { Result.Success(it) as Result<T> }
        .onStart { emit(Result.Loading) }
        .catch { e -> emit(Result.Error(e)) }
        .map { result ->
            when (result) {
                is Result.Success -> onSuccess(result.data)
                is Result.Loading -> onLoading()
                is Result.Error -> onError(result.exception)
            }
        }
}
