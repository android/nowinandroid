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

package com.google.samples.apps.nowinandroid.feature.foryou

import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource

/**
 * Types of items that can show up in the "For you" grid
 */
sealed class ForYouItem {
    data class OnBoarding(
        val onboardingUiState: OnboardingUiState
    ) : ForYouItem()

    sealed class News : ForYouItem() {
        object Loading : News()
        data class Loaded(
            val userNewsResource: UserNewsResource
        ) : News()
    }
}

val ForYouItem.key: String
    get() = when (val item = this) {
        is ForYouItem.News -> when (item) {
            is ForYouItem.News.Loading -> LOADING_KEY
            is ForYouItem.News.Loaded -> item.userNewsResource.id
        }
        is ForYouItem.OnBoarding -> ONBOARDING_KEY
    }

val ForYouItem.contentType: String
    get() = when (val item = this) {
        is ForYouItem.News -> when (item) {
            is ForYouItem.News.Loading -> "news-loading-item"
            is ForYouItem.News.Loaded -> "news-loaded-item"
        }
        is ForYouItem.OnBoarding -> "onboarding-item"
    }

private const val LOADING_KEY = "loading"
private const val ONBOARDING_KEY = "onboarding"
