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

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStack
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStackKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NiaNavDisplay(
    niaBackStack: NiaBackStack,
    entryProviderBuilders: Set<@JvmSuppressWildcards EntryProviderBuilder<NiaBackStackKey>.() -> Unit>,
) {
    val listDetailStrategy = rememberListDetailSceneStrategy<NiaBackStackKey>()

    NavDisplay(
        backStack = niaBackStack.backStack,
        sceneStrategy = listDetailStrategy,
        onBack = { niaBackStack.removeLast() },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entryProviderBuilders.forEach { builder ->
                builder()
            }
        },
    )
}