/*
 * Copyright 2024 The Android Open Source Project
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

package org.gradle.api.experimental.android;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

// TODO: Additional android configuration in AndroidLibraryConventionPlugin
// TODO: Lots of JaCoCo configuration (see AndroidLibraryJacocoConventionPlugin and Jacoco.kt)
// TODO: Apply and configure "nowinandroid.android.lint" plugin
// TODO: Add Conventional test dependencies
public abstract class ConventionalAndroidHiltJacocoPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        ConventionalAndroidHiltJacocoLibrary dslModel = createDslModel(project);

        // Setup Android Library conventions
        dslModel.getJdkVersion().convention(17);
        dslModel.getCompileSdk().convention(34);

        // Register an afterEvaluate listener before we apply the Android plugin to ensure we can
        // run actions before Android does.
        project.afterEvaluate(p -> AndroidDSLSupport.linkDslModelToPlugin(p, dslModel));

        // Apply the official Android plugin.
        project.getPlugins().apply("com.android.library");
        project.getPlugins().apply("org.jetbrains.kotlin.android");

        // ... and Hilt plugins
        project.getPlugins().apply("com.google.devtools.ksp");
        project.getPlugins().apply("dagger.hilt.android.plugin");

        // ...and the Jacoco plugin
        project.getPlugins().apply("org.gradle.jacoco");

        // Add Hilt deps
        project.getDependencies().add("ksp", "com.google.dagger:hilt-android-compiler:2.50");
        project.getDependencies().add("implementation", "com.google.dagger:hilt-android:2.50");

        linkDslModelToPluginLazy(project, dslModel);
    }

    private ConventionalAndroidHiltJacocoLibrary createDslModel(Project project) {
        AndroidTarget dslDebug = project.getObjects().newInstance(AndroidTarget.class, "debug");
        AndroidTarget dslRelease = project.getObjects().newInstance(AndroidTarget.class, "release");
        AndroidTargets dslTargets = project.getExtensions().create("targets", AndroidTargets.class, dslDebug, dslRelease);
        return project.getExtensions().create("conventionalHiltJacocoAndroidLibrary", ConventionalAndroidHiltJacocoLibrary.class, dslTargets);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void linkDslModelToPluginLazy(Project project, ConventionalAndroidHiltJacocoLibrary dslModel) {
        AndroidDSLSupport.linkDslModelToPluginLazy(project, dslModel);
        project.getConfigurations().getByName("ksp").getDependencies().addAllLater(dslModel.getDependencies().getKsp().getDependencies());
    }
}
