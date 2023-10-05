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
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlin.math.min

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
): ScrollbarState = produceState(
    initialValue = ScrollbarState.FULL,
    key1 = this,
    key2 = itemsAvailable,
) {
    snapshotFlow {
        if (itemsAvailable == 0) return@snapshotFlow null

        val visibleItemsInfo = layoutInfo.visibleItemsInfo
        if (visibleItemsInfo.isEmpty()) return@snapshotFlow null

        val firstIndex = min(
            a = interpolateFirstItemIndex(
                visibleItems = visibleItemsInfo,
                itemSize = { it.size },
                offset = { it.offset },
                nextItemOnMainAxis = { first -> visibleItemsInfo.find { it != first } },
                itemIndex = itemIndex,
            ),
            b = itemsAvailable.toFloat(),
        )
        if (firstIndex.isNaN()) return@snapshotFlow null

        val itemsVisible = visibleItemsInfo.floatSumOf { itemInfo ->
            itemVisibilityPercentage(
                itemSize = itemInfo.size,
                itemStartOffset = itemInfo.offset,
                viewportStartOffset = layoutInfo.viewportStartOffset,
                viewportEndOffset = layoutInfo.viewportEndOffset,
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
        ScrollbarState(
            thumbSizePercent = thumbSizePercent,
            thumbMovedPercent = when {
                layoutInfo.reverseLayout -> 1f - thumbTravelPercent
                else -> thumbTravelPercent
            },
        )
    }
        .filterNotNull()
        .distinctUntilChanged()
        .collect { value = it }
}.value

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
): ScrollbarState = produceState(
    initialValue = ScrollbarState.FULL,
    key1 = this,
    key2 = itemsAvailable,
) {
    snapshotFlow {
        if (itemsAvailable == 0) return@snapshotFlow null

        val visibleItemsInfo = layoutInfo.visibleItemsInfo
        if (visibleItemsInfo.isEmpty()) return@snapshotFlow null

        val firstIndex = min(
            a = interpolateFirstItemIndex(
                visibleItems = visibleItemsInfo,
                itemSize = { layoutInfo.orientation.valueOf(it.size) },
                offset = { layoutInfo.orientation.valueOf(it.offset) },
                nextItemOnMainAxis = { first ->
                    when (layoutInfo.orientation) {
                        Orientation.Vertical -> visibleItemsInfo.find {
                            it != first && it.row != first.row
                        }

                        Orientation.Horizontal -> visibleItemsInfo.find {
                            it != first && it.column != first.column
                        }
                    }
                },
                itemIndex = itemIndex,
            ),
            b = itemsAvailable.toFloat(),
        )
        if (firstIndex.isNaN()) return@snapshotFlow null

        val itemsVisible = visibleItemsInfo.floatSumOf { itemInfo ->
            itemVisibilityPercentage(
                itemSize = layoutInfo.orientation.valueOf(itemInfo.size),
                itemStartOffset = layoutInfo.orientation.valueOf(itemInfo.offset),
                viewportStartOffset = layoutInfo.viewportStartOffset,
                viewportEndOffset = layoutInfo.viewportEndOffset,
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
        ScrollbarState(
            thumbSizePercent = thumbSizePercent,
            thumbMovedPercent = when {
                layoutInfo.reverseLayout -> 1f - thumbTravelPercent
                else -> thumbTravelPercent
            },
        )
    }
        .filterNotNull()
        .distinctUntilChanged()
        .collect { value = it }
}.value

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
): ScrollbarState = produceState(
    initialValue = ScrollbarState.FULL,
    key1 = this,
    key2 = itemsAvailable,
) {
    snapshotFlow {
        if (itemsAvailable == 0) return@snapshotFlow null

        val visibleItemsInfo = layoutInfo.visibleItemsInfo
        if (visibleItemsInfo.isEmpty()) return@snapshotFlow null

        val firstIndex = min(
            a = interpolateFirstItemIndex(
                visibleItems = visibleItemsInfo,
                itemSize = { layoutInfo.orientation.valueOf(it.size) },
                offset = { layoutInfo.orientation.valueOf(it.offset) },
                nextItemOnMainAxis = { first ->
                    visibleItemsInfo.find { it != first && it.lane == first.lane }
                },
                itemIndex = itemIndex,
            ),
            b = itemsAvailable.toFloat(),
        )
        if (firstIndex.isNaN()) return@snapshotFlow null

        val itemsVisible = visibleItemsInfo.floatSumOf { itemInfo ->
            itemVisibilityPercentage(
                itemSize = layoutInfo.orientation.valueOf(itemInfo.size),
                itemStartOffset = layoutInfo.orientation.valueOf(itemInfo.offset),
                viewportStartOffset = layoutInfo.viewportStartOffset,
                viewportEndOffset = layoutInfo.viewportEndOffset,
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
        ScrollbarState(
            thumbSizePercent = thumbSizePercent,
            thumbMovedPercent = thumbTravelPercent,
        )
    }
        .filterNotNull()
        .distinctUntilChanged()
        .collect { value = it }
}.value

private inline fun <T> List<T>.floatSumOf(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}
