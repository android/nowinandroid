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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigator
import com.google.samples.apps.nowinandroid.core.navigation.simple.NavigationState
import com.google.samples.apps.nowinandroid.core.navigation.simple.rememberNavigationState
import com.google.samples.apps.nowinandroid.feature.foryou.api.navigation.ForYouRoute
import com.google.samples.apps.nowinandroid.navigation.BOOKMARKS
import com.google.samples.apps.nowinandroid.navigation.FOR_YOU
import com.google.samples.apps.nowinandroid.navigation.TopLevelDestination
import com.google.samples.apps.nowinandroid.navigation.TOP_LEVEL_ROUTES
//import com.google.samples.apps.nowinandroid.navigation.TopLevelDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone

@Composable
fun rememberNiaAppState(
    networkMonitor: NetworkMonitor,
    userNewsResourceRepository: UserNewsResourceRepository,
    timeZoneMonitor: TimeZoneMonitor,
    //niaNavigator: NiaNavigator,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): NiaAppState {
    //NavigationTrackingSideEffect(niaNavigator)

    val navigationState = rememberNavigationState(ForYouRoute, TOP_LEVEL_ROUTES.keys)

    return remember(
        //niaNavigator,
        navigationState,
        coroutineScope,
        networkMonitor,
        userNewsResourceRepository,
        timeZoneMonitor,
    ) {
        NiaAppState(
            //niaNavigator = niaNavigator,
            navigationState = navigationState,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
            userNewsResourceRepository = userNewsResourceRepository,
            timeZoneMonitor = timeZoneMonitor,
        )
    }
}

@Stable
class NiaAppState(
    //val niaNavigator: NiaNavigator,
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    userNewsResourceRepository: UserNewsResourceRepository,
    timeZoneMonitor: TimeZoneMonitor,
) {
/*
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = TOP_LEVEL_ROUTES[niaNavigator.navigationState.currentTopLevelKey]
*/
    // TODO: It seems unnecessary to expose this as a TopLevelDestination rather than just a key
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = TOP_LEVEL_ROUTES[navigationState.topLevelRoute]

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

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
}

/**
 * Stores information about navigation events to be used with JankStats
 */

// TODO: This shouldn't be commented out
@Composable
private fun NavigationTrackingSideEffect(niaNavigator: NiaNavigator) {
//    TrackDisposableJank(niaNavigator) { metricsHolder ->
//        snapshotFlow {
//            val stack = niaNavigator.backStack.toList()
//            metricsHolder.state?.putState("Navigation", stack.lastOrNull().toString())
//        }
//        onDispose { }
//    }
}
