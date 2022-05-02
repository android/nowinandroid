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

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.util.TestDispatcherRule
import com.google.samples.apps.nowinandroid.feature.topic.TopicDestinationsArgs.TOPIC_ID_ARG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopicViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val topicsRepository = TestTopicsRepository()
    private val newsRepository = TestNewsRepository()
    private lateinit var viewModel: TopicViewModel

    @Before
    fun setup() {
        viewModel = TopicViewModel(
            savedStateHandle = SavedStateHandle(mapOf(TOPIC_ID_ARG to testInputTopics[0].topic.id)),
            topicsRepository = topicsRepository,
            newsRepository = newsRepository
        )
    }

    @Test
    fun uiStateAuthor_whenSuccess_matchesTopicFromRepository() = runTest {
        viewModel.uiState.test {
            awaitItem()
            topicsRepository.sendTopics(testInputTopics.map(FollowableTopic::topic))
            topicsRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
            val item = awaitItem()
            assertTrue(item.topicState is TopicUiState.Success)

            val successTopicState = item.topicState as TopicUiState.Success
            val topicFromRepository = topicsRepository.getTopic(
                testInputTopics[0].topic.id
            ).first()

            assertEquals(topicFromRepository, successTopicState.followableTopic.topic)
            cancel()
        }
    }

    @Test
    fun uiStateNews_whenInitialized_thenShowLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(NewsUiState.Loading, awaitItem().newsState)
            cancel()
        }
    }

    @Test
    fun uiStateTopic_whenInitialized_thenShowLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(TopicUiState.Loading, awaitItem().topicState)
            cancel()
        }
    }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicLoading_thenShowLoading() = runTest {
        viewModel.uiState.test {
            topicsRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
            assertEquals(TopicUiState.Loading, awaitItem().topicState)
            cancel()
        }
    }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicSuccess_thenTopicSuccessAndNewsLoading() =
        runTest {
            viewModel.uiState.test {
                awaitItem()
                topicsRepository.sendTopics(testInputTopics.map { it.topic })
                topicsRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
                val item = awaitItem()
                assertTrue(item.topicState is TopicUiState.Success)
                assertTrue(item.newsState is NewsUiState.Loading)
                cancel()
            }
        }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicSuccessAndNewsIsSuccess_thenAllSuccess() =
        runTest {
            viewModel.uiState.test {
                awaitItem()
                topicsRepository.sendTopics(testInputTopics.map { it.topic })
                topicsRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
                newsRepository.sendNewsResources(sampleNewsResources)
                val item = awaitItem()
                assertTrue(item.topicState is TopicUiState.Success)
                assertTrue(item.newsState is NewsUiState.Success)
                cancel()
            }
        }

    @Test
    fun uiStateTopic_whenFollowingTopic_thenShowUpdatedTopic() = runTest {
        viewModel.uiState
            .test {
                awaitItem()
                topicsRepository.sendTopics(testInputTopics.map { it.topic })
                // Set which topic IDs are followed, not including 0.
                topicsRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))

                viewModel.followTopicToggle(true)

                assertEquals(
                    TopicUiState.Success(followableTopic = testOutputTopics[0]),
                    awaitItem().topicState
                )
                cancel()
            }
    }
}

private const val TOPIC_1_NAME = "Android Studio"
private const val TOPIC_2_NAME = "Build"
private const val TOPIC_3_NAME = "Compose"
private const val TOPIC_SHORT_DESC = "At vero eos et accusamus."
private const val TOPIC_LONG_DESC = "At vero eos et accusamus et iusto odio dignissimos ducimus."
private const val TOPIC_URL = "URL"
private const val TOPIC_IMAGE_URL = "Image URL"

private val testInputTopics = listOf(
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

private val testOutputTopics = listOf(
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
        isFollowed = true
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

private val sampleNewsResources = listOf(
    NewsResource(
        id = "1",
        episodeId = "52",
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
                id = "0",
                name = "Headlines",
                shortDescription = "",
                longDescription = "long description",
                url = "URL",
                imageUrl = "image URL",
            )
        ),
        authors = emptyList()
    )
)
