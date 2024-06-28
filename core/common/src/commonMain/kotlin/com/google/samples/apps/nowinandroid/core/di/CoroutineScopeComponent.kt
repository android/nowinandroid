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

package com.google.samples.apps.nowinandroid.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope

/**
 * The application-level scope. There will only be one instance of anything annotated with this.
 */
@Scope
annotation class ApplicationScope

@Component
abstract class CoroutineScopeComponent(
    @Component val dispatchersComponent: DispatchersComponent
) {
    @DefaultDispatcher abstract val defaultDispatcher: CoroutineDispatcher

    @Provides
    fun providesCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + defaultDispatcher)
}
