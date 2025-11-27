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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject
import kotlin.collections.plus

class NiaNavigatorState(
    internal val startKey: NiaNavKey,
) {
    internal var backStacks: MutableMap<NiaNavKey, SnapshotStateList<NiaNavKey>> =
        linkedMapOf(
            startKey to mutableStateListOf(startKey),
        )

    val activeTopLeveLKeys: SnapshotStateList<NiaNavKey> = mutableStateListOf(startKey)

    var currentTopLevelKey: NiaNavKey by mutableStateOf(activeTopLeveLKeys.last())
        private set

    @get:VisibleForTesting
    val currentBackStack: List<NiaNavKey>
        get() = activeTopLeveLKeys.fold(mutableListOf()) { list, topLevelKey ->
            list.apply {
                addAll(backStacks[topLevelKey]!!)
            }
        }

    @get:VisibleForTesting
    val currentKey: NiaNavKey
        get() = backStacks[currentTopLevelKey]!!.last()

    internal fun updateActiveTopLevelKeys(activeKeys: List<NiaNavKey>) {
        check(activeKeys.isNotEmpty()) { "List of active top-level keys should not be empty" }
        activeTopLeveLKeys.clear()
        activeTopLeveLKeys.addAll(activeKeys)
        currentTopLevelKey = activeTopLeveLKeys.last()
    }

    internal fun restore(activeKeys: List<NiaNavKey>, map: LinkedHashMap<NiaNavKey, SnapshotStateList<NiaNavKey>>?) {
        map ?: return
        backStacks.clear()
        map.forEach { entry ->
            backStacks[entry.key] = entry.value.toMutableStateList()
        }
        updateActiveTopLevelKeys(activeKeys)
    }
}

// https://github.com/android/nowinandroid/issues/1934
class NiaNavigator @Inject constructor(
    val navigatorState: NiaNavigatorState,
) {
    fun navigate(key: NiaNavKey) {
        val currentActiveSubStacks = linkedSetOf<NiaNavKey>()
        navigatorState.apply {
            currentActiveSubStacks.addAll(activeTopLeveLKeys)
            when {
                // top level singleTop -> clear substack
                key == currentTopLevelKey -> {
                    backStacks[key] = mutableStateListOf(key)
                    // no change to currentActiveTabs
                }
                // top level non-singleTop
                key.isTopLevel -> {
                    // if navigating back to start destination, then only show the starting substack
                    if (key == startKey) {
                        currentActiveSubStacks.clear()
                        currentActiveSubStacks.add(key)
                    } else {
                        // else either restore an existing substack or initiate new one
                        backStacks[key] = backStacks.remove(key) ?: mutableStateListOf(key)
                        // move this top level key to the top of active substacks
                        currentActiveSubStacks.remove(key)
                        currentActiveSubStacks.add(key)
                    }
                }
                // not top level - add to current substack
                else -> {
                    val currentStack = backStacks[currentTopLevelKey]!!
                    // single top
                    if (currentStack.lastOrNull() == key) {
                        currentStack.removeLastOrNull()
                    }
                    currentStack.add(key)
                    // no change to currentActiveTabs
                }
            }
            updateActiveTopLevelKeys(currentActiveSubStacks.toList())
        }
    }

    fun pop() {
        navigatorState.apply {
            val currentSubstack = backStacks[currentTopLevelKey]!!
            if (currentSubstack.size == 1) {
                // if current sub-stack only has one key, remove the sub-stack from the map
                currentSubstack.removeLastOrNull()
                backStacks.remove(currentTopLevelKey)
                updateActiveTopLevelKeys(activeTopLeveLKeys.dropLast(1))
            } else {
                currentSubstack.removeLastOrNull()
            }
        }
    }
}

interface NiaNavKey {
    val isTopLevel: Boolean
}

@Composable
public fun NiaNavigatorState.getEntries(
    entryProviderBuilders: Set<EntryProviderScope<NiaNavKey>.() -> Unit>,
): List<NavEntry<NiaNavKey>> =
    activeTopLeveLKeys.fold(emptyList()) { entries, topLevelKey ->
        val decorated = key(topLevelKey) {
            val decorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator<NiaNavKey>(),
            )
            rememberDecoratedNavEntries(
                backStack = backStacks[topLevelKey]!!,
                entryDecorators = decorators,
                entryProvider = entryProvider {
                    entryProviderBuilders.forEach { builder ->
                        builder()
                    }
                },
            )
        }
        entries + decorated
    }
