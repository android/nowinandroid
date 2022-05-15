import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.google.samples.apps.nowinandroid.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("com.android.application") {
                val extension = extensions.getByType<BaseAppModuleExtension>()
                configureAndroidCompose(extension)
            }
        }
    }

}