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

package com.google.samples.apps.nowinandroid.core.network.di

import com.google.samples.apps.nowinandroid.core.network.BuildKonfig
import com.google.samples.apps.nowinandroid.core.network.retrofit.RetrofitNiaNetwork
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.CallConverterFactory
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ksp.generated.module

internal val jsonModule = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }
}

internal val ktorfitModule = module {
    single<Ktorfit> {
        ktorfit {
            baseUrl(BuildKonfig.BACKEND_URL)
            httpClient(
                HttpClient {
                    install(ContentNegotiation) {
                        get<Json>()
                    }
                },
            )
            converterFactories(
                FlowConverterFactory(),
                CallConverterFactory(),
            )
        }
    }

    singleOf(::RetrofitNiaNetwork)
}

fun networkModule() = listOf(NetworkModule().module, jsonModule, ktorfitModule)

@Module
@ComponentScan
class NetworkModule
