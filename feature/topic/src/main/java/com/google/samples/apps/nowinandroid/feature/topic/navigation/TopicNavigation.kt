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

import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.samples.apps.nowinandroid.core.decoder.StringDecoder
import com.google.samples.apps.nowinandroid.feature.topic.TopicRoute

@VisibleForTesting
internal const val topicIdArg = "topicId"

internal class TopicArgs(val topicId: String) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
        this(stringDecoder.decodeString(checkNotNull(savedStateHandle[topicIdArg])))
}

fun NavController.navigateToTopic(topicId: String) {
    val encodedId = Uri.encode(topicId)
    this.navigate("topic_route/$encodedId")
}

fun NavGraphBuilder.topicScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "topic_route/{$topicIdArg}",
        arguments = listOf(
            navArgument(topicIdArg) { type = NavType.StringType },
        ),
    ) {
        TopicRoute(onBackClick = onBackClick)
    }
}
