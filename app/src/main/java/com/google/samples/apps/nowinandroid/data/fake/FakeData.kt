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

package com.google.samples.apps.nowinandroid.data.fake

import com.google.samples.apps.nowinandroid.data.network.NetworkNewsResource
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.intellij.lang.annotations.Language

object FakeDataSource {
    val sampleResource = NetworkNewsResource(
        id = 1,
        episodeId = 52,
        title = "Thanks for helping us reach 1M YouTube Subscribers",
        content = "Thank you everyone for following the Now in Android series and everything the Android Developers YouTube channel has to offer. During the Android Developer Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to thank you all.",
        url = "https://youtu.be/-fJ6poHQrjM",
        authors = emptyList(),
        publishDate = LocalDateTime(
            year = 2021,
            monthNumber = 11,
            dayOfMonth = 9,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        ).toInstant(TimeZone.UTC),
        type = "Video \uD83D\uDCFA",
        topics = listOf(0),
    )

    @Language("JSON")
    val topicsData = """
[
  {
    "id": 0,
    "name": "Headlines",
    "description": ""
  },
  {
    "id": 1,
    "name": "UI",
    "description": ""
  },
  {
    "id": 2,
    "name": "Testing",
    "description": ""
  },
  {
    "id": 3,
    "name": "Performance",
    "description": ""
  },
  {
    "id": 4,
    "name": "Camera & Media",
    "description": ""
  },
  {
    "id": 5,
    "name": "Android Studio",
    "description": ""
  },
  {
    "id": 6,
    "name": "New APIs & Libraries",
    "description": ""
  },
  {
    "id": 7,
    "name": "Data Storage",
    "description": ""
  },
  {
    "id": 8,
    "name": "Kotlin",
    "description": ""
  },
  {
    "id": 9,
    "name": "Compose",
    "description": ""
  },
  {
    "id": 10,
    "name": "Privacy & Security",
    "description": ""
  },
  {
    "id": 11,
    "name": "Publishing & Distribution",
    "description": ""
  },
  {
    "id": 12,
    "name": "Tools",
    "description": ""
  },
  {
    "id": 13,
    "name": "Platform & Releases",
    "description": ""
  },
  {
    "id": 14,
    "name": "Architecture",
    "description": ""
  },
  {
    "id": 15,
    "name": "Accessibility",
    "description": ""
  },
  {
    "id": 16,
    "name": "Android Auto",
    "description": ""
  },
  {
    "id": 17,
    "name": "Games",
    "description": ""
  },
  {
    "id": 18,
    "name": "Wear OS",
    "description": ""
  }
]
""".trimIndent()

