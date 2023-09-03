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

import com.google.samples.apps.nowinandroid.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin(
    val basePluginId: String? = null,
    val dependencyHandler: DependencyHandlerScope.(libs: VersionCatalog) -> Unit
) : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                basePluginId?.let(::apply)
                apply("com.google.devtools.ksp")
            }
            dependencies {
                "ksp"(libs.findLibrary("hilt.compiler").get())
                dependencyHandler(libs)
            }
        }
    }
}
