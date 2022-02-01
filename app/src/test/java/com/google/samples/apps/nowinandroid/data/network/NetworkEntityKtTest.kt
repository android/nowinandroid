/*
 * Copyright 2021 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.data.network

import com.google.samples.apps.nowinandroid.data.model.NewsResourceType
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkEntityKtTest {

    @Test
    fun network_author_can_be_mapped_to_author_entity() {
        val networkModel = NetworkAuthor(
            id = 0,
            name = "Test",
            imageUrl = "something"
        )
        val entity = networkModel.asEntity()

        assertEquals(0, entity.id)
        assertEquals("Test", entity.name)
        assertEquals("something", entity.imageUrl)
    }

    @Test
    fun network_topic_can_be_mapped_to_topic_entity() {
        val networkModel = NetworkTopic(
            id = 0,
            name = "Test",
            description = "something"
        )
        val entity = networkModel.asEntity()

        assertEquals(0, entity.id)
        assertEquals("Test", entity.name)
        assertEquals("something", entity.description)
    }

    @Test
    fun network_news_resource_can_be_mapped_to_news_resource_entity() {
        val networkModel = NetworkNewsResource(
            id = 0,
            episodeId = 2,
            title = "title",
            content = "content",
            url = "url",
            publishDate = Instant.fromEpochMilliseconds(1),
            type = NewsResourceType.Article.displayText,
        )
        val entity = networkModel.asEntity()

        assertEquals(0, entity.id)
        assertEquals(2, entity.episodeId)
        assertEquals("title", entity.title)
        assertEquals("content", entity.content)
        assertEquals("url", entity.url)
        assertEquals(Instant.fromEpochMilliseconds(1), entity.publishDate)
        assertEquals(NewsResourceType.Article.displayText, entity.type)

        val expandedNetworkModel = NetworkNewsResourceExpanded(
            id = 0,
            episodeId = 2,
            title = "title",
            content = "content",
            url = "url",
            publishDate = Instant.fromEpochMilliseconds(1),
            type = NewsResourceType.Article.displayText,
        )

        val entityFromExpanded = expandedNetworkModel.asEntity()

        assertEquals(0, entityFromExpanded.id)
        assertEquals(2, entityFromExpanded.episodeId)
        assertEquals("title", entityFromExpanded.title)
        assertEquals("content", entityFromExpanded.content)
        assertEquals("url", entityFromExpanded.url)
        assertEquals(Instant.fromEpochMilliseconds(1), entityFromExpanded.publishDate)
        assertEquals(NewsResourceType.Article.displayText, entityFromExpanded.type)
    }

    @Test
    fun network_episode_can_be_mapped_to_episode_entity() {
        val networkModel = NetworkEpisode(
            id = 0,
            name = "name",
            publishDate = Instant.fromEpochMilliseconds(1),
            alternateVideo = "alternateVideo",
            alternateAudio = "alternateAudio",
        )
        val entity = networkModel.asEntity()

        assertEquals(0, entity.id)
        assertEquals("name", entity.name)
        assertEquals("alternateVideo", entity.alternateVideo)
        assertEquals("alternateAudio", entity.alternateAudio)
        assertEquals(Instant.fromEpochMilliseconds(1), entity.publishDate)

        val expandedNetworkModel = NetworkEpisodeExpanded(
            id = 0,
            name = "name",
            publishDate = Instant.fromEpochMilliseconds(1),
            alternateVideo = "alternateVideo",
            alternateAudio = "alternateAudio",
        )

        val entityFromExpanded = expandedNetworkModel.asEntity()

        assertEquals(0, entityFromExpanded.id)
        assertEquals("name", entityFromExpanded.name)
        assertEquals("alternateVideo", entityFromExpanded.alternateVideo)
        assertEquals("alternateAudio", entityFromExpanded.alternateAudio)
        assertEquals(Instant.fromEpochMilliseconds(1), entityFromExpanded.publishDate)
    }
}
