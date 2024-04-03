/*
 * Copyright 2024 The Android Open Source Project
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

import com.google.samples.apps.nowinandroid.core.testing.repository.TestRecentSearchRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class GetRecentSearchQueriesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val recentSearchRepository = TestRecentSearchRepository()

    private val useCase = GetRecentSearchQueriesUseCase(
        recentSearchRepository,
    )

    @Test
    fun whenNoParams_recentSearchQueriesAreReturnedUpTo10() = runTest {
        // Obtain a stream of recent search queries with no param.
        val recentSearchQueries = useCase()

        // insert 5 search queries.
        for (index in 0 until 5) {
            recentSearchRepository.insertOrReplaceRecentSearch(testRecentSearchQueries[index])
            // delay for saving value
            delay(10L)
        }

        // Check that 5 recent search queries are ordered by latest.
        assertEquals(
            testRecentSearchQueries.take(5).reversed(),
            recentSearchQueries.first().map { it.query },
        )

        // insert 9 more search queries.
        for (index in 5 until testRecentSearchQueries.size) {
            recentSearchRepository.insertOrReplaceRecentSearch(testRecentSearchQueries[index])
            // delay for saving value
            delay(10L)
        }

        // Check that recent search queries are ordered by latest up to 10.
        assertEquals(
            testRecentSearchQueries.reversed().take(10),
            recentSearchQueries.first().map { it.query },
        )
    }

    @Test
    fun whenParamIsSetTo5_recentSearchQueriesAreReturnedUpTo5() = runTest {
        // Obtain a stream of recent search queries with param set 5.
        val recentSearchQueries = useCase(5)

        // insert 2 search queries.
        for (index in 0 until 2) {
            recentSearchRepository.insertOrReplaceRecentSearch(testRecentSearchQueries[index])
            // delay for saving value
            delay(10L)
        }

        // Check that 5 recent search queries are ordered by latest.
        assertEquals(
            testRecentSearchQueries.take(2).reversed(),
            recentSearchQueries.first().map { it.query },
        )

        // insert 12 more search queries.
        for (index in 2 until testRecentSearchQueries.size) {
            recentSearchRepository.insertOrReplaceRecentSearch(testRecentSearchQueries[index])
            // delay for saving value
            delay(10L)
        }

        // Check that recent search queries are ordered by latest up to 5.
        assertEquals(
            testRecentSearchQueries.reversed().take(5),
            recentSearchQueries.first().map { it.query },
        )
    }
}

private val testRecentSearchQueries = listOf(
    "Compose", "Wear OS",
    "Jetpack", "Headlines",
    "Architecture", "UI",
    "Testing", "Android Studio",
    "Performance", "New API",
    "Games", "Android TV",
    "Camera", "Media",
)
