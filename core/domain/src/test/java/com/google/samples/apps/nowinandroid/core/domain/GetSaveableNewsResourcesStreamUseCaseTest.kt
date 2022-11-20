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
import com.google.samples.apps.nowinandroid.core.model.data.fakeAuthor
import com.google.samples.apps.nowinandroid.core.model.data.fakeNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.fakeTopic
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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

private val sampleTopic1 = fakeTopic(id = "1", name = "Headlines")
private val sampleTopic2 = fakeTopic(id = "2", name = "UI")
private val sampleAuthor1 = fakeAuthor(id = "1")
private val sampleAuthor2 = fakeAuthor(id = "2")

private val sampleNewsResources = listOf(
    fakeNewsResource(
        id = "1",
        authors = listOf(sampleAuthor1),
        topics = listOf(sampleTopic1)
    ),
    fakeNewsResource(
        id = "2",
        authors = listOf(sampleAuthor1),
        topics = listOf(sampleTopic1, sampleTopic2)
    ),
    fakeNewsResource(
        id = "3",
        authors = listOf(sampleAuthor2),
        topics = listOf(sampleTopic2)
    ),
)
