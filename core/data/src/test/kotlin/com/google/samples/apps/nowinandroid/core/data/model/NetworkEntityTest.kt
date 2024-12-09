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

package com.google.samples.apps.nowinandroid.core.data.model

import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import com.google.samples.apps.nowinandroid.core.network.model.asExternalModel
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class NetworkEntityTest {

    @Test
    fun networkTopicMapsToDatabaseModel() {
        val networkModel = NetworkTopic(
            id = "0",
            name = "Test",
            shortDescription = "short description",
            longDescription = "long description",
            url = "URL",
            imageUrl = "image URL",
        )
        val entity = networkModel.asEntity()

        assertEquals("0", entity.id)
        assertEquals("Test", entity.name)
        assertEquals("short description", entity.shortDescription)
        assertEquals("long description", entity.longDescription)
        assertEquals("URL", entity.url)
        assertEquals("image URL", entity.imageUrl)
    }

    @Test
    fun networkNewsResourceMapsToDatabaseModel() {
        val networkModel =
            NetworkNewsResource(
                id = "0",
                title = "title",
                content = "content",
                url = "url",
                headerImageUrl = "headerImageUrl",
                publishDate = Instant.fromEpochMilliseconds(1),
                type = "Article 📚",
            )
        val entity = networkModel.asEntity()

        assertEquals("0", entity.id)
        assertEquals("title", entity.title)
        assertEquals("content", entity.content)
        assertEquals("url", entity.url)
        assertEquals("headerImageUrl", entity.headerImageUrl)
        assertEquals(Instant.fromEpochMilliseconds(1), entity.publishDate)
        assertEquals("Article 📚", entity.type)
    }

    @Test
    fun networkTopicMapsToExternalModel() {
        val networkTopic = NetworkTopic(
            id = "0",
            name = "Test",
            shortDescription = "short description",
            longDescription = "long description",
            url = "URL",
            imageUrl = "imageUrl",
        )

        val expected = Topic(
            id = "0",
            name = "Test",
            shortDescription = "short description",
            longDescription = "long description",
            url = "URL",
            imageUrl = "imageUrl",
        )

        assertEquals(expected, networkTopic.asExternalModel())
    }

    @Test
    fun networkNewsResourceMapsToExternalModel() {
        val networkNewsResource = NetworkNewsResource(
            id = "0",
            title = "title",
            content = "content",
            url = "url",
            headerImageUrl = "headerImageUrl",
            publishDate = Instant.fromEpochMilliseconds(1),
            type = "Article 📚",
            topics = listOf("1", "2"),
        )

        val networkTopics = listOf(
            NetworkTopic(
                id = "1",
                name = "Test 1",
                shortDescription = "short description 1",
                longDescription = "long description 1",
                url = "url 1",
                imageUrl = "imageUrl 1",
            ),
            NetworkTopic(
                id = "2",
                name = "Test 2",
                shortDescription = "short description 2",
                longDescription = "long description 2",
                url = "url 2",
                imageUrl = "imageUrl 2",
            ),
        )

        val expected = NewsResource(
            id = "0",
            title = "title",
            content = "content",
            url = "url",
            headerImageUrl = "headerImageUrl",
            publishDate = Instant.fromEpochMilliseconds(1),
            type = "Article 📚",
            topics = networkTopics.map(NetworkTopic::asExternalModel),
        )
        assertEquals(expected, networkNewsResource.asExternalModel(networkTopics))
    }
}
