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

import com.google.samples.apps.nowinandroid.configureKotlinAndroid
import org.gradle.kotlin.dsl.support.delegates.ProjectDelegate

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    defaultConfig {
        testInstrumentationRunner =
            "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
    }
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-data"))
    implementation(project(":core-common"))
    implementation(project(":core-navigation"))

    testImplementation(project(":core-testing"))
    androidTestImplementation(project(":core-testing"))

    add("implementation", libs.findLibrary("coil.kt").get())
    add("implementation", libs.findLibrary("coil.kt.compose").get())

    add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
    add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

    add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())

    add("implementation", libs.findLibrary("hilt.android").get())
    add("kapt", libs.findLibrary("hilt.compiler").get())

    // TODO : Remove this dependency once we upgrade to Android Studio Dolphin b/228889042
    // These dependencies are currently necessary to render Compose previews
    add("debugImplementation", libs.findLibrary("androidx.customview.poolingcontainer").get())
}
