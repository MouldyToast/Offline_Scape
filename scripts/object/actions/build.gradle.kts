plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "org.jesse.scripts.object"
version = "0.1.0"

dependencies {
    api(projects.scripts.`object`)
    compileOnly(projects.engine)
}