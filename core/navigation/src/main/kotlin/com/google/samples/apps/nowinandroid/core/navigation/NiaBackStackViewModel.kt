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

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.savedstate.serialization.SavedStateConfiguration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import javax.inject.Inject

@HiltViewModel
class NiaBackStackViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val niaBackStack: NiaBackStack,
    serializersModules: SerializersModule,
) : ViewModel() {

    private val config = SavedStateConfiguration { serializersModule = serializersModules }

    @VisibleForTesting
    internal var backStackMap by savedStateHandle.saved(
        serializer = getMapSerializer<NiaNavKey>(),
        configuration = config,
    ) {
        linkedMapOf()
    }

    init {
        if (backStackMap.isNotEmpty()) {
            // Restore backstack from saved state handle if not emtpy
            @Suppress("UNCHECKED_CAST")
            niaBackStack.restore(
                backStackMap as LinkedHashMap<NiaNavKey, MutableList<NiaNavKey>>,
            )
        }

        // Start observing changes to the backStack and save backStack whenever it updates
        viewModelScope.launch {
            snapshotFlow {
                niaBackStack.backStack.toList()
                backStackMap = niaBackStack.backStackMap
            }.collect()
        }
    }
}

private inline fun <reified T : NiaNavKey> getMapSerializer() = MapSerializer(serializer<T>(), serializer<List<T>>())
