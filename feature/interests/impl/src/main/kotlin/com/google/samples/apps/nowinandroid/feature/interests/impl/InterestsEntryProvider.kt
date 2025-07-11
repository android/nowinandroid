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

package com.google.samples.apps.nowinandroid.feature.interests.impl

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStack
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStackKey
import com.google.samples.apps.nowinandroid.feature.interests.api.navigation.InterestsRoute
import com.google.samples.apps.nowinandroid.feature.topic.api.TopicDetailPlaceholder
import com.google.samples.apps.nowinandroid.feature.topic.api.navigation.navigateToTopic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
object InterestsModule {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Provides
    @IntoSet
    fun provideInterestsEntryProviderBuilder(
        backStack: NiaBackStack
    ): EntryProviderBuilder<NiaBackStackKey>.() -> Unit = {
        entry<InterestsRoute>(
            metadata = ListDetailSceneStrategy.listPane {
                TopicDetailPlaceholder()
            }
        ) { key ->
//            InterestsListDetailScreen()
            InterestsScreen(
                onTopicClick = backStack::navigateToTopic,
                shouldHighlightSelectedTopic = false,
            )
        }
    }
}