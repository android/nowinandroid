androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.feature.bookmarks"

    dependencies {
        implementation(project(":core:data"))
    }

    feature {}

    compose {
        // TODO: This should be a file property, and not assume it's a path from the root project
        stabilityConfigurationFilePath = "/compose_compiler_config.conf"
        experimentalStrongSkipping = true
    }

    testing {
        dependencies {
            implementation(project(":core:testing"))
            androidImplementation(project(":core:testing"))
        }
    }
}
