# Declarative Gradle DSL

## Current status

This branch has been modified to work with the prototype of the Declarative Gradle DSL. This build relies on nightly versions of Gradle, Android Studio and [Declarative Gradle prototype plugins](https://github.com/gradle/declarative-gradle).

The NowInAndroid project is a "hybrid" build now where there's a mix of declarative and non-declarative (Kotlin DSL) build files. The build can be imported and builds.

The [settings file](settings.gradle.dcl) applies a new "Android ecosystem plugin", which exposes `androidLibrary` and `androidApplication` software types that can be used in subprojects. The current prototype is limited to a single `androidLibrary` software type convention, so only a few subprojects have been converted.

Converted subprojects:
- [`:core:common`](core/common/build.gradle.dcl)
- [`:core:data`](core/data/build.gradle.dcl)
- [`:core:domain`](core/domain/build.gradle.dcl)

Syntax highlighting is limited to the latest nightly for Android Studio that understand Gradle DCL files.

## Setup

```
git clone https://github.com/gradle/nowinandroid.git
cd nowinandroid
git clone https://github.com/gradle/declarative-gradle.git
```

This should checkout the `main` branch of the [Declarative Gradle prototype plugins](https://github.com/gradle/declarative-gradle) inside the **root** of a NowInAndroid fork.

You should have this project structure:
```
nowinandroid/
    declarative-gradle/
```

## Trying things out

### Building the project

You can assemble the project with the following command:

```shell
./gradlew buildDemoDebug
```

### Running tests
**Note:** See the note in [Screenshot tests](#screenshot-tests) about setting up Roborazzi for non-Linux test runs.

```shell
./gradlew testDemoDebug :lint:test
```

```shell
./gradlew testDemoDebugUnitTest -Proborazzi.test.verify=false
```

After starting a local Android emulator in Android Studio:
```shell
./gradlew connectedDemoDebugAndroidTest --daemon
````

