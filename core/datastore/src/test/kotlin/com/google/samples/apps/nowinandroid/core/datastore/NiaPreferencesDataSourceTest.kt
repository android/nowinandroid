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

package com.google.samples.apps.nowinandroid.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.google.samples.apps.nowinandroid.core.datastore.test.InMemoryDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NiaPreferencesDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: NiaPreferencesDataSource

    // A DataStore implementation that throws an IOException when accessed
    private val ioExceptionThrowingDataStore = object : DataStore<UserPreferences> {
        override val data: Flow<UserPreferences>
            get() = flow { throw IOException("Failed to read proto") }

        override suspend fun updateData(transform: suspend (t: UserPreferences) -> UserPreferences): UserPreferences {
            throw Exception("Not needed for this test")
        }
    }

    @Before
    fun setup() {
        subject = NiaPreferencesDataSource(InMemoryDataStore(UserPreferences.getDefaultInstance()))
    }

    @Test
    fun userData_emitDefault_whenDataStoreThrowsIOException() =
        testScope.runTest {
            // Given: NiaPreferencesDataSource with ioException throwing datastore
            val dataSource = NiaPreferencesDataSource(ioExceptionThrowingDataStore)

            // When: Retrieving user data from the data source
            val actualUserData = dataSource.userData.first()

            // Then: The default user data is returned
            assertEquals(subject.userData.first(), actualUserData)
        }

    @Test
    fun shouldHideOnboardingIsFalseByDefault() = testScope.runTest {
        assertFalse(subject.userData.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboardingIsTrueWhenSet() = testScope.runTest {
        subject.setShouldHideOnboarding(true)
        assertTrue(subject.userData.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboarding_unfollowsLastTopic_shouldHideOnboardingIsFalse() =
        testScope.runTest {
            // Given: user completes onboarding by selecting a single topic.
            subject.setTopicIdFollowed("1", true)
            subject.setShouldHideOnboarding(true)

            // When: they unfollow that topic.
            subject.setTopicIdFollowed("1", false)

            // Then: onboarding should be shown again
            assertFalse(subject.userData.first().shouldHideOnboarding)
        }

    @Test
    fun userShouldHideOnboarding_unfollowsAllTopics_shouldHideOnboardingIsFalse() =
        testScope.runTest {
            // Given: user completes onboarding by selecting several topics.
            subject.setFollowedTopicIds(setOf("1", "2"))
            subject.setShouldHideOnboarding(true)

            // When: they unfollow those topics.
            subject.setFollowedTopicIds(emptySet())

            // Then: onboarding should be shown again
            assertFalse(subject.userData.first().shouldHideOnboarding)
        }

    @Test
    fun getChangeListVersions_returnsDefault_whenDataStoreThrowsIOException() =
        testScope.runTest {
            // Given: NiaPreferencesDataSource with ioException throwing datastore
            val dataSource = NiaPreferencesDataSource(ioExceptionThrowingDataStore)

            // When: Retrieving change list versions from the data source
            val actualResult = dataSource.getChangeListVersions()

            // Then: The default value is returned
            assertEquals(subject.getChangeListVersions(), actualResult)
        }

    @Test
    fun shouldUseDynamicColorFalseByDefault() = testScope.runTest {
        assertFalse(subject.userData.first().useDynamicColor)
    }

    @Test
    fun userShouldUseDynamicColorIsTrueWhenSet() = testScope.runTest {
        subject.setDynamicColorPreference(true)
        assertTrue(subject.userData.first().useDynamicColor)
    }
}
