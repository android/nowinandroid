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
    alias(libs.plugins.nowinandroid.android.library)
    alias(libs.plugins.nowinandroid.android.library.jacoco)
    alias(libs.plugins.nowinandroid.android.hilt)
    alias(libs.plugins.protobuf)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "com.google.samples.apps.nowinandroid.core.datastore"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents.beforeVariants {
    android.sourceSets.register(it.name) {
        val buildDir = layout.buildDirectory.get().asFile
        java.srcDir(buildDir.resolve("generated/source/proto/${it.name}/java"))
        kotlin.srcDir(buildDir.resolve("generated/source/proto/${it.name}/kotlin"))
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.androidx.dataStore.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.protobuf.kotlin.lite)

    testImplementation(projects.core.datastoreTest)
    testImplementation(projects.core.testing)
}
