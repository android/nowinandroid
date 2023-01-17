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

import com.google.samples.apps.nowinandroid.core.domain.GetFollowableTopicsUseCase
import com.google.samples.apps.nowinandroid.core.domain.GetUserNewsResourcesUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.domain.model.mapToUserNewsResources
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestTopicsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.emptyUserData
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.core.testing.util.TestSyncStatusMonitor
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
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

    private val syncStatusMonitor = TestSyncStatusMonitor()
    private val userDataRepository = TestUserDataRepository()
    private val topicsRepository = TestTopicsRepository()
    private val newsRepository = TestNewsRepository()
    private val getUserNewsResourcesUseCase = GetUserNewsResourcesUseCase(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository
    )
    private val getFollowableTopicsUseCase = GetFollowableTopicsUseCase(
        topicsRepository = topicsRepository,
        userDataRepository = userDataRepository
    )
    private lateinit var viewModel: ForYouViewModel

    @Before
    fun setup() {
        viewModel = ForYouViewModel(
            syncStatusMonitor = syncStatusMonitor,
            userDataRepository = userDataRepository,
            getUserNewsResources = getUserNewsResourcesUseCase,
            getFollowableTopics = getFollowableTopicsUseCase
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(
            expected = listOf(
                ForYouItem.OnBoarding(OnboardingUiState.Loading),
                ForYouItem.News.Loading,
            ),
            actual = viewModel.forYouItems.value
        )
    }

    @Test
    fun stateIsLoadingWhenFollowedTopicsAreLoading() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.forYouItems.collect() }

        topicsRepository.sendTopics(sampleTopics)

        assertEquals(
            expected = listOf(
                ForYouItem.OnBoarding(OnboardingUiState.Loading),
                ForYouItem.News.Loading
            ),
            actual = viewModel.forYouItems.value
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsLoadingWhenAppIsSyncingWithNoInterests() = runTest {
        syncStatusMonitor.setSyncing(true)

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.isSyncing.collect() }

        assertEquals(
            expected = true,
            actual = viewModel.isSyncing.value
        )

        collectJob.cancel()
    }

    @Test
    fun onboardingStateIsLoadingWhenTopicsAreLoading() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.forYouItems.collect() }

        userDataRepository.setFollowedTopicIds(emptySet())

        assertEquals(
            expected = listOf(
                ForYouItem.OnBoarding(OnboardingUiState.Loading),
            ),
            actual = viewModel.forYouItems.value
        )

        collectJob.cancel()
    }

    @Test
    fun onboardingIsShownWhenTopicsArePresent() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.forYouItems.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
        advanceUntilIdle()

        assertEquals(
            expected = listOf(
                ForYouItem.OnBoarding(
                    OnboardingUiState.Shown(
                        topics = sampleTopics.map {
                            FollowableTopic(
                                topic = it,
                                isFollowed = false
                            )
                        },
                    )
                ),
            ),
            actual = viewModel.forYouItems.value
        )

        collectJob.cancel()
    }

    @Test
    fun onboardingIsShownWithNoNewsResourcesAfterLoadingEmptyFollowedTopics() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.forYouItems.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
        newsRepository.sendNewsResources(sampleNewsResources)

        assertEquals(
            expected = listOf(
                ForYouItem.OnBoarding(
                    OnboardingUiState.Shown(
                        topics = sampleTopics.map {
                            FollowableTopic(
                                topic = it,
                                isFollowed = false
                            )
                        }
                    )
                ),
            ),
            actual = viewModel.forYouItems.value
        )

        collectJob.cancel()
    }

    @Test
    fun onboardingIsNotShownAfterUserDismissesOnboarding() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.forYouItems.collect() }

        topicsRepository.sendTopics(sampleTopics)

        val followedTopicIds = setOf("0", "1")
        val userData = emptyUserData.copy(followedTopics = followedTopicIds)
        userDataRepository.setUserData(userData)
        viewModel.dismissOnboarding()
        advanceUntilIdle()

        assertEquals(
            expected = listOf<ForYouItem>(ForYouItem.News.Loading),
            actual = viewModel.forYouItems.value
        )

        newsRepository.sendNewsResources(sampleNewsResources)
        advanceUntilIdle()

        assertEquals(
            expected = sampleNewsResources
                .mapToUserNewsResources(userData)
                .map<UserNewsResource, ForYouItem>(ForYouItem.News::Loaded),
            actual = viewModel.forYouItems.value
        )

        collectJob.cancel()
    }

    @Test
    fun topicSelectionUpdatesAfterSelectingTopic() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.forYouItems.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
        newsRepository.sendNewsResources(sampleNewsResources)
        advanceUntilIdle()

        assertEquals(
            expected = listOf(
                ForYouItem.OnBoarding(
                    OnboardingUiState.Shown(
                        topics = sampleTopics.map {
                            FollowableTopic(
                                it, false
                            )
                        }
                    )
                ),
            ),
            actual = viewModel.forYouItems.value
        )

        val followedTopicId = sampleTopics[1].id
        viewModel.updateTopicSelection(followedTopicId, isChecked = true)
        advanceUntilIdle()

        val userData = emptyUserData.copy(followedTopics = setOf(followedTopicId))

        assertEquals(
            expected = listOf(
                ForYouItem.OnBoarding(
                    OnboardingUiState.Shown(
                        topics = sampleTopics.map {
                            FollowableTopic(it, it.id == followedTopicId)
                        },
                    )
                ),
                ForYouItem.News.Loaded(
                    UserNewsResource(sampleNewsResources[1], userData),
                ),
                ForYouItem.News.Loaded(
                    UserNewsResource(sampleNewsResources[2], userData),
                ),
            ),
            actual = viewModel.forYouItems.value
        )

        collectJob.cancel()
    }

    @Test
    fun topicSelectionUpdatesAfterUnselectingTopic() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.forYouItems.collect() }

        val followedTopicId = "1"
        val userData = emptyUserData.copy(
            followedTopics = setOf(followedTopicId)
        )
        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setUserData(userData)
        newsRepository.sendNewsResources(sampleNewsResources)
        advanceUntilIdle()

        assertEquals(
            expected = listOf<ForYouItem>(
                ForYouItem.OnBoarding(
                    OnboardingUiState.Shown(
                        topics = sampleTopics.map {
                            FollowableTopic(
                                topic = it,
                                isFollowed = it.id == followedTopicId
                            )
                        },
                    )
                ),
            ) + sampleNewsResources
                .filter { newsResource ->
                    newsResource.topics
                        .map(Topic::id)
                        .contains(followedTopicId)
                }
                .mapToUserNewsResources(userData)
                .map(ForYouItem.News::Loaded),
            actual = viewModel.forYouItems.value
        )

        viewModel.updateTopicSelection("1", isChecked = false)
        advanceUntilIdle()

        assertEquals(
            expected = listOf<ForYouItem>(
                ForYouItem.OnBoarding(
                    OnboardingUiState.Shown(
                        topics = sampleTopics.map {
                            FollowableTopic(
                                topic = it,
                                isFollowed = false
                            )
                        },
                    )
                ),
            ),
            actual = viewModel.forYouItems.value
        )

        collectJob.cancel()
    }

    @Test
    fun newsResourceSelectionUpdatesAfterLoadingFollowedTopics() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.forYouItems.collect() }

        val followedTopicIds = setOf("1")
        val userData = emptyUserData.copy(
            followedTopics = followedTopicIds,
            shouldHideOnboarding = true
        )

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setUserData(userData)
        newsRepository.sendNewsResources(sampleNewsResources)

        val bookmarkedNewsResourceId = "2"
        viewModel.updateNewsResourceSaved(
            newsResourceId = bookmarkedNewsResourceId,
            isChecked = true
        )

        val userDataExpected = userData.copy(
            bookmarkedNewsResources = setOf(bookmarkedNewsResourceId)
        )
        advanceUntilIdle()

        assertEquals(
            expected = listOf(
                UserNewsResource(newsResource = sampleNewsResources[1], userDataExpected),
                UserNewsResource(newsResource = sampleNewsResources[2], userDataExpected),

            ).map<UserNewsResource, ForYouItem>(ForYouItem.News::Loaded),
            actual = viewModel.forYouItems.value
        )

        collectJob.cancel()
    }
}

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
    ),
)
