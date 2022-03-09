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

package com.google.samples.apps.nowinandroid.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType
import kotlinx.datetime.Instant

/**
 * Defines an NiA news resource.
 * It is the child in a 1 to many relationship with [EpisodeEntity]
 */
@Entity(
    tableName = "news_resources",
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["episode_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class NewsResourceEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "episode_id")
    val episodeId: Int,
    val title: String,
    val content: String,
    val url: String,
    @ColumnInfo(name = "header_image_url")
    val headerImageUrl: String?,
    @ColumnInfo(name = "publish_date")
    val publishDate: Instant,
    val type: NewsResourceType,
)

fun NewsResourceEntity.asExternalModel() = NewsResource(
    id = id,
    episodeId = episodeId,
    title = title,
    content = content,
    url = url,
    headerImageUrl = headerImageUrl,
    publishDate = publishDate,
    type = type,
    authors = listOf(),
    topics = listOf()
)

/**
 * A shell [EpisodeEntity] to fulfill the foreign key constraint when inserting
 * a [NewsResourceEntity] into the DB
 */
fun NewsResourceEntity.episodeEntityShell() = EpisodeEntity(
    id = episodeId,
    name = "",
    publishDate = Instant.fromEpochMilliseconds(0),
    alternateVideo = null,
    alternateAudio = null,
)
