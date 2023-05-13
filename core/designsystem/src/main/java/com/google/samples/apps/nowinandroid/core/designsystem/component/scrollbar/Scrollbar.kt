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

package com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

private const val SCROLLBAR_PRESS_DELAY = 10L
private const val SCROLLBAR_PRESS_DELTA = 0.02f

/**
 * Class definition for the core properties of a scroll bar
 */
@Immutable
@JvmInline
value class ScrollbarState internal constructor(
    internal val packedValue: Long,
) {
    companion object {
        val FULL = ScrollbarState(
            thumbSizePercent = 1f,
            thumbDisplacementPercent = 0f,
        )
    }
}

/**
 * Class definition for the core properties of a scroll bar track
 */
@Immutable
@JvmInline
private value class ScrollbarTrack(
    val packedValue: Long,
) {
    constructor(
        max: Float,
        min: Float,
    ) : this(packFloats(max, min))
}

/**
 * Creates a scrollbar state with the listed properties
 * @param thumbSizePercent the thumb size of the scrollbar as a percentage of the total track size
 * @param thumbDisplacementPercent the distance the thumb has traveled as a percentage of total
 * track size
 */
fun ScrollbarState(
    thumbSizePercent: Float,
    thumbDisplacementPercent: Float,
) = ScrollbarState(
    packFloats(
        val1 = thumbSizePercent,
        val2 = thumbDisplacementPercent,
    ),
)

/**
 * Returns the thumb size of the scrollbar as a percentage of the total track size
 */
val ScrollbarState.thumbSizePercent
    get() = unpackFloat1(packedValue)

/**
 * Returns the distance the thumb has traveled as a percentage of total track size
 */
val ScrollbarState.thumbDisplacementPercent
    get() = unpackFloat2(packedValue)

/**
 * Returns the size of the scrollbar track in pixels
 */
private val ScrollbarTrack.size
    get() = unpackFloat2(packedValue) - unpackFloat1(packedValue)

/**
 * Returns the position of the scrollbar thumb on the track as a percentage
 */
private fun ScrollbarTrack.thumbPosition(
    dimension: Float,
): Float = max(
    a = min(
        a = dimension / size,
        b = 1f,
    ),
    b = 0f,
)

/**
 * Returns the value of [offset] along the axis specified by [this]
 */
internal fun Orientation.valueOf(offset: Offset) = when (this) {
    Orientation.Horizontal -> offset.x
    Orientation.Vertical -> offset.y
}

/**
 * Returns the value of [intSize] along the axis specified by [this]
 */
internal fun Orientation.valueOf(intSize: IntSize) = when (this) {
    Orientation.Horizontal -> intSize.width
    Orientation.Vertical -> intSize.height
}

/**
 * Returns the value of [intOffset] along the axis specified by [this]
 */
internal fun Orientation.valueOf(intOffset: IntOffset) = when (this) {
    Orientation.Horizontal -> intOffset.x
    Orientation.Vertical -> intOffset.y
}

/**
 * A Composable for drawing a Scrollbar
 * @param orientation the scroll direction of the scrollbar
 * @param state the state describing the position of the scrollbar
 * @param minThumbSize the minimum size of the scrollbar thumb
 * @param interactionSource allows for observing the state of the scroll bar
 * @param thumb a composable for drawing the scrollbar thumb
 * @param onThumbDisplaced an function for reacting to scroll bar displacements caused by direct
 * interactions on the scrollbar thumb by the user, for example implementing a fast scroll
 */
