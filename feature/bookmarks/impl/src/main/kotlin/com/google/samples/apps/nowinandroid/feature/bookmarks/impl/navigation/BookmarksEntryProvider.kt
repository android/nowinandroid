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

package com.google.samples.apps.nowinandroid.feature.bookmarks.impl.navigation

import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStack
import com.google.samples.apps.nowinandroid.core.navigation.NiaBackStackKey
import com.google.samples.apps.nowinandroid.feature.bookmarks.api.navigation.BookmarksRoute
import com.google.samples.apps.nowinandroid.feature.bookmarks.impl.BookmarksScreen
import com.google.samples.apps.nowinandroid.feature.topic.api.navigation.navigateToTopic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
object BookmarksModule {

    @Provides
    @IntoSet
    fun provideBookmarksEntryProviderBuilder(
        backStack: NiaBackStack,
    ): EntryProviderBuilder<NiaBackStackKey>.() -> Unit = {
        entry<BookmarksRoute> {
            val snackbarHostState = LocalSnackbarHostState.current
            BookmarksScreen(
                onTopicClick = backStack::navigateToTopic,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = Short,
                    ) == ActionPerformed
                }
            )
        }
    }
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState state should be initialized at runtime")
}
