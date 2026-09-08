plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly(projects.engine)
    implementation(libs.netty.buffer)
}
