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
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig.DARK
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.ANDROID
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.DEFAULT
import com.google.samples.apps.nowinandroid.feature.settings.SettingsUiState.Loading
import com.google.samples.apps.nowinandroid.feature.settings.SettingsUiState.Success
import org.junit.Rule
import org.junit.Test

class SettingsDialogTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun getString(id: Int) = composeTestRule.activity.resources.getString(id)

    @Test
    fun whenLoading_showsLoadingText() {
        composeTestRule.setContent {
            SettingsDialog(
                settingsUiState = Loading,
                onDismiss = {},
                onChangeDynamicColorPreference = {},
                onChangeThemeBrand = {},
                onChangeDarkThemeConfig = {},
            )
        }

        composeTestRule
            .onNodeWithText(getString(R.string.feature_settings_loading))
            .assertExists()
    }

    @Test
    fun whenStateIsSuccess_allDefaultSettingsAreDisplayed() {
        composeTestRule.setContent {
            SettingsDialog(
                settingsUiState = Success(
                    UserEditableSettings(
                        brand = ANDROID,
                        useDynamicColor = false,
                        darkThemeConfig = DARK,
                    ),
                ),
                onDismiss = { },
                onChangeDynamicColorPreference = {},
                onChangeThemeBrand = {},
                onChangeDarkThemeConfig = {},
            )
        }

        // Check that all the possible settings are displayed.
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_brand_default)).assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_brand_android)).assertExists()
        composeTestRule.onNodeWithText(
            getString(R.string.feature_settings_dark_mode_config_system_default),
        ).assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dark_mode_config_light)).assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dark_mode_config_dark)).assertExists()

        // Check that the correct settings are selected.
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_brand_android)).assertIsSelected()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dark_mode_config_dark)).assertIsSelected()
    }

    @Test
    fun whenStateIsSuccess_supportsDynamicColor_usesDefaultBrand_DynamicColorOptionIsDisplayed() {
        composeTestRule.setContent {
            SettingsDialog(
                settingsUiState = Success(
                    UserEditableSettings(
                        brand = DEFAULT,
                        darkThemeConfig = DARK,
                        useDynamicColor = false,
                    ),
                ),
                supportDynamicColor = true,
                onDismiss = {},
                onChangeDynamicColorPreference = {},
                onChangeThemeBrand = {},
                onChangeDarkThemeConfig = {},
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_preference)).assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_yes)).assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_no)).assertExists()

        // Check that the correct default dynamic color setting is selected.
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_no)).assertIsSelected()
    }

    @Test
    fun whenStateIsSuccess_notSupportDynamicColor_DynamicColorOptionIsNotDisplayed() {
        composeTestRule.setContent {
            SettingsDialog(
                settingsUiState = Success(
                    UserEditableSettings(
                        brand = ANDROID,
                        darkThemeConfig = DARK,
                        useDynamicColor = false,
                    ),
                ),
                onDismiss = {},
                onChangeDynamicColorPreference = {},
                onChangeThemeBrand = {},
                onChangeDarkThemeConfig = {},
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_preference))
            .assertDoesNotExist()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_yes)).assertDoesNotExist()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_no)).assertDoesNotExist()
    }

    @Test
    fun whenStateIsSuccess_usesAndroidBrand_DynamicColorOptionIsNotDisplayed() {
        composeTestRule.setContent {
            SettingsDialog(
                settingsUiState = Success(
                    UserEditableSettings(
                        brand = ANDROID,
                        darkThemeConfig = DARK,
                        useDynamicColor = false,
                    ),
                ),
                onDismiss = {},
                onChangeDynamicColorPreference = {},
                onChangeThemeBrand = {},
                onChangeDarkThemeConfig = {},
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_preference))
            .assertDoesNotExist()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_yes)).assertDoesNotExist()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_dynamic_color_no)).assertDoesNotExist()
    }

    @Test
    fun whenStateIsSuccess_allLinksAreDisplayed() {
        composeTestRule.setContent {
            SettingsDialog(
                settingsUiState = Success(
                    UserEditableSettings(
                        brand = ANDROID,
                        darkThemeConfig = DARK,
                        useDynamicColor = false,
                    ),
                ),
                onDismiss = {},
                onChangeDynamicColorPreference = {},
                onChangeThemeBrand = {},
                onChangeDarkThemeConfig = {},
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_privacy_policy)).assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_licenses)).assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_brand_guidelines)).assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_feedback)).assertExists()
    }
}
