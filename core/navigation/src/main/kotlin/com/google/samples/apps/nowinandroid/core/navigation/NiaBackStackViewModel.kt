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

package com.google.samples.apps.nowinandroid.core.navigation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedState
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@HiltViewModel
class NiaBackStackViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val niaBackStack: NiaBackStack,
    serializersModules: SerializersModule,
): ViewModel() {

    val config = SavedStateConfiguration { serializersModule = serializersModules }

    init {
        // Restore backstack from saved state handle if not null
        niaBackStack.restore(restoreBackStackMap())

        // Start observing changes to the backStack and save backStack whenever it updates
        viewModelScope.launch {
            snapshotFlow {
                niaBackStack.backStack.toList()
            }.collect {
                saveBackStackMap(niaBackStack.backStackMap)
            }
        }
    }

    private inline fun <reified T: NiaBackStackKey> restoreBackStackMap(): LinkedHashMap<T, MutableList<T>>?{
        val savedState: SavedState = savedStateHandle.get<SavedState>(BACKSTACK_SAVE_KEY) ?: return null
        @Suppress("UNCHECKED_CAST")
        return decodeFromSavedState(
            deserializer = getMapSerializer<T>(),
            savedState = savedState,
            configuration = config
        ) as LinkedHashMap<T, MutableList<T>>
    }

    private inline fun <reified T : NiaBackStackKey> saveBackStackMap(
        map: LinkedHashMap<T, MutableList<T>>
    ) {
        val savedState = encodeToSavedState(
            serializer = getMapSerializer<T>(),
            value = map,
            configuration = config
        )
        savedStateHandle[BACKSTACK_SAVE_KEY] = savedState
    }
}

private const val BACKSTACK_SAVE_KEY = "NiaBackStackSavedStateKey"

private inline fun <reified T : NiaBackStackKey> getMapSerializer() = MapSerializer(serializer<T>(), serializer<List<T>>())
