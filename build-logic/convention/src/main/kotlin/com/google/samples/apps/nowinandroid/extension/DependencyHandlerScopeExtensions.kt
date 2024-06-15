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

package com.google.samples.apps.nowinandroid.extension

import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.implementation(dependency: Any) =
    dependencies.add("implementation", dependency)

internal fun DependencyHandlerScope.testImplementation(dependency: Any) =
    dependencies.add("testImplementation", dependency)

internal fun DependencyHandlerScope.androidTestImplementation(dependency: Any) =
    dependencies.add("androidTestImplementation", dependency)

internal fun DependencyHandlerScope.debugImplementation(dependency: Any) =
    dependencies.add("debugImplementation", dependency)

internal fun DependencyHandlerScope.ksp(dependency: Any) =
    dependencies.add("ksp", dependency)
