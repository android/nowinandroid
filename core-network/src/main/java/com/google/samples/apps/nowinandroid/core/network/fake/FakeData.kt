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

import com.google.samples.apps.nowinandroid.core.model.data.NewsResourceType.Article
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.intellij.lang.annotations.Language

object FakeDataSource {
    val sampleTopic = NetworkTopic(
        id = 1,
        name = "UI",
        shortDescription = "Material Design, Navigation, Text, Paging, Compose, Accessibility (a11y), Internationalization (i18n), Localization (l10n), Animations, Large Screens, Widgets",
        longDescription = "Learn how to optimize your app's user interface - everything that users can see and interact with. Stay up to date on tocpis such as Material Design, Navigation, Text, Paging, Compose, Accessibility (a11y), Internationalization (i18n), Localization (l10n), Animations, Large Screens, Widgets, and many more!",
        url = "url",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_UI.svg?alt=media&token=5d1d25a8-db1b-4cf1-9706-82ba0d133bf9"
    )
    val sampleResource = NetworkNewsResource(
        id = 1,
        episodeId = 57,
        title = "Discontinuing Kotlin synthetics for views",
        content = "Synthetic properties to access views were created as a way to eliminate the common boilerplate of findViewById calls. These synthetics are provided by JetBrains in the Kotlin Android Extensions Gradle plugin (not to be confused with Android KTX).",
        url = "https://android-developers.googleblog.com/2022/02/discontinuing-kotlin-synthetics-for-views.html",
        headerImageUrl = "",
        authors = listOf(1),
        publishDate = LocalDateTime(
            year = 2022,
            monthNumber = 2,
            dayOfMonth = 18,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        ).toInstant(TimeZone.UTC),
        type = Article,
        topics = listOf(1, 8),
    )

    @Language("JSON")
    val topicsData = """
[
    {
      "id": "1",
      "name": "UI",
      "shortDescription": "Material Design, Navigation, Text, Paging, Compose, Accessibility (a11y), Internationalization (i18n), Localization (l10n), Animations, Large Screens, Widgets",
      "longDescription": "Learn how to optimize your app's user interface - everything that users can see and interact with. Stay up to date on tocpis such as Material Design, Navigation, Text, Paging, Compose, Accessibility (a11y), Internationalization (i18n), Localization (l10n), Animations, Large Screens, Widgets, and many more!",
      "url": "url",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_UI.svg?alt=media&token=5d1d25a8-db1b-4cf1-9706-82ba0d133bf9"
    },
    {
      "id": "0",
      "name": "Headlines",
      "shortDescription": "News we want everyone to see",
      "longDescription": "Stay up to date with the latest events and announcements from Android!",
      "url": "",
      "imageUrl": ""
    },
    {
      "id": "2",
      "name": "Testing",
      "shortDescription": "CI, Espresso, TestLab, etc",
      "longDescription": "Testing is an integral part of the app development process. By running tests against your app consistently, you can verify your app's correctness, functional behavior, and usability before you release it publicly. Stay up to date on the latest tricks in CI, Espresso, and Firebase TestLab.",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Testing.svg?alt=media&token=0d11b0b9-3eee-438e-8f64-b420ba6d445c"
    },
    {
      "id": "3",
      "name": "Performance",
      "shortDescription": "Optimization, profiling",
      "longDescription": "Topics here will try to optimize your app perfoamnce by profiling and identifying areas in which your app makes inefficient use of resources such as the CPU, memory, graphics, network, or the device battery.",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Performance.svg?alt=media&token=2becab75-8ba0-4af8-8f46-1aee1b299463"
    },
    {
      "id": "4",
      "name": "Camera & Media",
      "shortDescription": "",
      "longDescription": "Learn about Android's robust APIs for playing and recording media, help add video, audio, and photo capabilities to your app!",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Camera%20%26%20Media.svg?alt=media&token=1c4efeec-88fa-4777-b50b-fb79e5cdfef9"
    },
    {
      "id": "5",
      "name": "Android Studio",
      "shortDescription": "",
      "longDescription": "Android Studio is the official integrated development environment (IDE) for Android development. It provides the fastest tools for building apps on every type of Android device.",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Android%20Studio.svg?alt=media&token=b946fbef-5a27-49e6-8f58-12d89d6b6512"
    },
    {
      "id": "6",
      "name": "New APIs & Libraries",
      "shortDescription": "New Jetpack libraries",
      "longDescription": "Stay up to date with the latest new APIs & libraires",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_New%20APIs%20%26%20Libraries.svg?alt=media&token=317397c4-a173-435b-9a07-2ca35b7beaf6"
    },
    {
      "id": "7",
      "name": "Data Storage",
      "shortDescription": "Room, Data Store",
      "longDescription": "Android uses a file system that's similar to disk-based file systems on other platforms. The system provides several options for you to save your app data: App-specific storage, shared storage, preferences, and databases - learn about Room and Data Store!",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Data%20Storage.svg?alt=media&token=1dcddccc-b088-45a4-a23d-d874bd047eab"
    },
    {
      "id": "8",
      "name": "Kotlin",
      "shortDescription": "",
      "longDescription": "Kotlin is a modern statically typed programming language used by over 60% of professional Android developers that helps boost productivity, developer satisfaction, and code safety.",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Kotlin.svg?alt=media&token=e0bc5290-3670-4abb-b6a3-abf47327c332"
    },
    {
      "id": "9",
      "name": "Compose",
      "shortDescription": "",
      "longDescription": "Jetpack Compose is Android’s modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Compose.svg?alt=media&token=c7cee979-5062-49a9-a653-6fb10530d59d"
    },
    {
      "id": "10",
      "name": "Privacy & Security",
      "shortDescription": "Privacy, Security",
      "longDescription": "Learn about best practices and resources to help developers design and implement safe, secure, and private apps.",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Privacy%20%26%20Security.svg?alt=media&token=48cb3487-32f9-40fc-bf62-c488973150fc"
    },
    {
      "id": "11",
      "name": "Publishing & Distribution",
      "shortDescription": "Google Play",
      "longDescription": "Learn about Google Play publish and distrubution system to make your Android applications available to users.",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Publishing%20%26%20Distribution.svg?alt=media&token=e65d36cb-4050-4f56-be9f-34c599d38805"
    },
    {
      "id": "12",
      "name": "Tools",
      "shortDescription": "Gradle, Memory Safety, Debugging",
      "longDescription": "Android Studio, Compose tooling, APK Analyzer, Fast emulator, Intelligent code editor, Flexible build system, Realtime profilers, Gradle, Memory Safety, Debugging",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Tools.svg?alt=media&token=4df6167c-06ef-4fdd-9f7b-94a5d7f3376b"
    },
    {
      "id": "13",
      "name": "Platform & Releases",
      "shortDescription": "Android 12, Android 13, etc",
      "longDescription": "Stay up to date with the latest Android releases and features!",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Platform%20%26%20Releases.svg?alt=media&token=57779dd8-3b19-4e58-9959-25ff4aeef5a2"
    },
    {
      "id": "14",
      "name": "Architecture",
      "shortDescription": "Lifecycle, Dependency Injection, WorkManager",
      "longDescription": "Lifecycle, Dependency Injection, WorkManager",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Architecture.svg?alt=media&token=8f946cb6-2efa-462f-94b9-fb5112bcee48"
    },
    {
      "id": "15",
      "name": "Accessibility",
      "shortDescription": "",
      "longDescription": "Accessibility is an important part of any app. Whether you're developing a new app or improving an existing one, consider the accessibility of your app's components.\n\nBy integrating accessibility features and services, you can improve your app's usability, particularly for users with disabilities.",
      "url": "",
      "imageUrl": "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Accessibility.svg?alt=media&token=6333941e-eeaf-4ab5-bec7-19920cc81d97"
    },
    {
      "id": "16",
      "name": "Android Auto",
      "shortDescription": "",
      "longDescription": "Lean about how to build apps that help users connect on the road through Android Automotive OS and Android Auto",
      "url": "",
      "imageUrl": ""
    },
    {
      "id": "17",
      "name": "Android TV",
      "shortDescription": "",
      "longDescription": "Learn about how to build a great user experience for your TV app: create immersive content on the big screen and for a remote control",
      "url": "",
      "imageUrl": ""
    },
    {
      "id": "18",
      "name": "Games",
      "shortDescription": "",
      "longDescription": "Learn about new tools and best practices to support your game app development and game performance.",
      "url": "",
      "imageUrl": ""
    },
    {
      "id": "19",
      "name": "Wear OS",
      "shortDescription": "",
      "longDescription": "Learn about new tools and best practices to support your Wear OS development and watch performance.",
      "url": "",
      "imageUrl": ""
    }
]
""".trimIndent()

