plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.area"
version = "0.1.0"

dependencies {
    compileOnly(projects.core)
}
