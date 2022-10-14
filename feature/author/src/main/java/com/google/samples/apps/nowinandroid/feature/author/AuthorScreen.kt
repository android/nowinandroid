/*
 * Copyright 2021 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.feature.author

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaFilterChip
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.previewAuthors
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources
import com.google.samples.apps.nowinandroid.core.ui.DevicePreviews
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank
import com.google.samples.apps.nowinandroid.core.ui.newsResourceCardItems

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AuthorRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthorViewModel = hiltViewModel(),
) {
    val authorUiState: AuthorUiState by viewModel.authorUiState.collectAsStateWithLifecycle()
    val newsUiState: NewsUiState by viewModel.newsUiState.collectAsStateWithLifecycle()

    AuthorScreen(
        authorUiState = authorUiState,
        newsUiState = newsUiState,
        modifier = modifier,
        onBackClick = onBackClick,
        onFollowClick = viewModel::followAuthorToggle,
        onBookmarkChanged = viewModel::bookmarkNews,
    )
}

@VisibleForTesting
@Composable
internal fun AuthorScreen(
    authorUiState: AuthorUiState,
    newsUiState: NewsUiState,
    onBackClick: () -> Unit,
    onFollowClick: (Boolean) -> Unit,
    onBookmarkChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollableState = rememberLazyListState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "author:column")
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollableState
    ) {
        item {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        }
        when (authorUiState) {
            AuthorUiState.Loading -> {
                item {
                    NiaLoadingWheel(
                        modifier = modifier,
                        contentDesc = stringResource(id = R.string.author_loading),
                    )
                }
            }
            AuthorUiState.Error -> {
                TODO()
            }
            is AuthorUiState.Success -> {
                item {
                    AuthorToolbar(
                        onBackClick = onBackClick,
                        onFollowClick = onFollowClick,
                        uiState = authorUiState.followableAuthor,
                    )
                }
                authorBody(
                    author = authorUiState.followableAuthor.author,
                    news = newsUiState,
                    onBookmarkChanged = onBookmarkChanged,
                )
            }
        }
        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}

private fun LazyListScope.authorBody(
    author: Author,
    news: NewsUiState,
    onBookmarkChanged: (String, Boolean) -> Unit
) {
    item {
        AuthorHeader(author)
    }

    authorCards(news, onBookmarkChanged)
}

@Composable
private fun AuthorHeader(author: Author) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .size(216.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            model = author.imageUrl,
            contentDescription = "Author profile picture",
        )
        Text(author.name, style = MaterialTheme.typography.displayMedium)
        if (author.bio.isNotEmpty()) {
            Text(
                text = author.bio,
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun LazyListScope.authorCards(
    news: NewsUiState,
    onBookmarkChanged: (String, Boolean) -> Unit
) {
    when (news) {
        is NewsUiState.Success -> {
            newsResourceCardItems(
                items = news.news,
                newsResourceMapper = { it.newsResource },
                isBookmarkedMapper = { it.isSaved },
                onToggleBookmark = { onBookmarkChanged(it.newsResource.id, !it.isSaved) },
                itemModifier = Modifier.padding(24.dp)
            )
        }
        is NewsUiState.Loading -> item {
            NiaLoadingWheel(contentDesc = "Loading news") // TODO
        }
        else -> item {
            Text("Error") // TODO
        }
    }
}

@Composable
private fun AuthorToolbar(
    uiState: FollowableAuthor,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFollowClick: (Boolean) -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Filled.ArrowBack,
                contentDescription = stringResource(
                    id = com.google.samples.apps.nowinandroid.core.ui.R.string.back
                )
            )
        }
        val selected = uiState.isFollowed
        NiaFilterChip(
            modifier = Modifier.padding(horizontal = 16.dp),
            selected = selected,
            onSelectedChange = onFollowClick,
        ) {
            if (selected) {
                Text(stringResource(id = R.string.author_following))
            } else {
                Text(stringResource(id = R.string.author_not_following))
            }
        }
    }
}

@DevicePreviews
@Composable
fun AuthorScreenPopulated() {
    NiaTheme {
        NiaBackground {
            AuthorScreen(
                authorUiState = AuthorUiState.Success(FollowableAuthor(previewAuthors[0], false)),
                newsUiState = NewsUiState.Success(
                    previewNewsResources.mapIndexed { index, newsResource ->
                        SaveableNewsResource(
                            newsResource = newsResource,
                            isSaved = index % 2 == 0,
                        )
                    }
                ),
                onBackClick = {},
                onFollowClick = {},
                onBookmarkChanged = { _, _ -> },
            )
        }
    }
}

@DevicePreviews
@Composable
fun AuthorScreenLoading() {
    NiaTheme {
        NiaBackground {
            AuthorScreen(
                authorUiState = AuthorUiState.Loading,
                newsUiState = NewsUiState.Loading,
                onBackClick = {},
                onFollowClick = {},
                onBookmarkChanged = { _, _ -> },
            )
        }
    }
}
