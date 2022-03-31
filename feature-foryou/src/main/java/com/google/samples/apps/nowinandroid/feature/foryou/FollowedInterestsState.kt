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

package com.google.samples.apps.nowinandroid.feature.foryou

/**
 * A sealed hierarchy for the user's current followed interests state.
 */
sealed interface FollowedInterestsState {

    /**
     * The current state is unknown (hasn't loaded yet)
     */
    object Unknown : FollowedInterestsState

    /**
     * The user hasn't followed any interests yet.
     */
    object None : FollowedInterestsState

    /**
     * The user has followed the given (non-empty) set of [topicIds] or [authorIds].
     */
    data class FollowedInterests(
        val topicIds: Set<String>,
        val authorIds: Set<String>
    ) : FollowedInterestsState
}
