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

import com.google.samples.apps.nowinandroid.core.datastore.test.testUserPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NiaPreferencesDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: NiaPreferencesDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = NiaPreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore(testScope),
        )
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
    fun shouldUseDynamicColorFalseByDefault() = testScope.runTest {
        assertFalse(subject.userData.first().useDynamicColor)
    }

    @Test
    fun userShouldUseDynamicColorIsTrueWhenSet() = testScope.runTest {
        subject.setDynamicColorPreference(true)
        assertTrue(subject.userData.first().useDynamicColor)
    }
}
