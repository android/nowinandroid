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

package org.gradle.api.experimental.android

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class AndroidHiltPlugin : Plugin<Project> {
    @Suppress("UnstableApiUsage")
    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("org.gradle.experimental.android-library")
            apply("com.google.devtools.ksp")
            apply("dagger.hilt.android.plugin")
        }

        val hilt = createDslModel(project)
        project.configurations.getByName("ksp").dependencies.addAllLater(hilt.getDependencies().getKsp().getDependencies())

        project.dependencies.add("implementation", "com.google.dagger:hilt-android:2.50")
    }

    private fun createDslModel(project: Project): HiltExtension {
        // Must name extension hilt2, otherwise:
        //        An exception occurred applying plugin request [id: 'org.gradle.experimental.hilt']
        //        > Failed to apply plugin 'org.gradle.experimental.hilt'.
        //          > Cannot add extension with name 'hilt', as there is an extension already registered with that name.
        val hilt = project.getExtensions().create("hilt2", HiltExtension::class.java)

        return hilt
    }
}