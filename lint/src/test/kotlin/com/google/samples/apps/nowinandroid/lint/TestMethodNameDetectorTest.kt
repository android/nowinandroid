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

package com.google.samples.apps.nowinandroid.lint

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.google.samples.apps.nowinandroid.lint.TestMethodNameDetector.Companion.FORMAT
import com.google.samples.apps.nowinandroid.lint.TestMethodNameDetector.Companion.PREFIX
import org.junit.Test

class TestMethodNameDetectorTest {

    @Test
    fun `detect prefix`() {
        lint().issues(PREFIX)
            .files(
                JUNIT_TEST_STUB,
                kotlin(
                    """
                    import org.junit.Test
                    class Test {
                        @Test
                        fun foo() = Unit
                        @Test
                        fun test_foo() = Unit
                        @Test
                        fun `test foo`() = Unit
                    }
                """,
                ).indented(),
            )
            .run()
            .expect(
                """
                src/Test.kt:6: Warning: Test method starts with test [TestMethodPrefix]
                    fun test_foo() = Unit
                        ~~~~~~~~
                src/Test.kt:8: Warning: Test method starts with test [TestMethodPrefix]
                    fun `test foo`() = Unit
                        ~~~~~~~~~~
                0 errors, 2 warnings
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/Test.kt line 6: Remove prefix:
                @@ -6 +6
                -     fun test_foo() = Unit
                +     fun foo() = Unit
                Autofix for src/Test.kt line 8: Remove prefix:
                @@ -8 +8
                -     fun `test foo`() = Unit
                +     fun `foo`() = Unit
                """.trimIndent(),
            )
    }

    @Test
    fun `detect format`() {
        lint().issues(FORMAT)
            .files(
                JUNIT_TEST_STUB,
                kotlin(
                    "src/androidTest/com/example/Test.kt",
                    """
                    import org.junit.Test
                    class Test {
                        @Test
                        fun when_then() = Unit
                        @Test
                        fun given_when_then() = Unit

                        @Test
                        fun foo() = Unit
                        @Test
                        fun foo_bar_baz_qux() = Unit
                        @Test
                        fun `foo bar baz`() = Unit
                    }
                """,
                ).indented(),
            )
            .run()
            .expect(
                """
                src/androidTest/com/example/Test.kt:9: Warning: Test method does not follow the given_when_then or when_then format [TestMethodFormat]
                    fun foo() = Unit
                        ~~~
                src/androidTest/com/example/Test.kt:11: Warning: Test method does not follow the given_when_then or when_then format [TestMethodFormat]
                    fun foo_bar_baz_qux() = Unit
                        ~~~~~~~~~~~~~~~
                src/androidTest/com/example/Test.kt:13: Warning: Test method does not follow the given_when_then or when_then format [TestMethodFormat]
                    fun `foo bar baz`() = Unit
                        ~~~~~~~~~~~~~
                0 errors, 3 warnings
                """.trimIndent(),
            )
    }

    private companion object {
        private val JUNIT_TEST_STUB: TestFile = kotlin(
            """
                package org.junit
                annotation class Test
                """,
        ).indented()
    }
}
