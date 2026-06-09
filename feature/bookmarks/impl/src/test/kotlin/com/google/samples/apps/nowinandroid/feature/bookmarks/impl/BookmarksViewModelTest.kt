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

package com.google.samples.apps.nowinandroid.feature.bookmarks.impl

import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.testing.data.newsResourcesTestData
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState.Loading
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState.Success
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 */
class BookmarksViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()
    private val newsRepository = TestNewsRepository()
    private val userNewsResourceRepository = CompositeUserNewsResourceRepository(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository,
    )
    private lateinit var viewModel: BookmarksViewModel

    @Before
    fun setup() {
        viewModel = BookmarksViewModel(
            userDataRepository = userDataRepository,
            userNewsResourceRepository = userNewsResourceRepository,
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(Loading, viewModel.uiState.value.feedState)
    }

    @Test
    fun oneBookmark_showsInFeed() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        newsRepository.sendNewsResources(newsResourcesTestData)
        userDataRepository.setNewsResourceBookmarked(newsResourcesTestData[0].id, true)
        val feedState = viewModel.uiState.value.feedState
        assertIs<Success>(feedState)
        assertEquals(feedState.feed.size, 1)
    }

    @Test
    fun oneBookmark_whenRemoving_removesFromFeed() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        // Set the news resources to be used by this test
        newsRepository.sendNewsResources(newsResourcesTestData)
        // Start with the resource saved
        userDataRepository.setNewsResourceBookmarked(newsResourcesTestData[0].id, true)
        // Use viewModel to remove saved resource
        viewModel.removeFromSavedResources(newsResourcesTestData[0].id)
        // Verify list of saved resources is now empty
        val feedState = viewModel.uiState.value.feedState
        assertIs<Success>(feedState)
        assertEquals(feedState.feed.size, 0)
        assertTrue(viewModel.uiState.value.shouldDisplayUndoBookmark)
    }

    @Test
    fun feedUiState_resourceIsViewed_setResourcesViewed() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        // Given
        newsRepository.sendNewsResources(newsResourcesTestData)
        userDataRepository.setNewsResourceBookmarked(newsResourcesTestData[0].id, true)
        val feedStateBeforeViewed = viewModel.uiState.value.feedState
        assertIs<Success>(feedStateBeforeViewed)
        assertFalse(feedStateBeforeViewed.feed.first().hasBeenViewed)

        // When
        viewModel.setNewsResourceViewed(newsResourcesTestData[0].id, true)

        // Then
        val feedState = viewModel.uiState.value.feedState
        assertIs<Success>(feedState)
        assertTrue(feedState.feed.first().hasBeenViewed)
    }

    @Test
    fun enterSelectionMode_setsSelectedId() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.enterSelectionMode("news1")
        assertTrue(viewModel.uiState.value.isInSelectionMode)
        assertEquals(setOf("news1"), viewModel.uiState.value.selectedIds)
    }

    @Test
    fun toggleSelection_addsAndRemovesId() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.enterSelectionMode("news1")
        viewModel.toggleSelection("news2")
        assertEquals(setOf("news1", "news2"), viewModel.uiState.value.selectedIds)
        viewModel.toggleSelection("news1")
        assertEquals(setOf("news2"), viewModel.uiState.value.selectedIds)
    }

    @Test
    fun exitSelectionMode_clearsState() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.enterSelectionMode("news1")
        viewModel.exitSelectionMode()
        assertFalse(viewModel.uiState.value.isInSelectionMode)
        assertTrue(viewModel.uiState.value.selectedIds.isEmpty())
    }

    @Test
    fun removeSelected_capturesSnapshotBeforeRemoval() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        newsRepository.sendNewsResources(newsResourcesTestData)
        userDataRepository.setNewsResourceBookmarked(newsResourcesTestData[0].id, true)
        userDataRepository.setNewsResourceBookmarked(newsResourcesTestData[1].id, true)

        viewModel.enterSelectionMode(newsResourcesTestData[0].id)
        viewModel.toggleSelection(newsResourcesTestData[1].id)

        viewModel.removeSelected()

        assertTrue(viewModel.uiState.value.shouldDisplayUndoBulkRemove)
        assertFalse(viewModel.uiState.value.isInSelectionMode)
        assertTrue(viewModel.uiState.value.selectedIds.isEmpty())
    }

    @Test
    fun feedUiState_undoneBookmarkRemoval_bookmarkIsRestored() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        // Given
        newsRepository.sendNewsResources(newsResourcesTestData)
        userDataRepository.setNewsResourceBookmarked(newsResourcesTestData[0].id, true)
        viewModel.removeFromSavedResources(newsResourcesTestData[0].id)
        assertTrue(viewModel.uiState.value.shouldDisplayUndoBookmark)
        val feedStateBeforeUndo = viewModel.uiState.value.feedState
        assertIs<Success>(feedStateBeforeUndo)
        assertEquals(0, feedStateBeforeUndo.feed.size)

        // When
        viewModel.undoBookmarkRemoval()

        // Then
        assertFalse(viewModel.uiState.value.shouldDisplayUndoBookmark)
        val feedState = viewModel.uiState.value.feedState
        assertIs<Success>(feedState)
        assertEquals(1, feedState.feed.size)
    }
}
