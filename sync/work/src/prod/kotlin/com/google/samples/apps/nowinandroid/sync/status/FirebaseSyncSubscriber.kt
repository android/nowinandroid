/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.sync.status

import com.google.firebase.messaging.FirebaseMessaging
import com.google.samples.apps.nowinandroid.sync.initializers.SYNC_TOPIC
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of [SyncSubscriber] that subscribes to the FCM [SYNC_TOPIC]
 */
internal class FirebaseSyncSubscriber @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
) : SyncSubscriber {
    override suspend fun subscribe() {
        firebaseMessaging
            .subscribeToTopic(SYNC_TOPIC)
            .await()
    }
}
