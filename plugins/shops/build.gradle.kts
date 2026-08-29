plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins"
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
    api(projects.scripts.shops)
    implementation(kotlin("script-runtime"))
    compileOnly(projects.core)
}