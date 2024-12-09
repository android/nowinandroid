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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme

/**
 * Now in Android tab. Wraps Material 3 [Tab] and shifts text label down.
 *
 * @param selected Whether this tab is selected or not.
 * @param onClick The callback to be invoked when this tab is selected.
 * @param modifier Modifier to be applied to the tab.
 * @param enabled Controls the enabled state of the tab. When `false`, this tab will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The text label content.
 */
@Composable
fun NiaTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        text = {
            val style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(
                value = style,
                content = {
                    Box(modifier = Modifier.padding(top = NiaTabDefaults.TabTopPadding)) {
                        text()
                    }
                },
            )
        },
    )
}

/**
 * Now in Android tab row. Wraps Material 3 [TabRow].
 *
 * @param selectedTabIndex The index of the currently selected tab.
 * @param modifier Modifier to be applied to the tab row.
 * @param tabs The tabs inside this tab row. Typically this will be multiple [NiaTab]s. Each element
 * inside this lambda will be measured and placed evenly across the row, each taking up equal space.
 */
@Composable
fun NiaTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        tabs = tabs,
    )
}

@ThemePreviews
@Composable
fun TabsPreview() {
    NiaTheme {
        val titles = listOf("Topics", "People")
        NiaTabRow(selectedTabIndex = 0) {
            titles.forEachIndexed { index, title ->
                NiaTab(
                    selected = index == 0,
                    onClick = { },
                    text = { Text(text = title) },
                )
            }
        }
    }
}

object NiaTabDefaults {
    val TabTopPadding = 7.dp
}
