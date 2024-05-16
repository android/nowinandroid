androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.core.data"

    dependencies {
        api(project(":core:common"))
        api(project(":core:database"))
        api(project(":core:datastore"))
        api(project(":core:network"))

        // TODO: once deps in conventions are not REPLACED by project deps, this can be removed
        implementation("androidx.tracing:tracing-ktx:1.3.0-alpha02")

        implementation(project(":core:analytics"))
        implementation(project(":core:notifications"))

        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        testImplementation(project(":core:datastore-test"))
        testImplementation(project(":core:testing"))
        testImplementation(project(":core:network"))
    }

    kotlinSerialization {
        version = "1.6.3"
        json()
    }

    buildTypes {
        // Need the empty closure to avoid "dangling pure expression" error
        debug {}
        release {}
    }

    testing {
        jacoco {
            version = "0.8.7"
        }

        testOptions {
            includeAndroidResources = true
            returnDefaultValues = true
        }
    }
}
