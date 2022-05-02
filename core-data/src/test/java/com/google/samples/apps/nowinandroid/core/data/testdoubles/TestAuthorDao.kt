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

package com.google.samples.apps.nowinandroid.core.data.testdoubles

import com.google.samples.apps.nowinandroid.core.database.dao.AuthorDao
import com.google.samples.apps.nowinandroid.core.database.model.AuthorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Test double for [AuthorDao]
 */
class TestAuthorDao : AuthorDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            AuthorEntity(
                id = "1",
                name = "Topic",
                imageUrl = "imageUrl",
                twitter = "twitter",
                mediumPage = "mediumPage",
                bio = "bio",
            )
        )
    )

    override fun getAuthorEntitiesStream(): Flow<List<AuthorEntity>> =
        entitiesStateFlow

    override suspend fun insertOrIgnoreAuthors(authorEntities: List<AuthorEntity>): List<Long> {
        entitiesStateFlow.value = authorEntities
        // Assume no conflicts on insert
        return authorEntities.map { it.id.toLong() }
    }

    override suspend fun updateAuthors(entities: List<AuthorEntity>) {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun deleteAuthors(ids: List<String>) {
        val idSet = ids.toSet()
        entitiesStateFlow.update { entities ->
            entities.filterNot { idSet.contains(it.id) }
        }
    }
}
