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

package com.google.samples.apps.nowinandroid.core.model.data

/**
 * Type for [NewsResource]
 */
enum class NewsResourceType(
    val serializedName: String,
    val displayText: String,
    // TODO: descriptions should probably be string resources
    val description: String,
) {
    Video(
        serializedName = "Video 📺",
        displayText = "Video 📺",
        description = "A video published on YouTube",
    ),
    APIChange(
        serializedName = "API change",
        displayText = "API change",
        description = "An addition, deprecation or change to the Android platform APIs.",
    ),
    Article(
        serializedName = "Article 📚",
        displayText = "Article 📚",
        description = "An article, typically on Medium or the official Android blog",
    ),
    Codelab(
        serializedName = "Codelab",
        displayText = "Codelab",
        description = "A new or updated codelab",
    ),
    Podcast(
        serializedName = "Podcast 🎙",
        displayText = "Podcast 🎙",
        description = "A podcast",
    ),
    Docs(
        serializedName = "Docs 📑",
        displayText = "Docs 📑",
        description = "A new or updated piece of documentation",
    ),
    Event(
        serializedName = "Event 📆",
        displayText = "Event 📆",
        description = "Information about a developer event e.g. Android Developer Summit",
    ),
    DAC(
        serializedName = "DAC",
        displayText = "DAC",
        description = "Android version features - Information about features in an Android",
    ),
    Unknown(
        serializedName = "Unknown",
        displayText = "Unknown",
        description = "Unknown",
    ),
}

fun String?.asNewsResourceType() = when (this) {
    null -> NewsResourceType.Unknown
    else -> NewsResourceType.values()
        .firstOrNull { type -> type.serializedName == this }
        ?: NewsResourceType.Unknown
}
