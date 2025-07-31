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

package com.google.samples.apps.nowinandroid.core.ui
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme

@Preview
@Composable
fun InterestsCardPreview() {
    NiaTheme {
        Surface {
            InterestsItem(
                name = "Compose",
                description = "Description",
                following = false,
                topicImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
            )
        }
    }
}

@Preview
@Composable
fun InterestsCardLongNamePreview() {
    NiaTheme {
        Surface {
            InterestsItem(
                name = "This is a very very very very long name",
                description = "Description",
                following = true,
                topicImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
            )
        }
    }
}

@Preview
@Composable
fun InterestsCardLongDescriptionPreview() {
    NiaTheme {
        Surface {
            InterestsItem(
                name = "Compose",
                description = "This is a very very very very very very very " +
                    "very very very long description",
                following = false,
                topicImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
            )
        }
    }
}

@Preview
@Composable
fun InterestsCardWithEmptyDescriptionPreview() {
    NiaTheme {
        Surface {
            InterestsItem(
                name = "Compose",
                description = "",
                following = true,
                topicImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
            )
        }
    }
}

@Preview
@Composable
fun InterestsCardSelectedPreview() {
    NiaTheme {
        Surface {
            InterestsItem(
                name = "Compose",
                description = "",
                following = true,
                topicImageUrl = "",
                onClick = { },
                onFollowButtonClick = { },
                isSelected = true,
            )
        }
    }
}
