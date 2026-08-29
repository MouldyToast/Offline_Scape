plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.scripts"
version = "0.1.0"

dependencies {
    api(projects.scripts.common)
    compileOnly(projects.core)
}