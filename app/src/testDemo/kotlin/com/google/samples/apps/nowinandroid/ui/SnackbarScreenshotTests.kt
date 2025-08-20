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

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.Posture
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.samples.apps.nowinandroid.core.data.repository.TopicsRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserDataRepository
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.test.testDataModule
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.domain.di.domainModule
import com.google.samples.apps.nowinandroid.core.testing.di.testDispatchersModule
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultRoborazziOptions
import org.koin.test.KoinTest
import com.google.samples.apps.nowinandroid.core.analytics.analyticsModule
import com.google.samples.apps.nowinandroid.core.data.test.testScopeModule
import com.google.samples.apps.nowinandroid.core.datastore.test.testDataStoreModule
import com.google.samples.apps.nowinandroid.core.testing.rule.SafeKoinTestRule
import com.google.samples.apps.nowinandroid.di.appModule
import com.google.samples.apps.nowinandroid.di.featureModules
import com.google.samples.apps.nowinandroid.core.sync.test.testSyncModule
import com.google.samples.apps.nowinandroid.core.testing.KoinTestApplication
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
import org.koin.core.component.get

/**
 * Tests that the Snackbar is correctly displayed on different screen sizes.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// Configure Robolectric to use a very large screen size that can fit all of the test sizes.
// This allows enough room to render the content under test without clipping or scaling.
@Config(application = KoinTestApplication::class, qualifiers = "w1000dp-h1000dp-480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class SnackbarScreenshotTests : KoinTest {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val koinTestRule = SafeKoinTestRule.create(
        modules = listOf(
            testDataModule,
            testDataStoreModule,
            testNetworkModule,
            domainModule,
            testDispatchersModule,
            testScopeModule,
            analyticsModule,
            testSyncModule,
            appModule,
            featureModules,
        )
    )

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val networkMonitor: NetworkMonitor by lazy { get() }
    private val timeZoneMonitor: TimeZoneMonitor by lazy { get() }
    private val userDataRepository: UserDataRepository by lazy { get() }
    private val topicsRepository: TopicsRepository by lazy { get() }
    private val userNewsResourceRepository: UserNewsResourceRepository by lazy { get() }

    @Before
    fun setup() {        
        // Configure user data
        runBlocking {
            val fakeUserDataRepository = userDataRepository as com.google.samples.apps.nowinandroid.core.data.test.repository.FakeUserDataRepository
            fakeUserDataRepository.setShouldHideOnboarding(true)

            fakeUserDataRepository.setFollowedTopicIds(
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
            "snackbar_compact_medium_noSnackbar",
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
            "snackbar_compact_medium",
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
            "snackbar_medium_medium",
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
            "snackbar_expanded_expanded",
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
                    BoxWithConstraints {
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
                        }
                    }
                }
            }
        }

        scope.launch {
            action()
        }

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/testDemo/screenshots/$screenshotName.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

}
