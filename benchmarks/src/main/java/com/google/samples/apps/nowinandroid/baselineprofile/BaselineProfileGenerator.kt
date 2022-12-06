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

package com.google.samples.apps.nowinandroid.baselineprofile

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.google.samples.apps.nowinandroid.PACKAGE_NAME
import com.google.samples.apps.nowinandroid.bookmarks.goToBookmarksScreen
import com.google.samples.apps.nowinandroid.foryou.forYouScrollFeedDownUp
import com.google.samples.apps.nowinandroid.foryou.forYouSelectTopics
import com.google.samples.apps.nowinandroid.foryou.forYouWaitForContent
import com.google.samples.apps.nowinandroid.interests.goToInterestsScreen
import com.google.samples.apps.nowinandroid.interests.interestsScrollTopicsDownUp
import org.junit.Rule
import org.junit.Test

/**
 * Generates a baseline profile which can be copied to `app/src/main/baseline-prof.txt`.
 */
@ExperimentalBaselineProfilesApi
class BaselineProfileGenerator {
    @get:Rule val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() =
        baselineProfileRule.collectBaselineProfile(PACKAGE_NAME) {
            // This block defines the app's critical user journey. Here we are interested in
            // optimizing for app startup. But you can also navigate and scroll
            // through your most important UI.

            pressHome()
            startActivityAndWait()

            // Scroll the feed critical user journey
            forYouWaitForContent()
            forYouSelectTopics(true)
            forYouScrollFeedDownUp()

            // Navigate to saved screen
            goToBookmarksScreen()
            // TODO: we need to implement adding stuff to bookmarks before able to scroll it
            // bookmarksScrollFeedDownUp()

            // Navigate to interests screen
            goToInterestsScreen()
            interestsScrollTopicsDownUp()
        }
}
