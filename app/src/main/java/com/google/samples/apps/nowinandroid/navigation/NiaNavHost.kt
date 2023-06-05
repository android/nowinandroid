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

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.google.samples.apps.nowinandroid.feature.bookmarks.navigation.bookmarksScreen
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.forYouNavigationRoute
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.forYouScreen
import com.google.samples.apps.nowinandroid.feature.interests.navigation.interestsScreen
import com.google.samples.apps.nowinandroid.feature.interests.navigation.navigateToInterests
import com.google.samples.apps.nowinandroid.feature.search.navigation.searchScreen
import com.google.samples.apps.nowinandroid.ui.NiaAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun NiaNavHost(
    appState: NiaAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = forYouNavigationRoute,
) {
    val navController = appState.navController
    val interestsScrollState = rememberLazyGridState()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        // TODO: handle topic clicks from each top level destination
        forYouScreen(onTopicClick = navController::navigateToInterests)
        bookmarksScreen(
            onTopicClick = navController::navigateToInterests,
            onShowSnackbar = onShowSnackbar,
        )
        searchScreen(
            onBackClick = navController::popBackStack,
            onInterestsClick = navController::navigateToInterests,
            onTopicClick = navController::navigateToInterests,
        )
        interestsScreen(
            listState = interestsScrollState,
            shouldShowTwoPane = appState.shouldShowTwoPane,
            onTopicClick = navController::navigateToInterests,
            onBackClick = navController::navigateToInterests,
        )
    }
}
