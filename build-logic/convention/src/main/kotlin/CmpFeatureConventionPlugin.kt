/*
 *   Copyright 2024 The Android Open Source Project
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

import com.android.build.gradle.LibraryExtension
import com.google.samples.apps.nowinandroid.configureGradleManagedDevices
import com.google.samples.apps.nowinandroid.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

// Convention plugin for the Compose Multiplatform feature module
class CmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("nowinandroid.kmp.library")
                apply("nowinandroid.di.koin")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
                }
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }

            dependencies {
                add("commonMainImplementation", project(":core:ui"))
                add("commonMainImplementation", project(":core:designsystem"))
                add("commonMainImplementation", libs.findLibrary("jetbrains.compose.viewmodel").get())
                add("commonMainImplementation", libs.findLibrary("jetbrains.compose.navigation").get())
                add("commonMainImplementation", libs.findLibrary("koin.compose").get())
                add("commonMainImplementation", libs.findLibrary("koin.compose.viewmodel").get())
                add("commonMainImplementation", libs.findLibrary("koin.compose.viewmodel.navigation").get())

                add("androidMainImplementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("androidMainImplementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("androidMainImplementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("androidMainImplementation", libs.findLibrary("androidx.tracing.ktx").get())

                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.compose.ui.test").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.lifecycle.runtimeTesting").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.test.core").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.test.ext").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.test.junit").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.test.runner").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.test.espresso.core").get())
            }
        }
    }
}
