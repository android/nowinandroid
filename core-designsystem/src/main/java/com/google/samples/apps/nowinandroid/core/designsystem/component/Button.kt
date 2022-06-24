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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Now in Android filled button with generic content slot. Wraps Material 3 [Button].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [NiaButtonDefaults.filledButtonColors].
 * @param contentPadding The spacing values to apply internally between the container and the
 * content. See [NiaButtonDefaults.buttonContentPadding].
 * @param content The button content.
 */
@Composable
fun NiaFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = NiaButtonDefaults.filledButtonColors(),
    contentPadding: PaddingValues = NiaButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = NiaButtonDefaults.SmallButtonHeight)
        } else {
            modifier
        },
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        }
    )
}

/**
 * Now in Android filled button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [NiaButtonDefaults.filledButtonColors].
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun NiaFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = NiaButtonDefaults.filledButtonColors(),
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    NiaFilledButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        small = small,
        colors = colors,
        contentPadding = NiaButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        )
    ) {
        NiaButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

/**
 * Now in Android outlined button with generic content slot. Wraps Material 3 [OutlinedButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param border Border to draw around the button. Pass `null` here for no border.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [NiaButtonDefaults.outlinedButtonColors].
 * @param contentPadding The spacing values to apply internally between the container and the
 * content. See [NiaButtonDefaults.buttonContentPadding].
 * @param content The button content.
 */
@Composable
fun NiaOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    border: BorderStroke? = NiaButtonDefaults.outlinedButtonBorder(enabled = enabled),
    colors: ButtonColors = NiaButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = NiaButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = NiaButtonDefaults.SmallButtonHeight)
        } else {
            modifier
        },
        enabled = enabled,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        }
    )
}

/**
 * Now in Android outlined button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param border Border to draw around the button. Pass `null` here for no border.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [NiaButtonDefaults.outlinedButtonColors].
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun NiaOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    border: BorderStroke? = NiaButtonDefaults.outlinedButtonBorder(enabled = enabled),
    colors: ButtonColors = NiaButtonDefaults.outlinedButtonColors(),
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    NiaOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        small = small,
        border = border,
        colors = colors,
        contentPadding = NiaButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        )
    ) {
        NiaButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

/**
 * Now in Android text button with generic content slot. Wraps Material 3 [TextButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [NiaButtonDefaults.textButtonColors].
 * @param contentPadding The spacing values to apply internally between the container and the
 * content. See [NiaButtonDefaults.buttonContentPadding].
 * @param content The button content.
 */
@Composable
fun NiaTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = NiaButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = NiaButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = if (small) {
            modifier.heightIn(min = NiaButtonDefaults.SmallButtonHeight)
        } else {
            modifier
        },
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        }
    )
}

/**
 * Now in Android text button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [NiaButtonDefaults.textButtonColors].
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun NiaTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = NiaButtonDefaults.textButtonColors(),
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    NiaTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        small = small,
        colors = colors,
        contentPadding = NiaButtonDefaults.buttonContentPadding(
            small = small,
            leadingIcon = leadingIcon != null,
            trailingIcon = trailingIcon != null
        )
    ) {
        NiaButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
    }
}

/**
 * Internal Now in Android button content layout for arranging the text label, leading icon and
 * trailing icon.
 *
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
private fun RowScope.NiaButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = NiaButtonDefaults.ButtonIconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier
            .weight(1f, fill = false)
            .padding(
                start = if (leadingIcon != null) {
                    NiaButtonDefaults.ButtonContentSpacing
                } else {
                    0.dp
                },
                end = if (trailingIcon != null) {
                    NiaButtonDefaults.ButtonContentSpacing
                } else {
                    0.dp
                }
            )
    ) {
        text()
    }
    if (trailingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = NiaButtonDefaults.ButtonIconSize)) {
            trailingIcon()
        }
    }
}

/**
 * Now in Android button default values.
 */
object NiaButtonDefaults {
    val SmallButtonHeight = 32.dp
    const val DisabledButtonContainerAlpha = 0.12f
    const val DisabledButtonContentAlpha = 0.38f
    val ButtonHorizontalPadding = 24.dp
    val ButtonHorizontalIconPadding = 16.dp
    val ButtonVerticalPadding = 8.dp
    val SmallButtonHorizontalPadding = 16.dp
    val SmallButtonHorizontalIconPadding = 12.dp
    val SmallButtonVerticalPadding = 7.dp
    val ButtonContentSpacing = 8.dp
    val ButtonIconSize = 18.dp
    fun buttonContentPadding(
        small: Boolean,
        leadingIcon: Boolean = false,
        trailingIcon: Boolean = false
    ): PaddingValues {
        return PaddingValues(
            start = when {
                small && leadingIcon -> SmallButtonHorizontalIconPadding
                small -> SmallButtonHorizontalPadding
                leadingIcon -> ButtonHorizontalIconPadding
                else -> ButtonHorizontalPadding
            },
            top = if (small) SmallButtonVerticalPadding else ButtonVerticalPadding,
            end = when {
                small && trailingIcon -> SmallButtonHorizontalIconPadding
                small -> SmallButtonHorizontalPadding
                trailingIcon -> ButtonHorizontalIconPadding
                else -> ButtonHorizontalPadding
            },
            bottom = if (small) SmallButtonVerticalPadding else ButtonVerticalPadding
        )
    }
    @Composable
    fun filledButtonColors(
        containerColor: Color = MaterialTheme.colorScheme.onBackground,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContainerAlpha
        ),
        disabledContentColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContentAlpha
        )
    ) = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
    @Composable
    fun outlinedButtonBorder(
        enabled: Boolean,
        width: Dp = 1.dp,
        color: Color = MaterialTheme.colorScheme.onBackground,
        disabledColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContainerAlpha
        )
    ): BorderStroke = BorderStroke(
        width = width,
        color = if (enabled) color else disabledColor
    )
    @Composable
    fun outlinedButtonColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContentAlpha
        )
    ) = ButtonDefaults.outlinedButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
    @Composable
    fun textButtonColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = MaterialTheme.colorScheme.onBackground.copy(
            alpha = DisabledButtonContentAlpha
        )
    ) = ButtonDefaults.textButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
}
