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

package com.google.samples.apps.nowinandroid.core.database.util

import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType
import org.junit.Test
import kotlin.test.assertEquals

class NewsResourceTypeConverterTest {

    @Test
    fun test_room_news_resource_type_converter_for_video() {
        assertEquals(
            NewsResourceType.Video,
            NewsResourceTypeConverter().stringToNewsResourceType("Video 📺"),
        )
    }

    @Test
    fun test_room_news_resource_type_converter_for_article() {
        assertEquals(
            NewsResourceType.Article,
            NewsResourceTypeConverter().stringToNewsResourceType("Article 📚"),
        )
    }

    @Test
    fun test_room_news_resource_type_converter_for_api_change() {
        assertEquals(
            NewsResourceType.APIChange,
            NewsResourceTypeConverter().stringToNewsResourceType("API change"),
        )
    }

    @Test
    fun test_room_news_resource_type_converter_for_codelab() {
        assertEquals(
            NewsResourceType.Codelab,
            NewsResourceTypeConverter().stringToNewsResourceType("Codelab"),
        )
    }

    @Test
    fun test_room_news_resource_type_converter_for_podcast() {
        assertEquals(
            NewsResourceType.Podcast,
            NewsResourceTypeConverter().stringToNewsResourceType("Podcast 🎙"),
        )
    }

    @Test
    fun test_room_news_resource_type_converter_for_docs() {
        assertEquals(
            NewsResourceType.Docs,
            NewsResourceTypeConverter().stringToNewsResourceType("Docs 📑"),
        )
    }

    @Test
    fun test_room_news_resource_type_converter_for_event() {
        assertEquals(
            NewsResourceType.Event,
            NewsResourceTypeConverter().stringToNewsResourceType("Event 📆"),
        )
    }

    @Test
    fun test_room_news_resource_type_converter_for_dac() {
        assertEquals(
            NewsResourceType.DAC,
            NewsResourceTypeConverter().stringToNewsResourceType("DAC"),
        )
    }

    @Test
    fun test_room_news_resource_type_converter_for_umm() {
        assertEquals(
            NewsResourceType.Unknown,
            NewsResourceTypeConverter().stringToNewsResourceType("umm"),
        )
    }
}
