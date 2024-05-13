androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.feature.bookmarks"

    dependencies {
        implementation(project(":core:data"))
        testImplementation(project(":core:testing"))
        androidTestImplementation(project(":core:testing"))
    }

    feature {
        description = "Calling the configure method enables this lib to be treated as a feature"
    }

    compose {
        description = "Calling the configure method enables compose support"
    }
}
