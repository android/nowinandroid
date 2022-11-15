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

import com.google.samples.apps.nowinandroid.core.domain.model.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Author
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

class GetSaveableNewsResourcesStreamUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val newsRepository = TestNewsRepository()
    private val userDataRepository = TestUserDataRepository()

    val useCase = GetSaveableNewsResourcesStreamUseCase(newsRepository, userDataRepository)

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
                SaveableNewsResource(sampleNewsResources[0], true),
                SaveableNewsResource(sampleNewsResources[1], false),
                SaveableNewsResource(sampleNewsResources[2], true)
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
                .map { SaveableNewsResource(it, false) },
            saveableNewsResources.first()
        )
    }

    @Test
    fun whenFilteredByAuthorId_matchingNewsResourcesAreReturned() = runTest {

        // Obtain a stream of saveable news resources for the given author id.
        val saveableNewsResources = useCase(filterAuthorIds = setOf(sampleAuthor1.id))

        // Send some news resources and bookmarks.
        newsRepository.sendNewsResources(sampleNewsResources)
        userDataRepository.setNewsResourceBookmarks(setOf())

        // Check that only news resources with the given author id are returned.
        assertEquals(
            sampleNewsResources
                .filter { it.authors.contains(sampleAuthor1) }
                .map { SaveableNewsResource(it, false) },
            saveableNewsResources.first()
        )
    }

    @Test
    fun whenFilteredByAuthorIdAndTopicId_matchingNewsResourcesAreReturned() = runTest {

        // Obtain a stream of saveable news resources for the given author id.
        val saveableNewsResources = useCase(
            filterAuthorIds = setOf(sampleAuthor2.id),
            filterTopicIds = setOf(sampleTopic2.id),
        )

        // Send some news resources and bookmarks.
        newsRepository.sendNewsResources(sampleNewsResources)
        userDataRepository.setNewsResourceBookmarks(setOf())

        // Check that only news resources with the given author id or topic id are returned.
        assertEquals(
            sampleNewsResources
                .filter { it.authors.contains(sampleAuthor2) || it.topics.contains(sampleTopic2) }
                .map { SaveableNewsResource(it, false) },
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

private val sampleAuthor1 =
    Author(
        id = "Author1",
        name = "Android Dev",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
    )

private val sampleAuthor2 =
    Author(
        id = "Author2",
        name = "Android Dev",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
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
        topics = listOf(sampleTopic1),
        authors = listOf(sampleAuthor1)
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
        topics = listOf(sampleTopic1, sampleTopic2),
        authors = listOf(sampleAuthor1)
    ),
    NewsResource(
        id = "3",
        title = "Community tip on Paging",
        content = "Tips for using the Paging library from the developer community",
        url = "https://youtu.be/r5JgIyS3t3s",
        headerImageUrl = "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-08T00:00:00.000Z"),
        type = Video,
        topics = listOf(sampleTopic2),
        authors = listOf(sampleAuthor2)
    ),
)
