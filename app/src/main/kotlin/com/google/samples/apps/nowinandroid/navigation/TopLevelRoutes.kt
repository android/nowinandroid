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

import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.feature.bookmarks.api.navigation.BookmarksRoute
import com.google.samples.apps.nowinandroid.feature.foryou.api.navigation.ForYouRoute
import com.google.samples.apps.nowinandroid.feature.interests.api.navigation.InterestsRoute
import com.google.samples.apps.nowinandroid.feature.bookmarks.api.R as bookmarksR
import com.google.samples.apps.nowinandroid.feature.foryou.api.R as forYouR
import com.google.samples.apps.nowinandroid.feature.search.api.R as searchR

val FOR_YOU = TopLevelNavItem(
    selectedIcon = NiaIcons.Upcoming,
    unselectedIcon = NiaIcons.UpcomingBorder,
    iconTextId = forYouR.string.feature_foryou_api_title,
    titleTextId = R.string.app_name,
)

val BOOKMARKS = TopLevelNavItem(
    selectedIcon = NiaIcons.Bookmarks,
    unselectedIcon = NiaIcons.BookmarksBorder,
    iconTextId = bookmarksR.string.feature_bookmarks_api_title,
    titleTextId = bookmarksR.string.feature_bookmarks_api_title,
)

val INTERESTS = TopLevelNavItem(
    selectedIcon = NiaIcons.Grid3x3,
    unselectedIcon = NiaIcons.Grid3x3,
    iconTextId = searchR.string.feature_search_api_interests,
    titleTextId = searchR.string.feature_search_api_interests,
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    ForYouRoute to FOR_YOU,
    BookmarksRoute to BOOKMARKS,
    InterestsRoute(null) to INTERESTS,
)
