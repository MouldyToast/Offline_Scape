plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.npc"
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
    implementation(projects.scripts.npc.drops)
    compileOnly(projects.core)
}