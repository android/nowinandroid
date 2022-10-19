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

package com.google.samples.apps.nowinandroid.core.ui

import androidx.annotation.StringDef
import androidx.compose.ui.tooling.preview.Preview

/*
 * TODO: Remove this file and instead use androidx.compose.ui.tooling.preview once this bug is
 *  fixed - https://issuetracker.google.com/issues/254528382
*/
/**
 * List with the pre-defined devices available to be used in the preview.
 */
object Devices {

    // Reference devices
    const val PHONE = "spec:shape=Normal,width=411,height=891,unit=dp,dpi=420"
    const val FOLDABLE = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=420"
    const val TABLET = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=420"
    const val DESKTOP = "spec:shape=Normal,width=1920,height=1080,unit=dp,dpi=420"
}

/**
 * Annotation for defining the [Preview] device to use.
 * @suppress
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    open = true,
    value = [
        Devices.PHONE,
        Devices.FOLDABLE,
        Devices.TABLET,
        Devices.DESKTOP,
    ]
)
annotation class Device
