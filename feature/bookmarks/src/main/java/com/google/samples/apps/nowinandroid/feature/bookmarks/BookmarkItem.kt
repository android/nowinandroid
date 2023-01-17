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

package com.google.samples.apps.nowinandroid.feature.bookmarks

import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.feature.bookmarks.BookmarkItem.Loading
import com.google.samples.apps.nowinandroid.feature.bookmarks.BookmarkItem.News

sealed class BookmarkItem {

    object Loading : BookmarkItem()

    data class News(
        val userNewsResource: UserNewsResource
    ) : BookmarkItem()
}

val BookmarkItem.key: String
    get() = when (this) {
        Loading -> "loading"
        is News -> userNewsResource.id
    }

val BookmarkItem.contentType: String
    get() = when (this) {
        Loading -> "loading"
        is News -> "news"
    }
