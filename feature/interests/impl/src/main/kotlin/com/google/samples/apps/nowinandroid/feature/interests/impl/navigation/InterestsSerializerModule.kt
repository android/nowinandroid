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

package com.google.samples.apps.nowinandroid.feature.interests.impl.navigation

import com.google.samples.apps.nowinandroid.core.navigation.NiaNavKey
import com.google.samples.apps.nowinandroid.feature.interests.api.navigation.InterestsRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.modules.PolymorphicModuleBuilder

/**
 * Provides the DSL to register the route's [kotlinx.serialization.KSerializer] as a polymorphic serializer
 *
 */
/*
@Module
@InstallIn(SingletonComponent::class)
object InterestsSerializerModule {
    @Provides
    @IntoSet
    fun provideInterestsPolymorphicModuleBuilder(): PolymorphicModuleBuilder<@JvmSuppressWildcards NiaNavKey>.() -> Unit = {
        subclass(InterestsRoute::class, InterestsRoute.serializer())
    }
}
*/
