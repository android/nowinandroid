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

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import java.io.File

/**
 * Module types for the KMP fork of Now in Android.
 * Maps to convention plugin IDs.
 */
enum class PluginType(
    val id: String,
    val displayName: String,
    val color: String,
) {
    CmpApplication(
        id = "nowinandroid.cmp.application",
        displayName = "cmp-application",
        color = "#CAFFBF",
    ),
    CmpFeature(
        id = "nowinandroid.cmp.feature",
        displayName = "cmp-feature",
        color = "#FFD6A5",
    ),
    KmpLibrary(
        id = "nowinandroid.kmp.library",
        displayName = "kmp-library",
        color = "#9BF6FF",
    ),
    JvmLibrary(
        id = "nowinandroid.jvm.library",
        displayName = "jvm-library",
        color = "#BDB2FF",
    ),
    AndroidTest(
        id = "nowinandroid.android.test",
        displayName = "android-test",
        color = "#A0C4FF",
    ),
}

/**
 * Edge types representing different dependency configurations.
 */
enum class EdgeType(val mermaidStyle: String) {
    Api("-->"),
    Implementation("-.->"),
}

data class GraphEdge(
    val from: String,
    val to: String,
    val type: EdgeType,
    val label: String? = null,
)

private val supportedConfigurations = listOf(
    "commonMainApi" to EdgeType.Api,
    "commonMainImplementation" to EdgeType.Implementation,
    "api" to EdgeType.Api,
    "implementation" to EdgeType.Implementation,
    "baselineProfile" to EdgeType.Implementation,
    "testedApks" to EdgeType.Implementation,
)

private val labeledConfigurations = setOf("baselineProfile", "testedApks")

/**
 * Detects the plugin type of a project based on which convention plugins are applied.
 */
fun Project.pluginType(): PluginType? {
    return PluginType.entries.firstOrNull { pluginManager.hasPlugin(it.id) }
}

/**
 * Collects all project dependency edges for the given project.
 */
fun Project.collectEdges(): List<GraphEdge> {
    val edges = mutableListOf<GraphEdge>()
    for ((configName, edgeType) in supportedConfigurations) {
        val config = configurations.findByName(configName) ?: continue
        config.dependencies.filterIsInstance<ProjectDependency>().forEach { dep ->
            val label = if (configName in labeledConfigurations) configName else null
            val depPath = dep.path
            edges.add(GraphEdge(path, depPath, edgeType, label))
        }
    }
    return edges
}

/**
 * Generates the Mermaid graph text for a given root project, showing all
 * modules that are reachable from the specified project.
 */
fun generateMermaidGraph(
    rootProjectPath: String,
    allEdges: Map<String, List<GraphEdge>>,
    allPluginTypes: Map<String, PluginType?>,
    ignoredProjects: Set<String> = emptySet(),
): String {
    // Find all reachable modules from the root
    val reachable = mutableSetOf(rootProjectPath)
    val queue = ArrayDeque<String>()
    queue.add(rootProjectPath)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        allEdges[current]?.forEach { edge ->
            if (edge.to !in reachable && edge.to !in ignoredProjects) {
                reachable.add(edge.to)
                queue.add(edge.to)
            }
        }
    }

    val relevantEdges = allEdges.values.flatten()
        .filter { it.from in reachable && it.to in reachable }
        .filter { it.from !in ignoredProjects && it.to !in ignoredProjects }
        .sortedWith(compareBy({ it.from }, { it.to }))

    val modules = reachable.filter { it !in ignoredProjects }.sorted()

    // Group modules by top-level parent for subgraph generation
    val grouped = modules.groupBy { path ->
        val parts = path.removePrefix(":").split(":")
        if (parts.size > 1) ":${parts.first()}" else null
    }

    val sb = StringBuilder()
    sb.appendLine("```mermaid")
    sb.appendLine("---")
    sb.appendLine("config:")
    sb.appendLine("  layout: elk")
    sb.appendLine("  elk:")
    sb.appendLine("    nodePlacementStrategy: SIMPLE")
    sb.appendLine("---")
    sb.appendLine("graph TB")

    // Render subgraphs for grouped modules
    for ((group, members) in grouped.toSortedMap(nullsLast(compareBy { it }))) {
        if (group != null && members.size > 1) {
            sb.appendLine("  subgraph $group")
            sb.appendLine("    direction TB")
            for (member in members.sorted()) {
                val shortName = member.split(":").last()
                val pluginType = allPluginTypes[member]
                val classDef = pluginType?.displayName ?: "unknown"
                sb.appendLine("    $member[$shortName]:::$classDef")
            }
            sb.appendLine("  end")
        }
    }

    // Render ungrouped modules (top-level modules)
    for ((group, members) in grouped.toSortedMap(nullsLast(compareBy { it }))) {
        if (group == null || members.size == 1) {
            for (member in members.sorted()) {
                val shortName = member.split(":").last()
                val pluginType = allPluginTypes[member]
                val classDef = pluginType?.displayName ?: "unknown"
                sb.appendLine("  $member[$shortName]:::$classDef")
            }
        }
    }

    sb.appendLine()

    // Render edges
    for (edge in relevantEdges) {
        val labelPart = if (edge.label != null) "|${edge.label}| " else ""
        sb.appendLine("  ${edge.from} ${edge.type.mermaidStyle}$labelPart ${edge.to}")
    }

    sb.appendLine()

    // Render classDef styles
    val usedTypes = modules.mapNotNull { allPluginTypes[it] }.toSet()
    for (type in PluginType.entries) {
        sb.appendLine("classDef ${type.displayName} fill:${type.color},stroke:#000,stroke-width:2px,color:#000;")
    }
    sb.appendLine("classDef unknown fill:#FFADAD,stroke:#000,stroke-width:2px,color:#000;")

    sb.appendLine("```")
    return sb.toString()
}

