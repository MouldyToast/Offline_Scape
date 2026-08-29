plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.area"
version = "0.1.0"

kotlin {
    sourceSets.main {
        kotlin {
            setSrcDirs(listOf("src/main/kotlin"))
            include("**/*.kt")
        }
    }
}

dependencies {
    implementation(projects.scripts.npc.spawns)
    compileOnly(projects.core)
}