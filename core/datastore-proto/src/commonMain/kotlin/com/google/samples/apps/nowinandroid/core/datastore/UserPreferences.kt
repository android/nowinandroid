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

package com.google.samples.apps.nowinandroid.core.datastore

import com.google.samples.apps.nowinandroid.core.datastore.DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
import com.google.samples.apps.nowinandroid.core.datastore.ThemeBrandProto.THEME_BRAND_UNSPECIFIED
import kotlinx.serialization.Serializable

// A lot of workaround brought by Proto
@Serializable
data class UserPreferences(
    val topicChangeListVersion: Int,
    val authorChangeListVersion: Int,
    val newsResourceChangeListVersion: Int,
    val hasDoneIntToStringIdMigration: Boolean,
    val hasDoneListToMapMigration: Boolean,
    val followedTopicIds: Set<String> = emptySet(),
    val followedAuthorIds: Set<String> = emptySet(),
    val bookmarkedNewsResourceIds: Set<String> = emptySet(),
    val viewedNewsResourceIds: Set<String> = emptySet(),
    val themeBrand: ThemeBrandProto,
    val darkThemeConfig: DarkThemeConfigProto,
    val shouldHideOnboarding: Boolean,
    val useDynamicColor: Boolean,
) {
    companion object {
        val DEFAULT = UserPreferences(
            topicChangeListVersion = 0,
            authorChangeListVersion = 0,
            newsResourceChangeListVersion = 0,
            hasDoneIntToStringIdMigration = false,
            hasDoneListToMapMigration = false,
            themeBrand = THEME_BRAND_UNSPECIFIED,
            darkThemeConfig = DARK_THEME_CONFIG_FOLLOW_SYSTEM,
            shouldHideOnboarding = false,
            useDynamicColor = false,
        )
    }
}
