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

package com.google.samples.apps.nowinandroid.di

import com.google.samples.apps.nowinandroid.feature.bookmarks.BookmarksViewModel
import com.google.samples.apps.nowinandroid.feature.foryou.ForYouViewModel
import com.google.samples.apps.nowinandroid.feature.interests.InterestsViewModel
import com.google.samples.apps.nowinandroid.feature.search.SearchViewModel
import com.google.samples.apps.nowinandroid.feature.settings.SettingsViewModel
import com.google.samples.apps.nowinandroid.feature.topic.TopicViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureModules = module {
    // Feature ViewModels
    viewModelOf(::ForYouViewModel)
    viewModelOf(::BookmarksViewModel)
    viewModelOf(::InterestsViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel { (topicId: String) -> TopicViewModel(get(), get(), get(), topicId) }
}