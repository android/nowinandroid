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

package com.google.samples.apps.nowinandroid.feature.foryou.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank
import com.google.samples.apps.nowinandroid.feature.foryou.R.string

@Composable
fun AuthorsCarousel(
    authors: List<FollowableAuthor>,
    onAuthorClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val tag = "forYou:authors"
    TrackScrollJank(scrollableState = lazyListState, stateName = tag)

    LazyRow(
        modifier = modifier.testTag(tag),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState
    ) {
        items(items = authors, key = { item -> item.author.id }) { followableAuthor ->
            AuthorItem(
                author = followableAuthor.author,
                following = followableAuthor.isFollowed,
                onAuthorClick = { following ->
                    onAuthorClick(followableAuthor.author.id, following)
                },
            )
        }
    }
}

@Composable
fun AuthorItem(
    author: Author,
    following: Boolean,
    onAuthorClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val followDescription = if (following) {
        stringResource(string.following)
    } else {
        stringResource(string.not_following)
    }
    val followActionLabel = if (following) {
        stringResource(string.unfollow)
    } else {
        stringResource(string.follow)
    }

    Column(
        modifier = modifier
            .toggleable(
                value = following,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onValueChange = { newFollowing -> onAuthorClick(newFollowing) },
            )
            .padding(8.dp)
            .sizeIn(maxWidth = 48.dp)
            .semantics(mergeDescendants = true) {
                // Add information for A11y services, explaining what each state means and
                // what will happen when the user interacts with the author item.
                stateDescription = "$followDescription ${author.name}"
                onClick(label = followActionLabel, action = null)
            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val authorImageModifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
            if (author.imageUrl.isEmpty()) {
                Icon(
                    modifier = authorImageModifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(4.dp),
                    imageVector = NiaIcons.Person,
                    contentDescription = null // decorative image
                )
            } else {
                AsyncImage(
                    modifier = authorImageModifier,
                    model = author.imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            val backgroundColor =
                if (following)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface
            Icon(
                imageVector = if (following) NiaIcons.Check else NiaIcons.Add,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(18.dp)
                    .drawBehind {
                        drawCircle(
                            color = backgroundColor,
                            radius = 12.dp.toPx()
                        )
                    }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = author.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun AuthorCarouselPreview() {
    NiaTheme {
        Surface {
            AuthorsCarousel(
                authors = listOf(
                    FollowableAuthor(
                        Author(
                            id = "1",
                            name = "Android Dev",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        false
                    ),
                    FollowableAuthor(
                        author = Author(
                            id = "2",
                            name = "Android Dev2",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        isFollowed = true
                    ),
                    FollowableAuthor(
                        Author(
                            id = "3",
                            name = "Android Dev3",
                            imageUrl = "",
                            twitter = "",
                            mediumPage = "",
                            bio = "",
                        ),
                        false
                    )
                ),
                onAuthorClick = { _, _ -> },
            )
        }
    }
}

@Preview
@Composable
fun AuthorItemPreview() {
    NiaTheme {
        Surface {
            AuthorItem(
                author = Author(
                    id = "0",
                    name = "Android Dev",
                    imageUrl = "",
                    twitter = "",
                    mediumPage = "",
                    bio = "",
                ),
                following = true,
                onAuthorClick = { }
            )
        }
    }
}
