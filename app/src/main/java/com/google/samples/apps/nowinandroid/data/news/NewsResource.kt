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

package com.google.samples.apps.nowinandroid.data.news

import java.util.Date

/**
 * Item representing a summary of a noteworthy item from a Now In Android episode
 */
data class NewsResource(
    val episode: Int,
    val title: String,
    val content: String,
    val url: String,
    val authorName: String,
    // TODO: Replace this with a type from kotlinx-datetime or similar in row al0AcTYbwbU6lyRWL5dOQ1
    val publishDate: Date,
    val type: String,
    val topics: List<String>,
    val alternateVideo: VideoInfo?
)

/**
 * Data class summarizing video metadata
 */
data class VideoInfo(
    val url: String,
    val startTimestamp: String,
    val endTimestamp: String,
)
