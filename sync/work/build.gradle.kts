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
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.nowinandroid.di.koin)
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
    }
    namespace = "com.google.samples.apps.nowinandroid.sync"
}

kotlin {
    sourceSets{
        commonMain.dependencies {
            implementation(projects.core.analytics)
            implementation(projects.core.data)
            implementation(projects.core.notifications)
        }
        commonTest.dependencies {
            implementation(projects.core.testing)
        }
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.tracing.ktx)
            implementation(libs.androidx.work.ktx)
        }
        androidInstrumentedTest.dependencies {
            implementation(projects.core.testing)
            implementation(libs.androidx.test.core)
            implementation(libs.androidx.test.ext)
            implementation(libs.androidx.test.junit)
            implementation(libs.androidx.test.runner)
            implementation(libs.androidx.work.testing)
            implementation(libs.kotlinx.coroutines.guava)
        }
    }
}

//    prodImplementation(libs.firebase.cloud.messaging)
//    prodImplementation(platform(libs.firebase.bom))
