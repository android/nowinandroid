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
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import com.google.samples.apps.nowinandroid.core.data.model.RecentSearchQuery
import com.google.samples.apps.nowinandroid.core.model.data.DarkThemeConfig.DARK
import com.google.samples.apps.nowinandroid.core.model.data.ThemeBrand.ANDROID
import com.google.samples.apps.nowinandroid.core.model.data.UserData
import com.google.samples.apps.nowinandroid.core.model.data.UserNewsResource
import com.google.samples.apps.nowinandroid.core.testing.data.followableTopicTestData
import com.google.samples.apps.nowinandroid.core.testing.data.newsResourcesTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.samples.apps.nowinandroid.feature.interests.R as interestsR

/**
 * UI test for checking the correct behaviour of the Search screen.
 */
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var clearSearchContentDesc: String
    private lateinit var followButtonContentDesc: String
    private lateinit var unfollowButtonContentDesc: String
    private lateinit var clearRecentSearchesContentDesc: String
    private lateinit var topicsString: String
    private lateinit var updatesString: String
    private lateinit var tryAnotherSearchString: String
    private lateinit var searchNotReadyString: String

    private val userData: UserData = UserData(
        bookmarkedNewsResources = setOf("1", "3"),
        viewedNewsResources = setOf("1", "2", "4"),
        followedTopics = emptySet(),
        themeBrand = ANDROID,
        darkThemeConfig = DARK,
        shouldHideOnboarding = true,
        useDynamicColor = false,
    )

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            clearSearchContentDesc = getString(R.string.feature_search_clear_search_text_content_desc)
            clearRecentSearchesContentDesc = getString(R.string.feature_search_clear_recent_searches_content_desc)
            followButtonContentDesc =
                getString(interestsR.string.feature_interests_card_follow_button_content_desc)
            unfollowButtonContentDesc =
                getString(interestsR.string.feature_interests_card_unfollow_button_content_desc)
            topicsString = getString(R.string.feature_search_topics)
            updatesString = getString(R.string.feature_search_updates)
            tryAnotherSearchString = getString(R.string.feature_search_try_another_search) +
                " " + getString(R.string.feature_search_interests) + " " + getString(R.string.feature_search_to_browse_topics)
            searchNotReadyString = getString(R.string.feature_search_not_ready)
        }
    }

    @Test
    fun searchTextField_isFocused() {
        composeTestRule.setContent {
            SearchScreen()
        }

        composeTestRule
            .onNodeWithTag("searchTextField")
            .assertIsFocused()
    }

    @Test
    fun emptySearchResult_emptyScreenIsDisplayed() {
        composeTestRule.setContent {
            SearchScreen(
                searchResultUiState = SearchResultUiState.Success(),
            )
        }

        composeTestRule
            .onNodeWithText(tryAnotherSearchString)
            .assertIsDisplayed()
    }

    @Test
    fun emptySearchResult_nonEmptyRecentSearches_emptySearchScreenAndRecentSearchesAreDisplayed() {
        val recentSearches = listOf("kotlin")
        composeTestRule.setContent {
            SearchScreen(
                searchResultUiState = SearchResultUiState.Success(),
                recentSearchesUiState = RecentSearchQueriesUiState.Success(
                    recentQueries = recentSearches.map(::RecentSearchQuery),
                ),
            )
        }

        composeTestRule
            .onNodeWithText(tryAnotherSearchString)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(clearRecentSearchesContentDesc)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("kotlin")
            .assertIsDisplayed()
    }

    @Test
    fun searchResultWithTopics_allTopicsAreVisible_followButtonsVisibleForTheNumOfFollowedTopics() {
        composeTestRule.setContent {
            SearchScreen(
                searchResultUiState = SearchResultUiState.Success(topics = followableTopicTestData),
            )
        }

        composeTestRule
            .onNodeWithText(topicsString)
            .assertIsDisplayed()

        val scrollableNode = composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()

        followableTopicTestData.forEachIndexed { index, followableTopic ->
            scrollableNode.performScrollToIndex(index)

            composeTestRule
                .onNodeWithText(followableTopic.topic.name)
                .assertIsDisplayed()
        }

        composeTestRule
            .onAllNodesWithContentDescription(followButtonContentDesc)
            .assertCountEquals(2)
        composeTestRule
            .onAllNodesWithContentDescription(unfollowButtonContentDesc)
            .assertCountEquals(1)
    }

    @Test
    fun searchResultWithNewsResources_firstNewsResourcesIsVisible() {
        composeTestRule.setContent {
            SearchScreen(
                searchResultUiState = SearchResultUiState.Success(
                    newsResources = newsResourcesTestData.map {
                        UserNewsResource(
                            newsResource = it,
                            userData = userData,
                        )
                    },
                ),
            )
        }

        composeTestRule
            .onNodeWithText(updatesString)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(newsResourcesTestData[0].title)
            .assertIsDisplayed()
    }

    @Test
    fun emptyQuery_notEmptyRecentSearches_verifyClearSearchesButton_displayed() {
        val recentSearches = listOf("kotlin", "testing")
        composeTestRule.setContent {
            SearchScreen(
                searchResultUiState = SearchResultUiState.EmptyQuery,
                recentSearchesUiState = RecentSearchQueriesUiState.Success(
                    recentQueries = recentSearches.map(::RecentSearchQuery),
                ),
            )
        }

        composeTestRule
            .onNodeWithContentDescription(clearRecentSearchesContentDesc)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("kotlin")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("testing")
            .assertIsDisplayed()
    }

    @Test
    fun searchNotReady_verifySearchNotReadyMessageIsVisible() {
        composeTestRule.setContent {
            SearchScreen(
                searchResultUiState = SearchResultUiState.SearchNotReady,
            )
        }

        composeTestRule
            .onNodeWithText(searchNotReadyString)
            .assertIsDisplayed()
    }
}
