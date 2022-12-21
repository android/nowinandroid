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

import com.google.samples.apps.nowinandroid.core.model.R
import com.google.samples.apps.nowinandroid.core.uitext.UiText

/**
 * Type for [NewsResource]
 */
enum class NewsResourceType(
    val serializedName: String,
    val displayText: String,
    val description: UiText
) {
    Video(
        serializedName = "Video 📺",
        displayText = "Video 📺",
        description = UiText.StringResource(R.string.video)
    ),
    APIChange(
        serializedName = "API change",
        displayText = "API change",
        description = UiText.StringResource(R.string.apiChange)
    ),
    Article(
        serializedName = "Article 📚",
        displayText = "Article 📚",
        description = UiText.StringResource(R.string.article)
    ),
    Codelab(
        serializedName = "Codelab",
        displayText = "Codelab",
        description = UiText.StringResource(R.string.codeLab)
    ),
    Podcast(
        serializedName = "Podcast 🎙",
        displayText = "Podcast 🎙",
        description = UiText.StringResource(R.string.podcast)
    ),
    Docs(
        serializedName = "Docs 📑",
        displayText = "Docs 📑",
        description = UiText.StringResource(R.string.docs)
    ),
    Event(
        serializedName = "Event 📆",
        displayText = "Event 📆",
        description = UiText.StringResource(R.string.event)
    ),
    DAC(
        serializedName = "DAC",
        displayText = "DAC",
        description = UiText.StringResource(R.string.dac)
    ),
    Unknown(
        serializedName = "Unknown",
        displayText = "Unknown",
        description = UiText.StringResource(R.string.unknown)
    )
}

fun String?.asNewsResourceType() = when (this) {
    null -> NewsResourceType.Unknown
    else -> NewsResourceType.values()
        .firstOrNull { type -> type.serializedName == this }
        ?: NewsResourceType.Unknown
}
