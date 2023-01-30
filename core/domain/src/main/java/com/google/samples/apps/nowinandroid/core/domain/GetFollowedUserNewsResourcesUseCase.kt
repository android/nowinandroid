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

package com.google.samples.apps.nowinandroid.core.domain

import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A use case which returns a flow of news resources which the user is following.
 */
class GetFollowedUserNewsResourcesUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    val getUserNewsResources: GetUserNewsResourcesUseCase,
) {
    /**
     * Returns a list of UserNewsResources for topics which the user is following.
     */
    operator fun invoke(): Flow<List<UserNewsResource>> =
        /**
         * This sequence of flow transformation functions does the following:
         *
         * - map: maps the user data into a set of followed topic IDs or null if we should return
         * an empty list
         * - distinctUntilChanged: will only emit a set of followed topic IDs if it's changed. This
         * avoids calling potentially expensive operations (like setting up a new flow) when nothing
         * has changed.
         * - flatMapLatest: getUserNewsResources returns a flow, so we have a flow inside a
         * flow. flatMapLatest moves the inner flow (the one we want to return) to the outer flow
         * and cancels any previous flows created by getUserNewsResources.
         */
        userDataRepository.userData
            .map { userData ->
                if (userData.shouldShowEmptyFeed()) {
                    null
                } else {
                    userData.followedTopics
                }
            }
            .distinctUntilChanged()
            .flatMapLatest { followedTopics ->
                if (followedTopics == null) {
                    flowOf(emptyList())
                } else {
                    getUserNewsResources(filterTopicIds = followedTopics)
                }
            }

    /**
     * If the user hasn't completed the onboarding and hasn't selected any interests
     * show an empty news list to clearly demonstrate that their selections affect the
     * news articles they will see.
     *
     * Note: It should not be possible for the user to get into a state where the onboarding
     * is not displayed AND they haven't followed any topics, however, this method is to safeguard
     * against that scenario in future.
     */
    private fun UserData.shouldShowEmptyFeed() =
        !shouldHideOnboarding && followedTopics.isEmpty()
}
