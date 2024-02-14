/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.data.repository

import com.google.samples.apps.nowinandroid.core.analytics.AnalyticsEvent
import com.google.samples.apps.nowinandroid.core.analytics.AnalyticsEvent.Param
import com.google.samples.apps.nowinandroid.core.analytics.AnalyticsHelper

internal fun AnalyticsHelper.logNewsResourceBookmarkToggled(newsResourceId: String, isBookmarked: Boolean) {
    val eventType = if (isBookmarked) "news_resource_saved" else "news_resource_unsaved"
    val paramKey = if (isBookmarked) "saved_news_resource_id" else "unsaved_news_resource_id"
    logEvent(
        AnalyticsEvent(
            type = eventType,
            extras = listOf(
                Param(key = paramKey, value = newsResourceId),
            ),
        ),
    )
}

internal fun AnalyticsHelper.logTopicFollowToggled(followedTopicId: String, isFollowed: Boolean) {
    val eventType = if (isFollowed) "topic_followed" else "topic_unfollowed"
    val paramKey = if (isFollowed) "followed_topic_id" else "unfollowed_topic_id"
    logEvent(
        AnalyticsEvent(
            type = eventType,
            extras = listOf(
                Param(key = paramKey, value = followedTopicId),
            ),
        ),
    )
}

internal fun AnalyticsHelper.logThemeChanged(themeName: String) =
    logEvent(
        AnalyticsEvent(
            type = "theme_changed",
            extras = listOf(
                Param(key = "theme_name", value = themeName),
            ),
        ),
    )

internal fun AnalyticsHelper.logDarkThemeConfigChanged(darkThemeConfigName: String) =
    logEvent(
        AnalyticsEvent(
            type = "dark_theme_config_changed",
            extras = listOf(
                Param(key = "dark_theme_config", value = darkThemeConfigName),
            ),
        ),
    )

internal fun AnalyticsHelper.logDynamicColorPreferenceChanged(useDynamicColor: Boolean) =
    logEvent(
        AnalyticsEvent(
            type = "dynamic_color_preference_changed",
            extras = listOf(
                Param(key = "dynamic_color_preference", value = useDynamicColor.toString()),
            ),
        ),
    )

internal fun AnalyticsHelper.logOnboardingStateChanged(shouldHideOnboarding: Boolean) {
    val eventType = if (shouldHideOnboarding) "onboarding_complete" else "onboarding_reset"
    logEvent(
        AnalyticsEvent(type = eventType),
    )
}
