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
import org.jetbrains.annotations.VisibleForTesting
import kotlin.collections.mutableListOf

// TODO refine back behavior - perhaps take a lambda so that each screen / use site can customize back behavior?
// https://github.com/android/nowinandroid/issues/1934
class NiaBackStack(
    private val startKey: NiaNavKey,
) {
    internal var backStackMap: LinkedHashMap<NiaNavKey, MutableList<NiaNavKey>> =
        linkedMapOf(
            startKey to mutableListOf(startKey),
        )

    @VisibleForTesting
    val backStack: SnapshotStateList<NiaNavKey> = mutableStateListOf(startKey)

    var currentTopLevelKey: NiaNavKey by mutableStateOf(backStackMap.keys.last())
        private set

    @get:VisibleForTesting
    val currentKey: NiaNavKey
        get() = backStackMap[currentTopLevelKey]!!.last()

    fun navigate(key: NiaNavKey) {
        when {
            // top level singleTop -> clear substack
            key == currentTopLevelKey -> backStackMap[key] = mutableListOf(key)
            // top level non-singleTop
            key.isTopLevel -> {
                // if navigating back to start destination, pop all other top destinations and
                // store start destination substack
                if (key == startKey) {
                    val tempStack = mapOf(startKey to backStackMap[startKey]!!)
                    backStackMap.clear()
                    backStackMap.putAll(tempStack)
                    // else either restore an existing substack or initiate new one
                } else {
                    backStackMap[key] = backStackMap.remove(key) ?: mutableListOf(key)
                }
            }
            // not top level - add to current substack
            else -> {
                val currentStack = backStackMap.values.last()
                // single top
                if (currentStack.lastOrNull() == key) {
                    currentStack.removeLastOrNull()
                }
                currentStack.add(key)
            }
        }
        updateBackStack()
    }

    fun popLast(count: Int = 1) {
        var popCount = count
        var currentEntry = backStackMap.entries.last()
        while (popCount > 0) {
            val currentStack = currentEntry.value
            if (currentStack.size == 1) {
                // if current sub-stack only has one key, remove the sub-stack from the map
                backStackMap.remove(currentEntry.key)
                when {
                    // throw if map is empty after pop
                    backStackMap.isEmpty() -> error(popErrorMessage(count, currentEntry.key))
                    // otherwise update currentEntry
                    else -> currentEntry = backStackMap.entries.last()
                }
            } else {
                // if current sub-stack has more than one key, just pop the last key off the sub-stack
                currentStack.removeLastOrNull()
            }
            popCount--
        }
        updateBackStack()
    }

    private fun updateBackStack() {
        backStack.apply {
            clear()
            backStack.addAll(
                backStackMap.flatMap { it.value },
            )
        }

        currentTopLevelKey = backStackMap.keys.last()
    }

    internal fun restore(map: LinkedHashMap<NiaNavKey, MutableList<NiaNavKey>>?) {
        map ?: return
        backStackMap.clear()
        backStackMap.putAll(map)
        updateBackStack()
    }
}

interface NiaNavKey {
    val isTopLevel: Boolean
}

private fun popErrorMessage(count: Int, lastPopped: NiaNavKey) =
    """
        Failed to pop $count entries. BackStack has been popped to an empty stack. Last
        popped key is $lastPopped.
    """.trimIndent()
