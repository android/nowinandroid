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

import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.samples.apps.nowinandroid.feature.following.InterestsRoute
import com.google.samples.apps.nowinandroid.feature.foryou.ForYouRoute
import com.google.samples.apps.nowinandroid.feature.topic.InterestsDestinations
import com.google.samples.apps.nowinandroid.feature.topic.InterestsScreens.TOPIC_SCREEN
import com.google.samples.apps.nowinandroid.feature.topic.TopicDestinationsArgs
import com.google.samples.apps.nowinandroid.feature.topic.TopicRoute

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun NiaNavGraph(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NiaDestinations.FOR_YOU_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(NiaDestinations.FOR_YOU_ROUTE) {
            ForYouRoute(windowSizeClass)
        }
        composable(NiaDestinations.EPISODES_ROUTE) {
            Text("EPISODES")
        }
        composable(NiaDestinations.SAVED_ROUTE) {
            Text("SAVED")
        }
        navigation(
            startDestination = InterestsDestinations.INTERESTS_ROUTE,
            route = NiaDestinations.FOLLOWING_ROUTE
        ) {
            composable(InterestsDestinations.INTERESTS_ROUTE) {
                InterestsRoute(
                    navigateToTopic = { navController.navigate("$TOPIC_SCREEN/$it") },
                    navigateToAuthor = { /* TO IMPLEMENT */ },
                )
            }
            composable(
                InterestsDestinations.TOPIC_ROUTE,
                arguments = listOf(
                    navArgument(TopicDestinationsArgs.TOPIC_ID_ARG) {
                        type = NavType.StringType
                    }
                )
            ) {
                TopicRoute(onBackClick = { navController.popBackStack() })
            }
        }
    }
}
