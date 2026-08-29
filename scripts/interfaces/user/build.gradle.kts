plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.scripts.interfaces"
version = "0.1.0"

dependencies {
    api(projects.scripts.interfaces)
    compileOnly(projects.core)
}