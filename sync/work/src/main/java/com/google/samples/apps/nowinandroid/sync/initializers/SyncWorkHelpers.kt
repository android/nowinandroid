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

package com.google.samples.apps.nowinandroid.sync.initializers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import com.google.samples.apps.nowinandroid.sync.R

private const val SyncNotificationId = 0
private const val SyncNotificationChannelID = "SyncNotificationChannel"

// All sync work needs an internet connectionS
val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

/**
 * Foreground information for sync on lower API levels when sync workers are being
 * run with a foreground service
 */
fun Context.syncForegroundInfo() = ForegroundInfo(
    SyncNotificationId,
    syncWorkNotification(),
)

/**
 * Notification displayed on lower API levels when sync workers are being
 * run with a foreground service
 */
private fun Context.syncWorkNotification(): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            SyncNotificationChannelID,
            getString(R.string.sync_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = getString(R.string.sync_notification_channel_description)
        }
        // Register the channel with the system
        val notificationManager: NotificationManager? =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        notificationManager?.createNotificationChannel(channel)
    }

    return NotificationCompat.Builder(
        this,
        SyncNotificationChannelID,
    )
        .setSmallIcon(
            com.google.samples.apps.nowinandroid.core.common.R.drawable.ic_nia_notification,
        )
        .setContentTitle(getString(R.string.sync_notification_title))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}
