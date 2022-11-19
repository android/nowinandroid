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
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.nextFakeAuthor
import com.google.samples.apps.nowinandroid.core.model.data.nextFakeNewsResource
import kotlin.random.Random
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Author screen;
 * Verifies that, when a specific UiState is set, the corresponding
 * composables and details are shown
 */
class AuthorScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var authorLoading: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            authorLoading = getString(R.string.author_loading)
        }
    }

    @Test
    fun niaLoadingWheel_whenScreenIsLoading_showLoading() {
        composeTestRule.setContent {
            AuthorScreen(
                authorUiState = AuthorUiState.Loading,
                newsUiState = NewsUiState.Loading,
                onBackClick = { },
                onFollowClick = { },
                onBookmarkChanged = { _, _ -> },
            )
        }

        composeTestRule
            .onNodeWithContentDescription(authorLoading)
            .assertExists()
    }

    @Test
    fun authorTitle_whenAuthorIsSuccess_isShown() {
        val testAuthor = testAuthors.first()
        composeTestRule.setContent {
            AuthorScreen(
                authorUiState = AuthorUiState.Success(testAuthor),
                newsUiState = NewsUiState.Loading,
                onBackClick = { },
                onFollowClick = { },
                onBookmarkChanged = { _, _ -> },
            )
        }

        // Name is shown
        composeTestRule
            .onNodeWithText(testAuthor.author.name)
            .assertExists()

        // Bio is shown
        composeTestRule
            .onNodeWithText(testAuthor.author.bio)
            .assertExists()
    }

    @Test
    fun news_whenAuthorIsLoading_isNotShown() {
        composeTestRule.setContent {
            AuthorScreen(
                authorUiState = AuthorUiState.Loading,
                newsUiState = NewsUiState.Success(
                    sampleNewsResources.mapIndexed { index, newsResource ->
                        SaveableNewsResource(
                            newsResource = newsResource,
                            isSaved = index % 2 == 0,
                        )
                    }
                ),
                onBackClick = { },
                onFollowClick = { },
                onBookmarkChanged = { _, _ -> },
            )
        }

        // Loading indicator shown
        composeTestRule
            .onNodeWithContentDescription(authorLoading)
            .assertExists()
    }

    @Test
    fun news_whenSuccessAndAuthorIsSuccess_isShown() {
        val testAuthor = testAuthors.first()
        composeTestRule.setContent {
            AuthorScreen(
                authorUiState = AuthorUiState.Success(testAuthor),
                newsUiState = NewsUiState.Success(
                    sampleNewsResources.mapIndexed { index, newsResource ->
                        SaveableNewsResource(
                            newsResource = newsResource,
                            isSaved = index % 2 == 0,
                        )
                    }
                ),
                onBackClick = { },
                onFollowClick = { },
                onBookmarkChanged = { _, _ -> },
            )
        }

        // First news title shown
        composeTestRule
            .onNodeWithText(sampleNewsResources.first().title)
            .assertExists()
    }
}

private val testAuthors = listOf(
    FollowableAuthor(Random.nextFakeAuthor(id = "1"), isFollowed = true),
    FollowableAuthor(Random.nextFakeAuthor(id = "2"), isFollowed = false),
    FollowableAuthor(Random.nextFakeAuthor(id = "3"), isFollowed = false),
)

private val sampleNewsResources = listOf(
    Random.nextFakeNewsResource(id = "1", authors = listOf(testAuthors.first().author)),
)
