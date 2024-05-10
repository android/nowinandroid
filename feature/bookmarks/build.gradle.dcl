androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.feature.bookmarks"

    dependencies {
        implementation(project(":core:data"))
        testImplementation(project(":core:testing"))
        androidTestImplementation(project(":core:testing"))
    }

    feature {
        // Calling the configure method enables this lib to be treated as a feature
    }
}


// TODO
// alias(libs.plugins.nowinandroid.android.library.compose)
