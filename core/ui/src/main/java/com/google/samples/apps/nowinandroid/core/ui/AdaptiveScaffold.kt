/*
 * Copyright 2021 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator

class AdaptiveScaffoldNavigationComponentColors internal constructor(
    val detailsPaneContainerColor: Color,
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
        detailsPaneContainerColor: Color = NavigationBarDefaults.containerColor,
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
        unselectedContainerColor: Color = MaterialTheme.colorScheme.surface,
    ) = AdaptiveScaffoldNavigationComponentColors(
        detailsPaneContainerColor = detailsPaneContainerColor,
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
    isDetailsPaneVisible: Boolean = false,
    detailsPane: @Composable () -> Unit = {},
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val context = LocalContext.current
    val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(context)
    val widthDp = metrics.bounds.width() / context.resources.displayMetrics.density
    val movableContent = remember(content) {
        movableContentOf(content)
    }

    when {
        widthDp >= 1240f -> DrawerScaffold(
            modifier = modifier,
            navigationItems = navigationItems,
            navigationItemTitle = navigationItemTitle,
            navigationItemIcon = navigationItemIcon,
            isItemSelected = isItemSelected,
            onNavigationItemClick = onNavigationItemClick,
            topBar = topBar,
            snackbarHost = snackbarHost,
            colors = colors,
            contentWindowInsets = contentWindowInsets,
            isDetailsPaneVisible = isDetailsPaneVisible,
            detailsPane = detailsPane,
            content = movableContent,
        )

        widthDp >= 600f -> RailScaffold(
            modifier = modifier,
            navigationItems = navigationItems,
            navigationItemTitle = navigationItemTitle,
            navigationItemIcon = navigationItemIcon,
            isItemSelected = isItemSelected,
            onNavigationItemClick = onNavigationItemClick,
            topBar = topBar,
            snackbarHost = snackbarHost,
            colors = colors,
            contentWindowInsets = contentWindowInsets,
            isDetailsPaneVisible = isDetailsPaneVisible,
            detailsPane = detailsPane,
            content = movableContent,
        )

        else -> BottomBarScaffold(
            modifier = modifier,
            navigationItems = navigationItems,
            navigationItemTitle = navigationItemTitle,
            navigationItemIcon = navigationItemIcon,
            isItemSelected = isItemSelected,
            onNavigationItemClick = onNavigationItemClick,
            topBar = topBar,
            snackbarHost = snackbarHost,
            colors = colors,
            contentWindowInsets = contentWindowInsets,
            isDetailsPaneVisible = isDetailsPaneVisible,
            detailsPane = detailsPane,
            content = movableContent,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> BottomBarScaffold(
    modifier: Modifier,
    navigationItems: List<T>,
    navigationItemTitle: @Composable (item: T, isSelected: Boolean) -> Unit,
    navigationItemIcon: @Composable (item: T, isSelected: Boolean) -> Unit,
    isItemSelected: @Composable (item: T) -> Boolean,
    onNavigationItemClick: (item: T) -> Unit,
    topBar: @Composable () -> Unit,
    snackbarHost: @Composable () -> Unit,
    colors: AdaptiveScaffoldNavigationComponentColors,
    contentWindowInsets: WindowInsets,
    isDetailsPaneVisible: Boolean,
    detailsPane: @Composable () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    Box(modifier = modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = topBar,
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
            content(padding)
        }
        AnimatedVisibility(
            visible = isDetailsPaneVisible,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it }),
            modifier = Modifier.fillMaxSize(),
        ) {
            Surface(color = colors.detailsPaneContainerColor) {
                detailsPane()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> RailScaffold(
    modifier: Modifier,
    navigationItems: List<T>,
    navigationItemTitle: @Composable (item: T, isSelected: Boolean) -> Unit,
    navigationItemIcon: @Composable (item: T, isSelected: Boolean) -> Unit,
    isItemSelected: @Composable (item: T) -> Boolean,
    onNavigationItemClick: (item: T) -> Unit,
    topBar: @Composable () -> Unit,
    snackbarHost: @Composable () -> Unit,
    colors: AdaptiveScaffoldNavigationComponentColors,
    contentWindowInsets: WindowInsets,
    isDetailsPaneVisible: Boolean,
    detailsPane: @Composable () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val weight: Float by animateFloatAsState(
        targetValue = if (isDetailsPaneVisible) 0.5f else 1f,
        animationSpec = tween(durationMillis = 500),
        label = "Details Pane",
    )

    Row(modifier = modifier) {
        NavigationRail(
            modifier = Modifier.safeDrawingPadding(),
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
        Scaffold(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(weight),
            topBar = topBar,
            snackbarHost = snackbarHost,
            containerColor = colors.contentContainerColor,
            contentColor = colors.contentColor,
        ) { padding ->
            content(padding)
        }
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            color = colors.detailsPaneContainerColor,
        ) {
            detailsPane()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DrawerScaffold(
    modifier: Modifier,
    navigationItems: List<T>,
    navigationItemTitle: @Composable (item: T, isSelected: Boolean) -> Unit,
    navigationItemIcon: @Composable (item: T, isSelected: Boolean) -> Unit,
    isItemSelected: @Composable (item: T) -> Boolean,
    onNavigationItemClick: (item: T) -> Unit,
    topBar: @Composable () -> Unit,
    snackbarHost: @Composable () -> Unit,
    colors: AdaptiveScaffoldNavigationComponentColors,
    contentWindowInsets: WindowInsets,
    isDetailsPaneVisible: Boolean,
    detailsPane: @Composable () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit,
) {
    val weight: Float by animateFloatAsState(
        targetValue = if (isDetailsPaneVisible) 0.5f else 1f,
        animationSpec = tween(durationMillis = 500),
        label = "Details Pane",
    )

    Row(modifier = modifier) {
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
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            },
            modifier = Modifier.safeDrawingPadding(),
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(weight),
                topBar = topBar,
                snackbarHost = snackbarHost,
                containerColor = colors.contentContainerColor,
                contentColor = colors.contentColor,
                contentWindowInsets = contentWindowInsets,
            ) { padding ->
                content(padding)
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            color = colors.detailsPaneContainerColor,
        ) {
            detailsPane()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AdaptiveScaffold(
    navigator: AdaptiveScaffoldNavigator,
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
    val isDetailsPaneVisible by navigator.isVisible.collectAsState(initial = false)
    AdaptiveScaffold(
        modifier = modifier,
        navigationItems = navigationItems,
        navigationItemTitle = navigationItemTitle,
        navigationItemIcon = navigationItemIcon,
        isItemSelected = isItemSelected,
        onNavigationItemClick = onNavigationItemClick,
        topBar = topBar,
        snackbarHost = snackbarHost,
        colors = colors,
        contentWindowInsets = contentWindowInsets,
        isDetailsPaneVisible = isDetailsPaneVisible,
        detailsPane = navigator.detailsPaneContent,
        content = content,
    )
}
