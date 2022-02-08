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

package com.google.samples.apps.nowinandroid.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.google.samples.apps.nowinandroid.data.local.entities.AuthorEntity
import com.google.samples.apps.nowinandroid.data.local.entities.EpisodeEntity
import com.google.samples.apps.nowinandroid.data.local.entities.NewsResourceAuthorCrossRef
import com.google.samples.apps.nowinandroid.data.local.entities.NewsResourceEntity
import com.google.samples.apps.nowinandroid.data.local.entities.NewsResourceTopicCrossRef
import com.google.samples.apps.nowinandroid.data.local.entities.TopicEntity

/**
 * External data layer representation of a fully populated NiA news resource
 */
data class NewsResource(
    @Embedded
    val entity: NewsResourceEntity,
    @Relation(
        parentColumn = "episode_id",
        entityColumn = "id"
    )
    val episode: EpisodeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NewsResourceAuthorCrossRef::class,
            parentColumn = "news_resource_id",
            entityColumn = "author_id",
        )
    )
    val authors: List<AuthorEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NewsResourceTopicCrossRef::class,
            parentColumn = "news_resource_id",
            entityColumn = "topic_id",
        )
    )
    val topics: List<TopicEntity>
)
