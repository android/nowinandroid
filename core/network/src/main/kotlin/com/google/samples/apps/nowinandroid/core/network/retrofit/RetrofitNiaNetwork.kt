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

package com.google.samples.apps.nowinandroid.core.network.retrofit

import com.google.samples.apps.nowinandroid.core.network.BuildConfig
import com.google.samples.apps.nowinandroid.core.network.NiaNetworkDataSource
import com.google.samples.apps.nowinandroid.core.network.api.NewsResourceApi
import com.google.samples.apps.nowinandroid.core.network.api.TopicApi
import com.google.samples.apps.nowinandroid.core.network.model.NetworkChangeList
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

internal const val NIA_BASE_URL = BuildConfig.BACKEND_URL

/**
 * Wrapper for data provided from the [NIA_BASE_URL]
 */
@Serializable
internal data class NetworkResponse<T>(
    val data: T,
)

/**
 * [Retrofit] backed [NiaNetworkDataSource]
 */
@Singleton
internal class RetrofitNiaNetwork @Inject constructor(
    val newsResourceApi: NewsResourceApi,
    val topicApi: TopicApi,
) : NiaNetworkDataSource {

    override suspend fun getTopics(ids: List<String>?): List<NetworkTopic> =
        topicApi.getTopics(ids = ids).data

    override suspend fun getNewsResources(ids: List<String>?): List<NetworkNewsResource> =
        newsResourceApi.getNewsResources(ids = ids).data

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        topicApi.getTopicChangeList(after = after)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        newsResourceApi.getNewsResourcesChangeList(after = after)
}
