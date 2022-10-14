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
import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.google.samples.apps.nowinandroid.Flavor
import com.google.samples.apps.nowinandroid.FlavorDimension
import com.google.samples.apps.nowinandroid.configureFlavors
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
    id("nowinandroid.android.test")
}

android {
    namespace = "com.google.samples.apps.nowinandroid.benchmarking"

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It's signed with a debug key
        // for easy local/CI testing.
        val benchmark by creating {
            // Keep the build type debuggable so we can attach a debugger if needed.
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
        }
    }
    


    // Use the same flavor dimensions as the application to allow generating Baseline Profiles on prod,
    // which is more close to what will be shipped to users (no fake data), but has ability to run the
    // benchmarks on demo, so we benchmark on stable data. 
    configureFlavors(this)
    flavorDimensions += "benchmark"
    productFlavors {
        create("macro") {
            dimension = "benchmark"
            testInstrumentationRunnerArguments["androidx.benchmark.enabledRules"] = "Macrobenchmark"
        }
        create("baseline") {
            dimension = "benchmark"
            testInstrumentationRunnerArguments["androidx.benchmark.enabledRules"] = "BaselineProfile"
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    testOptions {
        managedDevices {
            devices {
                create<ManagedVirtualDevice>("AospAtd30") {
                    // Use device profiles you typically see in Android Studio.
                    device = "Pixel 2"
                    // ATDs currently support only API level 30.
                    apiLevel = 31
                    // You can also specify "google-atd" if you require Google Play Services.
                    systemImageSource = "aosp"
                }
                create<ManagedVirtualDevice>("GoogleAtd30") {
                    // Use device profiles you typically see in Android Studio.
                    device = "Pixel 2"
                    // ATDs currently support only API level 30.
                    apiLevel = 30
                    // You can also specify "google-atd" if you require Google Play Services.
                    systemImageSource = "google-atd"
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.profileinstaller)
}

androidComponents {
    beforeVariants {
        it.enable = it.buildType == "benchmark"
    }

    beforeVariants (selector()
        .withBuildType("benchmark")
        .withFlavor(Flavor.prod.dimension.name to Flavor.prod.name)
        .withFlavor("benchmark" to "baseline")) {
            val capitalName = it.name.capitalizeAsciiOnly()
            val task = tasks.register<Copy>("baselineProfileFor${capitalName}") {
                dependsOn("AospAtd30${capitalName}AndroidTest")
                from(
                    buildDir.resolve("outputs/" +
                        "managed_device_android_test_additional_output/" +
                        "flavors/" +
                        "${it.flavorName}/" +
                        "AospAtd30/" +
                        "BaselineProfileGenerator_generate-baseline-prof.txt")
                )
                destinationDir = buildDir.resolve("outputs/baselineProfile")
                rename {
                    "baseline-prof.txt"
                }
            }
            configurations {
                println("creating baselineProfile$capitalName")
                create("baselineProfile$capitalName") {
                    isCanBeConsumed = true
                    isCanBeResolved = false
                    attributes {
                        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
                        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named("baseline-profile"))
                    }
                }
            }

            artifacts {
                add("baselineProfile$capitalName", task.map {
                    it.outputs.files.singleFile.resolve("baseline-prof.txt")
                }) {
                    builtBy(task)
                }
            }

    }
}
