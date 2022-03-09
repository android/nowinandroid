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

package com.google.samples.apps.nowinandroid.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry

class NiaPreferences @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    suspend fun setFollowedTopicIds(followedTopicIds: Set<Int>) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.followedTopicIds.clear()
                    this.followedTopicIds.addAll(followedTopicIds)
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun toggleFollowedTopicId(followedTopicId: Int, followed: Boolean) {
        try {
            userPreferences.updateData {
                it.copy {
                    val current =
                        if (followed) {
                            followedTopicIds + followedTopicId
                        } else {
                            followedTopicIds - followedTopicId
                        }
                    this.followedTopicIds.clear()
                    this.followedTopicIds.addAll(current)
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    val followedTopicIds: Flow<Set<Int>> = userPreferences.data
        .retry {
            Log.e("NiaPreferences", "Failed to read user preferences", it)
            true
        }
        .map { it.followedTopicIdsList.toSet() }

    suspend fun hasRunFirstTimeSync() = userPreferences.data
        .map { it.hasRunFirstTimeSync }.firstOrNull() ?: false

    suspend fun markFirstTimeSyncDone() {
        try {
            userPreferences.updateData {
                it.copy {
                    hasRunFirstTimeSync = true
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }
}
