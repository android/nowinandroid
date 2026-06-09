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

package com.google.samples.apps.nowinandroid.feature.bookmarks.impl

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.analytics.LocalAnalyticsHelper
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.DraggableScrollbar
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.rememberDraggableScroller
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.scrollbarState
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.LocalTintTheme
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.UserNewsResource
import com.google.samples.apps.nowinandroid.core.ui.BookmarkNoteDialog
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState.Loading
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState.Success
import com.google.samples.apps.nowinandroid.core.ui.NewsResourceCardExpanded
import com.google.samples.apps.nowinandroid.core.ui.TrackScreenViewEvent
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank
import com.google.samples.apps.nowinandroid.core.ui.UserNewsResourcePreviewParameterProvider
import com.google.samples.apps.nowinandroid.core.ui.launchCustomChromeTab
import com.google.samples.apps.nowinandroid.core.ui.logNewsResourceOpened
import com.google.samples.apps.nowinandroid.feature.bookmarks.api.R

@Composable
internal fun BookmarksScreen(
    onTopicClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: BookmarksViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var editingNoteId by remember { mutableStateOf<String?>(null) }

    BookmarksScreen(
        feedState = uiState.feedState,
        onShowSnackbar = onShowSnackbar,
        removeFromBookmarks = viewModel::removeFromSavedResources,
        onNewsResourceViewed = { viewModel.setNewsResourceViewed(it, true) },
        onTopicClick = onTopicClick,
        modifier = modifier,
        shouldDisplayUndoBookmark = uiState.shouldDisplayUndoBookmark,
        undoBookmarkRemoval = viewModel::undoBookmarkRemoval,
        clearUndoState = viewModel::clearUndoState,
        onEditNote = { editingNoteId = it },
        isInSelectionMode = uiState.isInSelectionMode,
        selectedIds = uiState.selectedIds,
        enterSelectionMode = viewModel::enterSelectionMode,
        exitSelectionMode = viewModel::exitSelectionMode,
        toggleSelection = viewModel::toggleSelection,
        selectAll = viewModel::selectAll,
        shouldDisplayUndoBulkRemove = uiState.shouldDisplayUndoBulkRemove,
        bulkRemovedCount = uiState.bulkRemovedCount,
        removeSelected = viewModel::removeSelected,
        undoBulkRemove = viewModel::undoBulkRemove,
        clearBulkUndoState = viewModel::clearBulkUndoState,
    )

    editingNoteId?.let { id ->
        val currentNote =
            (uiState.feedState as? Success)?.feed?.find { it.id == id }?.bookmarkNote ?: ""
        BookmarkNoteDialog(
            initialNote = currentNote,
            onDismiss = { editingNoteId = null },
            onSave = { note ->
                viewModel.updateNote(id, note)
                editingNoteId = null
            },
        )
    }
}

