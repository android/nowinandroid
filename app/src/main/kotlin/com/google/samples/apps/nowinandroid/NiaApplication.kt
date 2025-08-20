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

package com.google.samples.apps.nowinandroid

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.samples.apps.nowinandroid.core.analytics.analyticsModule
import com.google.samples.apps.nowinandroid.core.data.di.dataModule
import com.google.samples.apps.nowinandroid.core.database.di.databaseModule
import com.google.samples.apps.nowinandroid.core.database.di.daosModule
import com.google.samples.apps.nowinandroid.core.datastore.di.dataStoreModule
import com.google.samples.apps.nowinandroid.core.domain.di.domainModule
import com.google.samples.apps.nowinandroid.core.network.di.networkModule
import com.google.samples.apps.nowinandroid.core.network.di.flavoredNetworkModule
import com.google.samples.apps.nowinandroid.core.network.di.dispatchersModule
import com.google.samples.apps.nowinandroid.core.network.di.coroutineScopesModule
import com.google.samples.apps.nowinandroid.core.notifications.notificationsModule
import com.google.samples.apps.nowinandroid.di.appModule
import com.google.samples.apps.nowinandroid.di.featureModules
import com.google.samples.apps.nowinandroid.sync.di.syncModule
import com.google.samples.apps.nowinandroid.sync.initializers.Sync
import com.google.samples.apps.nowinandroid.util.ProfileVerifierLogger
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * [Application] class for NiA
 */
class NiaApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        startKoin {
            androidLogger()
            androidContext(this@NiaApplication)
            modules(
                appModule, 
                featureModules,
                analyticsModule,
                dataModule, 
                databaseModule, 
                daosModule, 
                dataStoreModule, 
                domainModule, 
                networkModule, 
                flavoredNetworkModule, 
                dispatchersModule,
                coroutineScopesModule,
                notificationsModule, 
                syncModule
            )
        }

        setStrictModePolicy()

        // Initialize Sync; the system responsible for keeping data in the app up to date.
        Sync.initialize(context = this)
        
        // Initialize ProfileVerifierLogger after Koin is set up
        val profileVerifierLogger: ProfileVerifierLogger by inject()
        profileVerifierLogger()
    }

    override fun newImageLoader(): ImageLoader {
        val imageLoader: ImageLoader by inject()
        return imageLoader
    }

    /**
     * Return true if the application is debuggable.
     */
    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    /**
     * Set a thread policy that detects all potential problems on the main thread, such as network
     * and disk access.
     *
     * If a problem is found, the offending call will be logged and the application will be killed.
     */
    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            StrictMode.setThreadPolicy(
                Builder().detectAll().penaltyLog().build(),
            )
        }
    }
}
