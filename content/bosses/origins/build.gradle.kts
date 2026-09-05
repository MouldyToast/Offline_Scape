plugins { id("org.jetbrains.kotlin.jvm") }

dependencies {
    compileOnly(projects.core)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.npc.definitions)
    implementation(projects.scripts.npc.spawns)
}
