androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.core.domain"

    dependencies {
        implementation("javax.inject:javax.inject:1")

        api(project(":core:data"))
        api(project(":core:model"))
    }

    buildTypes {
        // Need the empty closure to avoid "dangling pure expression" error
        debug {}
        release {}
    }

    testing {
        dependencies {
            testImplementation(project(":core:testing"))
        }

        jacoco {
            version = "0.8.7"
        }
    }
}
