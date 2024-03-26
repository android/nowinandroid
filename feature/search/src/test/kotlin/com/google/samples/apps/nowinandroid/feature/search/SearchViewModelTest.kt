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

package com.google.samples.apps.nowinandroid.feature.search

import androidx.lifecycle.SavedStateHandle
import com.google.samples.apps.nowinandroid.core.analytics.NoOpAnalyticsHelper
import com.google.samples.apps.nowinandroid.core.domain.GetRecentSearchQueriesUseCase
import com.google.samples.apps.nowinandroid.core.domain.GetSearchContentsUseCase
import com.google.samples.apps.nowinandroid.core.testing.data.newsResourcesTestData
import com.google.samples.apps.nowinandroid.core.testing.data.topicsTestData
import com.google.samples.apps.nowinandroid.core.testing.repository.TestRecentSearchRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestSearchContentsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.emptyUserData
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.feature.search.RecentSearchQueriesUiState.Success
import com.google.samples.apps.nowinandroid.feature.search.SearchResultUiState.EmptyQuery
import com.google.samples.apps.nowinandroid.feature.search.SearchResultUiState.Loading
import com.google.samples.apps.nowinandroid.feature.search.SearchResultUiState.SearchNotReady
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 */
class SearchViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()
    private val searchContentsRepository = TestSearchContentsRepository()
    private val getSearchContentsUseCase = GetSearchContentsUseCase(
        searchContentsRepository = searchContentsRepository,
        userDataRepository = userDataRepository,
    )
    private val recentSearchRepository = TestRecentSearchRepository()
    private val getRecentQueryUseCase = GetRecentSearchQueriesUseCase(recentSearchRepository)

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        viewModel = SearchViewModel(
            getSearchContentsUseCase = getSearchContentsUseCase,
            recentSearchQueriesUseCase = getRecentQueryUseCase,
            searchContentsRepository = searchContentsRepository,
            savedStateHandle = SavedStateHandle(),
            recentSearchRepository = recentSearchRepository,
            userDataRepository = userDataRepository,
            analyticsHelper = NoOpAnalyticsHelper(),
        )
        userDataRepository.setUserData(emptyUserData)
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(Loading, viewModel.searchResultUiState.value)
    }

    @Test
    fun stateIsEmptyQuery_withEmptySearchQuery() = runTest {
        searchContentsRepository.addNewsResources(newsResourcesTestData)
        searchContentsRepository.addTopics(topicsTestData)
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.searchResultUiState.collect() }

        viewModel.onSearchQueryChanged("")

        assertEquals(EmptyQuery, viewModel.searchResultUiState.value)

        collectJob.cancel()
    }

    @Test
    fun emptyResultIsReturned_withNotMatchingQuery() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.searchResultUiState.collect() }

        viewModel.onSearchQueryChanged("XXX")
        searchContentsRepository.addNewsResources(newsResourcesTestData)
        searchContentsRepository.addTopics(topicsTestData)

        val result = viewModel.searchResultUiState.value
        assertIs<SearchResultUiState.Success>(result)

        collectJob.cancel()
    }

    @Test
    fun recentSearches_verifyUiStateIsSuccess() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.recentSearchQueriesUiState.collect() }
        viewModel.onSearchTriggered("kotlin")

        val result = viewModel.recentSearchQueriesUiState.value
        assertIs<Success>(result)

        collectJob.cancel()
    }

    @Test
    fun searchNotReady_withNoFtsTableEntity() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.searchResultUiState.collect() }

        viewModel.onSearchQueryChanged("")

        assertEquals(SearchNotReady, viewModel.searchResultUiState.value)

        collectJob.cancel()
    }

    @Test
    fun whenToggleNewsResourceSavedIsCalled_bookmarkStateIsUpdated() = runTest {
        val newsResourceId = "123"
        viewModel.setNewsResourceBookmarked(newsResourceId, true)

        assertEquals(
            expected = setOf(newsResourceId),
            actual = userDataRepository.userData.first().bookmarkedNewsResources,
        )

        viewModel.setNewsResourceBookmarked(newsResourceId, false)

        assertEquals(
            expected = emptySet(),
            actual = userDataRepository.userData.first().bookmarkedNewsResources,
        )
    }
}
