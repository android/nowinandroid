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

package com.google.samples.apps.nowinandroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsStartWidth
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.Posture
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.roundToIntRect
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.window.core.layout.WindowSizeClass
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.test.repository.FakeUserDataRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultRoborazziOptions
import com.google.samples.apps.nowinandroid.uitesthiltmanifest.HiltComponentActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import java.util.TimeZone
import javax.inject.Inject

/**
 * Tests that the Snackbar is correctly displayed on different screen sizes.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// Configure Robolectric to use a very large screen size that can fit all of the test sizes.
// This allows enough room to render the content under test without clipping or scaling.
@Config(application = HiltTestApplication::class, qualifiers = "w1000dp-h1000dp-480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
@HiltAndroidTest
class SnackbarInsetsScreenshotTests {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var timeZoneMonitor: TimeZoneMonitor

    @Inject
    lateinit var userDataRepository: FakeUserDataRepository

    @Inject
    lateinit var topicsRepository: TopicsRepository

    @Inject
    lateinit var userNewsResourceRepository: UserNewsResourceRepository

    @Before
    fun setup() {
        hiltRule.inject()

        // Configure user data
        runBlocking {
            userDataRepository.setShouldHideOnboarding(true)

            userDataRepository.setFollowedTopicIds(
                setOf(topicsRepository.getTopics().first().first().id),
            )
        }
    }

    @Before
    fun setTimeZone() {
        // Make time zone deterministic in tests
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun phone_noSnackbar() {
        val snackbarHostState = SnackbarHostState()
        testSnackbarScreenshotWithSize(
            snackbarHostState,
            400.dp,
            500.dp,
            "insets_snackbar_compact_medium_noSnackbar",
            action = { },
        )
    }

    @Test
    fun snackbarShown_phone() {
        val snackbarHostState = SnackbarHostState()
        testSnackbarScreenshotWithSize(
            snackbarHostState,
            400.dp,
            500.dp,
            "insets_snackbar_compact_medium",
        ) {
            snackbarHostState.showSnackbar(
                "This is a test snackbar message",
                actionLabel = "Action Label",
                duration = Indefinite,
            )
        }
    }

    @Test
    fun snackbarShown_foldable() {
        val snackbarHostState = SnackbarHostState()
        testSnackbarScreenshotWithSize(
            snackbarHostState,
            600.dp,
            600.dp,
            "insets_snackbar_medium_medium",
        ) {
            snackbarHostState.showSnackbar(
                "This is a test snackbar message",
                actionLabel = "Action Label",
                duration = Indefinite,
            )
        }
    }

    @Test
    fun snackbarShown_tablet() {
        val snackbarHostState = SnackbarHostState()
        testSnackbarScreenshotWithSize(
            snackbarHostState,
            900.dp,
            900.dp,
            "insets_snackbar_expanded_expanded",
        ) {
            snackbarHostState.showSnackbar(
                "This is a test snackbar message",
                actionLabel = "Action Label",
                duration = Indefinite,
            )
        }
    }

    private fun testSnackbarScreenshotWithSize(
        snackbarHostState: SnackbarHostState,
        width: Dp,
        height: Dp,
        screenshotName: String,
        action: suspend () -> Unit,
    ) {
        lateinit var scope: CoroutineScope
        composeTestRule.setContent {
            CompositionLocalProvider(
                // Replaces images with placeholders
                LocalInspectionMode provides true,
            ) {
                scope = rememberCoroutineScope()

                DeviceConfigurationOverride(
                    DeviceConfigurationOverride.ForcedSize(DpSize(width, height)),
                ) {
                    DeviceConfigurationOverride(
                        DeviceConfigurationOverride.WindowInsets(
                            WindowInsetsCompat.Builder()
                                .setInsets(
                                    WindowInsetsCompat.Type.statusBars(),
                                    DpRect(
                                        left = 0.dp,
                                        top = 64.dp,
                                        right = 0.dp,
                                        bottom = 0.dp,
                                    ).toInsets(),
                                )
                                .setInsets(
                                    WindowInsetsCompat.Type.navigationBars(),
                                    DpRect(
                                        left = 64.dp,
                                        top = 0.dp,
                                        right = 64.dp,
                                        bottom = 64.dp,
                                    ).toInsets(),
                                )
                                .build(),
                        ),
                    ) {
                        BoxWithConstraints(Modifier.testTag("root")) {
                            NiaTheme {
                                val appState = rememberNiaAppState(
                                    networkMonitor = networkMonitor,
                                    userNewsResourceRepository = userNewsResourceRepository,
                                    timeZoneMonitor = timeZoneMonitor,
                                )
                                NiaApp(
                                    appState = appState,
                                    snackbarHostState = snackbarHostState,
                                    showSettingsDialog = false,
                                    onSettingsDismissed = {},
                                    onTopAppBarActionClick = {},
                                    windowAdaptiveInfo = WindowAdaptiveInfo(
                                        windowSizeClass = WindowSizeClass.compute(
                                            maxWidth.value,
                                            maxHeight.value,
                                        ),
                                        windowPosture = Posture(),
                                    ),
                                )
                                DebugVisibleWindowInsets()
                            }
                        }
                    }
                }
            }
        }

        scope.launch {
            action()
        }

        composeTestRule.onNodeWithTag("root")
            .captureRoboImage(
                "src/testDemo/screenshots/$screenshotName.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }
}

@Composable
fun DebugVisibleWindowInsets(
    modifier: Modifier = Modifier,
    debugColor: Color = Color.Magenta.copy(alpha = 0.5f),
) {
    Box(modifier = modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .windowInsetsStartWidth(WindowInsets.safeDrawing)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
                .background(debugColor),
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .windowInsetsEndWidth(WindowInsets.safeDrawing)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
                .background(debugColor),
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
                .background(debugColor),
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsBottomHeight(WindowInsets.safeDrawing)
                .background(debugColor),
        )
    }
}

@Composable
private fun DpRect.toInsets() = toInsets(LocalDensity.current)

private fun DpRect.toInsets(density: Density) =
    Insets.of(with(density) { toRect() }.roundToIntRect().toAndroidRect())
