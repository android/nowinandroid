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

import com.google.samples.apps.nowinandroid.core.domain.GetFollowableTopicsStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.GetSaveableNewsResourcesStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.GetSortedFollowableAuthorsStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestAuthorsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.core.testing.util.TestNetworkMonitor
import com.google.samples.apps.nowinandroid.core.testing.util.TestSyncStatusMonitor
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 */
class ForYouViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val networkMonitor = TestNetworkMonitor()
    private val syncStatusMonitor = TestSyncStatusMonitor()
    private val userDataRepository = TestUserDataRepository()
    private val authorsRepository = TestAuthorsRepository()
    private val topicsRepository = TestTopicsRepository()
    private val newsRepository = TestNewsRepository()
    private val getSaveableNewsResourcesStreamUseCase = GetSaveableNewsResourcesStreamUseCase(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository
    )
    private val getSortedFollowableAuthorsStream = GetSortedFollowableAuthorsStreamUseCase(
        authorsRepository = authorsRepository,
        userDataRepository = userDataRepository
    )
    private val getFollowableTopicsStreamUseCase = GetFollowableTopicsStreamUseCase(
        topicsRepository = topicsRepository,
        userDataRepository = userDataRepository
    )
    private lateinit var viewModel: ForYouViewModel

    @Before
    fun setup() {
        viewModel = ForYouViewModel(
            syncStatusMonitor = syncStatusMonitor,
            userDataRepository = userDataRepository,
            getSaveableNewsResourcesStream = getSaveableNewsResourcesStreamUseCase,
            getSortedFollowableAuthorsStream = getSortedFollowableAuthorsStream,
            getFollowableTopicsStream = getFollowableTopicsStreamUseCase
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(
            OnboardingUiState.Loading,
            viewModel.onboardingUiState.value
        )
        assertEquals(NewsFeedUiState.Loading, viewModel.feedState.value)
    }

    @Test
    fun stateIsLoadingWhenFollowedTopicsAreLoading() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)

        assertEquals(
            OnboardingUiState.Loading,
            viewModel.onboardingUiState.value
        )
        assertEquals(NewsFeedUiState.Loading, viewModel.feedState.value)

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun stateIsLoadingWhenAppIsSyncingWithNoInterests() = runTest {
        syncStatusMonitor.setSyncing(true)

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.isSyncing.collect() }

        assertEquals(
            true,
            viewModel.isSyncing.value
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsLoadingWhenFollowedAuthorsAreLoading() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        authorsRepository.sendAuthors(sampleAuthors)

        assertEquals(
            OnboardingUiState.Loading,
            viewModel.onboardingUiState.value
        )
        assertEquals(NewsFeedUiState.Loading, viewModel.feedState.value)

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun onboardingStateIsLoadingWhenTopicsAreLoading() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        userDataRepository.setFollowedTopicIds(emptySet())

        assertEquals(
            OnboardingUiState.Loading,
            viewModel.onboardingUiState.value
        )
        assertEquals(NewsFeedUiState.Success(emptyList()), viewModel.feedState.value)

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun onboardingStateIsLoadingWhenAuthorsAreLoading() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        userDataRepository.setFollowedAuthorIds(emptySet())

        assertEquals(
            OnboardingUiState.Loading,
            viewModel.onboardingUiState.value
        )
        assertEquals(NewsFeedUiState.Success(emptyList()), viewModel.feedState.value)

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun onboardingIsShownWhenNewsResourcesAreLoading() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(emptySet())

        assertEquals(
            OnboardingUiState.Shown(
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
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "1",
                            name = "Android Dev 2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev 3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    )
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = emptyList()
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun onboardingIsShownAfterLoadingEmptyFollowedTopicsAndAuthors() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedTopicIds(emptySet())
        userDataRepository.setFollowedAuthorIds(emptySet())
        newsRepository.sendNewsResources(sampleNewsResources)

        assertEquals(
            OnboardingUiState.Shown(
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
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "1",
                            name = "Android Dev 2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev 3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    )
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = emptyList()

            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun onboardingIsNotShownAfterUserDismissesOnboarding() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(emptySet())
        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(setOf("0", "1"))
        viewModel.dismissOnboarding()

        assertEquals(
            OnboardingUiState.NotShown,
            viewModel.onboardingUiState.value
        )
        assertEquals(NewsFeedUiState.Loading, viewModel.feedState.value)

        newsRepository.sendNewsResources(sampleNewsResources)

        assertEquals(
            OnboardingUiState.NotShown,
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed =
                sampleNewsResources.map {
                    SaveableNewsResource(
                        newsResource = it,
                        isSaved = false
                    )
                }
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun topicSelectionUpdatesAfterSelectingTopic() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(emptySet())
        newsRepository.sendNewsResources(sampleNewsResources)

        assertEquals(
            OnboardingUiState.Shown(
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
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "1",
                            name = "Android Dev 2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev 3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    )
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = emptyList(),
            ),
            viewModel.feedState.value
        )

        viewModel.updateTopicSelection("1", isChecked = true)

        assertEquals(
            OnboardingUiState.Shown(
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
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "1",
                            name = "Android Dev 2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev 3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    )
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
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
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun authorSelectionUpdatesAfterSelectingAuthor() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(emptySet())
        newsRepository.sendNewsResources(sampleNewsResources)

        assertEquals(
            OnboardingUiState.Shown(
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
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "1",
                            name = "Android Dev 2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev 3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    )
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = emptyList(),
            ),
            viewModel.feedState.value
        )

        viewModel.updateAuthorSelection("1", isChecked = true)

        assertEquals(
            OnboardingUiState.Shown(
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
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "1",
                            name = "Android Dev 2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = true
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev 3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    )
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
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
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun topicSelectionUpdatesAfterUnselectingTopic() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(emptySet())
        newsRepository.sendNewsResources(sampleNewsResources)
        viewModel.updateTopicSelection("1", isChecked = true)
        viewModel.updateTopicSelection("1", isChecked = false)

        advanceUntilIdle()
        assertEquals(
            OnboardingUiState.Shown(
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
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "1",
                            name = "Android Dev 2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev 3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    )
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = emptyList()
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun authorSelectionUpdatesAfterUnselectingAuthor() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(emptySet())
        newsRepository.sendNewsResources(sampleNewsResources)
        viewModel.updateAuthorSelection("1", isChecked = true)
        viewModel.updateAuthorSelection("1", isChecked = false)

        assertEquals(

            OnboardingUiState.Shown(
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
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "1",
                            name = "Android Dev 2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev 3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = false
                    )
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = emptyList()
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }

    @Test
    fun newsResourceSelectionUpdatesAfterLoadingFollowedTopics() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(setOf("1"))
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(setOf("1"))
        userDataRepository.setShouldHideOnboarding(true)
        newsRepository.sendNewsResources(sampleNewsResources)
        viewModel.updateNewsResourceSaved("2", true)

        assertEquals(
            OnboardingUiState.NotShown,
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
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
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }
}

private val sampleAuthors = listOf(
    Author(
        id = "0",
        name = "Android Dev",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
    ),
    Author(
        id = "1",
        name = "Android Dev 2",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
    ),
    Author(
        id = "2",
        name = "Android Dev 3",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
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
                mediumPage = "",
                bio = "",
            )
        )
    ),
    NewsResource(
        id = "2",
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
                mediumPage = "",
                bio = "",
            )
        )
    ),
    NewsResource(
        id = "3",
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
                mediumPage = "",
                bio = "",
            )
        )
    ),
)
