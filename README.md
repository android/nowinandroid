Now in Android App [WIP]
==================

This is the repository for the [Now in Android](https://developer.android.com/series/now-in-android)
app.

Now in Android is a fully functional Android app built entirely with Kotlin and Jetpack Compose. It
follows Android design and development best practices and is intended to be a useful reference
for developers. As a running app, it's intended to help developers keep up-to-date with the world
of Android development by providing regular news updates.

# Features

Now in Android displays content from the
[Now in Android](https://developer.android.com/series/now-in-android) series. Users can browse for
links to recent videos, articles and other content. Users can also follow topics they are interested
in or follow specific authors.

<!-- TODO: Add screenshots -->

# Development Environment

Now in Android uses the Gradle build system and can be imported directly into the latest stable
version of Android Studio (available [here](https://developer.android.com/studio)). The `debug`
build can be built and run using the default configuration.

Once you're up and running, you can refer to the learning journeys below to get a better
understanding of which libraries and tools are being used, the reasoning behind the approaches to
UI, testing, architecture and more, and how all of these different pieces of the project fit
together to create a complete app.

<!--
# Architecture

TODO (brief overview, link to another doc?)

# Build

TODO (brief overview, link to another doc?

# Testing + CI

TODO (brief overview, link to another doc?)

# UI

TODO (brief overview, link to another doc?)
-->

# Baseline profiles

The baseline profile for this app is located at `app/src/main/baseline-prof.txt`.
It contains rules that enable AOT compilation of the critical user path taken during app launch.
For more information on baseline profiles, read [this document](https://developer.android.com/studio/profile/baselineprofiles).

| Note: The baseline profile needs to be re-generated for release builds that touched code which
| changes app startup.

To generate the baseline profile, select the `benchmark` build variant and run the
`BaselineProfileGenerator` benchmark test on an AOSP Android Emulator.
Then copy the resulting baseline profile from the emulator to `app/src/main/baseline-prof.txt`.

# License

Now in Android is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.
