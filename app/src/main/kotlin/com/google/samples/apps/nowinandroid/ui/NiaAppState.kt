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

package com.google.samples.apps.nowinandroid.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import androidx.tracing.trace
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.ui.TrackDisposableJank
import com.google.samples.apps.nowinandroid.feature.bookmarks.api.navigation.BookmarksRoute
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.navigateToForYou
import com.google.samples.apps.nowinandroid.feature.interests.navigation.InterestsRoute
import com.google.samples.apps.nowinandroid.feature.search.navigation.SearchRoute
import com.google.samples.apps.nowinandroid.feature.search.navigation.navigateToSearch
import com.google.samples.apps.nowinandroid.feature.topic.navigation.TopicRoute
import com.google.samples.apps.nowinandroid.navigation.TopLevelDestination
import com.google.samples.apps.nowinandroid.navigation.TopLevelDestination.BOOKMARKS
import com.google.samples.apps.nowinandroid.navigation.TopLevelDestination.FOR_YOU
import com.google.samples.apps.nowinandroid.navigation.TopLevelDestination.INTERESTS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone

@Composable
fun rememberNiaAppState(
    networkMonitor: NetworkMonitor,
    userNewsResourceRepository: UserNewsResourceRepository,
    timeZoneMonitor: TimeZoneMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): NiaAppState {
    NavigationTrackingSideEffect(navController)

    val nav3Navigator = remember(navController) {
        Nav3NavigatorSimple(navController)
    }

    return remember(
        navController,
        coroutineScope,
        networkMonitor,
        userNewsResourceRepository,
        timeZoneMonitor,
    ) {
        NiaAppState(
            navController = navController,
            nav3Navigator = nav3Navigator,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
            userNewsResourceRepository = userNewsResourceRepository,
            timeZoneMonitor = timeZoneMonitor,
        )
    }
}

@Stable
class NiaAppState(
    val navController: NavHostController,
    val nav3Navigator: Nav3NavigatorSimple,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    userNewsResourceRepository: UserNewsResourceRepository,
    timeZoneMonitor: TimeZoneMonitor,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) == true
            }
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * The top level destinations that have unread news resources.
     */
    val topLevelDestinationsWithUnreadResources: StateFlow<Set<TopLevelDestination>> =
        userNewsResourceRepository.observeAllForFollowedTopics()
            .combine(userNewsResourceRepository.observeAllBookmarked()) { forYouNewsResources, bookmarkedNewsResources ->
                setOfNotNull(
                    FOR_YOU.takeIf { forYouNewsResources.any { !it.hasBeenViewed } },
                    BOOKMARKS.takeIf { bookmarkedNewsResources.any { !it.hasBeenViewed } },
                )
            }
            .stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet(),
            )

    val currentTimeZone = timeZoneMonitor.currentTimeZone
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5_000),
            TimeZone.currentSystemDefault(),
        )

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

            when (topLevelDestination) {
                FOR_YOU -> navController.navigateToForYou(topLevelNavOptions)
                BOOKMARKS -> nav3Navigator.goTo(route = BookmarksRoute, topLevelNavOptions)
                INTERESTS -> {
                    nav3Navigator.goTo(route = InterestsRoute(null), topLevelNavOptions)
                }
            }
        }
    }

    fun navigateToSearch() = navController.navigateToSearch()
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}

class Nav3NavigatorSimple(val navController: NavHostController){

    val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    // We need a single element to avoid "backStack cannot be empty" error b/430023647
    val backStack = mutableStateListOf<Any>(Unit)

    // Expose the current top level route for consumers
    lateinit var topLevelKey : String

    // key = top level route string, value = route instance
    private var topLevelStacks : LinkedHashMap<String, MutableList<Any>> = linkedMapOf()

    init {
        coroutineScope.launch {
            navController.currentBackStack.collect { nav2BackStack ->

                topLevelStacks.clear()

                val rootNode : NavGraph = navController.graph
                println("Root node start destination: ${rootNode.startDestinationRoute}")

                nav2BackStack.forEach { entry ->

                    println("\n==ENTRY==")
                    println("Entry: $entry")
                    println("Destination: ${entry.destination}")
                    println("Destination navigatorName: ${entry.destination.navigatorName}")
                    println("Route: ${entry.destination.route}")
                    println("Parent destination: ${entry.destination.parent}")

                    val destination = entry.destination

                    // We only care about navigable destinations
                    if (destination.navigatorName == "composable"){

                        println("Entry is a composable")
                        val parentDestination = destination.parent
                        val stackName = destination.route

                        val routeInstance =
                            if (destination.hasRoute<BookmarksRoute>()) {
                                entry.toRoute<BookmarksRoute>()
                            } else if (destination.hasRoute<TopicRoute>()) {
                                entry.toRoute<TopicRoute>()
                            } else if (destination.hasRoute<SearchRoute>()) {
                                entry.toRoute<SearchRoute>()
                            } else if (destination.hasRoute<InterestsRoute>()) {
                                entry.toRoute<InterestsRoute>()
                            } else {
                                // Non migrated top level route
                                println("Non migrated route")
                                entry
                            }

                        if (parentDestination == rootNode){
                            println("This is a top level route")
                            // Create a new top level stack, if it doesn't exist already
                            if (stackName != null && topLevelStacks[stackName] == null){
                                add(stackName, routeInstance)
                            }
                        } else {
                            println("This is a nested route")
                            // This is a nested route, add it to the appropriate stack
                            val startDestinationRoute = parentDestination?.startDestinationRoute
                            println("Its parent has a start destination route of $startDestinationRoute")
                            if (startDestinationRoute != null){
                                if (startDestinationRoute == stackName){
                                    println("This is the start destination, creating new nested stack")
                                    // This is the start destination, create a new stack
                                    add(stackName, routeInstance)
                                } else {
                                    println("This is not the start destination, adding to nested stack")
                                    // This is not the start destination, just add it to the
                                    // previously created route
                                    add(startDestinationRoute, routeInstance)
                                }
                            }
                        }
                    }
                }

                updateBackStack()
            }
        }
    }

    fun add(stackName: String, route: Any){
        if (topLevelStacks[stackName] == null){
            topLevelStacks.put(stackName, mutableListOf(route))
        } else {
            topLevelStacks[stackName]?.add(route)
        }
        topLevelKey = stackName
        updateBackStack()
    }

    private fun updateBackStack() {
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }
        println("Back stack: $backStack")
    }

    fun goBack(){
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        topLevelKey = topLevelStacks.keys.last()
        updateBackStack()
        navController.popBackStack()
    }

    fun goTo(route: Any, navOptions: NavOptions? = null){
        add(topLevelKey, route)
        navController.navigate(route = route, navOptions = navOptions)
    }
}

