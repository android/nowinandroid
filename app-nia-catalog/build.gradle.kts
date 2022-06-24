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
    id("nowinandroid.android.application")
    id("nowinandroid.android.application.compose")
    id("nowinandroid.spotless")
}

android {
    defaultConfig {
        applicationId = "com.google.samples.apps.niacatalog"

        // The UI catalog does not depend on content from the app, however, it depends on modules
        // which do, so we must specify a default value for the contentType dimension.
        missingDimensionStrategy("contentType", "demo")
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":core-designsystem"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.flowlayout)
}