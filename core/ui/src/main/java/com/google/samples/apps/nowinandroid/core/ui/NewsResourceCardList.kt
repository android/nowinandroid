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

package com.google.samples.apps.nowinandroid.core.ui

import android.net.Uri
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource

/**
 * Extension function for displaying a [List] of [NewsResourceCardExpanded] backed by a generic
 * [List] [T].
 *
 * [newsResourceMapper] maps type [T] to a [NewsResource]
 * [isBookmarkedMapper] maps type [T] to whether the [NewsResource] is bookmarked
 * [onToggleBookmark] defines the action invoked when a user wishes to bookmark an item
 * [onItemClick] optional parameter for action to be performed when the card is clicked. The
 * default action launches an intent matching the card.
 */
fun <T> LazyListScope.newsResourceCardItems(
    items: List<T>,
    newsResourceMapper: (item: T) -> UserNewsResource, // TODO remove this?
    isBookmarkedMapper: (item: T) -> Boolean,
    onToggleBookmark: (item: T) -> Unit,
    onItemClick: ((item: T) -> Unit)? = null,
    itemModifier: Modifier = Modifier,
) = items(
    items = items,
    key = { newsResourceMapper(it).id },
    itemContent = { item ->
        val newsResource = newsResourceMapper(item)
        val resourceUrl = Uri.parse(newsResource.url)
        val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
        val context = LocalContext.current

        NewsResourceCardExpanded(
            userNewsResource = newsResource,
            isBookmarked = isBookmarkedMapper(item),
            onToggleBookmark = { onToggleBookmark(item) },
            onClick = {
                when (onItemClick) {
                    null -> launchCustomChromeTab(context, resourceUrl, backgroundColor)
                    else -> onItemClick(item)
                }
            },
            modifier = itemModifier
        )
    },
)
