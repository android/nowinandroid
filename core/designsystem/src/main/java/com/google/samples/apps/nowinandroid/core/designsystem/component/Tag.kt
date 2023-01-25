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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.samples.apps.nowinandroid.core.designsystem.R

@Composable
fun NiaTopicTag(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    followed: Boolean,
    onDropdownMenuToggle: (show: Boolean) -> Unit = {},
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onBrowseClick: () -> Unit,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    followText: @Composable () -> Unit = { Text(stringResource(R.string.follow)) },
    unFollowText: @Composable () -> Unit = { Text(stringResource(R.string.unfollow)) },
    browseText: @Composable () -> Unit = { Text(stringResource(R.string.browse_topic)) },
) {
    Box(modifier = modifier) {
        val containerColor = if (followed) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = NiaTagDefaults.UnfollowedTopicTagContainerAlpha,
            )
        }
        TextButton(
            onClick = { onDropdownMenuToggle(true) },
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                containerColor = containerColor,
                contentColor = contentColorFor(backgroundColor = containerColor),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = NiaTagDefaults.DisabledTopicTagContainerAlpha,
                ),
            ),
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                text()
            }
        }
        NiaDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDropdownMenuToggle(false) },
            items = if (followed) listOf(UNFOLLOW, BROWSE) else listOf(FOLLOW, BROWSE),
            onItemClick = { item ->
                when (item) {
                    FOLLOW -> onFollowClick()
                    UNFOLLOW -> onUnfollowClick()
                    BROWSE -> onBrowseClick()
                }
            },
            itemText = { item ->
                when (item) {
                    FOLLOW -> followText()
                    UNFOLLOW -> unFollowText()
                    BROWSE -> browseText()
                }
            },
        )
    }
}

/**
 * Now in Android tag default values.
 */
object NiaTagDefaults {
    const val UnfollowedTopicTagContainerAlpha = 0.5f

    // TODO: File bug
    // Button disabled container alpha value not exposed by ButtonDefaults
    const val DisabledTopicTagContainerAlpha = 0.12f
}

private const val FOLLOW = 1
private const val UNFOLLOW = 2
private const val BROWSE = 3