    @Language("JSON")
    val data = """
[
    {
      "id": "1",
      "episodeId": "57",
      "title": "Discontinuing Kotlin synthetics for views",
      "content": "Synthetic properties to access views were created as a way to eliminate the common boilerplate of findViewById calls. These synthetics are provided by JetBrains in the Kotlin Android Extensions Gradle plugin (not to be confused with Android KTX).",
      "url": "https://android-developers.googleblog.com/2022/02/discontinuing-kotlin-synthetics-for-views.html",
      "headerImageUrl": "",
      "publishDate": "2022-02-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1",
        "8"
      ],
      "authors": [
        1
      ]
    },
    {
      "id": "2",
      "episodeId": "57",
      "title": "Things to know from the 2022 Google for Games Developer Summit",
      "content": "This week marked the 2022 Google for Games Developer Summit, Google’s biggest event of the year centered around game development. The Android team shared information around the next generation of services, tools and features to help you develop and deliver high quality games. ",
      "url": "https://android-developers.googleblog.com/2022/03/GGDS-recap-blog.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhW4RL-UKUurgM2bVJRepqjKehVETjf9bqdXllyspPaWTTt8s86MGvfxlxLkDyJAnnkGr7vDpDTPx6bQbgkThYXMSaW1GQvXw9V57xybA8Y89vIE45JDElGxSNFHwOAndATPYrGmc200fkyBTRSNi7w53hTbS1ao-TSoEBFs8jvTgz6ud5Tcb1qitkt",
      "publishDate": "2022-03-15T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "18"
      ],
      "authors": [
        2
      ]
    },
    {
      "id": "3",
      "episodeId": "57",
      "title": "MAD Skills: DataStore and Introduction to Architecture💡",
      "content": "Now that our MAD Skills series on Jetpack DataStore is complete, let’s do a quick wrap up of all the things we’ve covered in each episode.",
      "url": "https://android-developers.googleblog.com/2022/03/jetpack-datastore-wrap-up.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgo2-I1LhMjWd1zzpIQXzjMCPoZeUZc35n43UosKDuLMyP7rIDe8cGfs23tmkSAed6Wxw9EoNTIpvvWCljermK_lCu0etlrCnONx3WeXMCGe-s8I45hYhuVo6w_Q2UTNATMTA70t2o9MS5p2pBdPFz5Ye4b2ajOJjNlW9rELtqWcEW4O1Rkzy4lfqRO",
      "publishDate": "2022-03-14T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        3
      ]
    },
    {
      "id": "4",
      "episodeId": "57",
      "title": "Play Time with Jetpack Compose",
      "content": "Learn about Google Play Store’s strategy for adopting Jetpack Compose, how they overcame specific performance challenges, and improved developer productivity and happiness.",
      "url": "https://android-developers.googleblog.com/2022/03/play-time-with-jetpack-compose.html",
      "headerImageUrl": "",
      "publishDate": "2022-03-10T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9",
        "11"
      ],
      "authors": [
        4
      ]
    },
    {
      "id": "5",
      "episodeId": "57",
      "title": "App Excellence Summit 2022 ⭐",
      "content": "Did you know that 54% of users who left a 1-star review in the Play Store mentioned app stability and bugs? *\n\nTo help product managers and business decision makers understand how high quality app experiences drive business growth and what tools they can use to make sound business and technical decisions, we are hosting our first Android App Excellence Summit in just a few weeks!",
      "url": "https://android-developers.googleblog.com/2022/03/app-excellence-summit-2022.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEh4Vck7mqle-tLweEgrIc1WT0ycY6O6zBxv9mC1Dt1xCnJN5COTGFxDSQlIM1rbbMKIMZHPtjzXgENMGk80oxb5Mn8kTn6qO7kgUXC_N5YSB0dWxcXvQOIPHEEgNJze9g8eZrY1xgA9_oBls71NLItDJKTYeoJGEXxIBiAE_c6SkXv2jSELZEoFfqVq",
      "publishDate": "2022-03-10T00:00:00.000Z",
      "type": "Event 📆",
      "topics": [
        "0"
      ],
      "authors": []
    },
    {
      "id": "6",
      "episodeId": "57",
      "title": "#TheAndroidShow: Tablets, Jetpack Compose, and Android 13 📹",
      "content": "Last week, Florina and Huyen hosted #TheAndroidShow, where we went Behind the scenes with animations & Jetpack Compose, asked whether now is the moment to think tablet first, and covered Android 13 along with other key themes for Android this year.",
      "url": "https://www.youtube.com/watch?v=WL9h46CymlU",
      "headerImageUrl": "https://i.ytimg.com/vi/WL9h46CymlU/maxresdefault.jpg",
      "publishDate": "2022-03-09T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "9",
        "1",
        "13",
        "0"
      ],
      "authors": [
        6
      ]
    },
    {
      "id": "7",
      "episodeId": "57",
      "title": "Freeing up 60% of storage for apps 💾",
      "content": "App archiving will allow users to reclaim ~60% of app storage temporarily by removing parts of the app rather than uninstalling the app completely.",
      "url": "https://android-developers.googleblog.com/2022/03/freeing-up-60-of-storage-for-apps.html",
      "headerImageUrl": "",
      "publishDate": "2022-03-08T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        7
      ]
    },
    {
      "id": "8",
      "episodeId": "57",
      "title": "Demystifying Jetpack Glance for app widgets",
      "content": "We recently announced the first Alpha version of Glance, initially with support for AppWidgets and now for Tiles for Wear OS. This new framework is built on top of the Jetpack Compose runtime and designed to make it faster and easier to build “glanceables” such as app widgets without having to handle a lot of boilerplate code or lifecycle events to connect different components.",
      "url": "https://medium.com/androiddevelopers/demystifying-jetpack-glance-for-app-widgets-8fbc7041955c",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*mlswR3fyxaIG-C1OUifYVw.jpeg",
      "publishDate": "2022-03-07T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1",
        "19"
      ],
      "authors": [
        9
      ]
    },
    {
      "id": "9",
      "episodeId": "57",
      "title": "Keeping Google Play safe with our key 2022 initiatives 🔒",
      "content": "We shared information about what’s ahead in 2022 for Google Play’s privacy and safety initiatives to give you time to prepare.",
      "url": "https://android-developers.googleblog.com/2022/03/privacy-and-security-direction.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhh3FMLL-etD7iDzhSI6CoYbuwgB9ZADjXa6A9C4aM3W-eRqj1FGfP8dyMY4i5RlMtQJD8Sx1y1NHFuaCae10iZkAs_cETaCAllzCDU075awpkAc1pkhld7uxwjTmwNdihGhB-FtySiSsf9aknd1ZULz0zkRtybX4gRUp8JCbPh2n3pPEhjK0mTjNWS",
      "publishDate": "2022-03-03T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "10"
      ],
      "authors": [
        10
      ]
    },
    {
      "id": "10",
      "episodeId": "57",
      "title": "Games-Activity Version 1.1.0",
      "content": "adds WindowInsets listening/querying for notch and IME response along with key and motion event filters.",
      "url": "https://developer.android.com/jetpack/androidx/releases/games#1.1.0",
      "headerImageUrl": "",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "18"
      ],
      "authors": []
    },
    {
      "id": "11",
      "episodeId": "57",
      "title": "Room Version 2.5.0-alpha01",
      "content": "Converted room-common, room-migration, and paging related files in room-runtime from Java to Kotlin along with a new API for multi-process lock to protect multi-process 1st time database creation and migrations",
      "url": "https://developer.android.com/jetpack/androidx/releases/room#2.5.0-alpha01",
      "headerImageUrl": "",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "7"
      ],
      "authors": []
    },
    {
      "id": "12",
      "episodeId": "57",
      "title": "Media Version 1.6.0-alpha 01",
      "content": "Adds the extras necessary to setup a signin/settings page using CarAppLibrary.",
      "url": "https://developer.android.com/jetpack/androidx/releases/media#media-1.6.0-alpha01",
      "headerImageUrl": "",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "4"
      ],
      "authors": []
    },
    {
      "id": "13",
      "episodeId": "57",
      "title": "AppCompat-Resources Version 1.6.0-alpha01",
      "content": "Adds support for customizing locales, providing backwards compatibility for the Android 13 per-language preferences API",
      "url": "https://developer.android.com/jetpack/androidx/releases/appcompat#1.6.0-alpha01",
      "headerImageUrl": "",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "14",
      "episodeId": "57",
      "title": "Recording Video with CameraX VideoCapture API",
      "content": "A picture is worth a thousand words, and CameraX ImageCapture has already made it much easier to tell your story through still images on Android. Now with the new VideoCapture API, CameraX can help you create thousands of continuous pictures to tell an even better and more engaging story!",
      "url": "https://medium.com/androiddevelopers/recording-video-with-camerax-videocapture-api-a36cfd8a48c8",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*GZmhCFMCrG4L_mOtwSb0zA.png",
      "publishDate": "2022-02-23T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "4"
      ],
      "authors": [
        11
      ]
    },
    {
      "id": "15",
      "episodeId": "57",
      "title": "Unbundling the stable WindowManager",
      "content": "The 1.0.0 stable release of Jetpack WindowManager, the foundation for great experiences on all types of large screen devices.",
      "url": "https://medium.com/androiddevelopers/unbundling-the-stable-windowmanager-a5471ff2907",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*dIXjHF8_-47CvYTb.png",
      "publishDate": "2022-02-17T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1"
      ],
      "authors": [
        12
      ]
    },
    {
      "id": "16",
      "episodeId": "56",
      "title": "Jetpack Compose 1.1 is now stable!",
      "content": "Last week we released version 1.1 of Jetpack Compose and Florina Muntenescu wrote an article giving us all the information! This release contains new features like improved focus handling, touch target sizing, ImageVector caching and support for Android 12 stretch overscroll. This also means that previously experimental APIs are now stable. Check out our recently updated samples, codelabs, and the Accompanist library!",
      "url": "https://android-developers.googleblog.com/2022/02/jetpack-compose-11-now-stable.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEiEIiQOoFF-f-sDcbYOMINZw5-2R9aQjrREfiXFMGsRYODVfaz1sgdCS2C3UjgeJjCII5oyE4y97kbvQIUsl9wIx8RqTSZPSdIoCywW89lvmAJ5a15bkFOwoR9UacCEUb4CjOMy0omVMfC0CQhUfz9VMTZR4iyjDGagEZfNuMid8BT0lvarns9Tp6PC",
      "publishDate": "2022-02-09T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "1"
      ],
      "authors": [
        6
      ]
    },
    {
      "id": "17",
      "episodeId": "56",
      "title": "MAD Skills: DataStore",
      "content": "The DataStore MAD Skills series rolls on! In the sixth episode, Simona Stojanovic covered DataStore: Best Practices part 2 covering DataStore-to-DataStore migration. This is used when you make significant changes to your dataset like renaming your data model values or changing their type. ",
      "url": "https://medium.com/androiddevelopers/datastore-and-data-migration-fdca806eb1aa",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*8wsdb7Z7QxT1d4lM",
      "publishDate": "2022-02-15T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        3
      ]
    },
    {
      "id": "18",
      "episodeId": "56",
      "title": "DataStore and Testing",
      "content": "For the final part of the DataStore series, Simona covered DataStore and testing and teaches you how to fully test your DataStore.",
      "url": "https://medium.com/androiddevelopers/datastore-and-testing-edf7ae8df3d8",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*5_yt1M6_QEMN0OgGU8VaZw.png",
      "publishDate": "2022-02-16T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        3
      ]
    },
    {
      "id": "19",
      "episodeId": "56",
      "title": "Material You: Coming to more Android Devices near you",
      "content": "Material You will soon be available on more Android 12 phones globally including devices by Samsung, Oppo, OnePlus and more! Material You has made the Android experience more fluid and personal than ever. Our OEM partners continue to work with us to ensure that key design APIs work consistently across the Android ecosystem so developers can benefit from a cohesive experience.",
      "url": "https://android-developers.googleblog.com/2022/02/material-you-coming-to-more-android.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhDOIPFoqZ8uvg7VmH5EuY3ocfxvKZXawUQ9NczUCEtOdpw3v42vSTrpUSvHjbph5KmTlDH-XtnmGeXmCFTMaHDnRS9ibzLUHBip_XnVHUL7xv-3UrVL6plimErj_oK_KyW5ULpmj6orVTaTq9r56K0V3npQFdIrBPE7_caRWb_QA5E9FljpREWVB7Y",
      "publishDate": "2022-02-10T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1",
        "13"
      ],
      "authors": []
    },
    {
      "id": "20",
      "episodeId": "56",
      "title": "The first developer preview of Android 13",
      "content": "We’re sharing a first look at the next release of Android, with the Android 13 Developer Preview 1. With Android 13 we’re continuing some important themes: privacy and security, as well as developer productivity. We’ll also build on some of the newer updates we made in 12L to help you take advantage of the 250+ million large screen Android devices currently running.",
      "url": "https://android-developers.googleblog.com/2022/02/first-preview-android-13.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEjnrShXcFkBmErmhgdmx82vJbaKBIxU6p2Yz2Vr1V7AlFkD2tGwRmx_a7tWcInPmiUh8VpPmEEqXut-EjP23lFYG9wiMO4sKBDEwbZ3MNppZOy_HW54OXO4SkdQVH08cWdi7QnTMMwGELFoPq_r7_cyaGU8fx2InJG2R-NfkqF1IRt7rKOfA8M1GhUy",
      "publishDate": "2022-02-10T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "13"
      ],
      "authors": [
        14
      ]
    },
    {
      "id": "21",
      "episodeId": "56",
      "title": "AndroidX releases 🚀",
      "content": "Since Compose just went stable, the Animation, Compiler, Foundation, Material, Runtime and UI Versions also went stable! Games-Text-Input and ProfileInstaller also went stable! \n\nThere are a bunch of new APIs in alpha including new Testing APIs (Test Runner, Test Monitor, Test Services and Test Orchestrator), Metrics Version and Startup Version.",
      "url": "https://developer.android.com/jetpack/androidx/versions/all-channel#february_9_2022",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*bux1xKYcB3A9pBFx",
      "publishDate": "2022-02-09T00:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "9",
        "6"
      ],
      "authors": [
        15
      ]
    },
    {
      "id": "22",
      "episodeId": "55",
      "title": "DataStore best practices part 1",
      "content": "learn about performing synchronous work and how to make it work with Kotlin data class serialization and Hilt.",
      "url": "https://www.youtube.com/watch?v=S10ci36lBJ4",
      "headerImageUrl": "",
      "publishDate": "2022-02-07T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "7"
      ],
      "authors": [
        3
      ]
    },
    {
      "id": "23",
      "episodeId": "55",
      "title": "All about Proto DataStore",
      "content": "In this post, we will learn about Proto DataStore, one of two DataStore implementations. We will discuss how to create it, read and write data and how to handle exceptions, to better understand the scenarios that make Proto a great choice.",
      "url": "https://medium.com/androiddevelopers/all-about-proto-datastore-1b1af6cd2879",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*UtNu7pmbt3WEA213SW9p9Q.png",
      "publishDate": "2022-01-31T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        3
      ]
    },
    {
      "id": "24",
      "episodeId": "55",
      "title": "Glance: Tiles for Wear OS made simple ⌚️",
      "content": "Last year we announced the Wear Tiles API. To complement that Java API, we are excited to announce that support for Wear OS Tiles has been added to Glance, a new framework built on top of Jetpack Compose designed to make it easier to build for surfaces outside your Android app. As this library is in alpha, we’d love to get your feedback.",
      "url": "https://android-developers.googleblog.com/2022/01/announcing-glance-tiles-for-wear-os.html",
      "headerImageUrl": "",
      "publishDate": "2022-01-26T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "19"
      ],
      "authors": [
        16
      ]
    },
    {
      "id": "25",
      "episodeId": "55",
      "title": "Android Studio Bumblebee 🐝 stable",
      "content": "Android Studio Bumblebee (2021.1.1) is now stable. We’ve since patched it to address some launch issues — so make sure to upgrade! It improves functionality across the typical developer workflow: Build and Deploy, Profiling and Inspection, and Design.",
      "url": "https://android-developers.googleblog.com/2022/01/android-studio-bumblebee-202111-stable.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEhQ7R2ySipHb8y5jNJeiIj3pE8dZfWAV7EF0wQZ4rQ65lB4MsZroAT4R_7rSfznMZ30xBMLx9_dwnt05V6I0Du0EfI7mvLicK6LwdkuZsF_Gc3sPqrZGxkojTJpHCXFI3Kvr3bLyoSjElldtt1NUpGSBzHgG3O1pvS9BR02L9R2_FYTUgPLfUoNLWYQ",
      "publishDate": "2022-01-25T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "5"
      ],
      "authors": [
        17
      ]
    },
    {
      "id": "26",
      "episodeId": "55",
      "title": "All about Preferences DataStore",
      "content": "In this post, we will take a look at Preferences DataStore, one of two DataStore implementations. We will go over how to create it, read and write data, and how to handle exceptions, all of which should, hopefully, provide you with enough information to decide if it’s the right choice for your app.",
      "url": "https://medium.com/androiddevelopers/all-about-preferences-datastore-cc7995679334",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*UtNu7pmbt3WEA213SW9p9Q.png",
      "publishDate": "2022-01-24T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        3
      ]
    },
    {
      "id": "27",
      "episodeId": "55",
      "title": "Building apps for Android Automotive OS 🚘",
      "content": "The Car App Library version 1.2 is already in beta, enabling app developers to start building their navigation, parking, and charging apps for Android Automotive OS. Now, developers can begin building and testing apps for these categories using the Automotive OS emulator across both Android Automotive OS and Android Auto.",
      "url": "https://android-developers.googleblog.com/2022/01/building-apps-for-android-automotive-os.html",
      "headerImageUrl": "",
      "publishDate": "2022-01-27T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "16"
      ],
      "authors": [
        18
      ]
    },
    {
      "id": "28",
      "episodeId": "55",
      "title": "Navigation 2.4 is stable ",
      "content": "It’s been rewritten in Kotlin, with two pane integration, Navigation routes + Kotlin DSL improvements, Navigation Compose’s first stable release, and multiple back stack support.",
      "url": "https://developer.android.com/jetpack/androidx/releases/navigation#2.4.0",
      "headerImageUrl": "",
      "publishDate": "2022-01-26T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "29",
      "episodeId": "55",
      "title": "Google Maps with Jetpack Compose",
      "content": "A project which contains Jetpack Compose components for the Google Maps SDK for Android.\n\n",
      "url": "https://github.com/googlemaps/android-maps-compose",
      "headerImageUrl": "",
      "publishDate": "2022-02-11T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "9"
      ],
      "authors": []
    },
    {
      "id": "30",
      "episodeId": "55",
      "title": "Improving App Performance with Baseline Profiles",
      "content": "In this blog post we’ll discuss Baseline Profiles and how they improve app and library performance, including startup time by up to 40%. While this blogpost focuses on startup, baseline profiles also significantly improve jank as well.",
      "url": "https://android-developers.googleblog.com/2022/01/improving-app-performance-with-baseline.html",
      "headerImageUrl": "",
      "publishDate": "2022-01-28T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        19
      ]
    },
    {
      "id": "31",
      "episodeId": "55",
      "title": "Smule Adopts Google’s Oboe to Improve Recording Quality & Completion Rates",
      "content": "As the most downloaded singing app of all time, Smule Inc. has been investing on Android to improve the overall audio quality and, more specifically, to reduce latency, i.e. allowing singers to hear their voices in the headset as they perform. The teams specialized in Audio and Video allocated a significant part of 2021 into making the necessary changes to convert the Smule application used by over ten million Android users from using the OpenSL audio API to the Oboe audio library, enabling roughly a 10%+ increase in recording completion rate.",
      "url": "https://android-developers.googleblog.com/2022/02/smule-adopts-googles-oboe-to-improve.html",
      "headerImageUrl": "",
      "publishDate": "2022-02-02T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "4"
      ],
      "authors": []
    },
    {
      "id": "32",
      "episodeId": "55",
      "title": "Guide to background work",
      "content": "Do you use coroutines or WorkManager for background work? The team updated the guide to background work to help you choose which library is best for your use case. It depends on whether or not the work is persistent, and if it needs to run immediately, it’s long running, or deferrable.",
      "url": "https://developer.android.com/guide/background",
      "headerImageUrl": "",
      "publishDate": "2022-02-11T00:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "14"
      ],
      "authors": []
    },
    {
      "id": "33",
      "episodeId": "55",
      "title": "Accessibility best practices",
      "content": "If you work on Android TV, you should be aware of the accessibility best practices that the team created. It provides recommendations for both native and non-native apps. Get to know why accessibility is important for your TV app, how to evaluate your apps when TalkBack is used, how to adopt system caption settings, and more!",
      "url": "https://developer.android.com/training/tv/accessibility",
      "headerImageUrl": "",
      "publishDate": "2022-02-11T00:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "17",
        "15"
      ],
      "authors": []
    },
    {
      "id": "34",
      "episodeId": "55",
      "title": "TalkBack - the Google screen reader",
      "content": "Next up in the Accessibility series is TalkBack, the Google screen reader! In this video, learn what TalkBack is, how to set it up, how to navigate through your app with it, and how you can use it to improve the Accessibility of your app.",
      "url": "https://www.youtube.com/watch?v=_1yRVwhEv5I",
      "headerImageUrl": "",
      "publishDate": "2022-01-21T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": []
    },
    {
      "id": "35",
      "episodeId": "55",
      "title": "ADB Podcast 182: Large screens are a big deal",
      "content": "Clara, Florina and Daniel join your usual hosts to talk about large screens, what they are and what they mean for app developers. You will also learn about the resources at your disposal to build high quality experiences on large screen devices: from samples and guidance to canonical layouts and new APIs such as window size classes. Disclaimer: Florina is very excited about this, don’t miss the epic Large screens! Large screens! Large screens! intro!",
      "url": "https://adbackstage.libsyn.com/episode-182-large-screens-are-a-big-deal",
      "headerImageUrl": "",
      "publishDate": "2022-02-01T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "36",
      "episodeId": "54",
      "title": "Jetpack Alpha for Glance Widgets 🔍",
      "content": "We made the first release of Jetpack Glance available, a new framework designed to make it faster and easier to build app widgets for the home screen and other surfaces. Glance offers similar modern, declarative Kotlin APIs that you are used to with Jetpack Compose, helping you build beautiful, responsive app widgets with way less code. Glance provides a base-set of its own Composables to help build “glanceable” experiences — starting today with app widget components but with more coming. Using the Jetpack Compose runtime, Glance translates these Composables into RemoteViews that can be displayed in an app widget",
      "url": "https://android-developers.googleblog.com/2021/12/announcing-jetpack-glance-alpha-for-app.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgol-A5cMCZY79MH5v0axcekWIVJ--ymPUe0U5Q4BLsC0BA1LTbWIlZ76XWi2cHjxHVu-kbpv0o2QJWBjNAda_93Ah7AW_PcAgz9o082cd6zyTJZAM8HjQnrZ69A6CaKQaCFuf2LLi4p6xRvS_WUn9tVA2K2wmV3_qB6JDKnFNhO3Guvn5tPc_SuoaY",
      "publishDate": "2021-12-15T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6"
      ],
      "authors": [
        22
      ]
    },
    {
      "id": "37",
      "episodeId": "54",
      "title": "Jetpack Watch Face Library ⌚",
      "content": "We launched the Jetpack Watch Face library written from the ground up in Kotlin, including all functionality from the Wearable Support Library along with many new features such as: Watch face styling which persists across both the watch and phone (with no need for your own database or companion app); Support for a WYSIWYG watch face configuration UI on the phone; Smaller, separate libraries (that only include what you need); Battery improvements through promoting good battery usage patterns out of the box, such as automatically reducing the interactive frame rate when the battery is low; New screenshot APIs so users can see previews of their watch face changes in real time on both the watch and phone.\n\nIf you are still using the Wearable Support Library, we strongly encourage migrating to the new Jetpack libraries to take advantage of the new APIs and upcoming features and bug fixes.",
      "url": "https://android-developers.googleblog.com/2021/12/develop-watch-faces-with-stable-jetpack.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-P4S1eEhqouE/YaaFy_bGD1I/AAAAAAAARNA/-w5O05Mppo8pe0hoeMC1yDNRWiX_mnTOgCLcBGAsYHQ/s0/image1.png",
      "publishDate": "2021-12-01T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "19",
        "9",
        "6"
      ],
      "authors": [
        23
      ]
    },
    {
      "id": "38",
      "episodeId": "54",
      "title": "Rebuilding our Guide to App Architecture 📐",
      "content": "We launched a revamped guide to app architecture which includes best practices. As Android apps grow in size, it’s important to design the code with an architecture in place that allows the app to scale, improves quality and robustness, and makes testing easier. The guide contains pages for UI, domain, and data layers including deep dives into more complex topics, such as how to handle UI events. We also have a learning pathway to walk you through it.",
      "url": "https://android-developers.googleblog.com/2021/12/rebuilding-our-guide-to-app-architecture.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgnJ0CCtKClhEOE_BDOoWiXGr2eA6LWjn-RPvFjFx8Va97f_1_xCmpF3uI_bUILoQPqJUDlXUbIRVPjvi3oCiFtRVZlcAAkHBa1cJlufG5OvmeovQeiHgH9bLhxREufi-fw7FnxIcmxGmzWuW0DmYUZolsM6rywTSZIm3KtI6yx9jSIeRpuYzRZubke",
      "publishDate": "2021-12-14T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "14"
      ],
      "authors": [
        24
      ]
    },
    {
      "id": "39",
      "episodeId": "54",
      "title": "Google Play Games on PC Beta 🎮",
      "content": "We announced that we’re opening sign-ups for Google Play Games on PC as a beta in Korea, Taiwan, and Hong Kong, allowing users participating in the beta to play a catalog of Google Play games on their PC via a standalone application built by Google. The developer site has a form to express interest, along with information about bringing your Android game to PCs. It involves many of the same updates that you do to optimize your game for Chrome OS devices, such as support for Mouse and Keyboard controls.",
      "url": "https://developers.googleblog.com/2022/01/googleplaygames.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgsNv-PVLNLlX2SYd2p5DwTN2Jxwb54Rc7Ekbm0LgcFuwHBrF_5Y-DiUblL9oTjmeJ1Y44nPRMMkH5K-xlC0OApgUGxqBpUcfuV1LYPVvKsI67BKTpc_gNhaHsNda6Q1Uk1UvTznmMydqNHtXSqTgSJbjpQCoTGZM_ZLXlkGwMoBFfnMQkAIdl2zjsC",
      "publishDate": "2022-01-19T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "18"
      ],
      "authors": [
        25
      ]
    },
    {
      "id": "40",
      "episodeId": "54",
      "title": "MAD Skills: Gradle 🐘",
      "content": "Murat covered building custom plugins in more depth, including the Artifact API in addition to the Variant API covered previously. It demonstrates building a plugin which automatically updates the version code specified in the app manifest with the git version. With the AGP 7.0 release, you can use these APIs to control build inputs, read, modify, or even replace intermediate and final artifacts.",
      "url": "https://medium.com/androiddevelopers/gradle-and-agp-build-apis-taking-your-plugin-to-the-next-step-95e7bd1cd4c9",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*WkRft2aAKv19MoIm.jpeg",
      "publishDate": "2021-12-01T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "12"
      ],
      "authors": [
        26
      ]
    },
    {
      "id": "41",
      "episodeId": "54",
      "title": "Gradle and AGP Build APIs: Community tip - MAD Skills",
      "content": "On this episode of Gradle and AGP Build APIs for MAD Skills, Alex Saveau walks you through manipulating Android build artifacts with the Android Gradle Plugin (AGP) and Gradle APIs.",
      "url": "https://www.youtube.com/watch?v=8SFfffaB0CU",
      "headerImageUrl": "https://i3.ytimg.com/vi/8SFfffaB0CU/maxresdefault.jpg",
      "publishDate": "2021-12-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12"
      ],
      "authors": [
        27
      ]
    },
    {
      "id": "42",
      "episodeId": "54",
      "title": "Gradle and AGP Build APIs: Taking your plugin to the next step - MAD Skills",
      "content": "On this episode of Gradle and AGP Build APIs for MAD Skills, Murat will discuss Gradle tasks, providers, properties, and basics of task inputs and outputs. Next, you will be able to take your plugin a step further and learn how to get access to various build artifacts using the new Artifact API. ",
      "url": "https://www.youtube.com/watch?v=SB4QlngQQW0",
      "headerImageUrl": "https://i3.ytimg.com/vi/SB4QlngQQW0/maxresdefault.jpg",
      "publishDate": "2021-11-29T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12"
      ],
      "authors": [
        26
      ]
    },
    {
      "id": "43",
      "episodeId": "54",
      "title": "MAD Skills Gradle and AGP build APIs Wrap Up!",
      "content": "This wrap-up post summarizes the whole MAD Skills Gradle series",
      "url": "https://android-developers.googleblog.com/2021/12/mad-skills-gradle-and-agp-build-apis.html",
      "headerImageUrl": "https://blogger.googleusercontent.com/img/a/AVvXsEgo1Fw61B9qtQESKdVJzcNXOG0RzhA2k85zkDMDNidBiQY7B6uguHXQ9t9IPB9BiHS0WTB1b4fwIgeN5zEIJrmznF9pt5lu9186wvXxJ3IKfLi8Fci8LyMDbQKGYc7nnijJ9_lhrNHtRQamaF2GTSXyJq5_lQk7we3cSfSviOxhgKN9TscMJaGgdMZJ",
      "publishDate": "2021-12-16T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "12"
      ],
      "authors": [
        26
      ]
    },
    {
      "id": "44",
      "episodeId": "54",
      "title": "MAD Skills: DataStore 🗄️",
      "content": "Simona began MAD Skills: DataStore. DataStore is a thread-safe, non-blocking library in Android Jetpack that provides a safe and consistent way to store small amounts of data, such as preferences or application state, replacing SharedPreferences. It provides an implementation that stores typed objects backed by protocol buffers (Proto DataStore) and an implementation that stores key-value pairs (Preferences DataStore).",
      "url": "https://www.youtube.com/watch?v=9ws-cJzlJkU",
      "headerImageUrl": "https://i3.ytimg.com/vi/9ws-cJzlJkU/maxresdefault.jpg",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "7"
      ],
      "authors": []
    },
    {
      "id": "45",
      "episodeId": "54",
      "title": "AndroidX releases 🚀",
      "content": "Since the last Now in Android episode, a lot of libraries were promoted to stable! Compose ConstraintLayout brings support for ConstraintLayout syntax to Compose. We also released CoordinatorLayout 1.2, Car App 1.1.0, Room 2.4.0, Sqlite 2.2.0, Collection 1.2.0, and Wear Watchface 1.0.0.\n\nOur first alpha of Jetpack Compose 1.2 was released, along with alphas for Glance 1.0.0, Core-Ktx 1.8.0, WorkManager 2.8.0, Mediarouter 1.3.0, Emoji2 1.1.0, Annotation 1.4.0, Core-RemoteViews, Core-Peformance, and more.",
      "url": "https://developer.android.com/jetpack/androidx/versions/all-channel#december_1_2021",
      "headerImageUrl": "",
      "publishDate": "2021-12-01T00:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "6"
      ],
      "authors": []
    },
    {
      "id": "46",
      "episodeId": "54",
      "title": "Jetnews for every screen",
      "content": "Alex wrote about the recent updates to Jetnews that improves its behavior across big and small mobile devices. It describes our design and development process so that you can learn our philosophy and associated implementation steps for building an application optimized for all screens with Jetpack Compose, including how to build a list/detail layout.",
      "url": "https://medium.com/androiddevelopers/jetnews-for-every-screen-4d8e7927752",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*678DlYtu4G7wFrq30FQ7Mw.png",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1"
      ],
      "authors": [
        23
      ]
    },
    {
      "id": "47",
      "episodeId": "54",
      "title": "Simplifying drag and drop",
      "content": "Paul wrote about drag & drop, and how the Android Jetpack DragAndDrop library alpha makes it easier to handle data dropped into your app.",
      "url": "https://medium.com/androiddevelopers/simplifying-drag-and-drop-3713d6ef526e",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*pUe4RBLe7FVlISDtAqeQ4Q.png",
      "publishDate": "2021-12-15T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6"
      ],
      "authors": [
        28
      ]
    },
    {
      "id": "48",
      "episodeId": "54",
      "title": "Accessibility series 🌐: Handling content that times out - Accessibility on Android",
      "content": "The accessibility series continues on, beginning with an episode on how to properly implement UI elements that disappear after a set amount of time.",
      "url": "https://www.youtube.com/watch?v=X97P6Y8WHl0",
      "headerImageUrl": "https://i3.ytimg.com/vi/X97P6Y8WHl0/maxresdefault.jpg",
      "publishDate": "2021-12-03T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": [
        29
      ]
    },
    {
      "id": "49",
      "episodeId": "54",
      "title": "Accessibility series 🌐: Acessibility Scanner",
      "content": "We also cover how Accessibility Scanner can help you improve your app for all users by suggesting improvements in areas of accessibility.",
      "url": "https://www.youtube.com/watch?v=i1gMzQv0hWU",
      "headerImageUrl": "https://i3.ytimg.com/vi/i1gMzQv0hWU/maxresdefault.jpg",
      "publishDate": "2021-12-10T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": [
        29
      ]
    },
    {
      "id": "50",
      "episodeId": "54",
      "title": "Accessibility series 🌐: Accessibility test framework and Espresso - Accessibility on Android",
      "content": "We investigate how Espresso and the Accessibility Test Framework can help you create automated accessibility tests.",
      "url": "https://www.youtube.com/watch?v=DLN2s16HwcE",
      "headerImageUrl": "https://i3.ytimg.com/vi/DLN2s16HwcE/maxresdefault.jpg",
      "publishDate": "2021-12-22T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": [
        29
      ]
    },
    {
      "id": "51",
      "episodeId": "54",
      "title": "Android TV & Google TV 📺",
      "content": "Mayuri covered best practices for the Watch Next API on Android TV & Google TV, which increases engagement with your app by allowing your content to show up in the Watch Next row.",
      "url": "https://www.youtube.com/watch?v=QFMIP5GOo70",
      "headerImageUrl": "https://i3.ytimg.com/vi/QFMIP5GOo70/maxresdefault.jpg",
      "publishDate": "2022-01-14T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "17"
      ],
      "authors": [
        30
      ]
    },
    {
      "id": "52",
      "episodeId": "54",
      "title": "ADB Podcast 179: Flibberty Widget",
      "content": "In this episode, Chet and Romain talked with Nicole McWilliams and Petr Čermák from the London engineering office about their work on App Widgets and Digital Wellbeing.",
      "url": "https://adbackstage.libsyn.com/flibberty-widget",
      "headerImageUrl": "https://ssl-static.libsyn.com/p/assets/4/0/e/c/40ec1fb11096bffed959afa2a1bf1c87/adb-180-flibberty-widget.png",
      "publishDate": "2021-11-30T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "13"
      ],
      "authors": [
        31
      ]
    },
    {
      "id": "53",
      "episodeId": "54",
      "title": "ADB Podcast 180: Kotlin Magic Platform",
      "content": "In this episode, we chat with Yigit Boyar from the Android Toolkit Team about Kotlin multi platform, while Romain provides light background music on his piano.",
      "url": "https://adbackstage.libsyn.com/episode-180-kotlin-magic-platform",
      "headerImageUrl": "https://ssl-static.libsyn.com/p/assets/2/6/2/5/262599d4ce76d20fa04421dee9605cbd/adb-181-kmp.png",
      "publishDate": "2021-12-16T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "8"
      ],
      "authors": [
        31
      ]
    },
    {
      "id": "54",
      "episodeId": "54",
      "title": "ADB Podcast 181: Architecture → Fewer bugs at the end",
      "content": "In this episode, we chat with Yigit Boyar (again!) from the Android Toolkit Team and Manuel Vivo from the Developer Relations team about application architecture. The team has released new architecture guidance, and we talk about that guidance here, as well as how our architecture recommendations apply in the new Jetpack Compose world.",
      "url": "https://adbackstage.libsyn.com/episode-181-architecture-fewer-bugs-at-the-end",
      "headerImageUrl": "https://ssl-static.libsyn.com/p/assets/8/d/1/3/8d137b65f392a68c27a2322813b393ee/ADB_181_Architecture.png",
      "publishDate": "2022-01-11T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "14"
      ],
      "authors": [
        32
      ]
    },
    {
      "id": "55",
      "episodeId": "53",
      "title": "Android 12",
      "content": "We released Android 12 and pushed it to the Android Open Source Project (AOSP). We introduced a new design language called Material You. We reduced the CPU time used by core system services, added performance class device capabilities, and added new features to improve performance. Users have more control of their privacy with the Privacy Dashboard and other new security and privacy features. We improved the user experience with a unified API for rich content insertion, compatible media transcoding, easier blurs and effects, AVIF image support, enhanced haptics, new camera effects/capabilities, improved native crash debugging, support for rounded screen corners, Play as you download, and Game Mode APIs.",
      "url": "https://android-developers.googleblog.com/2021/10/android-12-is-live-in-aosp.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-mGlzRmn42Rs/YVstltyrboI/AAAAAAAAK3A/44QpoNJDeuoHhlgrRJSbk0L_ZopgFDLFACLcBGAsYHQ/s0/Android%2B12%2Blogo.png",
      "publishDate": "2021-10-03T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "13"
      ],
      "authors": [
        14
      ]
    },
    {
      "id": "56",
      "episodeId": "53",
      "title": "Compose",
      "content": "Jetpack Compose, Android’s modern, native UI toolkit became stable and ready for you to adopt in production. It interoperates with your existing app, integrates with existing Jetpack libraries, implements Material Design with straightforward theming, supports lists with Lazy components using minimal boilerplate, and has a powerful, extensible animation system. You can learn more about working with Compose in the Compose learning path and see where we’re going in future Compose releases in the Compose roadmap.",
      "url": "https://developer.android.com/jetpack/compose",
      "headerImageUrl": "",
      "publishDate": "2021-12-07T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "9"
      ],
      "authors": []
    },
    {
      "id": "57",
      "episodeId": "53",
      "title": "Training",
      "content": "This year, the Android Training Team released the final four new units of Android Basics in Kotlin.",
      "url": "https://developer.android.com/courses/android-basics-kotlin/course",
      "headerImageUrl": "https://developer.android.com/images/hero-assets/android-basics-kotlin.svg",
      "publishDate": "2021-12-07T00:00:00.000Z",
      "type": "Codelab",
      "topics": [
        "8"
      ],
      "authors": []
    },
    {
      "id": "58",
      "episodeId": "53",
      "title": "Introduction to Kotlin and Jetpack ",
      "content": "Learn the basics of Jetpack KTX libraries, how to simplify callbacks with coroutines and Flow, and how to use and test Room/WorkManager APIs.",
      "url": "https://youtu.be/nw7nnlHDkHw?list=PLWz5rJ2EKKc98e0f5ZbsgB63MdjZTFgsy",
      "headerImageUrl": "https://i3.ytimg.com/vi/nw7nnlHDkHw/maxresdefault.jpg",
      "publishDate": "2021-12-14T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "8",
        "6"
      ],
      "authors": [
        6
      ]
    },
    {
      "id": "59",
      "episodeId": "53",
      "title": "Introduction to Motion Layout",
      "content": "Learn how to use MotionLayout and its design tool to create rich, animated experiences.",
      "url": "https://www.youtube.com/watch?v=M1jE3W3_NTQ&list=PLWz5rJ2EKKc_PEOEHNBEyy6tPX1EgtUw2",
      "headerImageUrl": "https://i3.ytimg.com/vi/M1jE3W3_NTQ/maxresdefault.jpg",
      "publishDate": "2022-01-19T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "8",
        "1",
        "6"
      ],
      "authors": [
        36
      ]
    },
    {
      "id": "60",
      "episodeId": "53",
      "title": "Introduction to WorkManager",
      "content": "Learn how to schedule critical background work with WorkManager: from basic usage, threading, custom configuration and more.",
      "url": "https://www.youtube.com/watch?v=NtpgWjiXEfg&list=PLWz5rJ2EKKc_J88-h0PhCO_aV0HIAs9Qk",
      "headerImageUrl": "https://i3.ytimg.com/vi/NtpgWjiXEfg/maxresdefault.jpg",
      "publishDate": "2022-03-01T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "6"
      ],
      "authors": [
        37
      ]
    },
    {
      "id": "61",
      "episodeId": "53",
      "title": "Introduction to Navigation",
      "content": "Learn the basics of the Navigation component, specific features of the tool and the APIs to create and navigate to destinations.",
      "url": "https://www.youtube.com/watch?list=PLWz5rJ2EKKc9VpBMZUS9geQtc5RJ2RsUd&v=fiQiMy0HzsY&feature=emb_title",
      "headerImageUrl": "https://i3.ytimg.com/vi/fiQiMy0HzsY/maxresdefault.jpg",
      "publishDate": "2022-03-25T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "6"
      ],
      "authors": [
        38
      ]
    },
    {
      "id": "62",
      "episodeId": "53",
      "title": "Introduction to Performance",
      "content": "Learn about using system tracing and sampling profiling to debug performance issues in apps.",
      "url": "https://www.youtube.com/watch?v=_5LgIrd4O5g&list=PLWz5rJ2EKKc-xjSI-rWn9SViXivBhQUnp",
      "headerImageUrl": "https://i3.ytimg.com/vi/_5LgIrd4O5g/maxresdefault.jpg",
      "publishDate": "2021-07-18T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3"
      ],
      "authors": [
        39
      ]
    },
    {
      "id": "63",
      "episodeId": "53",
      "title": "Introduction to Hilt",
      "content": "Learn how to add and use Hilt for dependency injection in your Android app, best practices for testing with Hilt, and more advanced content.",
      "url": "https://www.youtube.com/watch?v=mnMCgjuMJPA&list=PLWz5rJ2EKKc_9Qo-RBRYhVmME1iR4oeTK",
      "headerImageUrl": "https://i3.ytimg.com/vi/mnMCgjuMJPA/maxresdefault.jpg",
      "publishDate": "2021-08-22T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "6"
      ],
      "authors": [
        40
      ]
    },
    {
      "id": "64",
      "episodeId": "53",
      "title": "Paging",
      "content": "Learn the basics of paging, from the core types to binding them to your UI elements.",
      "url": "https://www.youtube.com/watch?v=Pw-jhS-ucYA&list=PLWz5rJ2EKKc9L-fmWJLhyXrdPi1YKmvqS",
      "headerImageUrl": "https://i3.ytimg.com/vi/Pw-jhS-ucYA/maxresdefault.jpg",
      "publishDate": "2021-09-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "6"
      ],
      "authors": [
        41
      ]
    },
    {
      "id": "65",
      "episodeId": "53",
      "title": "Introduction to Gradle and AGP Build APIs\n",
      "content": "Learn how to configure your build, customize the build process to your needs and how to write your own plugins to extend your build even further.",
      "url": "https://www.youtube.com/watch?v=mk0XBWenod8&list=PLWz5rJ2EKKc8fyNmwKXYvA2CqxMhXqKXX",
      "headerImageUrl": "https://i3.ytimg.com/vi/mk0XBWenod8/maxresdefault.jpg",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12",
        "5"
      ],
      "authors": [
        26
      ]
    },
    {
      "id": "66",
      "episodeId": "53",
      "title": "Google I/O",
      "content": "At I/O we released updates in Jetpack, Compose, Android Studio tooling, Large screens, Wear OS, Testing, and more! Get caught up on all the Android videos from I/O!",
      "url": "https://www.youtube.com/watch?v=D_mVOAXcrtc",
      "headerImageUrl": "https://i3.ytimg.com/vi/D_mVOAXcrtc/maxresdefault.jpg",
      "publishDate": "2021-05-17T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "0"
      ],
      "authors": []
    },
    {
      "id": "67",
      "episodeId": "53",
      "title": "Android Dev Summit",
      "content": "At Android Dev Summit we released updates on privacy and security, large screens, Android 12, Google Play & Games, Building across screens, Jetpack Compose, Modern Android Development and more. Check out all the videos from ADS!",
      "url": "https://www.youtube.com/watch?v=WZgR5Yf1iq8",
      "headerImageUrl": "https://i3.ytimg.com/vi/WZgR5Yf1iq8/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "0"
      ],
      "authors": []
    },
    {
      "id": "68",
      "episodeId": "52",
      "title": "Conveying state for Accessibility",
      "content": "In this episode of the Accessibility series, you can learn more about the StateDescription API, when to use stateDescription and contentDescription, and how to represent error states to the end user.",
      "url": "https://youtu.be/JvWM2PjLJls",
      "headerImageUrl": "https://i.ytimg.com/vi/JvWM2PjLJls/maxresdefault.jpg",
      "publishDate": "2021-11-30T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": [
        42
      ]
    },
    {
      "id": "69",
      "episodeId": "52",
      "title": "Take your Gradle plugin to the next step",
      "content": "This third and last episode of the Gradle MAD Skills series teaches you how to get access to various build artifacts using the new Artifact API.",
      "url": "https://youtu.be/SB4QlngQQW0",
      "headerImageUrl": "https://i.ytimg.com/vi/SB4QlngQQW0/maxresdefault.jpg",
      "publishDate": "2021-11-29T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12"
      ],
      "authors": [
        43
      ]
    },
    {
      "id": "70",
      "episodeId": "52",
      "title": "How to write a Gradle plugin",
      "content": "In this second episode of the Gradle MAD Skills series, Murat explains how to write your own custom Gradle plugin.",
      "url": "https://youtu.be/LPzBVtwGxlo",
      "headerImageUrl": "https://i.ytimg.com/vi/LPzBVtwGxlo/maxresdefault.jpg",
      "publishDate": "2021-11-22T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12"
      ],
      "authors": [
        43
      ]
    },
    {
      "id": "71",
      "episodeId": "52",
      "title": "Convert YUV to RGB for CameraX Image Analysis",
      "content": "Learn about a new feature in CameraX to convert YUV, the format that CameraX produces, to RGB used for image analysis capabilities available in TensorFlow Lite, for example. Read the blog post for more information about these formats and how to use the new conversion feature.",
      "url": "https://medium.com/androiddevelopers/convert-yuv-to-rgb-for-camerax-imageanalysis-6c627f3a0292",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*cuOorbZgMbRvkSSGuDGccw.png",
      "publishDate": "2021-11-19T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6",
        "4"
      ],
      "authors": [
        44
      ]
    },
    {
      "id": "72",
      "episodeId": "52",
      "title": "AppCompat, Activity, and Fragment to support multiple back stacks",
      "content": "The 1.4.0 release of these libraries brings stable support for multiple back stacks.",
      "url": "https://developer.android.com/jetpack/androidx/releases/appcompat#1.4.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "6",
        "1"
      ],
      "authors": []
    },
    {
      "id": "73",
      "episodeId": "52",
      "title": "Emoji2 adds support for modern emojis",
      "content": "The 1.0 stable release of Emoji2 allows you to use modern emojis in your app.",
      "url": "https://developer.android.com/jetpack/androidx/releases/emoji2#1.0.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "6",
        "1"
      ],
      "authors": []
    },
    {
      "id": "74",
      "episodeId": "52",
      "title": "Lifecycle introduces lifecycle-aware coroutine APIs",
      "content": "The new 2.4 release of Lifecycle introduces repeatOnLifecycle and flowWithLifecycle.",
      "url": "https://developer.android.com/jetpack/androidx/releases/lifecycle#2.4.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "6",
        "14"
      ],
      "authors": []
    },
    {
      "id": "75",
      "episodeId": "52",
      "title": "Paging release brings changes to LoadState",
      "content": "The new 3.1 release of Paging changes the behavior of LoadState.",
      "url": "https://developer.android.com/jetpack/androidx/releases/paging#3.1.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "6",
        "1"
      ],
      "authors": []
    },
    {
      "id": "76",
      "episodeId": "52",
      "title": "Wear tiles released as 1.0 stable",
      "content": "The library that you use to build custom tiles for Wear OS devices is now stable.",
      "url": "https://developer.android.com/jetpack/androidx/releases/wear-tiles#1.0.0",
      "headerImageUrl": "",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "6",
        "19"
      ],
      "authors": []
    },
    {
      "id": "77",
      "episodeId": "52",
      "title": "About Custom Accessibility Actions",
      "content": "The accessibility series continues on with more information on how to create custom accessibility actions to make your apps more accessible. You can provide a custom action to the accessibility services and implement logic related to the action. For more information, check out the following episode!",
      "url": "https://youtu.be/wWDYIGk0Kdo",
      "headerImageUrl": "https://i.ytimg.com/vi/wWDYIGk0Kdo/maxresdefault.jpg",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": [
        42
      ]
    },
    {
      "id": "78",
      "episodeId": "52",
      "title": "Improving App Startup: Lessons from the Facebook App",
      "content": "Improving app startup time is not a trivial task and requires a deep understanding of things that affect it. This year, the Android team and the Facebook app team have been working together on metrics and sharing approaches to improve app startup. Read more about the findings in this blog post.",
      "url": "https://android-developers.googleblog.com/2021/11/improving-app-startup-facebook-app.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-5VyrQpFJufM/YaVKxf_DanI/AAAAAAAALS4/ybeza_emDKoKP0gjiNkqfDS_ltwo0075ACLcBGAsYHQ/w1200-h630-p-k-no-nu/AppExcellence_Editorial_LessonsFromFBApp_4209x1253-01%2B%25281%2529%2B%25281%2529.png",
      "publishDate": "2021-11-16T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": []
    },
    {
      "id": "79",
      "episodeId": "52",
      "title": "Gradle series kicks off",
      "content": "Murat introduces the Gradle series and everything you'll learn in it.",
      "url": "https://youtu.be/mk0XBWenod8",
      "headerImageUrl": "https://i.ytimg.com/vi/mk0XBWenod8/maxresdefault.jpg",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12"
      ],
      "authors": [
        43
      ]
    },
    {
      "id": "80",
      "episodeId": "52",
      "title": "Intro to Gradle and AGP",
      "content": "In the first episode of the Gradle MAD Skills series, Murat explains how the Android build system works, and how to configure your build.",
      "url": "https://youtu.be/GjPS4xDMmQY",
      "headerImageUrl": "https://i.ytimg.com/vi/GjPS4xDMmQY/maxresdefault.jpg",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "12"
      ],
      "authors": [
        43
      ]
    },
    {
      "id": "81",
      "episodeId": "52",
      "title": "ADB Podcast episode 179 Hosts 3, Guests 0",
      "content": "Chet, Romain and Tor sit down to chat about the Android Developer Summit, and in particular all the new features arriving in Android Studio, along with a few other topics like Chet’s new jank stats library, the Android 12L release, and more.",
      "url": "https://adbackstage.libsyn.com/episode-178-hosts-3-guests-0",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "5",
        "3",
        "13"
      ],
      "authors": [
        38
      ]
    },
    {
      "id": "82",
      "episodeId": "52",
      "title": "The problem with emojis and how emoji2 can help out",
      "content": "Meghan wrote about the new emoji2 library that just became stable.",
      "url": "https://medium.com/androiddevelopers/support-modern-emoji-99f6dea8e57f",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*yAOOlpXKKUl5nWWsPkNb7g.png",
      "publishDate": "2021-11-12T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6",
        "1"
      ],
      "authors": [
        45
      ]
    },
    {
      "id": "83",
      "episodeId": "52",
      "title": "Paging Q&A",
      "content": "In this live session, TJ and Dustin answered your questions in the usual live Q&A format.",
      "url": "https://youtu.be/8i6vrlbIVCc",
      "headerImageUrl": "https://i.ytimg.com/vi/8i6vrlbIVCc/maxresdefault.jpg",
      "publishDate": "2021-11-11T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "84",
      "episodeId": "52",
      "title": "Thanks for helping us reach 1M YouTube Subscribers",
      "content": "Thank you everyone for following the Now in Android series and everything the Android Developers YouTube channel has to offer. During the Android Developer Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to thank you all.",
      "url": "https://youtu.be/-fJ6poHQrjM",
      "headerImageUrl": "https://i.ytimg.com/vi/-fJ6poHQrjM/maxresdefault.jpg",
      "publishDate": "2021-11-09T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "0"
      ],
      "authors": []
    },
    {
      "id": "85",
      "episodeId": "52",
      "title": "Community tip on Paging",
      "content": "Tips for using the Paging library from the developer community",
      "url": "https://youtu.be/r5JgIyS3t3s",
      "headerImageUrl": "https://i.ytimg.com/vi/r5JgIyS3t3s/maxresdefault.jpg",
      "publishDate": "2021-11-08T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "86",
      "episodeId": "52",
      "title": "Transformations and customisations in the Paging Library",
      "content": "A demonstration of different operations that can be performed with Paging. Transformations like inserting separators, when to create a new pager, and customisation options for consuming PagingData.",
      "url": "https://youtu.be/ZARz0pjm5YM",
      "headerImageUrl": "https://i.ytimg.com/vi/ZARz0pjm5YM/maxresdefault.jpg",
      "publishDate": "2021-11-01T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": [
        41
      ]
    },
    {
      "id": "87",
      "episodeId": "52",
      "title": "New Compose for Wear OS codelab",
      "content": "In this codelab, you can learn how Wear OS can work with Compose, what Wear OS specific composables are available, and more!",
      "url": "https://developer.android.com/codelabs/compose-for-wear-os",
      "headerImageUrl": "https://developer.android.com/codelabs/compose-for-wear-os/img/4d28d16f3f514083.png",
      "publishDate": "2021-10-27T23:00:00.000Z",
      "type": "Codelab",
      "topics": [
        "9",
        "19"
      ],
      "authors": [
        46
      ]
    },
    {
      "id": "88",
      "episodeId": "52",
      "title": "Introducing Jetpack Media3",
      "content": "The first alpha version of this new library is now available. Media3 is a collection of support libraries for media playback, including ExoPlayer. The following article explains why the team created Media3, what it contains, and how it can simplify your app architecture.",
      "url": "https://developer.android.com/jetpack/androidx/releases/media3",
      "headerImageUrl": "",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6",
        "4"
      ],
      "authors": [
        47
      ]
    },
    {
      "id": "89",
      "episodeId": "50",
      "title": "Building apps which are private by design",
      "content": "Sara N-Marandi, product manager, and Yacine Rezgui, developer relations engineer, provided guidelines and best practices on how to build apps that are private by design, covered new privacy features in Android 12 and previewed upcoming Android concepts.",
      "url": "https://youtu.be/hBVwr2ErQCw",
      "headerImageUrl": "https://i.ytimg.com/vi/hBVwr2ErQCw/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "10"
      ],
      "authors": []
    },
    {
      "id": "90",
      "episodeId": "50",
      "title": "Memory Safety Tools",
      "content": "Serban Constantinescu, product manager, talked about the Memory Safety Tools that became available starting in Android 11 and have continued to evolve in Android 12. These tools can help address memory bugs and improve the quality and security of your application.",
      "url": "https://youtu.be/JqLcTFpXreg",
      "headerImageUrl": "https://i.ytimg.com/vi/JqLcTFpXreg/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "10"
      ],
      "authors": []
    },
    {
      "id": "91",
      "episodeId": "50",
      "title": "Increasing User Transparency with Privacy Dashboard",
      "content": "Android is ever evolving in its quest to protect users’ privacy. In Android 12, the platform increases transparency by introducing Privacy Dashboard, which gives users a simple and clear timeline view of the apps that have accessed location, microphone and camera within the past 24 hours. ",
      "url": "https://medium.com/androiddevelopers/increasing-user-transparency-with-privacy-dashboard-23064f2d7ff6",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*cgaSAY9AvPWlndLimzIIzQ.png",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "10"
      ],
      "authors": [
        15
      ]
    },
    {
      "id": "92",
      "episodeId": "50",
      "title": "The most unusual and interesting security issues addressed last year",
      "content": "Lilian Young, software engineer, presented a selection of the most unusual, intricate, and interesting security issues addressed in the last year. Developers and researchers are able to contribute to the security of the Android platform by submitting to the Android Vulnerability Rewards Program.",
      "url": "https://medium.com/androiddevelopers/now-in-android-50-ads-special-9934422f8dd1",
      "headerImageUrl": "https://miro.medium.com/max/1400/0*6h0XYdyki_1jfImJ",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "10"
      ],
      "authors": [
        48
      ]
    },
    {
      "id": "93",
      "episodeId": "50",
      "title": "New Data Safety section in the Play Console",
      "content": "The new Data safety section will give you a simple way to showcase your app’s overall safety. It gives you a place to give users deeper insight into your app’s privacy and security practices, and explain the data your app may collect and why — all before users install.",
      "url": "https://youtu.be/J7TM0Yy0aTQ",
      "headerImageUrl": "https://i.ytimg.com/vi/J7TM0Yy0aTQ/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "10",
        "11"
      ],
      "authors": []
    },
    {
      "id": "94",
      "episodeId": "50",
      "title": "Building Android UIs for any screen size",
      "content": "Clara Bayarri, engineering manager and Daniel Jacobson, product manager, talked about the state of the ecosystem, focusing on new design guidance, APIs, and tools to help you make the most of your UI on different screen sizes.",
      "url": "https://youtu.be/ir3LztqbeRI",
      "headerImageUrl": "https://i.ytimg.com/vi/ir3LztqbeRI/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "95",
      "episodeId": "50",
      "title": "What's new for large screens & foldables",
      "content": "Emilie Roberts, Chrome OS developer advocate and Andrii Kulian, Android software engineer, introduced new features focused specifically on making apps look great on large screens, foldables, and Chrome OS. ",
      "url": "https://youtu.be/6-925K3hMHU",
      "headerImageUrl": "https://i.ytimg.com/vi/6-925K3hMHU/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "96",
      "episodeId": "50",
      "title": "Enable great input support for all devices",
      "content": "Users expect seamless experiences when using keyboards, mice, and stylus. Emilie Roberts taught us how to handle common keyboard and mouse input events and how to get started with more advanced support like keyboard shortcuts, low-latency styluses, MIDI, and more.",
      "url": "https://youtu.be/piLEZYTc_4g",
      "headerImageUrl": "https://i.ytimg.com/vi/piLEZYTc_4g/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "97",
      "episodeId": "50",
      "title": "Best practices for video apps on foldable devices",
      "content": "Francesco Romano, developer advocate, and Will Chan, product manager at Zoom explored new user experiences made possible by the foldable form factor, focusing on video conferencing and media applications. ",
      "url": "https://youtu.be/DBAek_P0nEw",
      "headerImageUrl": "https://i.ytimg.com/vi/DBAek_P0nEw/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1",
        "4"
      ],
      "authors": []
    },
    {
      "id": "98",
      "episodeId": "50",
      "title": "Design beautiful apps on foldables and large screens",
      "content": "Liam Spradlin, design advocate, and Jonathan Koren, developer relations engineer, talked about how to design and test Android applications that look and feel great across device types and screen sizes, from tablets to foldables to Chrome OS.",
      "url": "https://youtu.be/DJeJIJKOUbI",
      "headerImageUrl": "https://i.ytimg.com/vi/DJeJIJKOUbI/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "99",
      "episodeId": "50",
      "title": "12L and new Android APIs and tools for large screens",
      "content": "Dave Burke, vice president of engineering, wrote a post covering the developer preview of 12L, an upcoming feature drop that makes Android 12 even better on large screens. ",
      "url": "https://android-developers.googleblog.com/2021/10/12L-preview-large-screens.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-sjT5kFGiQtg/YXlpg0uByLI/AAAAAAAARJk/XHO_uo5bRJcMeQVm0Fn1wN-qe54FGI7MgCLcBGAsYHQ/w1200-h630-p-k-no-nu/12L-devices-hero.png",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1",
        "13"
      ],
      "authors": []
    },
    {
      "id": "100",
      "episodeId": "50",
      "title": "New features in ML Kit: Text Recognition V2 & Pose Detections",
      "content": "Zongmin Sun, software engineer, and Valentin Bazarevsky, MediaPipe Engineer, talked about Text Recognition V2 & Pose Detection, recently-released features in ML Kit. ",
      "url": "https://youtu.be/9EKQ0UC04S8",
      "headerImageUrl": "https://i.ytimg.com/vi/9EKQ0UC04S8/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "6",
        "1"
      ],
      "authors": []
    },
    {
      "id": "101",
      "episodeId": "50",
      "title": "How to retain users with Android backup and restore",
      "content": "In this talk, Martin Millmore, engineering manager, and Ruslan Tkhakokhov, software engineer, explored the benefits of transferring users’ data to a new device, using Backup and Restore to achieve that in a simple and secure way.",
      "url": "https://youtu.be/bg2drEhz1_s",
      "headerImageUrl": "https://i.ytimg.com/vi/bg2drEhz1_s/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "13"
      ],
      "authors": []
    },
    {
      "id": "102",
      "episodeId": "50",
      "title": "Compatibility changes in Android 12",
      "content": "Developer relations engineers Kseniia Shumelchyk and Slava Panasenko talked about new Android 12 features and changes. They shared tools and techniques to ensure that apps are compatible with the next Android release and users can take advantage of new features, along with app developer success stories.",
      "url": "https://youtu.be/fCMJmV6nqGo",
      "headerImageUrl": "https://i.ytimg.com/vi/fCMJmV6nqGo/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "13"
      ],
      "authors": []
    },
    {
      "id": "103",
      "episodeId": "50",
      "title": "Building great experiences for Novice Internet Users",
      "content": "Learn the principles to help craft great experiences for the novice Internet user segment from Mrinal Sharma, UX manager, and Amrit Sanjeev, developer relations engineer. They highlight the gap between nascent and tech savvy user segments and suggest strategies in areas to improve the overall user experience. Factors like low functional literacy, being multilingual by default, being less digitally confident, and having no prior internet experience requires that we rethink the way we build apps for these users.",
      "url": "https://youtu.be/Sf_TauUY4LE",
      "headerImageUrl": "https://i.ytimg.com/vi/Sf_TauUY4LE/maxresdefault.jpg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "104",
      "episodeId": "49",
      "title": "Android Basics in Kotlin course 🧑‍💻",
      "content": "Android Basics in Kotlin teaches people with no programming experience how to build simple Android apps. Since the first learning units were released in 2020, over 100,000 beginners have completed it! Today, we’re excited to share that the final unit has been released, and the full Android Basics in Kotlin course is now available.",
      "url": "https://android-developers.googleblog.com/2021/10/announcing-android-basics-in-kotlin.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-BmlW7k8RhME/YWRvsOes9aI/AAAAAAAAQ_g/FpFS6_new9Y7vdzP7P4RPs_x4WHVi4yxQCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-announcing-android-basics-in-Kotlin-course-16x9.png",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "8"
      ],
      "authors": [
        26
      ]
    },
    {
      "id": "105",
      "episodeId": "49",
      "title": "WorkManager 2.7 adds setExpedited API to help with Foreground Service restrictions",
      "content": "As the most outstanding release this time, WorkManager 2.7 was promoted to stable. This new version introduces a new setExpedited API to help with Foreground Service restrictions in Android 12.",
      "url": "https://developer.android.com/reference/android/app/job/JobInfo.Builder#setExpedited(boolean)",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "API change",
      "topics": [
        "14"
      ],
      "authors": []
    },
    {
      "id": "106",
      "episodeId": "49",
      "title": "Updated Widget docs",
      "content": "Widgets can make a huge impact on your user’s home screen! We updated the App Widgets documentation with the recent changes in the latest OS versions. New pages about how to create a simple widget, an advanced widget, and how to provide flexible widget layouts.",
      "url": "https://developer.android.com/guide/topics/appwidgets",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "1"
      ],
      "authors": []
    },
    {
      "id": "107",
      "episodeId": "49",
      "title": "Extend AGP by creating your own plugins",
      "content": "The Android Gradle Plugin (AGP) contains extension points for plugins to control build inputs and extend its functionality. Starting in version 7.0, AGP has a set of official, stable APIs that you can rely on. We also have a new documentation page that walks you through this and explains how to create your own plugins.",
      "url": "https://developer.android.com/studio/build/extend-agp",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "12",
        "5"
      ],
      "authors": []
    },
    {
      "id": "108",
      "episodeId": "49",
      "title": "Revamped Compose Basics Codelab",
      "content": "If you’re planning to start learning Jetpack Compose, our modern toolkit for building native Android UI, it’s your lucky day! We just revamped the Basics Jetpack Compose codelab to help you learn the core concepts of Compose, and only with this, you’ll see how much it improves building Android UIs.",
      "url": "https://developer.android.com/codelabs/jetpack-compose-basics",
      "headerImageUrl": "https://i.ytimg.com/vi/k3jvNqj4m08/maxresdefault.jpg",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Codelab",
      "topics": [
        "9"
      ],
      "authors": []
    },
    {
      "id": "109",
      "episodeId": "49",
      "title": "Start an activity for a result from a Composable",
      "content": "We expanded the Compose and other libraries page to cover how to start an activity for result, request runtime permissions, and handle the system back button directly from your composables.",
      "url": "https://developer.android.com/jetpack/compose/libraries",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "9"
      ],
      "authors": []
    },
    {
      "id": "110",
      "episodeId": "49",
      "title": "Material components in Compose",
      "content": "We added a new Material Components and layouts page that goes over the different Material components in Compose such as backdrop, app bars, modal drawers, etc.!",
      "url": "https://developer.android.com/jetpack/compose/layouts/material",
      "headerImageUrl": "https://developer.android.com/images/jetpack/compose/layouts/material/material_components.png",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "9",
        "1"
      ],
      "authors": []
    },
    {
      "id": "111",
      "episodeId": "49",
      "title": "How to implement a custom design system",
      "content": "How to implement a custom design system in Compose",
      "url": "https://developer.android.com/jetpack/compose/themes/custom",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "9",
        "1"
      ],
      "authors": []
    },
    {
      "id": "112",
      "episodeId": "49",
      "title": "The anatomy of a theme",
      "content": "Understanding the anatomy of a Compose theme",
      "url": "https://developer.android.com/jetpack/compose/themes/anatomy",
      "headerImageUrl": "",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        "9"
      ],
      "authors": []
    },
    {
      "id": "113",
      "episodeId": "49",
      "title": "Paging 📑  Displaying data and its loading state",
      "content": "In the third episode of the Paging video series, TJ adds a local cache to pull from and refresh only when necessary, making use of Room . The local cache acts as the single source of truth for paging data.",
      "url": "https://www.youtube.com/watch?v=OHH_FPbrjtA",
      "headerImageUrl": "https://i.ytimg.com/vi/OHH_FPbrjtA/maxresdefault.jpg",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "7",
        "1"
      ],
      "authors": [
        41
      ]
    },
    {
      "id": "114",
      "episodeId": "49",
      "title": "Data safety in the Play Console 🔒",
      "content": "Google Play is rolling out the Data safety form in the Google Play Console. With the new Data safety section, developers will now have a transparent way to show users if and how they collect, share, and protect user data, before users install an app.\nRead the blog post to learn more about how to submit your app information in Play Console, how to get prepared, and what your users will see in your app’s store listing starting February.",
      "url": "https://android-developers.googleblog.com/2021/10/launching-data-safety-in-play-console.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-Zde9ioLE3SY/YWh7qiquXKI/AAAAAAAARCU/m6D-qJJe6QowYPcDWUtb3-YzFGn9xIaUwCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-get-ready-to-sumbit-your-data-safety-secton-social.png",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "10",
        "11"
      ],
      "authors": [
        10
      ]
    },
    {
      "id": "115",
      "episodeId": "49",
      "title": "Honor every photo - How cameras capture images",
      "content": "Episode 177: Honor every photon. In this episode, Chet, Roman, and Tor have a chat with Bart Wronski from the Google Research team, discussing the camera pipeline that powers the Pixel phones. How cameras capture images, how the algorithms responsible for Pixel’s beautiful images, HDR+ or Night Sight mode works, and more!",
      "url": "https://adbackstage.libsyn.com/episode-177-honor-every-photon",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "4"
      ],
      "authors": [
        38
      ]
    },
    {
      "id": "116",
      "episodeId": "49",
      "title": "Accessibility series 🌐 - Touch targets",
      "content": "The accessibility series continues on with more information on how to follow basic accessibility principles to make sure that your app can be used by as many users as possible.\nIn general, you should ensure that interactive elements have a width and height of at least 48dp! In the touch targets episode, you’ll learn about a few ways in which you can make this happen.",
      "url": "https://www.youtube.com/watch?v=Dqqbe8IFBA4",
      "headerImageUrl": "https://i.ytimg.com/vi/Dqqbe8IFBA4/maxresdefault.jpg",
      "publishDate": "2021-10-16T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": [
        42
      ]
    },
    {
      "id": "117",
      "episodeId": "49",
      "title": "Using the CameraX Exposure Compensation API",
      "content": "This blog post by Wenhung Teng talks about how to use the CameraX Exposure Compensation that makes it much simpler to quickly take images with exceptional quality.",
      "url": "https://medium.com/androiddevelopers/using-camerax-exposure-compensation-api-11fd75785bf",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*zinEvf1keSZYuZojr31ehQ.png",
      "publishDate": "2021-10-12T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "4"
      ],
      "authors": [
        49
      ]
    },
    {
      "id": "118",
      "episodeId": "49",
      "title": "Compose for Wear OS in Developer preview ⌚",
      "content": "We’re bringing the best of Compose to Wear OS as well, with built-in support for Material You to help you create beautiful apps with less code. Read the following article to review the main composables for Wear OS we’ve built and point you towards resources to get started using them.",
      "url": "https://android-developers.googleblog.com/2021/10/compose-for-wear-os-now-in-developer.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-RkL3Yokn3XE/YWWmbuX8E7I/AAAAAAAAQ_o/CEmNJ5_mfq0kScxkFGoMpf1BlU5-uBHjACLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-compose-for-wear-os-now-in-dev-review-header-dark.png",
      "publishDate": "2021-10-11T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "19",
        "9"
      ],
      "authors": [
        46
      ]
    },
    {
      "id": "119",
      "episodeId": "49",
      "title": "Paging 📑  How to fetch data and bind the PagingData to the UI",
      "content": "The series on Paging continues on with more content! In the second episode, TJ shows how to fetch data and bind the PagingData to the UI, including headers and footers.",
      "url": "https://www.youtube.com/watch?v=C0H54K63Lww",
      "headerImageUrl": "https://i.ytimg.com/vi/C0H54K63Lww/maxresdefault.jpg",
      "publishDate": "2021-10-10T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": [
        41
      ]
    },
    {
      "id": "120",
      "episodeId": "49",
      "title": "Room adds support for Kotlin Symbol Processing",
      "content": "Yigit Boyar wrote the story about how Room added support for Kotlin Symbol Processing (KSP). Spoiler: it wasn’t easy, but it was definitely worth it.",
      "url": "https://medium.com/androiddevelopers/room-kotlin-symbol-processing-24808528a28e",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*yM7Lf4dC_hwse6YmoCO4uQ.png",
      "publishDate": "2021-10-09T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        35
      ]
    },
    {
      "id": "121",
      "episodeId": "49",
      "title": "Apply special effects to images with the CameraX Extensions API",
      "content": "Have you ever wanted to apply special effects such as HDR or Night mode when taking pictures from your app? CameraX is here to help you! In this article by Charcoal Chen, learn how to do that using the new ExtensionsManager available in the camera-extensions Jetpack library. ",
      "url": "https://medium.com/androiddevelopers/apply-special-effects-to-images-with-the-camerax-extensions-api-d1a169b803d3",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*GZmhCFMCrG4L_mOtwSb0zA.png",
      "publishDate": "2021-10-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "4"
      ],
      "authors": [
        50
      ]
    },
    {
      "id": "122",
      "episodeId": "49",
      "title": "Wear OS Jetpack libraries now in stable",
      "content": "The Wear OS Jetpack libraries are now in stable.",
      "url": "https://android-developers.googleblog.com/2021/09/wear-os-jetpack-libraries-now-in-stable.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-9zeEGNCG_As/YUD1UO_3kkI/AAAAAAAAQ8k/tCFBpTCwU4MEQHQNB9XzTOXSf6hd9TkQQCLcBGAsYHQ/w1200-h630-p-k-no-nu/image1.png",
      "publishDate": "2021-09-14T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6",
        "19"
      ],
      "authors": [
        46
      ]
    },
    {
      "id": "123",
      "episodeId": "48",
      "title": "Android Dev Summit returns on October 27-28, 2021! 📆",
      "content": "Join us October 27–28 for Android Dev Summit 2021! The show kicks off at 10 AM PST on October 27 with The Android Show: a technical keynote where you’ll hear all the latest developer news and updates. From there, we have over 30 sessions on a range of technical Android development topics, and we’ll be answering your #AskAndroid questions live.",
      "url": "https://developer.android.com/dev-summit",
      "headerImageUrl": "https://developer.android.com/dev-summit/images/android-dev-summit-2021.png",
      "publishDate": "2021-10-05T23:00:00.000Z",
      "type": "Event 📆",
      "topics": [
        "0"
      ],
      "authors": []
    },
    {
      "id": "124",
      "episodeId": "48",
      "title": "Android 12 is live in AOSP! 🤖",
      "content": "We released Android 12 and pushed it to the Android Open Source Project (AOSP). It will be coming to devices later on this year. Thank you for your feedback during the beta.\nAndroid 12 introduces a new design language called Material You along with redesigned widgets, notification UI updates, stretch overscroll, and app launch splash screens. We reduced the CPU time used by core system services, added performance class device capabilities, made ML accelerator drivers updatable outside of platform releases, and prevented apps from launching foreground services from the background and using notification trampolines to improve performance. The new Privacy Dashboard, approximate location, microphone and camera indicators/toggles, and nearby device permissions give users more insight into and control over privacy. We improved the user experience with a unified API for rich content insertion, compatible media transcoding, easier blurs and effects, AVIF image support, enhanced haptics, new camera effects/capabilities, improved native crash debugging, support for rounded screen corners, Play as you download, and Game Mode APIs.",
      "url": "https://android-developers.googleblog.com/2021/10/android-12-is-live-in-aosp.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-7dVmEfR3mJs/YVst2TdY16I/AAAAAAAAK3I/pLnt0r5S-pIaJwcSNsNBqT8w2Y4Ej0yaQCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android%2B12.jpeg",
      "publishDate": "2021-10-03T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "13"
      ],
      "authors": [
        14
      ]
    },
    {
      "id": "125",
      "episodeId": "48",
      "title": "Improved Google Play Console user management 🧑‍💼",
      "content": "The user and permission tools in Play Console have a new, decluttered interface and new team management features, making it easier to make sure every team member has the right set of permissions to fulfill their responsibilities without overexposing unrelated business data.\nWe’ve rewritten permission names and descriptions, clarified differentiation between account and app-level permissions, added new search, filtering, and batch-editing capabilities, and added the ability to export this information to a CSV file. In addition, Play Console users can request access to actions with a justification, and we’ve introduced permission groups to make it easier to assign multiple permissions at once to users that share the same or similar roles.",
      "url": "https://android-developers.googleblog.com/2021/09/improved-google-play-console-user.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-vw3eaKdwzVU/YUjvyJ6zy2I/AAAAAAAAQ9s/m39byf56P8Icog5e5TgCbu9et0VCZh1iACLcBGAsYHQ/w1200-h630-p-k-no-nu/PlayConsole-revamped-user-management-01.png",
      "publishDate": "2021-09-20T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "11"
      ],
      "authors": [
        51
      ]
    },
    {
      "id": "126",
      "episodeId": "48",
      "title": "Making Permissions auto-reset available to billions more devices 🔐",
      "content": "Android 11 introduced permission auto-reset, automatically resetting an app’s runtime permissions when it isn’t used for a few months. In December 2021, we are starting to roll this feature out to devices with Google Play services running Android 6.0 (API level 23) or higher for apps targeting Android 11 (API level 30) or higher. Users can manually enable permission auto-reset for apps targeting API levels 23 to 29.\nSome apps and permissions are automatically exempted from revocation, like active Device Administrator apps used by enterprises, and permissions fixed by enterprise policy. If your app is expected to work primarily in the background without user interaction, you can ask the user to prevent the system from resetting your app’s permissions.",
      "url": "https://android-developers.googleblog.com/2021/09/making-permissions-auto-reset-available.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-W3UAh-gyf3Y/YUJehjKWQjI/AAAAAAAAQ84/zkURLgqMRa4VZK3Is3ENNYG_OjXJxx2pgCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-making-permissions-auto-reset-social-v2.png",
      "publishDate": "2021-09-16T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        "10"
      ],
      "authors": [
        52
      ]
    },
    {
      "id": "127",
      "episodeId": "47",
      "title": "Migrating from Dagger to Hilt",
      "content": "While you will eventually want to migrate all your existing Dagger modules over to Hilt’s built in components, you can start by migrating application-wide components to Hilt’s singleton component. This episode explains how.",
      "url": "https://www.youtube.com/watch?v=Xt1_3Nq4lD0&t=15s",
      "headerImageUrl": "https://i.ytimg.com/vi/Xt1_3Nq4lD0/hqdefault.jpg",
      "publishDate": "2021-09-19T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "14"
      ],
      "authors": [
        53
      ]
    },
    {
      "id": "128",
      "episodeId": "47",
      "title": "ADB Podcast Episode 175: Creating delightful user experiences with Lottie animations",
      "content": "In this episode, Chet, Romain and Tor have a chat with Gabriel Peal from Tonal, well known for his contributions to the Android community on projects such as Mavericks and Lottie. They talked about Lottie and how it helps designers and developers deliver more delightful user experiences by taking complex animations designed in specialized authoring tools such as After Effects, and rendering them efficiently on mobile devices. They also explored the challenges of designing and implementing a rendering engine such as Lottie.",
      "url": "http://adbackstage.libsyn.com/episode-175-lottie",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-09-13T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "1"
      ],
      "authors": [
        38
      ]
    },
    {
      "id": "129",
      "episodeId": "47",
      "title": "Hilt extensions",
      "content": "This episode explains how to write your own Hilt Extensions. Hilt Extensions allow you to extend Hilt support to new libraries. Extensions can be created for common patterns in projects, to support non-standard member injection, mirroring bindings, and more.",
      "url": "https://medium.com/androiddevelopers/hilt-extensions-in-the-mad-skills-series-f2ed6fcba5fe",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*a_ZJwMHs17SmEFr3uEbxDg.png",
      "publishDate": "2021-09-12T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "14"
      ],
      "authors": [
        54
      ]
    },
    {
      "id": "130",
      "episodeId": "47",
      "title": "Labeling images for Accessibility",
      "content": "This Accessibilities series episode covers labeling images for accessibility, such as content descriptions for ImageViews and ImageButtons.",
      "url": "https://youtu.be/O2DeSITnzFk",
      "headerImageUrl": "https://i.ytimg.com/vi/O2DeSITnzFk/maxresdefault.jpg",
      "publishDate": "2021-09-09T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": [
        29
      ]
    },
    {
      "id": "131",
      "episodeId": "47",
      "title": "ADB Podcast Episode 174: Compose in Android Studio",
      "content": "In this episode, Tor and Nick are joined by Chris Sinco, Diego Perez and Nicolas Roard to discuss the features added to Android Studio for Jetpack Compose. Tune in as they discuss the Compose preview, interactive preview, animation inspector, and additions to the Layout inspector along with their approach to creating tooling to support Compose’s code-centric system.",
      "url": "http://adbackstage.libsyn.com/episode-174-compose-tooling",
      "headerImageUrl": "http://assets.libsyn.com/content/110962067?height=250&width=250&overlay=true",
      "publishDate": "2021-09-08T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "5",
        "9"
      ],
      "authors": [
        33
      ]
    },
    {
      "id": "132",
      "episodeId": "47",
      "title": "Hilt under the hood",
      "content": "This episode dives into how the Hilt annotation processors generate code, and how the Hilt Gradle plugin works behind the scenes to improve the overall experience when using Hilt with Gradle.",
      "url": "https://medium.com/androiddevelopers/mad-skills-series-hilt-under-the-hood-9d89ee227059",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*a_ZJwMHs17SmEFr3uEbxDg.png",
      "publishDate": "2021-09-07T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "14"
      ],
      "authors": [
        55
      ]
    },
    {
      "id": "133",
      "episodeId": "47",
      "title": "Trackr comes to the Big Screen",
      "content": "A blog post on Trackr, a sample task management app where we showcase Modern Android Development best practices. This post takes you through how applying Material Design and responsive patterns produced a more refined and intuitive user experience on large screen devices.",
      "url": "https://medium.com/androiddevelopers/trackr-comes-to-the-big-screen-9f13c6f927bf",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*678DlYtu4G7wFrq30FQ7Mw.png",
      "publishDate": "2021-09-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1"
      ],
      "authors": [
        56
      ]
    },
    {
      "id": "134",
      "episodeId": "47",
      "title": "Accessibility services and the Android Accessibility model",
      "content": "This Accessibilities series episode covers accessibility services like TalkBack, Switch Access and Voice Access and how they help users interact with your apps. Android’s accessibility framework allows you to write one app and the framework takes care of providing the information needed by different accessibility services.",
      "url": "https://youtu.be/LxKat_m7mHk",
      "headerImageUrl": "https://i.ytimg.com/vi/LxKat_m7mHk/maxresdefault.jpg",
      "publishDate": "2021-09-02T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "15"
      ],
      "authors": [
        42
      ]
    },
    {
      "id": "135",
      "episodeId": "47",
      "title": "New Accessibility Pathway",
      "content": "Want even more accessibility? You are in luck, check out this entire new learning pathway aimed at teaching you how to make your app more accessible.",
      "url": "https://developer.android.com/courses/pathways/make-your-android-app-accessible",
      "headerImageUrl": "https://developers.google.com/profile/badges/playlists/make-your-android-app-accessible/badge.svg",
      "publishDate": "2021-08-31T23:00:00.000Z",
      "type": "",
      "topics": [
        "15"
      ],
      "authors": []
    },
    {
      "id": "136",
      "episodeId": "47",
      "title": "AndroidX Activity Library 1.4.0-alpha01 released",
      "content": "The AndroidX ComponentActivity now implements the MenuHost interface which allows any component to add menu items to the ActionBar by adding a MenuProvider instance to the activity.",
      "url": "https://developer.android.com/jetpack/androidx/releases/activity#1.4.0-alpha01",
      "headerImageUrl": "",
      "publishDate": "2021-08-31T23:00:00.000Z",
      "type": "API change",
      "topics": [
        "6"
      ],
      "authors": []
    },
    {
      "id": "137",
      "episodeId": "45",
      "title": "DataStore released into stable",
      "content": "Datastore was released, providing a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.",
      "url": "https://developer.android.com/jetpack/androidx/releases/datastore#1.0.0",
      "headerImageUrl": "",
      "publishDate": "2021-08-03T23:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "7"
      ],
      "authors": []
    },
    {
      "id": "138",
      "episodeId": "44",
      "title": "Jetpack Compose 1.0 stable is released",
      "content": "Jetpack Compose, Android’s modern, native UI toolkit is now stable and ready for you to adopt in production. It interoperates with your existing app, integrates with existing Jetpack libraries, implements Material Design with straightforward theming, supports lists with Lazy components using minimal boilerplate, and has a powerful, extensible animation system.",
      "url": "https://android-developers.googleblog.com/2021/07/jetpack-compose-announcement.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-9MiK78CFMLM/YQFurOq9AII/AAAAAAAAQ1A/lKj5GiDnO_MkPLb72XqgnvD5uxOsHO-eACLcBGAsYHQ/w1200-h630-p-k-no-nu/Android-Compose-1.0-header-v2.png",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "9"
      ],
      "authors": [
        57
      ]
    },
    {
      "id": "139",
      "episodeId": "44",
      "title": "Android Studio Artic Fox stable is released",
      "content": "Android Studio Arctic Fox is now available in the stable release channel. Arctic Fox brings Jetpack Compose to life with Compose Preview, Deploy Preview, Compose support in the Layout Inspector, and Live Editing of literals. Compose Preview works with the @Preview annotation to let you instantly see the impact of changes across multiple themes, screen sizes, font sizes, and more. Deploy Preview deploys snippets of your Compose code to a device or emulator for quick testing. Layout inspector now works with apps written fully in Compose as well as apps that have Compose alongside Views, allowing you to explore your layouts and troubleshoot. With Live Edit of literals, you can edit literals such as strings, numbers, booleans, etc. and see the immediate results change in previews, the emulator, or on a physical device — all without having to compile.\n",
      "url": "https://android-developers.googleblog.com/2021/07/android-studio-arctic-fox-202031-stable.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-cmcRT5BGOTY/YQBKC6asA0I/AAAAAAAAQzg/hZrde9Sgx881Wdf-c__VMkTvsKoVjOwsACLcBGAsYHQ/w1200-h630-p-k-no-nu/Arctic_Fox_Splash_2x%2B%25281%2529.png",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "5",
        "9"
      ],
      "authors": [
        58
      ]
    },
    {
      "id": "140",
      "episodeId": "44",
      "title": "User control, privacy, security, and safety",
      "content": "Play announced new updates to bolster user control, privacy, and security. The post covered advertising ID updates, including zeroing out the advertising ID when users opt out of interest-based advertising or ads personalization, the developer preview of the app set ID, enhanced protection for kids, and policy updates around dormant accounts and users of the AccessibilityService API.",
      "url": "https://android-developers.googleblog.com/2021/07/announcing-policy-updates-to-bolster.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-pWCVY7BR-z8/YQAzb9zCZsI/AAAAAAAAQzY/2-OetxLvjOUYhHlTFJNw5JSm_BVjkI0VwCLcBGAsYHQ/s0/Untitled.png",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "11"
      ],
      "authors": [
        10
      ]
    },
    {
      "id": "141",
      "episodeId": "44",
      "title": "Identify performance bottlenecks using system trace",
      "content": "System trace profiling within Android Studio with a detailed walkthrough of app startup performance.",
      "url": "https://www.youtube.com/watch?v=aUrqx9AnDUg",
      "headerImageUrl": "https://i.ytimg.com/vi/aUrqx9AnDUg/hqdefault.jpg",
      "publishDate": "2021-07-25T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "3",
        "5"
      ],
      "authors": [
        39
      ]
    },
    {
      "id": "142",
      "episodeId": "44",
      "title": "Testing in Compose",
      "content": "ADB released episode #171, part of our continuing series on Jetpack Compose. In this episode, Nick and Romain are joined by Filip Pavlis, Jelle Fresen & Jose Alcérreca to talk about Testing in Compose. They discuss how Compose’s testing APIs were developed hand-in-hand with the UI toolkit, making them more deterministic and opening up new possibilities like manipulating time. They go on to discuss the semantics tree, interop testing, screenshot testing and the possibilities for host-side testing.",
      "url": "https://adbackstage.libsyn.com/episode-171-compose-testing",
      "headerImageUrl": "http://assets.libsyn.com/content/108505820?height=250&width=250&overlay=true",
      "publishDate": "2021-06-29T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "9",
        "2"
      ],
      "authors": [
        59
      ]
    },
    {
      "id": "143",
      "episodeId": "42",
      "title": "DataStore reached release candidate status",
      "content": "DataStore has reached release candidate status meaning the 1.0 stable release is right around the corner!",
      "url": "https://developer.android.com/topic/libraries/architecture/datastore",
      "headerImageUrl": "",
      "publishDate": "2021-06-29T23:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        "7"
      ],
      "authors": []
    },
    {
      "id": "144",
      "episodeId": "42",
      "title": "Scope Storage Myths",
      "content": "Apps will be required to update their targetSdkVersion to API 30 in the second half of the year. That means your app will be required to work with Scoped Storage. In this blog post, Nicole Borrelli busts some Scope storage myths in a Q&A format.",
      "url": "https://medium.com/androiddevelopers/scope-storage-myths-ca6a97d7ff37",
      "headerImageUrl": "",
      "publishDate": "2021-06-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7",
        "10"
      ],
      "authors": [
        60
      ]
    },
    {
      "id": "145",
      "episodeId": "41",
      "title": "Navigation with Multiple back stacks",
      "content": "As part of the rercommended Material pattern for bottom-navigation, the Jetpack Navigation library makes it easy to implement navigation with multiple back-stacks",
      "url": "https://medium.com/androiddevelopers/navigation-multiple-back-stacks-6c67ba41952f",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*v7S7LKg4TlrMRlneeP224Q.jpeg",
      "publishDate": "2021-06-14T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1"
      ],
      "authors": [
        26
      ]
    },
    {
      "id": "146",
      "episodeId": "41",
      "title": "Build sophisticated search features with AppSearch",
      "content": "AppSearch is an on-device search library which provides high performance and feature-rich full-text search functionality. Learn how to use the new Jetpack AppSearch library for doing high-performance on-device full text searches.",
      "url": "https://android-developers.googleblog.com/2021/06/sophisticated-search-with-appsearch-in-jetpack.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-PmN4MS50wvo/YMj-HmY4N2I/AAAAAAAAQoQ/5eCx8CU1HgAlFQnQ55IOb_CCVRhe8eGewCLcBGAsYHQ/w1200-h630-p-k-no-nu/AppSearch.jpg",
      "publishDate": "2021-06-13T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "6",
        "1"
      ],
      "authors": [
        61
      ]
    },
    {
      "id": "147",
      "episodeId": "41",
      "title": "ADB Podcast Episode 167: Jetpack Compose Layout",
      "content": "In this second episode of our mini-series on Jetpack Compose (AD/BC), Nick and Romain are joined by Anastasia Soboleva, George Mount and Mihai Popa to talk about Compose’s layout system. They explain how the Compose layout model works and its benefits, introduce common layout composables, discuss how writing your own layout is far simpler than Views, and how you can even animate layout.",
      "url": "https://adbackstage.libsyn.com/episode-167-jetpack-compose-layout",
      "headerImageUrl": "http://assets.libsyn.com/content/105399023?height=250&width=250&overlay=true",
      "publishDate": "2021-06-13T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "9"
      ],
      "authors": [
        62
      ]
    },
    {
      "id": "148",
      "episodeId": "41",
      "title": "Create an application CoroutineScope using Hilt",
      "content": "Learn how to create an applicatioon-scoped CoroutineScope using Hilt, and how to inject it as a dependency.",
      "url": "https://medium.com/androiddevelopers/create-an-application-coroutinescope-using-hilt-dd444e721528",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*MgDtM-AJmc2m2hg5chkflg.png",
      "publishDate": "2021-06-09T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "14"
      ],
      "authors": [
        40
      ]
    },
    {
      "id": "149",
      "episodeId": "41",
      "title": "Android 12 Beta 2 Update",
      "content": "The second Beta of Android 12 has just been released for you to try. Beta 2 adds new privacy features like the Privacy Dashboard and continues our work of refining the release.",
      "url": "https://android-developers.googleblog.com/2021/06/android-12-beta-2-update.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-tLt-TVPqpjA/YKMRwRPMfjI/AAAAAAAAQik/JNtMesFZ2i87RyBACHAVEC14CvcU7G__wCLcBGAsYHQ/w1200-h630-p-k-no-nu/Screen%2BShot%2B2021-05-17%2Bat%2B9.00.30%2BPM.png",
      "publishDate": "2021-06-08T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "13"
      ],
      "authors": [
        14
      ]
    },
    {
      "id": "150",
      "episodeId": "41",
      "title": "Top 3 things in Android 12  | Android @ Google I/O '21",
      "content": "Did you miss the latest in Android 12 at Google I/O 2021? Android Software Engineer Chet Haase will recap the top three themes in Android 12 from this year’s Google I/O!",
      "url": "https://www.youtube.com/watch?v=tvf1wmD5H0M",
      "headerImageUrl": "https://i.ytimg.com/vi/tvf1wmD5H0M/maxresdefault.jpg",
      "publishDate": "2021-06-08T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "13"
      ],
      "authors": [
        38
      ]
    },
    {
      "id": "151",
      "episodeId": "41",
      "title": "ADB Podcast Episode 166: Security Deposit",
      "content": "In this episode, Chad and Jeff from the Android Security team join Tor and Romain to talk about… security. They explain what the platform does to help preserve user trust and device integrity, why it sometimes means restricting existing APIs, and touch on what apps can do or should worry about.",
      "url": "http://adbackstage.libsyn.com/episode-166-security-deposit",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-06-07T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "10"
      ],
      "authors": [
        33
      ]
    },
    {
      "id": "152",
      "episodeId": "41",
      "title": "Multiple Back Stacks",
      "content": "A deep dive into multiple back stacks and some of the work it took to make this feature happen in Fragments and Navigation",
      "url": "https://medium.com/androiddevelopers/multiple-back-stacks-b714d974f134",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*5-lbc-YBJlZnxVFPvNMPAQ.png",
      "publishDate": "2021-06-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1"
      ],
      "authors": [
        63
      ]
    },
    {
      "id": "153",
      "episodeId": "41",
      "title": "Building across devices | Android @ Google I/O '21",
      "content": "Did you miss the latest in Building across screens at Google I/O 2021? Product Manager Diana Wong will recap the top three announcements from this year’s Google I/O!",
      "url": "https://www.youtube.com/watch?v=O5oRiIUk_F4",
      "headerImageUrl": "https://i.ytimg.com/vi/O5oRiIUk_F4/maxresdefault.jpg",
      "publishDate": "2021-06-02T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        "1"
      ],
      "authors": [
        64
      ]
    },
    {
      "id": "154",
      "episodeId": "41",
      "title": "Navigation in Feature Modules",
      "content": "Feature modules delivered with Play Feature delivery at not downloadedd at install time, but only when the app requestss them. Learn how to use the dynamic features navigation library to include the graph from the feature module.",
      "url": "https://medium.com/androiddevelopers/navigation-in-feature-modules-322ac3d79334",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*v7S7LKg4TlrMRlneeP224Q.jpeg",
      "publishDate": "2021-06-01T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "1"
      ],
      "authors": [
        26
      ]
    },
    {
      "id": "155",
      "episodeId": "41",
      "title": "ADB Podcast Episode 165: Material Witnesses",
      "content": "In this episode, Chet and Romain chattedd with Hunter and Nick from the Material Design team about recent additions and improvements to the Material Design Component libraries: transitions, motion theming, Compose, large screens support and guidance, etc.",
      "url": "http://adbackstage.libsyn.com/episode-165-material-witnesses",
      "headerImageUrl": "http://assets.libsyn.com/show/332855?height=250&width=250&overlay=true",
      "publishDate": "2021-06-01T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        "1"
      ],
      "authors": [
        38
      ]
    },
    {
      "id": "156",
      "episodeId": "41",
      "title": "Grow Your Indie Game with Help From Google Play",
      "content": "Google Play is opening submissions for two of our annual developer programs - the Indie Games Accelerator and the Indie Games Festival. These programs are designed to help small games studios grow on Google Play, no matter what stage they are in",
      "url": "https://developers.googleblog.com/2021/06/grow-your-indie-game-with-help-from-google-play.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-MNEblg7_8fA/YK7lludSxJI/AAAAAAAAKQM/_YIT15giTk42oPXWIhK6l2FBVt5PCFKTwCLcBGAsYHQ/w1200-h630-p-k-no-nu/Joint_Announcement_Android%2BDevelopers%2BBlog_Header_1200x600%2B%25282%2529.png",
      "publishDate": "2021-05-31T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "11",
        "18"
      ],
      "authors": [
        65
      ]
    },
    {
      "id": "157",
      "episodeId": "41",
      "title": "Untrusted Touch Events in Android",
      "content": "Android 12 prevents touch events from being deliverred if these touches first pass through a window from a different app to ensure users can see what they are interacting with. Learn about alternatives, to see if your app will be affected and how you can test to see if your app will be impacted.",
      "url": "https://medium.com/androiddevelopers/untrusted-touch-events-2c0e0b9c374c",
      "headerImageUrl": "https://miro.medium.com/max/1400/1*lvwe7v_bcNsNXI_7ltFkJA.jpeg",
      "publishDate": "2021-05-25T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "13"
      ],
      "authors": [
        15
      ]
    },
    {
      "id": "158",
      "episodeId": "41",
      "title": "Android @ Google I/O: 3 things to know in Modern Android Development",
      "content": "This year’s Google I/O brought lots of updates for Modern Android Development. Learn about the top 3 things you should know.",
      "url": "https://android-developers.googleblog.com/2021/05/mad-spotlight.html",
      "headerImageUrl": "https://1.bp.blogspot.com/-8cqMFObMeko/YK5RbJ7Yr_I/AAAAAAAAQkw/Iw4_hRZwa7QD1CmVGnZUZ4NjYowXZadTgCLcBGAsYHQ/w1200-h630-p-k-no-nu/Android_PostIO_blog-MAD.png",
      "publishDate": "2021-05-24T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "0"
      ],
      "authors": [
        66
      ]
    }
  ]
    """.trimIndent()

