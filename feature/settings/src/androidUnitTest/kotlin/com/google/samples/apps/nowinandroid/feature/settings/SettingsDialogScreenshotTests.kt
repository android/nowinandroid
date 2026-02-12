/*
 * Copyright 2026 The Android Open Source Project
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
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DarkMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultRoborazziOptions
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultTestDevices
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

/**
 * Screenshot tests for the [SettingsDialog].
 *
 * SettingsDialog uses AlertDialog which creates multiple root nodes in the compose tree.
 * Therefore we capture the dialog root node directly instead of using [captureMultiDevice].
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class SettingsDialogScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun captureDialogForDevice(
        deviceName: String,
        deviceSpec: String,
        screenshotName: String,
        darkMode: Boolean = false,
        body: @Composable () -> Unit,
    ) {
        val specs = deviceSpec.substringAfter("spec:")
            .split(",").map { it.split("=") }.associate { it[0] to it[1] }
        val width = specs["width"]?.toInt() ?: 640
        val height = specs["height"]?.toInt() ?: 480
        val dpi = specs["dpi"]?.toInt() ?: 480

        RuntimeEnvironment.setQualifiers("w${width}dp-h${height}dp-${dpi}dpi")

        composeTestRule.activity.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                DeviceConfigurationOverride(
                    override = DeviceConfigurationOverride.Companion.DarkMode(darkMode),
                ) {
                    body()
                }
            }
        }

        composeTestRule.mainClock.autoAdvance = false

        // AlertDialog creates 2 root nodes; capture the dialog root (index 1)
        composeTestRule.onAllNodes(isRoot())[1]
            .captureRoboImage(
                "src/test/screenshots/${screenshotName}_$deviceName.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    private fun captureDialogMultiDevice(
        screenshotName: String,
        body: @Composable () -> Unit,
    ) {
        DefaultTestDevices.entries.forEach {
            captureDialogForDevice(
                deviceName = it.description,
                deviceSpec = it.spec,
                screenshotName = screenshotName,
                body = body,
            )
        }
    }

    @Test
    fun settingsDialogLoaded() {
        captureDialogMultiDevice("SettingsDialogLoaded") {
            NiaTheme {
                SettingsDialog(
                    settingsUiState = SettingsUiState.Success(
                        settings = UserEditableSettings(
                            brand = ThemeBrand.DEFAULT,
                            useDynamicColor = false,
                            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                        ),
                    ),
                    supportDynamicColor = false,
                    onDismiss = {},
                    onChangeThemeBrand = {},
                    onChangeDynamicColorPreference = {},
                    onChangeDarkThemeConfig = {},
                )
            }
        }
    }

    @Test
    fun settingsDialogLoading() {
        captureDialogMultiDevice("SettingsDialogLoading") {
            NiaTheme {
                SettingsDialog(
                    settingsUiState = SettingsUiState.Loading,
                    supportDynamicColor = false,
                    onDismiss = {},
                    onChangeThemeBrand = {},
                    onChangeDynamicColorPreference = {},
                    onChangeDarkThemeConfig = {},
                )
            }
        }
    }

    @Test
    fun settingsDialogLoaded_dark() {
        captureDialogForDevice(
            deviceName = "phone_dark",
            deviceSpec = DefaultTestDevices.PHONE.spec,
            screenshotName = "SettingsDialogLoaded",
            darkMode = true,
        ) {
            NiaTheme {
                SettingsDialog(
                    settingsUiState = SettingsUiState.Success(
                        settings = UserEditableSettings(
                            brand = ThemeBrand.DEFAULT,
                            useDynamicColor = false,
                            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                        ),
                    ),
                    supportDynamicColor = false,
                    onDismiss = {},
                    onChangeThemeBrand = {},
                    onChangeDynamicColorPreference = {},
                    onChangeDarkThemeConfig = {},
                )
            }
        }
    }
}
