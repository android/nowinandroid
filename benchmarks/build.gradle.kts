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
import com.google.samples.apps.nowinandroid.NiaBuildType
import com.google.samples.apps.nowinandroid.configureFlavors

plugins {
    id("nowinandroid.android.test")
    alias(libs.plugins.baselineProfile)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.benchmarks"

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APP_BUILD_TYPE_SUFFIX", "\"\"")

        /**
         * ERROR: Running on Emulator
         *     Benchmark is running on an emulator, which is not representative of
         *     real user devices. Use a physical device to benchmark. Emulator
         *     benchmark improvements might not carry over to a real user's
         *     experience (or even regress real device performance).
         *
         * While you can suppress these errors (turning them into warnings)
         * PLEASE NOTE THAT EACH SUPPRESSED ERROR COMPROMISES ACCURACY
         */
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
        // testInstrumentationRunnerArguments["androidx.benchmark.enabledRules"] = "BaselineProfile"
    }

    buildFeatures {
        buildConfig = true
    }

    // Use the same flavor dimensions as the application to allow generating Baseline Profiles on prod,
    // which is more close to what will be shipped to users (no fake data), but has ability to run the
    // benchmarks on demo, so we benchmark on stable data. 
    configureFlavors(this) { flavor ->
        buildConfigField(
            "String",
            "APP_FLAVOR_SUFFIX",
            "\"${flavor.applicationIdSuffix ?: ""}\""
        )
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.uiautomator)
}

baselineProfile {
    managedDevices += "pixel6api31aosp"
    // managedDevices += "pixel4api30aospatd"
    useConnectedDevices = false
    @Suppress("UnstableApiUsage")
    enableEmulatorDisplay = true
}
