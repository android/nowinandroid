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

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons

/**
 * Now in Android dropdown menu button with included trailing icon as well as text label and item
 * content slots.
 *
 * @param items The list of items to display in the menu.
 * @param onItemClick Called when the user clicks on a menu item.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param dismissOnItemClick Whether the menu should be dismissed when an item is clicked.
 * @param itemText The text label content for a given item.
 * @param itemLeadingIcon The leading icon content for a given item.
 * @param itemTrailingIcon The trailing icon content for a given item.
 */
@Composable
fun <T> NiaDropdownMenuButton(
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    dismissOnItemClick: Boolean = true,
    text: @Composable () -> Unit,
    itemText: @Composable (item: T) -> Unit,
    itemLeadingIcon: @Composable ((item: T) -> Unit)? = null,
    itemTrailingIcon: @Composable ((item: T) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        NiaOutlinedButton(
            onClick = { expanded = true },
            enabled = enabled,
            text = text,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) NiaIcons.ArrowDropUp else NiaIcons.ArrowDropDown,
                    contentDescription = null
                )
            }
        )
        NiaDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = items,
            onItemClick = onItemClick,
            dismissOnItemClick = dismissOnItemClick,
            itemText = itemText,
            itemLeadingIcon = itemLeadingIcon,
            itemTrailingIcon = itemTrailingIcon
        )
    }
}

/**
 * Now in Android dropdown menu with item content slots. Wraps Material 3 [DropdownMenu] and
 * [DropdownMenuItem].
 *
 * @param expanded Whether the menu is currently open and visible to the user.
 * @param onDismissRequest Called when the user requests to dismiss the menu, such as by
 * tapping outside the menu's bounds.
 * @param items The list of items to display in the menu.
 * @param onItemClick Called when the user clicks on a menu item.
 * @param dismissOnItemClick Whether the menu should be dismissed when an item is clicked.
 * @param itemText The text label content for a given item.
 * @param itemLeadingIcon The leading icon content for a given item.
 * @param itemTrailingIcon The trailing icon content for a given item.
 */
@Composable
fun <T> NiaDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    dismissOnItemClick: Boolean = true,
    itemText: @Composable (item: T) -> Unit,
    itemLeadingIcon: @Composable ((item: T) -> Unit)? = null,
    itemTrailingIcon: @Composable ((item: T) -> Unit)? = null
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { itemText(item) },
                onClick = {
                    onItemClick(item)
                    if (dismissOnItemClick) onDismissRequest()
                },
                leadingIcon = if (itemLeadingIcon != null) {
                    { itemLeadingIcon(item) }
                } else {
                    null
                },
                trailingIcon = if (itemTrailingIcon != null) {
                    { itemTrailingIcon(item) }
                } else {
                    null
                }
            )
        }
    }
}
