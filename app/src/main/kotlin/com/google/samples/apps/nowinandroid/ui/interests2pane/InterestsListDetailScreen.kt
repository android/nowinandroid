/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.ui.interests2pane

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.samples.apps.nowinandroid.feature.interests.InterestsRoute
import com.google.samples.apps.nowinandroid.feature.interests.navigation.InterestsDestination
import com.google.samples.apps.nowinandroid.feature.topic.TopicDetailPlaceholder
import com.google.samples.apps.nowinandroid.feature.topic.navigation.navigateToTopic
import com.google.samples.apps.nowinandroid.feature.topic.navigation.topicScreen
import kotlinx.serialization.Serializable

@Serializable object TopicPlaceholderDestination

@Serializable object DetailPaneNavHostDestination

fun NavGraphBuilder.interestsListDetailScreen() {
    composable<InterestsDestination> {
        InterestsListDetailScreen()
    }
}

@Composable
internal fun InterestsListDetailScreen(
    viewModel: Interests2PaneViewModel = hiltViewModel(),
) {
    val selectedTopicId by viewModel.selectedTopicId.collectAsStateWithLifecycle()
    InterestsListDetailScreen(
        selectedTopicId = selectedTopicId,
        onTopicClick = viewModel::onTopicClick,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun InterestsListDetailScreen(
    selectedTopicId: String?,
    onTopicClick: (String) -> Unit,
) {
    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator()
    BackHandler(listDetailNavigator.canNavigateBack()) {
        listDetailNavigator.navigateBack()
    }

    val nestedNavController = rememberNavController()

    fun onTopicClickShowDetailPane(topicId: String) {
        onTopicClick(topicId)
        nestedNavController.navigateToTopic(topicId) {
            popUpTo<DetailPaneNavHostDestination>()
        }
        listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
    }

    ListDetailPaneScaffold(
        value = listDetailNavigator.scaffoldValue,
        directive = listDetailNavigator.scaffoldDirective,
        listPane = {
            InterestsRoute(
                onTopicClick = ::onTopicClickShowDetailPane,
                highlightSelectedTopic = listDetailNavigator.isDetailPaneVisible(),
            )
        },
        detailPane = {
            NavHost(
                navController = nestedNavController,
                startDestination = TopicPlaceholderDestination,
                route = DetailPaneNavHostDestination::class,
            ) {
                topicScreen(
                    showBackButton = !listDetailNavigator.isListPaneVisible(),
                    onBackClick = listDetailNavigator::navigateBack,
                    onTopicClick = ::onTopicClickShowDetailPane,
                )
                composable<TopicPlaceholderDestination> {
                    TopicDetailPlaceholder()
                }
            }
        },
    )
    LaunchedEffect(Unit) {
        if (selectedTopicId != null) {
            // Initial topic ID was provided when navigating to Interests, so show its details.
            onTopicClickShowDetailPane(selectedTopicId)
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isListPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isDetailPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
