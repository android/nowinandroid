/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *  
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import org.gradle.api.Plugin
import org.gradle.api.Project

class GitHooksConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register("setupGitHooks") {
                val prePushHook = file(".git/hooks/pre-push")
                val commitMsgHook = file(".git/hooks/commit-msg")
                val hooksInstalled = commitMsgHook.exists()
                    && prePushHook.exists()
                    && prePushHook.readBytes().contentEquals(file("tools/pre-push").readBytes())

                if (!hooksInstalled) {
                    exec {
                        commandLine("tools/setup.sh")
                        workingDir = rootProject.projectDir
                    }
                }
            }
        }
    }
}
