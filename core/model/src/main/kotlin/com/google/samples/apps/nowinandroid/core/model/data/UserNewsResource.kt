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
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val headerImageUrl: String?,
    val publishDate: Instant,
    val type: String,
    val followableTopics: List<FollowableTopic>,
    val isSaved: Boolean,
    val hasBeenViewed: Boolean,
) {
    constructor(newsResource: NewsResource, userData: UserData) : this(
        id = newsResource.id,
        title = newsResource.title,
        content = newsResource.content,
        url = newsResource.url,
        headerImageUrl = newsResource.headerImageUrl,
        publishDate = newsResource.publishDate,
        type = newsResource.type,
        followableTopics = newsResource.topics.map { topic ->
            FollowableTopic(
                topic = topic,
                isFollowed = topic.id in userData.followedTopics,
            )
        },
        isSaved = newsResource.id in userData.bookmarkedNewsResources,
        hasBeenViewed = newsResource.id in userData.viewedNewsResources,
    )
}

fun List<NewsResource>.mapToUserNewsResources(userData: UserData): List<UserNewsResource> =
    map { UserNewsResource(it, userData) }
