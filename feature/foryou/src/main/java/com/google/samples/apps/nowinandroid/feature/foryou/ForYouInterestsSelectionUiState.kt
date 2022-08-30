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

import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic

/**
 * A sealed hierarchy describing the interests selection state for the for you screen.
 */
sealed interface ForYouInterestsSelectionUiState {
    /**
     * The interests selection state is loading.
     */
    object Loading : ForYouInterestsSelectionUiState

    /**
     * The interests selection state was unable to load.
     */
    object LoadFailed : ForYouInterestsSelectionUiState

    /**
     * There is no interests selection state.
     */
    object NoInterestsSelection : ForYouInterestsSelectionUiState

    /**
     * There is a interests selection state, with the given lists of topics and authors.
     */
    data class WithInterestsSelection(
        val topics: List<FollowableTopic>,
        val authors: List<FollowableAuthor>
    ) : ForYouInterestsSelectionUiState {
        /**
         * True if the current in-progress selection can be saved.
         */
        val canSaveInterests: Boolean get() =
            topics.any { it.isFollowed } || authors.any { it.isFollowed }
    }
}
