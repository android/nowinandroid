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

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStack
import com.google.samples.apps.nowinandroid.feature.interests.api.navigation.navigateToInterests
import com.google.samples.apps.nowinandroid.feature.search.api.navigation.SearchRouteNav3
import com.google.samples.apps.nowinandroid.feature.search.impl.SearchRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
object SearchModule {

    @Provides
    @IntoSet
    fun provideSearchEntryProviderBuilder(
        backStack: NiaBackStack,
    ): EntryProviderBuilder<Any>.() -> @JvmSuppressWildcards Unit = {
        entry<SearchRouteNav3> { key ->
            SearchRoute(
                onBackClick = backStack::removeLast,
                onInterestsClick = key.onInterestsClick,
                onTopicClick = backStack::navigateToInterests,
            )
        }
    }
}