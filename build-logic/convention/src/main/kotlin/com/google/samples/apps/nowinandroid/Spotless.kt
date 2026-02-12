/*
 * Copyright 2022 The Android Open Source Project
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

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * Configures Spotless for Android/KMP modules (Kotlin + KTS + XML).
 *
 * Uses `target("src/ **")` instead of `target("** /") + targetExclude("build/")` to work around
 * [spotless#2717](https://github.com/diffplug/spotless/issues/2717).
 */
fun Project.configureSpotlessForAndroid() {
    configureSpotlessCommon()
    val rootDir = isolated.rootProject.projectDirectory
    extensions.configure<SpotlessExtension> {
        format("xml") {
            target("src/**/*.xml")
            licenseHeaderFile(rootDir.file("spotless/copyright.xml").asFile, "(<[^!?])")
        }
    }
}

/**
 * Configures Spotless for JVM-only modules (Kotlin + KTS, no XML).
 */
fun Project.configureSpotlessForJvm() {
    configureSpotlessCommon()
}

private fun Project.configureSpotlessCommon() {
    apply(plugin = "com.diffplug.spotless")
    val rootDir = isolated.rootProject.projectDirectory
    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            ktlint(libs.findVersion("ktlint").get().toString())
                .editorConfigOverride(mapOf("android" to "true"))
            licenseHeaderFile(rootDir.file("spotless/copyright.kt").asFile)
        }
        format("kts") {
            target("*.kts")
            targetExclude("**/build/**/*.kts")
            licenseHeaderFile(rootDir.file("spotless/copyright.kts").asFile, "(^(?![\\/ ]\\*).*$)")
        }
    }
}
