/*
 * Copyright 2026 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.feature.interests

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultTestDevices
import com.google.samples.apps.nowinandroid.core.testing.util.captureForDevice
import com.google.samples.apps.nowinandroid.core.testing.util.captureMultiDevice
import com.google.samples.apps.nowinandroid.core.ui.FollowableTopicPreviewParameterProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

/**
 * Screenshot tests for the [InterestsScreen].
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class InterestsScreenScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val topics = FollowableTopicPreviewParameterProvider().values.first()

    @Test
    fun interestsScreenPopulated() {
        composeTestRule.captureMultiDevice("InterestsScreenPopulated") {
            NiaTheme {
                InterestsScreen(
                    uiState = InterestsUiState.Interests(
                        selectedTopicId = null,
                        topics = topics,
                    ),
                    followTopic = { _, _ -> },
                    onTopicClick = {},
                )
            }
        }
    }

    @Test
    fun interestsScreenLoading() {
        composeTestRule.captureMultiDevice("InterestsScreenLoading") {
            NiaTheme {
                InterestsScreen(
                    uiState = InterestsUiState.Loading,
                    followTopic = { _, _ -> },
                    onTopicClick = {},
                )
            }
        }
    }

    @Test
    fun interestsScreenEmpty() {
        composeTestRule.captureMultiDevice("InterestsScreenEmpty") {
            NiaTheme {
                InterestsScreen(
                    uiState = InterestsUiState.Empty,
                    followTopic = { _, _ -> },
                    onTopicClick = {},
                )
            }
        }
    }

    @Test
    fun interestsScreenPopulated_dark() {
        composeTestRule.captureForDevice(
            deviceName = "phone_dark",
            deviceSpec = DefaultTestDevices.PHONE.spec,
            screenshotName = "InterestsScreenPopulated",
            darkMode = true,
        ) {
            NiaTheme {
                InterestsScreen(
                    uiState = InterestsUiState.Interests(
                        selectedTopicId = null,
                        topics = topics,
                    ),
                    followTopic = { _, _ -> },
                    onTopicClick = {},
                )
            }
        }
    }
}
