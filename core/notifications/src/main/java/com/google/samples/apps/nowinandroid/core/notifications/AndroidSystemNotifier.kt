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

package com.google.samples.apps.nowinandroid.core.notifications

import android.Manifest.permission
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.InboxStyle
import androidx.core.app.NotificationManagerCompat
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val NEWS_NOTIFICATION_SUMMARY_ID = 1
private const val NEWS_NOTIFICATION_CHANNEL_ID = ""
private const val NEWS_NOTIFICATION_GROUP = "NEWS_NOTIFICATIONS"

/**
 * Implementation of [Notifier] that displays notifications in the system tray.
 */
@Singleton
class AndroidSystemNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) : Notifier {

    override fun onNewsAdded(
        newsResources: List<NewsResource>,
    ) = with(context) {
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val newsNotifications = newsResources.map { newsResource ->
            newsNotification {
                setSmallIcon(
                    com.google.samples.apps.nowinandroid.core.common.R.drawable.ic_nia_notification,
                )
                    .setContentTitle(newsResource.title)
                    .setContentText(newsResource.content)
                    .setGroup(NEWS_NOTIFICATION_GROUP)
            }
        }
        val summaryNotification = newsNotification {
            val title = getString(
                R.string.news_notification_group_summary,
                newsNotifications.size,
            )
            setContentTitle(title)
                .setContentText(title)
                .setSmallIcon(
                    com.google.samples.apps.nowinandroid.core.common.R.drawable.ic_nia_notification,
                )
                // Build summary info into InboxStyle template.
                .setStyle(newsInboxStyle(newsResources, title))
                .setGroup(NEWS_NOTIFICATION_GROUP)
                .setGroupSummary(true)
                .build()
        }

        with(NotificationManagerCompat.from(this)) {
            newsNotifications.forEachIndexed { index, notification ->
                notify(newsResources[index].id.hashCode(), notification)
            }
            notify(NEWS_NOTIFICATION_SUMMARY_ID, summaryNotification)
        }
    }

    /**
     * Creates an inbox style summary notification for news updates
     */
    private fun newsInboxStyle(
        newsResources: List<NewsResource>,
        title: String,
    ): InboxStyle = newsResources
        // Show at most 5 lines
        .take(5)
        .fold(InboxStyle()) { inboxStyle, newsResource ->
            inboxStyle.addLine(newsResource.title)
        }
        .setBigContentTitle(title)
        .setSummaryText(title)
}

/**
 * Creates a notification for configured for news updates
 */
private fun Context.newsNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    ensureNotificationChannel()
    return NotificationCompat.Builder(
        this,
        NEWS_NOTIFICATION_CHANNEL_ID,
    )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .apply(block)
        .build()
}

/**
 * Ensures the a notification channel is is present if applicable
 */
private fun Context.ensureNotificationChannel() {
    if (VERSION.SDK_INT < VERSION_CODES.O) return

    val channel = NotificationChannel(
        NEWS_NOTIFICATION_CHANNEL_ID,
        getString(R.string.news_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = getString(R.string.news_notification_channel_description)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}
