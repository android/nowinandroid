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

package com.google.samples.apps.nowinandroid.feature.author

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource

internal class AuthorRobot(
    private val composeTestRule: AndroidComposeTestRule<
        ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    fun setContent(authorUiState: AuthorUiState, newsUiState: NewsUiState) {
        composeTestRule.setContent {
            AuthorScreen(
                authorUiState = authorUiState,
                newsUiState = newsUiState,
                onBackClick = { },
                onFollowClick = { },
                onBookmarkChanged = { _, _ -> },
            )
        }
    }

    fun loadingIndicatorExists() {
        val authorLoading = composeTestRule.activity.getString(R.string.author_loading)

        composeTestRule
            .onNodeWithContentDescription(authorLoading)
            .assertExists()
    }

    fun authorExists(author: FollowableAuthor) {
        composeTestRule
            .onNodeWithText(author.author.name)
            .assertExists()

        composeTestRule
            .onNodeWithText(author.author.bio)
            .assertExists()
    }

    fun newsResourceExists(newsResource: NewsResource) {
        composeTestRule
            .onNodeWithText(newsResource.title)
            .assertExists()
    }
}
