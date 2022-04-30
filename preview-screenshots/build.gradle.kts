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

import com.google.samples.apps.nowinandroid.Flavor
import com.google.samples.apps.nowinandroid.FlavorDimension

// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("nowinandroid.android.library")
    id("nowinandroid.android.library.compose")
    id("nowinandroid.android.library.jacoco")
    id("nowinandroid.spotless")
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

androidComponents {
    // Disable release builds for this test-only library, no need to run screenshot tests more than
    // once
    beforeVariants(selector().withBuildType("release")) { builder ->
        builder.enable = false
    }
    // Disable prod builds for this test-only library, no need to run screenshot tests more than
    // once
    beforeVariants(
        selector().withFlavor(FlavorDimension.contentType.name to Flavor.prod.name)
    ) { builder ->
        builder.enable = false
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":feature-author"))
    implementation(project(":feature-foryou"))
    implementation(project(":feature-interests"))
    implementation(project(":feature-topic"))

    implementation(libs.showkase.runtime)
    ksp(libs.showkase.processor)

    testImplementation(libs.junit4)
    testImplementation(libs.testParameterInjector)
}

tasks.named("check") {
    dependsOn("verifyPaparazziDemoDebug")
}

tasks.withType<Test>().configureEach {
    // Increase memory for Paparazzi tests
    maxHeapSize = "2g"
}
