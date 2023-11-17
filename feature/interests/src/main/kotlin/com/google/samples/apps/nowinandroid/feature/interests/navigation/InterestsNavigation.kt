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

package com.google.samples.apps.nowinandroid.feature.interests.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.samples.apps.nowinandroid.feature.interests.InterestsRoute

internal const val topicIdArg = "topicId"
const val interestsRoute = "interests_route?$topicIdArg={$topicIdArg}"

fun NavController.navigateToInterestsGraph(
    topicId: String? = null,
    navOptions: NavOptions? = null,
) {
    if (topicId == null) {
        navigate("interests_route", navOptions)
    } else {
        navigate("interests_route?$topicIdArg=$topicId", navOptions)
    }
}

fun NavGraphBuilder.interestsGraph(
    detailsPane: @Composable (String) -> Unit,
) {
    composable(
        route = interestsRoute,
        arguments = listOf(
            navArgument(topicIdArg) {
                defaultValue = null
                nullable = true
            },
        ),
    ) {
        InterestsRoute(detailsPane)
    }
}
