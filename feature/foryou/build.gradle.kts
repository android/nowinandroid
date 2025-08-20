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

plugins {
    alias(libs.plugins.nowinandroid.android.feature)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.feature.foryou"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.notifications)

    testImplementation(libs.koin.test)
    testImplementation(libs.robolectric)
    testImplementation(projects.core.testing)
    testImplementation(project(":core:data-test"))
    testImplementation(project(":core:datastore-test"))

    testDemoImplementation(projects.core.screenshotTesting)
    testDemoImplementation(libs.koin.test)
    testDemoImplementation(libs.robolectric)
    testDemoImplementation(projects.core.testing)
    testDemoImplementation(project(":core:data-test"))

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
