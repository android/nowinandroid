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

import com.google.samples.apps.nowinandroid.core.data.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

/**
 * A use case which obtains a sorted list of authors with their followed state obtained from user
 * data.
 */
class GetPersistentSortedFollowableAuthorsStreamUseCase @Inject constructor(
    authorsRepository: AuthorsRepository,
    private val userDataRepository: UserDataRepository
) {
    private val getSortedFollowableAuthorsStream =
        GetSortedFollowableAuthorsStreamUseCase(authorsRepository)

    /**
     * Returns a list of authors with their associated followed state sorted alphabetically by name.
     */
    operator fun invoke(): Flow<List<FollowableAuthor>> {
        return userDataRepository.userDataStream.map { userdata ->
            userdata.followedAuthors
        }.flatMapLatest {
            getSortedFollowableAuthorsStream(it)
        }
    }
}
