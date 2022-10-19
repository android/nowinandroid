/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.google.samples.apps.nowinandroid.feature.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig.DARK
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig.LIGHT
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.ANDROID
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.DEFAULT
import com.google.samples.apps.nowinandroid.feature.settings.SettingsUiState.Loading
import com.google.samples.apps.nowinandroid.feature.settings.SettingsUiState.Success

@ExperimentalLifecycleComposeApi
@Composable
internal fun SettingsDialog(
    viewModel: SettingsViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsDialog(
        onDismiss = onDismiss,
        settingsUiState = settingsUiState,
        onChangeThemeBrand = viewModel::updateThemeBrand,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
    )
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    settingsUiState: SettingsUiState,
    onChangeThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Divider()
                when (settingsUiState) {
                    Loading -> {
                        Text(
                            text = stringResource(R.string.loading),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                    is Success -> {
                        SettingsPanel(
                            settings = settingsUiState.settings,
                            onChangeThemeBrand = onChangeThemeBrand,
                            onChangeDarkThemeConfig = onChangeDarkThemeConfig
                        )
                    }
                }
                Divider(Modifier.padding(top = 8.dp))
                LegalPanel()
            }
        },
        confirmButton = {
            Text(
                text = "OK",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() }
            )
        }
    )
}

@Composable
private fun SettingsPanel(
    settings: UserEditableSettings,
    onChangeThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit
) {
    SettingsDialogSectionTitle(text = stringResource(R.string.theme))
    Column {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.brand_default),
            selected = settings.brand == DEFAULT,
            onClick = { onChangeThemeBrand(DEFAULT) }
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.brand_android),
            selected = settings.brand == ANDROID,
            onClick = { onChangeThemeBrand(ANDROID) }
        )
    }
    SettingsDialogSectionTitle(text = "Dark mode preference")
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_system_default),
            selected = settings.darkThemeConfig == FOLLOW_SYSTEM,
            onClick = { onChangeDarkThemeConfig(FOLLOW_SYSTEM) }
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_light),
            selected = settings.darkThemeConfig == LIGHT,
            onClick = { onChangeDarkThemeConfig(LIGHT) }
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_dark),
            selected = settings.darkThemeConfig == DARK,
            onClick = { onChangeDarkThemeConfig(DARK) }
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}


@Composable
private fun LegalPanel() {
    Row(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                TextLink(
                    text = stringResource(R.string.privacy_policy),
                    url = PRIVACY_POLICY_URL
                )
                Spacer(Modifier.width(16.dp))
                TextLink(
                    text = stringResource(R.string.licenses),
                    url = LICENSES_URL
                )
            }
        }
    }
}

@Composable
private fun TextLink(text: String, url: String) {

    val launchResourceIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val context = LocalContext.current

    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clickable {
                ContextCompat.startActivity(context, launchResourceIntent, null)
            }
    )
}

@Preview
@Composable
fun PreviewSettingsDialog() {
    NiaTheme {
        SettingsDialog(
            onDismiss = {},
            settingsUiState = Success(
                UserEditableSettings(
                    brand = DEFAULT,
                    darkThemeConfig = FOLLOW_SYSTEM
                )
            ),
            onChangeThemeBrand = { },
            onChangeDarkThemeConfig = { }
        )
    }
}

@Preview
@Composable
fun PreviewSettingsDialogLoading() {
    NiaTheme {
        SettingsDialog(
            onDismiss = {},
            settingsUiState = Loading,
            onChangeThemeBrand = { },
            onChangeDarkThemeConfig = { }
        )
    }
}

private const val PRIVACY_POLICY_URL = "https://policies.google.com/privacy"
private const val LICENSES_URL = "https://github.com/android/nowinandroid/blob/main/app/LICENSES.md"