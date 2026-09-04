plugins { id("org.jetbrains.kotlin.jvm") }
dependencies {
    compileOnly(projects.core)
    implementation(projects.scripts.item.definitions)
    implementation(projects.scripts.item.actions)
}
