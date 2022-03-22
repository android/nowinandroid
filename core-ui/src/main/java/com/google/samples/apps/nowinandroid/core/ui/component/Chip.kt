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

package com.google.samples.apps.nowinandroid.core.ui.component

import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import com.google.samples.apps.nowinandroid.core.ui.icon.NiaIcons

/**
 * Now in Android filter chip with included leading checked icon as well as text content slot.
 *
 * @param checked Whether the chip is currently checked.
 * @param onCheckedChange Called when the user clicks the chip and toggles checked.
 * @param modifier Modifier to be applied to the chip.
 * @param enabled Controls the enabled state of the chip. When `false`, this chip will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The text label content.
 */
@Composable
fun NiaFilterChip(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit
) {
    // TODO: Replace with Chip when available in Compose Material 3: b/197399111
    NiaOutlinedButton(
        onClick = { onCheckedChange(!checked) },
        modifier = Modifier
            .toggleable(value = checked, enabled = enabled, role = Role.Button, onValueChange = {})
            .then(modifier),
        enabled = enabled,
        small = true,
        border = NiaButtonDefaults.outlinedButtonBorder(
            enabled = enabled,
            disabledColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = if (checked) {
                    NiaButtonDefaults.DisabledButtonContentAlpha
                } else {
                    NiaButtonDefaults.DisabledButtonContainerAlpha
                }
            )
        ),
        colors = NiaButtonDefaults.outlinedButtonColors(
            containerColor = if (checked) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                Color.Transparent
            },
            disabledContainerColor = if (checked) {
                MaterialTheme.colorScheme.onBackground.copy(
                    alpha = NiaButtonDefaults.DisabledButtonContainerAlpha
                )
            } else {
                Color.Transparent
            }
        ),
        text = text,
        leadingIcon = if (checked) {
            {
                Icon(
                    imageVector = NiaIcons.Check,
                    contentDescription = null
                )
            }
        } else {
            null
        }
    )
}
