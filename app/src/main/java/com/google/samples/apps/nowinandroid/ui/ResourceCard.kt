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

package com.google.samples.apps.nowinandroid.ui

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.data.news.NewsResource
import com.google.samples.apps.nowinandroid.ui.theme.NiaTheme

/**
 * [com.google.samples.apps.nowinandroid.data.news.NewsResource] card used on the following screens:
 * For You, Episodes, Saved
 */

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clickActionLabel = stringResource(
        if (isBookmarked) R.string.unbookmark else R.string.bookmark
    )
    IconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onClick() },
        modifier = modifier.semantics {
            // Use custom label for accessibility services to communicate button's action to user.
            // Pass null for action to only override the label and not the actual action.
            this.onClick(label = clickActionLabel, action = null)
        }
    ) {
        Icon(
            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = null // handled by click label of parent
        )
    }
}

@Composable
fun ResourceAuthors(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun ResourceHeaderImage(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun ResourceTitle(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun ResourceDate(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun ResourceLink(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun ResourceShortDescription(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun ResourceTopics(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun ResourceCardExpanded() {
    TODO()
}

@Preview("Bookmark Button")
@Composable
fun BookmarkButtonPreview() {
    NiaTheme {
        Surface {
            BookmarkButton(isBookmarked = false, onClick = { })
        }
    }
}

@Preview("Bookmark Button Bookmarked")
@Composable
fun BookmarkButtonBookmarkedPreview() {
    NiaTheme {
        Surface {
            BookmarkButton(isBookmarked = true, onClick = { })
        }
    }
}

@Preview("Expanded resource card")
@Preview("Expanded resource card (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExpandedResourcePreview() {
    NiaTheme {
        Surface {
            ResourceCardExpanded()
        }
    }
}
