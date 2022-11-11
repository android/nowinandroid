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
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class NiaPreferencesDataSourceTest {
    private lateinit var subject: NiaPreferencesDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = NiaPreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore()
        )
    }

    @Test
    fun shouldHideOnboardingIsFalseByDefault() = runTest {
        assertFalse(subject.userDataStream.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboardingIsTrueWhenSet() = runTest {
        subject.setShouldHideOnboarding(true)
        assertTrue(subject.userDataStream.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboarding_unfollowsLastAuthor_shouldHideOnboardingIsFalse() = runTest {

        // Given: user completes onboarding by selecting a single author.
        subject.toggleFollowedAuthorId("1", true)
        subject.setShouldHideOnboarding(true)

        // When: they unfollow that author.
        subject.toggleFollowedAuthorId("1", false)

        // Then: onboarding should be shown again
        assertFalse(subject.userDataStream.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboarding_unfollowsLastTopic_shouldHideOnboardingIsFalse() = runTest {

        // Given: user completes onboarding by selecting a single topic.
        subject.toggleFollowedTopicId("1", true)
        subject.setShouldHideOnboarding(true)

        // When: they unfollow that topic.
        subject.toggleFollowedTopicId("1", false)

        // Then: onboarding should be shown again
        assertFalse(subject.userDataStream.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboarding_unfollowsAllAuthors_shouldHideOnboardingIsFalse() = runTest {

        // Given: user completes onboarding by selecting several authors.
        subject.setFollowedAuthorIds(setOf("1", "2"))
        subject.setShouldHideOnboarding(true)

        // When: they unfollow those authors.
        subject.setFollowedAuthorIds(emptySet())

        // Then: onboarding should be shown again
        assertFalse(subject.userDataStream.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboarding_unfollowsAllTopics_shouldHideOnboardingIsFalse() = runTest {

        // Given: user completes onboarding by selecting several topics.
        subject.setFollowedTopicIds(setOf("1", "2"))
        subject.setShouldHideOnboarding(true)

        // When: they unfollow those topics.
        subject.setFollowedTopicIds(emptySet())

        // Then: onboarding should be shown again
        assertFalse(subject.userDataStream.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnboarding_unfollowsAllTopicsButNotAuthors_shouldHideOnboardingIsTrue() =
        runTest {
            // Given: user completes onboarding by selecting several topics and authors.
            subject.setFollowedTopicIds(setOf("1", "2"))
            subject.setFollowedAuthorIds(setOf("3", "4"))
            subject.setShouldHideOnboarding(true)

            // When: they unfollow just the topics.
            subject.setFollowedTopicIds(emptySet())

            // Then: onboarding should still be dismissed
            assertTrue(subject.userDataStream.first().shouldHideOnboarding)
        }

    @Test
    fun userShouldHideOnboarding_unfollowsAllAuthorsButNotTopics_shouldHideOnboardingIsTrue() =
        runTest {
            // Given: user completes onboarding by selecting several topics and authors.
            subject.setFollowedTopicIds(setOf("1", "2"))
            subject.setFollowedAuthorIds(setOf("3", "4"))
            subject.setShouldHideOnboarding(true)

            // When: they unfollow just the authors.
            subject.setFollowedAuthorIds(emptySet())

            // Then: onboarding should still be dismissed
            assertTrue(subject.userDataStream.first().shouldHideOnboarding)
        }
}
