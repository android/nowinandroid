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

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaIconToggleButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTopicTag
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.domain.model.previewUserNewsResources
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.google.samples.apps.nowinandroid.core.designsystem.R as DesignsystemR

/**
 * [NewsResource] card used on the following screens: For You, Saved
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsResourceCardExpanded(
    userNewsResource: UserNewsResource,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        },
    ) {
        Column {
            if (!userNewsResource.headerImageUrl.isNullOrEmpty()) {
                Row {
                    NewsResourceHeaderImage(userNewsResource.headerImageUrl)
                }
            }
            Box(
                modifier = Modifier.padding(16.dp),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        NewsResourceTitle(
                            userNewsResource.title,
                            modifier = Modifier.fillMaxWidth((.8f)),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BookmarkButton(isBookmarked, onToggleBookmark)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsResourceMetaData(userNewsResource.publishDate, userNewsResource.type)
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsResourceShortDescription(userNewsResource.content)
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsResourceTopics(userNewsResource.followableTopics)
                }
            }
        }
    }
}

@Composable
fun NewsResourceHeaderImage(
    headerImageUrl: String?,
) {
    AsyncImage(
        placeholder = if (LocalInspectionMode.current) {
            painterResource(DesignsystemR.drawable.ic_placeholder_default)
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
        contentDescription = null, // decorative image
    )
}

@Composable
fun NewsResourceTitle(
    newsResourceTitle: String,
    modifier: Modifier = Modifier,
) {
    Text(newsResourceTitle, style = MaterialTheme.typography.headlineSmall, modifier = modifier)
}

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NiaIconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onClick() },
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(NiaIcons.BookmarkBorder),
                contentDescription = stringResource(R.string.bookmark),
            )
        },
        checkedIcon = {
            Icon(
                painter = painterResource(NiaIcons.Bookmark),
                contentDescription = stringResource(R.string.unbookmark),
            )
        },
    )
}

@Composable
fun dateFormatted(publishDate: Instant): String {
    var zoneId by remember { mutableStateOf(ZoneId.systemDefault()) }

    val context = LocalContext.current

    DisposableEffect(context) {
        val receiver = TimeZoneBroadcastReceiver(
            onTimeZoneChanged = { zoneId = ZoneId.systemDefault() },
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
fun NewsResourceMetaData(
    publishDate: Instant,
    resourceType: NewsResourceType,
) {
    val formattedDate = dateFormatted(publishDate)
    Text(
        if (resourceType != NewsResourceType.Unknown) {
            stringResource(R.string.card_meta_data_text, formattedDate, resourceType.displayText)
        } else {
            formattedDate
        },
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
fun NewsResourceLink(
    @Suppress("UNUSED_PARAMETER")
    newsResource: NewsResource,
) {
    TODO()
}

@Composable
fun NewsResourceShortDescription(
    newsResourceShortDescription: String,
) {
    Text(newsResourceShortDescription, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun NewsResourceTopics(
    topics: List<FollowableTopic>,
    modifier: Modifier = Modifier,
) {
    // Store the ID of the Topic which has its "following" menu expanded, if any.
    // To avoid UI confusion, only one topic can have an expanded menu at a time.
    var expandedTopicId by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()), // causes narrow chips
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        for (followableTopic in topics) {
            NiaTopicTag(
                expanded = expandedTopicId == followableTopic.topic.id,
                followed = followableTopic.isFollowed,
                onDropdownMenuToggle = { show ->
                    expandedTopicId = if (show) followableTopic.topic.id else null
                },
                onFollowClick = { }, // ToDo
                onUnfollowClick = { }, // ToDo
                onBrowseClick = { }, // ToDo
                text = {
                    val contentDescription = if (followableTopic.isFollowed) {
                        stringResource(
                            R.string.topic_chip_content_description_when_followed,
                            followableTopic.topic.name,
                        )
                    } else {
                        stringResource(
                            R.string.topic_chip_content_description_when_not_followed,
                            followableTopic.topic.name,
                        )
                    }
                    Text(
                        text = followableTopic.topic.name.uppercase(Locale.getDefault()),
                        modifier = Modifier.semantics {
                            this.contentDescription = contentDescription
                        },
                    )
                },
            )
        }
    }
}

@Preview("Bookmark Button")
@Composable
private fun BookmarkButtonPreview() {
    NiaTheme {
        Surface {
            BookmarkButton(isBookmarked = false, onClick = { })
        }
    }
}

@Preview("Bookmark Button Bookmarked")
@Composable
private fun BookmarkButtonBookmarkedPreview() {
    NiaTheme {
        Surface {
            BookmarkButton(isBookmarked = true, onClick = { })
        }
    }
}

@Preview("NewsResourceCardExpanded")
@Composable
private fun ExpandedNewsResourcePreview() {
    NiaTheme {
        Surface {
            NewsResourceCardExpanded(
                userNewsResource = previewUserNewsResources[0],
                isBookmarked = true,
                onToggleBookmark = {},
                onClick = {},
            )
        }
    }
}
