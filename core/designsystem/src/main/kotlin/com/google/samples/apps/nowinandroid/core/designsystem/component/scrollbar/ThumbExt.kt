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

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue

/**
 * Remembers a function to react to [Scrollbar] thumb position displacements for a [LazyListState]
 * @param itemsAvailable the amount of items in the list.
 */
@Composable
fun LazyListState.rememberDraggableScroller(
    itemsAvailable: Int,
): (Float) -> Unit = rememberDraggableScroller(
    itemsAvailable = itemsAvailable,
    scroll = ::scrollToItem,
)

/**
 * Remembers a function to react to [Scrollbar] thumb position displacements for a [LazyGridState]
 * @param itemsAvailable the amount of items in the grid.
 */
@Composable
fun LazyGridState.rememberDraggableScroller(
    itemsAvailable: Int,
): (Float) -> Unit = rememberDraggableScroller(
    itemsAvailable = itemsAvailable,
    scroll = ::scrollToItem,
)

/**
 * Remembers a function to react to [Scrollbar] thumb position displacements for a
 * [LazyStaggeredGridState]
 * @param itemsAvailable the amount of items in the staggered grid.
 */
@Composable
fun LazyStaggeredGridState.rememberDraggableScroller(
    itemsAvailable: Int,
): (Float) -> Unit = rememberDraggableScroller(
    itemsAvailable = itemsAvailable,
    scroll = ::scrollToItem,
)

/**
 * Generic function to react to [Scrollbar] thumb displacements in a lazy layout.
 * @param itemsAvailable the total amount of items available to scroll in the layout.
 * @param scroll a function to be invoked when an index has been identified to scroll to.
 */
@Composable
private inline fun rememberDraggableScroller(
    itemsAvailable: Int,
    crossinline scroll: suspend (index: Int) -> Unit,
): (Float) -> Unit {
    var percentage by remember { mutableFloatStateOf(Float.NaN) }
    val itemCount by rememberUpdatedState(itemsAvailable)

    LaunchedEffect(percentage) {
        if (percentage.isNaN()) return@LaunchedEffect
        val indexToFind = (itemCount * percentage).toInt()
        scroll(indexToFind)
    }
    return remember {
        { newPercentage -> percentage = newPercentage }
    }
}
