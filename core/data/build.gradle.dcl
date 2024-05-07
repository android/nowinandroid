androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.core.data"

    configureJacoco = true

    dependencies {
        api(project(":core:common"))
        api(project(":core:database"))
        api(project(":core:datastore"))
        api(project(":core:network"))

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

    testOptions {
        includeAndroidResources = true
        returnDefaultValues = true
    }

    buildTypes {
        // Need the empty closure to avoid "dangling pure expression" error
        debug {}
        release {}
    }
}
