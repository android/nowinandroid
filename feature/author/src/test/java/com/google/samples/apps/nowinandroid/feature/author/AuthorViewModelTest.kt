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

package com.google.samples.apps.nowinandroid.feature.author

import androidx.lifecycle.SavedStateHandle
import com.google.samples.apps.nowinandroid.core.domain.GetSaveableNewsResourcesStreamUseCase
import com.google.samples.apps.nowinandroid.core.domain.model.FollowableAuthor
import com.google.samples.apps.nowinandroid.core.model.data.Author
import com.google.samples.apps.nowinandroid.core.model.data.NewsResource
import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Video
import com.google.samples.apps.nowinandroid.core.testing.decoder.FakeStringDecoder
import com.google.samples.apps.nowinandroid.core.testing.repository.TestAuthorsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestNewsRepository
import com.google.samples.apps.nowinandroid.core.testing.repository.TestUserDataRepository
import com.google.samples.apps.nowinandroid.core.testing.util.MainDispatcherRule
import com.google.samples.apps.nowinandroid.feature.author.navigation.authorIdArg
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 */
class AuthorViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()
    private val authorsRepository = TestAuthorsRepository()
    private val newsRepository = TestNewsRepository()
    private val getSaveableNewsResourcesStreamUseCase = GetSaveableNewsResourcesStreamUseCase(
        newsRepository = newsRepository,
        userDataRepository = userDataRepository
    )
    private lateinit var viewModel: AuthorViewModel

    @Before
    fun setup() {
        viewModel = AuthorViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    authorIdArg to testInputAuthors[0].author.id
                )
            ),
            stringDecoder = FakeStringDecoder(),
            userDataRepository = userDataRepository,
            authorsRepository = authorsRepository,
            getSaveableNewsResourcesStream = getSaveableNewsResourcesStreamUseCase
        )
    }

    @Test
    fun uiStateAuthor_whenSuccess_matchesAuthorFromRepository() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.authorUiState.collect() }

        // To make sure AuthorUiState is success
        authorsRepository.sendAuthors(testInputAuthors.map(FollowableAuthor::author))
        userDataRepository.setFollowedAuthorIds(setOf(testInputAuthors[1].author.id))

        val item = viewModel.authorUiState.value
        assertTrue(item is AuthorUiState.Success)

        val successAuthorUiState = item as AuthorUiState.Success
        val authorFromRepository = authorsRepository.getAuthorStream(
            id = testInputAuthors[0].author.id
        ).first()

        successAuthorUiState.followableAuthor.author
        assertEquals(authorFromRepository, successAuthorUiState.followableAuthor.author)

        collectJob.cancel()
    }

    @Test
    fun uiStateNews_whenInitialized_thenShowLoading() = runTest {
        assertEquals(NewsUiState.Loading, viewModel.newsUiState.value)
    }

    @Test
    fun uiStateAuthor_whenInitialized_thenShowLoading() = runTest {
        assertEquals(AuthorUiState.Loading, viewModel.authorUiState.value)
    }

    @Test
    fun uiStateAuthor_whenFollowedIdsSuccessAndAuthorLoading_thenShowLoading() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.authorUiState.collect() }

        userDataRepository.setFollowedAuthorIds(setOf(testInputAuthors[1].author.id))
        assertEquals(AuthorUiState.Loading, viewModel.authorUiState.value)

        collectJob.cancel()
    }

    @Test
    fun uiStateAuthor_whenFollowedIdsSuccessAndAuthorSuccess_thenAuthorSuccessAndNewsLoading() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) {
                combine(
                    viewModel.authorUiState,
                    viewModel.newsUiState,
                    ::Pair
                ).collect()
            }

            authorsRepository.sendAuthors(testInputAuthors.map { it.author })
            userDataRepository.setFollowedAuthorIds(setOf(testInputAuthors[1].author.id))
            val authorState = viewModel.authorUiState.value
            val newsUiState = viewModel.newsUiState.value

            assertTrue(authorState is AuthorUiState.Success)
            assertTrue(newsUiState is NewsUiState.Loading)

            collectJob.cancel()
        }

    @Test
    fun uiStateAuthor_whenFollowedIdsSuccessAndAuthorSuccessAndNewsIsSuccess_thenAllSuccess() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) {
                combine(
                    viewModel.authorUiState,
                    viewModel.newsUiState,
                    ::Pair
                ).collect()
            }

            authorsRepository.sendAuthors(testInputAuthors.map { it.author })
            userDataRepository.setFollowedAuthorIds(setOf(testInputAuthors[1].author.id))
            newsRepository.sendNewsResources(sampleNewsResources)
            val authorState = viewModel.authorUiState.value
            val newsUiState = viewModel.newsUiState.value

            assertTrue(authorState is AuthorUiState.Success)
            assertTrue(newsUiState is NewsUiState.Success)

            collectJob.cancel()
        }

    @Test
    fun uiStateAuthor_whenFollowingAuthor_thenShowUpdatedAuthor() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.authorUiState.collect() }

        authorsRepository.sendAuthors(testInputAuthors.map { it.author })
        // Set which author IDs are followed, not including 0.
        userDataRepository.setFollowedAuthorIds(setOf(testInputAuthors[1].author.id))

        viewModel.followAuthorToggle(true)

        assertEquals(
            AuthorUiState.Success(followableAuthor = testOutputAuthors[0]),
            viewModel.authorUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun uiStateAuthor_whenNewsBookmarked_thenShowBookmarkedNews() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.newsUiState.collect() }

        authorsRepository.sendAuthors(testInputAuthors.map { it.author })
        newsRepository.sendNewsResources(sampleNewsResources)

        // Set initial bookmarked status to false
        userDataRepository.updateNewsResourceBookmark(
            newsResourceId = sampleNewsResources.first().id,
            bookmarked = false
        )

        viewModel.bookmarkNews(
            newsResourceId = sampleNewsResources.first().id,
            bookmarked = true
        )

        assertTrue(
            (viewModel.newsUiState.value as NewsUiState.Success)
                .news
                .first { it.newsResource.id == sampleNewsResources.first().id }
                .isSaved
        )

        collectJob.cancel()
    }
}