    @Language("JSON")
    val data = """
{
  "resources": [
    {
      "id": 1,
      "episodeId": 52,
      "title": "Thanks for helping us reach 1M YouTube Subscribers",
      "content": "Thank you everyone for following the Now in Android series and everything the Android Developers YouTube channel has to offer. During the Android Developer Summit, our YouTube channel reached 1 million subscribers! Here’s a small video to thank you all.",
      "url": "https://youtu.be/-fJ6poHQrjM",
      "publishDate": "2021-11-09T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        0
      ],
      "authors": []
    },
    {
      "id": 2,
      "episodeId": 52,
      "title": "Transformations and customisations in the Paging Library",
      "content": "A demonstration of different operations that can be performed with Paging. Transformations like inserting separators, when to create a new pager, and customisation options for consuming PagingData.",
      "url": "https://youtu.be/ZARz0pjm5YM",
      "publishDate": "2021-11-01T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": [
        0
      ]
    },
    {
      "id": 3,
      "episodeId": 52,
      "title": "Community tip on Paging",
      "content": "Tips for using the Paging library from the developer community",
      "url": "https://youtu.be/r5JgIyS3t3s",
      "publishDate": "2021-11-08T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": []
    },
    {
      "id": 4,
      "episodeId": 52,
      "title": "Paging Q&A",
      "content": "In this live session, TJ and Dustin answered your questions in the usual live Q&A format.",
      "url": "https://youtu.be/8i6vrlbIVCc",
      "publishDate": "2021-11-11T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": []
    },
    {
      "id": 5,
      "episodeId": 52,
      "title": "Gradle series kicks off",
      "content": "Murat introduces the Gradle series and everything you'll learn in it.",
      "url": "https://youtu.be/mk0XBWenod8",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        12
      ],
      "authors": [
        1
      ]
    },
    {
      "id": 6,
      "episodeId": 52,
      "title": "Intro to Gradle and AGP",
      "content": "In the first episode of the Gradle MAD Skills series, Murat explains how the Android build system works, and how to configure your build.",
      "url": "https://youtu.be/GjPS4xDMmQY",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        12
      ],
      "authors": [
        1
      ]
    },
    {
      "id": 7,
      "episodeId": 52,
      "title": "How to write a Gradle plugin",
      "content": "In this second episode of the Gradle MAD Skills series, Murat explains how to write your own custom Gradle plugin.",
      "url": "https://youtu.be/LPzBVtwGxlo",
      "publishDate": "2021-11-22T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        12
      ],
      "authors": [
        1
      ]
    },
    {
      "id": 8,
      "episodeId": 52,
      "title": "Take your Gradle plugin to the next step",
      "content": "This third and last episode of the Gradle MAD Skills series teaches you how to get access to various build artifacts using the new Artifact API.",
      "url": "https://youtu.be/SB4QlngQQW0",
      "publishDate": "2021-11-29T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        12
      ],
      "authors": [
        1
      ]
    },
    {
      "id": 9,
      "episodeId": 52,
      "title": "AppCompat, Activity, and Fragment to support multiple back stacks",
      "content": "The 1.4.0 release of these libraries brings stable support for multiple back stacks.",
      "url": "https://developer.android.com/jetpack/androidx/releases/appcompat#1.4.0",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "API change",
      "topics": [
        6,
        1
      ],
      "authors": []
    },
    {
      "id": 10,
      "episodeId": 52,
      "title": "Emoji2 adds support for modern emojis",
      "content": "The 1.0 stable release of Emoji2 allows you to use modern emojis in your app.",
      "url": "https://developer.android.com/jetpack/androidx/releases/emoji2#1.0.0",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "API change",
      "topics": [
        6,
        1
      ],
      "authors": []
    },
    {
      "id": 11,
      "episodeId": 52,
      "title": "Lifecycle introduces lifecycle-aware coroutine APIs",
      "content": "The new 2.4 release of Lifecycle introduces repeatOnLifecycle and flowWithLifecycle.",
      "url": "https://developer.android.com/jetpack/androidx/releases/lifecycle#2.4.0",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "API change",
      "topics": [
        6,
        14
      ],
      "authors": []
    },
    {
      "id": 12,
      "episodeId": 52,
      "title": "Paging release brings changes to LoadState",
      "content": "The new 3.1 release of Paging changes the behavior of LoadState.",
      "url": "https://developer.android.com/jetpack/androidx/releases/paging#3.1.0",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "API change",
      "topics": [
        6,
        1
      ],
      "authors": []
    },
    {
      "id": 13,
      "episodeId": 52,
      "title": "Wear tiles released as 1.0 stable",
      "content": "The library that you use to build custom tiles for Wear OS devices is now stable.",
      "url": "https://developer.android.com/jetpack/androidx/releases/wear-tiles#1.0.0",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "API change",
      "topics": [
        6,
        18
      ],
      "authors": []
    },
    {
      "id": 14,
      "episodeId": 52,
      "title": "Introducing Jetpack Media3",
      "content": "The first alpha version of this new library is now available. Media3 is a collection of support libraries for media playback, including ExoPlayer. The following article explains why the team created Media3, what it contains, and how it can simplify your app architecture.",
      "url": "https://developer.android.com/jetpack/androidx/releases/media3",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        6,
        4
      ],
      "authors": [
        2
      ]
    },
    {
      "id": 15,
      "episodeId": 52,
      "title": "The problem with emojis and how emoji2 can help out",
      "content": "Meghan wrote about the new emoji2 library that just became stable.",
      "url": "https://medium.com/androiddevelopers/support-modern-emoji-99f6dea8e57f",
      "publishDate": "2021-11-12T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        6,
        1
      ],
      "authors": [
        3
      ]
    },
    {
      "id": 16,
      "episodeId": 52,
      "title": "Convert YUV to RGB for CameraX Image Analysis",
      "content": "Learn about a new feature in CameraX to convert YUV, the format that CameraX produces, to RGB used for image analysis capabilities available in TensorFlow Lite, for example. Read the blog post for more information about these formats and how to use the new conversion feature.",
      "url": "https://medium.com/androiddevelopers/convert-yuv-to-rgb-for-camerax-imageanalysis-6c627f3a0292",
      "publishDate": "2021-11-19T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        6,
        4
      ],
      "authors": [
        4
      ]
    },
    {
      "id": 17,
      "episodeId": 52,
      "title": "Improving App Startup: Lessons from the Facebook App",
      "content": "Improving app startup time is not a trivial task and requires a deep understanding of things that affect it. This year, the Android team and the Facebook app team have been working together on metrics and sharing approaches to improve app startup. Read more about the findings in this blog post.",
      "url": "https://android-developers.googleblog.com/2021/11/improving-app-startup-facebook-app.html",
      "publishDate": "2021-11-16T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        3
      ],
      "authors": []
    },
    {
      "id": 18,
      "episodeId": 52,
      "title": "About Custom Accessibility Actions",
      "content": "The accessibility series continues on with more information on how to create custom accessibility actions to make your apps more accessible. You can provide a custom action to the accessibility services and implement logic related to the action. For more information, check out the following episode!",
      "url": "https://youtu.be/wWDYIGk0Kdo",
      "publishDate": "2021-11-17T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": [
        5
      ]
    },
    {
      "id": 19,
      "episodeId": 52,
      "title": "Conveying state for Accessibility",
      "content": "In this episode of the Accessibility series, you can learn more about the StateDescription API, when to use stateDescription and contentDescription, and how to represent error states to the end user.",
      "url": "https://youtu.be/JvWM2PjLJls",
      "publishDate": "2021-11-30T00:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": [
        5
      ]
    },
    {
      "id": 20,
      "episodeId": 52,
      "title": "New Compose for Wear OS codelab",
      "content": "In this codelab, you can learn how Wear OS can work with Compose, what Wear OS specific composables are available, and more!",
      "url": "https://developer.android.com/codelabs/compose-for-wear-os",
      "publishDate": "2021-10-27T23:00:00.000Z",
      "type": "Codelab",
      "topics": [
        9,
        18
      ],
      "authors": [
        6
      ]
    },
    {
      "id": 21,
      "episodeId": 52,
      "title": "ADB Podcast episode 179 Hosts 3, Guests 0",
      "content": "Chet, Romain and Tor sit down to chat about the Android Developer Summit, and in particular all the new features arriving in Android Studio, along with a few other topics like Chet’s new jank stats library, the Android 12L release, and more.",
      "url": "https://adbackstage.libsyn.com/episode-178-hosts-3-guests-0",
      "publishDate": "2021-11-15T00:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        5,
        3,
        13
      ],
      "authors": [
        7
      ]
    },
    {
      "id": 22,
      "episodeId": 50,
      "title": "Building apps which are private by design",
      "content": "Sara N-Marandi, product manager, and Yacine Rezgui, developer relations engineer, provided guidelines and best practices on how to build apps that are private by design, covered new privacy features in Android 12 and previewed upcoming Android concepts.",
      "url": "https://youtu.be/hBVwr2ErQCw",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        10
      ],
      "authors": []
    },
    {
      "id": 23,
      "episodeId": 50,
      "title": "Memory Safety Tools",
      "content": "Serban Constantinescu, product manager, talked about the Memory Safety Tools that became available starting in Android 11 and have continued to evolve in Android 12. These tools can help address memory bugs and improve the quality and security of your application.",
      "url": "https://youtu.be/JqLcTFpXreg",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        10
      ],
      "authors": []
    },
    {
      "id": 24,
      "episodeId": 50,
      "title": "Increasing User Transparency with Privacy Dashboard",
      "content": "Android is ever evolving in its quest to protect users’ privacy. In Android 12, the platform increases transparency by introducing Privacy Dashboard, which gives users a simple and clear timeline view of the apps that have accessed location, microphone and camera within the past 24 hours. ",
      "url": "https://medium.com/androiddevelopers/increasing-user-transparency-with-privacy-dashboard-23064f2d7ff6",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        10
      ],
      "authors": [
        8
      ]
    },
    {
      "id": 25,
      "episodeId": 50,
      "title": "The most unusual and interesting security issues addressed last year",
      "content": "Lilian Young, software engineer, presented a selection of the most unusual, intricate, and interesting security issues addressed in the last year. Developers and researchers are able to contribute to the security of the Android platform by submitting to the Android Vulnerability Rewards Program.",
      "url": "https://medium.com/androiddevelopers/now-in-android-50-ads-special-9934422f8dd1",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        10
      ],
      "authors": [
        9
      ]
    },
    {
      "id": 26,
      "episodeId": 50,
      "title": "New Data Safety section in the Play Console",
      "content": "The new Data safety section will give you a simple way to showcase your app’s overall safety. It gives you a place to give users deeper insight into your app’s privacy and security practices, and explain the data your app may collect and why — all before users install.",
      "url": "https://youtu.be/J7TM0Yy0aTQ",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        10,
        11
      ],
      "authors": []
    },
    {
      "id": 27,
      "episodeId": 50,
      "title": "Building Android UIs for any screen size",
      "content": "Clara Bayarri, engineering manager and Daniel Jacobson, product manager, talked about the state of the ecosystem, focusing on new design guidance, APIs, and tools to help you make the most of your UI on different screen sizes.",
      "url": "https://youtu.be/ir3LztqbeRI",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": []
    },
    {
      "id": 28,
      "episodeId": 50,
      "title": "What's new for large screens & foldables",
      "content": "Emilie Roberts, Chrome OS developer advocate and Andrii Kulian, Android software engineer, introduced new features focused specifically on making apps look great on large screens, foldables, and Chrome OS. ",
      "url": "https://youtu.be/6-925K3hMHU",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": []
    },
    {
      "id": 29,
      "episodeId": 50,
      "title": "Enable great input support for all devices",
      "content": "Users expect seamless experiences when using keyboards, mice, and stylus. Emilie Roberts taught us how to handle common keyboard and mouse input events and how to get started with more advanced support like keyboard shortcuts, low-latency styluses, MIDI, and more.",
      "url": "https://youtu.be/piLEZYTc_4g",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": []
    },
    {
      "id": 30,
      "episodeId": 50,
      "title": "Best practices for video apps on foldable devices",
      "content": "Francesco Romano, developer advocate, and Will Chan, product manager at Zoom explored new user experiences made possible by the foldable form factor, focusing on video conferencing and media applications. ",
      "url": "https://youtu.be/DBAek_P0nEw",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1,
        4
      ],
      "authors": []
    },
    {
      "id": 31,
      "episodeId": 50,
      "title": "Design beautiful apps on foldables and large screens",
      "content": "Liam Spradlin, design advocate, and Jonathan Koren, developer relations engineer, talked about how to design and test Android applications that look and feel great across device types and screen sizes, from tablets to foldables to Chrome OS.",
      "url": "https://youtu.be/DJeJIJKOUbI",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": []
    },
    {
      "id": 32,
      "episodeId": 50,
      "title": "12L and new Android APIs and tools for large screens",
      "content": "Dave Burke, vice president of engineering, wrote a post covering the developer preview of 12L, an upcoming feature drop that makes Android 12 even better on large screens. ",
      "url": "https://android-developers.googleblog.com/2021/10/12L-preview-large-screens.html",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        1,
        13
      ],
      "authors": []
    },
    {
      "id": 33,
      "episodeId": 50,
      "title": "New features in ML Kit: Text Recognition V2 & Pose Detections",
      "content": "Zongmin Sun, software engineer, and Valentin Bazarevsky, MediaPipe Engineer, talked about Text Recognition V2 & Pose Detection, recently-released features in ML Kit. ",
      "url": "https://youtu.be/9EKQ0UC04S8",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        6,
        1
      ],
      "authors": []
    },
    {
      "id": 34,
      "episodeId": 50,
      "title": "How to retain users with Android backup and restore",
      "content": "In this talk, Martin Millmore, engineering manager, and Ruslan Tkhakokhov, software engineer, explored the benefits of transferring users’ data to a new device, using Backup and Restore to achieve that in a simple and secure way.",
      "url": "https://youtu.be/bg2drEhz1_s",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        13
      ],
      "authors": []
    },
    {
      "id": 35,
      "episodeId": 50,
      "title": "Compatibility changes in Android 12",
      "content": "Developer relations engineers Kseniia Shumelchyk and Slava Panasenko talked about new Android 12 features and changes. They shared tools and techniques to ensure that apps are compatible with the next Android release and users can take advantage of new features, along with app developer success stories.",
      "url": "https://youtu.be/fCMJmV6nqGo",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        13
      ],
      "authors": []
    },
    {
      "id": 36,
      "episodeId": 50,
      "title": "Building great experiences for Novice Internet Users",
      "content": "Learn the principles to help craft great experiences for the novice Internet user segment from Mrinal Sharma, UX manager, and Amrit Sanjeev, developer relations engineer. They highlight the gap between nascent and tech savvy user segments and suggest strategies in areas to improve the overall user experience. Factors like low functional literacy, being multilingual by default, being less digitally confident, and having no prior internet experience requires that we rethink the way we build apps for these users.",
      "url": "https://youtu.be/Sf_TauUY4LE",
      "publishDate": "2021-10-26T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": []
    },
    {
      "id": 37,
      "episodeId": 49,
      "title": "Android Basics in Kotlin course 🧑‍💻",
      "content": "Android Basics in Kotlin teaches people with no programming experience how to build simple Android apps. Since the first learning units were released in 2020, over 100,000 beginners have completed it! Today, we’re excited to share that the final unit has been released, and the full Android Basics in Kotlin course is now available.",
      "url": "https://android-developers.googleblog.com/2021/10/announcing-android-basics-in-kotlin.html",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        8
      ],
      "authors": [
        10
      ]
    },
    {
      "id": 38,
      "episodeId": 49,
      "title": "WorkManager 2.7 adds setExpedited API to help with Foreground Service restrictions",
      "content": "As the most outstanding release this time, WorkManager 2.7 was promoted to stable. This new version introduces a new setExpedited API to help with Foreground Service restrictions in Android 12.",
      "url": "https://developer.android.com/reference/android/app/job/JobInfo.Builder#setExpedited(boolean)",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "API change",
      "topics": [
        14
      ],
      "authors": []
    },
    {
      "id": 39,
      "episodeId": 49,
      "title": "Updated Widget docs",
      "content": "Widgets can make a huge impact on your user’s home screen! We updated the App Widgets documentation with the recent changes in the latest OS versions. New pages about how to create a simple widget, an advanced widget, and how to provide flexible widget layouts.",
      "url": "https://developer.android.com/guide/topics/appwidgets",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        1
      ],
      "authors": []
    },
    {
      "id": 40,
      "episodeId": 49,
      "title": "Extend AGP by creating your own plugins",
      "content": "The Android Gradle Plugin (AGP) contains extension points for plugins to control build inputs and extend its functionality. Starting in version 7.0, AGP has a set of official, stable APIs that you can rely on. We also have a new documentation page that walks you through this and explains how to create your own plugins.",
      "url": "https://developer.android.com/studio/build/extend-agp",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        12,
        5
      ],
      "authors": []
    },
    {
      "id": 41,
      "episodeId": 49,
      "title": "Revamped Compose Basics Codelab",
      "content": "If you’re planning to start learning Jetpack Compose, our modern toolkit for building native Android UI, it’s your lucky day! We just revamped the Basics Jetpack Compose codelab to help you learn the core concepts of Compose, and only with this, you’ll see how much it improves building Android UIs.",
      "url": "https://developer.android.com/codelabs/jetpack-compose-basics",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Codelab",
      "topics": [
        9
      ],
      "authors": []
    },
    {
      "id": 42,
      "episodeId": 49,
      "title": "Start an activity for a result from a Composable",
      "content": "We expanded the Compose and other libraries page to cover how to start an activity for result, request runtime permissions, and handle the system back button directly from your composables.",
      "url": "https://developer.android.com/jetpack/compose/libraries",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        9
      ],
      "authors": []
    },
    {
      "id": 43,
      "episodeId": 49,
      "title": "Material components in Compose",
      "content": "We added a new Material Components and layouts page that goes over the different Material components in Compose such as backdrop, app bars, modal drawers, etc.!",
      "url": "https://developer.android.com/jetpack/compose/layouts/material",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        9,
        1
      ],
      "authors": []
    },
    {
      "id": 44,
      "episodeId": 49,
      "title": "How to implement a custom design system",
      "content": "How to implement a custom design system in Compose",
      "url": "https://developer.android.com/jetpack/compose/themes/custom",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        9,
        1
      ],
      "authors": []
    },
    {
      "id": 45,
      "episodeId": 49,
      "title": "The anatomy of a theme",
      "content": "Understanding the anatomy of a Compose theme",
      "url": "https://developer.android.com/jetpack/compose/themes/anatomy",
      "publishDate": "2021-10-20T23:00:00.000Z",
      "type": "Docs 📑",
      "topics": [
        9
      ],
      "authors": []
    },
    {
      "id": 46,
      "episodeId": 49,
      "title": "Paging 📑  Displaying data and its loading state",
      "content": "In the third episode of the Paging video series, TJ adds a local cache to pull from and refresh only when necessary, making use of Room . The local cache acts as the single source of truth for paging data.",
      "url": "https://www.youtube.com/watch?v=OHH_FPbrjtA",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        7,
        1
      ],
      "authors": [
        0
      ]
    },
    {
      "id": 47,
      "episodeId": 49,
      "title": "Data safety in the Play Console 🔒",
      "content": "Google Play is rolling out the Data safety form in the Google Play Console. With the new Data safety section, developers will now have a transparent way to show users if and how they collect, share, and protect user data, before users install an app.\nRead the blog post to learn more about how to submit your app information in Play Console, how to get prepared, and what your users will see in your app’s store listing starting February.",
      "url": "https://android-developers.googleblog.com/2021/10/launching-data-safety-in-play-console.html",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        10,
        11
      ],
      "authors": [
        11
      ]
    },
    {
      "id": 48,
      "episodeId": 49,
      "title": "Honor every photo - How cameras capture images",
      "content": "Episode 177: Honor every photon. In this episode, Chet, Roman, and Tor have a chat with Bart Wronski from the Google Research team, discussing the camera pipeline that powers the Pixel phones. How cameras capture images, how the algorithms responsible for Pixel’s beautiful images, HDR+ or Night Sight mode works, and more!",
      "url": "https://adbackstage.libsyn.com/episode-177-honor-every-photon",
      "publishDate": "2021-10-17T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        4
      ],
      "authors": [
        7
      ]
    },
    {
      "id": 49,
      "episodeId": 49,
      "title": "Accessibility series 🌐 - Touch targets",
      "content": "The accessibility series continues on with more information on how to follow basic accessibility principles to make sure that your app can be used by as many users as possible.\nIn general, you should ensure that interactive elements have a width and height of at least 48dp! In the touch targets episode, you’ll learn about a few ways in which you can make this happen.",
      "url": "https://www.youtube.com/watch?v=Dqqbe8IFBA4",
      "publishDate": "2021-10-16T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        15
      ],
      "authors": [
        5
      ]
    },
    {
      "id": 50,
      "episodeId": 49,
      "title": "Using the CameraX Exposure Compensation API",
      "content": "This blog post by Wenhung Teng talks about how to use the CameraX Exposure Compensation that makes it much simpler to quickly take images with exceptional quality.",
      "url": "https://medium.com/androiddevelopers/using-camerax-exposure-compensation-api-11fd75785bf",
      "publishDate": "2021-10-12T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        4
      ],
      "authors": [
        12
      ]
    },
    {
      "id": 51,
      "episodeId": 49,
      "title": "Compose for Wear OS in Developer preview ⌚",
      "content": "We’re bringing the best of Compose to Wear OS as well, with built-in support for Material You to help you create beautiful apps with less code. Read the following article to review the main composables for Wear OS we’ve built and point you towards resources to get started using them.",
      "url": "https://android-developers.googleblog.com/2021/10/compose-for-wear-os-now-in-developer.html",
      "publishDate": "2021-10-11T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        18,
        9
      ],
      "authors": [
        6
      ]
    },
    {
      "id": 52,
      "episodeId": 49,
      "title": "Paging 📑  How to fetch data and bind the PagingData to the UI",
      "content": "The series on Paging continues on with more content! In the second episode, TJ shows how to fetch data and bind the PagingData to the UI, including headers and footers.",
      "url": "https://www.youtube.com/watch?v=C0H54K63Lww",
      "publishDate": "2021-10-10T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": [
        0
      ]
    },
    {
      "id": 53,
      "episodeId": 49,
      "title": "Room adds support for Kotlin Symbol Processing",
      "content": "Yigit Boyar wrote the story about how Room added support for Kotlin Symbol Processing (KSP). Spoiler: it wasn’t easy, but it was definitely worth it.",
      "url": "https://medium.com/androiddevelopers/room-kotlin-symbol-processing-24808528a28e",
      "publishDate": "2021-10-09T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        7
      ],
      "authors": [
        13
      ]
    },
    {
      "id": 54,
      "episodeId": 49,
      "title": "Apply special effects to images with the CameraX Extensions API",
      "content": "Have you ever wanted to apply special effects such as HDR or Night mode when taking pictures from your app? CameraX is here to help you! In this article by Charcoal Chen, learn how to do that using the new ExtensionsManager available in the camera-extensions Jetpack library. ",
      "url": "https://medium.com/androiddevelopers/apply-special-effects-to-images-with-the-camerax-extensions-api-d1a169b803d3",
      "publishDate": "2021-10-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        4
      ],
      "authors": [
        14
      ]
    },
    {
      "id": 55,
      "episodeId": 49,
      "title": "Wear OS Jetpack libraries now in stable",
      "content": "The Wear OS Jetpack libraries are now in stable.",
      "url": "https://android-developers.googleblog.com/2021/09/wear-os-jetpack-libraries-now-in-stable.html",
      "publishDate": "2021-09-14T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        6,
        18
      ],
      "authors": [
        6
      ]
    },
    {
      "id": 56,
      "episodeId": 48,
      "title": "Android Dev Summit returns on October 27-28, 2021! 📆",
      "content": "Join us October 27–28 for Android Dev Summit 2021! The show kicks off at 10 AM PST on October 27 with The Android Show: a technical keynote where you’ll hear all the latest developer news and updates. From there, we have over 30 sessions on a range of technical Android development topics, and we’ll be answering your #AskAndroid questions live.",
      "url": "https://developer.android.com/dev-summit",
      "publishDate": "2021-10-05T23:00:00.000Z",
      "type": "Event 📆",
      "topics": [
        0
      ],
      "authors": []
    },
    {
      "id": 57,
      "episodeId": 48,
      "title": "Android 12 is live in AOSP! 🤖",
      "content": "We released Android 12 and pushed it to the Android Open Source Project (AOSP). It will be coming to devices later on this year. Thank you for your feedback during the beta.\nAndroid 12 introduces a new design language called Material You along with redesigned widgets, notification UI updates, stretch overscroll, and app launch splash screens. We reduced the CPU time used by core system services, added performance class device capabilities, made ML accelerator drivers updatable outside of platform releases, and prevented apps from launching foreground services from the background and using notification trampolines to improve performance. The new Privacy Dashboard, approximate location, microphone and camera indicators/toggles, and nearby device permissions give users more insight into and control over privacy. We improved the user experience with a unified API for rich content insertion, compatible media transcoding, easier blurs and effects, AVIF image support, enhanced haptics, new camera effects/capabilities, improved native crash debugging, support for rounded screen corners, Play as you download, and Game Mode APIs.",
      "url": "https://android-developers.googleblog.com/2021/10/android-12-is-live-in-aosp.html",
      "publishDate": "2021-10-03T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        13
      ],
      "authors": [
        15
      ]
    },
    {
      "id": 58,
      "episodeId": 48,
      "title": "Improved Google Play Console user management 🧑‍💼",
      "content": "The user and permission tools in Play Console have a new, decluttered interface and new team management features, making it easier to make sure every team member has the right set of permissions to fulfill their responsibilities without overexposing unrelated business data.\nWe’ve rewritten permission names and descriptions, clarified differentiation between account and app-level permissions, added new search, filtering, and batch-editing capabilities, and added the ability to export this information to a CSV file. In addition, Play Console users can request access to actions with a justification, and we’ve introduced permission groups to make it easier to assign multiple permissions at once to users that share the same or similar roles.",
      "url": "https://android-developers.googleblog.com/2021/09/improved-google-play-console-user.html",
      "publishDate": "2021-09-20T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        11
      ],
      "authors": [
        16
      ]
    },
    {
      "id": 59,
      "episodeId": 48,
      "title": "Making Permissions auto-reset available to billions more devices 🔐",
      "content": "Android 11 introduced permission auto-reset, automatically resetting an app’s runtime permissions when it isn’t used for a few months. In December 2021, we are starting to roll this feature out to devices with Google Play services running Android 6.0 (API level 23) or higher for apps targeting Android 11 (API level 30) or higher. Users can manually enable permission auto-reset for apps targeting API levels 23 to 29.\nSome apps and permissions are automatically exempted from revocation, like active Device Administrator apps used by enterprises, and permissions fixed by enterprise policy. If your app is expected to work primarily in the background without user interaction, you can ask the user to prevent the system from resetting your app’s permissions.",
      "url": "https://android-developers.googleblog.com/2021/09/making-permissions-auto-reset-available.html",
      "publishDate": "2021-09-16T23:00:00.000Z",
      "type": "DAC - Android version features",
      "topics": [
        10
      ],
      "authors": [
        17
      ]
    },
    {
      "id": 60,
      "episodeId": 47,
      "title": "Hilt under the hood",
      "content": "This episode dives into how the Hilt annotation processors generate code, and how the Hilt Gradle plugin works behind the scenes to improve the overall experience when using Hilt with Gradle.",
      "url": "https://medium.com/androiddevelopers/mad-skills-series-hilt-under-the-hood-9d89ee227059",
      "publishDate": "2021-09-07T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        14
      ],
      "authors": [
        18
      ]
    },
    {
      "id": 61,
      "episodeId": 47,
      "title": "Hilt extensions",
      "content": "This episode explains how to write your own Hilt Extensions. Hilt Extensions allow you to extend Hilt support to new libraries. Extensions can be created for common patterns in projects, to support non-standard member injection, mirroring bindings, and more.",
      "url": "https://medium.com/androiddevelopers/hilt-extensions-in-the-mad-skills-series-f2ed6fcba5fe",
      "publishDate": "2021-09-12T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        14
      ],
      "authors": [
        19
      ]
    },
    {
      "id": 62,
      "episodeId": 47,
      "title": "Migrating from Dagger to Hilt",
      "content": "While you will eventually want to migrate all your existing Dagger modules over to Hilt’s built in components, you can start by migrating application-wide components to Hilt’s singleton component. This episode explains how.",
      "url": "https://www.youtube.com/watch?v=Xt1_3Nq4lD0&t=15s",
      "publishDate": "2021-09-19T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        14
      ],
      "authors": [
        20
      ]
    },
    {
      "id": 63,
      "episodeId": 47,
      "title": "Trackr comes to the Big Screen",
      "content": "A blog post on Trackr, a sample task management app where we showcase Modern Android Development best practices. This post takes you through how applying Material Design and responsive patterns produced a more refined and intuitive user experience on large screen devices.",
      "url": "https://medium.com/androiddevelopers/trackr-comes-to-the-big-screen-9f13c6f927bf",
      "publishDate": "2021-09-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        1
      ],
      "authors": [
        21
      ]
    },
    {
      "id": 64,
      "episodeId": 47,
      "title": "Accessibility services and the Android Accessibility model",
      "content": "This Accessibilities series episode covers accessibility services like TalkBack, Switch Access and Voice Access and how they help users interact with your apps. Android’s accessibility framework allows you to write one app and the framework takes care of providing the information needed by different accessibility services.",
      "url": "https://youtu.be/LxKat_m7mHk",
      "publishDate": "2021-09-02T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        15
      ],
      "authors": [
        5
      ]
    },
    {
      "id": 65,
      "episodeId": 47,
      "title": "Labeling images for Accessibility",
      "content": "This Accessibilities series episode covers labeling images for accessibility, such as content descriptions for ImageViews and ImageButtons.",
      "url": "https://youtu.be/O2DeSITnzFk",
      "publishDate": "2021-09-09T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        15
      ],
      "authors": [
        22
      ]
    },
    {
      "id": 66,
      "episodeId": 47,
      "title": "New Accessibility Pathway",
      "content": "Want even more accessibility? You are in luck, check out this entire new learning pathway aimed at teaching you how to make your app more accessible.",
      "url": "https://developer.android.com/courses/pathways/make-your-android-app-accessible",
      "publishDate": "2021-08-31T23:00:00.000Z",
      "type": "",
      "topics": [
        15
      ],
      "authors": []
    },
    {
      "id": 67,
      "episodeId": 47,
      "title": "AndroidX Activity Library 1.4.0-alpha01 released",
      "content": "The AndroidX ComponentActivity now implements the MenuHost interface which allows any component to add menu items to the ActionBar by adding a MenuProvider instance to the activity.",
      "url": "https://developer.android.com/jetpack/androidx/releases/activity#1.4.0-alpha01",
      "publishDate": "2021-08-31T23:00:00.000Z",
      "type": "API change",
      "topics": [
        6
      ],
      "authors": []
    },
    {
      "id": 68,
      "episodeId": 47,
      "title": "ADB Podcast Episode 174: Compose in Android Studio",
      "content": "In this episode, Tor and Nick are joined by Chris Sinco, Diego Perez and Nicolas Roard to discuss the features added to Android Studio for Jetpack Compose. Tune in as they discuss the Compose preview, interactive preview, animation inspector, and additions to the Layout inspector along with their approach to creating tooling to support Compose’s code-centric system.",
      "url": "http://adbackstage.libsyn.com/episode-174-compose-tooling",
      "publishDate": "2021-09-08T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        5,
        9
      ],
      "authors": [
        23
      ]
    },
    {
      "id": 69,
      "episodeId": 47,
      "title": "ADB Podcast Episode 175: Creating delightful user experiences with Lottie animations",
      "content": "In this episode, Chet, Romain and Tor have a chat with Gabriel Peal from Tonal, well known for his contributions to the Android community on projects such as Mavericks and Lottie. They talked about Lottie and how it helps designers and developers deliver more delightful user experiences by taking complex animations designed in specialized authoring tools such as After Effects, and rendering them efficiently on mobile devices. They also explored the challenges of designing and implementing a rendering engine such as Lottie.",
      "url": "http://adbackstage.libsyn.com/episode-175-lottie",
      "publishDate": "2021-09-13T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        1
      ],
      "authors": [
        7
      ]
    },
    {
      "id": 70,
      "episodeId": 45,
      "title": "DataStore released into stable",
      "content": "Datastore was released, providing a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.",
      "url": "https://developer.android.com/jetpack/androidx/releases/datastore#1.0.0",
      "publishDate": "2021-08-03T23:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [
        7
      ],
      "authors": []
    },
    {
      "id": 71,
      "episodeId": 44,
      "title": "Jetpack Compose 1.0 stable is released",
      "content": "Jetpack Compose, Android’s modern, native UI toolkit is now stable and ready for you to adopt in production. It interoperates with your existing app, integrates with existing Jetpack libraries, implements Material Design with straightforward theming, supports lists with Lazy components using minimal boilerplate, and has a powerful, extensible animation system.",
      "url": "https://android-developers.googleblog.com/2021/07/jetpack-compose-announcement.html",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        9
      ],
      "authors": [
        24
      ]
    },
    {
      "id": 72,
      "episodeId": 44,
      "title": "Android Studio Artic Fox stable is released",
      "content": "Android Studio Arctic Fox is now available in the stable release channel. Arctic Fox brings Jetpack Compose to life with Compose Preview, Deploy Preview, Compose support in the Layout Inspector, and Live Editing of literals. Compose Preview works with the @Preview annotation to let you instantly see the impact of changes across multiple themes, screen sizes, font sizes, and more. Deploy Preview deploys snippets of your Compose code to a device or emulator for quick testing. Layout inspector now works with apps written fully in Compose as well as apps that have Compose alongside Views, allowing you to explore your layouts and troubleshoot. With Live Edit of literals, you can edit literals such as strings, numbers, booleans, etc. and see the immediate results change in previews, the emulator, or on a physical device — all without having to compile.\n",
      "url": "https://android-developers.googleblog.com/2021/07/android-studio-arctic-fox-202031-stable.html",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        5,
        9
      ],
      "authors": [
        25
      ]
    },
    {
      "id": 73,
      "episodeId": 44,
      "title": "User control, privacy, security, and safety",
      "content": "Play announced new updates to bolster user control, privacy, and security. The post covered advertising ID updates, including zeroing out the advertising ID when users opt out of interest-based advertising or ads personalization, the developer preview of the app set ID, enhanced protection for kids, and policy updates around dormant accounts and users of the AccessibilityService API.",
      "url": "https://android-developers.googleblog.com/2021/07/announcing-policy-updates-to-bolster.html",
      "publishDate": "2021-07-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        11
      ],
      "authors": [
        11
      ]
    },
    {
      "id": 74,
      "episodeId": 44,
      "title": "Identify performance bottlenecks using system trace",
      "content": "System trace profiling within Android Studio with a detailed walkthrough of app startup performance.",
      "url": "https://www.youtube.com/watch?v=aUrqx9AnDUg",
      "publishDate": "2021-07-25T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        3,
        5
      ],
      "authors": [
        26
      ]
    },
    {
      "id": 75,
      "episodeId": 44,
      "title": "Testing in Compose",
      "content": "ADB released episode #171, part of our continuing series on Jetpack Compose. In this episode, Nick and Romain are joined by Filip Pavlis, Jelle Fresen & Jose Alcérreca to talk about Testing in Compose. They discuss how Compose’s testing APIs were developed hand-in-hand with the UI toolkit, making them more deterministic and opening up new possibilities like manipulating time. They go on to discuss the semantics tree, interop testing, screenshot testing and the possibilities for host-side testing.",
      "url": "https://adbackstage.libsyn.com/episode-171-compose-testing",
      "publishDate": "2021-06-29T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        9,
        2
      ],
      "authors": [
        27
      ]
    },
    {
      "id": 76,
      "episodeId": 42,
      "title": "DataStore reached release candidate status",
      "content": "DataStore has reached release candidate status meaning the 1.0 stable release is right around the corner!",
      "url": "https://developer.android.com/topic/libraries/architecture/datastore",
      "publishDate": "2021-06-29T23:00:00.000Z",
      "type": "Jetpack release 🚀",
      "topics": [],
      "authors": []
    },
    {
      "id": 77,
      "episodeId": 42,
      "title": "Scope Storage Myths",
      "content": "Apps will be required to update their targetSdkVersion to API 30 in the second half of the year. That means your app will be required to work with Scoped Storage. In this blog post, Nicole Borrelli busts some Scope storage myths in a Q&A format.",
      "url": "https://medium.com/androiddevelopers/scope-storage-myths-ca6a97d7ff37",
      "publishDate": "2021-06-27T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        7,
        10
      ],
      "authors": [
        28
      ]
    },
    {
      "id": 78,
      "episodeId": 41,
      "title": "Android 12 Beta 2 Update",
      "content": "The second Beta of Android 12 has just been released for you to try. Beta 2 adds new privacy features like the Privacy Dashboard and continues our work of refining the release.",
      "url": "https://android-developers.googleblog.com/2021/06/android-12-beta-2-update.html",
      "publishDate": "2021-06-08T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        13
      ],
      "authors": [
        15
      ]
    },
    {
      "id": 79,
      "episodeId": 41,
      "title": "Grow Your Indie Game with Help From Google Play",
      "content": "Google Play is opening submissions for two of our annual developer programs - the Indie Games Accelerator and the Indie Games Festival. These programs are designed to help small games studios grow on Google Play, no matter what stage they are in",
      "url": "https://developers.googleblog.com/2021/06/grow-your-indie-game-with-help-from-google-play.html",
      "publishDate": "2021-05-31T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        11,
        17
      ],
      "authors": [
        29
      ]
    },
    {
      "id": 80,
      "episodeId": 41,
      "title": "Navigation with Multiple back stacks",
      "content": "As part of the rercommended Material pattern for bottom-navigation, the Jetpack Navigation library makes it easy to implement navigation with multiple back-stacks",
      "url": "https://medium.com/androiddevelopers/navigation-multiple-back-stacks-6c67ba41952f",
      "publishDate": "2021-06-14T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        1
      ],
      "authors": [
        10
      ]
    },
    {
      "id": 81,
      "episodeId": 41,
      "title": "Navigation in Feature Modules",
      "content": "Feature modules delivered with Play Feature delivery at not downloadedd at install time, but only when the app requestss them. Learn how to use the dynamic features navigation library to include the graph from the feature module.",
      "url": "https://medium.com/androiddevelopers/navigation-in-feature-modules-322ac3d79334",
      "publishDate": "2021-06-01T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        1
      ],
      "authors": [
        10
      ]
    },
    {
      "id": 82,
      "episodeId": 41,
      "title": "Android @ Google I/O: 3 things to know in Modern Android Development",
      "content": "This year’s Google I/O brought lots of updates for Modern Android Development. Learn about the top 3 things you should know.",
      "url": "https://android-developers.googleblog.com/2021/05/mad-spotlight.html",
      "publishDate": "2021-05-24T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        0
      ],
      "authors": [
        30
      ]
    },
    {
      "id": 83,
      "episodeId": 41,
      "title": "Top 3 things in Android 12  | Android @ Google I/O '21",
      "content": "Did you miss the latest in Android 12 at Google I/O 2021? Android Software Engineer Chet Haase will recap the top three themes in Android 12 from this year’s Google I/O!",
      "url": "https://www.youtube.com/watch?v=tvf1wmD5H0M",
      "publishDate": "2021-06-08T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        13
      ],
      "authors": [
        7
      ]
    },
    {
      "id": 84,
      "episodeId": 41,
      "title": "Building across devices | Android @ Google I/O '21",
      "content": "Did you miss the latest in Building across screens at Google I/O 2021? Product Manager Diana Wong will recap the top three announcements from this year’s Google I/O!",
      "url": "https://www.youtube.com/watch?v=O5oRiIUk_F4",
      "publishDate": "2021-06-02T23:00:00.000Z",
      "type": "Video 📺",
      "topics": [
        1
      ],
      "authors": [
        31
      ]
    },
    {
      "id": 85,
      "episodeId": 41,
      "title": "Multiple Back Stacks",
      "content": "A deep dive into multiple back stacks and some of the work it took to make this feature happen in Fragments and Navigation",
      "url": "https://medium.com/androiddevelopers/multiple-back-stacks-b714d974f134",
      "publishDate": "2021-06-06T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        1
      ],
      "authors": [
        32
      ]
    },
    {
      "id": 86,
      "episodeId": 41,
      "title": "Build sophisticated search features with AppSearch",
      "content": "AppSearch is an on-device search library which provides high performance and feature-rich full-text search functionality. Learn how to use the new Jetpack AppSearch library for doing high-performance on-device full text searches.",
      "url": "https://android-developers.googleblog.com/2021/06/sophisticated-search-with-appsearch-in-jetpack.html",
      "publishDate": "2021-06-13T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        6,
        1
      ],
      "authors": [
        33
      ]
    },
    {
      "id": 87,
      "episodeId": 41,
      "title": "Untrusted Touch Events in Android",
      "content": "Android 12 prevents touch events from being deliverred if these touches first pass through a window from a different app to ensure users can see what they are interacting with. Learn about alternatives, to see if your app will be affected and how you can test to see if your app will be impacted.",
      "url": "https://medium.com/androiddevelopers/untrusted-touch-events-2c0e0b9c374c",
      "publishDate": "2021-05-25T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        13
      ],
      "authors": [
        8
      ]
    },
    {
      "id": 88,
      "episodeId": 41,
      "title": "Create an application CoroutineScope using Hilt",
      "content": "Learn how to create an applicatioon-scoped CoroutineScope using Hilt, and how to inject it as a dependency.",
      "url": "https://medium.com/androiddevelopers/create-an-application-coroutinescope-using-hilt-dd444e721528",
      "publishDate": "2021-06-09T23:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        14
      ],
      "authors": [
        34
      ]
    },
    {
      "id": 89,
      "episodeId": 41,
      "title": "ADB Podcast Episode 165: Material Witnesses",
      "content": "In this episode, Chet and Romain chattedd with Hunter and Nick from the Material Design team about recent additions and improvements to the Material Design Component libraries: transitions, motion theming, Compose, large screens support and guidance, etc.",
      "url": "http://adbackstage.libsyn.com/episode-165-material-witnesses",
      "publishDate": "2021-06-01T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        1
      ],
      "authors": [
        7
      ]
    },
    {
      "id": 90,
      "episodeId": 41,
      "title": "ADB Podcast Episode 166: Security Deposit",
      "content": "In this episode, Chad and Jeff from the Android Security team join Tor and Romain to talk about… security. They explain what the platform does to help preserve user trust and device integrity, why it sometimes means restricting existing APIs, and touch on what apps can do or should worry about.",
      "url": "http://adbackstage.libsyn.com/episode-166-security-deposit",
      "publishDate": "2021-06-07T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        10
      ],
      "authors": [
        23
      ]
    },
    {
      "id": 91,
      "episodeId": 41,
      "title": "ADB Podcast Episode 167: Jetpack Compose Layout",
      "content": "In this second episode of our mini-series on Jetpack Compose (AD/BC), Nick and Romain are joined by Anastasia Soboleva, George Mount and Mihai Popa to talk about Compose’s layout system. They explain how the Compose layout model works and its benefits, introduce common layout composables, discuss how writing your own layout is far simpler than Views, and how you can even animate layout.",
      "url": "https://adbackstage.libsyn.com/episode-167-jetpack-compose-layout",
      "publishDate": "2021-06-13T23:00:00.000Z",
      "type": "Podcast 🎙",
      "topics": [
        9
      ],
      "authors": [
        35
      ]
    }
  ]
}

    """.trimIndent()
}
