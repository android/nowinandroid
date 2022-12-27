package com.google.samples.apps.nowinandroid.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass.Initial
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope

fun Modifier.disableSplitMotionEvents() = pointerInput(Unit) {
    coroutineScope {
        var currentId: Long = -1L
        awaitPointerEventScope {
            while (true) {
                awaitPointerEvent(Initial).changes.forEach { inputChange ->
                    when {
                        inputChange.pressed && currentId == -1L -> {
                            currentId = inputChange.id.value
                        }
                        inputChange.pressed.not() && currentId == inputChange.id.value -> {
                            currentId = -1
                        }
                        inputChange.id.value != currentId && currentId != -1L -> {
                            inputChange.consume()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}