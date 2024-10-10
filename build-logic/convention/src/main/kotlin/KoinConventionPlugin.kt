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

import com.google.samples.apps.nowinandroid.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class KoinConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            extensions.configure(KotlinMultiplatformExtension::class.java) {
                sourceSets.named("commonMain").configure {
                    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
                }
            }

            dependencies {
                "commonMainImplementation"(platform(libs.findLibrary("koin.bom").get()))
                "commonMainImplementation"(libs.findLibrary("koin.core").get())
                "commonMainApi"(platform(libs.findLibrary("koin.annotation.bom").get()))
                "commonMainApi"(libs.findLibrary("koin.annotation").get())
                "commonTestImplementation"(libs.findLibrary("koin.test").get())
                "kspCommonMainMetadata"(libs.findLibrary("koin.ksp.compiler").get())
                "kspAndroid"(libs.findLibrary("koin.ksp.compiler").get())
                "kspIosX64"(libs.findLibrary("koin.ksp.compiler").get())
                "kspIosArm64"(libs.findLibrary("koin.ksp.compiler").get())
                "kspIosSimulatorArm64"(libs.findLibrary("koin.ksp.compiler").get())
                "kspJvm"(libs.findLibrary("koin.ksp.compiler").get())
            }

            project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
                if(name != "kspCommonMainKotlinMetadata") {
                    dependsOn("kspCommonMainKotlinMetadata")
                }
            }
        }
    }
}
