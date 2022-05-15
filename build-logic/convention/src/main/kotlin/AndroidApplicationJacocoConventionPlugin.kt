import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.google.samples.apps.nowinandroid.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationJacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("com.android.application") {
                pluginManager.apply("org.gradle.jacoco")
                val extension = extensions.getByType<ApplicationAndroidComponentsExtension>()
                configureJacoco(extension)
            }
        }
    }

}