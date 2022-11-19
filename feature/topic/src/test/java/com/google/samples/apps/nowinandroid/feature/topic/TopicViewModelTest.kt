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
import com.google.samples.apps.nowinandroid.core.domain.GetSaveableNewsResourcesStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.nextFakeNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.nextFakeTopic
import com.google.samples.apps.nowinandroid.core.testing.decoder.FakeStringDecoder
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.feature.topic.navigation.topicIdArg
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    private val getSaveableNewsResourcesStreamUseCase = GetSaveableNewsResourcesStreamUseCase(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository
    )
    private lateinit var viewModel: TopicViewModel

    @Before
    fun setup() {
        viewModel = TopicViewModel(
            savedStateHandle = SavedStateHandle(mapOf(topicIdArg to testInputTopics[0].topic.id)),
            stringDecoder = FakeStringDecoder(),
            userDataRepository = userDataRepository,
            topicsRepository = topicsRepository,
            getSaveableNewsResourcesStream = getSaveableNewsResourcesStreamUseCase
        )
    }

    @Test
    fun uiStateTopic_whenSuccess_matchesTopicFromRepository() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

        topicsRepository.sendTopics(testInputTopics.map(FollowableTopic::topic))
        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
        val item = viewModel.topicUiState.value
        assertIs<TopicUiState.Success>(item)

        val topicFromRepository = topicsRepository.getTopic(
            testInputTopics[0].topic.id
        ).first()

        assertEquals(topicFromRepository, item.followableTopic.topic)

        collectJob.cancel()
    }

    @Test
    fun uiStateNews_whenInitialized_thenShowLoading() = runTest {
        assertEquals(NewsUiState.Loading, viewModel.newUiState.value)
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
            val newsUiState = viewModel.newUiState.value

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
                    viewModel.newUiState,
                    ::Pair
                ).collect()
            }
            topicsRepository.sendTopics(testInputTopics.map { it.topic })
            userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
            newsRepository.sendNewsResources(sampleNewsResources)
            val topicUiState = viewModel.topicUiState.value
            val newsUiState = viewModel.newUiState.value

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
            viewModel.topicUiState.value
        )

        collectJob.cancel()
    }
}

private val testInputTopics = listOf(
    FollowableTopic(Random.nextFakeTopic(id = "1", name = "Android Studio"), isFollowed = true),
    FollowableTopic(Random.nextFakeTopic(id = "2", name = "Build"), isFollowed = false),
    FollowableTopic(Random.nextFakeTopic(id = "3", name = "Compose"), isFollowed = false),
)

private val testOutputTopics = listOf(
    testInputTopics[0],
    testInputTopics[1].copy(isFollowed = true),
    testInputTopics[2],
)

private val sampleNewsResources = listOf(
    Random.nextFakeNewsResource(id = "1", topics = listOf(testInputTopics[0].topic)),
)
