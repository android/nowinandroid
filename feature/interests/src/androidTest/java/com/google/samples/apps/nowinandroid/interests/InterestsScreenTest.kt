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
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.nextFakeAuthor
import com.google.samples.apps.nowinandroid.core.model.data.nextFakeTopic
import com.google.samples.apps.nowinandroid.feature.interests.InterestsScreen
import com.google.samples.apps.nowinandroid.feature.interests.InterestsTabState
import com.google.samples.apps.nowinandroid.feature.interests.InterestsUiState
import com.google.samples.apps.nowinandroid.feature.interests.R
import kotlin.random.Random
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
            .onNodeWithText(testTopics[0].topic.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(testTopics[1].topic.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(testTopics[2].topic.name)
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
private const val TOPIC_SHORT_DESC = "At vero eos et accusamus."
private val testTopics = listOf(
    FollowableTopic(
        Random.nextFakeTopic(id = "1", shortDescription = TOPIC_SHORT_DESC),
        isFollowed = true
    ),
    FollowableTopic(
        Random.nextFakeTopic(id = "2", shortDescription = TOPIC_SHORT_DESC),
        isFollowed = false
    ),
    FollowableTopic(
        Random.nextFakeTopic(id = "3", shortDescription = TOPIC_SHORT_DESC),
        isFollowed = false
    )
)

private val testAuthors = listOf(
    FollowableAuthor(Random.nextFakeAuthor(id = "1", name = "Android Dev"), isFollowed = true),
    FollowableAuthor(Random.nextFakeAuthor(id = "2", name = "Android Dev 2"), isFollowed = false),
    FollowableAuthor(Random.nextFakeAuthor(id = "3", name = "Android Dev 3"), isFollowed = false),
)

private val numberOfUnfollowedTopics = testTopics.filter { !it.isFollowed }.size
private val numberOfUnfollowedAuthors = testAuthors.filter { !it.isFollowed }.size
