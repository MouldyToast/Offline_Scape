plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.excluded.boss"
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
    compileOnly(projects.core)

    implementation(projects.scripts.npc.actions)
	implementation(kotlin("script-runtime"))
}