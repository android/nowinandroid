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

package com.google.samples.apps.nowinandroid.util

import android.util.Log
import androidx.profileinstaller.ProfileVerifier
import com.google.samples.apps.nowinandroid.core.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Logs the app's Baseline Profile Compilation Status using [ProfileVerifier].
 *
 * When delivering through Google Play, the baseline profile is compiled during installation.
 * In this case you will see the correct state logged without any further action necessary.
 * To verify baseline profile installation locally, you need to manually trigger baseline
 * profile installation.
 *
 * For immediate compilation, call:
 * ```bash
 * adb shell cmd package compile -f -m speed-profile com.example.macrobenchmark.target
 * ```
 * You can also trigger background optimizations:
 * ```bash
 * adb shell pm bg-dexopt-job
 * ```
 * Both jobs run asynchronously and might take some time complete.
 *
 * To see quick turnaround of the ProfileVerifier, we recommend using `speed-profile`.
 * If you don't do either of these steps, you might only see the profile status reported as
 * "enqueued for compilation" when running the sample locally.
 *
 * @see androidx.profileinstaller.ProfileVerifier.CompilationStatus.ResultCode
 */
class ProfileVerifierLogger @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
) {
    companion object {
        private const val TAG = "ProfileInstaller"
    }

    operator fun invoke() = scope.launch {
        val status = ProfileVerifier.getCompilationStatusAsync().await()
        Log.d(TAG, "Status code: ${status.profileInstallResultCode}")
        Log.d(
            TAG,
            when {
                status.isCompiledWithProfile -> "App compiled with profile"
                status.hasProfileEnqueuedForCompilation() -> "Profile enqueued for compilation"
                else -> "Profile not compiled nor enqueued"
            },
        )
    }
}
