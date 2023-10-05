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

import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Network representation of [NewsResource] when fetched from /newsresources
 */
@Serializable
data class NetworkNewsResource(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val headerImageUrl: String,
    val publishDate: Instant,
    val type: String,
    val topics: List<String> = listOf(),
)

/**
 * Network representation of [NewsResource] when fetched from /newsresources/{id}
 */
@Serializable
data class NetworkNewsResourceExpanded(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val headerImageUrl: String,
    val publishDate: Instant,
    val type: String,
    val topics: List<NetworkTopic> = listOf(),
)
