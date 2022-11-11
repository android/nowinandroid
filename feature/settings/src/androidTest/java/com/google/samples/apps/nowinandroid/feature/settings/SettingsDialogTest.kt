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

package com.google.samples.apps.nowinandroid.feature.settings

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig.DARK
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.ANDROID
import com.google.samples.apps.nowinandroid.feature.settings.SettingsUiState.Loading
import com.google.samples.apps.nowinandroid.feature.settings.SettingsUiState.Success
import org.junit.Rule
import org.junit.Test

class SettingsDialogTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun whenLoading_showsLoadingText() {
        launchSettingsDialogRobot(composeTestRule, Loading) {
            loadingIndicatorExists()
        }
    }

    @Test
    fun whenStateIsSuccess_allSettingsAreDisplayed() {
        launchSettingsDialogRobot(
            composeTestRule,
            Success(
                UserEditableSettings(
                    brand = ANDROID,
                    darkThemeConfig = DARK
                )
            )
        ) {
            settingExists(getString(R.string.brand_default))
            settingExists(getString(R.string.brand_android))
            settingExists(getString(R.string.dark_mode_config_system_default))

            settingExists(getString(R.string.dark_mode_config_light))
            settingExists(getString(R.string.dark_mode_config_dark))

            settingIsSelected(getString(R.string.brand_android))
            settingIsSelected(getString(R.string.dark_mode_config_dark))
        }
    }

    @Test
    fun whenStateIsSuccess_allLinksAreDisplayed() {
        launchSettingsDialogRobot(
            composeTestRule,
            Success(
                UserEditableSettings(
                    brand = ANDROID,
                    darkThemeConfig = DARK
                )
            )
        ) {
            settingExists(getString(R.string.privacy_policy))
            settingExists(getString(R.string.licenses))
            settingExists(getString(R.string.brand_guidelines))
            settingExists(getString(R.string.feedback))
        }
    }
}

private fun launchSettingsDialogRobot(
    composeTestRule: AndroidComposeTestRule<
        ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    settingsUiState: SettingsUiState,
    func: SettingsDialogRobot.() -> Unit
) = SettingsDialogRobot(composeTestRule).apply {
    setContent(settingsUiState)
    func()
}
