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

package com.google.samples.apps.nowinandroid.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.domain.GetUserNewsResourcesUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    getSaveableNewsResources: GetUserNewsResourcesUseCase,
) : ViewModel() {

    val feedUiState: StateFlow<NewsFeedUiState> = getSaveableNewsResources()
        .filterNot { it.isEmpty() }
        .map { newsResources -> newsResources.filter(UserNewsResource::isSaved) } // Only show bookmarked news resources.
        .map<List<UserNewsResource>, NewsFeedUiState>(NewsFeedUiState::Success)
        .onStart { emit(Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Loading,
        )

    fun removeFromSavedResources(newsResourceId: String) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, false)
        }
    }
}
