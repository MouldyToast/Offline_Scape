plugins { id("org.jetbrains.kotlin.jvm") }
dependencies {
    compileOnly(projects.core)
    implementation(projects.scripts.`object`.actions)
    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.item.definitions)
    implementation(projects.scripts.item.actions)
}