/**
 * Generates the legend section for the graph.
 */
fun generateLegend(): String {
    val sb = StringBuilder()
    sb.appendLine("<details><summary>Graph legend</summary>")
    sb.appendLine()
    sb.appendLine("```mermaid")
    sb.appendLine("graph TB")

    for (type in PluginType.entries) {
        sb.appendLine("  ${type.displayName}[${type.displayName}]:::${type.displayName}")
    }

    sb.appendLine()
    sb.appendLine("  cmp-application -.-> cmp-feature")
    sb.appendLine("  kmp-library --> jvm-library")
    sb.appendLine()

    for (type in PluginType.entries) {
        sb.appendLine("classDef ${type.displayName} fill:${type.color},stroke:#000,stroke-width:2px,color:#000;")
    }
    sb.appendLine("classDef unknown fill:#FFADAD,stroke:#000,stroke-width:2px,color:#000;")
    sb.appendLine("```")
    sb.appendLine()
    sb.appendLine("</details>")
    return sb.toString()
}

/**
 * Updates a README.md file, replacing content between graph region markers.
 */
fun updateReadmeGraph(readmeFile: File, graphContent: String) {
    if (!readmeFile.exists()) return
    val content = readmeFile.readText()
    val startMarker = "<!--region graph-->"
    val endMarker = "<!--endregion-->"

    val startIdx = content.indexOf(startMarker)
    val endIdx = content.indexOf(endMarker)

    if (startIdx == -1 || endIdx == -1) return

    val newContent = buildString {
        append(content.substring(0, startIdx + startMarker.length))
        appendLine()
        append(graphContent)
        appendLine(generateLegend())
        append(content.substring(endIdx))
    }

    readmeFile.writeText(newContent)
}

/**
 * Registers the `graphDump` and `graphUpdate` tasks on the root project.
 */
fun Project.configureGraphTasks() {
    val ignoredProjects = providers.gradleProperty("graph.ignoredProjects")
        .orElse("")
        .map { it.split(",").map(String::trim).filter(String::isNotEmpty).toSet() }

    tasks.register("graphDump") {
        group = "documentation"
        description = "Dumps the module dependency graph as Mermaid text"
        doLast {
            val allEdges = mutableMapOf<String, List<GraphEdge>>()
            val allPluginTypes = mutableMapOf<String, PluginType?>()

            subprojects.forEach { sub ->
                allEdges[sub.path] = sub.collectEdges()
                allPluginTypes[sub.path] = sub.pluginType()
            }

            subprojects.forEach { sub ->
                val readmeFile = sub.file("README.md")
                if (readmeFile.exists() && readmeFile.readText().contains("<!--region graph-->")) {
                    val graph = generateMermaidGraph(
                        rootProjectPath = sub.path,
                        allEdges = allEdges,
                        allPluginTypes = allPluginTypes,
                        ignoredProjects = ignoredProjects.get(),
                    )
                    println("=== ${sub.path} ===")
                    println(graph)
                }
            }
        }
    }

    tasks.register("graphUpdate") {
        group = "documentation"
        description = "Updates README.md files with module dependency graphs"
        doLast {
            val allEdges = mutableMapOf<String, List<GraphEdge>>()
            val allPluginTypes = mutableMapOf<String, PluginType?>()

            subprojects.forEach { sub ->
                allEdges[sub.path] = sub.collectEdges()
                allPluginTypes[sub.path] = sub.pluginType()
            }

            subprojects.forEach { sub ->
                val readmeFile = sub.file("README.md")
                if (readmeFile.exists() && readmeFile.readText().contains("<!--region graph-->")) {
                    val graph = generateMermaidGraph(
                        rootProjectPath = sub.path,
                        allEdges = allEdges,
                        allPluginTypes = allPluginTypes,
                        ignoredProjects = ignoredProjects.get(),
                    )
                    updateReadmeGraph(readmeFile, graph)
                    println("Updated: ${readmeFile.relativeTo(rootDir)}")
                }
            }
        }
    }
}
