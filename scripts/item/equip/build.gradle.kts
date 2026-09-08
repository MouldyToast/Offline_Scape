plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "org.jesse.scripts.item"
version = "0.1.0"

dependencies {
    api(projects.scripts.item)
    compileOnly(projects.engine)
}