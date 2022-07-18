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
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("nowinandroid.spotless")
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
    }
}

dependencies {
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-data"))
    implementation(project(":core-datastore"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)

    testImplementation(project(":core-testing"))
    androidTestImplementation(project(":core-testing"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    androidTestImplementation(libs.androidx.work.testing)

    kaptAndroidTest(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.ext.compiler)
}
