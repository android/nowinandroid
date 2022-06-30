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

package com.google.samples.apps.nowinandroid.core.data.repository

import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferencesDataSource
import com.google.samples.apps.nowinandroid.core.datastore.test.testUserPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class OfflineFirstUserDataRepositoryTest {
    private lateinit var subject: OfflineFirstUserDataRepository

    private lateinit var niaPreferencesDataSource: NiaPreferencesDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        niaPreferencesDataSource = NiaPreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore()
        )

        subject = OfflineFirstUserDataRepository(
            niaPreferencesDataSource = niaPreferencesDataSource
        )
    }

    @Test
    fun offlineFirstUserDataRepository_toggle_followed_topics_logic_delegates_to_nia_preferences() =
        runTest {
            subject.toggleFollowedTopicId(followedTopicId = "0", followed = true)

            assertEquals(
                setOf("0"),
                subject.userDataStream
                    .map { it.followedTopics }
                    .first()
            )

            subject.toggleFollowedTopicId(followedTopicId = "1", followed = true)

            assertEquals(
                setOf("0", "1"),
                subject.userDataStream
                    .map { it.followedTopics }
                    .first()
            )

            assertEquals(
                niaPreferencesDataSource.userDataStream
                    .map { it.followedTopics }
                    .first(),
                subject.userDataStream
                    .map { it.followedTopics }
                    .first()
            )
        }

    @Test
    fun offlineFirstUserDataRepository_set_followed_topics_logic_delegates_to_nia_preferences() =
        runTest {
            subject.setFollowedTopicIds(followedTopicIds = setOf("1", "2"))

            assertEquals(
                setOf("1", "2"),
                subject.userDataStream
                    .map { it.followedTopics }
                    .first()
            )

            assertEquals(
                niaPreferencesDataSource.userDataStream
                    .map { it.followedTopics }
                    .first(),
                subject.userDataStream
                    .map { it.followedTopics }
                    .first()
            )
        }

    @Test
    fun offlineFirstUserDataRepository_bookmark_news_resource_logic_delegates_to_nia_preferences() =
        runTest {
            subject.updateNewsResourceBookmark(newsResourceId = "0", bookmarked = true)

            assertEquals(
                setOf("0"),
                subject.userDataStream
                    .map { it.bookmarkedNewsResources }
                    .first()
            )

            subject.updateNewsResourceBookmark(newsResourceId = "1", bookmarked = true)

            assertEquals(
                setOf("0", "1"),
                subject.userDataStream
                    .map { it.bookmarkedNewsResources }
                    .first()
            )

            assertEquals(
                niaPreferencesDataSource.userDataStream
                    .map { it.bookmarkedNewsResources }
                    .first(),
                subject.userDataStream
                    .map { it.bookmarkedNewsResources }
                    .first()
            )
        }
}
