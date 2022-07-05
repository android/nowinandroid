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

package com.google.samples.apps.nowinandroid.feature.foryou

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaToggleButton
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor

@Composable
fun AuthorsCarousel(
    authors: List<FollowableAuthor>,
    onAuthorClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.testTag("forYou:authors"),
        contentPadding = PaddingValues(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
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
        stringResource(id = R.string.following)
    } else {
        stringResource(id = R.string.not_following)
    }

    Column(
        modifier = modifier
            .toggleable(
                value = following,
                enabled = true,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onValueChange = { newFollowing -> onAuthorClick(newFollowing) },
            )
            .sizeIn(maxWidth = 48.dp)
            .semantics(mergeDescendants = true) {
                stateDescription = "$followDescription ${author.name}"
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
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }
            NiaToggleButton(
                checked = following,
                onCheckedChange = onAuthorClick,
                modifier = Modifier.align(Alignment.BottomEnd),
                icon = {
                    Icon(
                        imageVector = NiaIcons.Add,
                        contentDescription = null
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = NiaIcons.Check,
                        contentDescription = null
                    )
                },
                size = 24.dp,
                backgroundColor = MaterialTheme.colorScheme.surface
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
