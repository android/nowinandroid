/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.navigation.FloatingWindow
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorState
import androidx.navigation.compose.LocalOwnersProvider
import androidx.navigation.get
import com.google.samples.apps.nowinandroid.core.ui.AdaptiveScaffoldNavigator.Destination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@Navigator.Name("AdaptiveScaffoldNavigator")
class AdaptiveScaffoldNavigator : Navigator<Destination>() {

    private var attached = MutableStateFlow(false)

    private val backStack: Flow<List<NavBackStackEntry>> = attached.flatMapLatest {
        if (it) {
            state.backStack
        } else {
            MutableStateFlow(emptyList())
        }
    }

    val isVisible = backStack.map { it.isNotEmpty() }

    val detailsPaneContent: @Composable () -> Unit = {
        val saveableStateHolder = rememberSaveableStateHolder()
        val currentBackStackEntry: NavBackStackEntry? by produceState<NavBackStackEntry?>(
            initialValue = null,
            key1 = backStack,
        ) {
            backStack.map { backstack ->
                backstack.lastOrNull()
            }.collect { backStackEntry ->
                value = backStackEntry
            }
        }
        val backStackEntry = currentBackStackEntry
        backStackEntry?.LocalOwnersProvider(saveableStateHolder) {
            val destination = (backStackEntry.destination as? Destination)
            destination?.content?.invoke(backStackEntry)
        }
    }

    override fun onAttach(state: NavigatorState) {
        super.onAttach(state)
        attached.value = true
    }

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?,
    ) {
        entries.forEach { entry ->
            state.pushWithTransition(entry)
        }
    }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        state.popWithTransition(popUpTo, savedState)
    }

    @NavDestination.ClassType(Composable::class)
    class Destination(
        navigator: AdaptiveScaffoldNavigator,
        internal val content: @Composable (NavBackStackEntry) -> Unit,
    ) : NavDestination(navigator), FloatingWindow

    override fun createDestination(): Destination = Destination(
        this,
    ) { /* no-op */ }
}

fun NavGraphBuilder.detailsPane(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (backstackEntry: NavBackStackEntry) -> Unit,
) {
    addDestination(
        Destination(
            provider[AdaptiveScaffoldNavigator::class],
            content,
        ).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        },
    )
}
