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

package com.google.samples.apps.nowinandroid.data.fake

import com.google.samples.apps.nowinandroid.data.network.NetworkNewsResource
import com.google.samples.apps.nowinandroid.data.network.NetworkTopic
import com.google.samples.apps.nowinandroid.data.network.NiANetwork
import com.google.samples.apps.nowinandroid.di.NiaDispatchers
import javax.inject.Inject
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * [NiANetwork] implementation that provides static news resources to aid development
 */
class FakeNiANetwork @Inject constructor(
    private val dispatchers: NiaDispatchers,
    private val networkJson: Json
) : NiANetwork {
    override suspend fun getTopics(): List<NetworkTopic> =
        withContext(dispatchers.IO) {
            networkJson.decodeFromString(FakeDataSource.topicsData)
        }

    override suspend fun getNewsResources(): List<NetworkNewsResource> =
        withContext(dispatchers.IO) {
            networkJson.decodeFromString<ResourceData>(FakeDataSource.data).resources
        }
}

/**
 * Representation of resources as fetched from [FakeDataSource]
 */
@Serializable
private data class ResourceData(
    val resources: List<NetworkNewsResource>
)
