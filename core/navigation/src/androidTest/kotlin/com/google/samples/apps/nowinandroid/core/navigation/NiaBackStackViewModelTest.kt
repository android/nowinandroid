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

package com.google.samples.apps.nowinandroid.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.testing.ViewModelScenario
import androidx.lifecycle.viewmodel.testing.viewModelScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NiaBackStackViewModelTest {

    @get:Rule val rule = createComposeRule()

    private val serializersModules = SerializersModule {
        polymorphic(NiaNavKey::class) {
            subclass(TestStartKey::class, TestStartKey.serializer())
            subclass(TestTopLevelKeyFirst::class, TestTopLevelKeyFirst.serializer())
            subclass(TestTopLevelKeySecond::class, TestTopLevelKeySecond.serializer())
            subclass(TestKeyFirst::class, TestKeyFirst.serializer())
            subclass(TestKeySecond::class, TestKeySecond.serializer())
        }
    }

    private fun createViewModel() = NiaBackStackViewModel(
        savedStateHandle = SavedStateHandle(),
        niaNavigatorState = NiaNavigatorState(TestStartKey),
        serializersModules = serializersModules,
    )

    @Test
    fun testStartKeySaved() {
        rule.setContent {
            val viewModel = createViewModel()
            assertThat(viewModel.backStackMap.size).isEqualTo(1)
            val entry = viewModel.backStackMap[TestStartKey]
            assertThat(entry).isNotNull()
            assertThat(entry).containsExactly(TestStartKey)
        }
    }

    @Test
    fun testNonTopLevelKeySaved() {
        val viewModel = createViewModel()
        rule.setContent {
            val navigator = remember { NiaNavigator( viewModel.niaNavigatorState) }
            navigator.navigate(TestKeyFirst)
        }
        assertThat(viewModel.backStackMap.size).isEqualTo(1)
        val entry = viewModel.backStackMap[TestStartKey]
        assertThat(entry).isNotNull()
        assertThat(entry).containsExactly(TestStartKey, TestKeyFirst).inOrder()
    }

    @Test
    fun testTopLevelKeySaved() {
        val viewModel = createViewModel()
        rule.setContent {
            val navigator = remember { NiaNavigator( viewModel.niaNavigatorState) }

            navigator.navigate(TestKeyFirst)
            navigator.navigate(TestTopLevelKeyFirst)
        }

        assertThat(viewModel.backStackMap.size).isEqualTo(2)

        val entry = viewModel.backStackMap[TestStartKey]
        assertThat(entry).isNotNull()
        assertThat(entry).containsExactly(TestStartKey, TestKeyFirst).inOrder()

        val entry2 = viewModel.backStackMap[TestTopLevelKeyFirst]
        assertThat(entry2).isNotNull()
        assertThat(entry2).containsExactly(TestTopLevelKeyFirst)
    }

    @Test
    fun testMultiStacksSaved() {
        val viewModel = createViewModel()
        rule.setContent {
            val navigator = remember { NiaNavigator( viewModel.niaNavigatorState) }
            navigator.navigate(TestKeyFirst)
            navigator.navigate(TestTopLevelKeyFirst)
            navigator.navigate(TestKeySecond)
        }

        assertThat(viewModel.backStackMap.size).isEqualTo(2)

        val entry = viewModel.backStackMap[TestStartKey]
        assertThat(entry).isNotNull()
        assertThat(entry).containsExactly(TestStartKey, TestKeyFirst).inOrder()

        val entry2 = viewModel.backStackMap[TestTopLevelKeyFirst]
        assertThat(entry2).isNotNull()
        assertThat(entry2).containsExactly(TestTopLevelKeyFirst, TestKeySecond).inOrder()
    }

    @Test
    fun testPopSaved() {
        val viewModel = createViewModel()
        rule.setContent {
            val navigator = remember { NiaNavigator( viewModel.niaNavigatorState) }

            navigator.navigate(TestKeyFirst)

            assertThat(viewModel.backStackMap.size).isEqualTo(1)
            val entry = viewModel.backStackMap[TestStartKey]
            assertThat(entry).isNotNull()
            assertThat(entry).containsExactly(TestStartKey, TestKeyFirst).inOrder()

            navigator.pop()

            assertThat(viewModel.backStackMap.size).isEqualTo(1)
            val entry2 = viewModel.backStackMap[TestStartKey]
            assertThat(entry2).isNotNull()
            assertThat(entry2).containsExactly(TestStartKey).inOrder()
        }
    }

    @Test
    fun testRestore() {
        lateinit var scenario: ViewModelScenario<NiaBackStackViewModel>
        lateinit var navigator: NiaNavigator
        lateinit var navigatorState: NiaNavigatorState
        rule.setContent {
            navigatorState = remember { NiaNavigatorState(TestStartKey) }
            navigator = remember { NiaNavigator(navigatorState) }
            scenario = viewModelScenario {
                NiaBackStackViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    niaNavigatorState = navigatorState,
                    serializersModules = serializersModules,
                )
            }
        }

        rule.runOnIdle {
            navigator.navigate(TestKeyFirst)
            assertThat(navigatorState.currentBackStack).containsExactly(
                TestStartKey,
                TestKeyFirst,
            ).inOrder()
        }

        scenario.recreate()

        rule.runOnIdle {
            assertThat(navigatorState.currentBackStack).containsExactly(
                TestStartKey,
                TestKeyFirst,
            ).inOrder()
        }

        scenario.close()
    }

    @Test
    fun testRestoreMultiStacks() {
        lateinit var scenario: ViewModelScenario<NiaBackStackViewModel>
        lateinit var navigator: NiaNavigator
        lateinit var navigatorState: NiaNavigatorState
        rule.setContent {
            navigatorState = remember { NiaNavigatorState(TestStartKey) }
            navigator = remember { NiaNavigator(navigatorState) }
            scenario = viewModelScenario {
                NiaBackStackViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    niaNavigatorState = navigatorState,
                    serializersModules = serializersModules,
                )
            }
        }

        rule.runOnIdle {
            navigator.navigate(TestKeyFirst)
            navigator.navigate(TestTopLevelKeyFirst)
            navigator.navigate(TestKeySecond)

            assertThat(navigatorState.currentBackStack).containsExactly(
                TestStartKey,
                TestKeyFirst,
                TestTopLevelKeyFirst,
                TestKeySecond,
            ).inOrder()
        }

        scenario.recreate()

        rule.runOnIdle {
            assertThat(navigatorState.currentBackStack).containsExactly(
                TestStartKey,
                TestKeyFirst,
                TestTopLevelKeyFirst,
                TestKeySecond,
            ).inOrder()
        }

        scenario.close()
    }
}

@Serializable
private object TestStartKey : NiaNavKey {
    override val isTopLevel: Boolean
        get() = true
}

@Serializable
private object TestTopLevelKeyFirst : NiaNavKey {
    override val isTopLevel: Boolean
        get() = true
}

@Serializable
private object TestTopLevelKeySecond : NiaNavKey {
    override val isTopLevel: Boolean
        get() = true
}

@Serializable
private object TestKeyFirst : NiaNavKey {
    override val isTopLevel: Boolean
        get() = false
}

@Serializable
private object TestKeySecond : NiaNavKey {
    override val isTopLevel: Boolean
        get() = false
}
