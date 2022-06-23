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

package com.google.samples.apps.nowinandroid.core.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Shapes
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme

/**
 * Now in Android filter chip with included leading checked icon as well as text content slot.
 *
 * @param selected Whether the chip is currently checked.
 * @param onSelectedChange Called when the user clicks the chip and toggles checked.
 * @param modifier Modifier to be applied to the chip.
 * @param enabled Controls the enabled state of the chip. When `false`, this chip will not be
 * clickable and will appear disabled to accessibility services.
 * @param label The text label content.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NiaFilterChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = { onSelectedChange(!selected) },
        label = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                label()
            }
        },
        modifier = modifier,
        enabled = enabled,
        selectedIcon = {
            Icon(
                imageVector = NiaIcons.Check,
                contentDescription = null
            )
        },
        shape = Shapes.Full,
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.onBackground,
            selectedBorderColor = MaterialTheme.colorScheme.onBackground,
            disabledBorderColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = NiaChipDefaults.DisabledChipContentAlpha
            ),
            disabledSelectedBorderColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = NiaChipDefaults.DisabledChipContentAlpha
            ),
            borderWidth = NiaChipDefaults.ChipBorderWidth,
            selectedBorderWidth = NiaChipDefaults.ChipBorderWidth
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onBackground,
            iconColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = if (selected) {
                MaterialTheme.colorScheme.onBackground.copy(
                    alpha = NiaChipDefaults.DisabledChipContainerAlpha
                )
            } else {
                Color.Transparent
            },
            disabledLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = NiaChipDefaults.DisabledChipContentAlpha
            ),
            disabledLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = NiaChipDefaults.DisabledChipContentAlpha
            ),
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onBackground,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

/**
 * Now in Android topic chip that displays a topic name text.
 *
 * @param enabled Whether the chip is currently enabled.
 * @param onClick Called when the user clicks the chip for more options.
 * @param modifier Modifier to be applied to the chip.
 * @param label The text label content.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NiaTopicChip(
    enabled: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit
) {
    SuggestionChip(
        enabled = enabled,
        onClick = { onClick },
        label = {
            ProvideTextStyle(value = MaterialTheme.typography.labelMedium) {
                label()
            }
        },
        modifier = modifier,
        shape = Shapes.Full,
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderColor = MaterialTheme.colorScheme.primaryContainer,
            disabledBorderColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = NiaChipDefaults.DisabledChipContainerAlpha
            ),
            borderWidth = NiaChipDefaults.ChipBorderWidth
        ),
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = NiaChipDefaults.DisabledChipContainerAlpha
            ),
            labelColor = MaterialTheme.colorScheme.onBackground,
            disabledLabelColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview("Enabled Topic Chip Preview")
@Composable
fun NiaEnabledTopicChipPreview() {
    NiaTheme {
        Surface {
            NiaTopicChip(
                enabled = true,
                onClick = { }
            ) {
                Text("Accessibility")
            }
        }
    }
}

@Preview("Disabled Topic Chip Preview")
@Composable
fun NiaDisabledTopicChipPreview() {
    NiaTheme {
        Surface {
            NiaTopicChip(
                enabled = false,
                onClick = { }
            ) {
                Text("Accessibility")
            }
        }
    }
}

/**
 * Now in Android chip default values.
 */
object NiaChipDefaults {
    const val DisabledChipContainerAlpha = 0.12f
    const val DisabledChipContentAlpha = 0.38f
    val ChipBorderWidth = 1.dp
}
