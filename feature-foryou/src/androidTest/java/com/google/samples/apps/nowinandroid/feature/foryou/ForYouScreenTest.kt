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

package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import org.junit.Rule
import org.junit.Test

class ForYouScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val doneButtonMatcher by lazy {
        hasText(
            composeTestRule.activity.resources.getString(R.string.done)
        )
    }

    @Test
    fun circularProgressIndicator_whenScreenIsLoading_exists() {
        composeTestRule.setContent {
            ForYouScreen(
                uiState = ForYouFeedUiState.Loading,
                onAuthorCheckedChanged = { _, _ -> },
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.for_you_loading)
            )
            .assertExists()
    }

    @Test
    fun topicSelector_whenNoTopicsSelected_showsTopicChipsAndDisabledDoneButton() {
        composeTestRule.setContent {
            ForYouScreen(
                uiState = ForYouFeedUiState.PopulatedFeed.FeedWithInterestsSelection(
                    topics = listOf(
                        FollowableTopic(
                            topic = Topic(
                                id = 0,
                                name = "Headlines",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                        FollowableTopic(
                            topic = Topic(
                                id = 1,
                                name = "UI",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                        FollowableTopic(
                            topic = Topic(
                                id = 2,
                                name = "Tools",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                    ),
                    authors = listOf(
                        FollowableAuthor(
                            author = Author(
                                id = 0,
                                name = "Android Dev",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = ""
                            ),
                            isFollowed = false
                        ),
                        FollowableAuthor(
                            author = Author(
                                id = 1,
                                name = "Android Dev 2",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = ""
                            ),
                            isFollowed = false
                        ),
                    ),
                    feed = emptyList()
                ),
                onAuthorCheckedChanged = { _, _ -> },
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithText("Headlines")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("UI")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Tools")
            .assertIsDisplayed()
            .assertHasClickAction()

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()
    }

    @Test
    fun topicSelector_whenSomeTopicsSelected_showsTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            ForYouScreen(
                uiState = ForYouFeedUiState.PopulatedFeed.FeedWithInterestsSelection(
                    topics = listOf(
                        FollowableTopic(
                            topic = Topic(
                                id = 0,
                                name = "Headlines",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                        FollowableTopic(
                            topic = Topic(
                                id = 1,
                                name = "UI",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = true
                        ),
                        FollowableTopic(
                            topic = Topic(
                                id = 2,
                                name = "Tools",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                    ),
                    authors = listOf(
                        FollowableAuthor(
                            author = Author(
                                id = 0,
                                name = "Android Dev",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = ""
                            ),
                            isFollowed = false
                        ),
                        FollowableAuthor(
                            author = Author(
                                id = 1,
                                name = "Android Dev 2",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = ""
                            ),
                            isFollowed = false
                        ),
                    ),
                    feed = emptyList()
                ),
                onAuthorCheckedChanged = { _, _ -> },
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithText("Headlines")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("UI")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Tools")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Android Dev")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun topicSelector_whenSomeAuthorsSelected_showsTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            ForYouScreen(
                uiState = ForYouFeedUiState.PopulatedFeed.FeedWithInterestsSelection(
                    topics = listOf(
                        FollowableTopic(
                            topic = Topic(
                                id = 0,
                                name = "Headlines",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                        FollowableTopic(
                            topic = Topic(
                                id = 1,
                                name = "UI",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                        FollowableTopic(
                            topic = Topic(
                                id = 2,
                                name = "Tools",
                                shortDescription = "",
                                longDescription = "",
                                url = "",
                                imageUrl = ""
                            ),
                            isFollowed = false
                        ),
                    ),
                    authors = listOf(
                        FollowableAuthor(
                            author = Author(
                                id = 0,
                                name = "Android Dev",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = ""
                            ),
                            isFollowed = true
                        ),
                        FollowableAuthor(
                            author = Author(
                                id = 1,
                                name = "Android Dev 2",
                                imageUrl = "",
                                twitter = "",
                                mediumPage = ""
                            ),
                            isFollowed = false
                        ),
                    ),
                    feed = emptyList()
                ),
                onAuthorCheckedChanged = { _, _ -> },
                onTopicCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithText("Headlines")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("UI")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Tools")
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Android Dev")
            .assertIsDisplayed()
            .assertIsOn()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText("Android Dev 2")
            .assertIsDisplayed()
            .assertIsOff()
            .assertHasClickAction()

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }
}
