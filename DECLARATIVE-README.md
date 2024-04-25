# Declarative Gradle DSL

This branch has been modified to work with the prototype of the Declarative Gradle DSL.

## Feedback on basics

- "Hybrid" mode where some projects can use declarative files and some do not
- Ecosystem plugin in settings file and software type in declarative project file
- Importable and buildable (e.g., gradle build works)
- Syntax highlighting available in Android Studio
- Demonstrate NowInAndroid has at least one project file converted

## Setup

```
git clone https://github.com/gradle/nowinandroid.git
cd nowinandroid
git clone https://github.com/gradle/declarative-gradle.git
```

This should checkout the `main` branch of [declarative-gradle](https://github.com/gradle/declarative-gradle) inside the **root** of a NowInAndroid fork.

```
nowinandroid/
    declarative-gradle/
```

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

