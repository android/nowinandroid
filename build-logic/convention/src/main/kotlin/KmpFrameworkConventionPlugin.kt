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
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask

@OptIn(ExperimentalKotlinGradlePluginApi::class)
class KmpFrameworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlin-native-cocoapods").get().get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                (this as org.gradle.api.plugins.ExtensionAware).extensions.configure<CocoapodsExtension> {
                    version = findProperty("VERSION_NAME").toString()
                    summary = "Kotlin Multiplatform framework"
                    homepage = "https://github.com/xiaobailong24/nowinkotlin"
                    authors = "xiaobailong24"
                    license = "Apache"
                    ios.deploymentTarget = "11.0"

                    framework {
                        baseName = project.name
                        isStatic = true
                        transitiveExport = false

                        freeCompilerArgs = freeCompilerArgs + listOf(
                            "-module-name=KMP",
                            "-Xruntime-logs=gc=info",
                        )
                    }
                }

                val buildTypeMode = "debug"
                tasks.create("iosFatFramework", FatFrameworkTask::class) {
                    // The fat framework must have the same base name as the initial frameworks.
                    baseName = project.name

                    // The default destination directory is '<build directory>/fat-framework'.
                    destinationDir = layout.buildDirectory.asFile.get()
                        .resolve("fat-framework/${buildTypeMode.lowercase()}")
                    // Specify the frameworks to be merged.
                    from(
                        iosX64().binaries.getFramework("pod", buildTypeMode),
//                iosArm64().binaries.getFramework("pod", buildTypeMode),
                        iosSimulatorArm64().binaries.getFramework("pod", buildTypeMode),
                    )
                }
            }

        }
    }
}
