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

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestAuthorsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.util.TestDispatcherRule
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForYouViewModelTest {
    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val authorsRepository = TestAuthorsRepository()
    private val topicsRepository = TestTopicsRepository()
    private val newsRepository = TestNewsRepository()
    private lateinit var viewModel: ForYouViewModel

    @Before
    fun setup() {
        viewModel = ForYouViewModel(
            authorsRepository = authorsRepository,
            topicsRepository = topicsRepository,
            newsRepository = newsRepository,
            savedStateHandle = SavedStateHandle()
        )
    }

    /**
     * A pairing of [ForYouInterestsSelectionState] and [ForYouFeedState] for ease of testing
     * state updates as a single flow.
     */
    private data class ForYouUiState(
        val interestsSelectionState: ForYouInterestsSelectionState,
        val feedState: ForYouFeedState,
    )

    private val ForYouViewModel.uiState get() =
        combine(
            interestsSelectionState,
            feedState,
            ::ForYouUiState
        )

    @Test
    fun stateIsInitiallyLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(
                ForYouUiState(
                    ForYouInterestsSelectionState.Loading,
                    ForYouFeedState.Loading
                ),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun stateIsLoadingWhenFollowedTopicsAreLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(
                ForYouUiState(
                    ForYouInterestsSelectionState.Loading,
                    ForYouFeedState.Loading
                ),
                awaitItem()
            )
            topicsRepository.sendTopics(sampleTopics)

            cancel()
        }
    }

    @Test
    fun stateIsLoadingWhenFollowedAuthorsAreLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(
                ForYouUiState(
                    ForYouInterestsSelectionState.Loading,
                    ForYouFeedState.Loading
                ),
                awaitItem()
            )
            authorsRepository.sendAuthors(sampleAuthors)

            cancel()
        }
    }

    @Test
    fun stateIsLoadingWhenTopicsAreLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(
                ForYouUiState(
                    ForYouInterestsSelectionState.Loading,
                    ForYouFeedState.Loading
                ),
                awaitItem()
            )
            topicsRepository.setFollowedTopicIds(emptySet())

            cancel()
        }
    }

    @Test
    fun stateIsLoadingWhenAuthorsAreLoading() = runTest {
        viewModel.uiState.test {
            assertEquals(
                ForYouUiState(
                    ForYouInterestsSelectionState.Loading,
                    ForYouFeedState.Loading
                ),
                awaitItem()
            )
            authorsRepository.setFollowedAuthorIds(emptySet())

            cancel()
        }
    }

    @Test
    fun stateIsInterestsSelectionWhenNewsResourcesAreLoading() = runTest {
        viewModel.uiState.test {
            advanceUntilIdle()
            expectMostRecentItem()
            topicsRepository.sendTopics(sampleTopics)
            topicsRepository.setFollowedTopicIds(emptySet())
            authorsRepository.sendAuthors(sampleAuthors)
            authorsRepository.setFollowedAuthorIds(emptySet())

            advanceUntilIdle()
            assertEquals(
                ForYouUiState(
                    interestsSelectionState =
                    ForYouInterestsSelectionState.WithInterestsSelection(
                        topics = listOf(
                            FollowableTopic(
                                topic = Topic(
                                    id = "0",
                                    name = "Headlines",
                                    shortDescription = "",
                                    longDescription = "long description",
                                    url = "URL",
                                    imageUrl = "image URL",
                                ),
                                isFollowed = false
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "1",
                                    name = "UI",
                                    shortDescription = "",
                                    longDescription = "long description",
                                    url = "URL",
                                    imageUrl = "image URL",
                                ),
                                isFollowed = false
                            ),
                            FollowableTopic(
                                topic = Topic(
                                    id = "2",
                                    name = "Tools",
                                    shortDescription = "",
                                    longDescription = "long description",
                                    url = "URL",
                                    imageUrl = "image URL",
                                ),
                                isFollowed = false
                            ),
                        ),
                        authors = listOf(
                            FollowableAuthor(
                                author = Author(
                                    id = "0",
                                    name = "Android Dev",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = ""
                                ),
                                isFollowed = false
                            ),
                            FollowableAuthor(
                                author = Author(
                                    id = "1",
                                    name = "Android Dev 2",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = ""
                                ),
                                isFollowed = false
                            ),
                            FollowableAuthor(
                                author = Author(
                                    id = "2",
                                    name = "Android Dev 3",
                                    imageUrl = "",
                                    twitter = "",
                                    mediumPage = ""
                                ),
                                isFollowed = false
                            )
                        ),
                    ),
                    feedState = ForYouFeedState.Success(
                        feed = emptyList()
                    )
                ),
                expectMostRecentItem()
            )

            cancel()
        }
    }

    @Test
    fun stateIsInterestsSelectionAfterLoadingEmptyFollowedTopicsAndAuthors() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                authorsRepository.sendAuthors(sampleAuthors)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = emptyList()
                        )
                    ),
                    expectMostRecentItem()
                )
                cancel()
            }
    }

    @Test
    fun stateIsWithoutInterestsSelectionAfterLoadingFollowedTopics() = runTest {
        viewModel.uiState
            .test {
                advanceUntilIdle()
                expectMostRecentItem()
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(setOf("0", "1"))

                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.NoInterestsSelection,
                        feedState = ForYouFeedState.Loading
                    ),
                    awaitItem()
                )

                newsRepository.sendNewsResources(sampleNewsResources)

                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.NoInterestsSelection,
                        feedState = ForYouFeedState.Success(
                            feed = sampleNewsResources.map {
                                SaveableNewsResource(
                                    newsResource = it,
                                    isSaved = false
                                )
                            }
                        )
                    ),
                    awaitItem()
                )
                cancel()
            }
    }

    @Test
    fun stateIsWithoutInterestsSelectionAfterLoadingFollowedAuthors() = runTest {
        viewModel.uiState
            .test {
                advanceUntilIdle()
                expectMostRecentItem()
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(setOf("0", "1"))
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())

                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.NoInterestsSelection,
                        feedState = ForYouFeedState.Loading
                    ),
                    awaitItem()
                )

                newsRepository.sendNewsResources(sampleNewsResources)

                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.NoInterestsSelection,
                        feedState = ForYouFeedState.Success(
                            feed = sampleNewsResources.map {
                                SaveableNewsResource(
                                    newsResource = it,
                                    isSaved = false
                                )
                            }
                        )
                    ),
                    awaitItem()
                )
                cancel()
            }
    }

    @Test
    fun topicSelectionUpdatesAfterSelectingTopic() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)

                advanceUntilIdle()
                expectMostRecentItem()

                viewModel.updateTopicSelection("1", isChecked = true)

                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = true
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = emptyList(),
                        )
                    ),
                    awaitItem()
                )
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = true
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Loading
                    ),
                    awaitItem()
                )
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = true
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = listOf(
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[1],
                                    isSaved = false
                                ),
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[2],
                                    isSaved = false
                                )
                            )
                        )
                    ),
                    awaitItem()
                )
                cancel()
            }
    }

    @Test
    fun topicSelectionUpdatesAfterSelectingAuthor() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)

                advanceUntilIdle()
                expectMostRecentItem()

                viewModel.updateAuthorSelection("1", isChecked = true)

                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = true
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = emptyList(),
                        )
                    ),
                    awaitItem()
                )
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = true
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Loading
                    ),
                    awaitItem()
                )
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = true
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = listOf(
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[1],
                                    isSaved = false
                                ),
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[2],
                                    isSaved = false
                                )
                            )
                        )
                    ),
                    awaitItem()
                )
                cancel()
            }
    }

    @Test
    fun topicSelectionUpdatesAfterUnselectingTopic() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)
                viewModel.updateTopicSelection("1", isChecked = true)
                viewModel.updateTopicSelection("1", isChecked = false)

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = emptyList()
                        )
                    ),
                    expectMostRecentItem()
                )
                cancel()
            }
    }

    @Test
    fun topicSelectionUpdatesAfterUnselectingAuthor() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)
                viewModel.updateAuthorSelection("1", isChecked = true)
                viewModel.updateAuthorSelection("1", isChecked = false)

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            ),
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = emptyList()
                        )
                    ),
                    expectMostRecentItem()
                )
                cancel()
            }
    }

    @Test
    fun topicSelectionUpdatesAfterSavingTopicsOnly() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)
                viewModel.updateTopicSelection("1", isChecked = true)

                advanceUntilIdle()
                expectMostRecentItem()

                viewModel.saveFollowedInterests()

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.NoInterestsSelection,
                        feedState = ForYouFeedState.Success(
                            feed = listOf(
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[1],
                                    isSaved = false,
                                ),
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[2],
                                    isSaved = false,
                                )
                            )
                        )
                    ),
                    expectMostRecentItem()
                )
                assertEquals(setOf("1"), topicsRepository.getCurrentFollowedTopics())
                assertEquals(emptySet<Int>(), authorsRepository.getCurrentFollowedAuthors())
                cancel()
            }
    }

    @Test
    fun topicSelectionUpdatesAfterSavingAuthorsOnly() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)
                viewModel.updateAuthorSelection("0", isChecked = true)

                advanceUntilIdle()
                expectMostRecentItem()

                viewModel.saveFollowedInterests()

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.NoInterestsSelection,
                        feedState = ForYouFeedState.Success(
                            feed = listOf(
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[0],
                                    isSaved = false
                                ),
                            )
                        )
                    ),
                    expectMostRecentItem()
                )
                assertEquals(emptySet<Int>(), topicsRepository.getCurrentFollowedTopics())
                assertEquals(setOf("0"), authorsRepository.getCurrentFollowedAuthors())
                cancel()
            }
    }

    @Test
    fun topicSelectionUpdatesAfterSavingAuthorsAndTopics() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)
                viewModel.updateAuthorSelection("1", isChecked = true)
                viewModel.updateTopicSelection("1", isChecked = true)

                advanceUntilIdle()
                expectMostRecentItem()

                viewModel.saveFollowedInterests()

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.NoInterestsSelection,
                        feedState = ForYouFeedState.Success(
                            feed = listOf(
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[1],
                                    isSaved = false
                                ),
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[2],
                                    isSaved = false
                                )
                            )
                        )
                    ),
                    expectMostRecentItem()
                )
                assertEquals(setOf("1"), topicsRepository.getCurrentFollowedTopics())
                assertEquals(setOf("1"), authorsRepository.getCurrentFollowedAuthors())
                cancel()
            }
    }

    @Test
    fun topicSelectionIsResetAfterSavingTopicsAndRemovingThem() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)
                viewModel.updateTopicSelection("1", isChecked = true)
                viewModel.saveFollowedInterests()

                advanceUntilIdle()
                expectMostRecentItem()

                topicsRepository.setFollowedTopicIds(emptySet())

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            )
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = emptyList()
                        )
                    ),

                    expectMostRecentItem()
                )
                cancel()
            }
    }

    @Test
    fun authorSelectionIsResetAfterSavingAuthorsAndRemovingThem() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(emptySet())
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(emptySet())
                newsRepository.sendNewsResources(sampleNewsResources)
                viewModel.updateAuthorSelection("1", isChecked = true)
                viewModel.saveFollowedInterests()

                advanceUntilIdle()
                expectMostRecentItem()

                authorsRepository.setFollowedAuthorIds(emptySet())

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.WithInterestsSelection(
                            topics = listOf(
                                FollowableTopic(
                                    topic = Topic(
                                        id = "0",
                                        name = "Headlines",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "1",
                                        name = "UI",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                ),
                                FollowableTopic(
                                    topic = Topic(
                                        id = "2",
                                        name = "Tools",
                                        shortDescription = "",
                                        longDescription = "long description",
                                        url = "URL",
                                        imageUrl = "image URL",
                                    ),
                                    isFollowed = false
                                )
                            ),
                            authors = listOf(
                                FollowableAuthor(
                                    author = Author(
                                        id = "0",
                                        name = "Android Dev",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "1",
                                        name = "Android Dev 2",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                ),
                                FollowableAuthor(
                                    author = Author(
                                        id = "2",
                                        name = "Android Dev 3",
                                        imageUrl = "",
                                        twitter = "",
                                        mediumPage = ""
                                    ),
                                    isFollowed = false
                                )
                            )
                        ),
                        feedState = ForYouFeedState.Success(
                            feed = emptyList()
                        )
                    ),
                    expectMostRecentItem()
                )
                cancel()
            }
    }

    @Test
    fun newsResourceSelectionUpdatesAfterLoadingFollowedTopics() = runTest {
        viewModel.uiState
            .test {
                topicsRepository.sendTopics(sampleTopics)
                topicsRepository.setFollowedTopicIds(setOf("1"))
                authorsRepository.sendAuthors(sampleAuthors)
                authorsRepository.setFollowedAuthorIds(setOf("1"))
                newsRepository.sendNewsResources(sampleNewsResources)
                viewModel.updateNewsResourceSaved("2", true)

                advanceUntilIdle()
                assertEquals(
                    ForYouUiState(
                        interestsSelectionState =
                        ForYouInterestsSelectionState.NoInterestsSelection,
                        feedState = ForYouFeedState.Success(
                            feed = listOf(
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[1],
                                    isSaved = true
                                ),
                                SaveableNewsResource(
                                    newsResource = sampleNewsResources[2],
                                    isSaved = false
                                )
                            )
                        )
                    ),
                    expectMostRecentItem()
                )
                cancel()
            }
    }
}

