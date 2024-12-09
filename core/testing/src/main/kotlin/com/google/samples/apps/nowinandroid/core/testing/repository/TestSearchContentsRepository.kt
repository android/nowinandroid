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

package com.google.samples.apps.nowinandroid.core.testing.repository

import com.google.samples.apps.nowinandroid.core.data.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.SearchResult
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import org.jetbrains.annotations.TestOnly

class TestSearchContentsRepository : SearchContentsRepository {

    private val cachedTopics = MutableStateFlow(emptyList<Topic>())
    private val cachedNewsResources = MutableStateFlow(emptyList<NewsResource>())

    override suspend fun populateFtsData() = Unit

    override fun searchContents(searchQuery: String): Flow<SearchResult> =
        combine(cachedTopics, cachedNewsResources) { topics, news ->
            SearchResult(
                topics = topics.filter {
                    searchQuery in it.name || searchQuery in it.shortDescription || searchQuery in it.longDescription
                },
                newsResources = news.filter {
                    searchQuery in it.content || searchQuery in it.title
                },
            )
        }

    override fun getSearchContentsCount(): Flow<Int> = combine(cachedTopics, cachedNewsResources) { topics, news -> topics.size + news.size }

    @TestOnly
    fun addTopics(topics: List<Topic>) = cachedTopics.update { it + topics }

    @TestOnly
    fun addNewsResources(newsResources: List<NewsResource>) =
        cachedNewsResources.update { it + newsResources }
}
