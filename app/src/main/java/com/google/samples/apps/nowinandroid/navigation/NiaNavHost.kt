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

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigationDestination
import com.google.samples.apps.nowinandroid.feature.author.navigation.AuthorDestination
import com.google.samples.apps.nowinandroid.feature.author.navigation.authorGraph
import com.google.samples.apps.nowinandroid.feature.bookmarks.navigation.bookmarksGraph
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.ForYouDestination
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.forYouGraph
import com.google.samples.apps.nowinandroid.feature.interests.navigation.interestsGraph
import com.google.samples.apps.nowinandroid.feature.topic.navigation.TopicDestination
import com.google.samples.apps.nowinandroid.feature.topic.navigation.topicGraph

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun NiaNavHost(
    navController: NavHostController,
    onNavigateToDestination: (NiaNavigationDestination, String) -> Unit,
    onBackClick: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    startDestination: String = ForYouDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        forYouGraph(
            windowSizeClass = windowSizeClass
        )
        bookmarksGraph(windowSizeClass)
        interestsGraph(
            navigateToTopic = {
                onNavigateToDestination(
                    TopicDestination, TopicDestination.createNavigationRoute(it)
                )
            },
            navigateToAuthor = {
                onNavigateToDestination(
                    AuthorDestination, AuthorDestination.createNavigationRoute(it)
                )
            },
            nestedGraphs = {
                topicGraph(onBackClick)
                authorGraph(onBackClick)
            }
        )
    }
}
