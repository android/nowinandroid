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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.domain.GetSearchContentsUseCase
import com.google.samples.apps.nowinandroid.core.result.Result
import com.google.samples.apps.nowinandroid.core.result.asResult
import com.google.samples.apps.nowinandroid.feature.search.SearchResultUiState.LoadFailed
import com.google.samples.apps.nowinandroid.feature.search.SearchResultUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    // TODO: Add GetSearchContentsCountUseCase to check if the fts tables are populated
    getSearchContentsUseCase: GetSearchContentsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

    val searchResultUiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            if (query.length < SEARCH_QUERY_MIN_LENGTH) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                getSearchContentsUseCase(query).asResult().map {
                    when (it) {
                        is Result.Success -> {
                            SearchResultUiState.Success(
                                topics = it.data.topics,
                                newsResources = it.data.newsResources,
                            )
                        }

                        is Result.Loading -> {
                            Loading
                        }

                        is Result.Error -> {
                            LoadFailed
                        }
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchResultUiState.Loading,
        )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }
}

/** Minimum length where search query is considered as [SearchResultUiState.EmptyQuery] */
const val SEARCH_QUERY_MIN_LENGTH = 2
const val SEARCH_QUERY = "searchQuery"
