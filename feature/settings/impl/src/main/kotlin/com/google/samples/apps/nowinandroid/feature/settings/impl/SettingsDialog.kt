/*
 * Copyright 2025 The Android Open Source Project
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

@file:Suppress("ktlint:standard:max-line-length")

package com.google.samples.apps.nowinandroid.feature.settings.impl

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTextButton
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.designsystem.theme.supportsDynamicTheming
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.ANDROID
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.DEFAULT
import com.google.samples.apps.nowinandroid.core.ui.TrackScreenViewEvent
import com.google.samples.apps.nowinandroid.feature.settings.impl.R.string
import com.google.samples.apps.nowinandroid.feature.settings.impl.SettingsUiState.Loading
import com.google.samples.apps.nowinandroid.feature.settings.impl.SettingsUiState.Success


@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsDialog(
        onDismiss = onDismiss,
        settingsUiState = settingsUiState,
        onChangeThemeBrand = viewModel::updateThemeBrand,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
    )
}

@Composable
fun SettingsDialog(
    settingsUiState: SettingsUiState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onDismiss: () -> Unit,
    onChangeThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    val configuration = LocalConfiguration.current

    /**
     * usePlatformDefaultWidth = false is use as a temporary fix to allow
     * height recalculation during recomposition. This, however, causes
     * Dialog's to occupy full width in Compact mode. Therefore max width
     * is configured below. This should be removed when there's fix to
     * https://issuetracker.google.com/issues/221643630
     */
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = LocalWindowInfo.current.containerSize.width.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(string.feature_settings_impl_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                when (settingsUiState) {
                    Loading -> {
                        Text(
                            text = stringResource(string.feature_settings_impl_loading),
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }

                    is Success -> {
                        SettingsPanel(
                            settings = settingsUiState.settings,
                            supportDynamicColor = supportDynamicColor,
                            onChangeThemeBrand = onChangeThemeBrand,
                            onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                            onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                        )
                    }
                }
                HorizontalDivider(Modifier.padding(top = 8.dp))
                LinksPanel()
            }
            TrackScreenViewEvent(screenName = "Settings")
        },
        confirmButton = {
            NiaTextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Text(
                    text = stringResource(string.feature_settings_impl_dismiss_dialog_button_text),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
    )
}

// [ColumnScope] is used for using the [ColumnScope.AnimatedVisibility] extension overload composable.
@Composable
private fun ColumnScope.SettingsPanel(
    settings: UserEditableSettings,
    supportDynamicColor: Boolean,
    onChangeThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    SettingsDialogSectionTitle(text = stringResource(string.feature_settings_impl_theme))
    Row(Modifier.selectableGroup()) {
        SettingsDialogThemeRadioBtn(
            text = stringResource(string.feature_settings_impl_brand_default),
            selected = settings.brand == DEFAULT,
            onClick = { onChangeThemeBrand(DEFAULT) },
        )
        Spacer(Modifier.width(20.dp))
        SettingsDialogThemeRadioBtn(
            text = stringResource(string.feature_settings_impl_brand_android),
            selected = settings.brand == ANDROID,
            onClick = { onChangeThemeBrand(ANDROID) },
        )
    }
    AnimatedVisibility(visible = settings.brand == DEFAULT && supportDynamicColor) {
        Column {
            SettingsDialogSectionTitle(text = stringResource(string.feature_settings_impl_dynamic_color_preference))
            Column(Modifier.selectableGroup()) {

                SettingsDialogThemeDynamicColorRow(
                options = listOf( stringResource(string.feature_settings_impl_dynamic_color_yes), stringResource(string.feature_settings_impl_dynamic_color_no)),
                    selectedIndex = if (settings.useDynamicColor) 0 else 1,
                    onOptionSelected = { index ->
                        onChangeDynamicColorPreference(index == 0)
                    }
                )
            }
        }
    }
    SettingsDialogSectionTitle(text = stringResource(string.feature_settings_impl_dark_mode_preference))
    Column(Modifier.selectableGroup()) {
        val darkModeOptions = listOf(
            stringResource(string.feature_settings_impl_dark_mode_config_system_default),
            stringResource(string.feature_settings_impl_dark_mode_config_light),
            stringResource(string.feature_settings_impl_dark_mode_config_dark)
        )

        SettingsDialogThemeDarkModeRow(
            options = darkModeOptions,
            selectedIndex = when(settings.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> 0
                DarkThemeConfig.LIGHT -> 1
                DarkThemeConfig.DARK -> 2
            },
            onOptionSelected = { index ->val newConfig = when (index) {
                0 -> DarkThemeConfig.FOLLOW_SYSTEM
                1 -> DarkThemeConfig.LIGHT
                else -> DarkThemeConfig.DARK
            }
                onChangeDarkThemeConfig(newConfig)}
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeRadioBtn(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun SettingsDialogThemeDynamicColorRow(  options :List<String>,
    selectedIndex :Int,
    onOptionSelected : (Int) -> Unit
 ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, stringId ->
            val isSelected = index == selectedIndex

            if (isSelected) {
                Button(
                    onClick = { onOptionSelected(index) },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape
                ) {
                    Spacer(Modifier.width(3.dp))
                    Text(text = stringId)
                }
            } else {
                OutlinedButton(
                    onClick = { onOptionSelected(index) },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape 
                ) {
                    Spacer(Modifier.width(3.dp))
                    Text(text = stringId)                }
            }
        }
    }
 }

@Composable
fun SettingsDialogThemeDarkModeRow(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp)
    ) {
        options.forEachIndexed { index, stringId ->
            val isSelected = index == selectedIndex
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onOptionSelected(index) },
                selected = isSelected,
                label = {
                    Text(text = stringId, maxLines = 1)
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primary ,
                    activeContentColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveContainerColor = Color.Transparent,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurface
                ),
                icon = {}
            ) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LinksPanel() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        val uriHandler = LocalUriHandler.current
        NiaTextButton(
            onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) },
        ) {
            Text(text = stringResource(string.feature_settings_impl_privacy_policy))
        }
        val context = LocalContext.current
        NiaTextButton(
            onClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
        ) {
            Text(text = stringResource(string.feature_settings_impl_licenses))
        }
        NiaTextButton(
            onClick = { uriHandler.openUri(BRAND_GUIDELINES_URL) },
        ) {
            Text(text = stringResource(string.feature_settings_impl_brand_guidelines))
        }
        NiaTextButton(
            onClick = { uriHandler.openUri(FEEDBACK_URL) },
        ) {
            Text(text = stringResource(string.feature_settings_impl_feedback))
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsDialog() {
    NiaTheme {
        SettingsDialog(
            onDismiss = {},
            settingsUiState = Success(
                UserEditableSettings(
                    brand = DEFAULT,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    useDynamicColor = false,
                ),
            ),
            onChangeThemeBrand = {},
            onChangeDynamicColorPreference = {},
            onChangeDarkThemeConfig = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsDialogLoading() {
    NiaTheme {
        SettingsDialog(
            onDismiss = {},
            settingsUiState = Loading,
            onChangeThemeBrand = {},
            onChangeDynamicColorPreference = {},
            onChangeDarkThemeConfig = {},
        )
    }
}

private const val PRIVACY_POLICY_URL = "https://policies.google.com/privacy"
private const val BRAND_GUIDELINES_URL = "https://developer.android.com/distribute/marketing-tools/brand-guidelines"
private const val FEEDBACK_URL = "https://goo.gle/nia-app-feedback"
