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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Article
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * [NewsResource] card used on the following screens: For You, Episodes, Saved
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsResourceCardExpanded(
    newsResource: NewsResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clickActionLabel = stringResource(R.string.card_tap_action)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        // Use custom label for accessibility services to communicate button's action to user.
        // Pass null for action to only override the label and not the actual action.
        modifier = modifier.semantics {
            onClick(label = clickActionLabel, action = null)
        }
    ) {
        Column {
            if (!newsResource.headerImageUrl.isNullOrEmpty()) {
                Row {
                    NewsResourceHeaderImage(newsResource.headerImageUrl)
                }
            }
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Row {
                        NewsResourceAuthors(newsResource.authors)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        NewsResourceTitle(
                            newsResource.title,
                            modifier = Modifier.fillMaxWidth((.8f))
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        // TODO: Implement functionality to 'bookmark' a resource b/227246491
//            BookmarkButton(isBookmarked, onToggleBookmark)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsResourceDate(newsResource.publishDate)
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsResourceShortDescription(newsResource.content)
                }
            }
        }
    }
}

@Composable
fun NewsResourceHeaderImage(
    headerImageUrl: String?
) {
    AsyncImage(
        placeholder = if (LocalInspectionMode.current) {
            painterResource(R.drawable.ic_placeholder_default)
        } else {
            // TODO b/228077205, show specific loading image visual
            null
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentScale = ContentScale.Crop,
        model = headerImageUrl,
        // TODO b/226661685: Investigate using alt text of  image to populate content description
        contentDescription = null // decorative image
    )
}

@Composable
fun NewsResourceAuthors(
    authors: List<Author>
) {
    if (authors.isNotEmpty()) {
        // Only display first author for now
        val author = authors[0]
        val authorNameFormatted =
            author.name.uppercase(ConfigurationCompat.getLocales(LocalConfiguration.current).get(0))

        val authorImageUrl = author.imageUrl

        val authorImageModifier = Modifier
            .clip(CircleShape)
            .size(24.dp)

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (authorImageUrl.isNotEmpty()) {
                AsyncImage(
                    modifier = authorImageModifier,
                    contentScale = ContentScale.Crop,
                    model = authorImageUrl,
                    contentDescription = null // decorative image
                )
            } else {
                Icon(
                    modifier = authorImageModifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(4.dp),
                    imageVector = Icons.Filled.Person,
                    contentDescription = null // decorative image
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(authorNameFormatted, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun NewsResourceTitle(
    newsResourceTitle: String,
    modifier: Modifier = Modifier
) {
    Text(newsResourceTitle, style = MaterialTheme.typography.headlineSmall, modifier = modifier)
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
private fun dateFormatted(publishDate: Instant): String {
    var zoneId by remember { mutableStateOf(ZoneId.systemDefault()) }

    val context = LocalContext.current

    DisposableEffect(context) {
        val receiver = TimeZoneBroadcastReceiver(
            onTimeZoneChanged = { zoneId = ZoneId.systemDefault() }
        )
        receiver.register(context)
        onDispose {
            receiver.unregister(context)
        }
    }

    return DateTimeFormatter.ofPattern("MMM d, yyyy")
        .withZone(zoneId).format(publishDate.toJavaInstant())
}

@Composable
fun NewsResourceDate(
    publishDate: Instant
) {
    Text(dateFormatted(publishDate), style = MaterialTheme.typography.labelSmall)
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
    Text(newsResourceShortDescription, style = MaterialTheme.typography.bodyLarge)
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

@Preview("NewsResourceCardExpanded")
@Composable
fun ExpandedNewsResourcePreview() {
    NiaTheme {
        Surface {
            NewsResourceCardExpanded(newsResource, {})
        }
    }
}

private val newsResource = NewsResource(
    id = "1",
    episodeId = "1",
    title = "Title",
    content = "Content",
    url = "url",
    headerImageUrl = "https://i.ytimg.com/vi/WL9h46CymlU/maxresdefault.jpg",
    publishDate = Instant.DISTANT_FUTURE,
    type = Article,
    authors = listOf(
        Author(
            id = "1",
            name = "Name",
            imageUrl = "",
            twitter = "",
            mediumPage = "",
            bio = "",
        )
    ),
    topics = listOf(
        Topic(
            id = "1",
            name = "Name",
            shortDescription = "Short description",
            longDescription = "Long description",
            url = "URL",
            imageUrl = "image URL"
        )
    )
)