@Composable
fun Scrollbar(
    modifier: Modifier = Modifier,
    orientation: Orientation,
    state: ScrollbarState,
    minThumbSize: Dp = 40.dp,
    interactionSource: MutableInteractionSource? = null,
    thumb: @Composable () -> Unit,
    onThumbDisplaced: ((Float) -> Unit)? = null,
) {
    val localDensity = LocalDensity.current

    // Using Offset.Unspecified and Float.NaN instead of null
    // to prevent unnecessary boxing of primitives
    var pressedOffset by remember { mutableStateOf(Offset.Unspecified) }
    var draggedOffset by remember { mutableStateOf(Offset.Unspecified) }

    // Used to immediately show drag feedback in the UI while the scrolling implementation
    // catches up
    var interactionThumbTravelPercent by remember { mutableStateOf(Float.NaN) }

    var track by remember { mutableStateOf(ScrollbarTrack(packedValue = 0)) }

    val thumbTravelPercent = when {
        interactionThumbTravelPercent.isNaN() -> state.thumbDisplacementPercent
        else -> interactionThumbTravelPercent
    }
    val thumbSizePx = max(
        a = state.thumbSizePercent * track.size,
        b = with(localDensity) { minThumbSize.toPx() },
    )
    val thumbSizeDp by animateDpAsState(
        targetValue = with(localDensity) { thumbSizePx.toDp() },
        label = "scrollbar thumb size",
    )
    val thumbDisplacementPx = min(
        a = track.size * thumbTravelPercent,
        b = track.size - thumbSizePx,
    )
    val draggableState = rememberDraggableState { delta ->
        if (draggedOffset == Offset.Unspecified) return@rememberDraggableState

        draggedOffset = when (orientation) {
            Orientation.Vertical -> draggedOffset.copy(y = draggedOffset.y + delta)
            Orientation.Horizontal -> draggedOffset.copy(x = draggedOffset.x + delta)
        }
    }

    // Scrollbar track container
    Box(
        modifier = modifier
            .run {
                val withHover = interactionSource?.let(::hoverable) ?: this
                when (orientation) {
                    Orientation.Vertical -> withHover.fillMaxHeight()
                    Orientation.Horizontal -> withHover.fillMaxWidth()
                }
            }
            .onGloballyPositioned { coordinates ->
                val scrollbarStartCoordinate = orientation.valueOf(coordinates.positionInRoot())
                track = ScrollbarTrack(
                    max = scrollbarStartCoordinate,
                    min = scrollbarStartCoordinate + orientation.valueOf(coordinates.size),
                )
            }
            // Process scrollbar presses
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val initialPress = PressInteraction.Press(offset)
                        interactionSource?.tryEmit(initialPress)

                        // Start the press
                        pressedOffset = offset

                        interactionSource?.tryEmit(
                            when {
                                tryAwaitRelease() -> PressInteraction.Release(initialPress)
                                else -> PressInteraction.Cancel(initialPress)
                            },
                        )

                        // End the press
                        pressedOffset = Offset.Unspecified
                    },
                )
            }
            // Process scrollbar drags
            .draggable(
                state = draggableState,
                orientation = orientation,
                interactionSource = interactionSource,
                onDragStarted = { startedPosition: Offset ->
                    draggedOffset = startedPosition
                },
                onDragStopped = {
                    draggedOffset = Offset.Unspecified
                },
            ),
    ) {
        val scrollbarThumbDisplacement = max(
            a = with(localDensity) { thumbDisplacementPx.toDp() },
            b = 0.dp,
        )
        // Scrollbar thumb container
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .run {
                    when (orientation) {
                        Orientation.Horizontal -> width(thumbSizeDp)
                        Orientation.Vertical -> height(thumbSizeDp)
                    }
                }
                .offset(
                    y = when (orientation) {
                        Orientation.Horizontal -> 0.dp
                        Orientation.Vertical -> scrollbarThumbDisplacement
                    },
                    x = when (orientation) {
                        Orientation.Horizontal -> scrollbarThumbDisplacement
                        Orientation.Vertical -> 0.dp
                    },
                ),
        ) {
            thumb()
        }
    }

    if (onThumbDisplaced == null) return

    // State that will be read inside the effects that follow
    // but will not cause re-triggering of them
    val updatedState by rememberUpdatedState(state)

    // Process presses
    LaunchedEffect(pressedOffset) {
        // Press ended, reset interactionThumbTravelPercent
        if (pressedOffset == Offset.Unspecified) {
            interactionThumbTravelPercent = Float.NaN
            return@LaunchedEffect
        }

        var currentThumbDisplacement = updatedState.thumbDisplacementPercent
        val destinationThumbDisplacement = track.thumbPosition(
            dimension = orientation.valueOf(pressedOffset),
        )
        val isPositive = currentThumbDisplacement < destinationThumbDisplacement
        val delta = SCROLLBAR_PRESS_DELTA * if (isPositive) 1f else -1f

        while (currentThumbDisplacement != destinationThumbDisplacement) {
            currentThumbDisplacement = when {
                isPositive -> min(
                    a = currentThumbDisplacement + delta,
                    b = destinationThumbDisplacement,
                )
                else -> max(
                    a = currentThumbDisplacement + delta,
                    b = destinationThumbDisplacement,
                )
            }
            onThumbDisplaced(currentThumbDisplacement)
            interactionThumbTravelPercent = currentThumbDisplacement
            delay(SCROLLBAR_PRESS_DELAY)
        }
    }

    // Process drags
    LaunchedEffect(draggedOffset) {
        if (draggedOffset == Offset.Unspecified) {
            interactionThumbTravelPercent = Float.NaN
            return@LaunchedEffect
        }
        val currentTravel = track.thumbPosition(
            dimension = orientation.valueOf(draggedOffset),
        )
        onThumbDisplaced(currentTravel)
        interactionThumbTravelPercent = currentTravel
    }
}
