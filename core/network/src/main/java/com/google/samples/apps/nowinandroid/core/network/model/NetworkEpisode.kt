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

package com.google.samples.apps.nowinandroid.core.network.model

import com.google.samples.apps.nowinandroid.core.model.data.Episode
import com.google.samples.apps.nowinandroid.core.network.model.util.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Network representation of [Episode] when fetched from /episodes
 */
@Serializable
data class NetworkEpisode(
    val id: String,
    val name: String,
    @Serializable(InstantSerializer::class)
    val publishDate: Instant,
    val alternateVideo: String?,
    val alternateAudio: String?,
    val newsResources: List<String> = listOf(),
    val authors: List<String> = listOf(),
)

/**
 * Network representation of [Episode] when fetched from /episodes/{id}
 */
@Serializable
data class NetworkEpisodeExpanded(
    val id: String,
    val name: String,
    @Serializable(InstantSerializer::class)
    val publishDate: Instant,
    val alternateVideo: String,
    val alternateAudio: String,
    val newsResources: List<NetworkNewsResource> = listOf(),
    val authors: List<NetworkAuthor> = listOf(),
)
