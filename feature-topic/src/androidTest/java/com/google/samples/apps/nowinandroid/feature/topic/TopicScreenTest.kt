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

package com.google.samples.apps.nowinandroid.feature.topic

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Topic screen;
 * Verifies that, when a specific UiState is set, the corresponding
 * composables and details are shown
 */
class TopicScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var topicLoading: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            topicLoading = getString(R.string.topic_loading)
        }
    }

    @Test
    fun niaLoadingIndicator_whenScreenIsLoading_showLoading() {
        composeTestRule.setContent {
            TopicScreen(
                topicState = TopicUiState.Loading,
                newsState = NewsUiState.Loading,
                onBackClick = { },
                onFollowClick = { }
            )
        }

        composeTestRule
            .onNodeWithContentDescription(topicLoading)
            .assertExists()
    }

    @Test
    fun topicTitle_whenTopicIsSuccess_isShown() {
        val testTopic = testTopics.first()
        composeTestRule.setContent {
            TopicScreen(
                topicState = TopicUiState.Success(testTopic),
                newsState = NewsUiState.Loading,
                onBackClick = { },
                onFollowClick = { }
            )
        }

        // Name is shown
        composeTestRule
            .onNodeWithText(testTopic.topic.name)
            .assertExists()

        // Description is shown
        composeTestRule
            .onNodeWithText(testTopic.topic.longDescription)
            .assertExists()
    }

    @Test
    fun news_whenTopicIsLoading_isNotShown() {
        val testTopic = testTopics.first()
        composeTestRule.setContent {
            TopicScreen(
                topicState = TopicUiState.Loading,
                newsState = NewsUiState.Success(sampleNewsResources),
                onBackClick = { },
                onFollowClick = { }
            )
        }

        // Loading indicator shown
        composeTestRule
            .onNodeWithContentDescription(topicLoading)
            .assertExists()
    }
    @Test
    fun news_whenSuccessAndTopicIsSuccess_isShown() {
        val testTopic = testTopics.first()
        composeTestRule.setContent {
            TopicScreen(
                topicState = TopicUiState.Success(testTopic),
                newsState = NewsUiState.Success(sampleNewsResources),
                onBackClick = { },
                onFollowClick = { }
            )
        }

        // First news title shown
        composeTestRule
            .onNodeWithText(sampleNewsResources.first().title)
            .assertExists()
    }
}

private const val TOPIC_1_NAME = "Headlines"
private const val TOPIC_2_NAME = "UI"
private const val TOPIC_3_NAME = "Tools"
private const val TOPIC_DESC = "At vero eos et accusamus et iusto odio dignissimos ducimus qui."

private val testTopics = listOf(
    FollowableTopic(
        Topic(
            id = 0,
            name = TOPIC_1_NAME,
            longDescription = TOPIC_DESC,
            shortDescription = "",
            imageUrl = "",
            url = ""
        ),
        isFollowed = true
    ),
    FollowableTopic(
        Topic(
            id = 1,
            name = TOPIC_2_NAME,
            longDescription = TOPIC_DESC,
            shortDescription = "",
            imageUrl = "",
            url = ""
        ),
        isFollowed = false
    ),
    FollowableTopic(
        Topic(
            id = 2,
            name = TOPIC_3_NAME,
            longDescription = TOPIC_DESC,
            shortDescription = "",
            imageUrl = "",
            url = ""
        ),
        isFollowed = false
    )
)

private val numberOfUnfollowedTopics = testTopics.filter { !it.isFollowed }.size

private val sampleNewsResources = listOf(
    NewsResource(
        id = 1,
        episodeId = 52,
        title = "Thanks for helping us reach 1M YouTube Subscribers",
        content = "Thank you everyone for following the Now in Android series and everything the " +
            "Android Developers YouTube channel has to offer. During the Android Developer " +
            "Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to " +
            "thank you all.",
        url = "https://youtu.be/-fJ6poHQrjM",
        headerImageUrl = "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
        type = Video,
        topics = listOf(
            Topic(
                id = 0,
                name = "Headlines",
                shortDescription = "",
                longDescription = "",
                imageUrl = "",
                url = ""
            )
        ),
        authors = emptyList()
    )
)
