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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.TestNetworkMonitor
import com.google.samples.apps.nowinandroid.core.testing.util.TestTimeZoneMonitor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests [NiaAppState].
 *
 * Note: This could become an unit test if Robolectric is added to the project and the Context
 * is faked.
 */
class NiaAppStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Create the test dependencies.
    private val networkMonitor = TestNetworkMonitor()

    private val timeZoneMonitor = TestTimeZoneMonitor()

    private val userNewsResourceRepository =
        CompositeUserNewsResourceRepository(TestNewsRepository(), TestUserDataRepository())

    // Subject under test.
    private lateinit var state: NiaAppState

    @Test
    fun niaAppState_currentDestination() = runTest {
        var currentDestination: String? = null

        composeTestRule.setContent {
            val navController = rememberTestNavController()
            state = remember(navController) {
                NiaAppState(
                    navController = navController,
                    coroutineScope = backgroundScope,
                    networkMonitor = networkMonitor,
                    userNewsResourceRepository = userNewsResourceRepository,
                    timeZoneMonitor = timeZoneMonitor,
                )
            }

            // Update currentDestination whenever it changes
            currentDestination = state.currentDestination?.route

            // Navigate to destination b once
            LaunchedEffect(Unit) {
                navController.setCurrentDestination("b")
            }
        }

        assertEquals("b", currentDestination)
    }

    @Test
    fun niaAppState_destinations() = runTest {
        composeTestRule.setContent {
            state = rememberNiaAppState(
                networkMonitor = networkMonitor,
                userNewsResourceRepository = userNewsResourceRepository,
                timeZoneMonitor = timeZoneMonitor,
            )
        }

        assertEquals(3, state.topLevelDestinations.size)
        assertTrue(state.topLevelDestinations[0].name.contains("for_you", true))
        assertTrue(state.topLevelDestinations[1].name.contains("bookmarks", true))
        assertTrue(state.topLevelDestinations[2].name.contains("interests", true))
    }

    @Test
    fun niaAppState_whenNetworkMonitorIsOffline_StateIsOffline() = runTest(UnconfinedTestDispatcher()) {
        composeTestRule.setContent {
            state = NiaAppState(
                navController = NavHostController(LocalContext.current),
                coroutineScope = backgroundScope,
                networkMonitor = networkMonitor,
                userNewsResourceRepository = userNewsResourceRepository,
                timeZoneMonitor = timeZoneMonitor,
            )
        }

        backgroundScope.launch { state.isOffline.collect() }
        networkMonitor.setConnected(false)
        assertEquals(
            true,
            state.isOffline.value,
        )
    }

    @Test
    fun niaAppState_differentTZ_withTimeZoneMonitorChange() = runTest(UnconfinedTestDispatcher()) {
        composeTestRule.setContent {
            state = NiaAppState(
                navController = NavHostController(LocalContext.current),
                coroutineScope = backgroundScope,
                networkMonitor = networkMonitor,
                userNewsResourceRepository = userNewsResourceRepository,
                timeZoneMonitor = timeZoneMonitor,
            )
        }
        val changedTz = TimeZone.of("Europe/Prague")
        backgroundScope.launch { state.currentTimeZone.collect() }
        timeZoneMonitor.setTimeZone(changedTz)
        assertEquals(
            changedTz,
            state.currentTimeZone.value,
        )
    }
}

@Composable
private fun rememberTestNavController(): TestNavHostController {
    val context = LocalContext.current
    return remember {
        TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            graph = createGraph(startDestination = "a") {
                composable("a") { }
                composable("b") { }
                composable("c") { }
            }
        }
    }
}
