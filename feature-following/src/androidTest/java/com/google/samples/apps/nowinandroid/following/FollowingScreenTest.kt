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

package com.google.samples.apps.nowinandroid.following

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.feature.following.FollowingScreen
import com.google.samples.apps.nowinandroid.feature.following.FollowingUiState
import com.google.samples.apps.nowinandroid.feature.following.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Following screen;
 * Verifies that, when a specific UiState is set, the corresponding
 * composables and details are shown
 */
class FollowingScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var followingLoading: String
    private lateinit var followingErrorHeader: String
    private lateinit var followingTopicCardIcon: String
    private lateinit var followingTopicCardFollowButton: String
    private lateinit var followingTopicCardUnfollowButton: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            followingLoading = getString(R.string.following_loading)
            followingErrorHeader = getString(R.string.following_error_header)
            followingTopicCardIcon = getString(R.string.following_topic_card_icon_content_desc)
            followingTopicCardFollowButton =
                getString(R.string.following_topic_card_follow_button_content_desc)
            followingTopicCardUnfollowButton =
                getString(R.string.following_topic_card_unfollow_button_content_desc)
        }
    }

    @Test
    fun niaLoadingIndicator_whenScreenIsLoading_showLoading() {
        composeTestRule.setContent {
            FollowingScreen(
                uiState = FollowingUiState.Loading,
                followTopic = { _, _ -> },
                navigateToTopic = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(followingLoading)
            .assertExists()
    }

    @Test
    fun followingWithTopics_whenTopicsFollowed_showFollowedAndUnfollowedTopicsWithInfo() {
        composeTestRule.setContent {
            FollowingScreen(
                uiState = FollowingUiState.Topics(topics = testTopics),
                followTopic = { _, _ -> },
                navigateToTopic = {}
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
            .onAllNodesWithContentDescription(followingTopicCardIcon)
            .assertCountEquals(testTopics.count())

        composeTestRule
            .onAllNodesWithContentDescription(followingTopicCardFollowButton)
            .assertCountEquals(numberOfUnfollowedTopics)

        composeTestRule
            .onAllNodesWithContentDescription(followingTopicCardUnfollowButton)
            .assertCountEquals(testTopics.filter { it.isFollowed }.size)
    }

    @Test
    fun followingError_whenErrorOccurs_thenShowEmptyErrorScreen() {
        composeTestRule.setContent {
            FollowingScreen(
                uiState = FollowingUiState.Error,
                followTopic = { _, _ -> },
                navigateToTopic = {}
            )
        }

        composeTestRule
            .onNodeWithText(followingErrorHeader)
            .assertIsDisplayed()
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
            id = 0,
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
            id = 1,
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
            id = 2,
            name = TOPIC_3_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFollowed = false
    )
)

private val numberOfUnfollowedTopics = testTopics.filter { !it.isFollowed }.size
