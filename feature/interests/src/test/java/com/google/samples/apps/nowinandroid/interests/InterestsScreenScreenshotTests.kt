/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.interests

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.testing.data.followableTopicTestData
import com.google.samples.apps.nowinandroid.core.testing.util.captureMultiDevice
import com.google.samples.apps.nowinandroid.feature.interests.InterestsScreen
import com.google.samples.apps.nowinandroid.feature.interests.InterestsUiState
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

/**
 * Screenshot tests for the [InterestScreen].
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [33])
@LooperMode(LooperMode.Mode.PAUSED)
class InterestsScreenScreenshotTests {

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun interestsScreen_empty() {
        composeTestRule.captureMultiDevice("interestsScreen_empty") {
            InterestsScreen(InterestsUiState.Empty)
        }
    }

    @Test
    fun interestsScreen_loading() {
        composeTestRule.captureMultiDevice("interestsScreen_loading") {
            InterestsScreen(InterestsUiState.Loading)
        }
    }

    @Test
    fun interestsWithTopics_topicsFollowed() {
        composeTestRule.captureMultiDevice("interestsWithTopics_topicsFollowed") {
            InterestsScreen(InterestsUiState.Interests(topics = followableTopicTestData))
        }
    }

    @Composable
    private fun InterestsScreen(uiState: InterestsUiState) {
        NiaTheme {
            NiaBackground {
                InterestsScreen(
                    uiState = uiState,
                    followTopic = { _, _ -> },
                    onTopicClick = {},
                )
            }
        }
    }
}
