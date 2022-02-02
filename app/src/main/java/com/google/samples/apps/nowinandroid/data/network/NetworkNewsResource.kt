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

package com.google.samples.apps.nowinandroid.data.network

import com.google.samples.apps.nowinandroid.data.local.entities.NewsResourceEntity
import com.google.samples.apps.nowinandroid.data.network.utilities.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Network representation of [NewsResourceEntity] when fetched from /networkresources
 */
@Serializable
data class NetworkNewsResource(
    val id: Int,
    val episodeId: Int,
    val title: String,
    val content: String,
    val url: String,
    @Serializable(InstantSerializer::class)
    val publishDate: Instant,
    val type: String,
    val authors: List<Int> = listOf(),
    val topics: List<Int> = listOf(),
)

/**
 * Network representation of [NewsResourceEntity] when fetched from /networkresources{id}
 */
@Serializable
data class NetworkNewsResourceExpanded(
    val id: Int,
    val episodeId: Int,
    val title: String,
    val content: String,
    val url: String,
    @Serializable(InstantSerializer::class)
    val publishDate: Instant,
    val type: String,
    val authors: List<NetworkAuthor> = listOf(),
    val topics: List<NetworkTopic> = listOf(),
)

fun NetworkNewsResource.asEntity() = NewsResourceEntity(
    id = id,
    episodeId = episodeId,
    title = title,
    content = content,
    url = url,
    publishDate = publishDate,
    type = type,
)

fun NetworkNewsResourceExpanded.asEntity() = NewsResourceEntity(
    id = id,
    episodeId = episodeId,
    title = title,
    content = content,
    url = url,
    publishDate = publishDate,
    type = type,
)
