plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "org.jesse.scripts.npc"
version = "0.1.0"

dependencies {
    api(projects.scripts.npc)
    compileOnly(projects.engine)
}