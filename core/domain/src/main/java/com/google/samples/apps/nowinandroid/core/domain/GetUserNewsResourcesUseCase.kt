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

package com.google.samples.apps.nowinandroid.core.domain

import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.domain.model.mapToUserNewsResources
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

/**
 * A use case responsible for obtaining news resources with their associated bookmarked (also known
 * as "saved") state.
 */
class GetUserNewsResourcesUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userDataRepository: UserDataRepository,
) {
    /**
     * Returns a list of UserNewsResources which match the supplied set of topic ids.
     *
     * @param filterTopicIds - A set of topic ids used to filter the list of news resources. If
     * this is empty the list of news resources will not be filtered.
     */
    operator fun invoke(
        filterTopicIds: Set<String> = emptySet(),
    ): Flow<List<UserNewsResource>> =
        if (filterTopicIds.isEmpty()) {
            newsRepository.getNewsResources()
        } else {
            newsRepository.getNewsResources(filterTopicIds = filterTopicIds)
        }.mapToUserNewsResources(userDataRepository.userData)
}

private fun Flow<List<NewsResource>>.mapToUserNewsResources(
    userDataStream: Flow<UserData>,
): Flow<List<UserNewsResource>> =
    filterNot { it.isEmpty() }
        .combine(userDataStream) { newsResources, userData ->
            newsResources.mapToUserNewsResources(userData)
        }
