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
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * The delay between scrolls when a user long presses on the scrollbar track to initiate a scroll
 * instead of dragging the scrollbar thumb.
 */
private const val SCROLLBAR_PRESS_DELAY_MS = 10L

/**
 * The percentage displacement of the scrollbar when scrolled by long presses on the scrollbar
 * track.
 */
private const val SCROLLBAR_PRESS_DELTA_PCT = 0.02f

class ScrollbarState {
    private var packedValue by mutableLongStateOf(0L)

    internal fun onScroll(stateValue: ScrollbarStateValue) {
        packedValue = stateValue.packedValue
    }

    /**
     * Returns the thumb size of the scrollbar as a percentage of the total track size
     */
    val thumbSizePercent
        get() = unpackFloat1(packedValue)

    /**
     * Returns the distance the thumb has traveled as a percentage of total track size
     */
    val thumbMovedPercent
        get() = unpackFloat2(packedValue)

    /**
     * Returns the max distance the thumb can travel as a percentage of total track size
     */
    val thumbTrackSizePercent
        get() = 1f - thumbSizePercent
}

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
 * Class definition for the core properties of a scroll bar
 */
@Immutable
@JvmInline
value class ScrollbarStateValue internal constructor(
    internal val packedValue: Long,
)

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
 * Creates a [ScrollbarStateValue] with the listed properties
 * @param thumbSizePercent the thumb size of the scrollbar as a percentage of the total track size.
 *  Refers to either the thumb width (for horizontal scrollbars)
 *  or height (for vertical scrollbars).
 * @param thumbMovedPercent the distance the thumb has traveled as a percentage of total
 * track size.
 */
