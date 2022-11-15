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

package com.google.samples.apps.nowinandroid

import com.google.samples.apps.nowinandroid.benchmarks.BuildConfig

/**
 * Convenience parameter to use proper package name with regards to build type and build flavor.
 */
val PACKAGE_NAME = StringBuilder("com.google.samples.apps.nowinandroid").apply {
    if (BuildConfig.FLAVOR != "prod") {
        append(".${BuildConfig.FLAVOR}")
    }
    if (BuildConfig.BUILD_TYPE != "release") {
        append(".${BuildConfig.BUILD_TYPE}")
    }
}.toString()
