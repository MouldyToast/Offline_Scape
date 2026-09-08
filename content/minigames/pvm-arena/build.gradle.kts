plugins { id("org.jetbrains.kotlin.jvm") }

dependencies {
    compileOnly(projects.engine)
    compileOnly(projects.content.areas.wilderness)
    implementation(kotlin("reflect"))
}
