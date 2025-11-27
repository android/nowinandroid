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

package com.google.samples.apps.nowinandroid.feature.search.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavKey
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigator
import com.google.samples.apps.nowinandroid.feature.interests.api.navigation.InterestsRoute
import com.google.samples.apps.nowinandroid.feature.search.api.navigation.SearchRoute
import com.google.samples.apps.nowinandroid.feature.search.impl.SearchScreen
import com.google.samples.apps.nowinandroid.feature.topic.api.navigation.navigateToTopic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
object SearchEntryProvider {

    @Provides
    @IntoSet
    fun provideSearchEntryProviderBuilder(
        navigator: NiaNavigator,
    ): EntryProviderScope<NiaNavKey>.() -> Unit = {
        entry<SearchRoute> { key ->
            SearchScreen(
                onBackClick = navigator::pop,
                onInterestsClick = { navigator.navigate(InterestsRoute()) },
                onTopicClick = navigator::navigateToTopic,
            )
        }
    }
}
