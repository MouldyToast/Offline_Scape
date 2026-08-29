plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly(projects.core)
    implementation(libs.netty.buffer)
}