/**
 * Displays the user's bookmarked articles. Includes support for loading and empty states.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun BookmarksScreen(
    feedState: NewsFeedUiState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    removeFromBookmarks: (String) -> Unit,
    onNewsResourceViewed: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    shouldDisplayUndoBookmark: Boolean = false,
    undoBookmarkRemoval: () -> Unit = {},
    clearUndoState: () -> Unit = {},
    onEditNote: (String) -> Unit = {},
    isInSelectionMode: Boolean = false,
    selectedIds: Set<String> = emptySet(),
    enterSelectionMode: (String) -> Unit = {},
    exitSelectionMode: () -> Unit = {},
    toggleSelection: (String) -> Unit = {},
    selectAll: () -> Unit = {},
    shouldDisplayUndoBulkRemove: Boolean = false,
    bulkRemovedCount: Int = 0,
    removeSelected: () -> Unit = {},
    undoBulkRemove: () -> Unit = {},
    clearBulkUndoState: () -> Unit = {},
) {
    val bookmarkRemovedMessage = stringResource(id = R.string.feature_bookmarks_api_removed)
    val undoText = stringResource(id = R.string.feature_bookmarks_api_undo)

    BackHandler(enabled = isInSelectionMode) {
        exitSelectionMode()
    }

    LaunchedEffect(shouldDisplayUndoBookmark) {
        if (shouldDisplayUndoBookmark) {
            val snackBarResult = onShowSnackbar(bookmarkRemovedMessage, undoText)
            if (snackBarResult) {
                undoBookmarkRemoval()
            } else {
                clearUndoState()
            }
        }
    }

    LaunchedEffect(shouldDisplayUndoBulkRemove) {
        if (shouldDisplayUndoBulkRemove) {
            val result = onShowSnackbar(
                "$bulkRemovedCount bookmarks removed",
                undoText,
            )
            if (result) {
                undoBulkRemove()
            } else {
                clearBulkUndoState()
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        clearUndoState()
        clearBulkUndoState()
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (feedState) {
            Loading -> LoadingState()
            is Success -> if (feedState.feed.isNotEmpty()) {
                BookmarksGrid(
                    feedState = feedState,
                    removeFromBookmarks = removeFromBookmarks,
                    onNewsResourceViewed = onNewsResourceViewed,
                    onTopicClick = onTopicClick,
                    onEditNote = onEditNote,
                    isInSelectionMode = isInSelectionMode,
                    selectedIds = selectedIds,
                    enterSelectionMode = enterSelectionMode,
                    toggleSelection = toggleSelection,
                )
            } else {
                EmptyState()
            }
        }

        if (isInSelectionMode) {
            Surface(
                tonalElevation = 3.dp,
                shadowElevation = 6.dp,
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                ) {
                    IconButton(onClick = exitSelectionMode) {
                        Icon(NiaIcons.Close, contentDescription = "Cancel selection")
                    }
                    Text(
                        text = "${selectedIds.size} selected",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    TextButton(onClick = selectAll) { Text("All") }
                    Button(
                        onClick = removeSelected,
                        enabled = selectedIds.isNotEmpty(),
                    ) {
                        Text("Remove")
                    }
                }
            }
        }
    }

    TrackScreenViewEvent(screenName = "Saved")
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    NiaLoadingWheel(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .testTag("bookmarks:loading"),
        contentDesc = stringResource(id = R.string.feature_bookmarks_api_loading),
    )
}

@Composable
private fun BookmarksGrid(
    feedState: Success,
    removeFromBookmarks: (String) -> Unit,
    onNewsResourceViewed: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    onEditNote: (String) -> Unit,
    isInSelectionMode: Boolean,
    selectedIds: Set<String>,
    enterSelectionMode: (String) -> Unit,
    toggleSelection: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollableState = rememberLazyStaggeredGridState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "bookmarks:grid")
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 24.dp,
            state = scrollableState,
            modifier = Modifier
                .fillMaxSize()
                .testTag("bookmarks:feed"),
        ) {
            items(
                items = feedState.feed,
                key = { it.id },
                contentType = { "newsFeedItem" },
            ) { userNewsResource ->
                val context = LocalContext.current
                val analyticsHelper = LocalAnalyticsHelper.current
                val backgroundColor =
                    MaterialTheme.colorScheme.background.toArgb()
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .animateItem(),
                ) {
                    NewsResourceCardExpanded(
                        userNewsResource = userNewsResource,
                        isBookmarked = userNewsResource.isSaved,
                        onClick = {
                            if (isInSelectionMode) {
                                toggleSelection(userNewsResource.id)
                            } else {
                                analyticsHelper.logNewsResourceOpened(
                                    newsResourceId = userNewsResource.id,
                                )
                                launchCustomChromeTab(
                                    context,
                                    Uri.parse(userNewsResource.url),
                                    backgroundColor,
                                )
                                onNewsResourceViewed(userNewsResource.id)
                            }
                        },
                        onLongClick = {
                            if (!isInSelectionMode) enterSelectionMode(userNewsResource.id)
                        },
                        hasBeenViewed = userNewsResource.hasBeenViewed,
                        onToggleBookmark = {
                            if (!isInSelectionMode) removeFromBookmarks(
                                userNewsResource.id,
                            )
                        },
                        onTopicClick = onTopicClick,
                        bookmarkNote = userNewsResource.bookmarkNote?.let { note ->
                            {
                                UserNote(
                                    note = note,
                                    isInSelectionMode = isInSelectionMode,
                                    onEditNote = { onEditNote(userNewsResource.id) },
                                )
                            }
                        },
                    )
                    if (isInSelectionMode) {
                        Checkbox(
                            checked = userNewsResource.id in selectedIds,
                            onCheckedChange = { toggleSelection(userNewsResource.id) },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(4.dp),
                        )
                    }
                }
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
        val itemsAvailable = feedState.feed.size
        val scrollbarState = scrollableState.scrollbarState(
            itemsAvailable = itemsAvailable,
        )
        scrollableState.DraggableScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 2.dp)
                .align(Alignment.CenterEnd),
            state = scrollbarState,
            orientation = Orientation.Vertical,
            onThumbMoved = scrollableState.rememberDraggableScroller(
                itemsAvailable = itemsAvailable,
            ),
        )
    }
}

@Composable
private fun UserNote(
    note: String,
    isInSelectionMode: Boolean,
    onEditNote: () -> Unit,
) {
    Text(
        text = note,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .then(
                if (!isInSelectionMode) Modifier.clickable(onClick = onEditNote) else Modifier,
            ),
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .testTag("bookmarks:empty"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.feature_bookmarks_api_mg_empty_bookmarks),
            colorFilter = if (iconTint != Color.Unspecified) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(id = R.string.feature_bookmarks_api_empty_error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.feature_bookmarks_api_empty_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
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
private fun BookmarksGridPreview(
    @PreviewParameter(UserNewsResourcePreviewParameterProvider::class)
    userNewsResources: List<UserNewsResource>,
) {
    NiaTheme {
        BookmarksGrid(
            feedState = Success(userNewsResources),
            removeFromBookmarks = {},
            onNewsResourceViewed = {},
            onTopicClick = {},
            onEditNote = {},
            isInSelectionMode = false,
            selectedIds = emptySet(),
            enterSelectionMode = {},
            toggleSelection = {},
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
