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

import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.testing.repository.TestAuthorsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GetPersistentSortedFollowableAuthorsStreamUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authorsRepository = TestAuthorsRepository()
    private val userDataRepository = TestUserDataRepository()

    val useCase = GetPersistentSortedFollowableAuthorsStreamUseCase(
        authorsRepository = authorsRepository,
        userDataRepository = userDataRepository
    )

    @Test
    fun whenFollowedAuthorsSupplied_sortedFollowableAuthorsAreReturned() = runTest {

        // Obtain the stream of authors.
        val followableAuthorsStream = useCase()

        // Supply some authors and their followed state in user data.
        authorsRepository.sendAuthors(sampleAuthors)
        userDataRepository.setFollowedAuthorIds(setOf(sampleAuthor1.id, sampleAuthor3.id))

        // Check that the authors have been sorted, and that the followed state is correct.
        assertEquals(
            listOf(
                FollowableAuthor(sampleAuthor2, false),
                FollowableAuthor(sampleAuthor1, true),
                FollowableAuthor(sampleAuthor3, true)
            ),
            followableAuthorsStream.first()
        )
    }
}

private val sampleAuthor1 =
    Author(
        id = "Author1",
        name = "Mandy",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
    )

private val sampleAuthor2 =
    Author(
        id = "Author2",
        name = "Andy",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
    )

private val sampleAuthor3 =
    Author(
        id = "Author3",
        name = "Sandy",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
    )

private val sampleAuthors = listOf(sampleAuthor1, sampleAuthor2, sampleAuthor3)
