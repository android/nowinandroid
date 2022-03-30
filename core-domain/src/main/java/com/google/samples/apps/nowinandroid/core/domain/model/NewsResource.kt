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

package com.google.samples.apps.nowinandroid.core.domain.model

import com.google.samples.apps.nowinandroid.core.database.model.AuthorEntity
import com.google.samples.apps.nowinandroid.core.database.model.EpisodeEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceAuthorCrossRef
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceEntity
import com.google.samples.apps.nowinandroid.core.database.model.NewsResourceTopicCrossRef
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResourceExpanded

fun NetworkNewsResource.asEntity() = NewsResourceEntity(
    id = id,
    episodeId = episodeId,
    title = title,
    content = content,
    url = url,
    headerImageUrl = headerImageUrl,
    publishDate = publishDate,
    type = type,
)

fun NetworkNewsResourceExpanded.asEntity() = NewsResourceEntity(
    id = id,
    episodeId = episodeId,
    title = title,
    content = content,
    url = url,
    headerImageUrl = headerImageUrl,
    publishDate = publishDate,
    type = type,
)

/**
 * A shell [EpisodeEntity] to fulfill the foreign key constraint when inserting
 * a [NewsResourceEntity] into the DB
 */
fun NetworkNewsResource.episodeEntityShell() = EpisodeEntity(
    id = episodeId,
    name = "",
    publishDate = publishDate,
    alternateVideo = null,
    alternateAudio = null,
)

/**
 * A shell [AuthorEntity] to fulfill the foreign key constraint when inserting
 * a [NewsResourceEntity] into the DB
 */
fun NetworkNewsResource.authorEntityShells() =
    authors.map { authorId ->
        AuthorEntity(
            id = authorId,
            name = "",
            imageUrl = "",
        )
    }

/**
 * A shell [TopicEntity] to fulfill the foreign key constraint when inserting
 * a [NewsResourceEntity] into the DB
 */
fun NetworkNewsResource.topicEntityShells() =
    topics.map { topicId ->
        TopicEntity(
            id = topicId,
            name = "",
            url = "",
            imageUrl = "",
            shortDescription = "",
            longDescription = "",
        )
    }

fun NetworkNewsResource.topicCrossReferences(): List<NewsResourceTopicCrossRef> =
    topics.map { topicId ->
        NewsResourceTopicCrossRef(
            newsResourceId = id,
            topicId = topicId
        )
    }

fun NetworkNewsResource.authorCrossReferences(): List<NewsResourceAuthorCrossRef> =
    authors.map { authorId ->
        NewsResourceAuthorCrossRef(
            newsResourceId = id,
            authorId = authorId
        )
    }
