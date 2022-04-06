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

package com.google.samples.apps.nowinandroid.feature.foryou.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// These are placeholder solutions for https://issuetracker.google.com/issues/224565154 and
// https://issuetracker.google.com/issues/225014345.
// With the following, it is possible to use the Compose Saver APIs to back values in the
// SavedStateHandle to persist through process death, in a similar form as rememberSaveable

/**
 * A [PropertyDelegateProvider] allowing the use of [saveable] with the key provided by the name
 * of the property.
 *
 * https://issuetracker.google.com/issues/225014345
 */
@OptIn(SavedStateHandleSaveableApi::class)
fun <T : Any> SavedStateHandle.saveable(
    saver: Saver<T, out Any> = autoSaver(),
    init: () -> T,
): PropertyDelegateProvider<Any, ReadOnlyProperty<Any, T>> =
    PropertyDelegateProvider { _, property ->
        val value = saveable(
            key = property.name,
            saver = saver,
            init = init
        )

        ReadOnlyProperty { _, _ -> value }
    }

/**
 * A nested [PropertyDelegateProvider] allowing the use of [saveable] with the key provided by the
 * name, as well as direct access to the value contained with [MutableState].
 *
 * This allows the main usage to look almost identical to
 * `rememberSaveable { mutableStateOf(...) }`:
 *
 * ```
 * val value by savedStateHandle.saveable { mutableStateOf("initialValue") }
 * ```
 *
 * https://issuetracker.google.com/issues/225014345
 */
@OptIn(SavedStateHandleSaveableApi::class)
@JvmName("saveableMutableState")
fun <T : Any> SavedStateHandle.saveable(
    stateSaver: Saver<T, out Any> = autoSaver(),
    init: () -> MutableState<T>,
): PropertyDelegateProvider<Any, ReadWriteProperty<Any, T>> =
    PropertyDelegateProvider<Any, ReadWriteProperty<Any, T>> { _, property ->
        val mutableState = saveable(
            key = property.name,
            stateSaver = stateSaver,
            init = init
        )

        object : ReadWriteProperty<Any, T> {
            override fun getValue(thisRef: Any, property: KProperty<*>): T =
                mutableState.getValue(thisRef, property)

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
                mutableState.setValue(thisRef, property, value)
        }
    }
