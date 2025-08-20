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

package com.google.samples.apps.nowinandroid.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.ui.interests2pane.InterestsListDetailScreen
import com.google.samples.apps.nowinandroid.core.analytics.analyticsModule
import com.google.samples.apps.nowinandroid.core.data.test.testDataModule
import com.google.samples.apps.nowinandroid.core.datastore.test.testDataStoreModule
import com.google.samples.apps.nowinandroid.core.domain.di.domainModule
import com.google.samples.apps.nowinandroid.core.testing.KoinTestApplication
import com.google.samples.apps.nowinandroid.core.testing.di.testDispatchersModule
import com.google.samples.apps.nowinandroid.di.appModule
import com.google.samples.apps.nowinandroid.di.featureModules
import com.google.samples.apps.nowinandroid.core.sync.test.testSyncModule
import com.google.samples.apps.nowinandroid.core.testing.rule.SafeKoinTestRule
import org.koin.test.KoinTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.koin.core.component.inject
import kotlin.properties.ReadOnlyProperty
import kotlin.test.assertTrue
import com.google.samples.apps.nowinandroid.feature.topic.R as FeatureTopicR

private const val EXPANDED_WIDTH = "w1200dp-h840dp"
private const val COMPACT_WIDTH = "w412dp-h915dp"

@RunWith(RobolectricTestRunner::class)
@Config(application = KoinTestApplication::class)
class InterestsListDetailScreenTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = SafeKoinTestRule.create(
        modules = listOf(
            testDataModule,
            testDataStoreModule,
            testNetworkModule,
            domainModule,
            testDispatchersModule,
            testScopeModule,
            analyticsModule,
            testSyncModule,
            appModule,
            featureModules,
        )
    )

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val topicsRepository: TopicsRepository by inject()

    /** Convenience function for getting all topics during tests, */
    private fun getTopics(): List<Topic> = runBlocking {
        topicsRepository.getTopics().first().sortedBy { it.name }
    }

    // The strings used for matching in these tests.
    private val placeholderText by composeTestRule.stringResource(FeatureTopicR.string.feature_topic_select_an_interest)
    private val listPaneTag = "interests:topics"

    private val Topic.testTag
        get() = "topic:${this.id}"

    @Before
    fun setup() {
        // No need to inject with Koin
    }

    @Test
    @Config(qualifiers = EXPANDED_WIDTH)
    fun expandedWidth_initialState_showsTwoPanesWithPlaceholder() {
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
    @Config(qualifiers = COMPACT_WIDTH)
    fun compactWidth_initialState_showsListPane() {
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
    @Config(qualifiers = EXPANDED_WIDTH)
    fun expandedWidth_topicSelected_updatesDetailPane() {
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
    @Config(qualifiers = COMPACT_WIDTH)
    fun compactWidth_topicSelected_showsTopicDetailPane() {
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
    @Config(qualifiers = EXPANDED_WIDTH)
    fun expandedWidth_backPressFromTopicDetail_leavesInterests() {
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

            waitForIdle()
            Espresso.pressBack()

            assertTrue(unhandledBackPress)
        }
    }

    @Test
    @Config(qualifiers = COMPACT_WIDTH)
    fun compactWidth_backPressFromTopicDetail_showsListPane() {
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    InterestsListDetailScreen()
                }
            }

            val firstTopic = getTopics().first()
            onNodeWithText(firstTopic.name).performClick()

            waitForIdle()
            Espresso.pressBack()

            onNodeWithTag(listPaneTag).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
            onNodeWithTag(firstTopic.testTag).assertIsNotDisplayed()
        }
    }
}

private fun ComposeContentTestRule.stringResource(
    @StringRes resId: Int,
): ReadOnlyProperty<Any, String> =
    ReadOnlyProperty { _, _ -> 
        when (resId) {
            FeatureTopicR.string.feature_topic_select_an_interest -> "Select an Interest"
            else -> "Unknown string resource"
        }
    }
