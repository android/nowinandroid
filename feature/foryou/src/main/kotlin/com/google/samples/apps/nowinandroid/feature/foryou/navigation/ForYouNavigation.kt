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

package com.google.samples.apps.nowinandroid.feature.foryou.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.google.samples.apps.nowinandroid.core.notifications.DEEP_LINK_URI_PATTERN
import com.google.samples.apps.nowinandroid.feature.foryou.ForYouScreen
import kotlinx.serialization.Serializable

@Serializable data object ForYouRoute // route to ForYou screen

@Serializable data object ForYouBaseRoute // route to base navigation graph


/**
 *  The ForYou section of the app. It can also display information about topics.
 *  This should be supplied from a separate module.
 *
 *  @param onTopicClick - Called when a topic is clicked, contains the ID of the topic
 */
fun EntryProviderBuilder<Any>.forYouSection(
    onTopicClick: (String) -> Unit
) {
    entry<ForYouRoute>(
        /*deepLinks = listOf(
            navDeepLink {
                *//**
                 * This destination has a deep link that enables a specific news resource to be
                 * opened from a notification (@see SystemTrayNotifier for more). The news resource
                 * ID is sent in the URI rather than being modelled in the route type because it's
                 * transient data (stored in SavedStateHandle) that is cleared after the user has
                 * opened the news resource.
                 *//*
                uriPattern = DEEP_LINK_URI_PATTERN
            },
        ),*/
    ) {
        ForYouScreen(onTopicClick)
    }
}
