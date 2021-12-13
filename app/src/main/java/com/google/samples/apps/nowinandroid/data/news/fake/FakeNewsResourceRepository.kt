/*
 * Copyright 2021 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.data.news.fake

import com.google.samples.apps.nowinandroid.data.news.NewsResource
import com.google.samples.apps.nowinandroid.data.news.NewsResourceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * [NewsResourceRepository] implementation that provides static news resources to aid development
 */

class FakeNewsResourceRepository(
    private val ioDispatcher: CoroutineDispatcher
) : NewsResourceRepository {
    private val deserializer = Json { ignoreUnknownKeys = true }

    override fun monitor(): Flow<List<NewsResource>> = flow {
        emit(deserializer.decodeFromString<ResourceData>(FakeDataSource.data).resources)
    }
        .flowOn(ioDispatcher)
}

/**
 * Representation of resources aas fetched from [FakeDataSource]
 */
@Serializable
private data class ResourceData(
    val resources: List<NewsResource>
)
