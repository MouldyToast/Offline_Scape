plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "org.jesse.scripts"
version = "0.1.0"

dependencies {
    api(kotlin("scripting-common"))
    api(kotlin("scripting-jvm"))

    compileOnly(projects.engine)
}
