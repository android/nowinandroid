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
import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.feature.topic.navigation.TOPIC_ID_ARG
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 */
class TopicViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()
    private val topicsRepository = TestTopicsRepository()
    private val newsRepository = TestNewsRepository()
    private val userNewsResourceRepository = CompositeUserNewsResourceRepository(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository,
    )
    private lateinit var viewModel: TopicViewModel

    @Before
    fun setup() {
        viewModel = TopicViewModel(
            savedStateHandle = SavedStateHandle(mapOf(TOPIC_ID_ARG to testInputTopics[0].topic.id)),
            userDataRepository = userDataRepository,
            topicsRepository = topicsRepository,
            userNewsResourceRepository = userNewsResourceRepository,
        )
    }

    @Test
    fun topicId_matchesTopicIdFromSavedStateHandle() =
        assertEquals(testInputTopics[0].topic.id, viewModel.topicId)

    @Test
    fun uiStateTopic_whenSuccess_matchesTopicFromRepository() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

        topicsRepository.sendTopics(testInputTopics.map(FollowableTopic::topic))
        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
        val item = viewModel.topicUiState.value
        assertIs<TopicUiState.Success>(item)

        val topicFromRepository = topicsRepository.getTopic(
            testInputTopics[0].topic.id,
        ).first()

        assertEquals(topicFromRepository, item.followableTopic.topic)

        collectJob.cancel()
    }

    @Test
    fun uiStateNews_whenInitialized_thenShowLoading() = runTest {
        assertEquals(NewsUiState.Loading, viewModel.newsUiState.value)
    }

    @Test
    fun uiStateTopic_whenInitialized_thenShowLoading() = runTest {
        assertEquals(TopicUiState.Loading, viewModel.topicUiState.value)
    }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicLoading_thenShowLoading() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
        assertEquals(TopicUiState.Loading, viewModel.topicUiState.value)

        collectJob.cancel()
    }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicSuccess_thenTopicSuccessAndNewsLoading() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

            topicsRepository.sendTopics(testInputTopics.map { it.topic })
            userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
            val topicUiState = viewModel.topicUiState.value
            val newsUiState = viewModel.newsUiState.value

            assertIs<TopicUiState.Success>(topicUiState)
            assertIs<NewsUiState.Loading>(newsUiState)

            collectJob.cancel()
        }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicSuccessAndNewsIsSuccess_thenAllSuccess() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) {
                combine(
                    viewModel.topicUiState,
                    viewModel.newsUiState,
                    ::Pair,
                ).collect()
            }
            topicsRepository.sendTopics(testInputTopics.map { it.topic })
            userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
            newsRepository.sendNewsResources(sampleNewsResources)
            val topicUiState = viewModel.topicUiState.value
            val newsUiState = viewModel.newsUiState.value

            assertIs<TopicUiState.Success>(topicUiState)
            assertIs<NewsUiState.Success>(newsUiState)

            collectJob.cancel()
        }

    @Test
    fun uiStateTopic_whenFollowingTopic_thenShowUpdatedTopic() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

        topicsRepository.sendTopics(testInputTopics.map { it.topic })
        // Set which topic IDs are followed, not including 0.
        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))

        viewModel.followTopicToggle(true)

        assertEquals(
            TopicUiState.Success(followableTopic = testOutputTopics[0]),
            viewModel.topicUiState.value,
        )

        collectJob.cancel()
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
        isFollowed = true,
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
        isFollowed = false,
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
        isFollowed = false,
    ),
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
        isFollowed = true,
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
        isFollowed = true,
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
        isFollowed = false,
    ),
)

private val sampleNewsResources = listOf(
    NewsResource(
        id = "1",
        title = "Thanks for helping us reach 1M YouTube Subscribers",
        content = "Thank you everyone for following the Now in Android series and everything the " +
            "Android Developers YouTube channel has to offer. During the Android Developer " +
            "Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to " +
            "thank you all.",
        url = "https://youtu.be/-fJ6poHQrjM",
        headerImageUrl = "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
        type = "Video 📺",
        topics = listOf(
            Topic(
                id = "0",
                name = "Headlines",
                shortDescription = "",
                longDescription = "long description",
                url = "URL",
                imageUrl = "image URL",
            ),
        ),
    ),
)
