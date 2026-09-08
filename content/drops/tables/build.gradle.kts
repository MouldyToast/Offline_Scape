plugins { id("org.jetbrains.kotlin.jvm") }
dependencies {
    compileOnly(projects.engine)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.npc.definitions)
}
