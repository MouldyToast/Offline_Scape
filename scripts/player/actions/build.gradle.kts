plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "org.jesse.scripts.player"
version = "0.1.0"

dependencies {
    api(projects.scripts.player)
    compileOnly(projects.engine)
}
