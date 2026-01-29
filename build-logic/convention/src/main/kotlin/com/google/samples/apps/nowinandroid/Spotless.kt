/*
 * Copyright 2026 The Android Open Source Project
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

internal fun Project.configureSpotlessForAndroid() {
    configureSpotlessCommon()
    extensions.configure<SpotlessExtension> {
        format("xml") {
            target("src/**/*.xml")
            // Look for the first XML tag that isn't a comment (<!--) or the xml declaration (<?xml)
            licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "(<[^!?])")
        }
    }
}

internal fun Project.configureSpotlessForJvm() {
    configureSpotlessCommon()
}

private fun Project.configureSpotlessCommon() {
    apply(plugin = "com.diffplug.spotless")
    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            ktlint(libs.findVersion("ktlint").get().requiredVersion).editorConfigOverride(
                mapOf("android" to "true")
            )
            licenseHeaderFile(rootDir.resolve("spotless/copyright.kt"))
        }
        format("kts") {
            target("*.kts")
            // Look for the first line that doesn't have a block comment (assumed to be the license)
            licenseHeaderFile(rootDir.resolve("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
        }
    }
}