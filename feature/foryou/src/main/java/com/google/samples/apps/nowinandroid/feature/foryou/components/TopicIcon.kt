/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.google.samples.apps.nowinandroid.feature.foryou.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.ui.DevicePreviews
import com.google.samples.apps.nowinandroid.feature.foryou.R.drawable

@Composable
internal fun TopicIcon(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        // TODO b/228077205, show loading image visual instead of static placeholder
        placeholder = painterResource(drawable.ic_icon_placeholder),
        model = imageUrl,
        contentDescription = null, // decorative
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        modifier = modifier
            .padding(10.dp)
            .size(32.dp)
    )
}

@DevicePreviews
@Composable
fun TopicIconPreview() {
    TopicIcon(imageUrl = "https://picsum.photos/200")
}