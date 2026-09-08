plugins { id("org.jetbrains.kotlin.jvm") }
dependencies {
    compileOnly(projects.engine)
    implementation(projects.scripts.item.definitions)
    implementation(projects.scripts.item.actions)
}
