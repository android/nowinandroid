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
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaGradientBackground
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
class BackgroundScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun niaBackground_multipleThemes() {
        composeTestRule.captureMultiTheme("Background") {
            NiaBackground(Modifier.size(100.dp), content = {})
        }
    }

    @Test
    fun niaGradientBackground_multipleThemes() {
        composeTestRule.captureMultiTheme("Background", "GradientBackground") {
            NiaGradientBackground(Modifier.size(100.dp), content = {})
        }
    }
//
//    @Test
//    fun backgroundDefault() {
//        testScreenshotDarkAndLight("BackgroundDefault") {
//            NiaTheme(disableDynamicTheming = true) {
//                NiaBackground(Modifier.size(100.dp), content = {})
//            }
//        }
//    }
//
//    @Test
//    fun backgroundDynamic() {
//        testScreenshotDarkAndLight("BackgroundDynamic") {
//            NiaTheme(disableDynamicTheming = false) {
//                NiaBackground(Modifier.size(100.dp), content = {})
//            }
//        }
//    }
//
//    @Test
//    fun backgroundAndroid() {
//        testScreenshotDarkAndLight("BackgroundAndroid") {
//            NiaTheme(androidTheme = true) {
//                NiaBackground(Modifier.size(100.dp), content = {})
//            }
//        }
//    }
//
//    @Test
//    fun gradientBackgroundDefault() {
//        testScreenshotDarkAndLight("BackgroundGradientDefault") {
//            NiaTheme(disableDynamicTheming = true) {
//                NiaGradientBackground(Modifier.size(100.dp), content = {})
//            }
//        }
//    }
//
//    @Test
//    fun gradientBackgroundDynamic() {
//        testScreenshotDarkAndLight("BackgroundGradientDynamic") {
//            NiaTheme(disableDynamicTheming = false) {
//                NiaGradientBackground(Modifier.size(100.dp), content = {})
//            }
//        }
//    }
//
//    @Test
//    fun gradientBackgroundAndroid() {
//        testScreenshotDarkAndLight("BackgroundGradientAndroid") {
//            NiaTheme(androidTheme = true) {
//                NiaGradientBackground(Modifier.size(100.dp), content = {})
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
//                TestHarness(darkMode = darkMode.value) {
//                    content()
//                }
//            }
//        }
//
//        composeTestRule.onRoot()
//            .captureRoboImage(
//                "src/test/screenshots/Background/${screenshotName}_dark.png",
//                roborazziOptions = DefaultRoborazziOptions,
//            )
//        darkMode.value = false
//        composeTestRule.onRoot()
//            .captureRoboImage(
//                "src/test/screenshots/Background/${screenshotName}_light.png",
//                roborazziOptions = DefaultRoborazziOptions,
//            )
//    }
}
