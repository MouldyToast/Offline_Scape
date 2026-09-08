plugins { id("org.jetbrains.kotlin.jvm") }
dependencies {
    compileOnly(projects.engine)
    implementation(projects.scripts.npc.actions)
}