private const val AUTHOR_1_NAME = "Author 1"
private const val AUTHOR_2_NAME = "Author 2"
private const val AUTHOR_3_NAME = "Author 3"
private const val AUTHOR_BIO = "At vero eos et accusamus."
private const val AUTHOR_TWITTER = "dev"
private const val AUTHOR_MEDIUM_PAGE = "URL"
private const val AUTHOR_IMAGE_URL = "Image URL"

private val testInputAuthors = listOf(
    FollowableAuthor(
        Author(
            id = "0",
            name = AUTHOR_1_NAME,
            bio = AUTHOR_BIO,
            twitter = AUTHOR_TWITTER,
            mediumPage = AUTHOR_MEDIUM_PAGE,
            imageUrl = AUTHOR_IMAGE_URL,
        ),
        isFollowed = true
    ),
    FollowableAuthor(
        Author(
            id = "1",
            name = AUTHOR_2_NAME,
            bio = AUTHOR_BIO,
            twitter = AUTHOR_TWITTER,
            mediumPage = AUTHOR_MEDIUM_PAGE,
            imageUrl = AUTHOR_IMAGE_URL,
        ),
        isFollowed = false
    ),
    FollowableAuthor(
        Author(
            id = "2",
            name = AUTHOR_3_NAME,
            bio = AUTHOR_BIO,
            twitter = AUTHOR_TWITTER,
            mediumPage = AUTHOR_MEDIUM_PAGE,
            imageUrl = AUTHOR_IMAGE_URL,
        ),
        isFollowed = false
    )
)

private val testOutputAuthors = listOf(
    FollowableAuthor(
        Author(
            id = "0",
            name = AUTHOR_1_NAME,
            bio = AUTHOR_BIO,
            twitter = AUTHOR_TWITTER,
            mediumPage = AUTHOR_MEDIUM_PAGE,
            imageUrl = AUTHOR_IMAGE_URL,
        ),
        isFollowed = true
    ),
    FollowableAuthor(
        Author(
            id = "1",
            name = AUTHOR_2_NAME,
            bio = AUTHOR_BIO,
            twitter = AUTHOR_TWITTER,
            mediumPage = AUTHOR_MEDIUM_PAGE,
            imageUrl = AUTHOR_IMAGE_URL,
        ),
        isFollowed = true
    ),
    FollowableAuthor(
        Author(
            id = "2",
            name = AUTHOR_3_NAME,
            bio = AUTHOR_BIO,
            twitter = AUTHOR_TWITTER,
            mediumPage = AUTHOR_MEDIUM_PAGE,
            imageUrl = AUTHOR_IMAGE_URL,
        ),
        isFollowed = false
    )
)

private val sampleNewsResources = listOf(
    NewsResource(
        id = "1",
        title = "Thanks for helping us reach 1M YouTube Subscribers",
        content = "Thank you everyone for following the Now in Android series and everything the " +
            "Android Developers YouTube channel has to offer. During the Android Developer " +
            "Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to " +
            "thank you all.",
        url = "https://youtu.be/-fJ6poHQrjM",
        headerImageUrl = "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
        publishDate = Instant.parse("2021-11-09T00:00:00.000Z"),
        type = Video,
        authors = listOf(
            Author(
                id = "0",
                name = "Android Dev",
                bio = "Hello there!",
                twitter = "dev",
                mediumPage = "URL",
                imageUrl = "image URL",
            )
        ),
        topics = emptyList()
    )
)
