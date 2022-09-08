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

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.google.samples.apps.nowinandroid.core.model.data.Episode

/**
 * External data layer representation of an NiA episode
 */
data class PopulatedEpisode(
    @Embedded
    val entity: EpisodeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "episode_id"
    )
    val newsResources: List<NewsResourceEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = EpisodeAuthorCrossRef::class,
            parentColumn = "episode_id",
            entityColumn = "author_id",
        )
    )
    val authors: List<AuthorEntity>
)

fun PopulatedEpisode.asExternalModel() = Episode(
    id = entity.id,
    name = entity.name,
    publishDate = entity.publishDate,
    alternateVideo = entity.alternateVideo,
    alternateAudio = entity.alternateAudio,
    newsResources = newsResources.map(NewsResourceEntity::asExternalModel),
    authors = authors.map(AuthorEntity::asExternalModel)
)
