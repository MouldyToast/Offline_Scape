plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    sourceSets.main {
        kotlin {
            setSrcDirs(listOf("src/main/kotlin", "src/main/java"))
            include("**/*.kt", "**/*.java")
        }
    }
}

dependencies {
    compileOnly(projects.core)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.npc.definitions)
    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.npc.spawns)
    implementation(projects.scripts.item.definitions)
    implementation(projects.scripts.item.actions)
    implementation(projects.scripts.interfaces.user)
}
