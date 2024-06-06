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

import android.graphics.Bitmap
import android.util.Log
import androidx.test.core.app.takeScreenshot
import androidx.test.espresso.device.DeviceInteraction.Companion.setClosedMode
import androidx.test.espresso.device.DeviceInteraction.Companion.setFlatMode
import androidx.test.espresso.device.DeviceInteraction.Companion.setScreenOrientation
import androidx.test.espresso.device.EspressoDevice.Companion.onDevice
import androidx.test.espresso.device.action.ScreenOrientation.LANDSCAPE
import androidx.test.espresso.device.action.ScreenOrientation.PORTRAIT
import androidx.test.espresso.device.common.executeShellCommand
import androidx.test.espresso.device.controller.DeviceMode.CLOSED
import androidx.test.espresso.device.controller.DeviceMode.FLAT
import androidx.test.espresso.device.filter.RequiresDeviceMode
import androidx.test.espresso.device.filter.RequiresDisplay
import androidx.test.espresso.device.sizeclass.HeightSizeClass.Companion.HeightSizeClassEnum
import androidx.test.espresso.device.sizeclass.WidthSizeClass.Companion.WidthSizeClassEnum
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.dropbox.dropshots.Dropshots
import com.google.samples.apps.nowinandroid.MainActivity
import com.google.samples.apps.nowinandroid.core.rules.GrantPostNotificationsPermissionRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.AfterClass
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * These tests must be run on the following devices:
 *  - A phone on API 27 (pixel_5)
 *  - A foldable on API 33 (pixel_fold)
 *  - A foldable on API 35 (pixel_fold)
 */
@HiltAndroidTest
@InstrumentedScreenshotTests
class EdgeToEdgeTest {
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
     * Grant [android.Manifest.permission.POST_NOTIFICATIONS] permission to prevent the
     * permissions dialog from showing on top.
     */
    @get:Rule(order = 2)
    val postNotificationsPermission = GrantPostNotificationsPermissionRule()

