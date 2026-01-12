/*
 * Copyright 2026 The Android Open Source Project
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
    alias(libs.plugins.nowinandroid.android.library.compose)
//    alias(libs.plugins.nowinandroid.hilt)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.core.ads_api"
}

dependencies {
    implementation(libs.appodeal) {
        // Appodeal "networks" package
        exclude(group = "com.appodeal.ads.sdk.networks", module = "inmobi")

        // Extra InMobi adapters that the full SDK pulls in
        exclude(group = "com.applovin.mediation", module = "inmobi-adapter")
        exclude(group = "com.unity3d.ads-mediation", module = "inmobi-adapter")
        exclude(group = "org.bidon", module = "inmobi-adapter")
    }
}
