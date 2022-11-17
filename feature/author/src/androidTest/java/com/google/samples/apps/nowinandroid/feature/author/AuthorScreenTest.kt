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
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import kotlinx.datetime.Instant
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
                onBrowseTopic = {}
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
                onBrowseTopic = {}
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
                onBrowseTopic = {}
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
                onBrowseTopic = {}
            )
        }

        // First news title shown
        composeTestRule
            .onNodeWithText(sampleNewsResources.first().title)
            .assertExists()
    }
}

private const val AUTHOR_1_NAME = "Author 1"
private const val AUTHOR_2_NAME = "Author 2"
private const val AUTHOR_3_NAME = "Author 3"
private const val AUTHOR_BIO = "At vero eos et accusamus et iusto odio dignissimos ducimus qui."

private val testAuthors = listOf(
    FollowableAuthor(
        Author(
            id = "0",
            name = AUTHOR_1_NAME,
            twitter = "",
            bio = AUTHOR_BIO,
            mediumPage = "",
            imageUrl = ""
        ),
        isFollowed = true
    ),
    FollowableAuthor(
        Author(
            id = "1",
            name = AUTHOR_2_NAME,
            twitter = "",
            bio = AUTHOR_BIO,
            mediumPage = "",
            imageUrl = ""
        ),
        isFollowed = false
    ),
    FollowableAuthor(
        Author(
            id = "2",
            name = AUTHOR_3_NAME,
            twitter = "",
            bio = AUTHOR_BIO,
            mediumPage = "",
            imageUrl = ""
        ),
        isFollowed = false
    )
)

private val sampleNewsResources = listOf(
    NewsResource(
        id = "1",
        title = "Thanks for helping us reach 1M YouTube Subscribers",
        content = "Thank you everyone for following the Now in Android series and everything the " +
            "Android Developers YouTube channel has to offer. During the Android Developer " +
            "Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to " +
            "thank you all.",
        url = "https://youtu.be/-fJ6poHQrjM",
        headerImageUrl = "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
        type = Video,
        authors = listOf(
            Author(
                id = "0",
                name = "Headlines",
                twitter = "",
                bio = AUTHOR_BIO,
                mediumPage = "",
                imageUrl = ""
            )
        ),
        topics = emptyList()
    )
)
