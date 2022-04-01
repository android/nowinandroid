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

import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.model.NetworkAuthor
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API declaration for NIA Network API
 */
private interface RetrofitNiANetworkApi {
    @GET(value = "topics")
    suspend fun getTopics(
        @Query("pageSize") itemsPerPage: Int,
    ): NetworkResponse<List<NetworkTopic>>

    @GET(value = "authors")
    suspend fun getAuthors(
        @Query("pageSize") itemsPerPage: Int,
    ): NetworkResponse<List<NetworkAuthor>>

    @GET(value = "newsresources")
    suspend fun getNewsResources(
        @Query("pageSize") itemsPerPage: Int,
    ): NetworkResponse<List<NetworkNewsResource>>
}

private const val NiABaseUrl = "https://staging-url.com/"

/**
 * Wrapper for data provided from the [NiABaseUrl]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T
)

/**
 * [Retrofit] backed [NiANetwork]
 */
@Singleton
class RetrofitNiANetwork @Inject constructor(
    networkJson: Json
) : NiANetwork {

    private val networkApi = Retrofit.Builder()
        .baseUrl(NiABaseUrl)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    // TODO: Decide logging logic
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        )
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitNiANetworkApi::class.java)

    override suspend fun getTopics(itemsPerPage: Int): List<NetworkTopic> =
        networkApi.getTopics(itemsPerPage = itemsPerPage).data

    override suspend fun getAuthors(itemsPerPage: Int): List<NetworkAuthor> =
        networkApi.getAuthors(itemsPerPage = itemsPerPage).data

    override suspend fun getNewsResources(itemsPerPage: Int): List<NetworkNewsResource> =
        networkApi.getNewsResources(itemsPerPage = itemsPerPage).data
}
