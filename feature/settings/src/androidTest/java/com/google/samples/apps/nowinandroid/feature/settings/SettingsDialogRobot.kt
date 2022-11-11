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
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule

internal class SettingsDialogRobot(
    private val composeTestRule: AndroidComposeTestRule<
        ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {
    fun setContent(settingsUiState: SettingsUiState) {
        composeTestRule.setContent {
            SettingsDialog(
                settingsUiState = settingsUiState,
                onDismiss = { },
                onChangeThemeBrand = {},
                onChangeDarkThemeConfig = {}
            )
        }
    }

    fun loadingIndicatorExists() {
        composeTestRule
            .onNodeWithText(getString(R.string.loading))
            .assertExists()
    }

    fun settingExists(name: String) {
        composeTestRule
            .onNodeWithText(name)
            .assertExists()
    }

    fun settingIsSelected(name: String) {
        composeTestRule
            .onNodeWithText(name)
            .assertIsSelected()
    }

    fun getString(@StringRes stringId: Int) =
        composeTestRule.activity.resources.getString(stringId)
}
