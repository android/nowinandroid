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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.android.lint)
}

group = "com.google.samples.apps.nowinandroid.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    implementation(libs.truth)
    lintChecks(libs.androidx.lint.gradle)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplicationJacoco") {
            id = "nowinandroid.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }
        register("androidLibraryJacoco") {
            id = "nowinandroid.android.library.jacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }
        register("androidTest") {
            id = "nowinandroid.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidLint") {
            id = "nowinandroid.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("jvmLibrary") {
            id = "nowinandroid.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("kmpLibrary") {
            id = "nowinandroid.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kotlinInject") {
            id = "nowinandroid.kmp.inject"
            implementationClass = "KotlinInjectConventionPlugin"
        }
        register("sqldelight") {
            id = "nowinandroid.sqldelight"
            implementationClass = "SqlDelightConventionPlugin"
        }
        register("cmpFeature") {
            id = "nowinandroid.cmp.feature"
            implementationClass = "CmpFeatureConventionPlugin"
        }
        register("cmpApplication") {
            id = "nowinandroid.cmp.application"
            implementationClass = "CmpApplicationConventionPlugin"
        }
        register("koin") {
            id = "nowinandroid.di.koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("root") {
            id = "nowinandroid.root"
            implementationClass = "RootConventionPlugin"
        }
    }
}
