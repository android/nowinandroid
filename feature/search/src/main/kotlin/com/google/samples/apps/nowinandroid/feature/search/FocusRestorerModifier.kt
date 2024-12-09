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

package com.google.samples.apps.nowinandroid.feature.search

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop

private fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Failed to find Activity!")
}

/**
 * Obtain IME visibility as a State which can trigger recomposition.
 * Note that this may not work if edge-to-edge layout is not enabled.
 */
@Composable
private fun imeVisibilityAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

/**
 * Add this modifier to a [TextField] to retain focus and keyboard visibility
 * after configuration is changed. This also works with multiple [TextField]s.
 */
internal fun Modifier.focusRestorer(focusRequester: FocusRequester, defaultFocus: Boolean = true) = composed {
    var hasFocus by rememberSaveable { mutableStateOf(defaultFocus) }
    // Cache the hasFocus value during initialization because `onFocusChanged` is called
    // when not focused during the initial composition.
    var shouldRequestFocus by remember { mutableStateOf(hasFocus) }
    // Save whether the focus is lost by the user's keyboard operation or not.
    // This is required because IME will be hidden during configuration changes
    // but [Activity.isChangingConfigurations] is always false
    // in IME visibility event collector.
    var lostFocusByIme by rememberSaveable { mutableStateOf(defaultFocus) }
    val activity = LocalContext.current.findActivity()
    val modifier = onFocusChanged {
        when {
            it.isFocused -> hasFocus = true
            // Ignore focus changes during configuration changes,
            // because focus is lost before the activity is killed.
            !activity.isChangingConfigurations -> hasFocus = false
        }
        lostFocusByIme = false
    }
        .focusRequester(focusRequester)

    val imeVisibilityState by imeVisibilityAsState()
    LaunchedEffect(Unit) {
        if (shouldRequestFocus) {
            focusRequester.requestFocus()
            shouldRequestFocus = false
        }
        snapshotFlow { imeVisibilityState }
            .drop(2) // Drop the first two initial values when launched
            .collectLatest { isVisible ->
                when {
                    hasFocus && !isVisible -> {
                        hasFocus = false
                        lostFocusByIme = true
                    }
                    lostFocusByIme && isVisible -> {
                        hasFocus = true
                        lostFocusByIme = false
                    }
                }
            }
    }
    modifier
}
