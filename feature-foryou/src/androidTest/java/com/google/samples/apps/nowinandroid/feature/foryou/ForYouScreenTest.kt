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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
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
import androidx.compose.ui.unit.DpSize
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import kotlinx.datetime.Instant
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState = ForYouInterestsSelectionUiState.Loading,
                    feedState = NewsFeedUiState.Loading,
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
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
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState =
                    ForYouInterestsSelectionUiState.WithInterestsSelection(
                        topics = testTopics,
                        authors = testAuthors
                    ),
                    feedState = NewsFeedUiState.Success(
                        feed = emptyList()
                    ),
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        testAuthors.forEach { testAuthor ->
            composeTestRule
                .onNodeWithText(testAuthor.author.name)
                .assertIsDisplayed()
                .assertHasClickAction()
        }

        testTopics.forEach { testTopic ->
            composeTestRule
                .onNodeWithText(testTopic.topic.name)
                .assertIsDisplayed()
                .assertHasClickAction()
        }

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
    fun topicSelector_whenSomeTopicsSelected_showsAuthorAndTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
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
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        testAuthors.forEach { testAuthor ->
            composeTestRule
                .onNodeWithText(testAuthor.author.name)
                .assertIsDisplayed()
                .assertHasClickAction()
        }

        testTopics.forEach { testTopic ->
            composeTestRule
                .onNodeWithText(testTopic.topic.name)
                .assertIsDisplayed()
                .assertHasClickAction()
        }

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
    fun topicSelector_whenSomeAuthorsSelected_showsAuthorAndTopicChipsAndEnabledDoneButton() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
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
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        testAuthors.forEach { testAuthor ->
            composeTestRule
                .onNodeWithText(testAuthor.author.name)
                .assertIsDisplayed()
                .assertHasClickAction()
        }

        testTopics.forEach { testTopic ->
            composeTestRule
                .onNodeWithText(testTopic.topic.name)
                .assertIsDisplayed()
                .assertHasClickAction()
        }

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
    fun feed_whenInterestsSelectedAndLoading_showsLoadingIndicator() {
        composeTestRule.setContent {
            BoxWithConstraints {
                ForYouScreen(
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState =
                    ForYouInterestsSelectionUiState.WithInterestsSelection(
                        topics = testTopics,
                        authors = testAuthors
                    ),
                    feedState = NewsFeedUiState.Loading,
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
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
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(maxWidth, maxHeight)
                    ),
                    interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
                    feedState = NewsFeedUiState.Loading,
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
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
        lateinit var windowSizeClass: WindowSizeClass

        composeTestRule.setContent {
            BoxWithConstraints {
                windowSizeClass = WindowSizeClass.calculateFromSize(
                    DpSize(maxWidth, maxHeight)
                )

                ForYouScreen(
                    windowSizeClass = windowSizeClass,
                    interestsSelectionState = ForYouInterestsSelectionUiState.NoInterestsSelection,
                    feedState = NewsFeedUiState.Success(
                        feed = testNewsResources
                    ),
                    onAuthorCheckedChanged = { _, _ -> },
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> }
                )
            }
        }

        val firstFeedItem = composeTestRule
            .onNodeWithText(
                testNewsResources[0].newsResource.title,
                substring = true
            )
            .assertHasClickAction()
            .fetchSemanticsNode()

        val secondFeedItem = composeTestRule
            .onNodeWithText(
                testNewsResources[1].newsResource.title,
                substring = true
            )
            .assertHasClickAction()
            .fetchSemanticsNode()

        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
                // On smaller screen widths, the second feed item should be below the first because
                // they are displayed in a single column
                Assert.assertTrue(
                    firstFeedItem.positionInRoot.y < secondFeedItem.positionInRoot.y
                )
            }
            else -> {
                // On larger screen widths, the second feed item should be inline with the first
                // because they are displayed in more than one column
                Assert.assertTrue(
                    firstFeedItem.positionInRoot.y == secondFeedItem.positionInRoot.y
                )
            }
        }
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
private val testNewsResources = listOf(
    SaveableNewsResource(
        newsResource = NewsResource(
            id = "1",
            episodeId = "52",
            title = "Small Title",
            content = "small.",
            url = "https://youtu.be/-fJ6poHQrjM",
            headerImageUrl = null,
            publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
            type = Video,
            topics = emptyList(),
            authors = emptyList()
        ),
        isSaved = false
    ),
    SaveableNewsResource(
        newsResource = NewsResource(
            id = "2",
            episodeId = "52",
            title = "Transformations and customisations in the Paging Library",
            content = "A demonstration of different operations that can be performed " +
                "with Paging. Transformations like inserting separators, when to " +
                "create a new pager, and customisation options for consuming " +
                "PagingData.",
            url = "https://youtu.be/ZARz0pjm5YM",
            headerImageUrl = "https://i.ytimg.com/vi/ZARz0pjm5YM/maxresdefault.jpg",
            publishDate = Instant.parse("2021-11-01T00:00:00.000Z"),
            type = Video,
            topics = listOf(
                Topic(
                    id = "1",
                    name = "UI",
                    shortDescription = "",
                    longDescription = "",
                    url = "",
                    imageUrl = ""
                ),
            ),
            authors = emptyList()
        ),
        isSaved = false
    ),
    SaveableNewsResource(
        newsResource = NewsResource(
            id = "3",
            episodeId = "52",
            title = "Community tip on Paging",
            content = "Tips for using the Paging library from the developer community",
            url = "https://youtu.be/r5JgIyS3t3s",
            headerImageUrl = "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
            publishDate = Instant.parse("2021-11-08T00:00:00.000Z"),
            type = Video,
            topics = listOf(
                Topic(
                    id = "1",
                    name = "UI",
                    shortDescription = "",
                    longDescription = "",
                    url = "",
                    imageUrl = ""
                ),
            ),
            authors = emptyList()
        ),
        isSaved = false
    ),
)
