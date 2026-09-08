plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "org.jesse.scripts.interfaces"
version = "0.1.0"

dependencies {
    api(projects.scripts.interfaces)
    compileOnly(projects.engine)
}