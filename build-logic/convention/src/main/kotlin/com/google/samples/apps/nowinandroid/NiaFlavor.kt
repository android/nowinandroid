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

package com.google.samples.apps.nowinandroid

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType,
}

// The content for the app can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
@Suppress("EnumEntryName")
enum class NiaFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),
    prod(FlavorDimension.contentType),
}

fun <E : CommonExtension<*, *, *, F, *, *>, F : ProductFlavor> configureFlavors(
    commonExtension: E,
    flavorConfigurationBlock: ProductFlavor.(flavor: NiaFlavor) -> Unit = {},
) {
    when (commonExtension) {
        is ApplicationExtension -> commonExtension.configureFlavors { nia ->
            flavorConfigurationBlock(this, nia)
            this.applicationIdSuffix = nia.applicationIdSuffix
        }

        else -> commonExtension.configureFlavors(flavorConfigurationBlock)
    }
}

@JvmName("configureFlavorsImpl")
private inline fun <E : CommonExtension<*, *, *, F, *, *>, F : ProductFlavor> E.configureFlavors(
    crossinline flavorConfigurationBlock: F.(flavor: NiaFlavor) -> Unit = {},
) {
    apply {
        FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            NiaFlavor.values().forEach { niaFlavor ->
                register(niaFlavor.name) {
                    dimension = niaFlavor.dimension.name
                    flavorConfigurationBlock(this, niaFlavor)
                }
            }
        }
    }
}
