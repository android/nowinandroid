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

package com.google.samples.apps.nowinandroid.core.domain.repository

import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.NewsResourceQuery
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.domain.di.ApplicationScope
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.domain.model.mapToUserNewsResources
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

/**
 * Implements a [UserNewsResourceRepository] by combining a [NewsRepository] with a
 * [UserDataRepository].
 */
class CompositeUserNewsResourceRepository @Inject constructor(
    @ApplicationScope private val coroutineScope: CoroutineScope,
    val newsRepository: NewsRepository,
    val userDataRepository: UserDataRepository,
) : UserNewsResourceRepository {

    private val userNewsResources =
        newsRepository.getNewsResources().mapToUserNewsResources(userDataRepository.userData)
            .shareIn(coroutineScope, started = WhileSubscribed(5000), replay = 1)

    override fun getUserNewsResources(
        query: NewsResourceQuery,
    ): Flow<List<UserNewsResource>> =
        userNewsResources.map { resources ->
            resources.filter { resource ->
                query.filterTopicIds?.let { topics -> resource.hasTopic(topics) } ?: true &&
                    query.filterNewsIds?.contains(resource.id) ?: true
            }
        }

    override fun getUserNewsResourcesForFollowedTopics(): Flow<List<UserNewsResource>> =
        userDataRepository.userData.flatMapLatest { getUserNewsResources(NewsResourceQuery(filterTopicIds = it.followedTopics)) }

    private fun UserNewsResource.hasTopic(filterTopicIds: Set<String>) =
        followableTopics.any { filterTopicIds.contains(it.topic.id) }
}

private fun Flow<List<NewsResource>>.mapToUserNewsResources(
    userDataStream: Flow<UserData>,
): Flow<List<UserNewsResource>> =
    filterNot { it.isEmpty() }
        .combine(userDataStream) { newsResources, userData ->
            newsResources.mapToUserNewsResources(userData)
        }
