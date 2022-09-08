/*
 * Copyright 2021 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.network.fake

import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Codelab
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.intellij.lang.annotations.Language

/* ktlint-disable max-line-length */

object FakeDataSource {
    val sampleTopic = NetworkTopic(
        id = "2",
        name = "Headlines",
        shortDescription = "News we want everyone to see",
        longDescription = "Stay up to date with the latest events and announcements from Android!",
        url = "",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Headlines.svg?alt=media&token=506faab0-617a-4668-9e63-4a2fb996603f"
    )
    val sampleResource = NetworkNewsResource(
        id = "1",
        episodeId = "60",
        title = "Android Basics with Compose",
        content = "We released the first two units of Android Basics with Compose, our first free course that teaches Android Development with Jetpack Compose to anyone; you do not need any prior programming experience other than basic computer literacy to get started. You’ll learn the fundamentals of programming in Kotlin while building Android apps using Jetpack Compose, Android’s modern toolkit that simplifies and accelerates native UI development. These two units are just the beginning; more will be coming soon. Check out Android Basics with Compose to get started on your Android development journey",
        url = "https://android-developers.googleblog.com/2022/05/new-android-basics-with-compose-course.html",
        headerImageUrl = "https://developer.android.com/images/hero-assets/android-basics-compose.svg",
        authors = listOf("25"),
        publishDate = LocalDateTime(
            year = 2022,
            monthNumber = 5,
            dayOfMonth = 4,
            hour = 23,
            minute = 0,
            second = 0,
            nanosecond = 0
        ).toInstant(TimeZone.UTC),
        type = Codelab,
        topics = listOf("3", "11", "10"),
    )

    @Language("JSON")
    val topicsData = """
[
    {
      "id": "2",
      "name": "Headlines",
      "shortDescription": "News we want everyone to see",
      "longDescription": "Stay up to date with the latest events and announcements from Android!",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Headlines.svg?alt=media&token=506faab0-617a-4668-9e63-4a2fb996603f",
      "url": ""
    },
    {
      "id": "3",
      "name": "UI",
      "shortDescription": "Material Design, Navigation, Text, Paging, Accessibility (a11y), Internationalization (i18n), Localization (l10n), Animations, Large Screens, Widgets",
      "longDescription": "Learn how to optimize your app's user interface - everything that users can see and interact with. Stay up to date on tocpis such as Material Design, Navigation, Text, Paging, Compose, Accessibility (a11y), Internationalization (i18n), Localization (l10n), Animations, Large Screens, Widgets, and many more!",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_UI.svg?alt=media&token=0ee1842b-12e8-435f-87ba-a5bb02c47594",
      "url": ""
    },
    {
      "id": "4",
      "name": "Testing",
      "shortDescription": "CI, Espresso, TestLab, etc",
      "longDescription": "Testing is an integral part of the app development process. By running tests against your app consistently, you can verify your app's correctness, functional behavior, and usability before you release it publicly. Stay up to date on the latest tricks in CI, Espresso, and Firebase TestLab.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Testing.svg?alt=media&token=a11533c4-7cc8-4b11-91a3-806158ebf428",
      "url": ""
    },
    {
      "id": "5",
      "name": "Performance",
      "shortDescription": "Optimization, profiling",
      "longDescription": "Topics here will try to optimize your app perfoamnce by profiling and identifying areas in which your app makes inefficient use of resources such as the CPU, memory, graphics, network, or the device battery.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Performance.svg?alt=media&token=558fdf02-1918-4527-b13f-323db67e31cc",
      "url": ""
    },
    {
      "id": "6",
      "name": "Camera & Media",
      "shortDescription": "",
      "longDescription": "Learn about Android's robust APIs for playing and recording media, help add video, audio, and photo capabilities to your app!",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Camera-_-Media.svg?alt=media&token=73adea20-20d4-4f4c-8f3b-eb47c1097496",
      "url": ""
    },
    {
      "id": "7",
      "name": "Android Studio",
      "shortDescription": "",
      "longDescription": "Android Studio is the official integrated development environment (IDE) for Android development. It provides the fastest tools for building apps on every type of Android device.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Android-Studio.svg?alt=media&token=b28b82dc-5aa1-4098-9eff-deb04636d3ac",
      "url": ""
    },
    {
      "id": "8",
      "name": "New APIs & Libraries",
      "shortDescription": "New Jetpack libraries",
      "longDescription": "Stay up to date with the latest new APIs & libraires",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_New-APIs-_-Libraries.svg?alt=media&token=8efd12df-6dd9-4b1b-81fd-017a49a866ac",
      "url": ""
    },
    {
      "id": "9",
      "name": "Data Storage",
      "shortDescription": "Room, Data Store",
      "longDescription": "Android uses a file system that's similar to disk-based file systems on other platforms. The system provides several options for you to save your app data: App-specific storage, shared storage, preferences, and databases - learn about Room and Data Store!",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Data-Storage.svg?alt=media&token=c9f78039-f371-4ce1-ba82-2c0c1e20d180",
      "url": ""
    },
    {
      "id": "10",
      "name": "Kotlin",
      "shortDescription": "",
      "longDescription": "Kotlin is a modern statically typed programming language used by over 60% of professional Android developers that helps boost productivity, developer satisfaction, and code safety.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Kotlin.svg?alt=media&token=bdc73380-e80d-47df-8954-d9b61cccacd2",
      "url": ""
    },
    {
      "id": "11",
      "name": "Compose",
      "shortDescription": "",
      "longDescription": "Jetpack Compose is Android’s modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Compose.svg?alt=media&token=9f0228e8-fdf2-45ee-9fd0-7e51fda23b48",
      "url": ""
    },
    {
      "id": "12",
      "name": "Privacy & Security",
      "shortDescription": "Privacy, Security",
      "longDescription": "Learn about best practices and resources to help developers design and implement safe, secure, and private apps.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Privacy-_-Security.svg?alt=media&token=6232fd17-c1cc-43b3-bf70-a734323fa6df",
      "url": ""
    },
    {
      "id": "13",
      "name": "Publishing & Distribution",
      "shortDescription": "Google Play",
      "longDescription": "Learn about Google Play publish and distrubution system to make your Android applications available to users.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Publishing-_-Distribution.svg?alt=media&token=64a5aeaf-269a-479d-8a44-29f59d337dbf",
      "url": ""
    },
    {
      "id": "14",
      "name": "Tools",
      "shortDescription": "Gradle, Memory Safety, Debugging",
      "longDescription": "Android Studio, Compose tooling, APK Analyzer, Fast emulator, Intelligent code editor, Flexible build system, Realtime profilers, Gradle, Memory Safety, Debugging",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Tools.svg?alt=media&token=4b5fe6c1-c2c2-4f3c-af34-4a0694d2a2a7",
      "url": ""
    },
    {
      "id": "15",
      "name": "Platform & Releases",
      "shortDescription": "Android 12, Android 13, etc",
      "longDescription": "Stay up to date with the latest Android releases and features!",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Platform-_-Releases.svg?alt=media&token=ff6d7a38-5205-4a51-8b6a-721e665dc515",
      "url": ""
    },
    {
      "id": "16",
      "name": "Architecture",
      "shortDescription": "Lifecycle, Dependency Injection, WorkManager",
      "longDescription": "Lifecycle, Dependency Injection, WorkManager",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Architecture.svg?alt=media&token=e69ed228-fa91-49ae-9017-c8b7331f4269",
      "url": ""
    },
    {
      "id": "17",
      "name": "Accessibility",
      "shortDescription": "",
      "longDescription": "Accessibility is an important part of any app. Whether you're developing a new app or improving an existing one, consider the accessibility of your app's components.\n\nBy integrating accessibility features and services, you can improve your app's usability, particularly for users with disabilities.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Accessibility.svg?alt=media&token=5b783a03-dd3b-4d0c-9e0c-16ae8350295f",
      "url": ""
    },
    {
      "id": "18",
      "name": "Android Auto",
      "shortDescription": "",
      "longDescription": "Lean about how to build apps that help users connect on the road through Android Automotive OS and Android Auto",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Android-Auto.svg?alt=media&token=56453754-14a5-4953-b596-66d63c56c196",
      "url": ""
    },
    {
      "id": "19",
      "name": "Android TV",
      "shortDescription": "",
      "longDescription": "Learn about how to build a great user experience for your TV app: create immersive content on the big screen and for a remote control",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Android-TV.svg?alt=media&token=a78ca0df-f1ba-44a6-a89d-3912c82ef661",
      "url": ""
    },
    {
      "id": "20",
      "name": "Games",
      "shortDescription": "",
      "longDescription": "Learn about new tools and best practices to support your game app development and game performance.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Games.svg?alt=media&token=4effa537-cc42-4d7f-b6bd-f1f14568db07",
      "url": ""
    },
    {
      "id": "21",
      "name": "Wear OS",
      "shortDescription": "",
      "longDescription": "Learn about new tools and best practices to support your Wear OS development and watch performance.",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Wear.svg?alt=media&token=bd11fe4c-9c92-4536-8ebc-5210f44d09be",
      "url": ""
    }
  ]
    """.trimIndent()

