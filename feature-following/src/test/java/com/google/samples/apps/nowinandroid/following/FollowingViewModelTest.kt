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

package com.google.samples.apps.nowinandroid.following

import app.cash.turbine.test
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.util.TestDispatcherRule
import com.google.samples.apps.nowinandroid.feature.following.FollowingUiState
import com.google.samples.apps.nowinandroid.feature.following.FollowingViewModel
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
                topicsRepository.sendTopics(testInputTopics.map { it.topic })
                topicsRepository.setFollowedTopicIds(setOf(testInputTopics[0].topic.id))

                awaitItem()
                viewModel.followTopic(
                    followedTopicId = testInputTopics[1].topic.id,
                    followed = true
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
                topicsRepository.sendTopics(testOutputTopics.map { it.topic })
                topicsRepository.setFollowedTopicIds(
                    setOf(testOutputTopics[0].topic.id, testOutputTopics[1].topic.id)
                )

                awaitItem()
                viewModel.followTopic(
                    followedTopicId = testOutputTopics[1].topic.id,
                    followed = false
                )

                assertEquals(
                    FollowingUiState.Topics(
                        topics = testInputTopics
                    ),
                    awaitItem()
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
            id = 0,
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
            id = 1,
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
            id = 2,
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
            id = 0,
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
            id = 1,
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
            id = 2,
            name = TOPIC_3_NAME,
            shortDescription = TOPIC_SHORT_DESC,
            longDescription = TOPIC_LONG_DESC,
            url = TOPIC_URL,
            imageUrl = TOPIC_IMAGE_URL,
        ),
        isFollowed = false
    )
)
