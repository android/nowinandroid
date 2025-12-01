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
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.NavDisplay
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavKey
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigator
import com.google.samples.apps.nowinandroid.core.navigation.toEntries

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NiaNavDisplay(
    niaNavigator: NiaNavigator,
    entryProviderBuilders: Set<EntryProviderScope<NiaNavKey>.() -> Unit>,
) {
    val listDetailStrategy = rememberListDetailSceneStrategy<NiaNavKey>()
    val entries = niaNavigator.navigationState.toEntries(entryProviderBuilders)
    NavDisplay(
        entries = entries,
        sceneStrategy = listDetailStrategy,
        onBack = { niaNavigator.pop() },
    )
}
