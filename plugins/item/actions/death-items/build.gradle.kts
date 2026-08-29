plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.item.actions"
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
    implementation(projects.plugins.item.actions)
    compileOnly(projects.core)
}