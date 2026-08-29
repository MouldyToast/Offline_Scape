plugins {
    id("org.jetbrains.kotlin.jvm")
}

version = "0.1.0"
group = "com.near_reality.plugins.excluded.tools"

dependencies {
    compileOnly(projects.core)
    implementation(libs.netty.buffer)
}
