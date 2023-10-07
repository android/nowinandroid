/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

private val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.findLibrary(alias: String): Provider<MinimalExternalModuleDependency> {
    return libs.findLibrary(alias).get()
}

internal fun Project.implementation(alias: String) {
    dependencies.add("implementation", findLibrary(alias))
}

internal fun Project.projectImplementation(path: String) {
    dependencies.add("implementation", project(path))
}

internal fun Project.platformImplementation(alias: String) {
    dependencies {
        dependencies.add("implementation", platform(findLibrary(alias)))
    }
}

internal fun Project.testImplementation(alias: String) {
    dependencies.add("testImplementation", findLibrary(alias))
}

internal fun Project.androidTestImplementation(alias: String) {
    dependencies.add("androidTestImplementation", findLibrary(alias))
}

internal fun Project.debugImplementation(alias: String) {
    dependencies.add("debugImplementation", findLibrary(alias))
}

internal fun Project.platformAndroidTestImplementation(alias: String) {
    dependencies {
        dependencies.add("androidTestImplementation", platform(findLibrary(alias)))
    }
}

internal fun Project.projectAndroidTestImplementation(alias: String) {
    dependencies {
        dependencies.add("androidTestImplementation", project(alias))
    }
}

internal fun Project.projectTestImplementation(alias: String) {
    dependencies.add("testImplementation", project(alias))
}

internal fun Project.version(alias: String): String {
    return libs.findVersion(alias).get().toString()
}
