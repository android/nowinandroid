plugins {
    alias(libs.plugins.nowinandroid.android.library)
    alias(libs.plugins.nowinandroid.hilt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.google.samples.apps.nowinandroid.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.savedstate.compose)

    testImplementation(libs.truth)

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.compose.ui.testManifest)
    androidTestImplementation(libs.androidx.lifecycle.viewModel.testing)
    androidTestImplementation(libs.truth)
}
