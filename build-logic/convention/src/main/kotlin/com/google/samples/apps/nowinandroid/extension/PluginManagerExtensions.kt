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

package com.google.samples.apps.nowinandroid.extension

import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderConvertible
import org.gradle.plugin.use.PluginDependency

internal fun PluginManager.apply(pluginProvider: Provider<PluginDependency>) {
    apply(pluginProvider.get().pluginId)
}

internal fun PluginManager.apply(pluginProvider: ProviderConvertible<PluginDependency>) {
    apply(pluginProvider.asProvider())
}

internal fun PluginManager.hasPlugin(pluginProvider: Provider<PluginDependency>): Boolean {
    return hasPlugin(pluginProvider.get().pluginId)
}
