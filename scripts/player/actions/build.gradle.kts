plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.scripts.player"
version = "0.1.0"

dependencies {
    api(projects.scripts.player)
    compileOnly(projects.core)
}
