plugins {
    id("org.jetbrains.kotlin.jvm")
}

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
