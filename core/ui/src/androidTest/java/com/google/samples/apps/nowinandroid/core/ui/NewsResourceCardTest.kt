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

package com.google.samples.apps.nowinandroid.core.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.domain.model.previewFollowableTopics
import com.google.samples.apps.nowinandroid.core.domain.model.previewUserNewsResources
import org.junit.Rule
import org.junit.Test

class NewsResourceCardTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testMetaDataDisplay_withCodelabResource() {
        val newsWithKnownResourceType = previewUserNewsResources[0]
        var dateFormatted = ""

        composeTestRule.setContent {
            NewsResourceCardExpanded(
                userNewsResource = newsWithKnownResourceType,
                isBookmarked = false,
                onToggleBookmark = {},
                onClick = {},
            )

            dateFormatted = dateFormatted(publishDate = newsWithKnownResourceType.publishDate)
        }

        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(
                    R.string.card_meta_data_text,
                    dateFormatted,
                    newsWithKnownResourceType.type.displayText,
                ),
            )
            .assertExists()
    }

    @Test
    fun testMetaDataDisplay_withUnknownResource() {
        val newsWithUnknownResourceType = previewUserNewsResources[3]
        var dateFormatted = ""

        composeTestRule.setContent {
            NewsResourceCardExpanded(
                userNewsResource = newsWithUnknownResourceType,
                isBookmarked = false,
                onToggleBookmark = {},
                onClick = {},
            )

            dateFormatted = dateFormatted(publishDate = newsWithUnknownResourceType.publishDate)
        }

        composeTestRule
            .onNodeWithText(dateFormatted)
            .assertIsDisplayed()
    }

    @Test
    fun testTopicsChipColorBackground_matchesFollowedState() {
        composeTestRule.setContent {
            NewsResourceTopics(topics = previewFollowableTopics)
        }

        for (followableTopic in previewFollowableTopics) {
            val topicName = followableTopic.topic.name
            val expectedContentDescription = if (followableTopic.isFollowed) {
                "$topicName is followed"
            } else {
                "$topicName is not followed"
            }
            composeTestRule
                .onNodeWithText(topicName.uppercase())
                .assertContentDescriptionEquals(expectedContentDescription)
        }
    }
}
