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

import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class PopulatedNewsResourceKtTest {
    @Test
    fun populated_news_resource_can_be_mapped_to_news_resource() {
        val populatedNewsResource = PopulatedNewsResource(
            entity = NewsResourceEntity(
                id = "1",
                episodeId = "0",
                title = "news",
                content = "Hilt",
                url = "url",
                headerImageUrl = "headerImageUrl",
                type = Video,
                publishDate = Instant.fromEpochMilliseconds(1),
            ),
            episode = EpisodeEntity(
                id = "4",
                name = "episode 4",
                publishDate = Instant.fromEpochMilliseconds(2),
                alternateAudio = "audio",
                alternateVideo = "video",
            ),
            authors = listOf(
                AuthorEntity(
                    id = "2",
                    name = "name",
                    imageUrl = "imageUrl",
                    twitter = "twitter",
                    mediumPage = "mediumPage",
                    bio = "bio",
                )
            ),
            topics = listOf(
                TopicEntity(
                    id = "3",
                    name = "name",
                    shortDescription = "short description",
                    longDescription = "long description",
                    url = "URL",
                    imageUrl = "image URL",
                )
            ),
        )
        val newsResource = populatedNewsResource.asExternalModel()

        assertEquals(
            NewsResource(
                id = "1",
                episodeId = "0",
                title = "news",
                content = "Hilt",
                url = "url",
                headerImageUrl = "headerImageUrl",
                type = Video,
                publishDate = Instant.fromEpochMilliseconds(1),
                authors = listOf(
                    Author(
                        id = "2",
                        name = "name",
                        imageUrl = "imageUrl",
                        twitter = "twitter",
                        mediumPage = "mediumPage",
                        bio = "bio",
                    )
                ),
                topics = listOf(
                    Topic(
                        id = "3",
                        name = "name",
                        shortDescription = "short description",
                        longDescription = "long description",
                        url = "URL",
                        imageUrl = "image URL",
                    )
                )
            ),
            newsResource
        )
    }
}
