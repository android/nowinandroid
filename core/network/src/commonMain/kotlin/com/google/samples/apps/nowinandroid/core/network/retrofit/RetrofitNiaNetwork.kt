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

import com.google.samples.apps.nowinandroid.core.network.NiaNetworkDataSource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkChangeList
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.Serializable

/**
 * Retrofit API declaration for NIA Network API
 */
internal interface RetrofitNiaNetworkApi {
    @GET(value = "topics")
    suspend fun getTopics(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkTopic>>

    @GET(value = "newsresources")
    suspend fun getNewsResources(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkNewsResource>>

    @GET(value = "changelists/topics")
    suspend fun getTopicChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/newsresources")
    suspend fun getNewsResourcesChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>
}

/**
 * Wrapper for data provided from the [NIA_BASE_URL]
 */
@Serializable
internal data class NetworkResponse<T>(
    val data: T,
)

/**
 * [Ktrofit] backed [NiaNetworkDataSource]
 */
internal class RetrofitNiaNetwork(
    ktorfit: Ktorfit,
) : NiaNetworkDataSource {

    private val networkApi: RetrofitNiaNetworkApi = ktorfit.createRetrofitNiaNetworkApi()

    override suspend fun getTopics(ids: List<String>?): List<NetworkTopic> =
        networkApi.getTopics(ids = ids).data

    override suspend fun getNewsResources(ids: List<String>?): List<NetworkNewsResource> =
        networkApi.getNewsResources(ids = ids).data

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getTopicChangeList(after = after)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getNewsResourcesChangeList(after = after)
}
