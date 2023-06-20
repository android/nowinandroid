/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.WindowMetricsCalculator

class AdaptiveScaffoldNavigationComponentColors internal constructor(
    val railContainerColor: Color,
    val drawerContainerColor: Color,
    val bottomBarContainerColor: Color,
    val contentContainerColor: Color,
    val contentColor: Color,
    val selectedIconColor: Color,
    val selectedTextColor: Color,
    val unselectedIconColor: Color,
    val unselectedTextColor: Color,
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
)

object AdaptiveScaffoldNavigationComponentDefaults {
    @Composable
    fun colors(
        railContainerColor: Color = NavigationRailDefaults.ContainerColor,
        drawerContainerColor: Color = MaterialTheme.colorScheme.surface,
        bottomBarContainerColor: Color = NavigationBarDefaults.containerColor,
        contentContainerColor: Color = MaterialTheme.colorScheme.background,
        contentColor: Color = contentColorFor(contentContainerColor),
        selectedIconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        unselectedIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        unselectedContainerColor: Color = MaterialTheme.colorScheme.surface
    ) = AdaptiveScaffoldNavigationComponentColors(
        railContainerColor = railContainerColor,
        drawerContainerColor = drawerContainerColor,
        bottomBarContainerColor = bottomBarContainerColor,
        contentContainerColor = contentContainerColor,
        contentColor = contentColor,
        selectedIconColor = selectedIconColor,
        selectedTextColor = selectedTextColor,
        unselectedIconColor = unselectedIconColor,
        unselectedTextColor = unselectedTextColor,
        selectedContainerColor = selectedContainerColor,
        unselectedContainerColor = unselectedContainerColor,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AdaptiveScaffold(
    modifier: Modifier = Modifier,
    navigationItems: List<T>,
    navigationItemTitle: @Composable (item: T, isSelected: Boolean) -> Unit,
    navigationItemIcon: @Composable (item: T, isSelected: Boolean) -> Unit,
    isItemSelected: @Composable (item: T) -> Boolean,
    onNavigationItemClick: (item: T) -> Unit,
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    colors: AdaptiveScaffoldNavigationComponentColors = AdaptiveScaffoldNavigationComponentDefaults.colors(),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val context = LocalContext.current
    val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(context)
    val widthDp = metrics.bounds.width() / context.resources.displayMetrics.density
    val movableContent = remember(content) {
        movableContentOf(content)
    }

    when {
        widthDp >= 1240f -> {
            Scaffold(
                modifier = modifier,
                topBar = topBar,
                snackbarHost = snackbarHost,
                containerColor = colors.contentContainerColor,
                contentColor = colors.contentColor,
                contentWindowInsets = contentWindowInsets,
            ) { padding ->
                PermanentNavigationDrawer(
                    drawerContent = {
                        PermanentDrawerSheet(
                            drawerContainerColor = colors.drawerContainerColor,
                        ) {
                            navigationItems.forEach { item ->
                                NavigationDrawerItem(
                                    label = { navigationItemTitle(item, isItemSelected(item)) },
                                    icon = { navigationItemIcon(item, isItemSelected(item)) },
                                    selected = isItemSelected(item),
                                    onClick = { onNavigationItemClick(item) },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = colors.selectedContainerColor,
                                        unselectedContainerColor = colors.unselectedContainerColor,
                                        selectedIconColor = colors.selectedIconColor,
                                        unselectedIconColor = colors.unselectedIconColor,
                                        selectedTextColor = colors.selectedTextColor,
                                        unselectedTextColor = colors.unselectedTextColor,
                                    ),
                                )
                            }

                        }
                    },
                    modifier = Modifier.padding(padding),
                ) {
                    movableContent(padding)
                }
            }
        }

        widthDp >= 600f -> {
            Scaffold(
                modifier = modifier,
                topBar = topBar,
                snackbarHost = snackbarHost,
                containerColor = colors.contentContainerColor,
                contentColor = colors.contentColor,
                contentWindowInsets = contentWindowInsets,
            ) { padding ->
                Row {
                    NavigationRail(
                        containerColor = colors.railContainerColor,
                    ) {
                        navigationItems.forEach { item ->
                            NavigationRailItem(
                                label = { navigationItemTitle(item, isItemSelected(item)) },
                                icon = { navigationItemIcon(item, isItemSelected(item)) },
                                selected = isItemSelected(item),
                                onClick = { onNavigationItemClick(item) },
                                colors = NavigationRailItemDefaults.colors(
                                    indicatorColor = colors.selectedContainerColor,
                                    selectedIconColor = colors.selectedIconColor,
                                    unselectedIconColor = colors.unselectedIconColor,
                                    selectedTextColor = colors.selectedTextColor,
                                    unselectedTextColor = colors.unselectedTextColor,
                                ),
                            )
                        }
                    }
                    movableContent(padding)
                }
            }
        }

        else -> {
            Scaffold(
                modifier = modifier,
                snackbarHost = snackbarHost,
                bottomBar = {
                    NavigationBar(
                        containerColor = colors.bottomBarContainerColor,
                    ) {
                        navigationItems.forEach { item ->
                            NavigationBarItem(
                                label = { navigationItemTitle(item, isItemSelected(item)) },
                                icon = { navigationItemIcon(item, isItemSelected(item)) },
                                selected = isItemSelected(item),
                                onClick = { onNavigationItemClick(item) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = colors.selectedContainerColor,
                                    selectedIconColor = colors.selectedIconColor,
                                    unselectedIconColor = colors.unselectedIconColor,
                                    selectedTextColor = colors.selectedTextColor,
                                    unselectedTextColor = colors.unselectedTextColor,
                                ),
                            )
                        }
                    }
                },
                containerColor = colors.contentContainerColor,
                contentColor = colors.contentColor,
                contentWindowInsets = contentWindowInsets,
            ) { padding ->
                movableContent(padding)
            }
        }
    }
}
