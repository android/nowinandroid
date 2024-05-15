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

package com.google.samples.apps.nowinandroid.ui

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.MainActivity
import com.google.samples.apps.nowinandroid.R
import com.google.samples.apps.nowinandroid.core.data.repository.CompositeUserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.rules.GrantPostNotificationsPermissionRule
import com.google.samples.apps.nowinandroid.core.testing.util.TestNetworkMonitor
import com.google.samples.apps.nowinandroid.extensions.getStringById
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.inject.Inject

/**
 * Tests that Snackbar would showing above System UI
 * on different screen size by checking Spacer height.
 */
@HiltAndroidTest
class ConnectSnackBarTest {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Grant [android.Manifest.permission.POST_NOTIFICATIONS] permission.
     */
    @get:Rule(order = 2)
    val postNotificationsPermission = GrantPostNotificationsPermissionRule()

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var userNewsResourceRepository: CompositeUserNewsResourceRepository

    @Inject
    lateinit var timeZoneMonitor: TimeZoneMonitor

    private val networkMonitor: NetworkMonitor = TestNetworkMonitor().apply { setConnected(false) }

    private val forYou by composeTestRule.getStringById(com.google.samples.apps.nowinandroid.feature.foryou.R.string.feature_foryou_title)
    private val interests by composeTestRule.getStringById(com.google.samples.apps.nowinandroid.feature.interests.R.string.feature_interests_title)
    private val saved by composeTestRule.getStringById(com.google.samples.apps.nowinandroid.feature.bookmarks.R.string.feature_bookmarks_title)
    private val netConnected by composeTestRule.getStringById(R.string.not_connected)
    private val bottomPaddingTestTag = "Bottom padding for snackbar"
    private var bottomSafeDrawingHeight: Dp = 0.dp

    @Before
    fun setup() = hiltRule.inject()

    @Test
    fun compactWidth_notConnectedAndForYou_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            400.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(forYou).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag).assertDoesNotExist()

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    @Test
    fun compactWidth_notConnectedAndSaved_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            400.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(saved).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag).assertDoesNotExist()

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    @Test
    fun compactWidth_notConnectedAndInterests_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            400.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(interests).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag).assertDoesNotExist()

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    @Test
    fun mediumWidth_notConnectedAndForYou_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            610.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(forYou).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag)
                .assertExists()
                .assertHeightIsEqualTo(bottomSafeDrawingHeight)

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    @Test
    fun mediumWidth_notConnectedAndSaved_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            610.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(saved).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag)
                .assertExists()
                .assertHeightIsEqualTo(bottomSafeDrawingHeight)

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    @Test
    fun mediumWidth_notConnectedAndInterests_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            610.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(interests).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag)
                .assertExists()
                .assertHeightIsEqualTo(bottomSafeDrawingHeight)

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    @Test
    fun expandedWidth_notConnectedAndForYou_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            900.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(forYou).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag)
                .assertExists()
                .assertHeightIsEqualTo(bottomSafeDrawingHeight)

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    @Test
    fun expandedWidth_notConnectedAndSaved_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            900.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(saved).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag)
                .assertExists()
                .assertHeightIsEqualTo(bottomSafeDrawingHeight)

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    @Test
    fun expandedWidth_notConnectedAndInterests_connectSnackBarShowUp() {
        snackbarScreenWithSize(
            900.dp,
            1000.dp,
        )

        composeTestRule.apply {
            findNavigationButton(interests).apply {
                performClick()
                assertIsSelected()
            }

            onNodeWithTag(bottomPaddingTestTag)
                .assertExists()
                .assertHeightIsEqualTo(bottomSafeDrawingHeight)

            findSnackbarWithMessage(message = netConnected).assertIsDisplayed()
        }
    }

    private fun findSnackbarWithMessage(message: String): SemanticsNodeInteraction =
        composeTestRule.onNode(
            matcher = hasTestTag("Snackbar") and
                hasAnyDescendant(matcher = hasText(message)),
        )

    private fun findNavigationButton(string: String): SemanticsNodeInteraction =
        composeTestRule.onNode(matcher = isSelectable() and hasText(string))

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    private fun fakeAppState(maxWidth: Dp, maxHeight: Dp) = rememberNiaAppState(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
        networkMonitor = networkMonitor,
        userNewsResourceRepository = userNewsResourceRepository,
        timeZoneMonitor = timeZoneMonitor,
    )

    private fun snackbarScreenWithSize(
        width: Dp,
        height: Dp,
    ) {
        composeTestRule.activity.apply {
            setContent {
                DeviceConfigurationOverride(
                    DeviceConfigurationOverride.ForcedSize(DpSize(width, height)),
                ) {
                    BoxWithConstraints {
                        val density = LocalDensity.current
                        bottomSafeDrawingHeight =
                            density.run {
                                WindowInsets.safeDrawing.getBottom(density = density).toDp()
                            }
                        NiaApp(
                            appState = fakeAppState(maxWidth, maxHeight),
                        )
                    }
                }
            }
        }
    }
}
