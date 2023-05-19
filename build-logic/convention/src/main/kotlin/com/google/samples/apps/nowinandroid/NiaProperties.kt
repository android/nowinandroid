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
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import kotlin.reflect.KProperty

internal fun Project.nia() = NiaProperties(this)

internal class NiaProperties private constructor(project: Project) {
    private val catalog by lazy(project.rootProject::getVersionsCatalog)
    val libraries by lazy { NiaLibraries(catalog) }
    val versions by lazy { NiaVersions(catalog) }

    companion object {
        private const val EXT_KEY = "com.google.samples.apps.nowinandroid.NiaProperties"
        operator fun invoke(project: Project) = project.getOrCreateExtra(EXT_KEY, ::NiaProperties)
    }
}

internal class NiaVersions(catalog: VersionCatalog) {
    val androidxComposeCompiler by catalog
    val jacoco by catalog

    private operator fun VersionCatalog.getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): VersionConstraint = findVersion(property.name).orElseThrow {
        IllegalStateException("Missing catalog version '${property.name}'")
    }
}

@Suppress("PropertyName")
internal class NiaLibraries(catalog: VersionCatalog) {
    val `android-desugarJdkLibs` by catalog
    val `androidx-compose-bom` by catalog
    val `androidx-hilt-navigation-compose` by catalog
    val `androidx-lifecycle-runtimeCompose` by catalog
    val `androidx-lifecycle-viewModelCompose` by catalog
    val `coil-kt-compose` by catalog
    val `coil-kt` by catalog
    val `firebase-analytics` by catalog
    val `firebase-bom` by catalog
    val `firebase-crashlytics` by catalog
    val `firebase-performance` by catalog
    val `hilt-android` by catalog
    val `hilt-compiler` by catalog
    val `kotlinx-coroutines-android` by catalog
    val `room-compiler` by catalog
    val `room-ktx` by catalog
    val `room-runtime` by catalog
    val junit4 by catalog

    private operator fun VersionCatalog.getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Provider<MinimalExternalModuleDependency> = findLibrary(property.name).orElseThrow {
        IllegalStateException("Missing catalog library '${property.name}'")
    }
}

private fun Project.getVersionsCatalog(): VersionCatalog = runCatching {
    project.extensions.getByType<VersionCatalogsExtension>().named("libs")
}.recoverCatching {
    throw IllegalStateException("No versions catalog found!", it)
}.getOrThrow()

private fun <T> Project.getOrCreateExtra(
    key: String,
    create: (Project) -> T,
): T = extensions.extraProperties.run {
    @Suppress("UNCHECKED_CAST")
    (if (has(key)) get(key) as? T else null) ?: create(project).also { set(key, it) }
}
