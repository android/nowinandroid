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

package com.google.samples.apps.nowinandroid.feature.foryou.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.google.samples.apps.nowinandroid.core.notifications.DEEP_LINK_SCHEME_AND_HOST
import com.google.samples.apps.nowinandroid.core.notifications.FOR_YOU_PATH
import com.google.samples.apps.nowinandroid.feature.foryou.ForYouScreen
import kotlinx.serialization.Serializable

const val LINKED_NEWS_RESOURCE_ID = "linkedNewsResourceId"

private const val DEEP_LINK_BASE_PATH = "$DEEP_LINK_SCHEME_AND_HOST/$FOR_YOU_PATH"

@Serializable data class ForYouRoute(val linkedNewsResourceId: String? = null)

fun NavController.navigateToForYou(navOptions: NavOptions) = navigate(route = ForYouRoute(), navOptions)

fun NavGraphBuilder.forYouScreen(onTopicClick: (String) -> Unit) {
    composable<ForYouRoute>(
        deepLinks = listOf(
            navDeepLink<ForYouRoute>(basePath = DEEP_LINK_BASE_PATH),
        ),
    ) {
        ForYouScreen(onTopicClick)
    }
}
