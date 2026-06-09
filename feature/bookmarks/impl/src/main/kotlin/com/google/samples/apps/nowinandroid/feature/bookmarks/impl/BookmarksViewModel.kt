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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.model.data.UserNewsResource
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    userNewsResourceRepository: UserNewsResourceRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarksUiState())
    private var lastRemovedBookmarkId: String? = null
    private var bulkRemoveSnapshot: List<Pair<String, String?>> = emptyList()

    val uiState: StateFlow<BookmarksUiState> = combine(
        userNewsResourceRepository.observeAllBookmarked()
            .map<List<UserNewsResource>, NewsFeedUiState>(NewsFeedUiState::Success)
            .onStart { emit(Loading) },
        _state,
    ) { feedState, state ->
        state.copy(feedState = feedState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookmarksUiState(),
    )

    fun enterSelectionMode(initialId: String) {
        _state.update { it.copy(isInSelectionMode = true, selectedIds = setOf(initialId)) }
    }

    fun exitSelectionMode() {
        _state.update { it.copy(isInSelectionMode = false, selectedIds = emptySet()) }
    }

    fun toggleSelection(id: String) {
        _state.update { current ->
            val updated =
                if (id in current.selectedIds) current.selectedIds - id else current.selectedIds + id
            current.copy(selectedIds = updated)
        }
    }

    fun selectAll() {
        val currentFeed = (uiState.value.feedState as? NewsFeedUiState.Success)?.feed ?: return
        _state.update { it.copy(selectedIds = currentFeed.map { item -> item.id }.toSet()) }
    }

    fun removeFromSavedResources(newsResourceId: String) {
        lastRemovedBookmarkId = newsResourceId
        _state.update { it.copy(shouldDisplayUndoBookmark = true) }
        viewModelScope.launch {
            userDataRepository.setNewsResourceBookmarked(newsResourceId, false)
        }
    }

    fun setNewsResourceViewed(newsResourceId: String, viewed: Boolean) {
        viewModelScope.launch {
            userDataRepository.setNewsResourceViewed(newsResourceId, viewed)
        }
    }

    fun undoBookmarkRemoval() {
        viewModelScope.launch {
            lastRemovedBookmarkId?.let {
                userDataRepository.setNewsResourceBookmarked(it, true)
            }
            clearUndoState()
        }
    }

    fun clearUndoState() {
        lastRemovedBookmarkId = null
        _state.update { it.copy(shouldDisplayUndoBookmark = false) }
    }

    fun removeSelected() {
        val currentFeed = (uiState.value.feedState as? NewsFeedUiState.Success)?.feed ?: return
        val toRemove = _state.value.selectedIds
        bulkRemoveSnapshot = toRemove.map { id ->
            id to currentFeed.find { it.id == id }?.bookmarkNote
        }
        viewModelScope.launch {
            toRemove.forEach { id ->
                userDataRepository.setNewsResourceBookmarked(id, false)
            }
        }
        _state.update {
            it.copy(
                isInSelectionMode = false,
                selectedIds = emptySet(),
                shouldDisplayUndoBulkRemove = true,
                bulkRemovedCount = toRemove.size,
            )
        }
    }

    fun undoBulkRemove() {
        viewModelScope.launch {
            bulkRemoveSnapshot.forEach { (id, note) ->
                userDataRepository.setNewsResourceBookmarked(id, true)
                if (!note.isNullOrBlank()) {
                    userDataRepository.setBookmarkNote(id, note)
                }
            }
            clearBulkUndoState()
        }
    }

    fun clearBulkUndoState() {
        bulkRemoveSnapshot = emptyList()
        _state.update { it.copy(shouldDisplayUndoBulkRemove = false, bulkRemovedCount = 0) }
    }

    fun updateNote(newsResourceId: String, note: String) {
        viewModelScope.launch {
            if (note.isNotBlank()) {
                userDataRepository.setBookmarkNote(newsResourceId, note)
            } else {
                userDataRepository.removeBookmarkNote(newsResourceId)
            }
        }
    }
}
