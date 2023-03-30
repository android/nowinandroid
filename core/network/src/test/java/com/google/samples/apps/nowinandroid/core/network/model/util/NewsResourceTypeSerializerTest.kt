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

package com.google.samples.apps.nowinandroid.core.network.model.util

import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class NewsResourceTypeSerializerTest {

    @Test
    fun test_news_resource_serializer_video() {
        assertEquals(
            NewsResourceType.Video,
            Json.decodeFromString(NewsResourceTypeSerializer, """"Video 📺""""),
        )
    }

    @Test
    fun test_news_resource_serializer_article() {
        assertEquals(
            NewsResourceType.Article,
            Json.decodeFromString(NewsResourceTypeSerializer, """"Article 📚""""),
        )
    }

    @Test
    fun test_news_resource_serializer_api_change() {
        assertEquals(
            NewsResourceType.APIChange,
            Json.decodeFromString(NewsResourceTypeSerializer, """"API change""""),
        )
    }

    @Test
    fun test_news_resource_serializer_codelab() {
        assertEquals(
            NewsResourceType.Codelab,
            Json.decodeFromString(NewsResourceTypeSerializer, """"Codelab""""),
        )
    }

    @Test
    fun test_news_resource_serializer_podcast() {
        assertEquals(
            NewsResourceType.Podcast,
            Json.decodeFromString(NewsResourceTypeSerializer, """"Podcast 🎙""""),
        )
    }

    @Test
    fun test_news_resource_serializer_docs() {
        assertEquals(
            NewsResourceType.Docs,
            Json.decodeFromString(NewsResourceTypeSerializer, """"Docs 📑""""),
        )
    }

    @Test
    fun test_news_resource_serializer_event() {
        assertEquals(
            NewsResourceType.Event,
            Json.decodeFromString(NewsResourceTypeSerializer, """"Event 📆""""),
        )
    }

    @Test
    fun test_news_resource_serializer_dac() {
        assertEquals(
            NewsResourceType.DAC,
            Json.decodeFromString(NewsResourceTypeSerializer, """"DAC""""),
        )
    }

    @Test
    fun test_news_resource_serializer_unknown() {
        assertEquals(
            NewsResourceType.Unknown,
            Json.decodeFromString(NewsResourceTypeSerializer, """"umm""""),
        )
    }

    @Test
    fun test_serialize_and_deserialize() {
        val json = Json.encodeToString(NewsResourceTypeSerializer, NewsResourceType.Video)
        assertEquals(
            NewsResourceType.Video,
            Json.decodeFromString(NewsResourceTypeSerializer, json),
        )
    }
}
