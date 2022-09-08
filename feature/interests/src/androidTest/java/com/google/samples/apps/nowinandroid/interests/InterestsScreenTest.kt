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

package com.google.samples.apps.nowinandroid.interests

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.feature.interests.InterestsScreen
import com.google.samples.apps.nowinandroid.feature.interests.InterestsTabState
import com.google.samples.apps.nowinandroid.feature.interests.InterestsUiState
import com.google.samples.apps.nowinandroid.feature.interests.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Interests screen;
 * Verifies that, when a specific UiState is set, the corresponding
 * composables and details are shown
 */
class InterestsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var interestsLoading: String
    private lateinit var interestsEmptyHeader: String
    private lateinit var interestsTopicCardFollowButton: String
    private lateinit var interestsTopicCardUnfollowButton: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            interestsLoading = getString(R.string.interests_loading)
            interestsEmptyHeader = getString(R.string.interests_empty_header)
            interestsTopicCardFollowButton =
                getString(R.string.interests_card_follow_button_content_desc)
            interestsTopicCardUnfollowButton =
                getString(R.string.interests_card_unfollow_button_content_desc)
        }
    }

    @Test
    fun niaLoadingWheel_inTopics_whenScreenIsLoading_showLoading() {
        composeTestRule.setContent {
            InterestsScreen(uiState = InterestsUiState.Loading, tabIndex = 0)
        }

        composeTestRule
            .onNodeWithContentDescription(interestsLoading)
            .assertExists()
    }

    @Test
    fun niaLoadingWheel_inAuthors_whenScreenIsLoading_showLoading() {
        composeTestRule.setContent {
            InterestsScreen(uiState = InterestsUiState.Loading, tabIndex = 1)
        }

        composeTestRule
            .onNodeWithContentDescription(interestsLoading)
            .assertExists()
    }

    @Test
    fun interestsWithTopics_whenTopicsFollowed_showFollowedAndUnfollowedTopicsWithInfo() {
        composeTestRule.setContent {
            InterestsScreen(
                uiState = InterestsUiState.Interests(topics = testTopics, authors = listOf()),
                tabIndex = 0
            )
        }

        composeTestRule
            .onNodeWithText(TOPIC_1_NAME)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(TOPIC_2_NAME)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(TOPIC_3_NAME)
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(TOPIC_SHORT_DESC)
            .assertCountEquals(testTopics.count())

        composeTestRule
            .onAllNodesWithContentDescription(interestsTopicCardFollowButton)
            .assertCountEquals(numberOfUnfollowedTopics)

        composeTestRule
            .onAllNodesWithContentDescription(interestsTopicCardUnfollowButton)
            .assertCountEquals(testAuthors.filter { it.isFollowed }.size)
    }

    @Test
    fun interestsWithTopics_whenAuthorsFollowed_showFollowedAndUnfollowedTopicsWithInfo() {
        composeTestRule.setContent {
            InterestsScreen(
                uiState = InterestsUiState.Interests(topics = listOf(), authors = testAuthors),
                tabIndex = 1
            )
        }

        composeTestRule
            .onNodeWithText("Android Dev")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Android Dev 2")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Android Dev 3")
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithContentDescription(interestsTopicCardFollowButton)
            .assertCountEquals(numberOfUnfollowedAuthors)

        composeTestRule
            .onAllNodesWithContentDescription(interestsTopicCardUnfollowButton)
            .assertCountEquals(testTopics.filter { it.isFollowed }.size)
    }

    @Test
    fun topicsEmpty_whenDataIsEmptyOccurs_thenShowEmptyScreen() {
        composeTestRule.setContent {
            InterestsScreen(uiState = InterestsUiState.Empty, tabIndex = 0)
        }

        composeTestRule
            .onNodeWithText(interestsEmptyHeader)
            .assertIsDisplayed()
    }

    @Test
    fun authorsEmpty_whenDataIsEmptyOccurs_thenShowEmptyScreen() {
        composeTestRule.setContent {
            InterestsScreen(uiState = InterestsUiState.Empty, tabIndex = 1)
        }

        composeTestRule
            .onNodeWithText(interestsEmptyHeader)
            .assertIsDisplayed()
    }

    @Composable
    private fun InterestsScreen(uiState: InterestsUiState, tabIndex: Int = 0) {
        InterestsScreen(
            uiState = uiState,
            tabState = InterestsTabState(
                titles = listOf(R.string.interests_topics, R.string.interests_people),
                currentIndex = tabIndex
            ),
            followAuthor = { _, _ -> },
            followTopic = { _, _ -> },
            navigateToAuthor = {},
            navigateToTopic = {},
            switchTab = {},
        )
    }
}

private const val TOPIC_1_NAME = "Headlines"
private const val TOPIC_2_NAME = "UI"
private const val TOPIC_3_NAME = "Tools"
private const val TOPIC_SHORT_DESC = "At vero eos et accusamus."
private const val TOPIC_LONG_DESC = "At vero eos et accusamus et iusto odio dignissimos ducimus."
private const val TOPIC_URL = "URL"
private const val TOPIC_IMAGE_URL = "Image URL"

private val testTopics = listOf(
    FollowableTopic(
        Topic(
            id = "0",
            name = TOPIC_1_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFollowed = true
    ),
    FollowableTopic(
        Topic(
            id = "1",
            name = TOPIC_2_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFollowed = false
    ),
    FollowableTopic(
        Topic(
            id = "2",
            name = TOPIC_3_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFollowed = false
    )
)

private val testAuthors = listOf(
    FollowableAuthor(
        Author(
            id = "0",
            name = "Android Dev",
            imageUrl = "",
            twitter = "",
            mediumPage = "",
            bio = "",
        ),
        isFollowed = true
    ),
    FollowableAuthor(
        Author(
            id = "1",
            name = "Android Dev 2",
            imageUrl = "",
            twitter = "",
            mediumPage = "",
            bio = "",
        ),
        isFollowed = false
    ),
    FollowableAuthor(
        Author(
            id = "2",
            name = "Android Dev 3",
            imageUrl = "",
            twitter = "",
            mediumPage = "",
            bio = "",
        ),
        isFollowed = false
    )
)

private val numberOfUnfollowedTopics = testTopics.filter { !it.isFollowed }.size
private val numberOfUnfollowedAuthors = testAuthors.filter { !it.isFollowed }.size
