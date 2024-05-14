androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.core.common"

    buildTypes {
        buildTypes {
            // Need the empty closure to avoid "dangling pure expression" error
            debug {}
            release {}
        }
    }

    testing {
        dependencies {
            testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            testImplementation("app.cash.turbine:turbine:1.0.0")
        }
    }
}
