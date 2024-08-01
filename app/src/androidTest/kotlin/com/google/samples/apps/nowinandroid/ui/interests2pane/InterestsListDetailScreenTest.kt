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
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.espresso.device.DeviceInteraction.Companion.setDisplaySize
import androidx.test.espresso.device.EspressoDevice
import androidx.test.espresso.device.rules.DisplaySizeRule
import androidx.test.espresso.device.sizeclass.HeightSizeClass
import androidx.test.espresso.device.sizeclass.WidthSizeClass
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.Topic
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
    val displaySizeRule: DisplaySizeRule = DisplaySizeRule()

    @Inject
    lateinit var topicsRepository: TopicsRepository

    // The strings used for matching in these tests.
    private val placeholderText by composeTestRule.stringResource(FeatureTopicR.string.feature_topic_select_an_interest)
    private val listPaneTag = "interests:topics"

    private val Topic.testTag
        get() = "topic:${this.id}"

    /** Convenience function for getting all topics during tests, */
    private fun getTopics(): List<Topic> = runBlocking {
        topicsRepository.getTopics().first().sortedBy { it.name }
    }

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun expandedWidth_initialState_showsTwoPanesWithPlaceholder() {
        EspressoDevice.onDevice().setDisplaySize(
            widthSizeClass = WidthSizeClass.EXPANDED,
            heightSizeClass = HeightSizeClass.EXPANDED,
        )
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    InterestsListDetailScreen()
                }
            }

            onNodeWithTag(listPaneTag).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsDisplayed()
        }
    }

    @Test
    fun compactWidth_initialState_showsListPane() {
        EspressoDevice.onDevice().setDisplaySize(
            widthSizeClass = WidthSizeClass.COMPACT,
            heightSizeClass = HeightSizeClass.MEDIUM,
        )
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    InterestsListDetailScreen()
                }
            }

            onNodeWithTag(listPaneTag).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
        }
    }

    @Test
    fun expandedWidth_topicSelected_updatesDetailPane() {
        EspressoDevice.onDevice().setDisplaySize(
            widthSizeClass = WidthSizeClass.EXPANDED,
            heightSizeClass = HeightSizeClass.EXPANDED,
        )
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    InterestsListDetailScreen()
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
        EspressoDevice.onDevice().setDisplaySize(
            widthSizeClass = WidthSizeClass.COMPACT,
            heightSizeClass = HeightSizeClass.MEDIUM,
        )
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    InterestsListDetailScreen()
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
        EspressoDevice.onDevice().setDisplaySize(
            widthSizeClass = WidthSizeClass.EXPANDED,
            heightSizeClass = HeightSizeClass.EXPANDED,
        )

        var unhandledBackPress = false
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    // Back press should not be handled by the two pane layout, and thus
                    // "fall through" to this BackHandler.
                    BackHandler {
                        unhandledBackPress = true
                    }
                    InterestsListDetailScreen()
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
        EspressoDevice.onDevice().setDisplaySize(
            widthSizeClass = WidthSizeClass.COMPACT,
            heightSizeClass = HeightSizeClass.MEDIUM,
        )
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    InterestsListDetailScreen()
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
