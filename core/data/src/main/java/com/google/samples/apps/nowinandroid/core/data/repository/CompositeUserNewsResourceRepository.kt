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

package com.google.samples.apps.nowinandroid.core.data.repository

import com.google.samples.apps.nowinandroid.core.model.data.UserNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.mapToUserNewsResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implements a [UserNewsResourceRepository] by combining a [NewsRepository] with a
 * [UserDataRepository].
 */
class CompositeUserNewsResourceRepository @Inject constructor(
    val newsRepository: NewsRepository,
    val userDataRepository: UserDataRepository,
) : UserNewsResourceRepository {

    /**
     * Returns available news resources (joined with user data) matching the given query.
     */
    override fun observeAll(
        query: NewsResourceQuery,
    ): Flow<List<UserNewsResource>> =
        newsRepository.getNewsResources(query)
            .combine(userDataRepository.userData) { newsResources, userData ->
                newsResources.mapToUserNewsResources(userData)
            }

    /**
     * Returns available news resources (joined with user data) for the followed topics.
     */
    override fun observeAllForFollowedTopics(): Flow<List<UserNewsResource>> =
        userDataRepository.userData.map { it.followedTopics }.distinctUntilChanged()
            .flatMapLatest { followedTopics ->
                when {
                    followedTopics.isEmpty() -> flowOf(emptyList())
                    else -> observeAll(NewsResourceQuery(filterTopicIds = followedTopics))
                }
            }

    override fun observeAllBookmarked(): Flow<List<UserNewsResource>> =
        userDataRepository.userData.map { it.bookmarkedNewsResources }.distinctUntilChanged()
            .flatMapLatest { bookmarkedNewsResources ->
                when {
                    bookmarkedNewsResources.isEmpty() -> flowOf(emptyList())
                    else -> observeAll(NewsResourceQuery(filterNewsIds = bookmarkedNewsResources))
                }
            }
}
