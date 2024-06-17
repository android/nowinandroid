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

import com.google.samples.apps.nowinandroid.core.data.util.ErrorMessage
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.SnackbarErrorMonitor
import com.google.samples.apps.nowinandroid.core.testing.util.TestNetworkMonitor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [SnackbarErrorMonitor].
 */

class SnackbarErrorMonitorTest {

    lateinit var networkMonitor: NetworkMonitor

    // Subject under test.
    private lateinit var state: SnackbarErrorMonitor

    private var message: ErrorMessage? = null

    @Before
    fun setup() {
        networkMonitor = TestNetworkMonitor()
        state = SnackbarErrorMonitor(networkMonitor)
    }

    @Test
    fun whenErrorIsNotAdded_NullIsPresent() = runTest(UnconfinedTestDispatcher()) {
        backgroundScope.launch { state.errorMessage.collect() }
        assertEquals(
            null,
            message,
        )
    }

    @Test
    fun whenErrorIsAdded_ErrorMessageIsPresent() = runTest(UnconfinedTestDispatcher()) {
        backgroundScope.launch {
            state.errorMessage.collect {
                message = it
            }
        }

        val id = state.addShortErrorMessage("Test Error Message")

        assertEquals(
            id,
            message?.id,
        )
    }

    @Test
    fun whenErrorsAreAdded_FirstErrorMessageIsPresent() =
        runTest(UnconfinedTestDispatcher()) {
            val id1 = state.addShortErrorMessage("Test Error Message 1")
            state.addShortErrorMessage("Test Error Message 2")

            backgroundScope.launch {
                state.errorMessage.collect {
                    message = it
                }
            }

            assertEquals(
                id1,
                message?.id,
            )
        }

    @Test
    fun whenErrorIsCleared_ErrorMessageIsNotPresent() =
        runTest(UnconfinedTestDispatcher()) {
            backgroundScope.launch {
                state.errorMessage.collect {
                    message = it
                }
            }
            val id = state.addShortErrorMessage("Test Error Message 1")
            if (id != null) {
                state.clearErrorMessage(id)
                assertEquals(
                    null,
                    message,
                )
            }
        }

    @Test
    fun whenErrorsAreCleared_NextErrorMessageIsPresent() =
        runTest(UnconfinedTestDispatcher()) {
            backgroundScope.launch {
                state.errorMessage.collect {
                    message = it
                }
            }
            val id1 = state.addShortErrorMessage("Test Error Message 1")
            val id2 = state.addShortErrorMessage("Test Error Message 2")
            if (id1 != null) {
                state.clearErrorMessage(id1)
                assertEquals(
                    id2,
                    message?.id,
                )
            }
        }
}
