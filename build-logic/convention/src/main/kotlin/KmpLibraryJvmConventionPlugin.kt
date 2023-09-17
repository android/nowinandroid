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
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

@OptIn(ExperimentalKotlinGradlePluginApi::class)
class KmpLibraryJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlin-multiplatform").get().get().pluginId)
                apply(libs.findPlugin("kotlin-serialization").get().get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
//                applyDefaultHierarchyTemplate()
                targetHierarchy.default()
                jvmToolchain(11)

                jvm()
                iosX64()
                iosArm64()
                iosSimulatorArm64()

                (this as org.gradle.api.plugins.ExtensionAware).extensions
                    .configure<NamedDomainObjectContainer<KotlinSourceSet>>("sourceSets") {
                        all {
                            languageSettings.optIn("kotlinx.cinterop.BetaInteropApi")
                            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
                            languageSettings.optIn("kotlin.experimental.ExperimentalNativeApi")
                            languageSettings.optIn("kotlin.native.runtime.NativeRuntimeApi")
                        }
                        val commonMain by getting {
                            dependencies {
                            }
                        }
                        val commonTest by getting {
                            dependencies {
                                implementation(kotlin("test-common"))
                                implementation(kotlin("test-annotations-common"))
                                implementation(kotlin("test-junit"))
                            }
                        }

                        val jvmMain by getting {
                            dependencies {
                            }
                        }
                        val jvmTest by getting {
                            dependencies {
                                implementation(libs.findLibrary("junit4").get().get())
                            }
                        }

                        val iosMain by getting {
                            dependencies {
                            }
                        }
                        val iosTest by getting {
                            dependencies {
                            }
                        }
                    }
            }
        }
    }
}
