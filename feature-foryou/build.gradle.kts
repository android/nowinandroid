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
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("nowinandroid.spotless")
}

android {
    defaultConfig {
        testInstrumentationRunner =
            "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
    }
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-data"))
    implementation(project(":core-navigation"))

    testImplementation(project(":core-testing"))
    androidTestImplementation(project(":core-testing"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    implementation(libs.accompanist.flowlayout)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // TODO : Remove this dependency once we upgrade to Android Studio Dolphin b/228889042
    // These dependencies are currently necessary to render Compose previews
    debugImplementation(libs.androidx.customview.poolingcontainer)

    // androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
    configurations.configureEach {
        resolutionStrategy {
            force(libs.junit4)
            // Temporary workaround for https://issuetracker.google.com/174733673
            force("org.objenesis:objenesis:2.6")
        }
    }
}
