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
import com.google.samples.apps.nowinandroid.core.model.data.fakeAuthor
import com.google.samples.apps.nowinandroid.core.model.data.fakeNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.fakeTopic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestAuthorsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.core.testing.util.TestNetworkMonitor
import com.google.samples.apps.nowinandroid.core.testing.util.TestSyncStatusMonitor
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
                topics = sampleTopics.map { FollowableTopic(it, isFollowed = false) },
                authors = sampleAuthors.map { FollowableAuthor(it, isFollowed = false) },
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(emptyList()),
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
                topics = sampleTopics.map { FollowableTopic(it, isFollowed = false) },
                authors = sampleAuthors.map { FollowableAuthor(it, isFollowed = false) },
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(emptyList()),
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
        userDataRepository.setFollowedTopicIds(sampleTopics.take(2).map { it.id }.toSet())
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
                feed = sampleNewsResources.map { SaveableNewsResource(it, isSaved = false) }
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
                topics = sampleTopics.map { FollowableTopic(it, isFollowed = false) },
                authors = sampleAuthors.map { FollowableAuthor(it, isFollowed = false) },
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(emptyList()),
            viewModel.feedState.value
        )

        viewModel.updateTopicSelection(sampleTopics[1].id, isChecked = true)

        assertEquals(
            OnboardingUiState.Shown(
                topics = listOf(
                    FollowableTopic(sampleTopics[0], isFollowed = false),
                    FollowableTopic(sampleTopics[1], isFollowed = true),
                    FollowableTopic(sampleTopics[2], isFollowed = false),
                ),
                authors = sampleAuthors.map { FollowableAuthor(it, isFollowed = false) },
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = listOf(
                    SaveableNewsResource(sampleNewsResources[1], isSaved = false),
                    SaveableNewsResource(sampleNewsResources[2], isSaved = false)
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
                topics = sampleTopics.map { FollowableTopic(it, isFollowed = false) },
                authors = sampleAuthors.map { FollowableAuthor(it, isFollowed = false) },
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(emptyList()),
            viewModel.feedState.value
        )

        viewModel.updateAuthorSelection(sampleAuthors[1].id, isChecked = true)

        assertEquals(
            OnboardingUiState.Shown(
                topics = sampleTopics.map { FollowableTopic(it, isFollowed = false) },
                authors = listOf(
                    FollowableAuthor(sampleAuthors[0], isFollowed = false),
                    FollowableAuthor(sampleAuthors[1], isFollowed = true),
                    FollowableAuthor(sampleAuthors[2], isFollowed = false)
                ),
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = listOf(
                    SaveableNewsResource(sampleNewsResources[1], isSaved = false),
                    SaveableNewsResource(sampleNewsResources[2], isSaved = false)
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
        viewModel.updateTopicSelection(sampleTopics[1].id, isChecked = true)
        viewModel.updateTopicSelection(sampleTopics[1].id, isChecked = false)

        advanceUntilIdle()
        assertEquals(
            OnboardingUiState.Shown(
                topics = sampleTopics.map { FollowableTopic(it, isFollowed = false) },
                authors = sampleAuthors.map { FollowableAuthor(it, isFollowed = false) },
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(emptyList()),
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
        viewModel.updateAuthorSelection(sampleAuthors[1].id, isChecked = true)
        viewModel.updateAuthorSelection(sampleAuthors[1].id, isChecked = false)

        assertEquals(
            OnboardingUiState.Shown(
                topics = sampleTopics.map { FollowableTopic(it, isFollowed = false) },
                authors = sampleAuthors.map { FollowableAuthor(it, isFollowed = false) },
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(emptyList()),
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
        userDataRepository.setFollowedTopicIds(setOf(sampleTopics[1].id))
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(setOf(sampleAuthors[1].id))
        userDataRepository.setShouldHideOnboarding(true)
        newsRepository.sendNewsResources(sampleNewsResources)
        viewModel.updateNewsResourceSaved(sampleNewsResources[1].id, true)

        assertEquals(
            OnboardingUiState.NotShown,
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = listOf(
                    SaveableNewsResource(sampleNewsResources[1], isSaved = true),
                    SaveableNewsResource(sampleNewsResources[2], isSaved = false)
                )
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
    }
}

private val sampleAuthors = listOf(
    fakeAuthor(id = "1"),
    fakeAuthor(id = "2"),
    fakeAuthor(id = "3"),
)

private val sampleTopics = listOf(
    fakeTopic(id = "1", name = "Headlines"),
    fakeTopic(id = "2", name = "UI"),
    fakeTopic(id = "3", name = "Tools"),
)

private val sampleNewsResources = listOf(
    fakeNewsResource(
        id = "1",
        authors = listOf(sampleAuthors[0]),
        topics = listOf(sampleTopics[0])
    ),
    fakeNewsResource(
        id = "2",
        authors = listOf(sampleAuthors[1]),
        topics = listOf(sampleTopics[1])
    ),
    fakeNewsResource(
        id = "3",
        authors = listOf(sampleAuthors[1]),
        topics = listOf(sampleTopics[1])
    ),
)
