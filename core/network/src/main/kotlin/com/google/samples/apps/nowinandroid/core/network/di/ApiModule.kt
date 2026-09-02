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

package com.google.samples.apps.nowinandroid.core.network.di

import androidx.tracing.trace
import com.google.samples.apps.nowinandroid.core.network.api.NewsResourceApi
import com.google.samples.apps.nowinandroid.core.network.api.TopicApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    @Singleton
    internal fun providesTopicApi(retrofit: Retrofit): TopicApi {
        trace("RetrofitNiaNetwork") {
            return retrofit.create(TopicApi::class.java)
        }
    }

    @Provides
    @Singleton
    internal fun providesNewsResourceApi(retrofit: Retrofit): NewsResourceApi {
        trace("RetrofitNiaNetwork") {
            return retrofit.create(NewsResourceApi::class.java)
        }
    }
}
