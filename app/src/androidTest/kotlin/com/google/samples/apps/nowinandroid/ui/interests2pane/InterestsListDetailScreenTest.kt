/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.ui.interests2pane

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.espresso.Espresso
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.ui.FakeWindowMetricsCalculatorRule
import com.google.samples.apps.nowinandroid.ui.stringResource
import com.google.samples.apps.nowinandroid.uitesthiltmanifest.HiltComponentActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.inject.Inject
import kotlin.test.assertTrue
import com.google.samples.apps.nowinandroid.feature.topic.R as FeatureTopicR

@HiltAndroidTest
class InterestsListDetailScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @get:Rule(order = 3)
    val windowMetricsCalculatorRule = FakeWindowMetricsCalculatorRule()

    @Inject
    lateinit var topicsRepository: TopicsRepository

    // The strings used for matching in these tests.
    private val placeholderText by composeTestRule.stringResource(FeatureTopicR.string.feature_topic_select_an_interest)
    private val listPaneTag = "interests:topics"

    private val Topic.testTag
        get() = "topic:${this.id}"

    // Overrides for device sizes.
    private enum class TestDeviceConfig(val dpSize: DpSize) {
        Compact(DpSize(412.dp, 915.dp)),
        Expanded(DpSize(1200.dp, 840.dp)),
    }

    @Before
    fun setup() {
        hiltRule.inject()
    }

    /** Convenience function for getting all topics during tests, */
    private fun getTopics(): List<Topic> = runBlocking {
        topicsRepository.getTopics().first()
    }

    /**
     * Sets up a ForcedSize override with a matching size in the window metrics calculator rule.
     */
    @Composable
    private fun TestDeviceConfig.Override(
        content: @Composable () -> Unit
    ) {
        DeviceConfigurationOverride(override = DeviceConfigurationOverride.ForcedSize(dpSize)) {
            with(LocalDensity.current) {
                windowMetricsCalculatorRule.setWindowSize(dpSize.toSize())
            }
            content()
        }
    }

    @Test
    fun expandedWidth_initialState_showsTwoPanesWithPlaceholder() {
        composeTestRule.apply {
            setContent {
                TestDeviceConfig.Expanded.Override {
                    NiaTheme {
                        InterestsListDetailScreen()
                    }
                }
            }

            onNodeWithTag(listPaneTag).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsDisplayed()
        }
    }

    @Test
    fun compactWidth_initialState_showsListPane() {
        composeTestRule.apply {
            setContent {
                TestDeviceConfig.Compact.Override {
                    NiaTheme {
                        InterestsListDetailScreen()
                    }
                }
            }

            onNodeWithTag(listPaneTag).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
        }
    }

    @Test
    fun expandedWidth_topicSelected_updatesDetailPane() {
        composeTestRule.apply {
            setContent {
                TestDeviceConfig.Expanded.Override {
                    NiaTheme {
                        InterestsListDetailScreen()
                    }
                }
            }

            val firstTopic = getTopics().first()
            onNodeWithText(firstTopic.name).performClick()

            onNodeWithTag(listPaneTag).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
            onNodeWithTag(firstTopic.testTag).assertIsDisplayed()
        }
    }

    @Test
    fun compactWidth_topicSelected_showsTopicDetailPane() {
        composeTestRule.apply {
            setContent {
                TestDeviceConfig.Compact.Override {
                    NiaTheme {
                        InterestsListDetailScreen()
                    }
                }
            }

            val firstTopic = getTopics().first()
            onNodeWithText(firstTopic.name).performClick()

            onNodeWithTag(listPaneTag).assertIsNotDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
            onNodeWithTag(firstTopic.testTag).assertIsDisplayed()
        }
    }

    @Test
    fun expandedWidth_backPressFromTopicDetail_leavesInterests() {
        var unhandledBackPress = false
        composeTestRule.apply {
            setContent {
                TestDeviceConfig.Expanded.Override {
                    NiaTheme {
                        // Back press should not be handled by the two pane layout, and thus
                        // "fall through" to this BackHandler.
                        BackHandler {
                            unhandledBackPress = true
                        }
                        InterestsListDetailScreen()
                    }
                }
            }

            val firstTopic = getTopics().first()
            onNodeWithText(firstTopic.name).performClick()

            Espresso.pressBack()

            assertTrue(unhandledBackPress)
        }
    }

    @Test
    fun compactWidth_backPressFromTopicDetail_showsListPane() {
        composeTestRule.apply {
            setContent {
                TestDeviceConfig.Compact.Override {
                    NiaTheme {
                        InterestsListDetailScreen()
                    }
                }
            }

            val firstTopic = getTopics().first()
            onNodeWithText(firstTopic.name).performClick()

            Espresso.pressBack()

            onNodeWithTag(listPaneTag).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
            onNodeWithTag(firstTopic.testTag).assertIsNotDisplayed()
        }
    }
}
