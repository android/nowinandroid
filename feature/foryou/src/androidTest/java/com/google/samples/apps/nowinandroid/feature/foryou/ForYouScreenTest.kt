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
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import org.junit.Rule
import org.junit.Test

class ForYouScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun circularProgressIndicator_whenScreenIsLoading_exists() {
        launchForYouRobot(
            composeTestRule = composeTestRule,
            isSyncing = false,
            interestsSelectionState = ForYouInterestsSelectionUiState.Loading,
            feedState = NewsFeedUiState.Loading,
        ) {
            loadingIndicatorExists()
        }
    }

    @Test
    fun circularProgressIndicator_whenScreenIsSyncing_exists() {
        launchForYouRobot(
            composeTestRule = composeTestRule,
            isSyncing = true,
            interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
            feedState = NewsFeedUiState.Success(emptyList()),
        ) {
            loadingIndicatorExists()
        }
    }

    @Test
    fun topicSelector_whenNoTopicsSelected_showsAuthorAndTopicChipsAndDisabledDoneButton() {
        launchForYouRobot(
            composeTestRule = composeTestRule,
            isSyncing = false,
            interestsSelectionState = ForYouInterestsSelectionUiState.WithInterestsSelection(
                topics = testTopics,
                authors = testAuthors
            ),
            feedState = NewsFeedUiState.Success(emptyList()),
        ) {
            testAuthors.forEach { author ->
                clickableAuthorExists(author)
            }

            testTopics.forEach { topic ->
                clickableTopicExists(topic)
            }

            scrollToDoneButton()
            clickableDoneButtonExists(false)
        }
    }

    @Test
    fun topicSelector_whenSomeTopicsSelected_showsAuthorAndTopicChipsAndEnabledDoneButton() {
        launchForYouRobot(
            composeTestRule = composeTestRule,
            isSyncing = false,
            interestsSelectionState = ForYouInterestsSelectionUiState.WithInterestsSelection(
                // Follow one topic
                topics = testTopics.mapIndexed { index, testTopic ->
                    testTopic.copy(isFollowed = index == 1)
                },
                authors = testAuthors
            ),
            feedState = NewsFeedUiState.Success(emptyList()),
        ) {
            testAuthors.forEach { author ->
                clickableAuthorExists(author)
            }

            testTopics.forEach { topic ->
                clickableTopicExists(topic)
            }

            scrollToDoneButton()
            clickableDoneButtonExists(true)
        }
    }

    @Test
    fun topicSelector_whenSomeAuthorsSelected_showsAuthorAndTopicChipsAndEnabledDoneButton() {
        launchForYouRobot(
            composeTestRule = composeTestRule,
            isSyncing = false,
            interestsSelectionState = ForYouInterestsSelectionUiState.WithInterestsSelection(
                // Follow one topic
                topics = testTopics,
                authors = testAuthors.mapIndexed { index, testAuthor ->
                    testAuthor.copy(isFollowed = index == 1)
                }
            ),
            feedState = NewsFeedUiState.Success(emptyList()),
        ) {
            testAuthors.forEach { author ->
                clickableAuthorExists(author)
            }

            testTopics.forEach { topic ->
                clickableTopicExists(topic)
            }

            scrollToDoneButton()
            clickableDoneButtonExists(true)
        }
    }

    @Test
    fun feed_whenInterestsSelectedAndLoading_showsLoadingIndicator() {
        launchForYouRobot(
            composeTestRule = composeTestRule,
            isSyncing = false,
            interestsSelectionState = ForYouInterestsSelectionUiState.WithInterestsSelection(
                topics = testTopics,
                authors = testAuthors
            ),
            feedState = NewsFeedUiState.Loading,
        ) {
            loadingIndicatorExists()
        }
    }

    @Test
    fun feed_whenNoInterestsSelectionAndLoading_showsLoadingIndicator() {
        launchForYouRobot(
            composeTestRule = composeTestRule,
            isSyncing = false,
            interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
            feedState = NewsFeedUiState.Loading,
        ) {
            loadingIndicatorExists()
        }
    }

    @Test
    fun feed_whenNoInterestsSelectionAndLoaded_showsFeed() {
        launchForYouRobot(
            composeTestRule = composeTestRule,
            isSyncing = false,
            interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
            feedState = NewsFeedUiState.Success(
                feed = previewNewsResources.map {
                    SaveableNewsResource(it, false)
                }
            ),
        ) {
            clickableNewsResourceExists(previewNewsResources[0])
            scrollToNewsResource(previewNewsResources[1])
            clickableNewsResourceExists(previewNewsResources[1])
        }
    }
}

private fun launchForYouRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    isSyncing: Boolean,
    interestsSelectionState: ForYouInterestsSelectionUiState,
    feedState: NewsFeedUiState,
    func: ForYouRobot.() -> Unit
) = ForYouRobot(composeTestRule).apply {
    setContent(isSyncing, interestsSelectionState, feedState)
    func()
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
