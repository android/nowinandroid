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

package com.google.samples.apps.nowinandroid.core.data.di

import com.google.samples.apps.nowinandroid.core.data.util.ConnectivityManagerNetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneBroadcastMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val networkMonitorModule = module {
    singleOf(::ConnectivityManagerNetworkMonitor) bind NetworkMonitor::class
}

internal actual val timeZoneMonitorModule = module {
    singleOf(::TimeZoneBroadcastMonitor) bind TimeZoneMonitor::class
}
