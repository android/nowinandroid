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
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.Topic
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

    private val networkMonitor = TestNetworkMonitor()
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
            getSaveableNewsResources = getUserNewsResourcesUseCase,
            getFollowableTopics = getFollowableTopicsUseCase
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
    fun onboardingIsShownWhenNewsResourcesAreLoading() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())

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
    fun onboardingIsShownAfterLoadingEmptyFollowedTopics() = runTest {
        val collectJob1 =
            launch(UnconfinedTestDispatcher()) { viewModel.onboardingUiState.collect() }
        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.feedState.collect() }

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(emptySet())
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

        topicsRepository.sendTopics(sampleTopics)

        val followedTopicIds = setOf("0", "1")
        userDataRepository.setFollowedTopicIds(followedTopicIds)
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
                    UserNewsResource(
                        id = it.id,
                        title = it.title,
                        content = it.content,
                        url = it.url,
                        headerImageUrl = it.headerImageUrl,
                        publishDate = it.publishDate,
                        type = it.type,
                        followableTopics = it.topics.map { topic ->
                            FollowableTopic(
                                topic = topic,
                                isFollowed = followedTopicIds.contains(topic.id)
                            )
                        },
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
        newsRepository.sendNewsResources(sampleNewsResources)

        assertEquals(
            OnboardingUiState.Shown(
                topics = sampleTopics.map {
                    FollowableTopic(it, false)
                }
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = emptyList(),
            ),
            viewModel.feedState.value
        )

        val followedTopicId = sampleTopics[1].id
        viewModel.updateTopicSelection(followedTopicId, isChecked = true)

        assertEquals(
            OnboardingUiState.Shown(
                topics = sampleTopics.map {
                    FollowableTopic(it, it.id == followedTopicId)
                }
            ),
            viewModel.onboardingUiState.value
        )
        assertEquals(
            NewsFeedUiState.Success(
                feed = listOf(
                    UserNewsResource(
                        id = sampleNewsResources[1].id,
                        title = sampleNewsResources[1].title,
                        content = sampleNewsResources[1].content,
                        url = sampleNewsResources[1].url,
                        headerImageUrl = sampleNewsResources[1].headerImageUrl,
                        publishDate = sampleNewsResources[1].publishDate,
                        type = sampleNewsResources[1].type,
                        followableTopics = sampleNewsResources[1].topics.map { topic ->
                            FollowableTopic(
                                topic = topic,
                                isFollowed = topic.id == followedTopicId
                            )
                        },
                        isSaved = false
                    ),
                    UserNewsResource(
                        id = sampleNewsResources[2].id,
                        title = sampleNewsResources[2].title,
                        content = sampleNewsResources[2].content,
                        url = sampleNewsResources[2].url,
                        headerImageUrl = sampleNewsResources[2].headerImageUrl,
                        publishDate = sampleNewsResources[2].publishDate,
                        type = sampleNewsResources[2].type,
                        followableTopics = sampleNewsResources[2].topics.map { topic ->
                            FollowableTopic(
                                topic = topic,
                                isFollowed = topic.id == followedTopicId
                            )
                        },
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

        val followedTopicIds = setOf("1")

        topicsRepository.sendTopics(sampleTopics)
        userDataRepository.setFollowedTopicIds(followedTopicIds)
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
                    UserNewsResource(
                        id = sampleNewsResources[1].id,
                        title = sampleNewsResources[1].title,
                        content = sampleNewsResources[1].content,
                        url = sampleNewsResources[1].url,
                        headerImageUrl = sampleNewsResources[1].headerImageUrl,
                        publishDate = sampleNewsResources[1].publishDate,
                        type = sampleNewsResources[1].type,
                        followableTopics = sampleNewsResources[1].topics.map { topic ->
                            FollowableTopic(
                                topic = topic,
                                isFollowed = followedTopicIds.contains(topic.id)
                            )
                        },
                        isSaved = true
                    ),
                    UserNewsResource(
                        id = sampleNewsResources[2].id,
                        title = sampleNewsResources[2].title,
                        content = sampleNewsResources[2].content,
                        url = sampleNewsResources[2].url,
                        headerImageUrl = sampleNewsResources[2].headerImageUrl,
                        publishDate = sampleNewsResources[2].publishDate,
                        type = sampleNewsResources[2].type,
                        followableTopics = sampleNewsResources[2].topics.map { topic ->
                            FollowableTopic(
                                topic = topic,
                                isFollowed = followedTopicIds.contains(topic.id)
                            )
                        },
                        isSaved = false
                    ),
                )
            ),
            viewModel.feedState.value
        )

        collectJob1.cancel()
        collectJob2.cancel()
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
