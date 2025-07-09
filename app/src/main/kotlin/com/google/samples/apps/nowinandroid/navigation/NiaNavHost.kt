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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.google.samples.apps.nowinandroid.feature.bookmarks.api.navigation.BookmarksRoute
import com.google.samples.apps.nowinandroid.feature.bookmarks.impl.navigation.bookmarksScreen
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.ForYouBaseRoute
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.forYouSection
import com.google.samples.apps.nowinandroid.feature.interests.navigation.InterestsRoute
import com.google.samples.apps.nowinandroid.feature.interests.navigation.navigateToInterests
import com.google.samples.apps.nowinandroid.feature.search.navigation.SearchRoute
import com.google.samples.apps.nowinandroid.feature.search.navigation.searchScreen
import com.google.samples.apps.nowinandroid.feature.topic.navigation.TopicRoute
import com.google.samples.apps.nowinandroid.feature.topic.navigation.topicScreen
import com.google.samples.apps.nowinandroid.navigation.TopLevelDestination.INTERESTS
import com.google.samples.apps.nowinandroid.ui.NiaAppState
import com.google.samples.apps.nowinandroid.ui.interests2pane.interestsListDetailScreen

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
) {
    val navController = appState.navController
    val nav3Navigator = appState.nav3Navigator
    NavDisplay(
        backStack = nav3Navigator.backStack,
        onBack = { nav3Navigator.goBack() },
        entryProvider = entryProvider(
            fallback = { key ->
                println("$key not found, using fallback entry")
                NavEntry(key = key) {
                    NavHost(
                        navController = navController,
                        startDestination = ForYouBaseRoute,
                        modifier = modifier,
                    ) {
                        forYouSection(
                            onTopicClick = {
                                nav3Navigator.goTo(route = TopicRoute(it))
                            },
                        ) {
                            composable<TopicRoute>{}
                        }
                        composable<BookmarksRoute> {}
                        composable<SearchRoute> {}
                        interestsListDetailScreen()
                    }
                }
            },
        ) {
            bookmarksScreen(
                onTopicClick = { it: String ->
                    nav3Navigator.goTo(route = InterestsRoute(it))
                },
                onShowSnackbar = onShowSnackbar
            )
            topicScreen(
                showBackButton = true,
                onBackClick = { nav3Navigator.goBack() },
                onTopicClick = {
                    nav3Navigator.goTo(route = TopicRoute(it))
                },
            )
            searchScreen(
                onBackClick = navController::popBackStack,
                onInterestsClick = { appState.navigateToTopLevelDestination(INTERESTS) },
                onTopicClick = navController::navigateToInterests,
            )
        },
    )
}
