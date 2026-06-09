/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.model.data

import kotlinx.datetime.Instant

/**
 * A [NewsResource] with additional user information such as whether the user is following the
 * news resource's topics and whether they have saved (bookmarked) this news resource.
 */
data class UserNewsResource internal constructor(
    val newsResource: NewsResource,
    val userData: UserData,
) {
    val id: String get() = newsResource.id
    val title: String get() = newsResource.title
    val content: String get() = newsResource.content
    val url: String get() = newsResource.url
    val headerImageUrl: String? get() = newsResource.headerImageUrl
    val publishDate: Instant get() = newsResource.publishDate
    val type: String get() = newsResource.type
    val followableTopics: List<FollowableTopic>
        get() = newsResource.topics.map { topic ->
            FollowableTopic(
                topic = topic,
                isFollowed = topic.id in userData.followedTopics,
            )
        }
    val isSaved: Boolean get() = newsResource.id in userData.bookmarkedNewsResources
    val hasBeenViewed: Boolean get() = newsResource.id in userData.viewedNewsResources
    val bookmarkNote: String?
        get() = userData.bookmarkNotes[newsResource.id].takeIf { !it.isNullOrBlank() }
}

fun List<NewsResource>.mapToUserNewsResources(userData: UserData): List<UserNewsResource> =
    map { UserNewsResource(it, userData) }
