androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.feature.bookmarks"

    dependencies {
        implementation(project(":core:data"))
    }

    feature {}
    compose {}

    testing {
        dependencies {
            implementation(project(":core:testing"))
            androidImplementation(project(":core:testing"))
        }
    }
}
