plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.scripts.npc"
version = "0.1.0"

dependencies {
    api(projects.scripts.npc)
    compileOnly(projects.core)
}