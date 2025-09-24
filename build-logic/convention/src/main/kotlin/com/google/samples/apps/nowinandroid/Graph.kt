/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid

import com.android.utils.associateWithNotNull
import com.google.samples.apps.nowinandroid.PluginType.Unknown
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.NONE
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import kotlin.text.RegexOption.DOT_MATCHES_ALL

/**
 * Generates module dependency graphs with `graphDump` task, and update the corresponding `README.md` file with `graphUpdate`.
 *
 * This is not an optimal implementation and could be improved if needed:
 * - [Graph.invoke] is **recursively** searching through dependent projects (although in practice it will never reach a stack overflow).
 * - [Graph.invoke] is entirely re-executed for all projects, without re-using intermediate values.
 * - [Graph.invoke] is always executed during Gradle's Configuration phase (but takes in general less than 1 ms for a project).
 *
 * The resulting graphs can be configured with `graph.ignoredProjects` and `graph.supportedConfigurations` properties.
 */
private class Graph(
    private val root: Project,
    private val dependencies: MutableMap<Project, Set<Pair<Configuration, Project>>> = mutableMapOf(),
    private val plugins: MutableMap<Project, PluginType> = mutableMapOf(),
    private val seen: MutableSet<String> = mutableSetOf(),
) {

    private val ignoredProjects = root.providers.gradleProperty("graph.ignoredProjects")
        .map { it.split(",").toSet() }
        .orElse(emptySet())
    private val supportedConfigurations =
        root.providers.gradleProperty("graph.supportedConfigurations")
            .map { it.split(",").toSet() }
            .orElse(setOf("api", "implementation", "baselineProfile", "testedApks"))

    operator fun invoke(project: Project = root): Graph {
        if (project.path in seen) return this
        seen += project.path
        plugins.putIfAbsent(
            project,
            PluginType.entries.firstOrNull { project.pluginManager.hasPlugin(it.id) } ?: Unknown,
        )
        dependencies.compute(project) { _, u -> u.orEmpty() }
        project.configurations
            .matching { it.name in supportedConfigurations.get() }
            .associateWithNotNull { it.dependencies.withType<ProjectDependency>().ifEmpty { null } }
            .flatMap { (c, value) -> value.map { dep -> c to project.project(dep.path) } }
            .filter { (_, p) -> p.path !in ignoredProjects.get() }
            .forEach { (configuration: Configuration, projectDependency: Project) ->
                dependencies.compute(project) { _, u -> u.orEmpty() + (configuration to projectDependency) }
                invoke(projectDependency)
            }
        return this
    }

    fun dependencies(): Map<String, Set<Pair<String, String>>> = dependencies
        .mapKeys { it.key.path }
        .mapValues { it.value.mapTo(mutableSetOf()) { (c, p) -> c.name to p.path } }

    fun plugins() = plugins.mapKeys { it.key.path }
}

/**
 * Declaration order is important, as only the first match will be retained.
 */
internal enum class PluginType(val id: String, val ref: String, val style: String) {
    AndroidApplication(
        id = "nowinandroid.android.application",
        ref = "android-application",
        style = "fill:#CAFFBF,stroke:#000,stroke-width:2px,color:#000",
    ),
    AndroidFeature(
        id = "nowinandroid.android.feature",
        ref = "android-feature",
        style = "fill:#FFD6A5,stroke:#000,stroke-width:2px,color:#000",
    ),
    AndroidLibrary(
        id = "nowinandroid.android.library",
        ref = "android-library",
        style = "fill:#9BF6FF,stroke:#000,stroke-width:2px,color:#000",
    ),
    AndroidTest(
        id = "nowinandroid.android.test",
        ref = "android-test",
        style = "fill:#A0C4FF,stroke:#000,stroke-width:2px,color:#000",
    ),
    Jvm(
        id = "nowinandroid.jvm.library",
        ref = "jvm-library",
        style = "fill:#BDB2FF,stroke:#000,stroke-width:2px,color:#000",
    ),
    Unknown(
        id = "?",
        ref = "unknown",
        style = "fill:#FFADAD,stroke:#000,stroke-width:2px,color:#000",
    ),
}

internal fun Project.configureGraphTasks() {
    if (!buildFile.exists()) return // Ignore root modules without build file
    val dumpTask = tasks.register<GraphDumpTask>("graphDump") {
        val graph = Graph(this@configureGraphTasks).invoke()
        projectPath = this@configureGraphTasks.path
        dependencies = graph.dependencies()
        plugins = graph.plugins()
        output = this@configureGraphTasks.layout.buildDirectory.file("mermaid/graph.txt")
        legend = this@configureGraphTasks.layout.buildDirectory.file("mermaid/legend.txt")
    }
    tasks.register<GraphUpdateTask>("graphUpdate") {
        projectPath = this@configureGraphTasks.path
        input = dumpTask.flatMap { it.output }
        legend = dumpTask.flatMap { it.legend }
        output = this@configureGraphTasks.layout.projectDirectory.file("README.md")
    }
}

@CacheableTask
private abstract class GraphDumpTask : DefaultTask() {

    @get:Input
    abstract val projectPath: Property<String>

    @get:Input
    abstract val dependencies: MapProperty<String, Set<Pair<String, String>>>

