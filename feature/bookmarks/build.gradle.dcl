androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.feature.bookmarks"

    dependencies {
        implementation(project(":core:data"))
    }

    feature {
        description = "Calling the configure method enables this lib to be treated as a feature"
    }

    compose {
        description = "Calling the configure method enables compose support"
    }

    testing {
        dependencies {
            implementation(project(":core:testing"))
            androidImplementation(project(":core:testing"))
        }
    }
}
