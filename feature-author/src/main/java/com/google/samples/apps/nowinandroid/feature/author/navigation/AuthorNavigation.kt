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

package com.google.samples.apps.nowinandroid.feature.author.navigation

import android.net.Uri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigationDestination
import com.google.samples.apps.nowinandroid.feature.author.AuthorRoute

object AuthorDestination : NiaNavigationDestination {
    const val authorIdArg = "authorId"
    override val route = "author_route/{$authorIdArg}"
    override val destination = "author_destination"

    /**
     * Creates destination route for an authorId that could include special characters
     */
    fun createNavigationRoute(authorIdArg: String): String {
        val encodedId = Uri.encode(authorIdArg)
        return "author_route/$encodedId"
    }

    /**
     * Returns the authorId from a [NavBackStackEntry] after an author destination navigation call
     */
    fun fromNavArgs(entry: NavBackStackEntry): String {
        val encodedId = entry.arguments?.getString(authorIdArg)!!
        return Uri.decode(encodedId)
    }
}

fun NavGraphBuilder.authorGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = AuthorDestination.route,
        arguments = listOf(
            navArgument(AuthorDestination.authorIdArg) { type = NavType.StringType }
        )
    ) {
        AuthorRoute(onBackClick = onBackClick)
    }
}
