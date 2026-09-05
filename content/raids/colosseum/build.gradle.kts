plugins { id("org.jetbrains.kotlin.jvm") }
dependencies {
    compileOnly(projects.core)
    compileOnly(libs.jackson.module.kotlin)
}
