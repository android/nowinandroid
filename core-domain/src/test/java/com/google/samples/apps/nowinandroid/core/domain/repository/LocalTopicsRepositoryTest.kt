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

package com.google.samples.apps.nowinandroid.core.domain.repository

import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferences
import com.google.samples.apps.nowinandroid.core.datastore.test.testUserPreferencesDataStore
import com.google.samples.apps.nowinandroid.core.domain.model.asEntity
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestNiaNetwork
import com.google.samples.apps.nowinandroid.core.domain.testdoubles.TestTopicDao
import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class LocalTopicsRepositoryTest {

    private lateinit var subject: LocalTopicsRepository

    private lateinit var topicDao: TopicDao

    private lateinit var network: NiANetwork

    private lateinit var niaPreferences: NiaPreferences

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        topicDao = TestTopicDao()
        network = TestNiaNetwork()
        niaPreferences = NiaPreferences(
            tmpFolder.testUserPreferencesDataStore()
        )

        subject = LocalTopicsRepository(
            topicDao = topicDao,
            network = network,
            niaPreferences = niaPreferences
        )
    }

    @Test
    fun localTopicsRepository_topics_stream_is_backed_by_topics_dao() =
        runTest {
            Assert.assertEquals(
                topicDao.getTopicEntitiesStream()
                    .first()
                    .map(TopicEntity::asExternalModel),
                subject.getTopicsStream()
                    .first()
            )
        }

    @Test
    fun localTopicsRepository_news_resources_filtered_stream_is_backed_by_news_resource_dao() =
        runTest {
            Assert.assertEquals(
                niaPreferences.followedTopicIds
                    .first(),
                subject.getFollowedTopicIdsStream()
                    .first()
            )
        }

    @Test
    fun localTopicsRepository_sync_pulls_from_network() =
        runTest {
            subject.sync()

            val network = network.getTopics()
                .map(NetworkTopic::asEntity)

            val db = topicDao.getTopicEntitiesStream()
                .first()

            Assert.assertEquals(
                network.map(TopicEntity::id),
                db.map(TopicEntity::id)
            )
        }

    @Test
    fun localTopicsRepository_toggle_followed_topics_logic_delegates_to_nia_preferences() =
        runTest {
            subject.toggleFollowedTopicId(followedTopicId = 0, followed = true)

            Assert.assertEquals(
                setOf(0),
                subject.getFollowedTopicIdsStream()
                    .first()
            )

            subject.toggleFollowedTopicId(followedTopicId = 1, followed = true)

            Assert.assertEquals(
                setOf(0, 1),
                subject.getFollowedTopicIdsStream()
                    .first()
            )

            Assert.assertEquals(
                niaPreferences.followedTopicIds
                    .first(),
                subject.getFollowedTopicIdsStream()
                    .first()
            )
        }

    @Test
    fun localTopicsRepository_set_followed_topics_logic_delegates_to_nia_preferences() =
        runTest {
            subject.setFollowedTopicIds(followedTopicIds = setOf(1, 2))

            Assert.assertEquals(
                setOf(1, 2),
                subject.getFollowedTopicIdsStream()
                    .first()
            )

            Assert.assertEquals(
                niaPreferences.followedTopicIds
                    .first(),
                subject.getFollowedTopicIdsStream()
                    .first()
            )
        }
}
