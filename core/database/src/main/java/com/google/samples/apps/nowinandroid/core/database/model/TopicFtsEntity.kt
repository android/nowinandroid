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

package com.google.samples.apps.nowinandroid.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

/**
 * Fts entity for the topic. See https://developer.android.com/reference/androidx/room/Fts4.
 */
@Entity(tableName = "topicsFts")
@Fts4
data class TopicFtsEntity(

    @ColumnInfo(name = "topicId")
    val topicId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "shortDescription")
    val shortDescription: String,

    @ColumnInfo(name = "longDescription")
    val longDescription: String,
)

fun TopicEntity.asFtsEntity() = TopicFtsEntity(
    topicId = id,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
)
