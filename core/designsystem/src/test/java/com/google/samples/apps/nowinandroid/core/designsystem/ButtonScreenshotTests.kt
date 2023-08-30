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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.accompanist.testharness.TestHarness
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaOutlinedButton
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultRoborazziOptions
import com.google.samples.apps.nowinandroid.core.testing.util.captureMultiTheme
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [33], qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class ButtonScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun niaButton_multipleThemes() {
        composeTestRule.captureMultiTheme("Button") { description ->
            Surface {
                NiaButton(onClick = {}, text = { Text("$description Button") })
            }
        }
    }

    @Test
    fun niaOutlineButton_multipleThemes() {
        composeTestRule.captureMultiTheme("Button", "OutlineButton") { description ->
            Surface {
                NiaOutlinedButton(onClick = {}, text = { Text("$description OutlineButton") })
            }
        }
    }

    @Test
    fun niaButton_leadingIcon_multipleThemes() {
        composeTestRule.captureMultiTheme(
            name = "Button",
            overrideFileName = "ButtonLeadingIcon",
            shouldCompareAndroidTheme = false
        ) { description ->
            Surface {
                NiaButton(
                    onClick = {},
                    text = { Text("$description Icon Button") },
                    leadingIcon = { Icon(imageVector = NiaIcons.Add, contentDescription = null) },
                )
            }
        }
    }
//
//    @Test
//    fun buttonDefault() {
//        testScreenshotDarkAndLight("ButtonDefault") {
//            NiaTheme(disableDynamicTheming = true) {
//                NiaBackground {
//                    NiaButton(onClick = {}, text = { Text("Default Button") })
//                }
//            }
//        }
//    }
//
//    @Test
//    fun buttonDynamic() {
//        testScreenshotDarkAndLight("ButtonDynamic") {
//            NiaTheme(disableDynamicTheming = false) {
//                NiaBackground {
//                    NiaOutlinedButton(onClick = {}, text = { Text("Dynamic Button") })
//                }
//            }
//        }
//    }
//
//    @Test
//    fun outlinedButton() {
//        testScreenshotDarkAndLight("ButtonOutlined") {
//            NiaTheme(disableDynamicTheming = true) {
//                NiaBackground {
//                    NiaOutlinedButton(onClick = {}, text = { Text("Outlined Button") })
//                }
//            }
//        }
//    }
//
//    @Test
//    fun outlinedButtonDynamic() {
//        testScreenshotDarkAndLight("Buttonoutlineddynamic") {
//            NiaTheme(disableDynamicTheming = true) {
//                NiaBackground {
//                    NiaOutlinedButton(onClick = {}, text = { Text("Outlined Dynamic") })
//                }
//            }
//        }
//    }
//
//    @Test
//    fun buttonLeadingIcon() {
//        testScreenshotDarkAndLight("ButtonLeadingIcon") {
//            NiaTheme(disableDynamicTheming = true) {
//                NiaBackground {
//                    NiaButton(
//                        onClick = {},
//                        text = { Text("Test button") },
//                        leadingIcon = { Icon(imageVector = NiaIcons.Add, contentDescription = null) },
//                    )
//                }
//            }
//        }
//    }
//
//    private fun testScreenshotDarkAndLight(screenshotName: String, content: @Composable () -> Unit) {
//        val darkMode = mutableStateOf(true)
//        composeTestRule.setContent {
//            CompositionLocalProvider(
//                LocalInspectionMode provides true,
//            ) {
//                TestHarness(darkMode = darkMode.value, size = DpSize(170.dp, 50.dp)) {
//                    content()
//                }
//            }
//        }
//
//        composeTestRule.onRoot()
//            .captureRoboImage(
//                "src/test/screenshots/Button/${screenshotName}_dark.png",
//                roborazziOptions = DefaultRoborazziOptions,
//            )
//        darkMode.value = false
//        composeTestRule.onRoot()
//            .captureRoboImage(
//                "src/test/screenshots/Button/${screenshotName}_light.png",
//                roborazziOptions = DefaultRoborazziOptions,
//            )
//    }
}