    @get:Input
    abstract val plugins: MapProperty<String, PluginType>

    @get:OutputFile
    abstract val output: RegularFileProperty

    @get:OutputFile
    abstract val legend: RegularFileProperty

    override fun getDescription() = "Dumps project dependencies to a mermaid file."

    @TaskAction
    operator fun invoke() {
        output.get().asFile.writeText(mermaid())
        legend.get().asFile.writeText(legend())
        logger.lifecycle(output.get().asFile.toPath().toUri().toString())
    }

    private fun mermaid() = buildString {
        val dependencies: Set<Dependency> = dependencies.get()
            .flatMapTo(mutableSetOf()) { (project, entries) -> entries.map { it.toDependency(project) } }
        // FrontMatter configuration (not supported yet on GitHub.com)
        appendLine(
            // language=YAML
            """
            ---
            config:
              layout: elk
              elk:
                nodePlacementStrategy: SIMPLE
            ---
            """.trimIndent(),
        )
        // Graph declaration
        appendLine("graph TB")
        // Nodes and subgraphs (limited to a single nested layer)
        val (rootProjects, nestedProjects) = dependencies
            .map { listOf(it.project, it.dependency) }.flatten().toSet()
            .plus(projectPath.get()) // Special case when this specific module has no other dependency
            .groupBy { it.substringBeforeLast(":") }
            .entries.partition { it.key.isEmpty() }
        nestedProjects.sortedByDescending { it.value.size }.forEach { (group, projects) ->
            appendLine("  subgraph $group")
            appendLine("    direction TB")
            projects.sorted().forEach {
                appendLine(it.alias(indent = 4, plugins.get().getValue(it)))
            }
            appendLine("  end")
        }
        rootProjects.flatMap { it.value }.sortedDescending().forEach {
            appendLine(it.alias(indent = 2, plugins.get().getValue(it)))
        }
        // Links
        if (dependencies.isNotEmpty()) appendLine()
        dependencies
            .sortedWith(compareBy({ it.project }, { it.dependency }, { it.configuration }))
            .forEach { appendLine(it.link(indent = 2)) }
        // Classes
        appendLine()
        PluginType.entries.forEach { appendLine(it.classDef()) }
    }

    private fun legend() = buildString {
        appendLine("graph TB")
        listOf(
            "application" to PluginType.AndroidApplication,
            "feature" to PluginType.AndroidFeature,
            "library" to PluginType.AndroidLibrary,
            "jvm" to PluginType.Jvm,
        ).forEach { (name, type) ->
            appendLine(name.alias(indent = 2, type))
        }
        appendLine()
        listOf(
            Dependency("application", "implementation", "feature"),
            Dependency("library", "api", "jvm"),
        ).forEach {
            appendLine(it.link(indent = 2))
        }
        appendLine()
        PluginType.entries.forEach { appendLine(it.classDef()) }
    }

    private class Dependency(val project: String, val configuration: String, val dependency: String)

    private fun Pair<String, String>.toDependency(project: String) =
        Dependency(project, configuration = first, dependency = second)

    private fun String.alias(indent: Int, pluginType: PluginType): String = buildString {
        append(" ".repeat(indent))
        append(this@alias)
        append("[").append(substringAfterLast(":")).append("]:::")
        append(pluginType.ref)
    }

    private fun Dependency.link(indent: Int) = buildString {
        append(" ".repeat(indent))
        append(project).append(" ")
        append(
            when (configuration) {
                "api" -> "-->"
                "implementation" -> "-.->"
                else -> "-.->|$configuration|"
            },
        )
        append(" ").append(dependency)
    }

    private fun PluginType.classDef() = "classDef $ref $style;"
}

@CacheableTask
private abstract class GraphUpdateTask : DefaultTask() {

    @get:Input
    abstract val projectPath: Property<String>

    @get:InputFile
    @get:PathSensitive(NONE)
    abstract val input: RegularFileProperty

    @get:InputFile
    @get:PathSensitive(NONE)
    abstract val legend: RegularFileProperty

    @get:OutputFile
    abstract val output: RegularFileProperty

    override fun getDescription() = "Updates Markdown file with the corresponding dependency graph."

    @TaskAction
    operator fun invoke() = with(output.get().asFile) {
        if (!exists()) {
            createNewFile()
            writeText(
                """
                # `${projectPath.get()}`

                ## Module dependency graph

                <!--region graph--> <!--endregion-->

                """.trimIndent(),
            )
        }
        val mermaid = input.get().asFile.readText().trimTrailingNewLines()
        val legend = legend.get().asFile.readText().trimTrailingNewLines()
        val regex = """(<!--region graph-->)(.*?)(<!--endregion-->)""".toRegex(DOT_MATCHES_ALL)
        val text = readText().replace(regex) { match ->
            val (start, _, end) = match.destructured
            """
            |$start
            |```mermaid
            |$mermaid
            |```
            |
            |<details><summary>📋 Graph legend</summary>
            |
            |```mermaid
            |$legend
            |```
            |
            |</details>
            |$end
            """.trimMargin()
        }
        writeText(text)
    }

    private fun String.trimTrailingNewLines() = lines()
        .dropLastWhile(String::isBlank)
        .joinToString(System.lineSeparator())
}
