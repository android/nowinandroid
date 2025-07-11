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

package com.google.samples.apps.nowinandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStack

@Composable
fun NiaNavDisplay(
    niaBackStack: NiaBackStack,
    entryProviderBuilders: Set<@JvmSuppressWildcards EntryProviderBuilder<Any>.() -> Unit>,
) {
    NavDisplay(
        backStack = niaBackStack.backStack,
        onBack = { niaBackStack.removeLast() },
        entryProvider = entryProvider {
            entryProviderBuilders.forEach { builder ->
                builder()
            }
        },
    )
}