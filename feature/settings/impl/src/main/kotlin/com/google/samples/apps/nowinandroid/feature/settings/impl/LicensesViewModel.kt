/*
 * Copyright 2025 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.feature.settings.impl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import org.json.JSONArray
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LicensesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val licensesUiState: StateFlow<LicensesUiState> = flow {
        emit(LicensesUiState.Success(parseLicensesJson(context)))
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = LicensesUiState.Loading,
        )

    private fun parseLicensesJson(context: Context): List<LicenseArtifact> {
        return try {
            val json = context.assets.open("licenses.json").bufferedReader().use { it.readText() }
            val array = JSONArray(json)
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                val spdxLicenses = obj.optJSONArray("spdxLicenses")
                val unknownLicenses = obj.optJSONArray("unknownLicenses")
                val licenses = mutableListOf<LicenseInfo>()

                if (spdxLicenses != null) {
                    for (j in 0 until spdxLicenses.length()) {
                        val license = spdxLicenses.getJSONObject(j)
                        licenses.add(
                            LicenseInfo(
                                name = license.optString("name", ""),
                                url = license.optString("url", ""),
                            ),
                        )
                    }
                }
                if (unknownLicenses != null) {
                    for (j in 0 until unknownLicenses.length()) {
                        val license = unknownLicenses.getJSONObject(j)
                        licenses.add(
                            LicenseInfo(
                                name = license.optString("name", "Unknown"),
                                url = license.optString("url", ""),
                            ),
                        )
                    }
                }

                LicenseArtifact(
                    groupId = obj.optString("groupId", ""),
                    artifactId = obj.optString("artifactId", ""),
                    version = obj.optString("version", ""),
                    name = if (obj.has("name")) obj.getString("name") else null,
                    licenses = licenses,
                )
            }.sortedBy { (it.name ?: "${it.groupId}:${it.artifactId}").lowercase() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

sealed interface LicensesUiState {
    data object Loading : LicensesUiState
    data class Success(val artifacts: List<LicenseArtifact>) : LicensesUiState
}