fun scrollbarStateValue(
    thumbSizePercent: Float,
    thumbMovedPercent: Float,
) = ScrollbarStateValue(
    packFloats(
        val1 = thumbSizePercent,
        val2 = thumbMovedPercent,
    ),
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
 * A Composable for drawing a scrollbar
 * @param orientation the scroll direction of the scrollbar
 * @param state the state describing the position of the scrollbar
 * @param minThumbSize the minimum size of the scrollbar thumb
 * @param interactionSource allows for observing the state of the scroll bar
 * @param thumb a composable for drawing the scrollbar thumb
 * @param onThumbMoved an function for reacting to scroll bar displacements caused by direct
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
    onThumbMoved: ((Float) -> Unit)? = null,
) {
    // Using Offset.Unspecified and Float.NaN instead of null
    // to prevent unnecessary boxing of primitives
    var pressedOffset by remember { mutableStateOf(Offset.Unspecified) }
    var draggedOffset by remember { mutableStateOf(Offset.Unspecified) }

    // Used to immediately show drag feedback in the UI while the scrolling implementation
    // catches up
    var interactionThumbTravelPercent by remember { mutableFloatStateOf(Float.NaN) }

    var track by remember { mutableStateOf(ScrollbarTrack(packedValue = 0)) }

    // scrollbar track container
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
                        try {
                            // Wait for a long press before scrolling
                            withTimeout(viewConfiguration.longPressTimeoutMillis) {
                                tryAwaitRelease()
                            }
                        } catch (e: TimeoutCancellationException) {
                            // Start the press triggered scroll
                            val initialPress = PressInteraction.Press(offset)
                            interactionSource?.tryEmit(initialPress)

                            pressedOffset = offset
                            interactionSource?.tryEmit(
                                when {
                                    tryAwaitRelease() -> PressInteraction.Release(initialPress)
                                    else -> PressInteraction.Cancel(initialPress)
                                },
                            )

                            // End the press
                            pressedOffset = Offset.Unspecified
                        }
                    },
                )
            }
            // Process scrollbar drags
            .pointerInput(Unit) {
                var dragInteraction: DragInteraction.Start? = null
                val onDragStart: (Offset) -> Unit = { offset ->
                    val start = DragInteraction.Start()
                    dragInteraction = start
                    interactionSource?.tryEmit(start)
                    draggedOffset = offset
                }
                val onDragEnd: () -> Unit = {
                    dragInteraction?.let { interactionSource?.tryEmit(DragInteraction.Stop(it)) }
                    draggedOffset = Offset.Unspecified
                }
                val onDragCancel: () -> Unit = {
                    dragInteraction?.let { interactionSource?.tryEmit(DragInteraction.Cancel(it)) }
                    draggedOffset = Offset.Unspecified
                }
                val onDrag: (change: PointerInputChange, dragAmount: Float) -> Unit =
                    onDrag@{ _, delta ->
                        if (draggedOffset == Offset.Unspecified) return@onDrag
                        draggedOffset = when (orientation) {
                            Orientation.Vertical -> draggedOffset.copy(
                                y = draggedOffset.y + delta,
                            )

                            Orientation.Horizontal -> draggedOffset.copy(
                                x = draggedOffset.x + delta,
                            )
                        }
                    }

                when (orientation) {
                    Orientation.Horizontal -> detectHorizontalDragGestures(
                        onDragStart = onDragStart,
                        onDragEnd = onDragEnd,
                        onDragCancel = onDragCancel,
                        onHorizontalDrag = onDrag,
                    )

                    Orientation.Vertical -> detectVerticalDragGestures(
                        onDragStart = onDragStart,
                        onDragEnd = onDragEnd,
                        onDragCancel = onDragCancel,
                        onVerticalDrag = onDrag,
                    )
                }
            },
    ) {
        // scrollbar thumb container
        Layout(content = { thumb() }) { measurables, constraints ->
            val measurable = measurables.first()

            val thumbSizePx = max(
                a = state.thumbSizePercent * track.size,
                b = minThumbSize.toPx(),
            )

            val trackSizePx = when (state.thumbTrackSizePercent) {
                0f -> track.size
                else -> (track.size - thumbSizePx) / state.thumbTrackSizePercent
            }

            val thumbTravelPercent = max(
                a = min(
                    a = when {
                        interactionThumbTravelPercent.isNaN() -> state.thumbMovedPercent
                        else -> interactionThumbTravelPercent
                    },
                    b = state.thumbTrackSizePercent,
                ),
                b = 0f,
            )

            val thumbMovedPx = trackSizePx * thumbTravelPercent

            val y = when (orientation) {
                Horizontal -> 0
                Vertical -> thumbMovedPx.roundToInt()
            }
            val x = when (orientation) {
                Horizontal -> thumbMovedPx.roundToInt()
                Vertical -> 0
            }

            val updatedConstraints = when (orientation) {
                Horizontal -> {
                    constraints.copy(
                        minWidth = thumbSizePx.roundToInt(),
                        maxWidth = thumbSizePx.roundToInt(),
                    )
                }
                Vertical -> {
                    constraints.copy(
                        minHeight = thumbSizePx.roundToInt(),
                        maxHeight = thumbSizePx.roundToInt(),
                    )
                }
            }

            val placeable = measurable.measure(updatedConstraints)
            layout(placeable.width, placeable.height) {
                placeable.place(x, y)
            }
        }
    }

    if (onThumbMoved == null) return

    // Process presses
    LaunchedEffect(Unit) {
        snapshotFlow { pressedOffset }.collect { pressedOffset ->
            // Press ended, reset interactionThumbTravelPercent
            if (pressedOffset == Offset.Unspecified) {
                interactionThumbTravelPercent = Float.NaN
                return@collect
            }

            var currentThumbMovedPercent = state.thumbMovedPercent
            val destinationThumbMovedPercent = track.thumbPosition(
                dimension = orientation.valueOf(pressedOffset),
            )
            val isPositive = currentThumbMovedPercent < destinationThumbMovedPercent
            val delta = SCROLLBAR_PRESS_DELTA_PCT * if (isPositive) 1f else -1f

            while (currentThumbMovedPercent != destinationThumbMovedPercent) {
                currentThumbMovedPercent = when {
                    isPositive -> min(
                        a = currentThumbMovedPercent + delta,
                        b = destinationThumbMovedPercent,
                    )

                    else -> max(
                        a = currentThumbMovedPercent + delta,
                        b = destinationThumbMovedPercent,
                    )
                }
                onThumbMoved(currentThumbMovedPercent)
                interactionThumbTravelPercent = currentThumbMovedPercent
                delay(SCROLLBAR_PRESS_DELAY_MS)
            }
        }
    }

    // Process drags
    LaunchedEffect(Unit) {
        snapshotFlow { draggedOffset }.collect { draggedOffset ->
            if (draggedOffset == Offset.Unspecified) {
                interactionThumbTravelPercent = Float.NaN
                return@collect
            }
            val currentTravel = track.thumbPosition(
                dimension = orientation.valueOf(draggedOffset),
            )
            onThumbMoved(currentTravel)
            interactionThumbTravelPercent = currentTravel
        }
    }
}
