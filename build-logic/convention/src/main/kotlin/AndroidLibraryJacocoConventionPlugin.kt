import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.google.samples.apps.nowinandroid.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryJacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("com.android.library") {
                pluginManager.apply("org.gradle.jacoco")
                val extension = extensions.getByType<LibraryAndroidComponentsExtension>()
                configureJacoco(extension)
            }
        }
    }

}