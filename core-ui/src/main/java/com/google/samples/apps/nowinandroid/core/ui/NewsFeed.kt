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

import android.content.Intent
import android.net.Uri
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.SaveableNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources

/**
 * An extension on [LazyListScope] defining a feed with news resources.
 * Depending on the [feedState], this might emit no items.
 *
 * @param showLoadingUIIfLoading if true, show a visual indication of loading if the
 * [feedState] is loading. This allows a caller to suppress a loading visual if one is already
 * present in the UI elsewhere.
 */
fun LazyListScope.NewsFeed(
    feedState: NewsFeedUiState,
    showLoadingUIIfLoading: Boolean,
    @StringRes loadingContentDescription: Int,
    @IntRange(from = 1) numberOfColumns: Int,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit
) {
    when (feedState) {
        NewsFeedUiState.Loading -> {
            if (showLoadingUIIfLoading) {
                item {
                    NiaLoadingWheel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        contentDesc = stringResource(loadingContentDescription),
                    )
                }
            }
        }
        is NewsFeedUiState.Success -> {
            items(
                feedState.feed.chunked(numberOfColumns)
            ) { saveableNewsResources ->
                Row(
                    modifier = Modifier.padding(
                        top = 32.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // The last row may not be complete, but for a consistent grid
                    // structure we still want an element taking up the empty space.
                    // Therefore, the last row may have empty boxes.
                    repeat(numberOfColumns) { index ->
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            val saveableNewsResource =
                                saveableNewsResources.getOrNull(index)

                            if (saveableNewsResource != null) {
                                val launchResourceIntent =
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(saveableNewsResource.newsResource.url)
                                    )
                                val context = LocalContext.current

                                NewsResourceCardExpanded(
                                    newsResource = saveableNewsResource.newsResource,
                                    isBookmarked = saveableNewsResource.isSaved,
                                    onClick = {
                                        ContextCompat.startActivity(
                                            context,
                                            launchResourceIntent,
                                            null
                                        )
                                    },
                                    onToggleBookmark = {
                                        onNewsResourcesCheckedChanged(
                                            saveableNewsResource.newsResource.id,
                                            !saveableNewsResource.isSaved
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * A sealed hierarchy describing the state of the feed of news resources.
 */
sealed interface NewsFeedUiState {
    /**
     * The feed is still loading.
     */
    object Loading : NewsFeedUiState

    /**
     * The feed is loaded with the given list of news resources.
     */
    data class Success(
        /**
         * The list of news resources contained in this feed.
         */
        val feed: List<SaveableNewsResource>
    ) : NewsFeedUiState
}

@Preview
@Composable
fun NewsFeedLoadingPreview() {
    NiaTheme {
        LazyColumn {
            NewsFeed(
                feedState = NewsFeedUiState.Loading,
                showLoadingUIIfLoading = true,
                loadingContentDescription = 0,
                numberOfColumns = 1,
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}

@Preview
@Composable
fun NewsFeedSingleColumnPreview() {
    NiaTheme {
        LazyColumn {
            NewsFeed(
                feedState = NewsFeedUiState.Success(
                    previewNewsResources.map {
                        SaveableNewsResource(it, false)
                    }
                ),
                showLoadingUIIfLoading = true,
                loadingContentDescription = 0,
                numberOfColumns = 1,
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun NewsFeedTwoColumnPreview() {
    NiaTheme {
        LazyColumn {
            NewsFeed(
                feedState = NewsFeedUiState.Success(
                    (previewNewsResources + previewNewsResources).map {
                        SaveableNewsResource(it, false)
                    }
                ),
                showLoadingUIIfLoading = true,
                loadingContentDescription = 0,
                numberOfColumns = 2,
                onNewsResourcesCheckedChanged = { _, _ -> }
            )
        }
    }
}
