androidLibrary {
    namespace = "com.google.samples.apps.nowinandroid.core.domain"

    configureJacoco = true

    dependencies {
        implementation("javax.inject:javax.inject:1")

        api(projects.core.data)
        api(projects.core.model)

        testImplementation(projects.core.testing)
    }

    buildTypes {
        // Need the empty closure to avoid "dangling pure expression" error
        debug {}
        release {}
    }
}
