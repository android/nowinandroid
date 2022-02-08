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

package com.google.samples.apps.nowinandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.samples.apps.nowinandroid.data.local.dao.AuthorDao
import com.google.samples.apps.nowinandroid.data.local.dao.EpisodeDao
import com.google.samples.apps.nowinandroid.data.local.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.data.local.dao.TopicDao
import com.google.samples.apps.nowinandroid.data.local.entities.AuthorEntity
import com.google.samples.apps.nowinandroid.data.local.entities.EpisodeAuthorCrossRef
import com.google.samples.apps.nowinandroid.data.local.entities.EpisodeEntity
import com.google.samples.apps.nowinandroid.data.local.entities.NewsResourceAuthorCrossRef
import com.google.samples.apps.nowinandroid.data.local.entities.NewsResourceEntity
import com.google.samples.apps.nowinandroid.data.local.entities.NewsResourceTopicCrossRef
import com.google.samples.apps.nowinandroid.data.local.entities.TopicEntity
import com.google.samples.apps.nowinandroid.data.local.utilities.InstantConverter
import com.google.samples.apps.nowinandroid.data.local.utilities.NewsResourceTypeConverter

@Database(
    entities = [
        AuthorEntity::class,
        EpisodeAuthorCrossRef::class,
        EpisodeEntity::class,
        NewsResourceAuthorCrossRef::class,
        NewsResourceEntity::class,
        NewsResourceTopicCrossRef::class,
        TopicEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    InstantConverter::class,
    NewsResourceTypeConverter::class,
)
abstract class NiADatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun authorDao(): AuthorDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun newsResourceDao(): NewsResourceDao
}
