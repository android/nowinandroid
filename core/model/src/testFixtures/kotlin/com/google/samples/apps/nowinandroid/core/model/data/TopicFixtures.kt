/*
 * Copyright 2022 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.model.data

fun fakeTopic(
    id: String,
    name: String = "Topic $id",
    shortDescription: String = "At vero eos et accusamus.",
    longDescription: String = "At vero eos et accusamus et iusto odio dignissimos ducimus qui.",
    url: String = "https://example.org/topic/$id",
    imageUrl: String = "https://example.org/topic/$id.png"
): Topic = Topic(
    id = id,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    url = url,
    imageUrl = imageUrl,
)
