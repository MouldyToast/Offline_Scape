plugins { id("org.jetbrains.kotlin.jvm") }

dependencies {
    compileOnly(projects.core)
    compileOnly(projects.content.areas.wilderness)
    implementation(kotlin("reflect"))
}
