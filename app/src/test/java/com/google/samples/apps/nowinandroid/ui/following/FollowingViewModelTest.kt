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

package com.google.samples.apps.nowinandroid.ui.following

import app.cash.turbine.test
import com.google.samples.apps.nowinandroid.data.model.Topic
import com.google.samples.apps.nowinandroid.testutil.TestDispatcherRule
import com.google.samples.apps.nowinandroid.testutil.TestTopicsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FollowingViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val topicsRepository = TestTopicsRepository()
    private lateinit var viewModel: FollowingViewModel

    @Before
    fun setup() {
        viewModel = FollowingViewModel(topicsRepository = topicsRepository)
    }

    @Test
    fun uiState_whenInitialized_thenShowLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(FollowingUiState.Loading, awaitItem())
            cancel()
        }
    }

    @Test
    fun uiState_whenFollowedTopicsAreLoading_thenShowLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(FollowingUiState.Loading, awaitItem())
            topicsRepository.setFollowedTopicIds(emptySet())
            cancel()
        }
    }

    @Test
    fun uiState_whenFollowingNewTopic_thenShowUpdatedTopics() = runTest {
        viewModel.uiState
            .test {
                awaitItem()
                topicsRepository.sendTopics(testInputTopics)
                topicsRepository.setFollowedTopicIds(setOf(testInputTopics[0].id))

                awaitItem()
                viewModel.followTopic(
                    followedTopicId = testInputTopics[1].id,
                    followed = !testInputTopics[1].followed
                )

                assertEquals(
                    FollowingUiState.Topics(topics = testOutputTopics),
                    awaitItem()
                )
                cancel()
            }
    }

    @Test
    fun uiState_whenUnfollowingTopics_thenShowUpdatedTopics() = runTest {
        viewModel.uiState
            .test {
                awaitItem()
                topicsRepository.sendTopics(testOutputTopics)
                topicsRepository.setFollowedTopicIds(
                    setOf(testOutputTopics[0].id, testOutputTopics[1].id)
                )

                awaitItem()
                viewModel.followTopic(
                    followedTopicId = testOutputTopics[1].id,
                    followed = !testOutputTopics[1].followed
                )

                assertEquals(
                    FollowingUiState.Topics(topics = testInputTopics),
                    awaitItem()
                )
                cancel()
            }
    }
}

private const val TOPIC_1_NAME = "Android Studio"
private const val TOPIC_2_NAME = "Build"
private const val TOPIC_3_NAME = "Compose"
private const val TOPIC_DESC = "At vero eos et accusamus et iusto odio dignissimos ducimus qui."

private val testInputTopics = listOf(
    Topic(
        id = 0,
        name = TOPIC_1_NAME,
        description = TOPIC_DESC,
        followed = true
    ),
    Topic(
        id = 1,
        name = TOPIC_2_NAME,
        description = TOPIC_DESC
    ),
    Topic(
        id = 2,
        name = TOPIC_3_NAME,
        description = TOPIC_DESC
    )
)

private val testOutputTopics = listOf(
    Topic(
        id = 0,
        name = TOPIC_1_NAME,
        description = TOPIC_DESC,
        followed = true
    ),
    Topic(
        id = 1,
        name = TOPIC_2_NAME,
        description = TOPIC_DESC,
        followed = true
    ),
    Topic(
        id = 2,
        name = TOPIC_3_NAME,
        description = TOPIC_DESC
    )
)
