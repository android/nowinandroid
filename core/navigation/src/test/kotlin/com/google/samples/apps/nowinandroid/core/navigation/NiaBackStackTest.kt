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

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class NiaBackStackTest {

    private lateinit var niaBackStack: NiaBackStack

    @Before
    fun setup() {
        niaBackStack = NiaBackStack(TestStartKey)
    }

    @Test
    fun testStartKey() {
        assertThat(niaBackStack.currentKey).isEqualTo(TestStartKey)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testNavigate() {
        niaBackStack.navigate(TestKeyFirst)

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testNavigateTopLevel() {
        niaBackStack.navigate(TestTopLevelKey)

        assertThat(niaBackStack.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)
    }

    @Test
    fun testNavigateSingleTop() {
        niaBackStack.navigate(TestKeyFirst)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        niaBackStack.navigate(TestKeyFirst)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()
    }

    @Test
    fun testNavigateTopLevelSingleTop() {
        niaBackStack.navigate(TestTopLevelKey)
        niaBackStack.navigate(TestKeyFirst)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
            TestKeyFirst,
        ).inOrder()

        niaBackStack.navigate(TestTopLevelKey)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
        ).inOrder()
    }

    @Test
    fun testSubStack() {
        niaBackStack.navigate(TestKeyFirst)

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)

        niaBackStack.navigate(TestKeySecond)

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeySecond)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testMultiStack() {
        // add to start stack
        niaBackStack.navigate(TestKeyFirst)

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)

        // navigate to new top level
        niaBackStack.navigate(TestTopLevelKey)

        assertThat(niaBackStack.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // add to new stack
        niaBackStack.navigate(TestKeySecond)

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeySecond)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // go back to start stack
        niaBackStack.navigate(TestStartKey)

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testRestore() {
        assertThat(niaBackStack.backStack).containsExactly(TestStartKey)

        niaBackStack.restore(
            linkedMapOf(
                TestStartKey to mutableListOf(TestStartKey, TestKeyFirst),
                TestTopLevelKey to mutableListOf(TestTopLevelKey, TestKeySecond),
            ),
        )

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestTopLevelKey,
            TestKeySecond,
        ).inOrder()

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeySecond)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)
    }

    @Test
    fun testPopOneNonTopLevel() {
        niaBackStack.navigate(TestKeyFirst)
        niaBackStack.navigate(TestKeySecond)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestKeySecond,
        ).inOrder()

        niaBackStack.popLast()

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun testPopOneTopLevel() {
        niaBackStack.navigate(TestKeyFirst)
        niaBackStack.navigate(TestTopLevelKey)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestTopLevelKey,
        ).inOrder()

        assertThat(niaBackStack.currentKey).isEqualTo(TestTopLevelKey)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestTopLevelKey)

        // remove TopLevel
        niaBackStack.popLast()

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
        ).inOrder()

        assertThat(niaBackStack.currentKey).isEqualTo(TestKeyFirst)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun popMultipleNonTopLevel() {
        niaBackStack.navigate(TestKeyFirst)
        niaBackStack.navigate(TestKeySecond)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestKeyFirst,
            TestKeySecond,
        ).inOrder()

        niaBackStack.popLast(2)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
        ).inOrder()

        assertThat(niaBackStack.currentKey).isEqualTo(TestStartKey)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun popMultipleTopLevel() {
        val testTopLevelKeyTwo = object : NiaNavKey {
            override val isTopLevel: Boolean
                get() = true
        }

        // second sub-stack
        niaBackStack.navigate(TestTopLevelKey)
        niaBackStack.navigate(TestKeyFirst)
        // third sub-stack
        niaBackStack.navigate(testTopLevelKeyTwo)
        niaBackStack.navigate(TestKeySecond)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
            TestTopLevelKey,
            TestKeyFirst,
            testTopLevelKeyTwo,
            TestKeySecond,
        ).inOrder()

        niaBackStack.popLast(4)

        assertThat(niaBackStack.backStack).containsExactly(
            TestStartKey,
        ).inOrder()

        assertThat(niaBackStack.currentKey).isEqualTo(TestStartKey)
        assertThat(niaBackStack.currentTopLevelKey).isEqualTo(TestStartKey)
    }

    @Test
    fun throwOnEmptyBackStack() {
        assertFailsWith<IllegalStateException> {
            niaBackStack.popLast(1)
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
