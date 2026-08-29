plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins"
version = "0.1.0"

dependencies {
    compileOnly(projects.core)
}