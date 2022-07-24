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

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.google.samples.apps.nowinandroid.core.model.data.previewNewsResources
import org.junit.Rule
import org.junit.Test

class NewsResourceCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNewsResourceCard_displaysNewsResource() {
        val newsResource = previewNewsResources[0]
        var dateFormatted = ""

        composeTestRule.setContent {
            NewsResourceCardExpanded(
                newsResource = newsResource,
                isBookmarked = false,
                onToggleBookmark = {},
                onClick = {}
            )

            dateFormatted = dateFormatted(publishDate = newsResource.publishDate)
        }

        composeTestRule
            .onNodeWithText(newsResource.title)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(newsResource.authors[0].name.uppercase())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(dateFormatted)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(newsResource.content)
            .assertIsDisplayed()
    }

    @Test
    fun testNewsResourceCard_hasClickAction() {
        val newsResource = previewNewsResources[0]

        composeTestRule.setContent {
            NewsResourceCardExpanded(
                newsResource = newsResource,
                isBookmarked = false,
                onToggleBookmark = {},
                onClick = { }
            )
        }

        composeTestRule.onNodeWithTag(NewsResourceCardTestTag).assertHasClickAction()
    }
}
