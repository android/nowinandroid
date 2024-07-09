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
    alias(libs.plugins.nowinandroid.cmp.feature)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}
android {
    namespace = "com.google.samples.apps.nowinandroid.feature.interests"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.domain)
            implementation(projects.core.ui)
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        commonMain.dependencies {
            implementation(projects.core.testing)
        }
        androidUnitTest.dependencies {
            implementation(libs.robolectric)
            implementation(libs.roborazzi)
            implementation(projects.core.screenshotTesting)
        }
        androidInstrumentedTest.dependencies {
            implementation(projects.core.testing)
            implementation(libs.bundles.androidx.compose.ui.test)
        }
    }
}
