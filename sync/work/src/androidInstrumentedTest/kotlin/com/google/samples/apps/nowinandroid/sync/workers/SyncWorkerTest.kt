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

import androidx.work.NetworkType
import androidx.work.OutOfQuotaPolicy
import org.junit.Test
import kotlin.test.assertEquals

class SyncWorkerTest {
    @Test
    fun startUpSyncWork_buildsExpectedRequest() {
        val request = SyncWorker.startUpSyncWork()

        assertEquals(DelegatingWorker::class.qualifiedName, request.workSpec.workerClassName)
        assertEquals(SyncWorker::class.delegatedData(), request.workSpec.input)
        assertEquals(NetworkType.CONNECTED, request.workSpec.constraints.requiredNetworkType)
        assertEquals(true, request.workSpec.expedited)
        assertEquals(
            OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST,
            request.workSpec.outOfQuotaPolicy,
        )
    }
}
