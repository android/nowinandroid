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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.feature.settings.impl.R.string
import org.json.JSONArray

data class LicenseArtifact(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val name: String?,
    val licenses: List<LicenseInfo>,
)

data class LicenseInfo(
    val name: String,
    val url: String,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LicensesScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val artifacts = remember { parseLicensesJson(context) }
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(string.feature_settings_impl_licenses)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = NiaIcons.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            items(artifacts, key = { "${it.groupId}:${it.artifactId}" }) { artifact ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = artifact.name
                                ?: "${artifact.groupId}:${artifact.artifactId}",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = "${artifact.groupId}:${artifact.artifactId}:${artifact.version}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (artifact.licenses.isNotEmpty()) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                artifact.licenses.forEach { license ->
                                    SuggestionChip(
                                        onClick = {
                                            if (license.url.isNotBlank()) {
                                                uriHandler.openUri(license.url)
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = license.name,
                                                style = MaterialTheme.typography.labelSmall,
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

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
