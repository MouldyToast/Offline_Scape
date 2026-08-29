plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.near_reality.plugins.excluded.boss"
version = "0.1.0"

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

    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.npc.definitions)
    implementation(projects.scripts.npc.drops)

    implementation(projects.scripts.item.actions)
    implementation(projects.scripts.item.equip)
    implementation(projects.scripts.item.definitions)

    implementation(projects.scripts.`object`.actions)
}
