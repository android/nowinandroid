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
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.nowinandroid.kmp.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.core.designsystem"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    // Serve sources to debug inside browser
                    static(project.projectDir.path)
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.compose.ui.test)
            implementation(projects.core.testing)
        }
        androidUnitTest.dependencies {
            implementation(libs.androidx.compose.ui.test)
            implementation(libs.androidx.compose.ui.testManifest)
            implementation(libs.robolectric)
            implementation(libs.roborazzi)
            implementation(projects.core.screenshotTesting)
            implementation(projects.core.testing)
        }
        commonMain.dependencies {
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.material3.adaptiveNavigationSuite)
            implementation(libs.jetbrains.compose.material.iconsExtended)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.ui.util)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.uiToolingPreview)
            implementation(libs.coil.compose)
            implementation(libs.jetbrains.compose.material3.adaptive)
            implementation(libs.jetbrains.compose.material3.adaptive.layout)
        }
    }
}

dependencies {
    lintPublish(projects.lint)
}
