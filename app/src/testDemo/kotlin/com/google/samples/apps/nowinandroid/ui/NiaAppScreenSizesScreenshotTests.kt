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

import androidx.compose.material3.adaptive.Posture
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.CompositionLocalProvider
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
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultRoborazziOptions
import com.google.samples.apps.nowinandroid.core.data.test.testDataModule
import com.google.samples.apps.nowinandroid.core.domain.di.domainModule
import com.google.samples.apps.nowinandroid.core.analytics.analyticsModule
import com.google.samples.apps.nowinandroid.core.data.test.testScopeModule
import com.google.samples.apps.nowinandroid.core.datastore.test.testDataStoreModule
import com.google.samples.apps.nowinandroid.core.testing.di.testDispatchersModule
import com.google.samples.apps.nowinandroid.core.testing.rule.SafeKoinTestRule
import com.google.samples.apps.nowinandroid.di.appModule
import com.google.samples.apps.nowinandroid.di.featureModules
import com.google.samples.apps.nowinandroid.core.sync.test.testSyncModule
import com.google.samples.apps.nowinandroid.core.testing.KoinTestApplication
import org.koin.test.KoinTest
import kotlinx.coroutines.flow.first
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
import org.koin.core.component.inject

/**
 * Tests that the navigation UI is rendered correctly on different screen sizes.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// Configure Robolectric to use a very large screen size that can fit all of the test sizes.
// This allows enough room to render the content under test without clipping or scaling.
@Config(application = KoinTestApplication::class, qualifiers = "w1000dp-h1000dp-480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class NiaAppScreenSizesScreenshotTests : KoinTest {

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
            appModule,
            testSyncModule,
            featureModules,
        )
    )

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val networkMonitor: NetworkMonitor by inject()
    private val timeZoneMonitor: TimeZoneMonitor by inject()
    private val userDataRepository: UserDataRepository by inject()
    private val topicsRepository: TopicsRepository by inject()
    private val userNewsResourceRepository: UserNewsResourceRepository by inject()

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

    private fun testNiaAppScreenshotWithSize(width: Dp, height: Dp, screenshotName: String) {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                DeviceConfigurationOverride(
                    override = DeviceConfigurationOverride.ForcedSize(DpSize(width, height)),
                ) {
                    NiaTheme {
                        val fakeAppState = rememberNiaAppState(
                            networkMonitor = networkMonitor,
                            userNewsResourceRepository = userNewsResourceRepository,
                            timeZoneMonitor = timeZoneMonitor,
                        )
                        NiaApp(
                            fakeAppState,
                            windowAdaptiveInfo = WindowAdaptiveInfo(
                                windowSizeClass = WindowSizeClass.compute(
                                    width.value,
                                    height.value,
                                ),
                                windowPosture = Posture(),
                            ),
                        )
                    }
                }
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/testDemo/screenshots/$screenshotName.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Test
    fun compactWidth_compactHeight_showsNavigationBar() {
        testNiaAppScreenshotWithSize(
            400.dp,
            400.dp,
            "compactWidth_compactHeight_showsNavigationBar",
        )
    }

    @Test
    fun mediumWidth_compactHeight_showsNavigationBar() {
        testNiaAppScreenshotWithSize(
            610.dp,
            400.dp,
            "mediumWidth_compactHeight_showsNavigationBar",
        )
    }

    @Test
    fun expandedWidth_compactHeight_showsNavigationBar() {
        testNiaAppScreenshotWithSize(
            900.dp,
            400.dp,
            "expandedWidth_compactHeight_showsNavigationBar",
        )
    }

    @Test
    fun compactWidth_mediumHeight_showsNavigationBar() {
        testNiaAppScreenshotWithSize(
            400.dp,
            500.dp,
            "compactWidth_mediumHeight_showsNavigationBar",
        )
    }

    @Test
    fun mediumWidth_mediumHeight_showsNavigationRail() {
        testNiaAppScreenshotWithSize(
            610.dp,
            500.dp,
            "mediumWidth_mediumHeight_showsNavigationRail",
        )
    }

    @Test
    fun expandedWidth_mediumHeight_showsNavigationRail() {
        testNiaAppScreenshotWithSize(
            900.dp,
            500.dp,
            "expandedWidth_mediumHeight_showsNavigationRail",
        )
    }

    @Test
    fun compactWidth_expandedHeight_showsNavigationBar() {
        testNiaAppScreenshotWithSize(
            400.dp,
            1000.dp,
            "compactWidth_expandedHeight_showsNavigationBar",
        )
    }

    @Test
    fun mediumWidth_expandedHeight_showsNavigationRail() {
        testNiaAppScreenshotWithSize(
            610.dp,
            1000.dp,
            "mediumWidth_expandedHeight_showsNavigationRail",
        )
    }

    @Test
    fun expandedWidth_expandedHeight_showsNavigationRail() {
        testNiaAppScreenshotWithSize(
            900.dp,
            1000.dp,
            "expandedWidth_expandedHeight_showsNavigationRail",
        )
    }
}
