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

import com.android.build.gradle.LibraryExtension
import com.google.samples.apps.nowinandroid.androidTestImplementation
import com.google.samples.apps.nowinandroid.configureGradleManagedDevices
import com.google.samples.apps.nowinandroid.projectImplementation
import com.google.samples.apps.nowinandroid.implementation
import com.google.samples.apps.nowinandroid.projectAndroidTestImplementation
import com.google.samples.apps.nowinandroid.projectTestImplementation
import com.google.samples.apps.nowinandroid.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("nowinandroid.android.library")
                apply("nowinandroid.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
                }
                configureGradleManagedDevices(this)
            }

            dependencies {
                projectImplementation(":core:model")
                projectImplementation(":core:ui")
                projectImplementation(":core:designsystem")
                projectImplementation(":core:data")
                projectImplementation(":core:common")
                projectImplementation(":core:domain")
                projectImplementation(":core:analytics")

                testImplementation("kotlin.test")
                projectTestImplementation(":core:testing")
                androidTestImplementation("kotlin.test")
                projectAndroidTestImplementation(":core:testing")

                implementation("coil.kt")
                implementation("coil.kt.compose")

                implementation("androidx.hilt.navigation.compose")
                implementation("androidx.lifecycle.runtimeCompose")
                implementation("androidx.lifecycle.viewModelCompose")

                implementation("kotlinx.coroutines.android")
            }
        }
    }
}
