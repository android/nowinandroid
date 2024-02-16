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

plugins {
    alias(libs.plugins.nowinandroid.kmp.library)
    alias(libs.plugins.nowinandroid.kotlin.inject)
    alias(libs.plugins.sqldelight.gradle.plugin)
}

android {
    defaultConfig {
        testInstrumentationRunner =
            "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"
    }
    namespace = "com.google.samples.apps.nowinandroid.core.database"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
            implementation(libs.kotlinx.datetime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.sqldelight.primitive.adapters)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        androidUnitTest.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
        nativeMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
        jsMain.dependencies {
            implementation(libs.sqldelight.webworker.driver)
            implementation(npm("sql.js", "1.6.2"))
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

sqldelight {
    databases {
        create("NiaDatabase") {
            packageName.set("com.google.samples.apps.nowinandroid.core.database")
            generateAsync.set(true)
            dialect("app.cash.sqldelight:sqlite-3-38-dialect:2.0.1")
        }
    }
}
