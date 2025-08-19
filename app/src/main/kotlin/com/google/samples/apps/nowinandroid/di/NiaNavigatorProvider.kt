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

package com.google.samples.apps.nowinandroid.di

import androidx.navigation3.runtime.EntryProviderScope
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigator
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavKey
import com.google.samples.apps.nowinandroid.core.navigation.NiaNavigatorState
import com.google.samples.apps.nowinandroid.navigation.TopLevelDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NiaNavigatorProvider {
    @Provides
    @Singleton
    fun providerNiaNavigatorState(): NiaNavigatorState =
        NiaNavigatorState(
            startKey = TopLevelDestination.FOR_YOU.key,
        )
//
//    @Provides
//    @Singleton
//    fun provideNiaNavigator(
//        state: NiaNavigatorState
//    ): NiaNavigator =
//        NiaNavigator(state)



    /**
     * Registers feature modules' polymorphic serializers to support
     * feature keys' save and restore by savedstate
     * in [com.google.samples.apps.nowinandroid.core.navigation.NiaBackStackViewModel].
     */
    @Provides
    @Singleton
    fun provideSerializersModule(
        polymorphicModuleBuilders: Set<@JvmSuppressWildcards PolymorphicModuleBuilder<NiaNavKey>.() -> Unit>,
    ): SerializersModule = SerializersModule {
        polymorphic(NiaNavKey::class) {
            polymorphicModuleBuilders.forEach { it() }
        }
    }
}
