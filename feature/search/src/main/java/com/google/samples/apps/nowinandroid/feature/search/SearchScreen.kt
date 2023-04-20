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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableTopic
import com.google.samples.apps.nowinandroid.core.domain.model.UserNewsResource
import com.google.samples.apps.nowinandroid.core.ui.DevicePreviews
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.R.string
import com.google.samples.apps.nowinandroid.core.ui.TrackScreenViewEvent
import com.google.samples.apps.nowinandroid.core.ui.TrackScrollJank
import com.google.samples.apps.nowinandroid.core.ui.newsFeed
import com.google.samples.apps.nowinandroid.feature.foryou.ForYouViewModel
import com.google.samples.apps.nowinandroid.feature.interests.InterestsViewModel
import com.google.samples.apps.nowinandroid.feature.interests.TopicsTabContent
import com.google.samples.apps.nowinandroid.feature.search.R as searchR

@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onInterestsClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    interestsViewModel: InterestsViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    forYouViewModel: ForYouViewModel = hiltViewModel(),
) {
    val uiState by searchViewModel.searchResultUiState.collectAsStateWithLifecycle()
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    SearchScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onFollowButtonClick = interestsViewModel::followTopic,
        onInterestsClick = onInterestsClick,
        onSearchQueryChanged = searchViewModel::onSearchQueryChanged,
        onTopicClick = onTopicClick,
        onNewsResourcesCheckedChanged = forYouViewModel::updateNewsResourceSaved,
        searchQuery = searchQuery,
        uiState = uiState,
    )
}

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFollowButtonClick: (String, Boolean) -> Unit = { _, _ -> },
    onInterestsClick: () -> Unit = {},
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit = { _, _ -> },
    onSearchQueryChanged: (String) -> Unit = {},
    onTopicClick: (String) -> Unit = {},
    searchQuery: String = "",
    uiState: SearchResultUiState = SearchResultUiState.Loading,
) {
    TrackScreenViewEvent(screenName = "Search")
    Column(modifier = modifier) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        SearchToolbar(
            onBackClick = onBackClick,
            onSearchQueryChanged = onSearchQueryChanged,
            searchQuery = searchQuery,
        )
        when (uiState) {
            SearchResultUiState.Loading,
            SearchResultUiState.LoadFailed,
            SearchResultUiState.EmptyQuery,
            -> Unit
            is SearchResultUiState.Success -> {
                if (uiState.isEmpty()) {
                    EmptySearchResultBody(
                        onInterestsClick = onInterestsClick,
                        searchQuery = searchQuery,
                    )
                } else {
                    SearchResultBody(
                        topics = uiState.topics,
                        onFollowButtonClick = onFollowButtonClick,
                        onNewsResourcesCheckedChanged = onNewsResourcesCheckedChanged,
                        onTopicClick = onTopicClick,
                        newsResources = uiState.newsResources,
                    )
                }
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
}

@Composable
fun EmptySearchResultBody(
    onInterestsClick: () -> Unit,
    searchQuery: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val message = stringResource(id = searchR.string.search_result_not_found, searchQuery)
        val start = message.indexOf(searchQuery)
        Text(
            text = AnnotatedString(
                text = message,
                spanStyles = listOf(
                    AnnotatedString.Range(
                        SpanStyle(fontWeight = FontWeight.Bold),
                        start = start,
                        end = start + searchQuery.length,
                    ),
                ),
            ),
            modifier = Modifier.padding(horizontal = 36.dp, vertical = 24.dp),
        )
        val interests = stringResource(id = searchR.string.interests)
        val tryAnotherSearchString = buildAnnotatedString {
            append(stringResource(id = searchR.string.try_another_search))
            append(" ")
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                pushStringAnnotation(tag = interests, annotation = interests)
                append(interests)
            }
            append(" ")
            append(stringResource(id = searchR.string.to_browse_topics))
        }
        ClickableText(
            text = tryAnotherSearchString,
            modifier = Modifier
                .padding(start = 36.dp, end = 36.dp, bottom = 24.dp)
                .clickable {},
        ) { offset ->
            tryAnotherSearchString.getStringAnnotations(start = offset, end = offset)
                .firstOrNull()
                ?.let {
                    onInterestsClick()
                }
        }
    }
}

@Composable
private fun SearchResultBody(
    topics: List<FollowableTopic>,
    newsResources: List<UserNewsResource>,
    onFollowButtonClick: (String, Boolean) -> Unit,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit,
    onTopicClick: (String) -> Unit,
) {
    if (topics.isNotEmpty()) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = searchR.string.topics))
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        TopicsTabContent(
            topics = topics,
            onTopicClick = onTopicClick,
            onFollowButtonClick = onFollowButtonClick,
            withBottomSpacer = false,
        )
    }

    if (newsResources.isNotEmpty()) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = searchR.string.updates))
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        val state = rememberLazyGridState()
        TrackScrollJank(scrollableState = state, stateName = "search:newsResource")
        LazyVerticalGrid(
            columns = Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("search:newsResources"),
            state = state,
        ) {
            newsFeed(
                feedState = NewsFeedUiState.Success(feed = newsResources),
                onNewsResourcesCheckedChanged = onNewsResourcesCheckedChanged,
                onTopicClick = onTopicClick,
            )
        }
    }
}

@Composable
private fun SearchToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String = "",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = NiaIcons.ArrowBack,
                contentDescription = stringResource(
                    id = string.back,
                ),
            )
        }
        SearchTextField(
            onSearchQueryChanged = onSearchQueryChanged,
            searchQuery = searchQuery,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = NiaIcons.Search,
                contentDescription = stringResource(
                    id = searchR.string.search,
                ),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                onSearchQueryChanged("")
            }) {
                Icon(
                    imageVector = NiaIcons.Close,
                    contentDescription = stringResource(
                        id = searchR.string.clear_search_text_content_desc,
                    ),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        onValueChange = {
            onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
private fun SearchToolbarPreview() {
    NiaTheme {
        SearchToolbar(
            onBackClick = {},
            onSearchQueryChanged = {},
        )
    }
}

@Preview
@Composable
private fun EmptySearchResultColumnPreview() {
    NiaTheme {
        EmptySearchResultBody(
            onInterestsClick = {},
            searchQuery = "C++",
        )
    }
}

@DevicePreviews
@Composable
private fun SearchScreenPreview(
    @PreviewParameter(SearchUiStatePreviewParameterProvider::class)
    searchResultUiState: SearchResultUiState,
) {
    NiaTheme {
        SearchScreen(uiState = searchResultUiState)
    }
}
