plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "cloud.rsps.game.plugins.interfaces"
version = "0.1.0"

dependencies {
    implementation(projects.scripts.interfaces)
    compileOnly(projects.core)
}
