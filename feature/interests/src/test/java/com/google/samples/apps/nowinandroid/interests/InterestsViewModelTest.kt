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

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.domain.GetFollowableTopicsUseCase
import com.google.samples.apps.nowinandroid.core.testing.data.followableTopicTestData
import com.google.samples.apps.nowinandroid.core.testing.data.newsResourcesTestData
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.feature.interests.InterestsUiState
import com.google.samples.apps.nowinandroid.feature.interests.InterestsViewModel
import com.google.samples.apps.nowinandroid.feature.interests.TopicUiState
import com.google.samples.apps.nowinandroid.feature.interests.navigation.topicIdArg
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 */
class InterestsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()
    private val newsRepository = TestNewsRepository()
    private val userNewsResourceRepository = CompositeUserNewsResourceRepository(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository,
    )
    private val topicsRepository = TestTopicsRepository()
    private val getFollowableTopicsUseCase = GetFollowableTopicsUseCase(
        topicsRepository = topicsRepository,
        userDataRepository = userDataRepository,
    )
    private val selectedTopidId: String = testInputTopics[0].topic.id
    private lateinit var viewModel: InterestsViewModel

    @Before
    fun setup() {
        viewModel = InterestsViewModel(
            savedStateHandle = SavedStateHandle(mapOf(topicIdArg to selectedTopidId)),
            userDataRepository = userDataRepository,
            getFollowableTopics = getFollowableTopicsUseCase,
            topicsRepository = topicsRepository,
            userNewsResourceRepository = userNewsResourceRepository,
        )
    }

    @Test
    fun uiState_whenInitialized_thenShowLoading() = runTest {
        assertEquals(InterestsUiState.Loading, viewModel.interestUiState.value)
    }

    @Test
    fun uiState_whenFollowedTopicsAreLoading_thenShowLoading() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.interestUiState.collect() }

        userDataRepository.setFollowedTopicIds(emptySet())
        assertEquals(InterestsUiState.Loading, viewModel.interestUiState.value)

        collectJob.cancel()
    }

    @Test
    fun uiState_whenFollowingNewTopic_thenShowUpdatedTopics() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.interestUiState.collect() }

        val toggleTopicId = testOutputTopics[1].topic.id
        topicsRepository.sendTopics(testInputTopics.map { it.topic })
        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[0].topic.id))

        assertEquals(
            false,
            (viewModel.interestUiState.value as InterestsUiState.Interests)
                .topics.first { it.topic.id == toggleTopicId }.isFollowed,
        )

        viewModel.followTopic(
            followedTopicId = toggleTopicId,
            true,
        )

        assertEquals(
            InterestsUiState.Interests(topics = testOutputTopics, selectedTopicId = selectedTopidId),
            viewModel.interestUiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun uiState_whenUnfollowingTopics_thenShowUpdatedTopics() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.interestUiState.collect() }

        val toggleTopicId = testOutputTopics[1].topic.id

        topicsRepository.sendTopics(testOutputTopics.map { it.topic })
        userDataRepository.setFollowedTopicIds(
            setOf(testOutputTopics[0].topic.id, testOutputTopics[1].topic.id),
        )

        assertEquals(
            true,
            (viewModel.interestUiState.value as InterestsUiState.Interests)
                .topics.first { it.topic.id == toggleTopicId }.isFollowed,
        )

        viewModel.followTopic(
            followedTopicId = toggleTopicId,
            false,
        )

        assertEquals(
            InterestsUiState.Interests(topics = testInputTopics, selectedTopicId = selectedTopidId),
            viewModel.interestUiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun uiStateTopic_whenSuccess_matchesTopicFromRepository() = runTest {
        topicsRepository.sendTopics(testInputTopics.map { it.topic })
        userDataRepository.setFollowedTopicIds(setOf(followableTopicTestData[1].topic.id))
        newsRepository.sendNewsResources(newsResourcesTestData)

        runBlocking(UnconfinedTestDispatcher()) {
            viewModel.topicUiState.test {
                assertEquals(null, awaitItem())
                assertIs<TopicUiState.Loading>(awaitItem())
                assertIs<TopicUiState.Success>(awaitItem())

                val item = viewModel.topicUiState.value
                assertIs<TopicUiState.Success>(item)

                val topicFromRepository = topicsRepository.getTopic(
                    testInputTopics[0].topic.id,
                ).first()

                assertEquals(topicFromRepository, item.followableTopic.topic)
            }
        }
    }

    @Test
    fun uiStateTopic_whenFollowedIdsSuccessAndTopicLoading_thenShowLoading() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.topicUiState.collect() }

        userDataRepository.setFollowedTopicIds(setOf(testInputTopics[1].topic.id))
        assertEquals(TopicUiState.Loading, viewModel.topicUiState.value)

        collectJob.cancel()
    }
}
