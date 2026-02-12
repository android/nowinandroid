/*
 * Copyright 2024 The Android Open Source Project
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

import com.android.build.gradle.LibraryExtension
import com.google.samples.apps.nowinandroid.configureGradleManagedDevices
import com.google.samples.apps.nowinandroid.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

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
                testOptions.unitTests.isIncludeAndroidResources = true
                configureGradleManagedDevices(this)
            }

            dependencies {
                "commonMainImplementation"(project(":core:ui"))
                "commonMainImplementation"(project(":core:designsystem"))
                "commonMainImplementation"(libs.findLibrary("jetbrains.compose.viewmodel").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains.compose.navigation").get())
                "commonMainImplementation"(platform(libs.findLibrary("koin.bom").get()))
                "commonMainImplementation"(libs.findLibrary("koin.compose").get())
                "commonMainImplementation"(libs.findLibrary("koin.compose.viewmodel").get())
                "commonMainImplementation"(libs.findLibrary("koin.compose.viewmodel.navigation").get())

                "commonMainImplementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "androidMainImplementation"(libs.findLibrary("androidx.tracing.ktx").get())

                "androidInstrumentedTestImplementation"(libs.findLibrary("androidx.compose.ui.test").get())
                "androidInstrumentedTestImplementation"(libs.findLibrary("androidx.test.core").get())
                "androidInstrumentedTestImplementation"(libs.findLibrary("androidx.test.ext").get())
                "androidInstrumentedTestImplementation"(libs.findLibrary("androidx.test.junit").get())
                "androidInstrumentedTestImplementation"(libs.findLibrary("androidx.test.runner").get())
            }
        }
    }
}
