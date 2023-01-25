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

package com.google.samples.apps.niacatalog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaDropdownMenuButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaFilterChip
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaIconToggleButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaNavigationBar
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaNavigationBarItem
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaOutlinedButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTab
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTabRow
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTextButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTopicTag
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaViewToggleButton
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme

/**
 * Now in Android component catalog.
 */
@Composable
fun NiaCatalog() {
    NiaTheme {
        Surface {
            val contentPadding = WindowInsets
                .systemBars
                .add(WindowInsets(left = 16.dp, top = 16.dp, right = 16.dp, bottom = 16.dp))
                .asPaddingValues()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Text(
                        text = "NiA Catalog",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                item { Text("Buttons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        NiaButton(onClick = {}) {
                            Text(text = "Enabled")
                        }
                        NiaOutlinedButton(onClick = {}) {
                            Text(text = "Enabled")
                        }
                        NiaTextButton(onClick = {}) {
                            Text(text = "Enabled")
                        }
                    }
                }
                item { Text("Disabled buttons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        NiaButton(
                            onClick = {},
                            enabled = false,
                        ) {
                            Text(text = "Disabled")
                        }
                        NiaOutlinedButton(
                            onClick = {},
                            enabled = false,
                        ) {
                            Text(text = "Disabled")
                        }
                        NiaTextButton(
                            onClick = {},
                            enabled = false,
                        ) {
                            Text(text = "Disabled")
                        }
                    }
                }
                item { Text("Buttons with leading icons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        NiaButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = NiaIcons.Add, contentDescription = null)
                            },
                        )
                        NiaOutlinedButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = NiaIcons.Add, contentDescription = null)
                            },
                        )
                        NiaTextButton(
                            onClick = {},
                            text = { Text(text = "Enabled") },
                            leadingIcon = {
                                Icon(imageVector = NiaIcons.Add, contentDescription = null)
                            },
                        )
                    }
                }
                item { Text("Disabled buttons with leading icons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        NiaButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = NiaIcons.Add, contentDescription = null)
                            },
                        )
                        NiaOutlinedButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = NiaIcons.Add, contentDescription = null)
                            },
                        )
                        NiaTextButton(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "Disabled") },
                            leadingIcon = {
                                Icon(imageVector = NiaIcons.Add, contentDescription = null)
                            },
                        )
                    }
                }
                item { Text("Dropdown menus", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        NiaDropdownMenuButton(
                            text = { Text("Enabled") },
                            items = listOf("Item 1", "Item 2", "Item 3"),
                            onItemClick = {},
                            itemText = { item -> Text(item) },
                        )
                        NiaDropdownMenuButton(
                            text = { Text("Disabled") },
                            items = listOf("Item 1", "Item 2", "Item 3"),
                            onItemClick = {},
                            itemText = { item -> Text(item) },
                            enabled = false,
                        )
                    }
                }
                item { Text("Chips", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        var firstChecked by remember { mutableStateOf(false) }
                        NiaFilterChip(
                            selected = firstChecked,
                            onSelectedChange = { checked -> firstChecked = checked },
                            label = { Text(text = "Enabled") },
                        )
                        var secondChecked by remember { mutableStateOf(true) }
                        NiaFilterChip(
                            selected = secondChecked,
                            onSelectedChange = { checked -> secondChecked = checked },
                            label = { Text(text = "Enabled") },
                        )
                        NiaFilterChip(
                            selected = false,
                            onSelectedChange = {},
                            enabled = false,
                            label = { Text(text = "Disabled") },
                        )
                        NiaFilterChip(
                            selected = true,
                            onSelectedChange = {},
                            enabled = false,
                            label = { Text(text = "Disabled") },
                        )
                    }
                }
                item { Text("Icon buttons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        var firstChecked by remember { mutableStateOf(false) }
                        NiaIconToggleButton(
                            checked = firstChecked,
                            onCheckedChange = { checked -> firstChecked = checked },
                            icon = {
                                Icon(
                                    painter = painterResource(id = NiaIcons.BookmarkBorder),
                                    contentDescription = null,
                                )
                            },
                            checkedIcon = {
                                Icon(
                                    painter = painterResource(id = NiaIcons.Bookmark),
                                    contentDescription = null,
                                )
                            },
                        )
                        var secondChecked by remember { mutableStateOf(true) }
                        NiaIconToggleButton(
                            checked = secondChecked,
                            onCheckedChange = { checked -> secondChecked = checked },
                            icon = {
                                Icon(
                                    painter = painterResource(id = NiaIcons.BookmarkBorder),
                                    contentDescription = null,
                                )
                            },
                            checkedIcon = {
                                Icon(
                                    painter = painterResource(id = NiaIcons.Bookmark),
                                    contentDescription = null,
                                )
                            },
                        )
                        NiaIconToggleButton(
                            checked = false,
                            onCheckedChange = {},
                            icon = {
                                Icon(
                                    painter = painterResource(id = NiaIcons.BookmarkBorder),
                                    contentDescription = null,
                                )
                            },
                            checkedIcon = {
                                Icon(
                                    painter = painterResource(id = NiaIcons.Bookmark),
                                    contentDescription = null,
                                )
                            },
                            enabled = false,
                        )
                        NiaIconToggleButton(
                            checked = true,
                            onCheckedChange = {},
                            icon = {
                                Icon(
                                    painter = painterResource(id = NiaIcons.BookmarkBorder),
                                    contentDescription = null,
                                )
                            },
                            checkedIcon = {
                                Icon(
                                    painter = painterResource(id = NiaIcons.Bookmark),
                                    contentDescription = null,
                                )
                            },
                            enabled = false,
                        )
                    }
                }
                item { Text("View toggle", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        var firstExpanded by remember { mutableStateOf(false) }
                        NiaViewToggleButton(
                            expanded = firstExpanded,
                            onExpandedChange = { expanded -> firstExpanded = expanded },
                            compactText = { Text(text = "Compact view") },
                            expandedText = { Text(text = "Expanded view") },
                        )
                        var secondExpanded by remember { mutableStateOf(true) }
                        NiaViewToggleButton(
                            expanded = secondExpanded,
                            onExpandedChange = { expanded -> secondExpanded = expanded },
                            compactText = { Text(text = "Compact view") },
                            expandedText = { Text(text = "Expanded view") },
                        )
                        NiaViewToggleButton(
                            expanded = false,
                            onExpandedChange = {},
                            compactText = { Text(text = "Disabled") },
                            expandedText = { Text(text = "Disabled") },
                            enabled = false,
                        )
                    }
                }
                item { Text("Tags", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(mainAxisSpacing = 16.dp) {
                        var expandedTopicId by remember { mutableStateOf<String?>(null) }

                        var firstFollowed by remember { mutableStateOf(false) }
                        NiaTopicTag(
                            expanded = expandedTopicId == "Topic 1",
                            followed = firstFollowed,
                            onDropdownMenuToggle = { show ->
                                expandedTopicId = if (show) "Topic 1" else null
                            },
                            onFollowClick = { firstFollowed = true },
                            onUnfollowClick = { firstFollowed = false },
                            onBrowseClick = {},
                            text = { Text(text = "Topic 1".uppercase()) },
                            followText = { Text(text = "Follow") },
                            unFollowText = { Text(text = "Unfollow") },
                            browseText = { Text(text = "Browse topic") },
                        )
                        var secondFollowed by remember { mutableStateOf(true) }
                        NiaTopicTag(
                            expanded = expandedTopicId == "Topic 2",
                            followed = secondFollowed,
                            onDropdownMenuToggle = { show ->
                                expandedTopicId = if (show) "Topic 2" else null
                            },
                            onFollowClick = { secondFollowed = true },
                            onUnfollowClick = { secondFollowed = false },
                            onBrowseClick = {},
                            text = { Text(text = "Topic 2".uppercase()) },
                            followText = { Text(text = "Follow") },
                            unFollowText = { Text(text = "Unfollow") },
                            browseText = { Text(text = "Browse topic") },
                        )
                        NiaTopicTag(
                            expanded = false,
                            followed = false,
                            onDropdownMenuToggle = {},
                            onFollowClick = {},
                            onUnfollowClick = {},
                            onBrowseClick = {},
                            text = { Text(text = "Disabled".uppercase()) },
                            enabled = false,
                        )
                    }
                }
                item { Text("Tabs", Modifier.padding(top = 16.dp)) }
                item {
                    var selectedTabIndex by remember { mutableStateOf(0) }
                    val titles = listOf("Topics", "People")
                    NiaTabRow(selectedTabIndex = selectedTabIndex) {
                        titles.forEachIndexed { index, title ->
                            NiaTab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(text = title) },
                            )
                        }
                    }
                }
                item { Text("Navigation", Modifier.padding(top = 16.dp)) }
                item {
                    var selectedItem by remember { mutableStateOf(0) }
                    val items = listOf("For you", "Episodes", "Saved", "Interests")
                    val icons = listOf(
                        NiaIcons.UpcomingBorder,
                        NiaIcons.MenuBookBorder,
                        NiaIcons.BookmarksBorder,
                    )
                    val selectedIcons = listOf(
                        NiaIcons.Upcoming,
                        NiaIcons.MenuBook,
                        NiaIcons.Bookmarks,
                    )
                    val tagIcon = NiaIcons.Tag
                    NiaNavigationBar {
                        items.forEachIndexed { index, item ->
                            NiaNavigationBarItem(
                                icon = {
                                    if (index == 3) {
                                        Icon(imageVector = tagIcon, contentDescription = null)
                                    } else {
                                        Icon(
                                            painter = painterResource(id = icons[index]),
                                            contentDescription = item,
                                        )
                                    }
                                },
                                selectedIcon = {
                                    if (index == 3) {
                                        Icon(imageVector = tagIcon, contentDescription = null)
                                    } else {
                                        Icon(
                                            painter = painterResource(id = selectedIcons[index]),
                                            contentDescription = item,
                                        )
                                    }
                                },
                                label = { Text(item) },
                                selected = selectedItem == index,
                                onClick = { selectedItem = index },
                            )
                        }
                    }
                }
            }
        }
    }
}
