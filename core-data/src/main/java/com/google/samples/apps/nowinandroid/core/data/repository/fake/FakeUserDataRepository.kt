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

package com.google.samples.apps.nowinandroid.core.data.repository.fake

import com.google.samples.apps.nowinandroid.core.data.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferencesDataSource
import com.google.samples.apps.nowinandroid.core.model.data.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Fake implementation of the [AuthorsRepository] that returns hardcoded authors.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeUserDataRepository @Inject constructor(
    private val niaPreferencesDataSource: NiaPreferencesDataSource,
) : UserDataRepository {

    override val userDataStream: Flow<UserData> =
        niaPreferencesDataSource.userDataStream

    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) =
        niaPreferencesDataSource.setFollowedTopicIds(followedTopicIds)

    override suspend fun toggleFollowedTopicId(followedTopicId: String, followed: Boolean) =
        niaPreferencesDataSource.toggleFollowedTopicId(followedTopicId, followed)

    override suspend fun setFollowedAuthorIds(followedAuthorIds: Set<String>) {
        niaPreferencesDataSource.setFollowedAuthorIds(followedAuthorIds)
    }

    override suspend fun toggleFollowedAuthorId(followedAuthorId: String, followed: Boolean) {
        niaPreferencesDataSource.toggleFollowedAuthorId(followedAuthorId, followed)
    }

    override suspend fun updateNewsResourceBookmark(newsResourceId: String, bookmarked: Boolean) {
        niaPreferencesDataSource.toggleNewsResourceBookmark(newsResourceId, bookmarked)
    }
}
