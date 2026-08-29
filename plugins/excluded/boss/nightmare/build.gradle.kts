plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.excluded.boss"
version = "0.1.0"

dependencies {
    compileOnly(projects.core)
}
