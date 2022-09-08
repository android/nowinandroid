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

package com.google.samples.apps.nowinandroid.core.testing.repository

import com.google.samples.apps.nowinandroid.core.data.Synchronizer
import com.google.samples.apps.nowinandroid.core.data.repository.AuthorsRepository
import com.google.samples.apps.nowinandroid.core.model.data.Author
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestAuthorsRepository : AuthorsRepository {
    /**
     * The backing hot flow for the list of author ids for testing.
     */
    private val authorsFlow: MutableSharedFlow<List<Author>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getAuthorsStream(): Flow<List<Author>> = authorsFlow

    override fun getAuthorStream(id: String): Flow<Author> {
        return authorsFlow.map { authors -> authors.find { it.id == id }!! }
    }

    override suspend fun syncWith(synchronizer: Synchronizer) = true

    /**
     * A test-only API to allow controlling the list of authors from tests.
     */
    fun sendAuthors(authors: List<Author>) {
        authorsFlow.tryEmit(authors)
    }
}
