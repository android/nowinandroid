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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.google.samples.apps.nowinandroid.configureBadgingTasks
import com.google.samples.apps.nowinandroid.configureGradleManagedDevices
import com.google.samples.apps.nowinandroid.configureKotlinAndroid
import com.google.samples.apps.nowinandroid.configurePrintApksTask
import com.google.samples.apps.nowinandroid.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// Convention plugin for the Compose Multiplatform feature module
class CmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
//                apply("com.dropbox.dependency-guard")
            }
            configureComposeMultiplatformApp()
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
                configureBadgingTasks(this)
            }

            dependencies {
                "commonMainImplementation"(project(":core:ui"))
                "commonMainImplementation"(project(":core:designsystem"))
                "commonMainImplementation"(libs.findLibrary("jetbrains.compose.viewmodel").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains.compose.navigation").get())
                "commonMainImplementation"(libs.findLibrary("koin.compose").get())
                "commonMainImplementation"(libs.findLibrary("koin.compose.viewmodel").get())
                "commonMainImplementation"(libs.findLibrary("koin.compose.viewmodel.navigation").get())

                "androidMainImplementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "androidMainImplementation"(libs.findLibrary("androidx.tracing.ktx").get())
            }

        }
    }
}

private fun Project.configureComposeMultiplatformApp() {
    extensions.configure<KotlinMultiplatformExtension> {
        // Enable native group by default
        // https://kotlinlang.org/docs/whatsnew1820.html#new-approach-to-source-set-hierarchy
        applyDefaultHierarchyTemplate()

        // Configure JVM target for Android
        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }

        // Add JVM target for desktop
        jvm {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }

        // Configure iOS targets
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64(),
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
            }
        }

        // Other targets
        macosX64()
        macosArm64()

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
