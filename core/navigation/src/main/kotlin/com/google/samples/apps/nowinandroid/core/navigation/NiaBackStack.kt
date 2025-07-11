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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import javax.inject.Inject
import kotlin.collections.remove

class NiaBackStack @Inject constructor(
    startKey: NiaBackStackKey,
) {
    val backStack = mutableStateListOf(startKey)

    // Maintain a stack for each top level route
    private var topLevelStacks : LinkedHashMap<NiaBackStackKey, SnapshotStateList<NiaBackStackKey>> = linkedMapOf(
        startKey to mutableStateListOf(startKey)
    )

    // Expose the current top level route for consumers
    var currentTopLevelKey by mutableStateOf(startKey)
        private set

    internal val currentKey: NiaBackStackKey
        get() = topLevelStacks[currentTopLevelKey]!!.last()

    private fun updateBackStack() =
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }

    fun navigateToTopLevelDestination(key: NiaBackStackKey){
        // If the top level doesn't exist, add it
        if (topLevelStacks[key] == null){
            topLevelStacks.put(key, mutableStateListOf(key))
        } else {
            // Otherwise just move it to the end of the stacks
            topLevelStacks.apply {
                remove(key)?.let {
                    put(key, it)
                }
            }
        }

        currentTopLevelKey = key
        updateBackStack()
    }

    fun navigate(key: NiaBackStackKey){
        if (backStack.lastOrNull() != key) {
            topLevelStacks[currentTopLevelKey]?.add(key)
            updateBackStack()
        }
    }

    fun removeLast(){
        val removedKey = topLevelStacks[currentTopLevelKey]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        currentTopLevelKey = topLevelStacks.keys.last()
        updateBackStack()
    }
}

interface NiaBackStackKey