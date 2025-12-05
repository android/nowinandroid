/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.util

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.util.Consumer
import androidx.datastore.core.DataStore
import com.google.samples.apps.nowinandroid.core.datastore.DarkThemeConfigProto
import com.google.samples.apps.nowinandroid.core.datastore.UserPreferences
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Convenience wrapper for dark mode checking
 */
val Configuration.isSystemInDarkTheme
    get() = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Registers listener for configuration changes to retrieve whether system is in dark theme or not.
 * Immediately upon subscribing, it sends the current value and then registers listener for changes.
 */
fun ComponentActivity.isSystemInDarkTheme() = callbackFlow {
    channel.trySend(resources.configuration.isSystemInDarkTheme)

    val listener = Consumer<Configuration> {
        channel.trySend(it.isSystemInDarkTheme)
    }

    addOnConfigurationChangedListener(listener)

    awaitClose { removeOnConfigurationChangedListener(listener) }
}
    .distinctUntilChanged()
    .conflate()

/**
 * Converts [DarkThemeConfig] to AppCompat night mode constant.
 */
fun DarkThemeConfig.toNightMode(): Int = when (this) {
    DarkThemeConfig.FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    DarkThemeConfig.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
    DarkThemeConfig.DARK -> AppCompatDelegate.MODE_NIGHT_YES
}

/**
 * Maps a [DarkThemeConfig] value to the corresponding night mode setting
 * used by UiModeManager on Android 12 (API level 31) and above.
 */
@RequiresApi(Build.VERSION_CODES.S)
fun DarkThemeConfig.toUiNightMode(): Int = when(this) {
    DarkThemeConfig.FOLLOW_SYSTEM -> UiModeManager.MODE_NIGHT_AUTO
    DarkThemeConfig.LIGHT -> UiModeManager.MODE_NIGHT_NO
    DarkThemeConfig.DARK -> UiModeManager.MODE_NIGHT_YES
}

/**
 * Applies this [DarkThemeConfig] as default night mode.
 */
fun DarkThemeConfig.applyAsDefaultNightMode(context: Context) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        uiModeManager.setApplicationNightMode(toUiNightMode())
    } else {
        AppCompatDelegate.setDefaultNightMode(toNightMode())
    }
}

/**
 * Converts stored proto data into a DarkThemeConfig object.
 */
fun DarkThemeConfigProto.toDarkThemeConfig(): DarkThemeConfig = when (this) {
    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> DarkThemeConfig.LIGHT
    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
    else -> DarkThemeConfig.FOLLOW_SYSTEM
}

/**
 * Sets night mode from user prefs. Call in Application.onCreate() to show correct splash theme.
 */
fun Context.initializeNightModeFromPreferences(userPrefsDataStore: DataStore<UserPreferences>) {
    runBlocking {
        runCatching {
            val darkThemeConfig = userPrefsDataStore.data
                .first()
                .darkThemeConfig
                .toDarkThemeConfig()

            darkThemeConfig.applyAsDefaultNightMode(this@initializeNightModeFromPreferences)
        }.onFailure {
            DarkThemeConfig.FOLLOW_SYSTEM.applyAsDefaultNightMode(this@initializeNightModeFromPreferences)
        }
    }
}

/**
 * Observe theme changes and updates UiModeManager to prevent a bug where the first cold start
 * uses the previous theme, while later starts use the correct one.
 */
fun Context.observeNightModePreferences(
    userPrefsDataStore: DataStore<UserPreferences>,
    scope: CoroutineScope,
) {
    scope.launch {
        userPrefsDataStore.data
            .map { it.darkThemeConfig.toDarkThemeConfig() }
            .distinctUntilChanged()
            .drop(1) // Skip first emission (already handled by initializeNightModeFromPreferences)
            .collect { darkThemeConfig ->
                darkThemeConfig.applyAsDefaultNightMode(this@observeNightModePreferences)
            }
    }
}