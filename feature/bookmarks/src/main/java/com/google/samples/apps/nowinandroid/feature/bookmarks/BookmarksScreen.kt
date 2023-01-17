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

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.domain.model.previewUserNewsResources
import com.google.samples.apps.nowinandroid.core.ui.NewsItem
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun BookmarksRoute(
    modifier: Modifier = Modifier,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val bookmarkItems by viewModel.bookmarkItems.collectAsStateWithLifecycle()
    BookmarksScreen(
        bookmarkItems = bookmarkItems,
        removeFromBookmarks = viewModel::removeFromSavedResources,
        modifier = modifier
    )
}

/**
 * Displays the user's bookmarked articles. Includes support for loading and empty states.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun BookmarksScreen(
    bookmarkItems: List<BookmarkItem>,
    removeFromBookmarks: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        bookmarkItems.isNotEmpty() -> BookmarksGrid(
            bookmarkItems = bookmarkItems,
            removeFromBookmarks = removeFromBookmarks,
            modifier = modifier
        )
        else -> EmptyState(modifier)
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    NiaLoadingWheel(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .testTag("forYou:loading"),
        contentDesc = stringResource(id = R.string.saved_loading),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BookmarksGrid(
    bookmarkItems: List<BookmarkItem>,
    removeFromBookmarks: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollableState = rememberLazyGridState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "bookmarks:grid")
    LazyVerticalGrid(
        columns = Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        state = scrollableState,
        modifier = modifier
            .fillMaxSize()
            .testTag("bookmarks:feed")
    ) {
        items(
            items = bookmarkItems,
            key = BookmarkItem::key,
            contentType = BookmarkItem::contentType,
            itemContent = { item ->
                when (item) {
                    BookmarkItem.Loading -> LoadingState(modifier)
                    is BookmarkItem.News -> NewsItem(
                        modifier = Modifier.animateItemPlacement(),
                        userNewsResource = item.userNewsResource,
                        onNewsResourcesCheckedChanged = { id, _ -> removeFromBookmarks(id) }
                    )
                }
            }
        )

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .testTag("bookmarks:empty"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.img_empty_bookmarks),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.bookmarks_empty_error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.bookmarks_empty_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun LoadingStatePreview() {
    NiaTheme {
        LoadingState()
    }
}

@Preview
@Composable
private fun BookmarksGridPreview() {
    NiaTheme {
        BookmarksGrid(
            bookmarkItems = previewUserNewsResources.map(BookmarkItem::News),
            removeFromBookmarks = {}
        )
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    NiaTheme {
        EmptyState()
    }
}
