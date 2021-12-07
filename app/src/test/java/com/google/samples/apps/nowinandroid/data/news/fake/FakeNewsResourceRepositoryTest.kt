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

package com.google.samples.apps.nowinandroid.data.news.fake

import android.content.Context
import android.content.res.Resources
import com.google.samples.apps.nowinandroid.R
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before

class FakeNewsResourceRepositoryTest {

    private lateinit var subject: FakeNewsResourceRepository

    @Before
    fun setup() {
        val context = mockk<Context>()
        val resources = mockk<Resources>()
        every { resources.openRawResource(R.raw.data) } returns testResourcesJson.byteInputStream()
        every { context.resources } returns resources

        subject = FakeNewsResourceRepository(context)
    }

    @org.junit.Test
    fun newsResources() = runBlocking {
        // TODO: Implement this
        // assertEquals(listOf<NewsResource>(), subject.monitor().first())
    }
}

private val testResourcesJson = """
        [
          {
            "episode": 52,
            "title": "MAD Skills: Paging Q&A",
            "content": "In this live session, TJ and Dustin answered your questions in the usual live Q&A format.",
            "URL": "https://youtu.be/8i6vrlbIVCc",
            "authorName": "",
            "publishDate": "2021-11-11T00:00:00.000Z",
            "type": "Video 📺",
            "topics": [
              "MAD Skills",
              "Paging"
            ],
            "alternateVideo": {
              "URL": "",
              "startTimestamp": "",
              "endTimestamp": ""
            }
          }
        ]
""".trimIndent()
