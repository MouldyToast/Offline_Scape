plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.interfaces"
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
    implementation(projects.scripts.interfaces)
    compileOnly(projects.core)
}