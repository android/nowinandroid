/*
 * Copyright 2023 The Android Open Source Project
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

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import kotlinx.collections.immutable.toImmutableList

@Immutable
@JvmInline
value class ImmutableListWrapper<T>(val value: ImmutableList<T>) : ImmutableList<T> by value {
    operator fun component1(): ImmutableList<T> = value
}

fun <T> List<T>.toImmutableListWrapper(): ImmutableListWrapper<T> =
    ImmutableListWrapper(this.toImmutableList())

fun <T> immutableListWrapperOf(vararg elements: T): ImmutableListWrapper<T> =
    ImmutableListWrapper(ImmutableListAdapter(elements.asList()))
