/*
 * Copyright 2026 The Android Open Source Project
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
import androidx.navigation3.runtime.NavKey
import com.google.samples.apps.nowinandroid.core.model.data.UserNewsResource
import com.google.samples.apps.nowinandroid.core.navigation.NavigationState
import com.google.samples.apps.nowinandroid.core.navigation.rememberNavigationState
import com.google.samples.apps.nowinandroid.feature.bookmarks.navigation.BookmarksNavKey
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.ForYouNavKey

fun getTopLevelNavKeysWithUnreadResources(
    forYouNewsResources: List<UserNewsResource>,
    bookmarkedNewsResources: List<UserNewsResource>,
): Set<NavKey> = setOfNotNull(
    ForYouNavKey.takeIf { forYouNewsResources.any { !it.hasBeenViewed } },
    BookmarksNavKey.takeIf { bookmarkedNewsResources.any { !it.hasBeenViewed } },
)

@Composable
fun getNavigationState(): NavigationState =
    rememberNavigationState(startKey = ForYouNavKey, topLevelKeys = TOP_LEVEL_NAV_ITEMS.keys)

fun NavigationState.shouldShowGradiantBackground(): Boolean = currentTopLevelKey == ForYouNavKey
