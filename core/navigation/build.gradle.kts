plugins {
    alias(libs.plugins.nowinandroid.jvm.library)
    alias(libs.plugins.nowinandroid.hilt)
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
}
