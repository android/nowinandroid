/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.network.demo

import JvmUnitTestDemoAssetManager
import android.os.Build
import com.google.samples.apps.nowinandroid.core.network.Dispatcher
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers.IO
import com.google.samples.apps.nowinandroid.core.network.NiaNetworkDataSource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkChangeList
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

/**
 * [NiaNetworkDataSource] implementation that provides static news resources to aid development
 */
class DemoNiaNetworkDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: DemoAssetManager = JvmUnitTestDemoAssetManager,
) : NiaNetworkDataSource {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getTopics(ids: List<String>?): List<NetworkTopic> =
        withContext(ioDispatcher) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                assets.open(TOPICS_ASSET).use(networkJson::decodeFromStream)
            } else {
                // Use decodeFromString to capability with API 24 below.
                // https://github.com/Kotlin/kotlinx.serialization/issues/2457#issuecomment-1786923342
                val topicsJsonString = convertStreamToString(assets.open(TOPICS_ASSET))
                networkJson.decodeFromString(topicsJsonString)
            }
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getNewsResources(ids: List<String>?): List<NetworkNewsResource> =
        withContext(ioDispatcher) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                assets.open(NEWS_ASSET).use(networkJson::decodeFromStream)
            } else {
                // Use decodeFromString to capability with API 24 below.
                // https://github.com/Kotlin/kotlinx.serialization/issues/2457#issuecomment-1786923342
                val newsJsonString = convertStreamToString(assets.open(NEWS_ASSET))
                networkJson.decodeFromString(newsJsonString)
            }
        }

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        getTopics().mapToChangeList(NetworkTopic::id)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        getNewsResources().mapToChangeList(NetworkNewsResource::id)

    /**
     * Convert [InputStream] to [String].
     */
    private suspend fun convertStreamToString(inputStream: InputStream): String = withContext(
        coroutineContext,
    ) {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (true) {
            length = inputStream.read(buffer)
            if (length == -1) break
            result.write(buffer, 0, length)
        }

        result.toString(StandardCharsets.UTF_8.name())
    }

    companion object {
        private const val NEWS_ASSET = "news.json"
        private const val TOPICS_ASSET = "topics.json"
    }
}

/**
 * Converts a list of [T] to change list of all the items in it where [idGetter] defines the
 * [NetworkChangeList.id]
 */
private fun <T> List<T>.mapToChangeList(
    idGetter: (T) -> String,
) = mapIndexed { index, item ->
    NetworkChangeList(
        id = idGetter(item),
        changeListVersion = index,
        isDelete = false,
    )
}
