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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.data.fake.FakeDataSource
import com.google.samples.apps.nowinandroid.data.model.NewsResource
import com.google.samples.apps.nowinandroid.data.network.NetworkAuthor
import com.google.samples.apps.nowinandroid.data.network.NetworkEpisode
import com.google.samples.apps.nowinandroid.data.network.NetworkTopic
import com.google.samples.apps.nowinandroid.data.network.asEntity
import com.google.samples.apps.nowinandroid.ui.theme.NiaTheme

/**
 * [NewsResource] card used on the following screens: For You, Episodes, Saved
 */

@Composable
fun NewsResourceCardExpanded(
    newsResource: NewsResource,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row {
            NewsResourceTitle(newsResource.entity.title, modifier = Modifier.fillMaxWidth((.8f)))
            Spacer(modifier = Modifier.weight(1f))
            BookmarkButton(isBookmarked, onToggleBookmark)
        }
        Spacer(modifier = Modifier.height(12.dp))
        NewsResourceShortDescription(newsResource.entity.content)
    }
}

@Composable
fun NewsResourceHeaderImage(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun NewsResourceAuthors(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun NewsResourceTitle(
    newsResourceTitle: String,
    modifier: Modifier = Modifier
) {
    Text(newsResourceTitle, style = MaterialTheme.typography.h4, modifier = modifier)
}

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
fun NewsResourceDate(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun NewsResourceLink(
    newsResource: NewsResource
) {
    TODO()
}

@Composable
fun NewsResourceShortDescription(
    newsResourceShortDescription: String
) {
    Text(newsResourceShortDescription, style = MaterialTheme.typography.body1)
}

@Composable
fun NewsResourceTopics(
    newsResource: NewsResource
) {
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

@Composable
fun ExpandedNewsResourcePreview(
    @PreviewParameter(NewsResourcePreviewParameterProvider::class) newsResource: NewsResource
) {
    NiaTheme {
        Surface {
            NewsResourceCardExpanded(newsResource, true, {})
        }
    }
}

class NewsResourcePreviewParameterProvider : PreviewParameterProvider<NewsResource> {
    override val values = sequenceOf(
        NewsResource(
            FakeDataSource.sampleResource.asEntity(),
            NetworkEpisode(
                id = 1,
                name = "Now in Android 40",
                alternateVideo = null,
                alternateAudio = null,
                publishDate = FakeDataSource.sampleResource.publishDate
            ).asEntity(),
            listOf(
                NetworkAuthor(
                    id = 1,
                    name = "Android",
                    imageUrl = ""
                )
                    .asEntity()
            ),
            listOf(
                NetworkTopic(
                    id = 3,
                    name = "Performance",
                    description = ""
                ).asEntity()
            )
        )
    )
}
