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
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.feature.interests.InterestsUiState
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

    @Test
    fun niaLoadingWheel_inTopics_whenScreenIsLoading_showLoading() {
        launchBookmarksRobot(
            composeTestRule,
            InterestsUiState.Loading,
            0
        ) {
            interestsLoadingExists()
        }
    }

    @Test
    fun niaLoadingWheel_inAuthors_whenScreenIsLoading_showLoading() {
        launchBookmarksRobot(
            composeTestRule,
            InterestsUiState.Loading,
            1
        ) {
            interestsLoadingExists()
        }
    }

    @Test
    fun interestsWithTopics_whenTopicsFollowed_showFollowedAndUnfollowedTopicsWithInfo() {
        launchBookmarksRobot(
            composeTestRule,
            InterestsUiState.Interests(topics = testTopics, authors = listOf()),
            0
        ) {
            nodeWithTextDisplayed(TOPIC_1_NAME)
            nodeWithTextDisplayed(TOPIC_2_NAME)
            nodeWithTextDisplayed(TOPIC_3_NAME)

            nodesWithTextCountEquals(TOPIC_SHORT_DESC, testTopics.count())

            interestsTopicCardFollowButtonCountEquals(numberOfUnfollowedTopics)
            interestsTopicCardUnfollowButtonCountEquals(numberOfFollowedTopics)
        }
    }

    @Test
    fun interestsWithTopics_whenAuthorsFollowed_showFollowedAndUnfollowedTopicsWithInfo() {
        launchBookmarksRobot(
            composeTestRule,
            InterestsUiState.Interests(topics = listOf(), authors = testAuthors),
            1
        ) {
            nodeWithTextDisplayed("Android Dev")
            nodeWithTextDisplayed("Android Dev 2")
            nodeWithTextDisplayed("Android Dev 3")

            interestsTopicCardFollowButtonCountEquals(numberOfUnfollowedAuthors)
            interestsTopicCardUnfollowButtonCountEquals(numberOfFollowedAuthors)
        }
    }

    @Test
    fun topicsEmpty_whenDataIsEmptyOccurs_thenShowEmptyScreen() {
        launchBookmarksRobot(
            composeTestRule,
            InterestsUiState.Empty,
            0
        ) {
            interestsEmptyHeaderDisplayed()
        }
    }

    @Test
    fun authorsEmpty_whenDataIsEmptyOccurs_thenShowEmptyScreen() {
        launchBookmarksRobot(
            composeTestRule,
            InterestsUiState.Empty,
            1
        ) {
            interestsEmptyHeaderDisplayed()
        }
    }
}

private fun launchBookmarksRobot(
    composeTestRule: AndroidComposeTestRule<
        ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    uiState: InterestsUiState,
    tabIndex: Int,
    func: InterestsRobot.() -> Unit
) = InterestsRobot(composeTestRule).apply {
    setContent(uiState, tabIndex)
    func()
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
private val numberOfFollowedTopics = testTopics.filter { it.isFollowed }.size

private val numberOfUnfollowedAuthors = testAuthors.filter { !it.isFollowed }.size
private val numberOfFollowedAuthors = testAuthors.filter { it.isFollowed }.size
