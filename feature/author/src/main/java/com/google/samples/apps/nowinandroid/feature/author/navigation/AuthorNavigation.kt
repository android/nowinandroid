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
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.samples.apps.nowinandroid.core.decoder.StringDecoder
import com.google.samples.apps.nowinandroid.feature.author.AuthorRoute

@VisibleForTesting
internal const val authorIdArg = "authorId"

internal class AuthorArgs(val authorId: String) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
        this(stringDecoder.decodeString(checkNotNull(savedStateHandle[authorIdArg])))
}

fun NavController.navigateToAuthor(authorId: String) {
    val encodedString = Uri.encode(authorId)
    this.navigate("author_route/$encodedString")
}

fun NavGraphBuilder.authorScreen(
    onBackClick: () -> Unit,
    navigateToTopic: (String) -> Unit,
) {
    composable(
        route = "author_route/{$authorIdArg}",
        arguments = listOf(
            navArgument(authorIdArg) { type = NavType.StringType }
        )
    ) {
        AuthorRoute(onBackClick = onBackClick, navigateToTopic = navigateToTopic)
    }
}
