plugins {
    alias(libs.plugins.nowinandroid.jvm.library)
    alias(libs.plugins.nowinandroid.hilt)
}

dependencies {
    api(libs.androidx.navigation3.runtime)
}
