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

package com.google.samples.apps.nowinandroid.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.accompanist.testharness.TestHarness
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaFilterChip
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultRoborazziOptions
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [33], qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class FilterChipScreenshotTests() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun filterChipPermutationsThemeSelectedDynamic() {

        val darkMode = mutableStateOf(false)
        val selected = mutableStateOf(false)
        val dynamicTheming = mutableStateOf(false)

        composeTestRule.setContent {
            TestHarness(darkMode = darkMode.value, size = DpSize(80.dp, 40.dp)) {
                NiaTheme(disableDynamicTheming = !dynamicTheming.value) {
                    NiaBackground {
                        NiaFilterChip(selected = selected.value, onSelectedChange = {}) {
                            Text("Chip")
                        }
                    }
                }
            }
        }

        listOf(true, false).forEach { darkModeValue ->
            darkMode.value = darkModeValue
            val darkModeDesc = if (darkModeValue) "dark" else "light"

            listOf(true, false).forEach { selectedValue ->
                selected.value = selectedValue
                val selectedDesc = if (selectedValue) "selected" else "notSelected"

                listOf(true, false).forEach { dynamicThemingValue ->
                    dynamicTheming.value = dynamicThemingValue
                    val dynamicThemingDesc = if (dynamicThemingValue) "dynamic" else "default"

                    composeTestRule.onRoot()
                        .captureRoboImage(
                            "src/test/screenshots/FilterChip/FilterChip_${darkModeDesc}_${selectedDesc}_${dynamicThemingDesc}.png",
                            roborazziOptions = DefaultRoborazziOptions,
                        )
                }
            }
        }
    }


    @Test
    fun NavigationHugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                TestHarness(fontScale = 2f, size = DpSize(80.dp, 40.dp)) {
                    NiaTheme {
                        NiaBackground {
                            NiaFilterChip(selected = true, onSelectedChange = {}) {
                                Text("Chip")
                            }
                        }
                    }
                }
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/FilterChip/FilterChip_fontScale2.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }
}
