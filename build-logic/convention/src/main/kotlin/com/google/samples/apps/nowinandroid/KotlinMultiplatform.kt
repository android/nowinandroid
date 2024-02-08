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
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.konan.target.HostManager

internal fun Project.configureKotlinMultiplatform() {
    extensions.configure<KotlinMultiplatformExtension> {
        jvm()

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

        // tier 2
        linuxArm64()
        watchosSimulatorArm64()
        watchosX64()
        watchosArm32()
        watchosArm64()
        tvosSimulatorArm64()
        tvosX64()
        tvosArm64()
        iosArm64()

        // tier 3
        androidNativeArm32()
        androidNativeArm64()
        androidNativeX86()
        androidNativeX64()
        mingwX64()
        watchosDeviceArm64()

        // linking fails for the linux test build if not built on a linux host
        // ensure the tests and linking for them is only done on linux hosts
        project.tasks.named("linuxX64Test") { enabled = HostManager.hostIsLinux }
        project.tasks.named("linkDebugTestLinuxX64") { enabled = HostManager.hostIsLinux }

        project.tasks.named("mingwX64Test") { enabled = HostManager.hostIsMingw }
        project.tasks.named("linkDebugTestMingwX64") { enabled = HostManager.hostIsMingw }
    }
}
