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

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
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

        jvm {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
        androidTarget()
// SqlDelight does not support wasm yet
// https://github.com/cashapp/sqldelight/pull/4965/files

//        wasmJs {
//            browser {
//                commonWebpackConfig {
//                    devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                        static = (static ?: mutableListOf()).apply {
//                            // Serve sources to debug inside browser
//                            add(project.projectDir.path)
//                        }
//                    }
//                }
//            }
//        }

        // tier 1
// :core:datastore:linuxMain: Could not resolve com.russhwolf:multiplatform-settings-no-arg:1.1.1.
// https://github.com/russhwolf/multiplatform-settings/issues/113
//        linuxX64()
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
//        project.tasks.named("linuxX64Test") { enabled = HostManager.hostIsLinux }
//        project.tasks.named("linkDebugTestLinuxX64") { enabled = HostManager.hostIsLinux }

        // Suppress 'expect'/'actual' classes are in Beta.
        targets.configureEach {
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.addAll("-Xexpect-actual-classes")
                    }
                }
            }
        }

        // Fixes Cannot locate tasks that match ':core:model:testClasses' as task 'testClasses'
        // not found in project ':core:model'. Some candidates are: 'jsTestClasses', 'jvmTestClasses'.
        project.tasks.register("testClasses") {
            dependsOn("allTests")
        }
    }

    // Set Java compilation compatibility for JVM target to match Kotlin JVM target (11).
    // Only apply --release to non-Android Java tasks, since AGP manages bootclasspath itself.
    project.tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
        // Android tasks are named like "compileDebugJavaWithJavac"; KMP JVM tasks like "compileJvmMainJava"
        if (!name.endsWith("JavaWithJavac")) {
            options.release.set(11)
        }
    }
}
