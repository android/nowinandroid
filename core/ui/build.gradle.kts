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
    alias(libs.plugins.nowinandroid.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.google.samples.apps.nowinandroid.core.ui"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.metrics)
            implementation(libs.androidx.browser)
        }
        commonMain.dependencies {
            api(projects.core.analytics)
            api(projects.core.designsystem)
            api(projects.core.model)
            implementation(libs.coil)
            implementation(libs.coil.compose)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        androidInstrumentedTest.dependencies {
            implementation(projects.core.testing)
        }
    }
}