    @Language("JSON")
    val data = """
[
    {
      "id": "1",
      "episodeId": "60",
      "title": "Android Basics with Compose",
      "content": "We released the first two units of Android Basics with Compose, our first free course that teaches Android Development with Jetpack Compose to anyone; you do not need any prior programming experience other than basic computer literacy to get started. You’ll learn the fundamentals of programming in Kotlin while building Android apps using Jetpack Compose, Android’s modern toolkit that simplifies and accelerates native UI development. These two units are just the beginning; more will be coming soon. Check out Android Basics with Compose to get started on your Android development journey",
      "url": "https://android-developers.googleblog.com/2022/05/new-android-basics-with-compose-course.html",
      "headerImageUrl": "https://developer.android.com/images/hero-assets/android-basics-compose.svg",
      "publishDate": "2022-05-04T23:00:00.000Z",
      "type": "Codelab",
      "topics": [
        "3",
        "11",
        "10"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "2",
      "episodeId": "60",
      "title": "Android 13 Beta 1",
      "content": "Beta 1 includes the latest updates to photo picker, themed app icons, improved localization and language support, and the new notification permission which requires apps targeting Android 13 to request the notification permission from the user before posting notifications. There are also a few new features, including more granular permissions for media file access, better error reporting in Keystore and KeyMint, and help for media apps to identify how their audio is going to be routed. Check out the beta by visiting the Android 13 developer site.",
      "url": "https://android-developers.googleblog.com/2022/04/android-13-beta-1-blog.html",
      "headerImageUrl": "https://developer.android.com/about/versions/13/images/android-13-hero_1440.png",
      "publishDate": "2022-05-04T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "15"
      ],
      "authors": [
        "14"
      ]
    },
    {
      "id": "3",
      "episodeId": "60",
      "title": "Architecture: Entities - MAD Skills",
      "content": "In this episode, Garima from GoDaddy Studio talks about entities and more specifically how creating separate entities per layer per project leads to clean and scalable model architecture.",
      "url": "https://www.youtube.com/watch?v=cfak1jDKM_4",
      "headerImageUrl": "https://i3.ytimg.com/vi/cfak1jDKM_4/maxresdefault.jpg",
      "publishDate": "2022-05-04T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": []
    },
    {
      "id": "4",
      "episodeId": "60",
      "title": "Architecture: Live Q&A - MAD Skills",
      "content": "Manuel , Yigit , TJ , and Milosz hosted a very special Architecture Q&A and answered questions from the community. Find out the answers to: “Is LiveData deprecated?”, “MVVM or MVI for Compose”, “Should we use flow in DataSources” and more.",
      "url": "https://www.youtube.com/watch?v=_2BtE1P6MPE",
      "headerImageUrl": "https://i3.ytimg.com/vi/_2BtE1P6MPE/maxresdefault.jpg",
      "publishDate": "2022-05-04T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": [
        "23",
        "34",
        "38"
      ]
    },
    {
      "id": "5",
      "episodeId": "60",
      "title": "MAD Skills: Architecture",
      "content": "To wrap up the Architecture Android MAD skills series, \nManuel wrote a blog post summarizing each episode of the series! Check it out to get caught up on all things Android Architecture.",
      "url": "https://android-developers.googleblog.com/2022/04/architecture-mad-skills-series-wrap-up.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjwpLUhIDR7IVIgPnCayAMbm0cYAC0ktSWLT9_vWJ1au0oZK_0un_IlXfu4HixEtc4G_AOi4BkWATw6BsyFCTPtCiu2wSvnfL3OVqWVNdblp6neIuFh9N3KH02SYDBgr6hIpAU7A9KjX9mT3oFJI6uuasaYqqMg_GZgptg0aooIqLywmeTp_PrpTAOj/s1600/1_J2NKRQ4qedvMVWoxL_4ZLA.jpeg",
      "publishDate": "2022-05-04T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "16"
      ],
      "authors": [
        "23"
      ]
    },
    {
      "id": "6",
      "episodeId": "60",
      "title": "The first developer preview of Privacy Sandbox on Android",
      "content": "Privacy Sandbox is a program to help you conduct initial testing of the proposed APIs and evaluate how you might adopt them for your solutions. The Developer Preview provides additional platform APIs and services on top of the Android 13 Developer Beta release, SDK, system images, Preview APIs, API reference, and support references. See the release notes for more details on what’s included in the release.",
      "url": "https://android-developers.googleblog.com/2022/04/first-preview-privacy-sandbox-android.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEimeEtKqNjaaRY2oxecefVbcytzULjln30fNMxfJQfWOu6Tqy9XYQKAVkwLTeGRiPh50RBIxyA6HD86_Qm_Vpiit7eEO1ZmeZttgdsH187-cL8YE-w6NOvqYDwcn-MzIPmk0yiJy-4_kbsZ4_k3CngfP36F-U5g-PQmunzFpPAHuWtBNCsHcbP80flJ/s1600/Android_PrivacySandboxonAndroidDeveloper_4209x1253.png",
      "publishDate": "2022-05-04T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "12",
        "15"
      ],
      "authors": []
    },
    {
      "id": "7",
      "episodeId": "59",
      "title": "Expanding Play’s Target Level API Requirements 🎯",
      "content": "Starting on November 1, 2022, apps that don’t target an API level within two years of the latest major Android release version will not be available on Google Play to new users with devices running Android OS versions newer than your app’s target API level. For example, as of this November, existing apps need to target at least API level 30, Android 11, to be available to new users on Android 12 and 13 devices.",
      "url": "https://android-developers.googleblog.com/2022/04/expanding-plays-target-level-api-requirements-to-strengthen-user-security.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEifh6osWctzfS76FGmd91DArGexlVFw7BNh0ZCqgSuU5aO1AU2pt2T554nkpGy8AzeY_oIOY-TWc0YsS_DwMR9yp3aV_TSrgh7-XPNAg8jSDe_8ySG4ae6D6OqVUMzPmwEoPDXvEhA09um5qahSO1cfSjWIk03bq7vUVDvDHnvt-EubXLKw_Dz2uoUI/s1600/Android-New-policy-update-to-strengthen-Google-Play-social.png",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "13"
      ],
      "authors": [
        "10"
      ]
    },
    {
      "id": "8",
      "episodeId": "59",
      "title": "Google Play PolicyBytes - April 2022 policy updates",
      "content": "Users who have previously installed your app from Google Play will continue to be able to discover, re-install, and use your app, even if they move to a new Android device. App updates still also need to target an API level within a year of the latest major Android release version. Expanding our target level API requirements will protect users from installing older apps that may not have the privacy and security protections in place that newer Android releases offer.",
      "url": "https://www.youtube.com/watch?v=O0UwUF2DgQc",
      "headerImageUrl": "https://i3.ytimg.com/vi/O0UwUF2DgQc/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "13"
      ],
      "authors": []
    },
    {
      "id": "9",
      "episodeId": "59",
      "title": "Upgrading Android Attestation: Remote Provisioning 🔐",
      "content": "Attestation for device integrity has been mandated since Android 8.0, and is used in a variety of services such as SafetyNet. Android 12 added the option of Remote Key Provisioning for device manufacturers, and it will be mandated in Android 13. If you’re leveraging attestation in your app, watch out for a longer certificate chain structure, a new root of trust, the deprecation of RSA attestation, and short-lived certificates/attestation keys.",
      "url": "https://android-developers.googleblog.com/2022/03/upgrading-android-attestation-remote.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEhXGH6tNkY1UkXgIQluciMoaSR9hZNKAoKcRyv_UxyHbEuPRvTVfWT4A_3BQEb_HCMUALR5bScXZsIEzHiRJwrFgm9fhouknFkP5H5ngCUtdf7uiGpTuCOm5dF5rtDrjR5Vm0r9NNU4J7lzN3k0sdMQumgan-NPp2nPSgXypTqj-yqn6BBS9URGrh1F/s1600/Android-KeyAttestation-Header.png",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "15"
      ],
      "authors": []
    },
    {
      "id": "10",
      "episodeId": "59",
      "title": "Architecture: Handling UI events - MAD Skills",
      "content": "With this episode of MAD skills we continue with our architecture series of videos. In this video you'll learn about UI events. Developer Relations Engineer Manuel Vivo covers the different types of UI events, the best practices for handling them, and more!",
      "url": "https://www.youtube.com/watch?v=lwGtp0Yr0PE",
      "headerImageUrl": "https://i3.ytimg.com/vi/lwGtp0Yr0PE/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": [
        "23"
      ]
    },
    {
      "id": "11",
      "episodeId": "59",
      "title": "Architecture: The Domain Layer - MAD Skills",
      "content": "In this episode of MAD skills you'll learn about the Domain Layer - an optional layer which sits between the UI and Data layers. Developer Relations Engineer Don Turner will explain how the domain layer can simplify your app architecture, making it easier to understand and test.",
      "url": "https://www.youtube.com/watch?v=gIhjCh3U88I",
      "headerImageUrl": "https://i3.ytimg.com/vi/gIhjCh3U88I/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": [
        "42"
      ]
    },
    {
      "id": "12",
      "episodeId": "59",
      "title": "Architecture: Organizing modules - MAD Skills",
      "content": "In this episode of Architecture for Modern Android Development Skills, Emily Kager shares a tip around organizing modules in Android apps.",
      "url": "https://www.youtube.com/watch?v=HB_r9wn49Gc",
      "headerImageUrl": "https://i3.ytimg.com/vi/HB_r9wn49Gc/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": []
    },
    {
      "id": "13",
      "episodeId": "59",
      "title": "App Excellence: Android Architecture",
      "content": "How do you build a successful app? Our advice: think like a building architect. If you need help getting started, we have the perfect blueprint for success when building on Android. Check out our updated guide to Android App Architecture, and build something that your users will love.",
      "url": "https://www.youtube.com/watch?v=fodD6UHjLmw",
      "headerImageUrl": "https://i3.ytimg.com/vi/fodD6UHjLmw/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": []
    },
    {
      "id": "14",
      "episodeId": "59",
      "title": "Accessibility on TV - Integrate with Android TV & Google TV",
      "content": "Thinking about accessibility is critical when developing a quality app on Android TV OS. Ian will be covering the most common issues, the solution to these issues, and some more complex scenarios. ",
      "url": "https://www.youtube.com/watch?v=GyglHvJ6LMY",
      "headerImageUrl": "https://i3.ytimg.com/vi/GyglHvJ6LMY/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "19",
        "17"
      ],
      "authors": []
    },
    {
      "id": "15",
      "episodeId": "59",
      "title": "Google Play Billing - Integrate with Android TV & Google TV",
      "content": "In this episode of Integrate with Android TV & Google TV, Thomas will discuss how you can monetize your Android TV app using Google Play Billing. ",
      "url": "https://www.youtube.com/watch?v=gb55CjH7NHY",
      "headerImageUrl": "https://i3.ytimg.com/vi/gb55CjH7NHY/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "19"
      ],
      "authors": []
    },
    {
      "id": "16",
      "episodeId": "59",
      "title": "Android for Cars 🚗",
      "content": "Android for cars has introduced media recommendations powered by Google Assistant, a progress bar for long form content, and per-item content styles to allow browsable/playable items to be individually assigned to a list or grid. Head on over to the developer documentation to learn about all of these changes.",
      "url": "https://developer.android.com/cars",
      "headerImageUrl": "https://developers.google.com/cars/design/images/designforcars_1920.png",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "19"
      ],
      "authors": []
    },
    {
      "id": "17",
      "episodeId": "59",
      "title": "Google Play Academy🎓 - Go Global: Japan\n",
      "content": "With over 2 billion active Android devices globally, more and more developers are looking for new markets to expand. If you’re looking to succeed in Japan, one of the largest mobile app and gaming markets, these courses will cover strategies to make your content relevant across development, marketing and growth, and monetization.",
      "url": "https://www.youtube.com/watch?v=hY1HH-9efkg",
      "headerImageUrl": "https://i3.ytimg.com/vi/hY1HH-9efkg/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "13"
      ],
      "authors": []
    },
    {
      "id": "18",
      "episodeId": "59",
      "title": "Google Play Academy🎓 - Go Global: Southeast Asia",
      "content": "With over 2 billion active Android devices globally, more and more developers are looking for new markets to expand. If you’re looking to succeed in Southeast Asia, a fast-growing market that spends as much as 1.5x more time on the mobile internet than any other region, these courses will cover strategies to make your content relevant across development, marketing and growth, and monetization.",
      "url": "https://www.youtube.com/watch?v=j9VRzvDhTO0",
      "headerImageUrl": "https://i3.ytimg.com/vi/j9VRzvDhTO0/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "13"
      ],
      "authors": []
    },
    {
      "id": "19",
      "episodeId": "59",
      "title": "Google Play Academy🎓 - Design for All Users",
      "content": "Learn how to optimize for onboarding, build accessible apps, and reduce app size to reach more users.",
      "url": "https://www.youtube.com/watch?v=07NUULjEJ5A",
      "headerImageUrl": "https://i3.ytimg.com/vi/07NUULjEJ5A/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "13"
      ],
      "authors": []
    },
    {
      "id": "20",
      "episodeId": "59",
      "title": "Game development 🎮",
      "content": "We covered how to help you monitor your game’s stability using Android vitals on Google Play Console, how to best optimize your game to improve your customer engagement during the month of Ramadan, and announced that the Indie Games Accelerator & Indie Games Festival 2022 from Google Play is coming soon, offering a way to get notified when submissions open.",
      "url": "https://www.youtube.com/watch?v=m2gTnT2kCRQ",
      "headerImageUrl": "https://i3.ytimg.com/vi/m2gTnT2kCRQ/maxresdefault.jpg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "20"
      ],
      "authors": []
    },
    {
      "id": "21",
      "episodeId": "59",
      "title": "Migrating Architecture Blueprints to Jetpack Compose",
      "content": "Manuel wrote about how and why we’ve Migrated our Architecture Blueprints to Jetpack Compose, and some issues we faced in doing so.",
      "url": "https://medium.com/androiddevelopers/migrating-architecture-blueprints-to-jetpack-compose-8ffa6615ede3",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*J2NKRQ4qedvMVWoxL_4ZLA.jpeg",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "16",
        "11"
      ],
      "authors": [
        "23"
      ]
    },
    {
      "id": "22",
      "episodeId": "59",
      "title": "The curious case of FLAG_ACTIVITY_LAUNCH_ADJACENT",
      "content": "Pietro wrote about how to enable split screen use cases using the Android 7.0 FLAG_ACTIVITY_LAUNCH_ADJACENT flag to open your Activity in a new adjacent window on Android 12L. (and some supported Android 11+ devices)",
      "url": "https://medium.com/androiddevelopers/the-curious-case-of-flag-activity-launch-adjacent-f1212f37a8e0",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*YWg6uZkqSakAb5vW6uc-gg.png",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "12"
      ]
    },
    {
      "id": "23",
      "episodeId": "59",
      "title": "AndroidX releases 🚀",
      "content": "AppCompat AppCompat-Resources Version 1.5.0-alpha01 contains a bunch of bugfixes, as well as updated nullability to match Android 13 DP2 and a few small compatibility features involving TextView, AppCompatDialog, SearchView, and SwitchCompat.\n\nNavigation Version 2.4.2 has been released with all the new bugfixes backported from the 2.5 alpha releases.",
      "url": "https://developer.android.com/jetpack/androidx/versions/all-channel",
      "headerImageUrl": "",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "8"
      ],
      "authors": []
    },
    {
      "id": "24",
      "episodeId": "59",
      "title": "ADB Podcast Episodes🎙184: Skia and AGSL - Shaders of Things to Come",
      "content": "In this episode Tor, Chet, and Romain chat with Derek and Brian from the Skia team about Skia (the graphics layer that backs the Android Canvas APIs), pixel shaders, and the new “AGSL” API that lets you provide pixel shaders for advanced graphics effects, which was recently added as part of the Android T preview release. If you’re interested in graphics technology, this is the episode for you.",
      "url": "https://adbackstage.libsyn.com/episode-184-skia-and-agsl-shaders-of-things-to-come",
      "headerImageUrl": "https://ssl-static.libsyn.com/p/assets/9/4/d/b/94dbe077f2f14ee640be95ea3302a6a1/ADB184_Skia_and_AGSL.png",
      "publishDate": "2022-04-13T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "3",
        "8"
      ],
      "authors": [
        "32",
        "30",
        "31"
      ]
    },
    {
      "id": "25",
      "episodeId": "58",
      "title": "Android 13 DP 2 😍",
      "content": "Recently we shared Android 13 Developer Preview 2 with more new features and changes for you to try in your apps! Some notable features include Developer downgradable permissions which allows your app to protect user privacy by downgrading previously granted runtime permissions, and Bluetooth LE Audio which helps users receive high fidelity audio without sacrificing battery life; it can also seamlessly switch between different use cases that were not possible with Bluetooth Classic. Check out all the new features in the post!",
      "url": "https://android-developers.googleblog.com/2022/03/second-preview-android-13.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEjnrShXcFkBmErmhgdmx82vJbaKBIxU6p2Yz2Vr1V7AlFkD2tGwRmx_a7tWcInPmiUh8VpPmEEqXut-EjP23lFYG9wiMO4sKBDEwbZ3MNppZOy_HW54OXO4SkdQVH08cWdi7QnTMMwGELFoPq_r7_cyaGU8fx2InJG2R-NfkqF1IRt7rKOfA8M1GhUy",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "15"
      ],
      "authors": [
        "14"
      ]
    },
    {
      "id": "26",
      "episodeId": "58",
      "title": "Architecture: The data layer - MAD Skills",
      "content": "Jose goes over the data layer and its two components: repositories and data sources. You will dive deeper into what the roles of these two are and understand their differences. You will also learn about data immutability, error handling, threading testing and more!",
      "url": "https://www.youtube.com/watch?v=r5AseKQh2ZE",
      "headerImageUrl": "https://i3.ytimg.com/vi/r5AseKQh2ZE/maxresdefault.jpg",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": []
    },
    {
      "id": "27",
      "episodeId": "58",
      "title": "Architecture: The UI layer - MAD Skills",
      "content": "TJ covers the UI layer in this episode of MAD skills using the JetNews sample app as a case study You will learn UI Layer pipeline, UI state exposure, UI state consumption and more!",
      "url": "https://www.youtube.com/watch?v=p9VR8KbmzEE",
      "headerImageUrl": "https://i3.ytimg.com/vi/p9VR8KbmzEE/maxresdefault.jpg",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": [
        "38"
      ]
    },
    {
      "id": "28",
      "episodeId": "58",
      "title": "Account Linking - Integrate with Android TV & Google TV",
      "content": "Miguel and Adekunle discuss account linking. ​​Google Account Linking lets you safely link a user’s Google Account with their account on your platform, granting Google applications and devices access to your services and is needed for many integrations on Android TV & Google TV. They discuss the basics of OAuth like implementing your authorization, token exchange, and revocation endpoints. You will also learn the difference between the Web OAuth, Streamlined, and App Flip linking flows.",
      "url": "https://www.youtube.com/watch?v=-Fa99hpUsdk",
      "headerImageUrl": "https://i3.ytimg.com/vi/-Fa99hpUsdk/maxresdefault.jpg",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "19"
      ],
      "authors": []
    },
    {
      "id": "29",
      "episodeId": "58",
      "title": "Modern media playback on Android - Integrate with Android TV & Google TV",
      "content": "Paul explores best practices for integrating and validating media sessions, the unified way for Android apps to interact with media content. MediaSessions allows different devices like smart speakers, watches, peripherals and accessories to surface and interact with playback and content metadata.",
      "url": "https://www.youtube.com/watch?v=OYy41ceW59s",
      "headerImageUrl": "https://i3.ytimg.com/vi/OYy41ceW59s/maxresdefault.jpg",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "19"
      ],
      "authors": [
        "27"
      ]
    },
    {
      "id": "30",
      "episodeId": "58",
      "title": "FHIR SDK for Android Developers 🏥",
      "content": "Community health workers in low-and-middle-income countries use mobile devices as critical tools for doing community outreach and providing crucial health services. Unfortunately, the lack of data interoperability means that patient records are fragmented and caregivers may only receive incomplete information. Last year, Google introduced a collaboration with the World Health Organization to build an open source software developer kit designed to help developers build mobile solutions using the Fast Healthcare Interoperability Resources (FHIR) global standard for healthcare data. Read the article to learn more about how this SDK can help you create apps to aid community health workers in low-and-middle-income countries.",
      "url": "https://medium.com/androiddevelopers/our-fhir-sdk-for-android-developers-9f8455e0b42f",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*azSHuKma0fz1RxcPcqiusg.png",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "2"
      ],
      "authors": []
    },
    {
      "id": "31",
      "episodeId": "58",
      "title": "Helping Users Discover Quality Apps on Large Screens 🔎",
      "content": "Adoption of large screens is growing rapidly and now there are over 250M active Android tablets, foldables, and ChromeOS devices. To help people get the most from their devices, we’re making three big changes in Google Play to enable users to discover and engage with high quality apps and games: ranking and promotability changes, alerts for low quality apps, and device-specific ratings and reviews. Read all about it in the post!",
      "url": "https://android-developers.googleblog.com/2022/03/helping-users-discover-quality-apps-on.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEiWXMaly6_CP_gSHmxE92yVBXUQ1X5EcTA6pdKwo_NsAtO1Ouv_RhHxG1HqtbStufdnylV51VbHI0FmmPV8lvmLAOqNzhcD2znU3vWVajQXfOlFw_kP-01jxSvrzVIXZG7SCQMiw58yUaWgmqzO-dsaso5DOeYVKnwQm3Vdu9lFmogfCkQT5u7H0sVt",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "3"
      ],
      "authors": [
        "61"
      ]
    },
    {
      "id": "32",
      "episodeId": "58",
      "title": "Access Android vitals data through the new Play Developer Reporting API",
      "content": "In this article Lauren talks about Android vitals which are a great way to track how your app is performing in Google Play Console. Now there are new use cases for Android vitals which include building internal dashboards, joining with other datasets for deeper analysis and automation troubleshooting and releases. Learn more about the API and how to use it in this post!",
      "url": "https://android-developers.googleblog.com/2022/03/play-developer-reporting-API.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhnvMF36lJv9wDDHWLQb7AfVBajueyEuocw_9ne1jgKJAO5dgXWcAyrKa92f4miTcFoSH5usz_Jha2C1gJwJNSr6et8sZGSCnkZTgtdaKPemEfwaHJDjiurWaPtqFF3qI0aX7aRB7B9WUW1VXT_Wgkyyq8nYK7RrOy9zW4a7gROkzd3H5m9T36Bc7Ww",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "13"
      ],
      "authors": []
    },
    {
      "id": "33",
      "episodeId": "58",
      "title": "Using performance class to optimize your user experience",
      "content": "The Jetpack Core Performance library in alpha has launched! This library enables you to easily identify what a device is capable of, and tailor your user experience accordingly. As developer, this means you can reliably group devices with the same level of performance and tailor your app’s behavior to those different groups. This enables you to deliver an optimal experience to users with both more and less capable devices.",
      "url": "https://android-developers.googleblog.com/2022/03/using-performance-class-to-optimize.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjYa28aPEBLCSzLkir02bVSWusH5BBIiAcq_CzCx5DD3iQu5WyDLC1sZe1y5OInomon5KHJKemMCa5q6XAtfkMhljMoePuebLGDz6yRDU3cjkwMo7sV5WKe20KNzWhP1ktdOn7OxOxeiXqzeDrPwLcpoVaStm8840phqHOqDptiQ0twMsGTD2u3o0Xf/s1600/Android-using-performance-class-to-optimize-user-experience-header%20%281%29.png",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "5"
      ],
      "authors": [
        "42"
      ]
    },
    {
      "id": "34",
      "episodeId": "58",
      "title": "AndroidX releases 🚀",
      "content": "We have a few libraries in alpha-01 including Activity Version 1.6.0-alpha01, CustomView-Poolingcontainer Version 1.0.0-alpha01, and Junit-Gtest Version 1.0.0-alpha01.\n\nCar App Version 1.2.0-rc01 and Mediarouter Version 1.3.0-rc01 are also in rc-01.",
      "url": "https://developer.android.com/jetpack/androidx/versions/all-channel",
      "headerImageUrl": "",
      "publishDate": "2022-03-29T23:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "8"
      ],
      "authors": []
    },
    {
      "id": "35",
      "episodeId": "57",
      "title": "Discontinuing Kotlin synthetics for views",
      "content": "Synthetic properties to access views were created as a way to eliminate the common boilerplate of findViewById calls. These synthetics are provided by JetBrains in the Kotlin Android Extensions Gradle plugin (not to be confused with Android KTX).",
      "url": "https://android-developers.googleblog.com/2022/02/discontinuing-kotlin-synthetics-for-views.html",
      "headerImageUrl": "",
      "publishDate": "2022-02-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3",
        "10"
      ],
      "authors": [
        "1"
      ]
    },
    {
      "id": "36",
      "episodeId": "57",
      "title": "Things to know from the 2022 Google for Games Developer Summit",
      "content": "This week marked the 2022 Google for Games Developer Summit, Google’s biggest event of the year centered around game development. The Android team shared information around the next generation of services, tools and features to help you develop and deliver high quality games. ",
      "url": "https://android-developers.googleblog.com/2022/03/GGDS-recap-blog.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhW4RL-UKUurgM2bVJRepqjKehVETjf9bqdXllyspPaWTTt8s86MGvfxlxLkDyJAnnkGr7vDpDTPx6bQbgkThYXMSaW1GQvXw9V57xybA8Y89vIE45JDElGxSNFHwOAndATPYrGmc200fkyBTRSNi7w53hTbS1ao-TSoEBFs8jvTgz6ud5Tcb1qitkt",
      "publishDate": "2022-03-15T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "20"
      ],
      "authors": [
        "2"
      ]
    },
    {
      "id": "37",
      "episodeId": "57",
      "title": "MAD Skills: DataStore and Introduction to Architecture💡",
      "content": "Now that our MAD Skills series on Jetpack DataStore is complete, let’s do a quick wrap up of all the things we’ve covered in each episode.",
      "url": "https://android-developers.googleblog.com/2022/03/jetpack-datastore-wrap-up.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgo2-I1LhMjWd1zzpIQXzjMCPoZeUZc35n43UosKDuLMyP7rIDe8cGfs23tmkSAed6Wxw9EoNTIpvvWCljermK_lCu0etlrCnONx3WeXMCGe-s8I45hYhuVo6w_Q2UTNATMTA70t2o9MS5p2pBdPFz5Ye4b2ajOJjNlW9rELtqWcEW4O1Rkzy4lfqRO",
      "publishDate": "2022-03-14T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9"
      ],
      "authors": [
        "3"
      ]
    },
    {
      "id": "38",
      "episodeId": "57",
      "title": "Play Time with Jetpack Compose",
      "content": "Learn about Google Play Store’s strategy for adopting Jetpack Compose, how they overcame specific performance challenges, and improved developer productivity and happiness.",
      "url": "https://android-developers.googleblog.com/2022/03/play-time-with-jetpack-compose.html",
      "headerImageUrl": "",
      "publishDate": "2022-03-10T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "11",
        "13"
      ],
      "authors": [
        "4",
        "5"
      ]
    },
    {
      "id": "39",
      "episodeId": "57",
      "title": "App Excellence Summit 2022 ⭐",
      "content": "Did you know that 54% of users who left a 1-star review in the Play Store mentioned app stability and bugs? *\n\nTo help product managers and business decision makers understand how high quality app experiences drive business growth and what tools they can use to make sound business and technical decisions, we are hosting our first Android App Excellence Summit in just a few weeks!",
      "url": "https://android-developers.googleblog.com/2022/03/app-excellence-summit-2022.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEh4Vck7mqle-tLweEgrIc1WT0ycY6O6zBxv9mC1Dt1xCnJN5COTGFxDSQlIM1rbbMKIMZHPtjzXgENMGk80oxb5Mn8kTn6qO7kgUXC_N5YSB0dWxcXvQOIPHEEgNJze9g8eZrY1xgA9_oBls71NLItDJKTYeoJGEXxIBiAE_c6SkXv2jSELZEoFfqVq",
      "publishDate": "2022-03-10T00:00:00.000Z",
      "type": "Event 📆",
      "topics": [
        "2"
      ],
      "authors": []
    },
    {
      "id": "40",
      "episodeId": "57",
      "title": "#TheAndroidShow: Tablets, Jetpack Compose, and Android 13 📹",
      "content": "Last week, Florina and Huyen hosted #TheAndroidShow, where we went Behind the scenes with animations & Jetpack Compose, asked whether now is the moment to think tablet first, and covered Android 13 along with other key themes for Android this year.",
      "url": "https://www.youtube.com/watch?v=WL9h46CymlU",
      "headerImageUrl": "https://i.ytimg.com/vi/WL9h46CymlU/maxresdefault.jpg",
      "publishDate": "2022-03-09T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "11",
        "3",
        "15",
        "2"
      ],
      "authors": [
        "6"
      ]
    },
    {
      "id": "41",
      "episodeId": "57",
      "title": "Freeing up 60% of storage for apps 💾",
      "content": "App archiving will allow users to reclaim ~60% of app storage temporarily by removing parts of the app rather than uninstalling the app completely.",
      "url": "https://android-developers.googleblog.com/2022/03/freeing-up-60-of-storage-for-apps.html",
      "headerImageUrl": "",
      "publishDate": "2022-03-08T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9"
      ],
      "authors": [
        "7",
        "8"
      ]
    },
    {
      "id": "42",
      "episodeId": "57",
      "title": "Demystifying Jetpack Glance for app widgets",
      "content": "We recently announced the first Alpha version of Glance, initially with support for AppWidgets and now for Tiles for Wear OS. This new framework is built on top of the Jetpack Compose runtime and designed to make it faster and easier to build “glanceables” such as app widgets without having to handle a lot of boilerplate code or lifecycle events to connect different components.",
      "url": "https://medium.com/androiddevelopers/demystifying-jetpack-glance-for-app-widgets-8fbc7041955c",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*mlswR3fyxaIG-C1OUifYVw.jpeg",
      "publishDate": "2022-03-07T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3",
        "21"
      ],
      "authors": [
        "9"
      ]
    },
    {
      "id": "43",
      "episodeId": "57",
      "title": "Keeping Google Play safe with our key 2022 initiatives 🔒",
      "content": "We shared information about what’s ahead in 2022 for Google Play’s privacy and safety initiatives to give you time to prepare.",
      "url": "https://android-developers.googleblog.com/2022/03/privacy-and-security-direction.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhh3FMLL-etD7iDzhSI6CoYbuwgB9ZADjXa6A9C4aM3W-eRqj1FGfP8dyMY4i5RlMtQJD8Sx1y1NHFuaCae10iZkAs_cETaCAllzCDU075awpkAc1pkhld7uxwjTmwNdihGhB-FtySiSsf9aknd1ZULz0zkRtybX4gRUp8JCbPh2n3pPEhjK0mTjNWS",
      "publishDate": "2022-03-03T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "12"
      ],
      "authors": [
        "10"
      ]
    },
    {
      "id": "44",
      "episodeId": "57",
      "title": "Games-Activity Version 1.1.0",
      "content": "adds WindowInsets listening/querying for notch and IME response along with key and motion event filters.",
      "url": "https://developer.android.com/jetpack/androidx/releases/games#1.1.0",
      "headerImageUrl": "",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "20"
      ],
      "authors": []
    },
    {
      "id": "45",
      "episodeId": "57",
      "title": "Room Version 2.5.0-alpha01",
      "content": "Converted room-common, room-migration, and paging related files in room-runtime from Java to Kotlin along with a new API for multi-process lock to protect multi-process 1st time database creation and migrations",
      "url": "https://developer.android.com/jetpack/androidx/releases/room#2.5.0-alpha01",
      "headerImageUrl": "",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "9"
      ],
      "authors": []
    },
    {
      "id": "46",
      "episodeId": "57",
      "title": "Media Version 1.6.0-alpha 01",
      "content": "Adds the extras necessary to setup a signin/settings page using CarAppLibrary.",
      "url": "https://developer.android.com/jetpack/androidx/releases/media#media-1.6.0-alpha01",
      "headerImageUrl": "",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "6"
      ],
      "authors": []
    },
    {
      "id": "47",
      "episodeId": "57",
      "title": "AppCompat-Resources Version 1.6.0-alpha01",
      "content": "Adds support for customizing locales, providing backwards compatibility for the Android 13 per-language preferences API",
      "url": "https://developer.android.com/jetpack/androidx/releases/appcompat#1.6.0-alpha01",
      "headerImageUrl": "",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "48",
      "episodeId": "57",
      "title": "Recording Video with CameraX VideoCapture API",
      "content": "A picture is worth a thousand words, and CameraX ImageCapture has already made it much easier to tell your story through still images on Android. Now with the new VideoCapture API, CameraX can help you create thousands of continuous pictures to tell an even better and more engaging story!",
      "url": "https://medium.com/androiddevelopers/recording-video-with-camerax-videocapture-api-a36cfd8a48c8",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*GZmhCFMCrG4L_mOtwSb0zA.png",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6"
      ],
      "authors": [
        "11"
      ]
    },
    {
      "id": "49",
      "episodeId": "57",
      "title": "Unbundling the stable WindowManager",
      "content": "The 1.0.0 stable release of Jetpack WindowManager, the foundation for great experiences on all types of large screen devices.",
      "url": "https://medium.com/androiddevelopers/unbundling-the-stable-windowmanager-a5471ff2907",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*dIXjHF8_-47CvYTb.png",
      "publishDate": "2022-02-17T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "12"
      ]
    },
    {
      "id": "50",
      "episodeId": "56",
      "title": "Jetpack Compose 1.1 is now stable!",
      "content": "Last week we released version 1.1 of Jetpack Compose and Florina Muntenescu wrote an article giving us all the information! This release contains new features like improved focus handling, touch target sizing, ImageVector caching and support for Android 12 stretch overscroll. This also means that previously experimental APIs are now stable. Check out our recently updated samples, codelabs, and the Accompanist library!",
      "url": "https://android-developers.googleblog.com/2022/02/jetpack-compose-11-now-stable.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEiEIiQOoFF-f-sDcbYOMINZw5-2R9aQjrREfiXFMGsRYODVfaz1sgdCS2C3UjgeJjCII5oyE4y97kbvQIUsl9wIx8RqTSZPSdIoCywW89lvmAJ5a15bkFOwoR9UacCEUb4CjOMy0omVMfC0CQhUfz9VMTZR4iyjDGagEZfNuMid8BT0lvarns9Tp6PC",
      "publishDate": "2022-02-09T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "3"
      ],
      "authors": [
        "6"
      ]
    },
    {
      "id": "51",
      "episodeId": "56",
      "title": "MAD Skills: DataStore",
      "content": "The DataStore MAD Skills series rolls on! In the sixth episode, Simona Stojanovic covered DataStore: Best Practices part 2 covering DataStore-to-DataStore migration. This is used when you make significant changes to your dataset like renaming your data model values or changing their type. ",
      "url": "https://medium.com/androiddevelopers/datastore-and-data-migration-fdca806eb1aa",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*8wsdb7Z7QxT1d4lM",
      "publishDate": "2022-02-15T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9"
      ],
      "authors": [
        "3"
      ]
    },
    {
      "id": "52",
      "episodeId": "56",
      "title": "DataStore and Testing",
      "content": "For the final part of the DataStore series, Simona covered DataStore and testing and teaches you how to fully test your DataStore.",
      "url": "https://medium.com/androiddevelopers/datastore-and-testing-edf7ae8df3d8",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*5_yt1M6_QEMN0OgGU8VaZw.png",
      "publishDate": "2022-02-16T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9"
      ],
      "authors": [
        "3"
      ]
    },
    {
      "id": "53",
      "episodeId": "56",
      "title": "Material You: Coming to more Android Devices near you",
      "content": "Material You will soon be available on more Android 12 phones globally including devices by Samsung, Oppo, OnePlus and more! Material You has made the Android experience more fluid and personal than ever. Our OEM partners continue to work with us to ensure that key design APIs work consistently across the Android ecosystem so developers can benefit from a cohesive experience.",
      "url": "https://android-developers.googleblog.com/2022/02/material-you-coming-to-more-android.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhDOIPFoqZ8uvg7VmH5EuY3ocfxvKZXawUQ9NczUCEtOdpw3v42vSTrpUSvHjbph5KmTlDH-XtnmGeXmCFTMaHDnRS9ibzLUHBip_XnVHUL7xv-3UrVL6plimErj_oK_KyW5ULpmj6orVTaTq9r56K0V3npQFdIrBPE7_caRWb_QA5E9FljpREWVB7Y",
      "publishDate": "2022-02-10T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3",
        "15"
      ],
      "authors": [
        "13"
      ]
    },
    {
      "id": "54",
      "episodeId": "56",
      "title": "The first developer preview of Android 13",
      "content": "We’re sharing a first look at the next release of Android, with the Android 13 Developer Preview 1. With Android 13 we’re continuing some important themes: privacy and security, as well as developer productivity. We’ll also build on some of the newer updates we made in 12L to help you take advantage of the 250+ million large screen Android devices currently running.",
      "url": "https://android-developers.googleblog.com/2022/02/first-preview-android-13.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEjnrShXcFkBmErmhgdmx82vJbaKBIxU6p2Yz2Vr1V7AlFkD2tGwRmx_a7tWcInPmiUh8VpPmEEqXut-EjP23lFYG9wiMO4sKBDEwbZ3MNppZOy_HW54OXO4SkdQVH08cWdi7QnTMMwGELFoPq_r7_cyaGU8fx2InJG2R-NfkqF1IRt7rKOfA8M1GhUy",
      "publishDate": "2022-02-10T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "15"
      ],
      "authors": [
        "14"
      ]
    },
    {
      "id": "55",
      "episodeId": "56",
      "title": "AndroidX releases 🚀",
      "content": "Since Compose just went stable, the Animation, Compiler, Foundation, Material, Runtime and UI Versions also went stable! Games-Text-Input and ProfileInstaller also went stable! \n\nThere are a bunch of new APIs in alpha including new Testing APIs (Test Runner, Test Monitor, Test Services and Test Orchestrator), Metrics Version and Startup Version.",
      "url": "https://developer.android.com/jetpack/androidx/versions/all-channel#february_9_2022",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*bux1xKYcB3A9pBFx",
      "publishDate": "2022-02-09T00:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "11",
        "8"
      ],
      "authors": [
        "15"
      ]
    },
    {
      "id": "56",
      "episodeId": "55",
      "title": "DataStore best practices part 1",
      "content": "learn about performing synchronous work and how to make it work with Kotlin data class serialization and Hilt.",
      "url": "https://www.youtube.com/watch?v=S10ci36lBJ4",
      "headerImageUrl": "",
      "publishDate": "2022-02-07T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "9"
      ],
      "authors": [
        "3"
      ]
    },
    {
      "id": "57",
      "episodeId": "55",
      "title": "All about Proto DataStore",
      "content": "In this post, we will learn about Proto DataStore, one of two DataStore implementations. We will discuss how to create it, read and write data and how to handle exceptions, to better understand the scenarios that make Proto a great choice.",
      "url": "https://medium.com/androiddevelopers/all-about-proto-datastore-1b1af6cd2879",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*UtNu7pmbt3WEA213SW9p9Q.png",
      "publishDate": "2022-01-31T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9"
      ],
      "authors": [
        "3"
      ]
    },
    {
      "id": "58",
      "episodeId": "55",
      "title": "Glance: Tiles for Wear OS made simple ⌚️",
      "content": "Last year we announced the Wear Tiles API. To complement that Java API, we are excited to announce that support for Wear OS Tiles has been added to Glance, a new framework built on top of Jetpack Compose designed to make it easier to build for surfaces outside your Android app. As this library is in alpha, we’d love to get your feedback.",
      "url": "https://android-developers.googleblog.com/2022/01/announcing-glance-tiles-for-wear-os.html",
      "headerImageUrl": "",
      "publishDate": "2022-01-26T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "21"
      ],
      "authors": [
        "16"
      ]
    },
    {
      "id": "59",
      "episodeId": "55",
      "title": "Android Studio Bumblebee 🐝 stable",
      "content": "Android Studio Bumblebee (2021.1.1) is now stable. We’ve since patched it to address some launch issues — so make sure to upgrade! It improves functionality across the typical developer workflow: Build and Deploy, Profiling and Inspection, and Design.",
      "url": "https://android-developers.googleblog.com/2022/01/android-studio-bumblebee-202111-stable.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhQ7R2ySipHb8y5jNJeiIj3pE8dZfWAV7EF0wQZ4rQ65lB4MsZroAT4R_7rSfznMZ30xBMLx9_dwnt05V6I0Du0EfI7mvLicK6LwdkuZsF_Gc3sPqrZGxkojTJpHCXFI3Kvr3bLyoSjElldtt1NUpGSBzHgG3O1pvS9BR02L9R2_FYTUgPLfUoNLWYQ",
      "publishDate": "2022-01-25T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        "17"
      ]
    },
    {
      "id": "60",
      "episodeId": "55",
      "title": "All about Preferences DataStore",
      "content": "In this post, we will take a look at Preferences DataStore, one of two DataStore implementations. We will go over how to create it, read and write data, and how to handle exceptions, all of which should, hopefully, provide you with enough information to decide if it’s the right choice for your app.",
      "url": "https://medium.com/androiddevelopers/all-about-preferences-datastore-cc7995679334",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*UtNu7pmbt3WEA213SW9p9Q.png",
      "publishDate": "2022-01-24T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9"
      ],
      "authors": [
        "3"
      ]
    },
    {
      "id": "61",
      "episodeId": "55",
      "title": "Building apps for Android Automotive OS 🚘",
      "content": "The Car App Library version 1.2 is already in beta, enabling app developers to start building their navigation, parking, and charging apps for Android Automotive OS. Now, developers can begin building and testing apps for these categories using the Automotive OS emulator across both Android Automotive OS and Android Auto.",
      "url": "https://android-developers.googleblog.com/2022/01/building-apps-for-android-automotive-os.html",
      "headerImageUrl": "",
      "publishDate": "2022-01-27T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "18"
      ],
      "authors": [
        "18"
      ]
    },
    {
      "id": "62",
      "episodeId": "55",
      "title": "Navigation 2.4 is stable ",
      "content": "It’s been rewritten in Kotlin, with two pane integration, Navigation routes + Kotlin DSL improvements, Navigation Compose’s first stable release, and multiple back stack support.",
      "url": "https://developer.android.com/jetpack/androidx/releases/navigation#2.4.0",
      "headerImageUrl": "",
      "publishDate": "2022-01-26T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "63",
      "episodeId": "55",
      "title": "Google Maps with Jetpack Compose",
      "content": "A project which contains Jetpack Compose components for the Google Maps SDK for Android.\n\n",
      "url": "https://github.com/googlemaps/android-maps-compose",
      "headerImageUrl": "",
      "publishDate": "2022-02-11T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "11"
      ],
      "authors": []
    },
    {
      "id": "64",
      "episodeId": "55",
      "title": "Improving App Performance with Baseline Profiles",
      "content": "In this blog post we’ll discuss Baseline Profiles and how they improve app and library performance, including startup time by up to 40%. While this blogpost focuses on startup, baseline profiles also significantly improve jank as well.",
      "url": "https://android-developers.googleblog.com/2022/01/improving-app-performance-with-baseline.html",
      "headerImageUrl": "",
      "publishDate": "2022-01-28T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "5"
      ],
      "authors": [
        "19",
        "20",
        "21"
      ]
    },
    {
      "id": "65",
      "episodeId": "55",
      "title": "Smule Adopts Google’s Oboe to Improve Recording Quality & Completion Rates",
      "content": "As the most downloaded singing app of all time, Smule Inc. has been investing on Android to improve the overall audio quality and, more specifically, to reduce latency, i.e. allowing singers to hear their voices in the headset as they perform. The teams specialized in Audio and Video allocated a significant part of 2021 into making the necessary changes to convert the Smule application used by over ten million Android users from using the OpenSL audio API to the Oboe audio library, enabling roughly a 10%+ increase in recording completion rate.",
      "url": "https://android-developers.googleblog.com/2022/02/smule-adopts-googles-oboe-to-improve.html",
      "headerImageUrl": "",
      "publishDate": "2022-02-02T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6"
      ],
      "authors": []
    },
    {
      "id": "66",
      "episodeId": "55",
      "title": "Guide to background work",
      "content": "Do you use coroutines or WorkManager for background work? The team updated the guide to background work to help you choose which library is best for your use case. It depends on whether or not the work is persistent, and if it needs to run immediately, it’s long running, or deferrable.",
      "url": "https://developer.android.com/guide/background",
      "headerImageUrl": "",
      "publishDate": "2022-02-11T00:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "16"
      ],
      "authors": []
    },
    {
      "id": "67",
      "episodeId": "55",
      "title": "Accessibility best practices",
      "content": "If you work on Android TV, you should be aware of the accessibility best practices that the team created. It provides recommendations for both native and non-native apps. Get to know why accessibility is important for your TV app, how to evaluate your apps when TalkBack is used, how to adopt system caption settings, and more!",
      "url": "https://developer.android.com/training/tv/accessibility",
      "headerImageUrl": "",
      "publishDate": "2022-02-11T00:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "19",
        "17"
      ],
      "authors": []
    },
    {
      "id": "68",
      "episodeId": "55",
      "title": "TalkBack - the Google screen reader",
      "content": "Next up in the Accessibility series is TalkBack, the Google screen reader! In this video, learn what TalkBack is, how to set it up, how to navigate through your app with it, and how you can use it to improve the Accessibility of your app.",
      "url": "https://www.youtube.com/watch?v=_1yRVwhEv5I",
      "headerImageUrl": "",
      "publishDate": "2022-01-21T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "17"
      ],
      "authors": []
    },
    {
      "id": "69",
      "episodeId": "55",
      "title": "ADB Podcast 182: Large screens are a big deal",
      "content": "Clara, Florina and Daniel join your usual hosts to talk about large screens, what they are and what they mean for app developers. You will also learn about the resources at your disposal to build high quality experiences on large screen devices: from samples and guidance to canonical layouts and new APIs such as window size classes. Disclaimer: Florina is very excited about this, don’t miss the epic Large screens! Large screens! Large screens! intro!",
      "url": "https://adbackstage.libsyn.com/episode-182-large-screens-are-a-big-deal",
      "headerImageUrl": "",
      "publishDate": "2022-02-01T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "70",
      "episodeId": "54",
      "title": "Jetpack Alpha for Glance Widgets 🔍",
      "content": "We made the first release of Jetpack Glance available, a new framework designed to make it faster and easier to build app widgets for the home screen and other surfaces. Glance offers similar modern, declarative Kotlin APIs that you are used to with Jetpack Compose, helping you build beautiful, responsive app widgets with way less code. Glance provides a base-set of its own Composables to help build “glanceable” experiences — starting today with app widget components but with more coming. Using the Jetpack Compose runtime, Glance translates these Composables into RemoteViews that can be displayed in an app widget",
      "url": "https://android-developers.googleblog.com/2021/12/announcing-jetpack-glance-alpha-for-app.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgol-A5cMCZY79MH5v0axcekWIVJ--ymPUe0U5Q4BLsC0BA1LTbWIlZ76XWi2cHjxHVu-kbpv0o2QJWBjNAda_93Ah7AW_PcAgz9o082cd6zyTJZAM8HjQnrZ69A6CaKQaCFuf2LLi4p6xRvS_WUn9tVA2K2wmV3_qB6JDKnFNhO3Guvn5tPc_SuoaY",
      "publishDate": "2021-12-15T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "8"
      ],
      "authors": [
        "9"
      ]
    },
    {
      "id": "71",
      "episodeId": "54",
      "title": "Jetpack Watch Face Library ⌚",
      "content": "We launched the Jetpack Watch Face library written from the ground up in Kotlin, including all functionality from the Wearable Support Library along with many new features such as: Watch face styling which persists across both the watch and phone (with no need for your own database or companion app); Support for a WYSIWYG watch face configuration UI on the phone; Smaller, separate libraries (that only include what you need); Battery improvements through promoting good battery usage patterns out of the box, such as automatically reducing the interactive frame rate when the battery is low; New screenshot APIs so users can see previews of their watch face changes in real time on both the watch and phone.\n\nIf you are still using the Wearable Support Library, we strongly encourage migrating to the new Jetpack libraries to take advantage of the new APIs and upcoming features and bug fixes.",
      "url": "https://android-developers.googleblog.com/2021/12/develop-watch-faces-with-stable-jetpack.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-P4S1eEhqouE/YaaFy_bGD1I/AAAAAAAARNA/-w5O05Mppo8pe0hoeMC1yDNRWiX_mnTOgCLcBGAsYHQ/s0/image1.png",
      "publishDate": "2021-12-01T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "21",
        "11",
        "8"
      ],
      "authors": [
        "22"
      ]
    },
    {
      "id": "72",
      "episodeId": "54",
      "title": "Rebuilding our Guide to App Architecture 📐",
      "content": "We launched a revamped guide to app architecture which includes best practices. As Android apps grow in size, it’s important to design the code with an architecture in place that allows the app to scale, improves quality and robustness, and makes testing easier. The guide contains pages for UI, domain, and data layers including deep dives into more complex topics, such as how to handle UI events. We also have a learning pathway to walk you through it.",
      "url": "https://android-developers.googleblog.com/2021/12/rebuilding-our-guide-to-app-architecture.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgnJ0CCtKClhEOE_BDOoWiXGr2eA6LWjn-RPvFjFx8Va97f_1_xCmpF3uI_bUILoQPqJUDlXUbIRVPjvi3oCiFtRVZlcAAkHBa1cJlufG5OvmeovQeiHgH9bLhxREufi-fw7FnxIcmxGmzWuW0DmYUZolsM6rywTSZIm3KtI6yx9jSIeRpuYzRZubke",
      "publishDate": "2021-12-14T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "16"
      ],
      "authors": [
        "23"
      ]
    },
    {
      "id": "73",
      "episodeId": "54",
      "title": "Google Play Games on PC Beta 🎮",
      "content": "We announced that we’re opening sign-ups for Google Play Games on PC as a beta in Korea, Taiwan, and Hong Kong, allowing users participating in the beta to play a catalog of Google Play games on their PC via a standalone application built by Google. The developer site has a form to express interest, along with information about bringing your Android game to PCs. It involves many of the same updates that you do to optimize your game for Chrome OS devices, such as support for Mouse and Keyboard controls.",
      "url": "https://developers.googleblog.com/2022/01/googleplaygames.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgsNv-PVLNLlX2SYd2p5DwTN2Jxwb54Rc7Ekbm0LgcFuwHBrF_5Y-DiUblL9oTjmeJ1Y44nPRMMkH5K-xlC0OApgUGxqBpUcfuV1LYPVvKsI67BKTpc_gNhaHsNda6Q1Uk1UvTznmMydqNHtXSqTgSJbjpQCoTGZM_ZLXlkGwMoBFfnMQkAIdl2zjsC",
      "publishDate": "2022-01-19T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "20"
      ],
      "authors": [
        "24"
      ]
    },
    {
      "id": "74",
      "episodeId": "54",
      "title": "MAD Skills: Gradle 🐘",
      "content": "Murat covered building custom plugins in more depth, including the Artifact API in addition to the Variant API covered previously. It demonstrates building a plugin which automatically updates the version code specified in the app manifest with the git version. With the AGP 7.0 release, you can use these APIs to control build inputs, read, modify, or even replace intermediate and final artifacts.",
      "url": "https://medium.com/androiddevelopers/gradle-and-agp-build-apis-taking-your-plugin-to-the-next-step-95e7bd1cd4c9",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*WkRft2aAKv19MoIm.jpeg",
      "publishDate": "2021-12-01T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "14"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "75",
      "episodeId": "54",
      "title": "Gradle and AGP Build APIs: Community tip - MAD Skills",
      "content": "On this episode of Gradle and AGP Build APIs for MAD Skills, Alex Saveau walks you through manipulating Android build artifacts with the Android Gradle Plugin (AGP) and Gradle APIs.",
      "url": "https://www.youtube.com/watch?v=8SFfffaB0CU",
      "headerImageUrl": "https://i3.ytimg.com/vi/8SFfffaB0CU/maxresdefault.jpg",
      "publishDate": "2021-12-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "14"
      ],
      "authors": [
        "26"
      ]
    },
    {
      "id": "76",
      "episodeId": "54",
      "title": "Gradle and AGP Build APIs: Taking your plugin to the next step - MAD Skills",
      "content": "On this episode of Gradle and AGP Build APIs for MAD Skills, Murat will discuss Gradle tasks, providers, properties, and basics of task inputs and outputs. Next, you will be able to take your plugin a step further and learn how to get access to various build artifacts using the new Artifact API. ",
      "url": "https://www.youtube.com/watch?v=SB4QlngQQW0",
      "headerImageUrl": "https://i3.ytimg.com/vi/SB4QlngQQW0/maxresdefault.jpg",
      "publishDate": "2021-11-29T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "14"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "77",
      "episodeId": "54",
      "title": "MAD Skills Gradle and AGP build APIs Wrap Up!",
      "content": "This wrap-up post summarizes the whole MAD Skills Gradle series",
      "url": "https://android-developers.googleblog.com/2021/12/mad-skills-gradle-and-agp-build-apis.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgo1Fw61B9qtQESKdVJzcNXOG0RzhA2k85zkDMDNidBiQY7B6uguHXQ9t9IPB9BiHS0WTB1b4fwIgeN5zEIJrmznF9pt5lu9186wvXxJ3IKfLi8Fci8LyMDbQKGYc7nnijJ9_lhrNHtRQamaF2GTSXyJq5_lQk7we3cSfSviOxhgKN9TscMJaGgdMZJ",
      "publishDate": "2021-12-16T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "14"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "78",
      "episodeId": "54",
      "title": "MAD Skills: DataStore 🗄️",
      "content": "Simona began MAD Skills: DataStore. DataStore is a thread-safe, non-blocking library in Android Jetpack that provides a safe and consistent way to store small amounts of data, such as preferences or application state, replacing SharedPreferences. It provides an implementation that stores typed objects backed by protocol buffers (Proto DataStore) and an implementation that stores key-value pairs (Preferences DataStore).",
      "url": "https://www.youtube.com/watch?v=9ws-cJzlJkU",
      "headerImageUrl": "https://i3.ytimg.com/vi/9ws-cJzlJkU/maxresdefault.jpg",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "9"
      ],
      "authors": [
        "3"
      ]
    },
    {
      "id": "79",
      "episodeId": "54",
      "title": "AndroidX releases 🚀",
      "content": "Since the last Now in Android episode, a lot of libraries were promoted to stable! Compose ConstraintLayout brings support for ConstraintLayout syntax to Compose. We also released CoordinatorLayout 1.2, Car App 1.1.0, Room 2.4.0, Sqlite 2.2.0, Collection 1.2.0, and Wear Watchface 1.0.0.\n\nOur first alpha of Jetpack Compose 1.2 was released, along with alphas for Glance 1.0.0, Core-Ktx 1.8.0, WorkManager 2.8.0, Mediarouter 1.3.0, Emoji2 1.1.0, Annotation 1.4.0, Core-RemoteViews, Core-Peformance, and more.",
      "url": "https://developer.android.com/jetpack/androidx/versions/all-channel#december_1_2021",
      "headerImageUrl": "",
      "publishDate": "2021-12-01T00:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "8"
      ],
      "authors": []
    },
    {
      "id": "80",
      "episodeId": "54",
      "title": "Jetnews for every screen",
      "content": "Alex wrote about the recent updates to Jetnews that improves its behavior across big and small mobile devices. It describes our design and development process so that you can learn our philosophy and associated implementation steps for building an application optimized for all screens with Jetpack Compose, including how to build a list/detail layout.",
      "url": "https://medium.com/androiddevelopers/jetnews-for-every-screen-4d8e7927752",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*678DlYtu4G7wFrq30FQ7Mw.png",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "22"
      ]
    },
    {
      "id": "81",
      "episodeId": "54",
      "title": "Simplifying drag and drop",
      "content": "Paul wrote about drag & drop, and how the Android Jetpack DragAndDrop library alpha makes it easier to handle data dropped into your app.",
      "url": "https://medium.com/androiddevelopers/simplifying-drag-and-drop-3713d6ef526e",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*pUe4RBLe7FVlISDtAqeQ4Q.png",
      "publishDate": "2021-12-15T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "8"
      ],
      "authors": [
        "27"
      ]
    },
    {
      "id": "82",
      "episodeId": "54",
      "title": "Accessibility series 🌐: Handling content that times out - Accessibility on Android",
      "content": "The accessibility series continues on, beginning with an episode on how to properly implement UI elements that disappear after a set amount of time.",
      "url": "https://www.youtube.com/watch?v=X97P6Y8WHl0",
      "headerImageUrl": "https://i3.ytimg.com/vi/X97P6Y8WHl0/maxresdefault.jpg",
      "publishDate": "2021-12-03T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "17"
      ],
      "authors": [
        "28"
      ]
    },
    {
      "id": "83",
      "episodeId": "54",
      "title": "Accessibility series 🌐: Acessibility Scanner",
      "content": "We also cover how Accessibility Scanner can help you improve your app for all users by suggesting improvements in areas of accessibility.",
      "url": "https://www.youtube.com/watch?v=i1gMzQv0hWU",
      "headerImageUrl": "https://i3.ytimg.com/vi/i1gMzQv0hWU/maxresdefault.jpg",
      "publishDate": "2021-12-10T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "17"
      ],
      "authors": [
        "28"
      ]
    },
    {
      "id": "84",
      "episodeId": "54",
      "title": "Accessibility series 🌐: Accessibility test framework and Espresso - Accessibility on Android",
      "content": "We investigate how Espresso and the Accessibility Test Framework can help you create automated accessibility tests.",
      "url": "https://www.youtube.com/watch?v=DLN2s16HwcE",
      "headerImageUrl": "https://i3.ytimg.com/vi/DLN2s16HwcE/maxresdefault.jpg",
      "publishDate": "2021-12-22T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "17"
      ],
      "authors": [
        "28"
      ]
    },
    {
      "id": "85",
      "episodeId": "54",
      "title": "Android TV & Google TV 📺",
      "content": "Mayuri covered best practices for the Watch Next API on Android TV & Google TV, which increases engagement with your app by allowing your content to show up in the Watch Next row.",
      "url": "https://www.youtube.com/watch?v=QFMIP5GOo70",
      "headerImageUrl": "https://i3.ytimg.com/vi/QFMIP5GOo70/maxresdefault.jpg",
      "publishDate": "2022-01-14T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "19"
      ],
      "authors": [
        "29"
      ]
    },
    {
      "id": "86",
      "episodeId": "54",
      "title": "ADB Podcast 179: Flibberty Widget",
      "content": "In this episode, Chet and Romain talked with Nicole McWilliams and Petr Čermák from the London engineering office about their work on App Widgets and Digital Wellbeing.",
      "url": "https://adbackstage.libsyn.com/flibberty-widget",
      "headerImageUrl": "https://ssl-static.libsyn.com/p/assets/4/0/e/c/40ec1fb11096bffed959afa2a1bf1c87/adb-180-flibberty-widget.png",
      "publishDate": "2021-11-30T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "15"
      ],
      "authors": [
        "30",
        "31",
        "32",
        "33"
      ]
    },
    {
      "id": "87",
      "episodeId": "54",
      "title": "ADB Podcast 180: Kotlin Magic Platform",
      "content": "In this episode, we chat with Yigit Boyar from the Android Toolkit Team about Kotlin multi platform, while Romain provides light background music on his piano.",
      "url": "https://adbackstage.libsyn.com/episode-180-kotlin-magic-platform",
      "headerImageUrl": "https://ssl-static.libsyn.com/p/assets/2/6/2/5/262599d4ce76d20fa04421dee9605cbd/adb-181-kmp.png",
      "publishDate": "2021-12-16T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "10"
      ],
      "authors": [
        "30",
        "31",
        "32",
        "34"
      ]
    },
    {
      "id": "88",
      "episodeId": "54",
      "title": "ADB Podcast 181: Architecture → Fewer bugs at the end",
      "content": "In this episode, we chat with Yigit Boyar (again!) from the Android Toolkit Team and Manuel Vivo from the Developer Relations team about application architecture. The team has released new architecture guidance, and we talk about that guidance here, as well as how our architecture recommendations apply in the new Jetpack Compose world.",
      "url": "https://adbackstage.libsyn.com/episode-181-architecture-fewer-bugs-at-the-end",
      "headerImageUrl": "https://ssl-static.libsyn.com/p/assets/8/d/1/3/8d137b65f392a68c27a2322813b393ee/ADB_181_Architecture.png",
      "publishDate": "2022-01-11T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "16"
      ],
      "authors": [
        "31",
        "31",
        "32",
        "23"
      ]
    },
    {
      "id": "89",
      "episodeId": "53",
      "title": "Android 12",
      "content": "We released Android 12 and pushed it to the Android Open Source Project (AOSP). We introduced a new design language called Material You. We reduced the CPU time used by core system services, added performance class device capabilities, and added new features to improve performance. Users have more control of their privacy with the Privacy Dashboard and other new security and privacy features. We improved the user experience with a unified API for rich content insertion, compatible media transcoding, easier blurs and effects, AVIF image support, enhanced haptics, new camera effects/capabilities, improved native crash debugging, support for rounded screen corners, Play as you download, and Game Mode APIs.",
      "url": "https://android-developers.googleblog.com/2021/10/android-12-is-live-in-aosp.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-mGlzRmn42Rs/YVstltyrboI/AAAAAAAAK3A/44QpoNJDeuoHhlgrRJSbk0L_ZopgFDLFACLcBGAsYHQ/s0/Android%2B12%2Blogo.png",
      "publishDate": "2021-10-03T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "15"
      ],
      "authors": [
        "14"
      ]
    },
    {
      "id": "90",
      "episodeId": "53",
      "title": "Compose",
      "content": "Jetpack Compose, Android’s modern, native UI toolkit became stable and ready for you to adopt in production. It interoperates with your existing app, integrates with existing Jetpack libraries, implements Material Design with straightforward theming, supports lists with Lazy components using minimal boilerplate, and has a powerful, extensible animation system. You can learn more about working with Compose in the Compose learning path and see where we’re going in future Compose releases in the Compose roadmap.",
      "url": "https://developer.android.com/jetpack/compose",
      "headerImageUrl": "",
      "publishDate": "2021-12-07T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "11"
      ],
      "authors": []
    },
    {
      "id": "91",
      "episodeId": "53",
      "title": "Training",
      "content": "This year, the Android Training Team released the final four new units of Android Basics in Kotlin.",
      "url": "https://developer.android.com/courses/android-basics-kotlin/course",
      "headerImageUrl": "https://developer.android.com/images/hero-assets/android-basics-kotlin.svg",
      "publishDate": "2021-12-07T00:00:00.000Z",
      "type": "Codelab",
      "topics": [
        "10"
      ],
      "authors": []
    },
    {
      "id": "92",
      "episodeId": "53",
      "title": "Introduction to Kotlin and Jetpack ",
      "content": "Learn the basics of Jetpack KTX libraries, how to simplify callbacks with coroutines and Flow, and how to use and test Room/WorkManager APIs.",
      "url": "https://youtu.be/nw7nnlHDkHw?list=PLWz5rJ2EKKc98e0f5ZbsgB63MdjZTFgsy",
      "headerImageUrl": "https://i3.ytimg.com/vi/nw7nnlHDkHw/maxresdefault.jpg",
      "publishDate": "2021-12-14T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "10",
        "8"
      ],
      "authors": [
        "6"
      ]
    },
    {
      "id": "93",
      "episodeId": "53",
      "title": "Introduction to Motion Layout",
      "content": "Learn how to use MotionLayout and its design tool to create rich, animated experiences.",
      "url": "https://www.youtube.com/watch?v=M1jE3W3_NTQ&list=PLWz5rJ2EKKc_PEOEHNBEyy6tPX1EgtUw2",
      "headerImageUrl": "https://i3.ytimg.com/vi/M1jE3W3_NTQ/maxresdefault.jpg",
      "publishDate": "2022-01-19T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "10",
        "3",
        "8"
      ],
      "authors": [
        "35"
      ]
    },
    {
      "id": "94",
      "episodeId": "53",
      "title": "Introduction to WorkManager",
      "content": "Learn how to schedule critical background work with WorkManager: from basic usage, threading, custom configuration and more.",
      "url": "https://www.youtube.com/watch?v=NtpgWjiXEfg&list=PLWz5rJ2EKKc_J88-h0PhCO_aV0HIAs9Qk",
      "headerImageUrl": "https://i3.ytimg.com/vi/NtpgWjiXEfg/maxresdefault.jpg",
      "publishDate": "2022-03-01T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "8"
      ],
      "authors": [
        "36"
      ]
    },
    {
      "id": "95",
      "episodeId": "53",
      "title": "Introduction to Navigation",
      "content": "Learn the basics of the Navigation component, specific features of the tool and the APIs to create and navigate to destinations.",
      "url": "https://www.youtube.com/watch?list=PLWz5rJ2EKKc9VpBMZUS9geQtc5RJ2RsUd&v=fiQiMy0HzsY&feature=emb_title",
      "headerImageUrl": "https://i3.ytimg.com/vi/fiQiMy0HzsY/maxresdefault.jpg",
      "publishDate": "2022-03-25T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "8"
      ],
      "authors": [
        "31"
      ]
    },
    {
      "id": "96",
      "episodeId": "53",
      "title": "Introduction to Performance",
      "content": "Learn about using system tracing and sampling profiling to debug performance issues in apps.",
      "url": "https://www.youtube.com/watch?v=_5LgIrd4O5g&list=PLWz5rJ2EKKc-xjSI-rWn9SViXivBhQUnp",
      "headerImageUrl": "https://i3.ytimg.com/vi/_5LgIrd4O5g/maxresdefault.jpg",
      "publishDate": "2021-07-18T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "5"
      ],
      "authors": [
        "37"
      ]
    },
    {
      "id": "97",
      "episodeId": "53",
      "title": "Introduction to Hilt",
      "content": "Learn how to add and use Hilt for dependency injection in your Android app, best practices for testing with Hilt, and more advanced content.",
      "url": "https://www.youtube.com/watch?v=mnMCgjuMJPA&list=PLWz5rJ2EKKc_9Qo-RBRYhVmME1iR4oeTK",
      "headerImageUrl": "https://i3.ytimg.com/vi/mnMCgjuMJPA/maxresdefault.jpg",
      "publishDate": "2021-08-22T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "8"
      ],
      "authors": [
        "23"
      ]
    },
    {
      "id": "98",
      "episodeId": "53",
      "title": "Paging",
      "content": "Learn the basics of paging, from the core types to binding them to your UI elements.",
      "url": "https://www.youtube.com/watch?v=Pw-jhS-ucYA&list=PLWz5rJ2EKKc9L-fmWJLhyXrdPi1YKmvqS",
      "headerImageUrl": "https://i3.ytimg.com/vi/Pw-jhS-ucYA/maxresdefault.jpg",
      "publishDate": "2021-09-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "8"
      ],
      "authors": [
        "38"
      ]
    },
    {
      "id": "99",
      "episodeId": "53",
      "title": "Introduction to Gradle and AGP Build APIs\n",
      "content": "Learn how to configure your build, customize the build process to your needs and how to write your own plugins to extend your build even further.",
      "url": "https://www.youtube.com/watch?v=mk0XBWenod8&list=PLWz5rJ2EKKc8fyNmwKXYvA2CqxMhXqKXX",
      "headerImageUrl": "https://i3.ytimg.com/vi/mk0XBWenod8/maxresdefault.jpg",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "14",
        "7"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "100",
      "episodeId": "53",
      "title": "Google I/O",
      "content": "At I/O we released updates in Jetpack, Compose, Android Studio tooling, Large screens, Wear OS, Testing, and more! Get caught up on all the Android videos from I/O!",
      "url": "https://www.youtube.com/watch?v=D_mVOAXcrtc",
      "headerImageUrl": "https://i3.ytimg.com/vi/D_mVOAXcrtc/maxresdefault.jpg",
      "publishDate": "2021-05-17T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "2"
      ],
      "authors": []
    },
    {
      "id": "101",
      "episodeId": "53",
      "title": "Android Dev Summit",
      "content": "At Android Dev Summit we released updates on privacy and security, large screens, Android 12, Google Play & Games, Building across screens, Jetpack Compose, Modern Android Development and more. Check out all the videos from ADS!",
      "url": "https://www.youtube.com/watch?v=WZgR5Yf1iq8",
      "headerImageUrl": "https://i3.ytimg.com/vi/WZgR5Yf1iq8/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "2"
      ],
      "authors": []
    },
    {
      "id": "102",
      "episodeId": "52",
      "title": "Conveying state for Accessibility",
      "content": "In this episode of the Accessibility series, you can learn more about the StateDescription API, when to use stateDescription and contentDescription, and how to represent error states to the end user.",
      "url": "https://youtu.be/JvWM2PjLJls",
      "headerImageUrl": "https://i.ytimg.com/vi/JvWM2PjLJls/maxresdefault.jpg",
      "publishDate": "2021-11-30T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": [
        "39"
      ]
    },
    {
      "id": "103",
      "episodeId": "52",
      "title": "Take your Gradle plugin to the next step",
      "content": "This third and last episode of the Gradle MAD Skills series teaches you how to get access to various build artifacts using the new Artifact API.",
      "url": "https://youtu.be/SB4QlngQQW0",
      "headerImageUrl": "https://i.ytimg.com/vi/SB4QlngQQW0/maxresdefault.jpg",
      "publishDate": "2021-11-29T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "14"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "104",
      "episodeId": "52",
      "title": "How to write a Gradle plugin",
      "content": "In this second episode of the Gradle MAD Skills series, Murat explains how to write your own custom Gradle plugin.",
      "url": "https://youtu.be/LPzBVtwGxlo",
      "headerImageUrl": "https://i.ytimg.com/vi/LPzBVtwGxlo/maxresdefault.jpg",
      "publishDate": "2021-11-22T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "14"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "105",
      "episodeId": "52",
      "title": "Convert YUV to RGB for CameraX Image Analysis",
      "content": "Learn about a new feature in CameraX to convert YUV, the format that CameraX produces, to RGB used for image analysis capabilities available in TensorFlow Lite, for example. Read the blog post for more information about these formats and how to use the new conversion feature.",
      "url": "https://medium.com/androiddevelopers/convert-yuv-to-rgb-for-camerax-imageanalysis-6c627f3a0292",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*cuOorbZgMbRvkSSGuDGccw.png",
      "publishDate": "2021-11-19T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "8",
        "6"
      ],
      "authors": [
        "40"
      ]
    },
    {
      "id": "106",
      "episodeId": "52",
      "title": "AppCompat, Activity, and Fragment to support multiple back stacks",
      "content": "The 1.4.0 release of these libraries brings stable support for multiple back stacks.",
      "url": "https://developer.android.com/jetpack/androidx/releases/appcompat#1.4.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "8",
        "3"
      ],
      "authors": []
    },
    {
      "id": "107",
      "episodeId": "52",
      "title": "Emoji2 adds support for modern emojis",
      "content": "The 1.0 stable release of Emoji2 allows you to use modern emojis in your app.",
      "url": "https://developer.android.com/jetpack/androidx/releases/emoji2#1.0.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "8",
        "3"
      ],
      "authors": []
    },
    {
      "id": "108",
      "episodeId": "52",
      "title": "Lifecycle introduces lifecycle-aware coroutine APIs",
      "content": "The new 2.4 release of Lifecycle introduces repeatOnLifecycle and flowWithLifecycle.",
      "url": "https://developer.android.com/jetpack/androidx/releases/lifecycle#2.4.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "8",
        "16"
      ],
      "authors": []
    },
    {
      "id": "109",
      "episodeId": "52",
      "title": "Paging release brings changes to LoadState",
      "content": "The new 3.1 release of Paging changes the behavior of LoadState.",
      "url": "https://developer.android.com/jetpack/androidx/releases/paging#3.1.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "8",
        "3"
      ],
      "authors": []
    },
    {
      "id": "110",
      "episodeId": "52",
      "title": "Wear tiles released as 1.0 stable",
      "content": "The library that you use to build custom tiles for Wear OS devices is now stable.",
      "url": "https://developer.android.com/jetpack/androidx/releases/wear-tiles#1.0.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "8",
        "21"
      ],
      "authors": []
    },
    {
      "id": "111",
      "episodeId": "52",
      "title": "About Custom Accessibility Actions",
      "content": "The accessibility series continues on with more information on how to create custom accessibility actions to make your apps more accessible. You can provide a custom action to the accessibility services and implement logic related to the action. For more information, check out the following episode!",
      "url": "https://youtu.be/wWDYIGk0Kdo",
      "headerImageUrl": "https://i.ytimg.com/vi/wWDYIGk0Kdo/maxresdefault.jpg",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": [
        "39"
      ]
    },
    {
      "id": "112",
      "episodeId": "52",
      "title": "Improving App Startup: Lessons from the Facebook App",
      "content": "Improving app startup time is not a trivial task and requires a deep understanding of things that affect it. This year, the Android team and the Facebook app team have been working together on metrics and sharing approaches to improve app startup. Read more about the findings in this blog post.",
      "url": "https://android-developers.googleblog.com/2021/11/improving-app-startup-facebook-app.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-5VyrQpFJufM/YaVKxf_DanI/AAAAAAAALS4/ybeza_emDKoKP0gjiNkqfDS_ltwo0075ACLcBGAsYHQ/w1200-h630-p-k-no-nu/AppExcellence_Editorial_LessonsFromFBApp_4209x1253-01%2B%25281%2529%2B%25281%2529.png",
      "publishDate": "2021-11-16T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "5"
      ],
      "authors": []
    },
    {
      "id": "113",
      "episodeId": "52",
      "title": "Gradle series kicks off",
      "content": "Murat introduces the Gradle series and everything you'll learn in it.",
      "url": "https://youtu.be/mk0XBWenod8",
      "headerImageUrl": "https://i.ytimg.com/vi/mk0XBWenod8/maxresdefault.jpg",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "14"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "114",
      "episodeId": "52",
      "title": "Intro to Gradle and AGP",
      "content": "In the first episode of the Gradle MAD Skills series, Murat explains how the Android build system works, and how to configure your build.",
      "url": "https://youtu.be/GjPS4xDMmQY",
      "headerImageUrl": "https://i.ytimg.com/vi/GjPS4xDMmQY/maxresdefault.jpg",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "14"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "115",
      "episodeId": "52",
      "title": "ADB Podcast episode 179 Hosts 3, Guests 0",
      "content": "Chet, Romain and Tor sit down to chat about the Android Developer Summit, and in particular all the new features arriving in Android Studio, along with a few other topics like Chet’s new jank stats library, the Android 12L release, and more.",
      "url": "https://adbackstage.libsyn.com/episode-178-hosts-3-guests-0",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "7",
        "5",
        "15"
      ],
      "authors": [
        "31"
      ]
    },
    {
      "id": "116",
      "episodeId": "52",
      "title": "The problem with emojis and how emoji2 can help out",
      "content": "Meghan wrote about the new emoji2 library that just became stable.",
      "url": "https://medium.com/androiddevelopers/support-modern-emoji-99f6dea8e57f",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*yAOOlpXKKUl5nWWsPkNb7g.png",
      "publishDate": "2021-11-12T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "8",
        "3"
      ],
      "authors": [
        "15"
      ]
    },
    {
      "id": "117",
      "episodeId": "52",
      "title": "Paging Q&A",
      "content": "In this live session, TJ and Dustin answered your questions in the usual live Q&A format.",
      "url": "https://youtu.be/8i6vrlbIVCc",
      "headerImageUrl": "https://i.ytimg.com/vi/8i6vrlbIVCc/maxresdefault.jpg",
      "publishDate": "2021-11-11T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "118",
      "episodeId": "52",
      "title": "Thanks for helping us reach 1M YouTube Subscribers",
      "content": "Thank you everyone for following the Now in Android series and everything the Android Developers YouTube channel has to offer. During the Android Developer Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to thank you all.",
      "url": "https://youtu.be/-fJ6poHQrjM",
      "headerImageUrl": "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
      "publishDate": "2021-11-09T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "2"
      ],
      "authors": []
    },
    {
      "id": "119",
      "episodeId": "52",
      "title": "Community tip on Paging",
      "content": "Tips for using the Paging library from the developer community",
      "url": "https://youtu.be/r5JgIyS3t3s",
      "headerImageUrl": "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
      "publishDate": "2021-11-08T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "120",
      "episodeId": "52",
      "title": "Transformations and customisations in the Paging Library",
      "content": "A demonstration of different operations that can be performed with Paging. Transformations like inserting separators, when to create a new pager, and customisation options for consuming PagingData.",
      "url": "https://youtu.be/ZARz0pjm5YM",
      "headerImageUrl": "https://i.ytimg.com/vi/ZARz0pjm5YM/maxresdefault.jpg",
      "publishDate": "2021-11-01T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": [
        "38"
      ]
    },
    {
      "id": "121",
      "episodeId": "52",
      "title": "New Compose for Wear OS codelab",
      "content": "In this codelab, you can learn how Wear OS can work with Compose, what Wear OS specific composables are available, and more!",
      "url": "https://developer.android.com/codelabs/compose-for-wear-os",
      "headerImageUrl": "https://developer.android.com/codelabs/compose-for-wear-os/img/4d28d16f3f514083.png",
      "publishDate": "2021-10-27T23:00:00.000Z",
      "type": "Codelab",
      "topics": [
        "11",
        "21"
      ],
      "authors": [
        "41"
      ]
    },
    {
      "id": "122",
      "episodeId": "52",
      "title": "Introducing Jetpack Media3",
      "content": "The first alpha version of this new library is now available. Media3 is a collection of support libraries for media playback, including ExoPlayer. The following article explains why the team created Media3, what it contains, and how it can simplify your app architecture.",
      "url": "https://developer.android.com/jetpack/androidx/releases/media3",
      "headerImageUrl": "",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "8",
        "6"
      ],
      "authors": [
        "42"
      ]
    },
    {
      "id": "123",
      "episodeId": "50",
      "title": "Building apps which are private by design",
      "content": "Sara N-Marandi, product manager, and Yacine Rezgui, developer relations engineer, provided guidelines and best practices on how to build apps that are private by design, covered new privacy features in Android 12 and previewed upcoming Android concepts.",
      "url": "https://youtu.be/hBVwr2ErQCw",
      "headerImageUrl": "https://i.ytimg.com/vi/hBVwr2ErQCw/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12"
      ],
      "authors": []
    },
    {
      "id": "124",
      "episodeId": "50",
      "title": "Memory Safety Tools",
      "content": "Serban Constantinescu, product manager, talked about the Memory Safety Tools that became available starting in Android 11 and have continued to evolve in Android 12. These tools can help address memory bugs and improve the quality and security of your application.",
      "url": "https://youtu.be/JqLcTFpXreg",
      "headerImageUrl": "https://i.ytimg.com/vi/JqLcTFpXreg/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12"
      ],
      "authors": []
    },
    {
      "id": "125",
      "episodeId": "50",
      "title": "Increasing User Transparency with Privacy Dashboard",
      "content": "Android is ever evolving in its quest to protect users’ privacy. In Android 12, the platform increases transparency by introducing Privacy Dashboard, which gives users a simple and clear timeline view of the apps that have accessed location, microphone and camera within the past 24 hours. ",
      "url": "https://medium.com/androiddevelopers/increasing-user-transparency-with-privacy-dashboard-23064f2d7ff6",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*cgaSAY9AvPWlndLimzIIzQ.png",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "12"
      ],
      "authors": [
        "15"
      ]
    },
    {
      "id": "126",
      "episodeId": "50",
      "title": "The most unusual and interesting security issues addressed last year",
      "content": "Lilian Young, software engineer, presented a selection of the most unusual, intricate, and interesting security issues addressed in the last year. Developers and researchers are able to contribute to the security of the Android platform by submitting to the Android Vulnerability Rewards Program.",
      "url": "https://medium.com/androiddevelopers/now-in-android-50-ads-special-9934422f8dd1",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*6h0XYdyki_1jfImJ",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "12"
      ],
      "authors": [
        "43"
      ]
    },
    {
      "id": "127",
      "episodeId": "50",
      "title": "New Data Safety section in the Play Console",
      "content": "The new Data safety section will give you a simple way to showcase your app’s overall safety. It gives you a place to give users deeper insight into your app’s privacy and security practices, and explain the data your app may collect and why — all before users install.",
      "url": "https://youtu.be/J7TM0Yy0aTQ",
      "headerImageUrl": "https://i.ytimg.com/vi/J7TM0Yy0aTQ/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12",
        "13"
      ],
      "authors": []
    },
    {
      "id": "128",
      "episodeId": "50",
      "title": "Building Android UIs for any screen size",
      "content": "Clara Bayarri, engineering manager and Daniel Jacobson, product manager, talked about the state of the ecosystem, focusing on new design guidance, APIs, and tools to help you make the most of your UI on different screen sizes.",
      "url": "https://youtu.be/ir3LztqbeRI",
      "headerImageUrl": "https://i.ytimg.com/vi/ir3LztqbeRI/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "129",
      "episodeId": "50",
      "title": "What's new for large screens & foldables",
      "content": "Emilie Roberts, Chrome OS developer advocate and Andrii Kulian, Android software engineer, introduced new features focused specifically on making apps look great on large screens, foldables, and Chrome OS. ",
      "url": "https://youtu.be/6-925K3hMHU",
      "headerImageUrl": "https://i.ytimg.com/vi/6-925K3hMHU/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "130",
      "episodeId": "50",
      "title": "Enable great input support for all devices",
      "content": "Users expect seamless experiences when using keyboards, mice, and stylus. Emilie Roberts taught us how to handle common keyboard and mouse input events and how to get started with more advanced support like keyboard shortcuts, low-latency styluses, MIDI, and more.",
      "url": "https://youtu.be/piLEZYTc_4g",
      "headerImageUrl": "https://i.ytimg.com/vi/piLEZYTc_4g/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "131",
      "episodeId": "50",
      "title": "Best practices for video apps on foldable devices",
      "content": "Francesco Romano, developer advocate, and Will Chan, product manager at Zoom explored new user experiences made possible by the foldable form factor, focusing on video conferencing and media applications. ",
      "url": "https://youtu.be/DBAek_P0nEw",
      "headerImageUrl": "https://i.ytimg.com/vi/DBAek_P0nEw/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3",
        "6"
      ],
      "authors": []
    },
    {
      "id": "132",
      "episodeId": "50",
      "title": "Design beautiful apps on foldables and large screens",
      "content": "Liam Spradlin, design advocate, and Jonathan Koren, developer relations engineer, talked about how to design and test Android applications that look and feel great across device types and screen sizes, from tablets to foldables to Chrome OS.",
      "url": "https://youtu.be/DJeJIJKOUbI",
      "headerImageUrl": "https://i.ytimg.com/vi/DJeJIJKOUbI/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "133",
      "episodeId": "50",
      "title": "12L and new Android APIs and tools for large screens",
      "content": "Dave Burke, vice president of engineering, wrote a post covering the developer preview of 12L, an upcoming feature drop that makes Android 12 even better on large screens. ",
      "url": "https://android-developers.googleblog.com/2021/10/12L-preview-large-screens.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-sjT5kFGiQtg/YXlpg0uByLI/AAAAAAAARJk/XHO_uo5bRJcMeQVm0Fn1wN-qe54FGI7MgCLcBGAsYHQ/w1200-h630-p-k-no-nu/12L-devices-hero.png",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3",
        "15"
      ],
      "authors": []
    },
    {
      "id": "134",
      "episodeId": "50",
      "title": "New features in ML Kit: Text Recognition V2 & Pose Detections",
      "content": "Zongmin Sun, software engineer, and Valentin Bazarevsky, MediaPipe Engineer, talked about Text Recognition V2 & Pose Detection, recently-released features in ML Kit. ",
      "url": "https://youtu.be/9EKQ0UC04S8",
      "headerImageUrl": "https://i.ytimg.com/vi/9EKQ0UC04S8/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "8",
        "3"
      ],
      "authors": []
    },
    {
      "id": "135",
      "episodeId": "50",
      "title": "How to retain users with Android backup and restore",
      "content": "In this talk, Martin Millmore, engineering manager, and Ruslan Tkhakokhov, software engineer, explored the benefits of transferring users’ data to a new device, using Backup and Restore to achieve that in a simple and secure way.",
      "url": "https://youtu.be/bg2drEhz1_s",
      "headerImageUrl": "https://i.ytimg.com/vi/bg2drEhz1_s/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": []
    },
    {
      "id": "136",
      "episodeId": "50",
      "title": "Compatibility changes in Android 12",
      "content": "Developer relations engineers Kseniia Shumelchyk and Slava Panasenko talked about new Android 12 features and changes. They shared tools and techniques to ensure that apps are compatible with the next Android release and users can take advantage of new features, along with app developer success stories.",
      "url": "https://youtu.be/fCMJmV6nqGo",
      "headerImageUrl": "https://i.ytimg.com/vi/fCMJmV6nqGo/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": []
    },
    {
      "id": "137",
      "episodeId": "50",
      "title": "Building great experiences for Novice Internet Users",
      "content": "Learn the principles to help craft great experiences for the novice Internet user segment from Mrinal Sharma, UX manager, and Amrit Sanjeev, developer relations engineer. They highlight the gap between nascent and tech savvy user segments and suggest strategies in areas to improve the overall user experience. Factors like low functional literacy, being multilingual by default, being less digitally confident, and having no prior internet experience requires that we rethink the way we build apps for these users.",
      "url": "https://youtu.be/Sf_TauUY4LE",
      "headerImageUrl": "https://i.ytimg.com/vi/Sf_TauUY4LE/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "138",
      "episodeId": "49",
      "title": "Android Basics in Kotlin course 🧑‍💻",
      "content": "Android Basics in Kotlin teaches people with no programming experience how to build simple Android apps. Since the first learning units were released in 2020, over 100,000 beginners have completed it! Today, we’re excited to share that the final unit has been released, and the full Android Basics in Kotlin course is now available.",
      "url": "https://android-developers.googleblog.com/2021/10/announcing-android-basics-in-kotlin.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-BmlW7k8RhME/YWRvsOes9aI/AAAAAAAAQ_g/FpFS6_new9Y7vdzP7P4RPs_x4WHVi4yxQCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-announcing-android-basics-in-Kotlin-course-16x9.png",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "10"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "139",
      "episodeId": "49",
      "title": "WorkManager 2.7 adds setExpedited API to help with Foreground Service restrictions",
      "content": "As the most outstanding release this time, WorkManager 2.7 was promoted to stable. This new version introduces a new setExpedited API to help with Foreground Service restrictions in Android 12.",
      "url": "https://developer.android.com/reference/android/app/job/JobInfo.Builder#setExpedited(boolean)",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "API change",
      "topics": [
        "16"
      ],
      "authors": []
    },
    {
      "id": "140",
      "episodeId": "49",
      "title": "Updated Widget docs",
      "content": "Widgets can make a huge impact on your user’s home screen! We updated the App Widgets documentation with the recent changes in the latest OS versions. New pages about how to create a simple widget, an advanced widget, and how to provide flexible widget layouts.",
      "url": "https://developer.android.com/guide/topics/appwidgets",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "141",
      "episodeId": "49",
      "title": "Extend AGP by creating your own plugins",
      "content": "The Android Gradle Plugin (AGP) contains extension points for plugins to control build inputs and extend its functionality. Starting in version 7.0, AGP has a set of official, stable APIs that you can rely on. We also have a new documentation page that walks you through this and explains how to create your own plugins.",
      "url": "https://developer.android.com/studio/build/extend-agp",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "14",
        "7"
      ],
      "authors": []
    },
    {
      "id": "142",
      "episodeId": "49",
      "title": "Revamped Compose Basics Codelab",
      "content": "If you’re planning to start learning Jetpack Compose, our modern toolkit for building native Android UI, it’s your lucky day! We just revamped the Basics Jetpack Compose codelab to help you learn the core concepts of Compose, and only with this, you’ll see how much it improves building Android UIs.",
      "url": "https://developer.android.com/codelabs/jetpack-compose-basics",
      "headerImageUrl": "https://i.ytimg.com/vi/k3jvNqj4m08/maxresdefault.jpg",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Codelab",
      "topics": [
        "11"
      ],
      "authors": []
    },
    {
      "id": "143",
      "episodeId": "49",
      "title": "Start an activity for a result from a Composable",
      "content": "We expanded the Compose and other libraries page to cover how to start an activity for result, request runtime permissions, and handle the system back button directly from your composables.",
      "url": "https://developer.android.com/jetpack/compose/libraries",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "11"
      ],
      "authors": []
    },
    {
      "id": "144",
      "episodeId": "49",
      "title": "Material components in Compose",
      "content": "We added a new Material Components and layouts page that goes over the different Material components in Compose such as backdrop, app bars, modal drawers, etc.!",
      "url": "https://developer.android.com/jetpack/compose/layouts/material",
      "headerImageUrl": "https://developer.android.com/images/jetpack/compose/layouts/material/material_components.png",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "11",
        "3"
      ],
      "authors": []
    },
    {
      "id": "145",
      "episodeId": "49",
      "title": "How to implement a custom design system",
      "content": "How to implement a custom design system in Compose",
      "url": "https://developer.android.com/jetpack/compose/themes/custom",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "11",
        "3"
      ],
      "authors": []
    },
    {
      "id": "146",
      "episodeId": "49",
      "title": "The anatomy of a theme",
      "content": "Understanding the anatomy of a Compose theme",
      "url": "https://developer.android.com/jetpack/compose/themes/anatomy",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "11"
      ],
      "authors": []
    },
    {
      "id": "147",
      "episodeId": "49",
      "title": "Paging 📑  Displaying data and its loading state",
      "content": "In the third episode of the Paging video series, TJ adds a local cache to pull from and refresh only when necessary, making use of Room . The local cache acts as the single source of truth for paging data.",
      "url": "https://www.youtube.com/watch?v=OHH_FPbrjtA",
      "headerImageUrl": "https://i.ytimg.com/vi/OHH_FPbrjtA/maxresdefault.jpg",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "9",
        "3"
      ],
      "authors": [
        "38"
      ]
    },
    {
      "id": "148",
      "episodeId": "49",
      "title": "Data safety in the Play Console 🔒",
      "content": "Google Play is rolling out the Data safety form in the Google Play Console. With the new Data safety section, developers will now have a transparent way to show users if and how they collect, share, and protect user data, before users install an app.\nRead the blog post to learn more about how to submit your app information in Play Console, how to get prepared, and what your users will see in your app’s store listing starting February.",
      "url": "https://android-developers.googleblog.com/2021/10/launching-data-safety-in-play-console.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-Zde9ioLE3SY/YWh7qiquXKI/AAAAAAAARCU/m6D-qJJe6QowYPcDWUtb3-YzFGn9xIaUwCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-get-ready-to-sumbit-your-data-safety-secton-social.png",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "12",
        "13"
      ],
      "authors": [
        "10"
      ]
    },
    {
      "id": "149",
      "episodeId": "49",
      "title": "Honor every photo - How cameras capture images",
      "content": "Episode 177: Honor every photon. In this episode, Chet, Roman, and Tor have a chat with Bart Wronski from the Google Research team, discussing the camera pipeline that powers the Pixel phones. How cameras capture images, how the algorithms responsible for Pixel’s beautiful images, HDR+ or Night Sight mode works, and more!",
      "url": "https://adbackstage.libsyn.com/episode-177-honor-every-photon",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "6"
      ],
      "authors": [
        "31"
      ]
    },
    {
      "id": "150",
      "episodeId": "49",
      "title": "Accessibility series 🌐 - Touch targets",
      "content": "The accessibility series continues on with more information on how to follow basic accessibility principles to make sure that your app can be used by as many users as possible.\nIn general, you should ensure that interactive elements have a width and height of at least 48dp! In the touch targets episode, you’ll learn about a few ways in which you can make this happen.",
      "url": "https://www.youtube.com/watch?v=Dqqbe8IFBA4",
      "headerImageUrl": "https://i.ytimg.com/vi/Dqqbe8IFBA4/maxresdefault.jpg",
      "publishDate": "2021-10-16T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "17"
      ],
      "authors": [
        "39"
      ]
    },
    {
      "id": "151",
      "episodeId": "49",
      "title": "Using the CameraX Exposure Compensation API",
      "content": "This blog post by Wenhung Teng talks about how to use the CameraX Exposure Compensation that makes it much simpler to quickly take images with exceptional quality.",
      "url": "https://medium.com/androiddevelopers/using-camerax-exposure-compensation-api-11fd75785bf",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*zinEvf1keSZYuZojr31ehQ.png",
      "publishDate": "2021-10-12T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6"
      ],
      "authors": [
        "44"
      ]
    },
    {
      "id": "152",
      "episodeId": "49",
      "title": "Compose for Wear OS in Developer preview ⌚",
      "content": "We’re bringing the best of Compose to Wear OS as well, with built-in support for Material You to help you create beautiful apps with less code. Read the following article to review the main composables for Wear OS we’ve built and point you towards resources to get started using them.",
      "url": "https://android-developers.googleblog.com/2021/10/compose-for-wear-os-now-in-developer.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-RkL3Yokn3XE/YWWmbuX8E7I/AAAAAAAAQ_o/CEmNJ5_mfq0kScxkFGoMpf1BlU5-uBHjACLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-compose-for-wear-os-now-in-dev-review-header-dark.png",
      "publishDate": "2021-10-11T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "21",
        "11"
      ],
      "authors": [
        "41"
      ]
    },
    {
      "id": "153",
      "episodeId": "49",
      "title": "Paging 📑  How to fetch data and bind the PagingData to the UI",
      "content": "The series on Paging continues on with more content! In the second episode, TJ shows how to fetch data and bind the PagingData to the UI, including headers and footers.",
      "url": "https://www.youtube.com/watch?v=C0H54K63Lww",
      "headerImageUrl": "https://i.ytimg.com/vi/C0H54K63Lww/maxresdefault.jpg",
      "publishDate": "2021-10-10T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": [
        "38"
      ]
    },
    {
      "id": "154",
      "episodeId": "49",
      "title": "Room adds support for Kotlin Symbol Processing",
      "content": "Yigit Boyar wrote the story about how Room added support for Kotlin Symbol Processing (KSP). Spoiler: it wasn’t easy, but it was definitely worth it.",
      "url": "https://medium.com/androiddevelopers/room-kotlin-symbol-processing-24808528a28e",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*yM7Lf4dC_hwse6YmoCO4uQ.png",
      "publishDate": "2021-10-09T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9"
      ],
      "authors": [
        "34"
      ]
    },
    {
      "id": "155",
      "episodeId": "49",
      "title": "Apply special effects to images with the CameraX Extensions API",
      "content": "Have you ever wanted to apply special effects such as HDR or Night mode when taking pictures from your app? CameraX is here to help you! In this article by Charcoal Chen, learn how to do that using the new ExtensionsManager available in the camera-extensions Jetpack library. ",
      "url": "https://medium.com/androiddevelopers/apply-special-effects-to-images-with-the-camerax-extensions-api-d1a169b803d3",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*GZmhCFMCrG4L_mOtwSb0zA.png",
      "publishDate": "2021-10-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6"
      ],
      "authors": [
        "45"
      ]
    },
    {
      "id": "156",
      "episodeId": "49",
      "title": "Wear OS Jetpack libraries now in stable",
      "content": "The Wear OS Jetpack libraries are now in stable.",
      "url": "https://android-developers.googleblog.com/2021/09/wear-os-jetpack-libraries-now-in-stable.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-9zeEGNCG_As/YUD1UO_3kkI/AAAAAAAAQ8k/tCFBpTCwU4MEQHQNB9XzTOXSf6hd9TkQQCLcBGAsYHQ/w1200-h630-p-k-no-nu/image1.png",
      "publishDate": "2021-09-14T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "8",
        "21"
      ],
      "authors": [
        "41"
      ]
    },
    {
      "id": "157",
      "episodeId": "48",
      "title": "Android Dev Summit returns on October 27-28, 2021! 📆",
      "content": "Join us October 27–28 for Android Dev Summit 2021! The show kicks off at 10 AM PST on October 27 with The Android Show: a technical keynote where you’ll hear all the latest developer news and updates. From there, we have over 30 sessions on a range of technical Android development topics, and we’ll be answering your #AskAndroid questions live.",
      "url": "https://developer.android.com/dev-summit",
      "headerImageUrl": "https://developer.android.com/dev-summit/images/android-dev-summit-2021.png",
      "publishDate": "2021-10-05T23:00:00.000Z",
      "type": "Event 📆",
      "topics": [
        "2"
      ],
      "authors": []
    },
    {
      "id": "158",
      "episodeId": "48",
      "title": "Android 12 is live in AOSP! 🤖",
      "content": "We released Android 12 and pushed it to the Android Open Source Project (AOSP). It will be coming to devices later on this year. Thank you for your feedback during the beta.\nAndroid 12 introduces a new design language called Material You along with redesigned widgets, notification UI updates, stretch overscroll, and app launch splash screens. We reduced the CPU time used by core system services, added performance class device capabilities, made ML accelerator drivers updatable outside of platform releases, and prevented apps from launching foreground services from the background and using notification trampolines to improve performance. The new Privacy Dashboard, approximate location, microphone and camera indicators/toggles, and nearby device permissions give users more insight into and control over privacy. We improved the user experience with a unified API for rich content insertion, compatible media transcoding, easier blurs and effects, AVIF image support, enhanced haptics, new camera effects/capabilities, improved native crash debugging, support for rounded screen corners, Play as you download, and Game Mode APIs.",
      "url": "https://android-developers.googleblog.com/2021/10/android-12-is-live-in-aosp.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-7dVmEfR3mJs/YVst2TdY16I/AAAAAAAAK3I/pLnt0r5S-pIaJwcSNsNBqT8w2Y4Ej0yaQCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android%2B12.jpeg",
      "publishDate": "2021-10-03T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "15"
      ],
      "authors": [
        "14"
      ]
    },
    {
      "id": "159",
      "episodeId": "48",
      "title": "Improved Google Play Console user management 🧑‍💼",
      "content": "The user and permission tools in Play Console have a new, decluttered interface and new team management features, making it easier to make sure every team member has the right set of permissions to fulfill their responsibilities without overexposing unrelated business data.\nWe’ve rewritten permission names and descriptions, clarified differentiation between account and app-level permissions, added new search, filtering, and batch-editing capabilities, and added the ability to export this information to a CSV file. In addition, Play Console users can request access to actions with a justification, and we’ve introduced permission groups to make it easier to assign multiple permissions at once to users that share the same or similar roles.",
      "url": "https://android-developers.googleblog.com/2021/09/improved-google-play-console-user.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-vw3eaKdwzVU/YUjvyJ6zy2I/AAAAAAAAQ9s/m39byf56P8Icog5e5TgCbu9et0VCZh1iACLcBGAsYHQ/w1200-h630-p-k-no-nu/PlayConsole-revamped-user-management-01.png",
      "publishDate": "2021-09-20T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "13"
      ],
      "authors": [
        "46"
      ]
    },
    {
      "id": "160",
      "episodeId": "48",
      "title": "Making Permissions auto-reset available to billions more devices 🔐",
      "content": "Android 11 introduced permission auto-reset, automatically resetting an app’s runtime permissions when it isn’t used for a few months. In December 2021, we are starting to roll this feature out to devices with Google Play services running Android 6.0 (API level 23) or higher for apps targeting Android 11 (API level 30) or higher. Users can manually enable permission auto-reset for apps targeting API levels 23 to 29.\nSome apps and permissions are automatically exempted from revocation, like active Device Administrator apps used by enterprises, and permissions fixed by enterprise policy. If your app is expected to work primarily in the background without user interaction, you can ask the user to prevent the system from resetting your app’s permissions.",
      "url": "https://android-developers.googleblog.com/2021/09/making-permissions-auto-reset-available.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-W3UAh-gyf3Y/YUJehjKWQjI/AAAAAAAAQ84/zkURLgqMRa4VZK3Is3ENNYG_OjXJxx2pgCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-making-permissions-auto-reset-social-v2.png",
      "publishDate": "2021-09-16T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "12"
      ],
      "authors": [
        "47"
      ]
    },
    {
      "id": "161",
      "episodeId": "47",
      "title": "Migrating from Dagger to Hilt",
      "content": "While you will eventually want to migrate all your existing Dagger modules over to Hilt’s built in components, you can start by migrating application-wide components to Hilt’s singleton component. This episode explains how.",
      "url": "https://www.youtube.com/watch?v=Xt1_3Nq4lD0&t=15s",
      "headerImageUrl": "https://i.ytimg.com/vi/Xt1_3Nq4lD0/hqdefault.jpg",
      "publishDate": "2021-09-19T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "16"
      ],
      "authors": [
        "48"
      ]
    },
    {
      "id": "162",
      "episodeId": "47",
      "title": "ADB Podcast Episode 175: Creating delightful user experiences with Lottie animations",
      "content": "In this episode, Chet, Romain and Tor have a chat with Gabriel Peal from Tonal, well known for his contributions to the Android community on projects such as Mavericks and Lottie. They talked about Lottie and how it helps designers and developers deliver more delightful user experiences by taking complex animations designed in specialized authoring tools such as After Effects, and rendering them efficiently on mobile devices. They also explored the challenges of designing and implementing a rendering engine such as Lottie.",
      "url": "http://adbackstage.libsyn.com/episode-175-lottie",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-09-13T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "3"
      ],
      "authors": [
        "31"
      ]
    },
    {
      "id": "163",
      "episodeId": "47",
      "title": "Hilt extensions",
      "content": "This episode explains how to write your own Hilt Extensions. Hilt Extensions allow you to extend Hilt support to new libraries. Extensions can be created for common patterns in projects, to support non-standard member injection, mirroring bindings, and more.",
      "url": "https://medium.com/androiddevelopers/hilt-extensions-in-the-mad-skills-series-f2ed6fcba5fe",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*a_ZJwMHs17SmEFr3uEbxDg.png",
      "publishDate": "2021-09-12T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "16"
      ],
      "authors": [
        "49"
      ]
    },
    {
      "id": "164",
      "episodeId": "47",
      "title": "Labeling images for Accessibility",
      "content": "This Accessibilities series episode covers labeling images for accessibility, such as content descriptions for ImageViews and ImageButtons.",
      "url": "https://youtu.be/O2DeSITnzFk",
      "headerImageUrl": "https://i.ytimg.com/vi/O2DeSITnzFk/maxresdefault.jpg",
      "publishDate": "2021-09-09T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "17"
      ],
      "authors": [
        "28"
      ]
    },
    {
      "id": "165",
      "episodeId": "47",
      "title": "ADB Podcast Episode 174: Compose in Android Studio",
      "content": "In this episode, Tor and Nick are joined by Chris Sinco, Diego Perez and Nicolas Roard to discuss the features added to Android Studio for Jetpack Compose. Tune in as they discuss the Compose preview, interactive preview, animation inspector, and additions to the Layout inspector along with their approach to creating tooling to support Compose’s code-centric system.",
      "url": "http://adbackstage.libsyn.com/episode-174-compose-tooling",
      "headerImageUrl": "http://assets.libsyn.com/content/110962067?height=250&width=250&overlay=true",
      "publishDate": "2021-09-08T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "7",
        "11"
      ],
      "authors": [
        "32"
      ]
    },
    {
      "id": "166",
      "episodeId": "47",
      "title": "Hilt under the hood",
      "content": "This episode dives into how the Hilt annotation processors generate code, and how the Hilt Gradle plugin works behind the scenes to improve the overall experience when using Hilt with Gradle.",
      "url": "https://medium.com/androiddevelopers/mad-skills-series-hilt-under-the-hood-9d89ee227059",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*a_ZJwMHs17SmEFr3uEbxDg.png",
      "publishDate": "2021-09-07T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "16"
      ],
      "authors": [
        "50"
      ]
    },
    {
      "id": "167",
      "episodeId": "47",
      "title": "Trackr comes to the Big Screen",
      "content": "A blog post on Trackr, a sample task management app where we showcase Modern Android Development best practices. This post takes you through how applying Material Design and responsive patterns produced a more refined and intuitive user experience on large screen devices.",
      "url": "https://medium.com/androiddevelopers/trackr-comes-to-the-big-screen-9f13c6f927bf",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*678DlYtu4G7wFrq30FQ7Mw.png",
      "publishDate": "2021-09-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "51"
      ]
    },
    {
      "id": "168",
      "episodeId": "47",
      "title": "Accessibility services and the Android Accessibility model",
      "content": "This Accessibilities series episode covers accessibility services like TalkBack, Switch Access and Voice Access and how they help users interact with your apps. Android’s accessibility framework allows you to write one app and the framework takes care of providing the information needed by different accessibility services.",
      "url": "https://youtu.be/LxKat_m7mHk",
      "headerImageUrl": "https://i.ytimg.com/vi/LxKat_m7mHk/maxresdefault.jpg",
      "publishDate": "2021-09-02T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "17"
      ],
      "authors": [
        "39"
      ]
    },
    {
      "id": "169",
      "episodeId": "47",
      "title": "New Accessibility Pathway",
      "content": "Want even more accessibility? You are in luck, check out this entire new learning pathway aimed at teaching you how to make your app more accessible.",
      "url": "https://developer.android.com/courses/pathways/make-your-android-app-accessible",
      "headerImageUrl": "https://developers.google.com/profile/badges/playlists/make-your-android-app-accessible/badge.svg",
      "publishDate": "2021-08-31T23:00:00.000Z",
      "type": "",
      "topics": [
        "17"
      ],
      "authors": []
    },
    {
      "id": "170",
      "episodeId": "47",
      "title": "AndroidX Activity Library 1.4.0-alpha01 released",
      "content": "The AndroidX ComponentActivity now implements the MenuHost interface which allows any component to add menu items to the ActionBar by adding a MenuProvider instance to the activity.",
      "url": "https://developer.android.com/jetpack/androidx/releases/activity#1.4.0-alpha01",
      "headerImageUrl": "",
      "publishDate": "2021-08-31T23:00:00.000Z",
      "type": "API change",
      "topics": [
        "8"
      ],
      "authors": []
    },
    {
      "id": "171",
      "episodeId": "45",
      "title": "DataStore released into stable",
      "content": "Datastore was released, providing a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.",
      "url": "https://developer.android.com/jetpack/androidx/releases/datastore#1.0.0",
      "headerImageUrl": "",
      "publishDate": "2021-08-03T23:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "9"
      ],
      "authors": []
    },
    {
      "id": "172",
      "episodeId": "44",
      "title": "Jetpack Compose 1.0 stable is released",
      "content": "Jetpack Compose, Android’s modern, native UI toolkit is now stable and ready for you to adopt in production. It interoperates with your existing app, integrates with existing Jetpack libraries, implements Material Design with straightforward theming, supports lists with Lazy components using minimal boilerplate, and has a powerful, extensible animation system.",
      "url": "https://android-developers.googleblog.com/2021/07/jetpack-compose-announcement.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-9MiK78CFMLM/YQFurOq9AII/AAAAAAAAQ1A/lKj5GiDnO_MkPLb72XqgnvD5uxOsHO-eACLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-Compose-1.0-header-v2.png",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "11"
      ],
      "authors": [
        "52"
      ]
    },
    {
      "id": "173",
      "episodeId": "44",
      "title": "Android Studio Artic Fox stable is released",
      "content": "Android Studio Arctic Fox is now available in the stable release channel. Arctic Fox brings Jetpack Compose to life with Compose Preview, Deploy Preview, Compose support in the Layout Inspector, and Live Editing of literals. Compose Preview works with the @Preview annotation to let you instantly see the impact of changes across multiple themes, screen sizes, font sizes, and more. Deploy Preview deploys snippets of your Compose code to a device or emulator for quick testing. Layout inspector now works with apps written fully in Compose as well as apps that have Compose alongside Views, allowing you to explore your layouts and troubleshoot. With Live Edit of literals, you can edit literals such as strings, numbers, booleans, etc. and see the immediate results change in previews, the emulator, or on a physical device — all without having to compile.\n",
      "url": "https://android-developers.googleblog.com/2021/07/android-studio-arctic-fox-202031-stable.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-cmcRT5BGOTY/YQBKC6asA0I/AAAAAAAAQzg/hZrde9Sgx881Wdf-c__VMkTvsKoVjOwsACLcBGAsYHQ/w1200-h630-p-k-no-nu/Arctic_Fox_Splash_2x%2B%25281%2529.png",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7",
        "11"
      ],
      "authors": [
        "53"
      ]
    },
    {
      "id": "174",
      "episodeId": "44",
      "title": "User control, privacy, security, and safety",
      "content": "Play announced new updates to bolster user control, privacy, and security. The post covered advertising ID updates, including zeroing out the advertising ID when users opt out of interest-based advertising or ads personalization, the developer preview of the app set ID, enhanced protection for kids, and policy updates around dormant accounts and users of the AccessibilityService API.",
      "url": "https://android-developers.googleblog.com/2021/07/announcing-policy-updates-to-bolster.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-pWCVY7BR-z8/YQAzb9zCZsI/AAAAAAAAQzY/2-OetxLvjOUYhHlTFJNw5JSm_BVjkI0VwCLcBGAsYHQ/s0/Untitled.png",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "13"
      ],
      "authors": [
        "10"
      ]
    },
    {
      "id": "175",
      "episodeId": "44",
      "title": "Identify performance bottlenecks using system trace",
      "content": "System trace profiling within Android Studio with a detailed walkthrough of app startup performance.",
      "url": "https://www.youtube.com/watch?v=aUrqx9AnDUg",
      "headerImageUrl": "https://i.ytimg.com/vi/aUrqx9AnDUg/hqdefault.jpg",
      "publishDate": "2021-07-25T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "5",
        "7"
      ],
      "authors": [
        "37"
      ]
    },
    {
      "id": "176",
      "episodeId": "44",
      "title": "Testing in Compose",
      "content": "ADB released episode #171, part of our continuing series on Jetpack Compose. In this episode, Nick and Romain are joined by Filip Pavlis, Jelle Fresen & Jose Alcérreca to talk about Testing in Compose. They discuss how Compose’s testing APIs were developed hand-in-hand with the UI toolkit, making them more deterministic and opening up new possibilities like manipulating time. They go on to discuss the semantics tree, interop testing, screenshot testing and the possibilities for host-side testing.",
      "url": "https://adbackstage.libsyn.com/episode-171-compose-testing",
      "headerImageUrl": "http://assets.libsyn.com/content/108505820?height=250&width=250&overlay=true",
      "publishDate": "2021-06-29T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "11",
        "4"
      ],
      "authors": [
        "54"
      ]
    },
    {
      "id": "177",
      "episodeId": "42",
      "title": "DataStore reached release candidate status",
      "content": "DataStore has reached release candidate status meaning the 1.0 stable release is right around the corner!",
      "url": "https://developer.android.com/topic/libraries/architecture/datastore",
      "headerImageUrl": "",
      "publishDate": "2021-06-29T23:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "9"
      ],
      "authors": []
    },
    {
      "id": "178",
      "episodeId": "42",
      "title": "Scope Storage Myths",
      "content": "Apps will be required to update their targetSdkVersion to API 30 in the second half of the year. That means your app will be required to work with Scoped Storage. In this blog post, Nicole Borrelli busts some Scope storage myths in a Q&A format.",
      "url": "https://medium.com/androiddevelopers/scope-storage-myths-ca6a97d7ff37",
      "headerImageUrl": "",
      "publishDate": "2021-06-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9",
        "12"
      ],
      "authors": [
        "55"
      ]
    },
    {
      "id": "179",
      "episodeId": "41",
      "title": "Navigation with Multiple back stacks",
      "content": "As part of the rercommended Material pattern for bottom-navigation, the Jetpack Navigation library makes it easy to implement navigation with multiple back-stacks",
      "url": "https://medium.com/androiddevelopers/navigation-multiple-back-stacks-6c67ba41952f",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*v7S7LKg4TlrMRlneeP224Q.jpeg",
      "publishDate": "2021-06-14T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "180",
      "episodeId": "41",
      "title": "Build sophisticated search features with AppSearch",
      "content": "AppSearch is an on-device search library which provides high performance and feature-rich full-text search functionality. Learn how to use the new Jetpack AppSearch library for doing high-performance on-device full text searches.",
      "url": "https://android-developers.googleblog.com/2021/06/sophisticated-search-with-appsearch-in-jetpack.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-PmN4MS50wvo/YMj-HmY4N2I/AAAAAAAAQoQ/5eCx8CU1HgAlFQnQ55IOb_CCVRhe8eGewCLcBGAsYHQ/w1200-h630-p-k-no-nu/AppSearch.jpg",
      "publishDate": "2021-06-13T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "8",
        "3"
      ],
      "authors": [
        "56"
      ]
    },
    {
      "id": "181",
      "episodeId": "41",
      "title": "ADB Podcast Episode 167: Jetpack Compose Layout",
      "content": "In this second episode of our mini-series on Jetpack Compose (AD/BC), Nick and Romain are joined by Anastasia Soboleva, George Mount and Mihai Popa to talk about Compose’s layout system. They explain how the Compose layout model works and its benefits, introduce common layout composables, discuss how writing your own layout is far simpler than Views, and how you can even animate layout.",
      "url": "https://adbackstage.libsyn.com/episode-167-jetpack-compose-layout",
      "headerImageUrl": "http://assets.libsyn.com/content/105399023?height=250&width=250&overlay=true",
      "publishDate": "2021-06-13T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "11"
      ],
      "authors": [
        "57"
      ]
    },
    {
      "id": "182",
      "episodeId": "41",
      "title": "Create an application CoroutineScope using Hilt",
      "content": "Learn how to create an applicatioon-scoped CoroutineScope using Hilt, and how to inject it as a dependency.",
      "url": "https://medium.com/androiddevelopers/create-an-application-coroutinescope-using-hilt-dd444e721528",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*MgDtM-AJmc2m2hg5chkflg.png",
      "publishDate": "2021-06-09T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "16"
      ],
      "authors": [
        "23"
      ]
    },
    {
      "id": "183",
      "episodeId": "41",
      "title": "Android 12 Beta 2 Update",
      "content": "The second Beta of Android 12 has just been released for you to try. Beta 2 adds new privacy features like the Privacy Dashboard and continues our work of refining the release.",
      "url": "https://android-developers.googleblog.com/2021/06/android-12-beta-2-update.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-tLt-TVPqpjA/YKMRwRPMfjI/AAAAAAAAQik/JNtMesFZ2i87RyBACHAVEC14CvcU7G__wCLcBGAsYHQ/w1200-h630-p-k-no-nu/Screen%2BShot%2B2021-05-17%2Bat%2B9.00.30%2BPM.png",
      "publishDate": "2021-06-08T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "15"
      ],
      "authors": [
        "14"
      ]
    },
    {
      "id": "184",
      "episodeId": "41",
      "title": "Top 3 things in Android 12  | Android @ Google I/O '21",
      "content": "Did you miss the latest in Android 12 at Google I/O 2021? Android Software Engineer Chet Haase will recap the top three themes in Android 12 from this year’s Google I/O!",
      "url": "https://www.youtube.com/watch?v=tvf1wmD5H0M",
      "headerImageUrl": "https://i.ytimg.com/vi/tvf1wmD5H0M/maxresdefault.jpg",
      "publishDate": "2021-06-08T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": [
        "31"
      ]
    },
    {
      "id": "185",
      "episodeId": "41",
      "title": "ADB Podcast Episode 166: Security Deposit",
      "content": "In this episode, Chad and Jeff from the Android Security team join Tor and Romain to talk about… security. They explain what the platform does to help preserve user trust and device integrity, why it sometimes means restricting existing APIs, and touch on what apps can do or should worry about.",
      "url": "http://adbackstage.libsyn.com/episode-166-security-deposit",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-06-07T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "12"
      ],
      "authors": [
        "32"
      ]
    },
    {
      "id": "186",
      "episodeId": "41",
      "title": "Multiple Back Stacks",
      "content": "A deep dive into multiple back stacks and some of the work it took to make this feature happen in Fragments and Navigation",
      "url": "https://medium.com/androiddevelopers/multiple-back-stacks-b714d974f134",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*5-lbc-YBJlZnxVFPvNMPAQ.png",
      "publishDate": "2021-06-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "58"
      ]
    },
    {
      "id": "187",
      "episodeId": "41",
      "title": "Building across devices | Android @ Google I/O '21",
      "content": "Did you miss the latest in Building across screens at Google I/O 2021? Product Manager Diana Wong will recap the top three announcements from this year’s Google I/O!",
      "url": "https://www.youtube.com/watch?v=O5oRiIUk_F4",
      "headerImageUrl": "https://i.ytimg.com/vi/O5oRiIUk_F4/maxresdefault.jpg",
      "publishDate": "2021-06-02T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": [
        "59"
      ]
    },
    {
      "id": "188",
      "episodeId": "41",
      "title": "Navigation in Feature Modules",
      "content": "Feature modules delivered with Play Feature delivery at not downloadedd at install time, but only when the app requestss them. Learn how to use the dynamic features navigation library to include the graph from the feature module.",
      "url": "https://medium.com/androiddevelopers/navigation-in-feature-modules-322ac3d79334",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*v7S7LKg4TlrMRlneeP224Q.jpeg",
      "publishDate": "2021-06-01T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "25"
      ]
    },
    {
      "id": "189",
      "episodeId": "41",
      "title": "ADB Podcast Episode 165: Material Witnesses",
      "content": "In this episode, Chet and Romain chattedd with Hunter and Nick from the Material Design team about recent additions and improvements to the Material Design Component libraries: transitions, motion theming, Compose, large screens support and guidance, etc.",
      "url": "http://adbackstage.libsyn.com/episode-165-material-witnesses",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-06-01T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "3"
      ],
      "authors": [
        "31"
      ]
    },
    {
      "id": "190",
      "episodeId": "41",
      "title": "Grow Your Indie Game with Help From Google Play",
      "content": "Google Play is opening submissions for two of our annual developer programs - the Indie Games Accelerator and the Indie Games Festival. These programs are designed to help small games studios grow on Google Play, no matter what stage they are in",
      "url": "https://developers.googleblog.com/2021/06/grow-your-indie-game-with-help-from-google-play.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-MNEblg7_8fA/YK7lludSxJI/AAAAAAAAKQM/_YIT15giTk42oPXWIhK6l2FBVt5PCFKTwCLcBGAsYHQ/w1200-h630-p-k-no-nu/Joint_Announcement_Android%2BDevelopers%2BBlog_Header_1200x600%2B%25282%2529.png",
      "publishDate": "2021-05-31T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "13",
        "20"
      ],
      "authors": [
        "60"
      ]
    },
    {
      "id": "191",
      "episodeId": "41",
      "title": "Untrusted Touch Events in Android",
      "content": "Android 12 prevents touch events from being deliverred if these touches first pass through a window from a different app to ensure users can see what they are interacting with. Learn about alternatives, to see if your app will be affected and how you can test to see if your app will be impacted.",
      "url": "https://medium.com/androiddevelopers/untrusted-touch-events-2c0e0b9c374c",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*lvwe7v_bcNsNXI_7ltFkJA.jpeg",
      "publishDate": "2021-05-25T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "15"
      ],
      "authors": [
        "15"
      ]
    },
    {
      "id": "192",
      "episodeId": "41",
      "title": "Android @ Google I/O: 3 things to know in Modern Android Development",
      "content": "This year’s Google I/O brought lots of updates for Modern Android Development. Learn about the top 3 things you should know.",
      "url": "https://android-developers.googleblog.com/2021/05/mad-spotlight.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-8cqMFObMeko/YK5RbJ7Yr_I/AAAAAAAAQkw/Iw4_hRZwa7QD1CmVGnZUZ4NjYowXZadTgCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android_PostIO_blog-MAD.png",
      "publishDate": "2021-05-24T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "2"
      ],
      "authors": [
        "61"
      ]
    }
  ]
    """.trimIndent()

