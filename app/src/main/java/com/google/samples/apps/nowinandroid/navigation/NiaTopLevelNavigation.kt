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

package com.google.samples.apps.nowinandroid.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.samples.apps.nowinandroid.core.designsystem.icon.Icon
import com.google.samples.apps.nowinandroid.core.designsystem.icon.Icon.DrawableResourceIcon
import com.google.samples.apps.nowinandroid.core.designsystem.icon.Icon.ImageVectorIcon
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.feature.bookmarks.navigation.BookmarksDestination
import com.google.samples.apps.nowinandroid.feature.foryou.R.string.for_you
import com.google.samples.apps.nowinandroid.feature.foryou.R.string.saved
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.ForYouDestination
import com.google.samples.apps.nowinandroid.feature.interests.R.string.interests
import com.google.samples.apps.nowinandroid.feature.interests.navigation.InterestsDestination

/**
 * Routes for the different top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */

/**
 * Models the navigation top level actions in the app.
 */
class NiaTopLevelNavigation(private val navController: NavHostController) {

    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
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

data class TopLevelDestination(
    val route: String,
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        route = ForYouDestination.route,
        selectedIcon = DrawableResourceIcon(NiaIcons.Upcoming),
        unselectedIcon = DrawableResourceIcon(NiaIcons.UpcomingBorder),
        iconTextId = for_you
    ),
    TopLevelDestination(
        route = BookmarksDestination.route,
        selectedIcon = DrawableResourceIcon(NiaIcons.Bookmarks),
        unselectedIcon = DrawableResourceIcon(NiaIcons.BookmarksBorder),
        iconTextId = saved
    ),
    TopLevelDestination(
        route = InterestsDestination.route,
        selectedIcon = ImageVectorIcon(NiaIcons.Grid3x3),
        unselectedIcon = ImageVectorIcon(NiaIcons.Grid3x3),
        iconTextId = interests
    )
)