private val sampleAuthors = listOf(
    Author(
        id = "0",
        name = "Android Dev",
        imageUrl = "",
        twitter = "",
        mediumPage = ""
    ),
    Author(
        id = "1",
        name = "Android Dev 2",
        imageUrl = "",
        twitter = "",
        mediumPage = ""
    ),
    Author(
        id = "2",
        name = "Android Dev 3",
        imageUrl = "",
        twitter = "",
        mediumPage = ""
    )
)

private val sampleTopics = listOf(
    Topic(
        id = "0",
        name = "Headlines",
        shortDescription = "",
        longDescription = "long description",
        url = "URL",
        imageUrl = "image URL",
    ),
    Topic(
        id = "1",
        name = "UI",
        shortDescription = "",
        longDescription = "long description",
        url = "URL",
        imageUrl = "image URL",
    ),
    Topic(
        id = "2",
        name = "Tools",
        shortDescription = "",
        longDescription = "long description",
        url = "URL",
        imageUrl = "image URL",
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
        authors = listOf(
            Author(
                id = "0",
                name = "Android Dev",
                imageUrl = "",
                twitter = "",
                mediumPage = ""
            )
        )
    ),
    NewsResource(
        id = "2",
        episodeId = "52",
        title = "Transformations and customisations in the Paging Library",
        content = "A demonstration of different operations that can be performed with Paging. " +
            "Transformations like inserting separators, when to create a new pager, and " +
            "customisation options for consuming PagingData.",
        url = "https://youtu.be/ZARz0pjm5YM",
        headerImageUrl = "https://i.ytimg.com/vi/ZARz0pjm5YM/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-01T00:00:00.000Z"),
        type = Video,
        topics = listOf(
            Topic(
                id = "1",
                name = "UI",
                shortDescription = "",
                longDescription = "long description",
                url = "URL",
                imageUrl = "image URL",
            ),
        ),
        authors = listOf(
            Author(
                id = "1",
                name = "Android Dev 2",
                imageUrl = "",
                twitter = "",
                mediumPage = ""
            )
        )
    ),
    NewsResource(
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
                longDescription = "long description",
                url = "URL",
                imageUrl = "image URL",
            ),
        ),
        authors = listOf(
            Author(
                id = "1",
                name = "Android Dev 2",
                imageUrl = "",
                twitter = "",
                mediumPage = ""
            )
        )
    ),
)
