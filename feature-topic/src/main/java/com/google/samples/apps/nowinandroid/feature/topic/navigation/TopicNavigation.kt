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

package com.google.samples.apps.nowinandroid.feature.topic.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigationDestination
import com.google.samples.apps.nowinandroid.feature.topic.TopicRoute

object TopicDestination : NiaNavigationDestination {
    override val route = "topic_route"
    override val destination = "topic_destination"
    const val topicIdArg = "topicId"
    val routeWithArgs = "$route/{$topicIdArg}"
}

fun NavGraphBuilder.topicGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = TopicDestination.routeWithArgs,
        arguments = listOf(
            navArgument(TopicDestination.topicIdArg) {
                type = NavType.StringType
            }
        )
    ) {
        TopicRoute(onBackClick = onBackClick)
    }
}