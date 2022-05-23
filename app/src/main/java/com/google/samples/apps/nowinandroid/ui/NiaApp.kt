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

package com.google.samples.apps.nowinandroid.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.samples.apps.nowinandroid.core.ui.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.ui.theme.NiaTheme
import com.google.samples.apps.nowinandroid.navigation.NiaNavHost
import com.google.samples.apps.nowinandroid.navigation.NiaTopLevelNavigation
import com.google.samples.apps.nowinandroid.navigation.TOP_LEVEL_DESTINATIONS
import com.google.samples.apps.nowinandroid.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NiaApp(windowSizeClass: WindowSizeClass) {
    NiaTheme {
        val navController = rememberNavController()
        val niaTopLevelNavigation = remember(navController) {
            NiaTopLevelNavigation(navController)
        }

        NiaBackground {
            Scaffold(
                modifier = Modifier,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                bottomBar = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        NiABottomBar(
                            onNavigateToTopLevelDestination = niaTopLevelNavigation::navigateTo
                        )
                    }
                }
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal
                            )
                        )
                ) {
                    if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
                        NiANavRail(
                            onNavigateToTopLevelDestination = niaTopLevelNavigation::navigateTo,
                            modifier = Modifier.safeDrawingPadding()
                        )
                    }

                    NiaNavHost(
                        windowSizeClass = windowSizeClass,
                        navController = navController,
                        modifier = Modifier
                            .padding(padding)
                            .consumedWindowInsets(padding)
                    )
                }
            }
        }
    }
}

@Composable
private fun NiANavRail(
    onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        var indexOfSelectedItem by rememberSaveable {
            mutableStateOf(0)
        }
        TOP_LEVEL_DESTINATIONS.forEachIndexed { itemIndex, destination ->
            val isSelect = indexOfSelectedItem == itemIndex
            NavigationRailItem(
                selected = isSelect,
                onClick = {
                    indexOfSelectedItem = itemIndex
                    onNavigateToTopLevelDestination(destination)
                },
                icon = {
                    Icon(
                        if (isSelect) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) }
            )
        }
    }
}

@Composable
private fun NiABottomBar(
    onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit
) {
    // Wrap the navigation bar in a surface so the color behind the system
    // navigation is equal to the container color of the navigation bar.
    Surface(color = MaterialTheme.colorScheme.surface) {
        NavigationBar(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            ),
            tonalElevation = 0.dp
        ) {
            var indexOfSelectedItem by rememberSaveable {
                mutableStateOf(0)
            }
            TOP_LEVEL_DESTINATIONS.forEachIndexed { itemIndex, destination ->
                val isSelect = indexOfSelectedItem == itemIndex
                NavigationBarItem(
                    selected = isSelect,
                    onClick = {
                        indexOfSelectedItem = itemIndex
                        onNavigateToTopLevelDestination(destination)
                    },
                    icon = {
                        Icon(
                            if (isSelect) {
                                destination.selectedIcon
                            } else {
                                destination.unselectedIcon
                            },
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(destination.iconTextId)) }
                )
            }
        }
    }
}
