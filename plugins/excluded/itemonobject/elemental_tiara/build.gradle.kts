plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.excluded.itemonobject"
version = "0.1.0"

dependencies {
    compileOnly(projects.core)
}
