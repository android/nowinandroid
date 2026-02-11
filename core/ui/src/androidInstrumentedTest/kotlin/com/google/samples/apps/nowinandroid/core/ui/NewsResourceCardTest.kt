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
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.testing.data.followableTopicTestData
import com.google.samples.apps.nowinandroid.core.testing.data.userNewsResourcesTestData
import nowinandroid.core.ui.generated.resources.Res
import nowinandroid.core.ui.generated.resources.core_ui_card_meta_data_text
import nowinandroid.core.ui.generated.resources.core_ui_unread_resource_dot_content_description
import org.jetbrains.compose.resources.stringResource
import org.junit.Rule
import org.junit.Test

class NewsResourceCardTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testMetaDataDisplay_withCodelabResource() {
        val newsWithKnownResourceType = userNewsResourcesTestData[0]
        lateinit var expectedMetaData: String

        composeTestRule.setContent {
            expectedMetaData = stringResource(
                Res.string.core_ui_card_meta_data_text,
                dateFormatted(publishDate = newsWithKnownResourceType.publishDate),
                newsWithKnownResourceType.type,
            )
            NewsResourceCardExpanded(
                userNewsResource = newsWithKnownResourceType,
                isBookmarked = false,
                hasBeenViewed = false,
                onToggleBookmark = {},
                onClick = {},
                onTopicClick = {},
            )
        }

        composeTestRule
            .onNodeWithText(expectedMetaData)
            .assertExists()
    }

    @Test
    fun testMetaDataDisplay_withEmptyResourceType() {
        val newsWithEmptyResourceType = userNewsResourcesTestData[3]
        lateinit var dateFormatted: String

        composeTestRule.setContent {
            NewsResourceCardExpanded(
                userNewsResource = newsWithEmptyResourceType,
                isBookmarked = false,
                hasBeenViewed = false,
                onToggleBookmark = {},
                onClick = {},
                onTopicClick = {},
            )

            dateFormatted = dateFormatted(publishDate = newsWithEmptyResourceType.publishDate)
        }

        composeTestRule
            .onNodeWithText(dateFormatted)
            .assertIsDisplayed()
    }

    @Test
    fun testTopicsChipColorBackground_matchesFollowedState() {
        composeTestRule.setContent {
            NewsResourceTopics(
                topics = followableTopicTestData,
                onTopicClick = {},
            )
        }

        for (followableTopic in followableTopicTestData) {
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

    @Test
    fun testUnreadDot_displayedWhenUnread() {
        val unreadNews = userNewsResourcesTestData[2]
        lateinit var unreadContentDescription: String

        composeTestRule.setContent {
            unreadContentDescription = stringResource(
                Res.string.core_ui_unread_resource_dot_content_description,
            )
            NewsResourceCardExpanded(
                userNewsResource = unreadNews,
                isBookmarked = false,
                hasBeenViewed = false,
                onToggleBookmark = {},
                onClick = {},
                onTopicClick = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(unreadContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun testUnreadDot_notDisplayedWhenRead() {
        val readNews = userNewsResourcesTestData[0]
        lateinit var unreadContentDescription: String

        composeTestRule.setContent {
            unreadContentDescription = stringResource(
                Res.string.core_ui_unread_resource_dot_content_description,
            )
            NewsResourceCardExpanded(
                userNewsResource = readNews,
                isBookmarked = false,
                hasBeenViewed = true,
                onToggleBookmark = {},
                onClick = {},
                onTopicClick = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(unreadContentDescription)
            .assertDoesNotExist()
    }
}
