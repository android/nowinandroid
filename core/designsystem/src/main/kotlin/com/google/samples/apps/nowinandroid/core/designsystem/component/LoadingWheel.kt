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

package com.google.samples.apps.nowinandroid.core.designsystem.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import kotlinx.coroutines.launch

@Composable
fun NiaLoadingWheel(
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wheel transition")

    // Specifies the float animation for slowly drawing out the lines on entering
    val startValue = if (LocalInspectionMode.current) 0F else 1F
    val floatAnimValues = (0 until NUM_OF_LINES).map { remember { Animatable(startValue) } }
    LaunchedEffect(floatAnimValues) {
        (0 until NUM_OF_LINES).map { index ->
            launch {
                floatAnimValues[index].animateTo(
                    targetValue = 0F,
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing,
                        delayMillis = 40 * index,
                    ),
                )
            }
        }
    }

    // Specifies the rotation animation of the entire Canvas composable
    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ROTATION_TIME, easing = LinearEasing),
        ),
        label = "wheel rotation animation",
    )

    // Specifies the color animation for the base-to-progress line color change
    val baseLineColor = MaterialTheme.colorScheme.onBackground
    val progressLineColor = MaterialTheme.colorScheme.inversePrimary

    val colorAnimValues = (0 until NUM_OF_LINES).map { index ->
        infiniteTransition.animateColor(
            initialValue = baseLineColor,
            targetValue = baseLineColor,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = ROTATION_TIME / 2
                    progressLineColor at ROTATION_TIME / NUM_OF_LINES / 2 using LinearEasing
                    baseLineColor at ROTATION_TIME / NUM_OF_LINES using LinearEasing
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(ROTATION_TIME / NUM_OF_LINES / 2 * index),
            ),
            label = "wheel color animation",
        )
    }

    // Draws out the LoadingWheel Canvas composable and sets the animations
    Canvas(
        modifier = modifier
            .size(48.dp)
            .padding(8.dp)
            .graphicsLayer { rotationZ = rotationAnim }
            .semantics { contentDescription = contentDesc }
            .testTag("loadingWheel"),
    ) {
        repeat(NUM_OF_LINES) { index ->
            rotate(degrees = index * 30f) {
                drawLine(
                    color = colorAnimValues[index].value,
                    // Animates the initially drawn 1 pixel alpha from 0 to 1
                    alpha = if (floatAnimValues[index].value < 1f) 1f else 0f,
                    strokeWidth = 4F,
                    cap = StrokeCap.Round,
                    start = Offset(size.width / 2, size.height / 4),
                    end = Offset(size.width / 2, floatAnimValues[index].value * size.height / 4),
                )
            }
        }
    }
}

@Composable
fun NiaOverlayLoadingWheel(
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(60.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.83f),
        modifier = modifier
            .size(60.dp),
    ) {
        NiaLoadingWheel(
            contentDesc = contentDesc,
        )
    }
}

@ThemePreviews
@Composable
fun NiaLoadingWheelPreview() {
    NiaTheme {
        Surface {
            NiaLoadingWheel(contentDesc = "LoadingWheel")
        }
    }
}

@ThemePreviews
@Composable
fun NiaOverlayLoadingWheelPreview() {
    NiaTheme {
        Surface {
            NiaOverlayLoadingWheel(contentDesc = "LoadingWheel")
        }
    }
}

private const val ROTATION_TIME = 12000
private const val NUM_OF_LINES = 12
