/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.data

import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.repository.NewsResourceQuery
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.model.data.mapToUserNewsResources
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.emptyUserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class CompositeUserNewsResourceRepositoryTest {

    private val newsRepository = TestNewsRepository()
    private val userDataRepository = TestUserDataRepository()

    private val userNewsResourceRepository = CompositeUserNewsResourceRepository(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository,
    )

    @Test
    fun whenNoFilters_allNewsResourcesAreReturned() = runTest {
        // Obtain the user news resources flow.
        val userNewsResources = userNewsResourceRepository.observeAll()

        // Send some news resources and user data into the data repositories.
        newsRepository.sendNewsResources(sampleNewsResources)

        // Construct the test user data with bookmarks and followed topics.
        val userData = emptyUserData.copy(
            bookmarkedNewsResources = setOf(sampleNewsResources[0].id, sampleNewsResources[2].id),
            followedTopics = setOf(sampleTopic1.id),
        )

        userDataRepository.setUserData(userData)

        // Check that the correct news resources are returned with their bookmarked state.
        assertEquals(
            sampleNewsResources.mapToUserNewsResources(userData),
            userNewsResources.first(),
        )
    }

    @Test
    fun whenFilteredByTopicId_matchingNewsResourcesAreReturned() = runTest {
        // Obtain a stream of user news resources for the given topic id.
        val userNewsResources =
            userNewsResourceRepository.observeAll(
                NewsResourceQuery(
                    filterTopicIds = setOf(
                        sampleTopic1.id,
                    ),
                ),
            )

        // Send test data into the repositories.
        newsRepository.sendNewsResources(sampleNewsResources)
        userDataRepository.setUserData(emptyUserData)

        // Check that only news resources with the given topic id are returned.
        assertEquals(
            sampleNewsResources
                .filter { sampleTopic1 in it.topics }
                .mapToUserNewsResources(emptyUserData),
            userNewsResources.first(),
        )
    }

    @Test
    fun whenFilteredByFollowedTopics_matchingNewsResourcesAreReturned() = runTest {
        // Obtain a stream of user news resources for the given topic id.
        val userNewsResources =
            userNewsResourceRepository.observeAllForFollowedTopics()

        // Send test data into the repositories.
        val userData = emptyUserData.copy(
            followedTopics = setOf(sampleTopic1.id),
        )
        newsRepository.sendNewsResources(sampleNewsResources)
        userDataRepository.setUserData(userData)

        // Check that only news resources with the given topic id are returned.
        assertEquals(
            sampleNewsResources
                .filter { sampleTopic1 in it.topics }
                .mapToUserNewsResources(userData),
            userNewsResources.first(),
        )
    }

    @Test
    fun whenFilteredByBookmarkedResources_matchingNewsResourcesAreReturned() = runTest {
        // Obtain the bookmarked user news resources flow.
        val userNewsResources = userNewsResourceRepository.observeAllBookmarked()

        // Send some news resources and user data into the data repositories.
        newsRepository.sendNewsResources(sampleNewsResources)

        // Construct the test user data with bookmarks and followed topics.
        val userData = emptyUserData.copy(
            bookmarkedNewsResources = setOf(sampleNewsResources[0].id, sampleNewsResources[2].id),
            followedTopics = setOf(sampleTopic1.id),
        )

        userDataRepository.setUserData(userData)

        // Check that the correct news resources are returned with their bookmarked state.
        assertEquals(
            listOf(sampleNewsResources[0], sampleNewsResources[2]).mapToUserNewsResources(userData),
            userNewsResources.first(),
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
        type = "Video 📺",
        topics = listOf(sampleTopic1),
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
        type = "Video 📺",
        topics = listOf(sampleTopic1, sampleTopic2),
    ),
    NewsResource(
        id = "3",
        title = "Community tip on Paging",
        content = "Tips for using the Paging library from the developer community",
        url = "https://youtu.be/r5JgIyS3t3s",
        headerImageUrl = "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-08T00:00:00.000Z"),
        type = "Video 📺",
        topics = listOf(sampleTopic2),
    ),
)
