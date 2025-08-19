/*
 * Copyright 2025 The Android Open Source Project
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

@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.google.samples.apps.nowinandroid.interests.impl

import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.test.espresso.Espresso
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigator
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStackViewModel
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavKey
import com.google.samples.apps.nowinandroid.feature.interests.api.R
import com.google.samples.apps.nowinandroid.feature.interests.api.navigation.InterestsRoute
import com.google.samples.apps.nowinandroid.feature.interests.impl.LIST_PANE_TEST_TAG
import com.google.samples.apps.nowinandroid.uitesthiltmanifest.HiltComponentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.getValue
import kotlin.properties.ReadOnlyProperty

private const val EXPANDED_WIDTH = "w1200dp-h840dp"
private const val COMPACT_WIDTH = "w412dp-h915dp"

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [35])
class InterestsListDetailScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    // entry point to get the features' hilt-injected EntryProviders that are installed in ActivityComponent
    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface EntryProvidersEntryPoint {
        fun getEntryProviders(): Set<@JvmSuppressWildcards EntryProviderScope<NiaNavKey>.() -> Unit>
    }

    @Inject
    lateinit var topicsRepository: TopicsRepository

    /** Convenience function for getting all topics during tests, */
    private fun getTopics(): List<Topic> = runBlocking {
        topicsRepository.getTopics().first().sortedBy { it.name }
    }

    // The strings used for matching in these tests.
    private val placeholderText by composeTestRule.stringResource(R.string.feature_interests_api_select_an_interest)

    private val Topic.testTag
        get() = "topic:${this.id}"

    private lateinit var entryProviderBuilders: Set<EntryProviderScope<NiaNavKey>.() -> Unit>

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.apply {
            entryProviderBuilders = EntryPoints.get(activity, EntryProvidersEntryPoint::class.java)
                .getEntryProviders()
        }
    }

    @Test
    @Config(qualifiers = EXPANDED_WIDTH)
    fun expandedWidth_initialState_showsTwoPanesWithPlaceholder() {
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    NavDisplay(
                        backStack = listOf<NiaNavKey>(InterestsRoute()),
                        sceneStrategy = rememberListDetailSceneStrategy(),
                        entryProvider = entryProvider {
                            entryProviderBuilders.forEach { it() }
                        },
                    )
                }
            }
            onNodeWithTag(LIST_PANE_TEST_TAG).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsDisplayed()
        }
    }

    @Test
    @Config(qualifiers = COMPACT_WIDTH)
    fun compactWidth_initialState_showsListPane() {
        composeTestRule.apply {
            setContent {
                NiaTheme {
                    NavDisplay(
                        backStack = listOf<NiaNavKey>(InterestsRoute()),
                        sceneStrategy = rememberListDetailSceneStrategy(),
                        entryProvider = entryProvider {
                            entryProviderBuilders.forEach { it() }
                        },
                    )
                }
            }

            onNodeWithTag(LIST_PANE_TEST_TAG).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
        }
    }

    @Test
    @Config(qualifiers = EXPANDED_WIDTH)
    fun expandedWidth_topicSelected_updatesDetailPane() {
        composeTestRule.apply {
            setContent {
                val backStackViewModel by composeTestRule.activity.viewModels<NiaBackStackViewModel>()
                val backStack = backStackViewModel.niaNavigator.backStack
                NiaTheme {
                    NavDisplay(
                        backStack = backStack,
                        sceneStrategy = rememberListDetailSceneStrategy(),
                        entryProvider = entryProvider {
                            entryProviderBuilders.forEach { it() }
                        },
                    )
                }
            }
            val firstTopic = getTopics().first()
            onNodeWithText(firstTopic.name).performClick()
            waitForIdle()

            onNodeWithTag(LIST_PANE_TEST_TAG).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
            onNodeWithTag(firstTopic.testTag).assertIsDisplayed()
        }
    }

    @Test
    @Config(qualifiers = COMPACT_WIDTH)
    fun compactWidth_topicSelected_showsTopicDetailPane() {
        composeTestRule.apply {
            setContent {
                val backStackViewModel by composeTestRule.activity.viewModels<NiaBackStackViewModel>()
                val backStack = backStackViewModel.niaNavigator.backStack
                NiaTheme {
                    NavDisplay(
                        backStack = backStack,
                        sceneStrategy = rememberListDetailSceneStrategy(),
                        entryProvider = entryProvider {
                            entryProviderBuilders.forEach { it() }
                        },
                    )
                }
            }

            val firstTopic = getTopics().first()
            onNodeWithText(firstTopic.name).performClick()

            onNodeWithTag(LIST_PANE_TEST_TAG).assertIsNotDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
            onNodeWithTag(firstTopic.testTag).assertIsDisplayed()
        }
    }

    @Test
    @Config(qualifiers = COMPACT_WIDTH)
    fun compactWidth_backPressFromTopicDetail_showsListPane() {
        composeTestRule.apply {
            setContent {
                val backStackViewModel by composeTestRule.activity.viewModels<NiaBackStackViewModel>()
                val backStack = backStackViewModel.niaNavigator.backStack
                NiaTheme {
                    NavDisplay(
                        backStack = backStack,
                        sceneStrategy = rememberListDetailSceneStrategy(),
                        entryProvider = entryProvider {
                            entryProviderBuilders.forEach { it() }
                        },
                    )
                }
            }

            val firstTopic = getTopics().first()
            onNodeWithText(firstTopic.name).performClick()

            waitForIdle()
            Espresso.pressBack()

            onNodeWithTag(LIST_PANE_TEST_TAG).assertIsDisplayed()
            onNodeWithText(placeholderText).assertIsNotDisplayed()
            onNodeWithTag(firstTopic.testTag).assertIsNotDisplayed()
        }
    }
}

private fun AndroidComposeTestRule<*, *>.stringResource(
    @StringRes resId: Int,
): ReadOnlyProperty<Any, String> =
    ReadOnlyProperty { _, _ -> activity.getString(resId) }

@Module
@InstallIn(SingletonComponent::class)
object BackStackProvider {
    @Provides
    @Singleton
    fun provideNiaBackStack(): NiaNavigator =
        NiaNavigator(startKey = InterestsRoute())

    @Provides
    @Singleton
    fun provideSerializersModule(
        polymorphicModuleBuilders: Set<@JvmSuppressWildcards PolymorphicModuleBuilder<NiaNavKey>.() -> Unit>,
    ): SerializersModule = SerializersModule {
        polymorphic(NiaNavKey::class) {
            polymorphicModuleBuilders.forEach { it() }
        }
    }
}
