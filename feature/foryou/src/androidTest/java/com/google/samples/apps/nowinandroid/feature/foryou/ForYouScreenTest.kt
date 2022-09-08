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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
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
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
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
            BoxWithConstraints {
                ForYouScreen(
                    interestsSelectionState = ForYouInterestsSelectionUiState.Loading,
                    feedState = NewsFeedUiState.Loading,
                    onTopicCheckedChanged = { _, _ -> },
                    onAuthorCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.for_you_loading)
            )
            .assertExists()
    }

    @Test
    fun topicSelector_whenNoTopicsSelected_showsAuthorAndTopicChipsAndDisabledDoneButton() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    interestsSelectionState =
                    ForYouInterestsSelectionUiState.WithInterestsSelection(
                        topics = testTopics,
                        authors = testAuthors
                    ),
                    feedState = NewsFeedUiState.Success(
                        feed = emptyList()
                    ),
                    onTopicCheckedChanged = { _, _ -> },
                    onAuthorCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        testAuthors.forEach { testAuthor ->
            composeTestRule
                .onNodeWithText(testAuthor.author.name)
                .assertExists()
                .assertHasClickAction()
        }

        testTopics.forEach { testTopic ->
            composeTestRule
                .onNodeWithText(testTopic.topic.name)
                .assertExists()
                .assertHasClickAction()
        }

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertExists()
            .assertIsNotEnabled()
            .assertHasClickAction()
    }

    @Test
    fun topicSelector_whenSomeTopicsSelected_showsAuthorAndTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    interestsSelectionState =
                    ForYouInterestsSelectionUiState.WithInterestsSelection(
                        // Follow one topic
                        topics = testTopics.mapIndexed { index, testTopic ->
                            testTopic.copy(isFollowed = index == 1)
                        },
                        authors = testAuthors
                    ),
                    feedState = NewsFeedUiState.Success(
                        feed = emptyList()
                    ),
                    onTopicCheckedChanged = { _, _ -> },
                    onAuthorCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        testAuthors.forEach { testAuthor ->
            composeTestRule
                .onNodeWithText(testAuthor.author.name)
                .assertExists()
                .assertHasClickAction()
        }

        testTopics.forEach { testTopic ->
            composeTestRule
                .onNodeWithText(testTopic.topic.name)
                .assertExists()
                .assertHasClickAction()
        }

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertExists()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun topicSelector_whenSomeAuthorsSelected_showsAuthorAndTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    interestsSelectionState =
                    ForYouInterestsSelectionUiState.WithInterestsSelection(
                        // Follow one topic
                        topics = testTopics,
                        authors = testAuthors.mapIndexed { index, testAuthor ->
                            testAuthor.copy(isFollowed = index == 1)
                        }
                    ),
                    feedState = NewsFeedUiState.Success(
                        feed = emptyList()
                    ),
                    onTopicCheckedChanged = { _, _ -> },
                    onAuthorCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        testAuthors.forEach { testAuthor ->
            composeTestRule
                .onNodeWithText(testAuthor.author.name)
                .assertExists()
                .assertHasClickAction()
        }

        testTopics.forEach { testTopic ->
            composeTestRule
                .onNodeWithText(testTopic.topic.name)
                .assertExists()
                .assertHasClickAction()
        }

        // Scroll until the Done button is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(doneButtonMatcher)

        composeTestRule
            .onNode(doneButtonMatcher)
            .assertExists()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun feed_whenInterestsSelectedAndLoading_showsLoadingIndicator() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    interestsSelectionState =
                    ForYouInterestsSelectionUiState.WithInterestsSelection(
                        topics = testTopics,
                        authors = testAuthors
                    ),
                    feedState = NewsFeedUiState.Loading,
                    onTopicCheckedChanged = { _, _ -> },
                    onAuthorCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        // Scroll until the loading indicator is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(
                hasContentDescription(
                    composeTestRule.activity.resources.getString(R.string.for_you_loading)
                )
            )

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.for_you_loading)
            )
            .assertExists()
    }

    @Test
    fun feed_whenNoInterestsSelectionAndLoading_showsLoadingIndicator() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
                    feedState = NewsFeedUiState.Loading,
                    onTopicCheckedChanged = { _, _ -> },
                    onAuthorCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        // Scroll until the loading indicator is visible
        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(
                hasContentDescription(
                    composeTestRule.activity.resources.getString(R.string.for_you_loading)
                )
            )

        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.resources.getString(R.string.for_you_loading)
            )
            .assertExists()
    }

    @Test
    fun feed_whenNoInterestsSelectionAndLoaded_showsFeed() {
        composeTestRule.setContent {
            ForYouScreen(
                interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
                feedState = NewsFeedUiState.Success(
                    feed = previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                onTopicCheckedChanged = { _, _ -> },
                onAuthorCheckedChanged = { _, _ -> },
                saveFollowedTopics = {},
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithText(
                previewNewsResources[0].title,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(
                hasText(
                    previewNewsResources[1].title,
                    substring = true
                )
            )

        composeTestRule
            .onNodeWithText(
                previewNewsResources[1].title,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }
}

private val testTopic = Topic(
    id = "",
    name = "",
    shortDescription = "",
    longDescription = "",
    url = "",
    imageUrl = ""
)
private val testAuthor = Author(
    id = "",
    name = "",
    imageUrl = "",
    twitter = "",
    mediumPage = "",
    bio = ""
)
private val testTopics = listOf(
    FollowableTopic(
        topic = testTopic.copy(id = "0", name = "Headlines"),
        isFollowed = false
    ),
    FollowableTopic(
        topic = testTopic.copy(id = "1", name = "UI"),
        isFollowed = false
    ),
    FollowableTopic(
        topic = testTopic.copy(id = "2", name = "Tools"),
        isFollowed = false
    ),
)
private val testAuthors = listOf(
    FollowableAuthor(
        author = testAuthor.copy(id = "0", name = "Android Dev"),
        isFollowed = false
    ),
    FollowableAuthor(
        author = testAuthor.copy(id = "1", name = "Android Dev 2"),
        isFollowed = false
    ),
)