    @Language("JSON")
    val authors = """
  [
    {
      "id": "1",
      "name": "Márton Braun",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "2",
      "name": "Greg Hartrell",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "3",
      "name": "Simona Stojanovic",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "4",
      "name": "Andrew Flynn",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "5",
      "name": "Jon Boekenoogen",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "6",
      "name": "Florina Muntenescu",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "7",
      "name": "Lidia Gaymond",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "8",
      "name": "Vicki Amin",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "9",
      "name": "Marcel Pintó",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "10",
      "name": "Krish Vitaldevara",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "11",
      "name": "Gerry Fan",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "12",
      "name": "Pietro Maggi",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "13",
      "name": "Rohan Shah",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "14",
      "name": "Dave Burke",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "15",
      "name": "Meghan Mehta",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "16",
      "name": "Anna Bernbaum",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "17",
      "name": "Adarsh Fernando",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "18",
      "name": "Madan Ankapura",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "19",
      "name": "Kateryna Semenova",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "20",
      "name": "Rahul Ravikumar",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "21",
      "name": "Chris Craik",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "22",
      "name": "Marcel Pintó Biescas",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "23",
      "name": "Alex Vanyo",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "24",
      "name": "Manuel Vicente Vivo",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "25",
      "name": "Arjun Dayal",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "26",
      "name": "Murat Yener",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "27",
      "name": "Alex Saveau",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "28",
      "name": "Paul Lammertsma",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "29",
      "name": "Caren Chang",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "30",
      "name": "Mayuri Khinvasara Khabya",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "31",
      "name": "Romain Guy",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "32",
      "name": "Chet Hasse",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "33",
      "name": "Tor Norbye",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "34",
      "name": "Nicole Laure",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "35",
      "name": "Yigit Boyar",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "36",
      "name": "Sean McQuillan",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "37",
      "name": "Ben Weiss",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "38",
      "name": "Chet Haase",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "39",
      "name": "Carmen Jackson",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "40",
      "name": "Manuel Vivo",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "41",
      "name": "TJ Dahunsi",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "42",
      "name": "Shailen Tuli",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "43",
      "name": "Murat",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "44",
      "name": "Kailiang Chen",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "45",
      "name": "Meghan",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "46",
      "name": "Jeremy Walker",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "47",
      "name": "Don Turner",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "48",
      "name": "Lilian Young",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "49",
      "name": "Wenhung Teng",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "50",
      "name": "Charcoal Chen",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "51",
      "name": "Mike Yerou",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "52",
      "name": "Peter Visontay",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "53",
      "name": "Marcelo Hernandez",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "54",
      "name": "Daniel Santiago",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "55",
      "name": "Brad Corso",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "56",
      "name": "Jonathan Koren",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "57",
      "name": "Anna-Chiara Bellini",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "58",
      "name": "Amanda Alexander",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "59",
      "name": "Android Developers Backstage",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "60",
      "name": "Nicole Borrelli",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "61",
      "name": "Dan Saadati",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "62",
      "name": "Nick Butcher",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "63",
      "name": "Ian Lake",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "64",
      "name": "Diana Wong",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "65",
      "name": "Patricia Correa",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    },
    {
      "id": "66",
      "name": "The Modern Android Development Team",
      "mediumPage": "",
      "twitter": "",
      "imageUrl": ""
    }
  ]
    """.trimIndent()
}
