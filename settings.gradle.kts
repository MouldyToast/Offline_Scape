import java.nio.file.Files
import java.nio.file.Path

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven(url = "https://maven.rsps.cloud/")
        maven(url = "https://repo.runelite.net/") {
            content {
                includeGroup("net.runelite")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"

    id("org.jetbrains.kotlin.jvm") version "2.2.0" apply false
}

include(":api")

include(
    ":threads",
    ":util",
    ":core-model",
    ":cache",
    ":engine",
)

include(
    ":scripts:common",

    ":scripts:ground-items",

    ":scripts:interfaces",
    ":scripts:interfaces:user",

    ":scripts:item",
    ":scripts:item:actions",
    ":scripts:item:definitions",
    ":scripts:item:equip",

    ":scripts:npc",
    ":scripts:npc:actions",
    ":scripts:npc:definitions",
    ":scripts:npc:drops",
    ":scripts:npc:spawns",

    ":scripts:object",
    ":scripts:object:actions",

    ":scripts:player",
    ":scripts:player:actions",

    ":scripts:shops",
)

include(":app")

// Auto-discovered content and tools modules.
// Any directory under content/ or tools/ with a build.gradle.kts
// automatically becomes a Gradle module — no manual edits needed.
include("content")
include("tools")
includeProjects(project(":content"))
includeProjects(project(":tools"))

rootProject.name = "nr-228"

fun includeProjects(pluginProject: ProjectDescriptor) {
    val projectPath = pluginProject.projectDir.toPath()
    Files.walk(projectPath).use { stream ->
        stream.filter { Files.isDirectory(it) }.forEach {
            searchProject(pluginProject.name, projectPath, it)
        }
    }
}

fun searchProject(parentName: String, root: Path, currentPath: Path) {
    val dirName = currentPath.fileName?.toString() ?: return
    if (dirName == "build" || dirName.startsWith(".")) return

    val hasBuildFile = Files.exists(currentPath.resolve("build.gradle.kts"))
    if (!hasBuildFile) return
    val relativePath = root.relativize(currentPath)
    if (relativePath.toString().isEmpty()) return
    val projectName = relativePath.toString().replace(File.separator, ":")
    include("$parentName:$projectName")
}
