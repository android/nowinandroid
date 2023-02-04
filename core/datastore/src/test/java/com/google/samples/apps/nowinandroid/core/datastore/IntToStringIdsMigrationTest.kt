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

import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit test for [IntToStringIdsMigration]
 */
class IntToStringIdsMigrationTest {

    @Test
    fun IntToStringIdsMigration_should_migrate_topic_ids() = runTest {
        // Set up existing preferences with topic int ids
        val preMigrationUserPreferences = userPreferences {
            deprecatedIntFollowedTopicIds.addAll(listOf(1, 2, 3))
        }
        // Assert that there are no string topic ids yet
        assertEquals(
            emptyList<String>(),
            preMigrationUserPreferences.deprecatedFollowedTopicIdsList,
        )

        // Run the migration
        val postMigrationUserPreferences =
            IntToStringIdsMigration.migrate(preMigrationUserPreferences)

        // Assert the deprecated int topic ids have been migrated to the string topic ids
        assertEquals(
            userPreferences {
                deprecatedFollowedTopicIds.addAll(listOf("1", "2", "3"))
                hasDoneIntToStringIdMigration = true
            },
            postMigrationUserPreferences,
        )

        // Assert that the migration has been marked complete
        assertTrue(postMigrationUserPreferences.hasDoneIntToStringIdMigration)
    }

    @Test
    fun IntToStringIdsMigration_should_migrate_author_ids() = runTest {
        // Set up existing preferences with author int ids
        val preMigrationUserPreferences = userPreferences {
            deprecatedIntFollowedAuthorIds.addAll(listOf(4, 5, 6))
        }
        // Assert that there are no string author ids yet
        assertEquals(
            emptyList<String>(),
            preMigrationUserPreferences.deprecatedFollowedAuthorIdsList,
        )

        // Run the migration
        val postMigrationUserPreferences =
            IntToStringIdsMigration.migrate(preMigrationUserPreferences)

        // Assert the deprecated int author ids have been migrated to the string author ids
        assertEquals(
            userPreferences {
                deprecatedFollowedAuthorIds.addAll(listOf("4", "5", "6"))
                hasDoneIntToStringIdMigration = true
            },
            postMigrationUserPreferences,
        )

        // Assert that the migration has been marked complete
        assertTrue(postMigrationUserPreferences.hasDoneIntToStringIdMigration)
    }
}
