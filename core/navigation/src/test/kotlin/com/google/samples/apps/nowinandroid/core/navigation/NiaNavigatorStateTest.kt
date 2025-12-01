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
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class NiaNavigatorStateTest {

    private lateinit var navigationState: NavigationState
    private lateinit var niaNavigator: NiaNavigator

    @Before
    fun setup() {
        navigationState = NavigationState(TestStartKey)
        niaNavigator = NiaNavigator(navigationState)
    }

    @Test
    fun testStartKey() {
        assertThat(navigationState.currentKey).isEqualTo(TestStartKey)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testNavigate() {
        niaNavigator.navigate(TestKeyFirst)

        assertThat(navigationState.currentKey).isEqualTo(TestKeyFirst)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testNavigateTopLevel() {
        niaNavigator.navigate(TestTopLevelKey)

        assertThat(navigationState.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestTopLevelKey)
    }

    @Test
    fun testNavigateSingleTop() {
        niaNavigator.navigate(TestKeyFirst)

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        niaNavigator.navigate(TestKeyFirst)

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()
    }

    @Test
    fun testNavigateTopLevelSingleTop() {
        niaNavigator.navigate(TestTopLevelKey)
        niaNavigator.navigate(TestKeyFirst)

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
            TestKeyFirst,
        ).inOrder()

        niaNavigator.navigate(TestTopLevelKey)

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
        ).inOrder()
    }

    @Test
    fun testSubStack() {
        niaNavigator.navigate(TestKeyFirst)

        assertThat(navigationState.currentKey).isEqualTo(TestKeyFirst)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)

        niaNavigator.navigate(TestKeySecond)

        assertThat(navigationState.currentKey).isEqualTo(TestKeySecond)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testMultiStack() {
        // add to start stack
        niaNavigator.navigate(TestKeyFirst)

        assertThat(navigationState.currentKey).isEqualTo(TestKeyFirst)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)

        // navigate to new top level
        niaNavigator.navigate(TestTopLevelKey)

        assertThat(navigationState.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // add to new stack
        niaNavigator.navigate(TestKeySecond)

        assertThat(navigationState.currentKey).isEqualTo(TestKeySecond)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // go back to start stack
        niaNavigator.navigate(TestStartKey)

        assertThat(navigationState.currentKey).isEqualTo(TestKeyFirst)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testRestore() {
        assertThat(navigationState.currentBackStack).containsExactly(TestStartKey)

        navigationState.restore(
            listOf(TestStartKey, TestTopLevelKey),
            linkedMapOf(
                TestStartKey to mutableStateListOf(TestStartKey, TestKeyFirst),
                TestTopLevelKey to mutableStateListOf(TestTopLevelKey, TestKeySecond),
            ),
        )

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestTopLevelKey,
            TestKeySecond,
        ).inOrder()

        assertThat(navigationState.currentKey).isEqualTo(TestKeySecond)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestTopLevelKey)
    }

    @Test
    fun testPopOneNonTopLevel() {
        niaNavigator.navigate(TestKeyFirst)
        niaNavigator.navigate(TestKeySecond)

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestKeySecond,
        ).inOrder()

        niaNavigator.pop()

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        assertThat(navigationState.currentKey).isEqualTo(TestKeyFirst)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testPopOneTopLevel() {
        niaNavigator.navigate(TestKeyFirst)
        niaNavigator.navigate(TestTopLevelKey)

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestTopLevelKey,
        ).inOrder()

        assertThat(navigationState.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // remove TopLevel
        niaNavigator.pop()

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        assertThat(navigationState.currentKey).isEqualTo(TestKeyFirst)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun popMultipleNonTopLevel() {
        niaNavigator.navigate(TestKeyFirst)
        niaNavigator.navigate(TestKeySecond)

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestKeySecond,
        ).inOrder()

        niaNavigator.pop()
        niaNavigator.pop()

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
        ).inOrder()

        assertThat(navigationState.currentKey).isEqualTo(TestStartKey)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun popMultipleTopLevel() {
        val testTopLevelKeyTwo = object : NiaNavKey {
            override val isTopLevel: Boolean
                get() = true
        }

        // second sub-stack
        niaNavigator.navigate(TestTopLevelKey)
        niaNavigator.navigate(TestKeyFirst)
        // third sub-stack
        niaNavigator.navigate(testTopLevelKeyTwo)
        niaNavigator.navigate(TestKeySecond)

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
            TestKeyFirst,
            testTopLevelKeyTwo,
            TestKeySecond,
        ).inOrder()

        repeat(4) {
            niaNavigator.pop()
        }

        assertThat(navigationState.currentBackStack).containsExactly(
            TestStartKey,
        ).inOrder()

        assertThat(navigationState.currentKey).isEqualTo(TestStartKey)
        assertThat(navigationState.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun throwOnEmptyBackStack() {
        assertFailsWith<IllegalStateException> {
            niaNavigator.pop()
        }
    }
}

private object TestStartKey : NiaNavKey {
    override val isTopLevel: Boolean
        get() = true
}

private object TestTopLevelKey : NiaNavKey {
    override val isTopLevel: Boolean
        get() = true
}

private object TestKeyFirst : NiaNavKey {
    override val isTopLevel: Boolean
        get() = false
}

private object TestKeySecond : NiaNavKey {
    override val isTopLevel: Boolean
        get() = false
}
