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

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.feature.bookmarks.api.navigation.BookmarksRoute
import com.google.samples.apps.nowinandroid.feature.foryou.api.navigation.ForYouRoute
import com.google.samples.apps.nowinandroid.feature.interests.api.navigation.InterestsRoute
import kotlin.reflect.KClass
import com.google.samples.apps.nowinandroid.feature.bookmarks.api.R as bookmarksR
import com.google.samples.apps.nowinandroid.feature.foryou.api.R as forYouR
import com.google.samples.apps.nowinandroid.feature.search.api.R as searchR

/**
 * Type for the top level destinations in the application. Contains metadata about the destination
 * that is used in the top app bar and common navigation UI.
 *
 * @param selectedIcon The icon to be displayed in the navigation UI when this destination is
 * selected.
 * @param unselectedIcon The icon to be displayed in the navigation UI when this destination is
 * not selected.
 * @param iconTextId Text that to be displayed in the navigation UI.
 * @param titleTextId Text that is displayed on the top app bar.
 * @param route The route to use when navigating to this destination.
 * @param baseRoute The highest ancestor of this destination. Defaults to [route], meaning that
 * there is a single destination in that section of the app (no nested destinations).
 */
/*
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val key: NiaNavKey,
) {
    FOR_YOU(
        selectedIcon = NiaIcons.Upcoming,
        unselectedIcon = NiaIcons.UpcomingBorder,
        iconTextId = forYouR.string.feature_foryou_api_title,
        titleTextId = R.string.app_name,
        route = ForYouRoute::class,
        key = ForYouRoute,
    ),
    BOOKMARKS(
        selectedIcon = NiaIcons.Bookmarks,
        unselectedIcon = NiaIcons.BookmarksBorder,
        iconTextId = bookmarksR.string.feature_bookmarks_api_title,
        titleTextId = bookmarksR.string.feature_bookmarks_api_title,
        route = BookmarksRoute::class,
        key = BookmarksRoute,
    ),
    INTERESTS(
        selectedIcon = NiaIcons.Grid3x3,
        unselectedIcon = NiaIcons.Grid3x3,
        iconTextId = searchR.string.feature_search_api_interests,
        titleTextId = searchR.string.feature_search_api_interests,
        route = InterestsRoute::class,
        key = InterestsRoute(null),
    ),
}

internal val TopLevelDestinations = TopLevelDestination.entries.associateBy { dest -> dest.key }
*/

val FOR_YOU = TopLevelDestination(
    selectedIcon = NiaIcons.Upcoming,
    unselectedIcon = NiaIcons.UpcomingBorder,
    iconTextId = forYouR.string.feature_foryou_api_title,
    titleTextId = R.string.app_name,
    route = ForYouRoute::class,
    key = ForYouRoute,
)

val BOOKMARKS = TopLevelDestination(
    selectedIcon = NiaIcons.Bookmarks,
    unselectedIcon = NiaIcons.BookmarksBorder,
    iconTextId = bookmarksR.string.feature_bookmarks_api_title,
    titleTextId = bookmarksR.string.feature_bookmarks_api_title,
    route = BookmarksRoute::class,
    key = BookmarksRoute,
)

val INTERESTS = TopLevelDestination(
    selectedIcon = NiaIcons.Grid3x3,
    unselectedIcon = NiaIcons.Grid3x3,
    iconTextId = searchR.string.feature_search_api_interests,
    titleTextId = searchR.string.feature_search_api_interests,
    route = InterestsRoute::class,
    key = InterestsRoute(null)
)


val TOP_LEVEL_ROUTES = mapOf<NavKey, TopLevelDestination>(
    ForYouRoute to FOR_YOU,
    BookmarksRoute to BOOKMARKS,
    InterestsRoute(null) to INTERESTS,
)


/**
 * Type for the top level destinations in the application. Contains metadata about the destination
 * that is used in the top app bar and common navigation UI.
 *
 * @param selectedIcon The icon to be displayed in the navigation UI when this destination is
 * selected.
 * @param unselectedIcon The icon to be displayed in the navigation UI when this destination is
 * not selected.
 * @param iconTextId Text that to be displayed in the navigation UI.
 * @param titleTextId Text that is displayed on the top app bar.
 * @param route The route to use when navigating to this destination.
 * @param baseRoute The highest ancestor of this destination. Defaults to [route], meaning that
 * there is a single destination in that section of the app (no nested destinations).
 */
data class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val key: NavKey,
)
