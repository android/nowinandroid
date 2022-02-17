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

/**
 * Cross reference for many to many relationship between [NewsResourceEntity] and [AuthorEntity]
 */
@Entity(
    tableName = "news_resources_authors",
    primaryKeys = ["news_resource_id", "author_id"],
    foreignKeys = [
        ForeignKey(
            entity = NewsResourceEntity::class,
            parentColumns = ["id"],
            childColumns = ["news_resource_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AuthorEntity::class,
            parentColumns = ["id"],
            childColumns = ["author_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class NewsResourceAuthorCrossRef(
    @ColumnInfo(name = "news_resource_id")
    val newsResourceId: Int,
    @ColumnInfo(name = "author_id")
    val authorId: Long,
)
