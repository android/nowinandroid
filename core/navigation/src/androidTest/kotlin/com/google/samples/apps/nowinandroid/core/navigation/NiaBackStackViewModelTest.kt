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
        niaBackStack = NiaBackStack(TestStartKey),
        serializersModules = serializersModules,
    )

    @Test
    fun testStartKeySaved() {
        rule.setContent {
            val viewModel = createViewModel()
            assertThat(viewModel.backStackMap).containsEntry(
                TestStartKey,
                mutableListOf(TestStartKey),
            )
        }
    }

    @Test
    fun testNonTopLevelKeySaved() {
        val viewModel = createViewModel()
        rule.setContent {
            val backStack = viewModel.niaBackStack

            backStack.navigate(TestKeyFirst)
        }

        assertThat(viewModel.backStackMap).containsEntry(
            TestStartKey,
            mutableListOf(TestStartKey, TestKeyFirst),
        )
    }

    @Test
    fun testTopLevelKeySaved() {
        val viewModel = createViewModel()
        rule.setContent {
            val backStack = viewModel.niaBackStack

            backStack.navigate(TestKeyFirst)
            backStack.navigate(TestTopLevelKeyFirst)
        }

        assertThat(viewModel.backStackMap).containsExactly(
            TestStartKey,
            mutableListOf(TestStartKey, TestKeyFirst),
            TestTopLevelKeyFirst,
            mutableListOf(TestTopLevelKeyFirst),
        ).inOrder()
    }

    @Test
    fun testMultiStacksSaved() {
        val viewModel = createViewModel()
        rule.setContent {
            viewModel.niaBackStack.navigate(TestKeyFirst)
            viewModel.niaBackStack.navigate(TestTopLevelKeyFirst)
            viewModel.niaBackStack.navigate(TestKeySecond)
        }

        assertThat(viewModel.backStackMap).containsExactly(
            TestStartKey,
            mutableListOf(TestStartKey, TestKeyFirst),
            TestTopLevelKeyFirst,
            mutableListOf(TestTopLevelKeyFirst, TestKeySecond),
        ).inOrder()
    }

    @Test
    fun testPopSaved() {
        val viewModel = createViewModel()
        rule.setContent {
            val backStack = viewModel.niaBackStack

            backStack.navigate(TestKeyFirst)
            assertThat(viewModel.backStackMap).containsExactly(
                TestStartKey,
                mutableListOf(TestStartKey, TestKeyFirst),
            )

            backStack.popLast()
            assertThat(viewModel.backStackMap).containsExactly(
                TestStartKey,
                mutableListOf(TestStartKey),
            )
        }
    }

    @Test
    fun testRestore() {
        lateinit var scenario: ViewModelScenario<NiaBackStackViewModel>
        rule.setContent {
            scenario = viewModelScenario {
                NiaBackStackViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    niaBackStack = NiaBackStack(TestStartKey),
                    serializersModules = serializersModules,
                )
            }
        }

        rule.runOnIdle {
            scenario.viewModel.niaBackStack.navigate(TestKeyFirst)
            assertThat(scenario.viewModel.niaBackStack.backStack).containsExactly(
                TestStartKey,
                TestKeyFirst,
            ).inOrder()
        }

        scenario.recreate()

        rule.runOnIdle {
            assertThat(scenario.viewModel.niaBackStack.backStack).containsExactly(
                TestStartKey,
                TestKeyFirst,
            ).inOrder()
        }
    }

    @Test
    fun testRestoreMultiStacks() {
        lateinit var scenario: ViewModelScenario<NiaBackStackViewModel>
        rule.setContent {
            scenario = viewModelScenario {
                NiaBackStackViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    niaBackStack = NiaBackStack(TestStartKey),
                    serializersModules = serializersModules,
                )
            }
        }

        rule.runOnIdle {
            scenario.viewModel.niaBackStack.navigate(TestKeyFirst)
            scenario.viewModel.niaBackStack.navigate(TestTopLevelKeyFirst)
            scenario.viewModel.niaBackStack.navigate(TestKeySecond)

            assertThat(scenario.viewModel.niaBackStack.backStack).containsExactly(
                TestStartKey,
                TestKeyFirst,
                TestTopLevelKeyFirst,
                TestKeySecond,
            ).inOrder()
        }

        scenario.recreate()

        rule.runOnIdle {
            assertThat(scenario.viewModel.niaBackStack.backStack).containsExactly(
                TestStartKey,
                TestKeyFirst,
                TestTopLevelKeyFirst,
                TestKeySecond,
            ).inOrder()
        }
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
