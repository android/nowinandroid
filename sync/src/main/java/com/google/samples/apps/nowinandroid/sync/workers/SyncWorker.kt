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
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.google.samples.apps.nowinandroid.core.domain.repository.NewsRepository
import com.google.samples.apps.nowinandroid.core.domain.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.sync.SyncRepository
import com.google.samples.apps.nowinandroid.sync.initializers.SyncConstraints
import com.google.samples.apps.nowinandroid.sync.initializers.syncForegroundInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * Syncs the data layer by delegating to the appropriate repository instances with
 * sync functionality.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: SyncRepository,
    private val topicRepository: TopicsRepository,
    private val newsRepository: NewsRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result =
        // First sync the repositories
        when (topicRepository.sync() && newsRepository.sync()) {
            // Sync ran successfully, notify the SyncRepository that sync has been run
            true -> {
                syncRepository.notifyFirstTimeSyncRun()
                Result.success()
            }
            false -> Result.retry()
        }

    companion object {
        private const val SyncInterval = 1L
        private val SyncIntervalTimeUnit = TimeUnit.DAYS

        /**
         * Expedited one time work to sync data as quickly as possible because the app has
         * either launched for the first time, or hasn't had the opportunity to sync at all
         */
        fun firstTimeSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()

        /**
         * Periodic sync work to routinely keep the app up to date
         */
        fun periodicSyncWork() = PeriodicWorkRequestBuilder<DelegatingWorker>(
            SyncInterval,
            SyncIntervalTimeUnit
        )
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}
