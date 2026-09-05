plugins { id("org.jetbrains.kotlin.jvm") }

dependencies {
    compileOnly(projects.core)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.item.actions)
}
