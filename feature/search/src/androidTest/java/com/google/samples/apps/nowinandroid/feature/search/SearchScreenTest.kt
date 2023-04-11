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

package com.google.samples.apps.nowinandroid.feature.search

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onParent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Search screen.
 */
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var clearSearchText: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            clearSearchText = getString(R.string.clear_search_text)
        }
    }

    @Test
    fun searchTextField_isFocused() {
        composeTestRule.setContent {
            SearchScreen()
        }

        composeTestRule
            .onNodeWithContentDescription(clearSearchText)
            // The parent of the IconButton whose contentDescription matches the clearSearchText
            // should be the TextField for search
            .onParent()
            .assertIsFocused()
    }
}
