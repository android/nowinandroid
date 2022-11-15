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
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class GetSortedFollowableAuthorsStreamUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authorsRepository = TestAuthorsRepository()
    private val userDataRepository = TestUserDataRepository()

    val useCase = GetSortedFollowableAuthorsStreamUseCase(
        authorsRepository = authorsRepository,
        userDataRepository = userDataRepository
    )

    @Test
    fun whenFollowedAuthorsSupplied_sortedFollowableAuthorsAreReturned() = runTest {

        // Specify some authors which the user is following.
        userDataRepository.setFollowedAuthorIds(setOf(sampleAuthor1.id))

        // Obtain the stream of authors, specifying their followed state.
        val followableAuthorsStream = useCase()

        // Supply some authors.
        authorsRepository.sendAuthors(sampleAuthors)

        // Check that the authors have been sorted, and that the followed state is correct.
        assertEquals(
            followableAuthorsStream.first(),
            listOf(
                FollowableAuthor(sampleAuthor2, false),
                FollowableAuthor(sampleAuthor1, true),
                FollowableAuthor(sampleAuthor3, false)
            )
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
        id = "Author2",
        name = "Sandy",
        imageUrl = "",
        twitter = "",
        mediumPage = "",
        bio = "",
    )

private val sampleAuthors = listOf(sampleAuthor1, sampleAuthor2, sampleAuthor3)
