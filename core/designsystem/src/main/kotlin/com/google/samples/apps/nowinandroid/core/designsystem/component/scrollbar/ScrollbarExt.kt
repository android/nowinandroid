/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlin.math.min

/**
 * Calculates a [ScrollbarState] driven by the changes in a [LazyState].
 *
 * @param itemsAvailable the total amount of items available to scroll in the [LazyState].
 * @param itemSize a lookup function for the size of an item in the layout.
 * @param offset a lookup function for the offset of an item relative to the start of the view port.
 * @param nextItemOnMainAxis a lookup function for the next item on the main axis in the direction
 *  of the scroll.
 * @param itemSize next [LazyStateItem] on the main axis of [LazyState], null if none.
 * @param index a lookup function for index of an item in the layout relative to
 * @param viewportStartOffset a lookup function for the start offset of the view port
 * @param viewportEndOffset a lookup function for the end offset of the view port
 * @param reverseLayout a lookup function for the reverseLayout of [LazyState].
 */
@Composable
internal inline fun <LazyState : ScrollableState, LazyStateItem> LazyState.genericScrollbarState(
    itemsAvailable: Int,
    crossinline visibleItems: () -> List<LazyStateItem>,
    crossinline itemSize: LazyState.(LazyStateItem) -> Int,
    crossinline offset: LazyState.(LazyStateItem) -> Int,
    crossinline nextItemOnMainAxis: LazyState.(LazyStateItem) -> LazyStateItem?,
    crossinline index: (LazyStateItem) -> Int,
    crossinline viewportStartOffset: () -> Int,
    crossinline viewportEndOffset: () -> Int,
    crossinline reverseLayout: () -> Boolean = { false },
): ScrollbarState {
    val state = remember { ScrollbarState() }
    LaunchedEffect(this, itemsAvailable) {
        snapshotFlow {
            if (itemsAvailable == 0) return@snapshotFlow null

            val visibleItemsInfo = visibleItems()
            if (visibleItemsInfo.isEmpty()) return@snapshotFlow null

            val firstIndex = min(
                a = interpolateFirstItemIndex(
                    visibleItems = visibleItemsInfo,
                    itemSize = itemSize,
                    offset = offset,
                    nextItemOnMainAxis = nextItemOnMainAxis,
                    itemIndex = index,
                ),
                b = itemsAvailable.toFloat(),
            )
            if (firstIndex.isNaN()) return@snapshotFlow null

            val itemsVisible = visibleItemsInfo.floatSumOf { itemInfo ->
                itemVisibilityPercentage(
                    itemSize = itemSize(itemInfo),
                    itemStartOffset = offset(itemInfo),
                    viewportStartOffset = viewportStartOffset(),
                    viewportEndOffset = viewportEndOffset(),
                )
            }

            val thumbTravelPercent = min(
                a = firstIndex / itemsAvailable,
                b = 1f,
            )
            val thumbSizePercent = min(
                a = itemsVisible / itemsAvailable,
                b = 1f,
            )
            scrollbarStateValue(
                thumbSizePercent = thumbSizePercent,
                thumbMovedPercent = when {
                    reverseLayout() -> 1f - thumbTravelPercent
                    else -> thumbTravelPercent
                },
            )
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { state.onScroll(it) }
    }
    return state
}

/**
 * Calculates a [ScrollbarState] driven by the changes in a [LazyListState].
 *
 * @param itemsAvailable the total amount of items available to scroll in the lazy list.
 * @param itemIndex a lookup function for index of an item in the list relative to [itemsAvailable].
 */
@Composable
fun LazyListState.scrollbarState(
    itemsAvailable: Int,
    itemIndex: (LazyListItemInfo) -> Int = LazyListItemInfo::index,
) = genericScrollbarState(
    itemsAvailable = itemsAvailable,
    visibleItems = { layoutInfo.visibleItemsInfo },
    itemSize = { it.size },
    offset = { it.offset },
    nextItemOnMainAxis = { first -> layoutInfo.visibleItemsInfo.find { it != first } },
    index = itemIndex,
    viewportStartOffset = { layoutInfo.viewportStartOffset },
    viewportEndOffset = { layoutInfo.viewportEndOffset },
    reverseLayout = { layoutInfo.reverseLayout },
)

/**
 * Calculates a [ScrollbarState] driven by the changes in a [LazyGridState]
 *
 * @param itemsAvailable the total amount of items available to scroll in the grid.
 * @param itemIndex a lookup function for index of an item in the grid relative to [itemsAvailable].
 */
@Composable
fun LazyGridState.scrollbarState(
    itemsAvailable: Int,
    itemIndex: (LazyGridItemInfo) -> Int = LazyGridItemInfo::index,
) = genericScrollbarState(
    itemsAvailable = itemsAvailable,
    visibleItems = { layoutInfo.visibleItemsInfo },
    itemSize = { layoutInfo.orientation.valueOf(it.size) },
    offset = { layoutInfo.orientation.valueOf(it.offset) },
    nextItemOnMainAxis = { first ->
        when (layoutInfo.orientation) {
            Orientation.Vertical -> layoutInfo.visibleItemsInfo.find {
                it != first && it.row != first.row
            }

            Orientation.Horizontal -> layoutInfo.visibleItemsInfo.find {
                it != first && it.column != first.column
            }
        }
    },
    index = itemIndex,
    viewportStartOffset = { layoutInfo.viewportStartOffset },
    viewportEndOffset = { layoutInfo.viewportEndOffset },
    reverseLayout = { layoutInfo.reverseLayout },
)

/**
 * Remembers a [ScrollbarState] driven by the changes in a [LazyStaggeredGridState]
 *
 * @param itemsAvailable the total amount of items available to scroll in the staggered grid.
 * @param itemIndex a lookup function for index of an item in the staggered grid relative
 * to [itemsAvailable].
 */
@Composable
fun LazyStaggeredGridState.scrollbarState(
    itemsAvailable: Int,
    itemIndex: (LazyStaggeredGridItemInfo) -> Int = LazyStaggeredGridItemInfo::index,
) = genericScrollbarState(
    itemsAvailable = itemsAvailable,
    visibleItems = { layoutInfo.visibleItemsInfo },
    itemSize = { layoutInfo.orientation.valueOf(it.size) },
    offset = { layoutInfo.orientation.valueOf(it.offset) },
    nextItemOnMainAxis = { first ->
        layoutInfo.visibleItemsInfo.find { it != first && it.lane == first.lane }
    },
    index = itemIndex,
    viewportStartOffset = { layoutInfo.viewportStartOffset },
    viewportEndOffset = { layoutInfo.viewportEndOffset },
)

private inline fun <T> List<T>.floatSumOf(selector: (T) -> Float): Float =
    fold(initial = 0f) { accumulator, listItem -> accumulator + selector(listItem) }
