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

package com.google.samples.apps.nowinandroid.core.domain

import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Rule
import org.junit.Test

class GetUserNewsResourcesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val newsRepository = TestNewsRepository()
    private val userDataRepository = TestUserDataRepository()

    val useCase = GetUserNewsResourcesUseCase(newsRepository, userDataRepository)

    @Test
    fun whenNoFilters_allNewsResourcesAreReturned() = runTest {

        // Obtain the saveable news resources stream.
        val saveableNewsResources = useCase()

        // Send some news resources and bookmarks.
        newsRepository.sendNewsResources(sampleNewsResources)
        userDataRepository.setNewsResourceBookmarks(
            setOf(sampleNewsResources[0].id, sampleNewsResources[2].id)
        )

        // Check that the correct news resources are returned with their bookmarked state.
        assertEquals(
            listOf(
                UserNewsResource(
                    sampleNewsResources[0].id,
                    sampleNewsResources[0].title,
                    sampleNewsResources[0].content,
                    sampleNewsResources[0].url,
                    sampleNewsResources[0].headerImageUrl,
                    sampleNewsResources[0].publishDate,
                    sampleNewsResources[0].type,
                    sampleNewsResources[0].topics.map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = false
                        )
                    },
                    true
                ),
                UserNewsResource(
                    sampleNewsResources[1].id,
                    sampleNewsResources[1].title,
                    sampleNewsResources[1].content,
                    sampleNewsResources[1].url,
                    sampleNewsResources[1].headerImageUrl,
                    sampleNewsResources[1].publishDate,
                    sampleNewsResources[1].type,
                    sampleNewsResources[1].topics.map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = false
                        )
                    },
                    false
                ),
                UserNewsResource(
                    sampleNewsResources[2].id,
                    sampleNewsResources[2].title,
                    sampleNewsResources[2].content,
                    sampleNewsResources[2].url,
                    sampleNewsResources[2].headerImageUrl,
                    sampleNewsResources[2].publishDate,
                    sampleNewsResources[2].type,
                    sampleNewsResources[2].topics.map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = false
                        )
                    },
                    true
                ),
            ),
            saveableNewsResources.first()
        )
    }

    @Test
    fun whenFilteredByTopicId_matchingNewsResourcesAreReturned() = runTest {

        // Obtain a stream of saveable news resources for the given topic id.
        val saveableNewsResources = useCase(filterTopicIds = setOf(sampleTopic1.id))

        // Send some news resources and bookmarks.
        newsRepository.sendNewsResources(sampleNewsResources)
        userDataRepository.setNewsResourceBookmarks(setOf())

        // Check that only news resources with the given topic id are returned.
        assertEquals(
            sampleNewsResources
                .filter { it.topics.contains(sampleTopic1) }
                .map {
                    UserNewsResource(
                        id = it.id,
                        title = it.title,
                        content = it.content,
                        url = it.url,
                        headerImageUrl = it.headerImageUrl,
                        publishDate = it.publishDate,
                        type = it.type,
                        topics = it.topics.map { topic ->
                            FollowableTopic(
                                topic = topic,
                                isFollowed = false
                            )
                        },
                        isSaved = false
                    )
                },
            saveableNewsResources.first()
        )
    }

    //whenTopicFollowed_UserNewsResources
    @Test
    fun checkNewsResourcesContainCorrectFollowedTopics() = runTest {

        // Obtain the saveable news resources stream.
        val saveableNewsResources = useCase()

        // Send some news resources.
        newsRepository.sendNewsResources(sampleNewsResources)

        // Set a followed topic for the user.
        userDataRepository.setFollowedTopicIds(setOf(sampleTopic1.id))

        // Check that the followed topic is marked followed in the UserNewsResources
        assertEquals(
            listOf(
                UserNewsResource(
                    sampleNewsResources[0].id,
                    sampleNewsResources[0].title,
                    sampleNewsResources[0].content,
                    sampleNewsResources[0].url,
                    sampleNewsResources[0].headerImageUrl,
                    sampleNewsResources[0].publishDate,
                    sampleNewsResources[0].type,
                    sampleNewsResources[0].topics.map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = topic.id == sampleTopic1.id
                        )
                    },
                    false
                ),
                UserNewsResource(
                    sampleNewsResources[1].id,
                    sampleNewsResources[1].title,
                    sampleNewsResources[1].content,
                    sampleNewsResources[1].url,
                    sampleNewsResources[1].headerImageUrl,
                    sampleNewsResources[1].publishDate,
                    sampleNewsResources[1].type,
                    sampleNewsResources[1].topics.map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = topic.id == sampleTopic1.id
                        )
                    },
                    false
                ),
                UserNewsResource(
                    sampleNewsResources[2].id,
                    sampleNewsResources[2].title,
                    sampleNewsResources[2].content,
                    sampleNewsResources[2].url,
                    sampleNewsResources[2].headerImageUrl,
                    sampleNewsResources[2].publishDate,
                    sampleNewsResources[2].type,
                    sampleNewsResources[2].topics.map { topic ->
                        FollowableTopic(
                            topic = topic,
                            isFollowed = topic.id == sampleTopic1.id
                        )
                    },
                    false
                ),
            ),
            saveableNewsResources.first()
        )
    }
}

private val sampleTopic1 = Topic(
    id = "Topic1",
    name = "Headlines",
    shortDescription = "",
    longDescription = "long description",
    url = "URL",
    imageUrl = "image URL",
)

private val sampleTopic2 = Topic(
    id = "Topic2",
    name = "UI",
    shortDescription = "",
    longDescription = "long description",
    url = "URL",
    imageUrl = "image URL",
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
        topics = listOf(sampleTopic1)
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
        topics = listOf(sampleTopic1, sampleTopic2)
    ),
    NewsResource(
        id = "3",
        title = "Community tip on Paging",
        content = "Tips for using the Paging library from the developer community",
        url = "https://youtu.be/r5JgIyS3t3s",
        headerImageUrl = "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-08T00:00:00.000Z"),
        type = Video,
        topics = listOf(sampleTopic2)
    ),
)
