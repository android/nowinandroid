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

package com.google.samples.apps.nowinandroid

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.HostManager

/**
 * A plugin that applies the Kotlin Multiplatform plugin and configures it for the project.
 * https://github.com/cashapp/sqldelight/blob/master/buildLogic/multiplatform-convention/src/main/kotlin/app/cash/sqldelight/multiplatform/MultiplatformConventions.kt
 */
internal fun Project.configureKotlinMultiplatform() {
    extensions.configure<KotlinMultiplatformExtension> {
        // Enable native group by default
        // https://kotlinlang.org/docs/whatsnew1820.html#new-approach-to-source-set-hierarchy
        applyDefaultHierarchyTemplate()

        jvm()
        androidTarget()

        js {
            browser {
                testTask {
                    useKarma {
                        useChromeHeadless()
                    }
                }
            }
            compilations.configureEach {
                kotlinOptions {
                    moduleKind = "umd"
                }
            }
        }

        // tier 1
        linuxX64()
        macosX64()
        macosArm64()
        iosSimulatorArm64()
        iosX64()

// Fix :core:database:linuxArm64Main: Could not resolve me.tatarka.inject:kotlin-inject-runtime:0.6.3.
//        // tier 2
//        linuxArm64()
//        watchosSimulatorArm64()
//        watchosX64()
//        watchosArm32()
//        watchosArm64()
//        tvosSimulatorArm64()
//        tvosX64()
//        tvosArm64()
        iosArm64()

// fix :core:model:androidNativeArm32Main: Could not resolve org.jetbrains.kotlinx:kotlinx-datetime
//        // tier 3
//        androidNativeArm32()
//        androidNativeArm64()
//        androidNativeX86()
//        androidNativeX64()
//        mingwX64()
//        watchosDeviceArm64()

        // linking fails for the linux test build if not built on a linux host
        // ensure the tests and linking for them is only done on linux hosts
        project.tasks.named("linuxX64Test") { enabled = HostManager.hostIsLinux }
        project.tasks.named("linkDebugTestLinuxX64") { enabled = HostManager.hostIsLinux }

        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf(
                    // Suppress warning:'expect'/'actual' classes (including interfaces, objects,
                    // annotations, enums, and 'actual' typealiases) are in Beta.
                    "-Xexpect-actual-classes",
                )
            }
        }

        // Fixes Cannot locate tasks that match ':core:model:testClasses' as task 'testClasses'
        // not found in project ':core:model'. Some candidates are: 'jsTestClasses', 'jvmTestClasses'.
        project.tasks.create("testClasses") {
            dependsOn("allTests")
        }
    }
}
