/*
 * Copyright 2026 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.feature.foryou.impl

import com.google.samples.apps.nowinandroid.core.model.data.UserNewsResource
import com.google.samples.apps.nowinandroid.core.ui.ForYouFeedItemUi
import javax.inject.Inject

/**
 * Maps domain news items into UI feed items with optional banner insertion.
 *
 * Responsibilities:
 * - decide where banner/preload items appear
 * - do NOT know anything about Android / Compose / Ads SDK
 */
interface ForYouFeedItemsMapper {

    fun map(
        news: List<UserNewsResource>,
        showBanner: Boolean,
    ): List<ForYouFeedItemUi>

    /**
     * Default implementation.
     */
    class Default @Inject constructor (
        private val bannerFrequency: Int = 3,
        private val includePreload: Boolean = true,
    ) : ForYouFeedItemsMapper {

        override fun map(
            news: List<UserNewsResource>,
            showBanner: Boolean,
        ): List<ForYouFeedItemUi> = buildList {

            // 1. Preload item (0dp host) — lives outside Lazy content logic
            if (includePreload) {
                add(ForYouFeedItemUi.BannerPreload)
            }

            // 2. News + banner insertion rule
            news.forEachIndexed { index, item ->
                add(ForYouFeedItemUi.News(item))

                val isBannerPosition = (index + 1) % bannerFrequency == 0
                if (showBanner && isBannerPosition) {
                    add(ForYouFeedItemUi.Banner)
                }
            }
        }
    }
}
