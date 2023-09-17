/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.google.samples.apps.nowinandroid.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply(libs.findPlugin("hilt").get().get().pluginId)
                // KAPT must go last to avoid build warnings.
                // See: https://stackoverflow.com/questions/70550883/warning-the-following-options-were-not-recognized-by-any-processor-dagger-f
                apply(libs.findPlugin("kotlin-kapt").get().get().pluginId)
            }

            dependencies {
                "implementation"(libs.findLibrary("hilt.android").get())
                with(libs.findLibrary("hilt.compiler").get().get()) {
                    "kapt"(this)
                    "kaptTest"(this)
                    "kaptAndroidTest"(this)
                }
                with(libs.findLibrary("hilt.android.testing").get().get()) {
                    "testImplementation"(this)
                    "androidTestImplementation"(this)
                }
            }
            // https://youtrack.jetbrains.com/issue/KT-30589/Kapt-An-illegal-reflective-access-operation-has-occurred#focus=Comments-27-7442009.0-0
            tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask>()
                .configureEach {
                    listOf(
                        "util",
                        "file",
                        "main",
                        "jvm",
                        "processing",
                        "comp",
                        "tree",
                        "api",
                        "parser",
                        "code",
                    )
                        .flatMap {
                            listOf(
                                "--add-opens",
                                "jdk.compiler/com.sun.tools.javac.$it=ALL-UNNAMED",
                            )
                        }
                        .forEach(kaptProcessJvmArgs::addAll)
                }
        }
    }
}
