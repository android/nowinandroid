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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.google.samples.apps.nowinandroid.configureBadgingTasks
import com.google.samples.apps.nowinandroid.configureGradleManagedDevices
import com.google.samples.apps.nowinandroid.configureKotlinAndroid
import com.google.samples.apps.nowinandroid.configurePrintApksTask
import com.google.samples.apps.nowinandroid.configureSpotlessForAndroid
import com.google.samples.apps.nowinandroid.isIsolatedProjectsEnabled
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.configuration.BuildFeatures
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import javax.inject.Inject

abstract class AndroidApplicationConventionPlugin : Plugin<Project> {
    @get:Inject abstract val buildFeatures: BuildFeatures
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "nowinandroid.android.lint")
            apply(plugin = "com.dropbox.dependency-guard")

            // Apply Google's OSS Licenses plugin only on CI to avoid breaking configuration cache on local builds
            // https://github.com/google/play-services-plugins/issues/246
            if (providers.environmentVariable("CI").isPresent) apply(plugin = "com.google.android.gms.oss-licenses-plugin")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
                configureBadgingTasks(this)
            }
            if (!buildFeatures.isIsolatedProjectsEnabled()) {
                configureSpotlessForAndroid()
            }
        }
    }
}
