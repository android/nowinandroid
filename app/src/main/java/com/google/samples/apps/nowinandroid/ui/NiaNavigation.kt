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

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Routes for the different destinations in the application. Each of these destinations can contain
 * one or more screens (based on the window size). Navigation from one screen to the next within a
 * single destination will be handled directly in Compose, not using the Navigation component.
 */
object NiaDestinations {
    const val FOR_YOU_ROUTE = "for_you"
    const val EPISODES_ROUTE = "episodes"
    const val SAVED_ROUTE = "saved"
    const val FOLLOWING_ROUTE = "following"
    const val TOPIC_ROUTE = "topic"
}

/**
 * Models the navigation actions in the app.
 */
class NiaNavigationActions(private val navController: NavHostController) {
    fun navigateToTopLevelDestination(route: String) {
        navController.navigate(route) {
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
    }
}
