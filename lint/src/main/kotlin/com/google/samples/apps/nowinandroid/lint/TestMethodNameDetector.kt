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

import com.android.tools.lint.detector.api.AnnotationInfo
import com.android.tools.lint.detector.api.AnnotationUsageInfo
import com.android.tools.lint.detector.api.Category.Companion.TESTING
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.TextFormat.RAW
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UElement
import java.util.EnumSet
import kotlin.io.path.Path

/**
 * A detector that checks for common patterns in naming the test methods:
 * - [detectPrefix] removes unnecessary "test" prefix in all unit test.
 * - [detectFormat] Checks the `given_when_then` format of Android instrumented tests (backticks are not supported).
 */
class TestMethodNameDetector : Detector(), SourceCodeScanner {

    override fun applicableAnnotations() = listOf("org.junit.Test")

    override fun visitAnnotationUsage(
        context: JavaContext,
        element: UElement,
        annotationInfo: AnnotationInfo,
        usageInfo: AnnotationUsageInfo,
    ) {
        val method = usageInfo.referenced as? PsiMethod ?: return

        method.detectPrefix(context, usageInfo)
        method.detectFormat(context, usageInfo)
    }

    private fun JavaContext.isAndroidTest() = Path("androidTest") in file.toPath()

    private fun PsiMethod.detectPrefix(
        context: JavaContext,
        usageInfo: AnnotationUsageInfo,
    ) {
        if (!name.startsWith("test")) return
        context.report(
            issue = PREFIX,
            scope = usageInfo.usage,
            location = context.getNameLocation(this),
            message = PREFIX.getBriefDescription(RAW),
            quickfixData = LintFix.create()
                .name("Remove prefix")
                .replace().pattern("""test[\s_]*""")
                .with("")
                .autoFix()
                .build(),
        )
    }

    private fun PsiMethod.detectFormat(
        context: JavaContext,
        usageInfo: AnnotationUsageInfo,
    ) {
        if (!context.isAndroidTest()) return
        if ("""[^\W_]+(_[^\W_]+){1,2}""".toRegex().matches(name)) return
        context.report(
            issue = FORMAT,
            scope = usageInfo.usage,
            location = context.getNameLocation(this),
            message = FORMAT.getBriefDescription(RAW),
        )
    }

    companion object {

        private fun issue(
            id: String,
            briefDescription: String,
            explanation: String,
        ): Issue = Issue.create(
            id = id,
            briefDescription = briefDescription,
            explanation = explanation,
            category = TESTING,
            priority = 5,
            severity = WARNING,
            implementation = Implementation(
                TestMethodNameDetector::class.java,
                EnumSet.of(JAVA_FILE, TEST_SOURCES),
            ),
        )

        @JvmField
        val PREFIX: Issue = issue(
            id = "TestMethodPrefix",
            briefDescription = "Test method starts with `test`",
            explanation = "Test method should not start with `test`.",
        )

        @JvmField
        val FORMAT: Issue = issue(
            id = "TestMethodFormat",
            briefDescription = "Test method does not follow the `given_when_then` or `when_then` format",
            explanation = "Test method should follow the `given_when_then` or `when_then` format.",
        )
    }
}
