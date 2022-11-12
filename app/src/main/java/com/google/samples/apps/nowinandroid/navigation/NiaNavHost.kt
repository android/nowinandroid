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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.samples.apps.nowinandroid.feature.author.navigation.authorScreen
import com.google.samples.apps.nowinandroid.feature.author.navigation.navigateToAuthor
import com.google.samples.apps.nowinandroid.feature.bookmarks.navigation.bookmarksScreen
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.forYouNavigationRoute
import com.google.samples.apps.nowinandroid.feature.foryou.navigation.forYouScreen
import com.google.samples.apps.nowinandroid.feature.interests.navigation.interestsGraph
import com.google.samples.apps.nowinandroid.feature.topic.navigation.navigateToTopic
import com.google.samples.apps.nowinandroid.feature.topic.navigation.topicScreen

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
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = forYouNavigationRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        forYouScreen(
            navigateToTopic = { topicId ->
                navController.navigateToTopic(topicId)
            }
        )
        bookmarksScreen(
            navigateToTopic = { topicId ->
                navController.navigateToTopic(topicId)
            }
        )
        interestsGraph(
            navigateToTopic = { topicId ->
                navController.navigateToTopic(topicId)
            },
            navigateToAuthor = { authorId ->
                navController.navigateToAuthor(authorId)
            },
            nestedGraphs = {
                topicScreen(
                    onBackClick = onBackClick,
                    navigateToTopic = { topicId ->
                        navController.navigateToTopic(topicId)
                    }
                )
                authorScreen(
                    onBackClick = onBackClick,
                    navigateToTopic = { topicId ->
                        navController.navigateToTopic(topicId)
                    }
                )
            }
        )
    }
}
