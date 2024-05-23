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

import androidx.test.core.app.takeScreenshot
import androidx.test.espresso.device.common.executeShellCommand
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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@HiltAndroidTest
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
            executeShellCommand("cmd overlay enable-exclusive com.android.internal.systemui.navbar.threebutton")
            executeShellCommand("settings put global sysui_demo_allowed 1")
            executeShellCommand("am broadcast -a com.android.systemui.demo -e command enter")
            executeShellCommand("am broadcast -a com.android.systemui.demo -e command notifications -e visible false")
            executeShellCommand("am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1234")
            executeShellCommand("am broadcast -a com.android.systemui.demo -e command network -e wifi hide")
            executeShellCommand("am broadcast -a com.android.systemui.demo -e command network -e mobile hide")
        }
    }

    @After
    fun resetDemoMode() {
        executeShellCommand("am broadcast -a com.android.systemui.demo -e command exit")
    }

    @RequiresDisplay(WidthSizeClassEnum.COMPACT, HeightSizeClassEnum.MEDIUM)
    @SdkSuppress(minSdkVersion = 27, maxSdkVersion = 27)
    @Test
    fun edgeToEdge_Phone_Api27() {
        testEdgeToEdge("edgeToEdge_Phone_Api27")
    }

    @RequiresDisplay(WidthSizeClassEnum.COMPACT, HeightSizeClassEnum.MEDIUM)
    @SdkSuppress(minSdkVersion = 31, maxSdkVersion = 31)
    @Test
    fun edgeToEdge_Phone_Api31() {
        testEdgeToEdge("edgeToEdge_Phone_Api31")
    }

    @RequiresDisplay(WidthSizeClassEnum.EXPANDED, HeightSizeClassEnum.MEDIUM)
    @SdkSuppress(minSdkVersion = 30, maxSdkVersion = 30)
    @Test
    fun edgeToEdge_Tablet_Api30() {
        testEdgeToEdge("edgeToEdge_Tablet_Api30")
    }

    private fun testEdgeToEdge(screenshotFileName: String) {
        dropshots.assertSnapshot(takeScreenshot(), screenshotFileName)
    }
}
