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

package com.google.samples.apps.nowinandroid.core.domain.usecase

import com.google.samples.apps.nowinandroid.core.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.model.SearchResult
import com.google.samples.apps.nowinandroid.core.model.UserData
import com.google.samples.apps.nowinandroid.core.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.model.UserSearchResult
import com.google.samples.apps.nowinandroid.core.domain.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * A use case which returns the searched contents matched with the search query.
 */
class GetSearchContentsUseCase @Inject constructor(
    private val searchContentsRepository: SearchContentsRepository,
    private val userDataRepository: UserDataRepository,
) {

    operator fun invoke(
        searchQuery: String,
    ): Flow<com.google.samples.apps.nowinandroid.core.model.UserSearchResult> =
        searchContentsRepository.searchContents(searchQuery)
            .mapToUserSearchResult(userDataRepository.userData)
}

private fun Flow<com.google.samples.apps.nowinandroid.core.model.SearchResult>.mapToUserSearchResult(userDataStream: Flow<com.google.samples.apps.nowinandroid.core.model.UserData>): Flow<com.google.samples.apps.nowinandroid.core.model.UserSearchResult> =
    combine(userDataStream) { searchResult, userData ->
        com.google.samples.apps.nowinandroid.core.model.UserSearchResult(
            topics = searchResult.topics.map { topic ->
                com.google.samples.apps.nowinandroid.core.model.FollowableTopic(
                    topic = topic,
                    isFollowed = topic.id in userData.followedTopics,
                )
            },
            newsResources = searchResult.newsResources.map { news ->
                com.google.samples.apps.nowinandroid.core.model.UserNewsResource(
                    newsResource = news,
                    userData = userData,
                )
            },
        )
    }
