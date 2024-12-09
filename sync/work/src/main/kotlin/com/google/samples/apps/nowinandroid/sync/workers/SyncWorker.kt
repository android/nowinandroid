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

package com.google.samples.apps.nowinandroid.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.tracing.traceAsync
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.google.samples.apps.nowinandroid.core.analytics.AnalyticsHelper
import com.google.samples.apps.nowinandroid.core.data.Synchronizer
import com.google.samples.apps.nowinandroid.core.data.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.SearchContentsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.datastore.ChangeListVersions
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferencesDataSource
import com.google.samples.apps.nowinandroid.core.network.Dispatcher
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers.IO
import com.google.samples.apps.nowinandroid.sync.initializers.SyncConstraints
import com.google.samples.apps.nowinandroid.sync.initializers.syncForegroundInfo
import com.google.samples.apps.nowinandroid.sync.status.SyncSubscriber
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Syncs the data layer by delegating to the appropriate repository instances with
 * sync functionality.
 */
@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val niaPreferences: NiaPreferencesDataSource,
    private val topicRepository: TopicsRepository,
    private val newsRepository: NewsRepository,
    private val searchContentsRepository: SearchContentsRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val analyticsHelper: AnalyticsHelper,
    private val syncSubscriber: SyncSubscriber,
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        traceAsync("Sync", 0) {
            analyticsHelper.logSyncStarted()

            syncSubscriber.subscribe()

            // First sync the repositories in parallel
            val syncedSuccessfully = awaitAll(
                async { topicRepository.sync() },
                async { newsRepository.sync() },
            ).all { it }

            analyticsHelper.logSyncFinished(syncedSuccessfully)

            if (syncedSuccessfully) {
                searchContentsRepository.populateFtsData()
                Result.success()
            } else {
                Result.retry()
            }
        }
    }

    override suspend fun getChangeListVersions(): ChangeListVersions =
        niaPreferences.getChangeListVersions()

    override suspend fun updateChangeListVersions(
        update: ChangeListVersions.() -> ChangeListVersions,
    ) = niaPreferences.updateChangeListVersion(update)

    companion object {
        /**
         * Expedited one time work to sync data on app startup
         */
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}
