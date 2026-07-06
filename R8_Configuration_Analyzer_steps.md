# R8 Configuration Analyzer Guide

## Custom R8 Version Setup
To force the Android Gradle Plugin to use a custom version of R8 (like `9.3.+`), you need to update your `settings.gradle.kts` file.

Add the following `buildscript` block at the very top inside `pluginManagement`:

```kotlin
pluginManagement {
    buildscript {
        repositories {
            mavenCentral()
            maven {
                url = uri("https://storage.googleapis.com/r8-releases/raw")
            }
        }
        dependencies {
            classpath("com.android.tools:r8:9.3.7-dev")
        }
    }
    // ... rest of your pluginManagement block
}
```

## Generating the R8 Keep Rule Analyzer Report
To generate the R8 Keep Rule dump, you need to execute a minified build and pass the `dumpkeepradiushtmltodirectory` flag to the JVM arguments running the R8 compiler.

Run the following command in your terminal:

```bash
./gradlew clean assembleBenchmark --no-build-cache -Dcom.android.tools.r8.dumpkeepradiushtmltodirectory=/tmp/r8analysis
```
After the build succeeds, the HTML tree report will be available in the `/tmp/r8analysis` directory.
