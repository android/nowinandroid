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

package com.google.samples.apps.nowinandroid.lint.designsystem

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

@Suppress("TestFunctionName") // prevent code snippets from being flagged as incorrectly named test functions
class DesignSystemDetectorTest {

    @Test
    fun methodNameWithReplacement() {
        lint()
            .issues(DesignSystemDetector.ISSUE)
            .files(
                kotlin(
                    """
                        package androidx.compose.material3
        
                        fun TextButton()
                    """,
                ).indented(),
                kotlin(
                    """
                        import androidx.compose.material3.TextButton

                        fun MyScreen() {
                            TextButton()
                        }
                    """,
                ).indented(),
            )
            .allowMissingSdk()
            .run()
            .expectContains(
                """
                    src/test.kt:4: Error: Using TextButton instead of NiaTextButton [DesignSystem]
                        TextButton()
                        ~~~~~~~~~~~~
                    1 errors, 0 warnings
                """,
            )
    }

    @Test
    fun methodNameInDesignSystem() {
        lint()
            .issues(DesignSystemDetector.ISSUE)
            .files(
                kotlin(
                    """
                        package com.google.samples.apps.nowinandroid.core.designsystem.component
        
                        fun NiaTextButton()
                    """,
                ).indented(),
                kotlin(
                    """
                        import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTextButton
    
                        fun MyScreen() {
                            NiaTextButton()
                        }
                    """,
                ).indented(),
            )
            .allowMissingSdk()
            .run()
            .expectClean()
    }

    @Test
    fun receiverWithReplacement() {
        lint()
            .issues(DesignSystemDetector.ISSUE)
            .files(
                kotlin(
                    """
                        package androidx.compose.material.icons
        
                        object Icons {
                            object Rounded
                        }
        
                        val Icons.Rounded.Add: Any = TODO()
                    """,
                ).indented(),
                kotlin(
                    """
                        import androidx.compose.material.icons.Icons

                        val add = Icons.Rounded.Add
                    """,
                ).indented(),
            )
            .allowMissingSdk()
            .run()
            .expectContains("Using Icons instead of NiaIcons")
    }

    @Test
    fun receiverInDesignSystem() {
        lint()
            .issues(DesignSystemDetector.ISSUE)
            .files(
                kotlin(
                    """
                        package com.google.samples.apps.nowinandroid.core.designsystem.icon

                        object NiaIcons {
                            object Add
                        }
                    """,
                ).indented(),
                kotlin(
                    """
                        import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons

                        val add = NiaIcons.Add
                    """,
                ).indented(),
            )
            .allowMissingSdk()
            .run()
            .expectClean()
    }
}
