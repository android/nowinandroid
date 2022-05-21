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

package com.google.samples.apps.nowinandroid.core.network.fake

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FakeNiaNetworkTest {

    private lateinit var subject: FakeNiaNetwork

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        subject = com.google.samples.apps.nowinandroid.core.network.fake.FakeNiaNetwork(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true }
        )
    }

    @Test
    fun testDeserializationOfTopics() = runTest(testDispatcher) {
        assertEquals(
            FakeDataSource.sampleTopic,
            subject.getTopics().first()
        )
    }

    @Test
    fun testDeserializationOfNewsResources() = runTest(testDispatcher) {
        assertEquals(
            FakeDataSource.sampleResource,
            subject.getNewsResources().first()
        )
    }
}
