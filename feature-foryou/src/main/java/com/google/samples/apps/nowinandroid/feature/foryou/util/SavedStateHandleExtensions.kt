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
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotMutableState
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
 * https://issuetracker.google.com/issues/224565154 and
 * https://issuetracker.google.com/issues/225014345
 */
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

/**
 * A basic interop between [SavedStateHandle] and [Saver], so the latter can be used to save
 * state holders into the [SavedStateHandle].
 *
 * This implementation is based on [rememberSaveable], [SaveableStateRegistry] and
 * [DisposableSaveableStateRegistry], with some simplifications since there will be exactly one
 * state provider storing exactly one value.
 *
 * This implementation makes use of [SavedStateHandle.setSavedStateProvider], so this
 * state will not be kept in sync with any other way to change the internal state
 * of the [SavedStateHandle].
 *
 * Use this overload if you remember a mutable state with a type which can't be stored in the
 * Bundle so you have to provide a custom saver object.
 *
 * https://issuetracker.google.com/issues/224565154
 */
@OptIn(SavedStateHandleSaveableApi::class)
fun <T> SavedStateHandle.saveable(
    key: String,
    stateSaver: Saver<T, out Any>,
    init: () -> MutableState<T>
): MutableState<T> = saveable(
    saver = mutableStateSaver(stateSaver),
    key = key,
    init = init
)

/**
 * Copied from RememberSaveable.kt
 */
@Suppress("UNCHECKED_CAST")
private fun <T> mutableStateSaver(inner: Saver<T, out Any>) = with(inner as Saver<T, Any>) {
    Saver<MutableState<T>, MutableState<Any?>>(
        save = { state ->
            require(state is SnapshotMutableState<T>) {
                "If you use a custom MutableState implementation you have to write a custom " +
                    "Saver and pass it as a saver param to saveable()"
            }
            mutableStateOf(save(state.value), state.policy as SnapshotMutationPolicy<Any?>)
        },
        restore = @Suppress("UNCHECKED_CAST") {
            require(it is SnapshotMutableState<Any?>)
            mutableStateOf(
                if (it.value != null) restore(it.value!!) else null,
                it.policy as SnapshotMutationPolicy<T?>
            ) as MutableState<T>
        }
    )
}