    @get:Rule(order = 3)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 4)
    val dropshots = Dropshots()

    @Before
    fun setup() = hiltRule.inject()

    @Before
    fun enableDemoMode() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).apply {
            executeShellCommand("settings put global sysui_demo_allowed 1")
            executeShellCommand(
                "am broadcast -a com.android.systemui.demo -e command " +
                    "enter",
            )
            executeShellCommand(
                "am broadcast -a com.android.systemui.demo -e command " +
                    "notifications -e visible false",
            )
            executeShellCommand(
                "am broadcast -a com.android.systemui.demo -e command " +
                    "clock -e hhmm 1234",
            )
            executeShellCommand(
                "am broadcast -a com.android.systemui.demo -e command " +
                    "network -e wifi hide",
            )
            executeShellCommand(
                "am broadcast -a com.android.systemui.demo -e command " +
                    "network -e mobile hide",
            )
        }
    }

    companion object {
        @JvmStatic
        @AfterClass
        fun resetDemoMode() {
            executeShellCommand("am broadcast -a com.android.systemui.demo -e command exit")
        }
    }

    @RequiresDisplay(WidthSizeClassEnum.COMPACT, HeightSizeClassEnum.MEDIUM)
    @SdkSuppress(minSdkVersion = 27, maxSdkVersion = 27)
    @Test
    fun edgeToEdge_Phone_Api27() {
        screenshotSystemBar("edgeToEdge_Phone_systemBar_Api27")
        screenshotNavigationBar("edgeToEdge_Phone_navBar_Api27")
    }

    @RequiresDeviceMode(mode = FLAT)
    @RequiresDeviceMode(mode = CLOSED)
    @SdkSuppress(minSdkVersion = 33, maxSdkVersion = 33)
    @Test
    fun edgeToEdge_Foldable_api33() {
        runFoldableTests(apiName = "api33")
    }

    @RequiresDeviceMode(mode = FLAT)
    @RequiresDeviceMode(mode = CLOSED)
    @SdkSuppress(minSdkVersion = 35, codeName = "VanillaIceCream")
    @Test
    fun edgeToEdge_Foldable_api35() {
        runFoldableTests(apiName = "api35")
    }

    @RequiresDeviceMode(mode = FLAT)
    @RequiresDeviceMode(mode = CLOSED)
    @SdkSuppress(minSdkVersion = 35, codeName = "VanillaIceCream")
    @Test
    fun edgeToEdge_Foldable_api35_tallCutout() {
        forceTallCutout()
        onDevice().setClosedMode()
        enableDemoMode() // Mode change resets demo mode!
        screenshotSystemBar("edgeToEdge_Foldable_closed_system_tallCutout_api35")

        onDevice().setFlatMode()
        enableDemoMode() // Mode change resets demo mode!
        screenshotSystemBar("edgeToEdge_Foldable_flat_system_tallCutout_api35")

        onDevice().setClosedMode()
        resetCutout()
    }

    // Very flaky:DeviceControllerOperationException: Device could not be set to the
    // requested screen orientation.

    @RequiresDeviceMode(mode = CLOSED)
    @SdkSuppress(minSdkVersion = 35, codeName = "VanillaIceCream")
    @Test
    fun edgeToEdge_Foldable_api35_landscape() {
        onDevice().setClosedMode()
        onDevice().setScreenOrientation(LANDSCAPE)
        forceThreeButtonNavigation()
        screenshotSideNavigationBar("edgeToEdge_Foldable_landscape_sideNav3button_35")
        onDevice().setScreenOrientation(PORTRAIT)
    }

    private fun runFoldableTests(apiName: String) {
        resetCutout()
        onDevice().setClosedMode()
        screenshotSystemBar("edgeToEdge_Foldable_closed_system_$apiName")
        forceThreeButtonNavigation()
        screenshotNavigationBar("edgeToEdge_Foldable_closed_nav3button_$apiName")
        forceGestureNavigation()
        screenshotNavigationBar("edgeToEdge_Foldable_closed_navGesture_$apiName")

        onDevice().setFlatMode()
        enableDemoMode() // Flat mode resets demo mode!
        screenshotSystemBar("edgeToEdge_Foldable_flat_system_$apiName")
        forceThreeButtonNavigation()
        screenshotNavigationBar("edgeToEdge_Foldable_flat_nav3button_$apiName")
        forceGestureNavigation()
        screenshotNavigationBar("edgeToEdge_Foldable_flat_navGesture_$apiName")
        onDevice().setClosedMode()
    }

    private fun screenshotSystemBar(screenshotFileName: String) {
        waitForWindowUpdate()

        // Crop the top, adding extra pixels to check continuity
        val bitmap = takeScreenshot().let {
            val newHeight = 200
            Bitmap.createBitmap(it, 0, 0, it.width, newHeight)
        }
        assertSnapshot(bitmap, screenshotFileName)
    }

    private fun screenshotNavigationBar(screenshotFileName: String) {
        waitForWindowUpdate()

        // Crop the bottom, adding extra pixels to check continuity
        val bitmap = takeScreenshot().let {
            val newHeight = 200
            Bitmap.createBitmap(it, 0, it.height - newHeight, it.width, newHeight)
        }
        assertSnapshot(bitmap, screenshotFileName)
    }

    private fun screenshotSideNavigationBar(screenshotFileName: String) {
        waitForWindowUpdate()

        // Crop the top, adding extra pixels to check continuity
        val bitmap = takeScreenshot().let {
            Bitmap.createBitmap(it, it.width - 250, 0, 250, it.height)
        }
        assertSnapshot(bitmap, screenshotFileName)
    }

    private fun forceThreeButtonNavigation() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).apply {
            executeShellCommand(
                "cmd overlay enable-exclusive " +
                    "com.android.internal.systemui.navbar.threebutton",
            )
        }
        waitForWindowUpdate()
    }

    private fun forceGestureNavigation() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).apply {
            executeShellCommand(
                "cmd overlay enable-exclusive " +
                    "com.android.internal.systemui.navbar.gestural",
            )
        }
        waitForWindowUpdate()
    }

    private fun forceTallCutout() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).apply {
            executeShellCommand(
                "cmd overlay enable-exclusive " +
                    "--category com.android.internal.display.cutout.emulation.tall ",
            )
        }
        waitForWindowUpdate()
    }
    private fun resetCutout() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).apply {
            executeShellCommand(
                "cmd overlay enable-exclusive " +
                    "--category com.android.internal.display.cutout.emulation.noCutout ",
            )
        }
        waitForWindowUpdate()
    }

    private fun waitForWindowUpdate() {
        // TODO: This works but it's unclear if it's making it wait too long. Investigate.
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .waitForWindowUpdate(
                InstrumentationRegistry.getInstrumentation().targetContext.packageName,
                8000,
            )
    }

    private fun assertSnapshot(
        bitmap: Bitmap,
        name: String,
        filePath: String? = null,
    ) {
        // Try to assert 3 times
        var count = 2
        while (true) {
            try {
                dropshots.assertSnapshot(bitmap, name, filePath)
            } catch (e: AssertionError) {
                if (count == 0) throw e
                count -= 1
                Log.i("EdgeToEdgeTest", "Test failed, retrying (count=$count)")
                waitForWindowUpdate()
                continue
            }
            break
        }
    }
}
