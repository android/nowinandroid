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

package com.google.samples.apps.nowinandroid.feature.settings

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig.DARK
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig.LIGHT
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.ANDROID
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.DEFAULT
import com.google.samples.apps.nowinandroid.core.testing.util.captureDialog
import com.google.samples.apps.nowinandroid.feature.settings.SettingsUiState.Success
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(application = HiltTestApplication::class, qualifiers = "w480dp-h960dp-480dpi")
class SettingsDialogScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val defaultSettingsUiState = Success(
        UserEditableSettings(
            brand = DEFAULT,
            darkThemeConfig = LIGHT,
            useDynamicColor = false,
        ),
    )

    @Test
    fun settingsDialogLoading() {
        composeTestRule.captureDialog(
            screenshotName = "SettingsDialogLoading",
            nodeTag = "SettingsDialog",
        ) {
            SettingsDialogDefaultTheme(
                settingsUiState = SettingsUiState.Loading,
            )
        }
    }

    @Test
    fun settingsDialogLoadingDark() {
        composeTestRule.captureDialog(
            screenshotName = "SettingsDialogLoadingDark",
            nodeTag = "SettingsDialog",
        ) {
            SettingsDialogDefaultTheme(
                darkMode = true,
                settingsUiState = SettingsUiState.Loading,
            )
        }
    }

    @Test
    fun settingsDialogDefaultTheme() {
        composeTestRule.captureDialog(
            screenshotName = "SettingsDialogDefaultTheme",
            nodeTag = "SettingsDialog",
        ) {
            SettingsDialogDefaultTheme()
        }
    }

    @Test
    fun settingsDialogDefaultThemeDark() {
        composeTestRule.captureDialog(
            screenshotName = "SettingsDialogDefaultThemeDark",
            nodeTag = "SettingsDialog",
        ) {
            SettingsDialogDefaultTheme(
                darkMode = true,
                settingsUiState = Success(
                    defaultSettingsUiState.settings.copy(
                        darkThemeConfig = DARK,
                    ),
                ),
            )
        }
    }

    @Test
    fun settingsDialogDynamicColor() {
        composeTestRule.captureDialog(
            screenshotName = "SettingsDialogDynamicColor",
            nodeTag = "SettingsDialog",
        ) {
            SettingsDialogDefaultTheme(
                disableDynamicTheming = false,
                settingsUiState = Success(
                    defaultSettingsUiState.settings.copy(
                        useDynamicColor = true,
                    ),
                ),
            )
        }
    }

    @Test
    fun settingsDialogDynamicColorDark() {
        composeTestRule.captureDialog(
            screenshotName = "SettingsDialogDynamicColorDark",
            nodeTag = "SettingsDialog",
        ) {
            SettingsDialogDefaultTheme(
                darkMode = true,
                disableDynamicTheming = false,
                settingsUiState = Success(
                    defaultSettingsUiState.settings.copy(
                        darkThemeConfig = DARK,
                        useDynamicColor = true,
                    ),
                ),
            )
        }
    }

    @Test
    fun settingsDialogAndroidTheme() {
        composeTestRule.captureDialog(
            screenshotName = "SettingsDialogAndroidTheme",
            nodeTag = "SettingsDialog",
        ) {
            SettingsDialogDefaultTheme(
                androidTheme = true,
                settingsUiState = Success(
                    defaultSettingsUiState.settings.copy(
                        brand = ANDROID,
                    ),
                ),
            )
        }
    }

    @Test
    fun settingsDialogAndroidThemeDark() {
        composeTestRule.captureDialog(
            screenshotName = "SettingsDialogAndroidThemeDark",
            nodeTag = "SettingsDialog",
        ) {
            SettingsDialogDefaultTheme(
                darkMode = true,
                androidTheme = true,
                settingsUiState = Success(
                    defaultSettingsUiState.settings.copy(
                        darkThemeConfig = DARK,
                        brand = ANDROID,
                    ),
                ),
            )
        }
    }

    @Composable
    private fun SettingsDialogDefaultTheme(
        darkMode: Boolean = false,
        androidTheme: Boolean = false,
        disableDynamicTheming: Boolean = true,
        settingsUiState: SettingsUiState = defaultSettingsUiState,
    ) {
        NiaTheme(
            darkTheme = darkMode,
            androidTheme = androidTheme,
            disableDynamicTheming = disableDynamicTheming,
        ) {
            SettingsDialog(
                modifier = Modifier.testTag("SettingsDialog"),
                onDismiss = {},
                settingsUiState = settingsUiState,
                onChangeThemeBrand = {},
                onChangeDynamicColorPreference = {},
                onChangeDarkThemeConfig = {},
            )
        }
    }
}
