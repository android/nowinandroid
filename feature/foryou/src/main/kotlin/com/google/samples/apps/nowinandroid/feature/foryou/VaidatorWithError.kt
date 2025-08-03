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

package com.google.samples.apps.nowinandroid.feature.foryou

abstract class ValidatorWithErrorType<ParamType : Any, ReturnType : Any, Error : Any> {
    fun validate(data: ParamType): ValidatorResult<ReturnType, Error> {
        return doValidation(data)
    }

    protected abstract fun doValidation(data: ParamType): ValidatorResult<ReturnType, Error>
    sealed class ValidatorResult<out Success : Any, out Error : Any> {
        data class Success<out ReturnType : Any>(val data: ReturnType) : ValidatorResult<ReturnType, Nothing>()
        data class Failure<out Error : Any>(val error: Error) : ValidatorResult<Nothing, Error>()
    }
}