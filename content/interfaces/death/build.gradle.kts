plugins { id("org.jetbrains.kotlin.jvm") }
dependencies {
    implementation(projects.scripts.interfaces)
    compileOnly(projects.core)
}
