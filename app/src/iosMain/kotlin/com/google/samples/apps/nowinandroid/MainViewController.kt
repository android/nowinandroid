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

package com.google.samples.apps.nowinandroid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import com.google.samples.apps.nowinandroid.core.analytics.AnalyticsHelper
import com.google.samples.apps.nowinandroid.core.analytics.LocalAnalyticsHelper
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.ui.LocalTimeZone
import com.google.samples.apps.nowinandroid.core.ui.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.di.appModules
import com.google.samples.apps.nowinandroid.ui.NiaApp
import com.google.samples.apps.nowinandroid.ui.rememberNiaAppState
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

fun mainViewController() = ComposeUIViewController {
    App()
}

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(appModules)
        },
    ) {
        val networkMonitor: NetworkMonitor = koinInject()
        val timeZoneMonitor: TimeZoneMonitor = koinInject()
        val analyticsHelper: AnalyticsHelper = koinInject()
        val userNewsResourceRepository: UserNewsResourceRepository = koinInject()
        val viewModel: MainScreenViewModel = koinInject()
        val uiState: MainScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val darkTheme = shouldUseDarkTheme(uiState)

        val appState = rememberNiaAppState(
            networkMonitor = networkMonitor,
            userNewsResourceRepository = userNewsResourceRepository,
            timeZoneMonitor = timeZoneMonitor,
        )

        val currentTimeZone by appState.currentTimeZone.collectAsStateWithLifecycle()

        CompositionLocalProvider(
            LocalAnalyticsHelper provides analyticsHelper,
            LocalTimeZone provides currentTimeZone,
        ) {
            NiaTheme(
                darkTheme = darkTheme,
                androidTheme = shouldUseAndroidTheme(uiState),
                disableDynamicTheming = shouldDisableDynamicTheming(uiState),
            ) {
                NiaApp(appState)
            }
        }
    }
}
