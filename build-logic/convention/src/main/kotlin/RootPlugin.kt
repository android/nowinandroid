/*
 * Copyright 2025 The Android Open Source Project
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

import com.google.samples.apps.nowinandroid.configureGraphTasks
import com.google.samples.apps.nowinandroid.configureSpotlessForRootProject
import com.google.samples.apps.nowinandroid.isIsolatedProjectsEnabled
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.configuration.BuildFeatures
import javax.inject.Inject

abstract class RootPlugin : Plugin<Project> {
    @get:Inject abstract val buildFeatures: BuildFeatures

    override fun apply(target: Project) {
        require(target.path == ":")
        if (!buildFeatures.isIsolatedProjectsEnabled()) {
            target.subprojects { configureGraphTasks() }
            target.configureSpotlessForRootProject()
        }
    }
}
