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

import org.gradle.api.model.ObjectFactory;
import org.gradle.declarative.dsl.model.annotations.Restricted;

import javax.inject.Inject;

@Restricted
public abstract class ConventionalAndroidHiltJacocoLibrary implements AndroidLibrary {
    private final KSPAndroidLibraryDependencies dependencies;
    private final AndroidTargets targets;

    @Inject
    public ConventionalAndroidHiltJacocoLibrary(AndroidTargets targets, ObjectFactory objectFactory) {
        this.targets = targets;
        this.dependencies = objectFactory.newInstance(KSPAndroidLibraryDependencies.class);
    }

    /**
     * Static targets extension block.
     */
    @Override
    public AndroidTargets getTargets() {
        return targets;
    }

    /**
     * Common dependencies for all targets.
     */
    @Override
    public KSPAndroidLibraryDependencies getDependencies() {
        return dependencies;
    }
}