    @Language("JSON")
    val authors = """
 [
    {
      "id": "17",
      "name": "Adarsh Fernando",
      "mediumPage": "",
      "twitter": "https://twitter.com/AdarshFernando",
      "imageUrl": "https://pbs.twimg.com/profile_images/1291755351298121728/SFGD_KCm_400x400.jpg",
      "bio": ""
    },
    {
      "id": "26",
      "name": "Alex Saveau",
      "mediumPage": "https://medium.com/@SUPERCILEX",
      "twitter": "https://twitter.com/SUPERCILEX",
      "imageUrl": "https://pbs.twimg.com/profile_images/1093296033759604736/kO9WSDy4_400x400.jpg",
      "bio": ""
    },
    {
      "id": "22",
      "name": "Alex Vanyo",
      "mediumPage": "https://medium.com/@alexvanyo",
      "twitter": "https://twitter.com/alex_vanyo",
      "imageUrl": "https://pbs.twimg.com/profile_images/1431339735931305989/nOE2mmi2_400x400.jpg",
      "bio": "Alex joined Android DevRel in 2021, and has worked supporting form factors from small watches to large foldables and tablets. His special interests include insets, Compose, testing and state."
    },
    {
      "id": "53",
      "name": "Amanda Alexander",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "https://developer.android.com/events/dev-summit/images/speakers/amanda_alexander_720.jpg",
      "bio": ""
    },
    {
      "id": "4",
      "name": "Andrew Flynn",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "https://lh3.googleusercontent.com/xfI5PujnEqdQ4GlsRZAvVOGiC_v3VTz6wYM8kxaPyOtXIZY4-BDYOr-d-cjN8kxAkr4yAthuWu2gTZ7t-do=s1016-rw-no",
      "bio": "Andrew joined Google in 2007 after graduating from Dartmouth College. He has worked on a wide range of team from Ads to Android devices to Google Fi, and currently works on the Play Store."
    },
    {
      "id": "54",
      "name": "Android Developers Backstage",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "https://ssl-static.libsyn.com/p/assets/d/b/3/6/db362cf09b34bd4ce5bbc093207a2619/height_250_width_250_Android_Devs_Backstage_Thumb_v2.png",
      "bio": "Android Developers Backstage is a podcast by and for Android developers. Hosted by developers from the Android platform team, this show covers topics of interest to Android programmers, with in-depth discussions and interviews with engineers on the Android team at Google."
    },
    {
      "id": "16",
      "name": "Anna Bernbaum",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": "Anna Bernbaum is a Product Manager for Wear OS, she specifically focusses on Watch Faces, Complications and Tiles."
    },
    {
      "id": "52",
      "name": "Anna-Chiara Bellini",
      "mediumPage": "",
      "twitter": "https://twitter.com/dr0nequeen",
      "imageUrl": "https://pbs.twimg.com/profile_images/852490449961066496/AxcrR7xX_400x400.jpg",
      "bio": "Anna-Chiara joined Google in 2019 after a long career as a Computer Engineer and Startup founder. Now she focuses on making Android developers' lives easier. "
    },
    {
      "id": "24",
      "name": "Arjun Dayal",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "36",
      "name": "Ben Weiss",
      "mediumPage": "https://medium.com/@keyboardsurfer",
      "twitter": "https://twitter.com/keyboardsurfer",
      "imageUrl": "https://pbs.twimg.com/profile_images/1455956781830709255/GqeqbgEY_400x400.jpg",
      "bio": "Ben works as an Engineer on the Android Developer Relations team. Over the years he contributed to many areas of Android. To get the latest info on what's up with Ben, read one of his articles or follow him on Twitter."
    },
    {
      "id": "50",
      "name": "Brad Corso",
      "mediumPage": "https://medium.com/@bcorso1",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "28",
      "name": "Caren Chang",
      "mediumPage": "https://medium.com/@calren24",
      "twitter": "https://twitter.com/calren24",
      "imageUrl": "https://pbs.twimg.com/profile_images/1521260707521519617/cW-G-T2R_400x400.jpg",
      "bio": "Developer Relations Engineer on the Android team, with a focus on Accessibiliity. "
    },
    {
      "id": "37",
      "name": "Carmen Jackson",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "https://media-exp1.licdn.com/dms/image/C5603AQE8wPl4iNUPeA/profile-displayphoto-shrink_200_200/0/1553574292680?e=1655337600&v=beta&t=6zsc9QaC95DM19uXbG3FJQwUIpObp4XeO-WsLzlNGIo",
      "bio": "Carmen has been an engineer on the Android Platform Performance team since 2016 and has worked with Android for over 10 years. She currently leads a team focused on improving application performance."
    },
    {
      "id": "45",
      "name": "Charcoal Chen",
      "mediumPage": "https://medium.com/@charcoalchen",
      "twitter": "",
      "imageUrl": "https://miro.medium.com/fit/c/262/262/0*XgfVFjuchPekBYfh",
      "bio": "Charcoal Chen is a software engineer on Jetpack CameraX team."
    },
    {
      "id": "31",
      "name": "Chet Haase",
      "mediumPage": "https://chethaase.medium.com/",
      "twitter": "https://twitter.com/chethaase",
      "imageUrl": "https://miro.medium.com/max/3150/1*5pR0GFT8Cosn_zGIdRfc0Q.jpeg",
      "bio": "Chet joined the Android team in 2010, where he has worked as an engineer and lead for the UI Toolkit team, as Chief Android Advocate on the Developer Relations team, and now as an engineer on the Graphics team."
    },
    {
      "id": "21",
      "name": "Chris Craik",
      "mediumPage": "https://medium.com/@chriscraik",
      "twitter": "https://twitter.com/chris_craik",
      "imageUrl": "https://pbs.twimg.com/profile_images/865356883971919872/EkFpz3r1_400x400.jpg",
      "bio": ""
    },
    {
      "id": "56",
      "name": "Dan Saadati",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "49",
      "name": "Daniel Santiago",
      "mediumPage": "https://medium.com/@danysantiago",
      "twitter": "",
      "imageUrl": "https://miro.medium.com/fit/c/262/262/2*fe7m2z-tRofWYjaiXihi9g.jpeg",
      "bio": ""
    },
    {
      "id": "14",
      "name": "Dave Burke",
      "mediumPage": "",
      "twitter": "https://twitter.com/davey_burke",
      "imageUrl": "https://pbs.twimg.com/profile_images/1035017742825357312/f0IHVAG1_400x400.jpg",
      "bio": ""
    },
    {
      "id": "59",
      "name": "Diana Wong",
      "mediumPage": "",
      "twitter": "https://twitter.com/droidiana1000",
      "imageUrl": "https://pbs.twimg.com/profile_images/1268358805537951744/zCYvvpvV_400x400.jpg",
      "bio": "Diana is a Product Manager on the Android Developer team, owning the Large Screen app ecosystem and loves Android foldables and tablets. Previously she's worked on Android Jetpack, the NDK, and the Android runtime (ART), among other things."
    },
    {
      "id": "42",
      "name": "Don Turner",
      "mediumPage": "https://medium.com/@donturner",
      "twitter": "https://twitter.com/donturner",
      "imageUrl": "https://pbs.twimg.com/profile_images/1282701855555018753/xcnlScis_400x400.jpg",
      "bio": "Don is an engineer on the Android Developer Relations team. He has founded and led several businesses in the software development, digital marketing and events industries. He joined Google in 2014 and focuses on improving Android app architecture. "
    },
    {
      "id": "6",
      "name": "Florina Muntenescu",
      "mediumPage": "https://medium.com/@florina.muntenescu",
      "twitter": "https://twitter.com/FMuntenescu",
      "imageUrl": "https://pbs.twimg.com/profile_images/726323972686503937/nZkTQVQJ_400x400.jpg",
      "bio": "Florina is working as an Android Developer Relations Engineer at Google, helping developers build beautiful apps with Jetpack Compose. She has been working with Android for more than 10 years, previous work covering news at upday, payment solutions at payleven and navigation services at Garmin."
    },
    {
      "id": "11",
      "name": "Gerry Fan",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "2",
      "name": "Greg Hartrell",
      "mediumPage": "https://medium.com/@greghart/about",
      "twitter": "https://twitter.com/ghartrell",
      "imageUrl": "https://pbs.twimg.com/profile_images/971602488984940547/plY3bBRz_400x400.jpg",
      "bio": "Greg Hartrell is a product leader with a 15+ year history helping large teams build high performing software products and businesses. At Google, he’s a Product Management Director at Google Play / Android, covering product lines from games, to digital content and platform expansion.  He’s previously been VP of Product Development at Capcom/Beeline, and a product leader for 8 years at Microsoft for Xbox Live/360 and the Windows platform. When he’s not speaking, he enjoys looting random objects out of boxes, jumping from platform to platform, and grinding while afk."
    },
    {
      "id": "58",
      "name": "Ian Lake",
      "mediumPage": "https://medium.com/@ianhlake",
      "twitter": "https://twitter.com/ianhlake",
      "imageUrl": "https://pbs.twimg.com/profile_images/438932314504978434/uj3Sgwy9_400x400.jpeg",
      "bio": "Ian is a software engineer on the Android Toolkit team at Google. He leads the teams working on the Navigation Component, Fragments, and the integration of fundamental building blocks such as Lifecycle, Saved State, and Activity APIs across Jetpack."
    },
    {
      "id": "41",
      "name": "Jeremy Walker",
      "mediumPage": "https://medium.com/@codingjeremy",
      "twitter": "https://twitter.com/codingjeremy",
      "imageUrl": "https://pbs.twimg.com/profile_images/1124334569887428610/etnNE5hz_400x400.png",
      "bio": ""
    },
    {
      "id": "5",
      "name": "Jon Boekenoogen",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "51",
      "name": "Jonathan Koren",
      "mediumPage": "https://medium.com/@jdkoren",
      "twitter": "",
      "imageUrl": "",
      "bio": "Jonathan is an Android Developer Relations Engineer at Google, with particular interest in UI and app architecture. In the past, he developed Android applications and libraries at Yahoo and Personal Capital. In the future, he will have fixed the bugs he is writing in the present (hopefully)."
    },
    {
      "id": "40",
      "name": "Kailiang Chen",
      "mediumPage": "https://medium.com/@bbfee",
      "twitter": "https://twitter.com/KailiangChen3",
      "imageUrl": "https://avatars.githubusercontent.com/u/1756481?v=4",
      "bio": "Kailiang is a software engineer on Android Camera Platform team. He works on Jetpack CameraX. In the past, he worked at Youtube and Uber."
    },
    {
      "id": "19",
      "name": "Kateryna Semenova",
      "mediumPage": "https://medium.com/@katerynasemenova",
      "twitter": "https://twitter.com/SKateryna",
      "imageUrl": "https://miro.medium.com/fit/c/176/176/2*MWidJNpRKpwnPhMYw1hBTA.png",
      "bio": "Kate joined Google Android DevRel team in 2020 with the focus on Android performance. She worked on Android12 features, such as Splash screens and App links as well as App startup improvements. Right now she is working on new Deep links tools for develoeprs.\n\nBefore Google, Kate worked as an Android engineer at Lyft. She built LyftPink membership, ride passes, subscriptions and users onboarding flow."
    },
    {
      "id": "10",
      "name": "Krish Vitaldevara",
      "mediumPage": "",
      "twitter": "https://twitter.com/vitaldevara",
      "imageUrl": "https://pbs.twimg.com/profile_images/1326982232100122626/GDN-QoX-_400x400.png",
      "bio": ""
    },
    {
      "id": "7",
      "name": "Lidia Gaymond",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "43",
      "name": "Lilian Young",
      "mediumPage": "",
      "twitter": "https://twitter.com/memnus",
      "imageUrl": "https://developer.android.com/events/dev-summit/images/speakers/lilian_young_720.jpg",
      "bio": "Lilian has been part of the Android Vulnerability Rewards Program nearly from the beginning, since joining Android Security Assurance in 2016. As a member of both the vulnerability assessment team and the design review team for upcoming Android versions, they review bugs and features to determine which is which. "
    },
    {
      "id": "18",
      "name": "Madan Ankapura",
      "mediumPage": "",
      "twitter": "https://twitter.com/madan_ankapura",
      "imageUrl": "https://pbs.twimg.com/profile_images/195140822/IMG_0612_400x400.JPG",
      "bio": ""
    },
    {
      "id": "23",
      "name": "Manuel Vivo",
      "mediumPage": "https://medium.com/@manuelvicnt",
      "twitter": "https://twitter.com/manuelvicnt",
      "imageUrl": "https://pbs.twimg.com/profile_images/1126564755202760705/x3qLaiBB_400x400.jpg",
      "bio": "Manuel is an Android Developer Relations Engineer at Google. With previous experience at Capital One, he currently focuses on App Architecture, Kotlin & Coroutines, Dependency Injection and Jetpack Compose."
    },
    {
      "id": "9",
      "name": "Marcel Pintó",
      "mediumPage": "https://medium.com/@marxallski",
      "twitter": "https://twitter.com/marxallski",
      "imageUrl": "https://pbs.twimg.com/profile_images/1196804242310234112/6pq8qguX_400x400.jpg",
      "bio": ""
    },
    {
      "id": "48",
      "name": "Marcelo Hernandez",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "1",
      "name": "Márton Braun",
      "mediumPage": "https://medium.com/@zsmb13",
      "twitter": "https://twitter.com/zsmb13",
      "imageUrl": "https://pbs.twimg.com/profile_images/1047591879431397377/nNjQUt0F_400x400.jpg",
      "bio": "Márton is an Android Developer Relations Engineer at Google, working on anything and everything Kotlin."
    },
    {
      "id": "29",
      "name": "Mayuri Khinvasara Khabya",
      "mediumPage": "https://medium.com/@mayuri.k18",
      "twitter": "https://twitter.com/mayuri_k",
      "imageUrl": "https://pbs.twimg.com/profile_images/769886964170436608/6tsEB7zi_400x400.jpg",
      "bio": "Mayuri is an Android Developer Relations Engineer and has worked across Android TV, App performance and device backup"
    },
    {
      "id": "15",
      "name": "Meghan Mehta",
      "mediumPage": "https://medium.com/@magicalmeghan",
      "twitter": "https://twitter.com/adressyengineer",
      "imageUrl": "https://pbs.twimg.com/profile_images/1121847353214824449/wIB-dD0__400x400.jpg",
      "bio": "Meghan has been in Google DevRel since 2018 and loves every minute of it. She mostly works on training and is passionate about making learning Android accessible to all. Outside of work you can find her singing, dancing, or petting dogs."
    },
    {
      "id": "46",
      "name": "Mike Yerou",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "25",
      "name": "Murat Yener",
      "mediumPage": "https://medium.com/@yenerm",
      "twitter": "https://twitter.com/yenerm",
      "imageUrl": "https://pbs.twimg.com/profile_images/1201558524385316866/7lBmCYmD_400x400.jpg",
      "bio": ""
    },
    {
      "id": "57",
      "name": "Nick Butcher",
      "mediumPage": "https://medium.com/@crafty",
      "twitter": "https://twitter.com/crafty",
      "imageUrl": "https://pbs.twimg.com/profile_images/964457443517444097/AiMNiPtx_400x400.jpg",
      "bio": ""
    },
    {
      "id": "55",
      "name": "Nicole Borrelli",
      "mediumPage": "https://medium.com/@borrelli",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "33",
      "name": "Nicole Laure",
      "mediumPage": "",
      "twitter": "https://twitter.com/nicolelaure",
      "imageUrl": "https://pbs.twimg.com/profile_images/442979550285557760/0nq7QOTn_400x400.jpeg",
      "bio": ""
    },
    {
      "id": "60",
      "name": "Patricia Correa",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "https://pbs.twimg.com/profile_images/940666196050944000/w12h2qOz_400x400.jpg",
      "bio": ""
    },
    {
      "id": "27",
      "name": "Paul Lammertsma",
      "mediumPage": "https://medium.com/@officesunshine",
      "twitter": "https://twitter.com/officesunshine",
      "imageUrl": "https://pbs.twimg.com/profile_images/1281278859506331651/2XBTbfIK_400x400.jpg",
      "bio": "Paul is an Android Developer Relations Engineer at Google, supporting developers for TVs, watches and large screens. He has passionate about building amazing Android experiences since Froyo."
    },
    {
      "id": "47",
      "name": "Peter Visontay",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "12",
      "name": "Pietro Maggi",
      "mediumPage": "https://medium.com/@pmaggi",
      "twitter": "https://twitter.com/pfmaggi",
      "imageUrl": "https://pbs.twimg.com/profile_images/1149328128868794370/T59BFarK_400x400.png",
      "bio": "Pietro joined Android DevRel in 2018, and has worked supporting Jetpack WorkManager, large screen devices and lately, Android Enterprise."
    },
    {
      "id": "20",
      "name": "Rahul Ravikumar",
      "mediumPage": "https://medium.com/@rahulrav",
      "twitter": "https://twitter.com/tikurahul",
      "imageUrl": "https://pbs.twimg.com/profile_images/839866273160822785/sLb2Ld53_400x400.jpg",
      "bio": "Software Engineer on the Toolkit team. Recent projects include Jetpack Macrobenchmarks, Baseline Profiles and WorkManager."
    },
    {
      "id": "13",
      "name": "Rohan Shah",
      "mediumPage": "",
      "twitter": "twitter.com/rohanscloud",
      "imageUrl": "https://pbs.twimg.com/profile_images/828723313396502530/dfVBFeZP_400x400.jpg",
      "bio": "Product Manager on the System UI team, experienced Android developer in a past life. Focused on customization, gesture navigation, and smooth motion."
    },
    {
      "id": "30",
      "name": "Romain Guy",
      "mediumPage": "https://medium.com/@romainguy",
      "twitter": "https://twitter.com/romainguy",
      "imageUrl": "https://pbs.twimg.com/profile_images/459175652105527298/6qGNL0QI_400x400.jpeg",
      "bio": "Romain joined the Android team in 2007, working on the UI Toolkit and the Graphics pipeline for many years. He now leads the Toolkit and Jetpack teams."
    },
    {
      "id": "35",
      "name": "Sean McQuillan",
      "mediumPage": "https://medium.com/@objcode",
      "twitter": "https://twitter.com/objcode",
      "imageUrl": "https://pbs.twimg.com/profile_images/913524063175286784/nhyO1wkU_400x400.jpg",
      "bio": "Software Engineer on the Toolkit team. Working on making text 📜 sparkle ✨. Also helping melting face 🫠 render on your phone."
    },
    {
      "id": "39",
      "name": "Shailen Tuli",
      "mediumPage": "",
      "twitter": "https://twitter.com/shailentuli",
      "imageUrl": "https://pbs.twimg.com/profile_images/1521593961/shailen_400x400.jpg",
      "bio": ""
    },
    {
      "id": "3",
      "name": "Simona Stojanovic",
      "mediumPage": "https://medium.com/@anomisSi",
      "twitter": "https://twitter.com/anomisSi",
      "imageUrl": "https://pbs.twimg.com/profile_images/1437506849016778756/pG0NZALw_400x400.jpg",
      "bio": "Android Developer Relations Engineer @Google, working on the Compose team and taking care of Layouts & Navigation."
    },
    {
      "id": "61",
      "name": "The Modern Android Development Team",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": "Development tools, APIs, language, and distribution technologies recommended by the Android team to help developers be productive and create better apps that run across billions of devices."
    },
    {
      "id": "38",
      "name": "TJ Dahunsi",
      "mediumPage": "https://tunjid.medium.com/",
      "twitter": "",
      "imageUrl": "https://pbs.twimg.com/profile_images/1504815529848152074/iA9Q_QME_400x400.jpg",
      "bio": "Tj is an engineer on the Android Developer Relations Team working on app architecture. He's also a connoiseur of fine memes."
    },
    {
      "id": "32",
      "name": "Tor Norbye",
      "mediumPage": "",
      "twitter": "https://twitter.com/tornorbye",
      "imageUrl": "https://pbs.twimg.com/profile_images/1411058065839845376/SeUHA-sR_400x400.jpg",
      "bio": "Tor joined the Android team in 2010, and leads engineering for Android Studio."
    },
    {
      "id": "8",
      "name": "Vicki Amin",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "",
      "bio": ""
    },
    {
      "id": "44",
      "name": "Wenhung Teng",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": "https://lh3.googleusercontent.com/a-/AOh14Ghsnwfup3BUugAxNwQsAD8Ph_CWNrH8SL6Wb8OZ",
      "bio": ""
    },
    {
      "id": "34",
      "name": "Yigit Boyar",
      "mediumPage": "https://medium.com/@yigit",
      "twitter": "https://twitter.com/yigitboyar",
      "imageUrl": "https://pbs.twimg.com/profile_images/530840695347482624/me4HgEMU_400x400.jpeg",
      "bio": "Yigit joined the Android team in 2014 and worked on various projects from RecyclerView to Architecture Components with the dream of making Android development better. Still trying :)"
    }
  ]
    """.trimIndent()
}

/* ktlint-enable max-line-length */
