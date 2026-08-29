plugins {
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.kotlin.serialization)
}

group = "com.near_reality.plugins"
version = "0.1.0"

kotlin {
    sourceSets.main {
        kotlin {
            setSrcDirs(listOf("src/main/kotlin"))
            include("**/*.kt")
        }
    }
}

dependencies {
    implementation(libs.apache.commons.lang3)
    implementation(libs.rsprot.osrs228.api)
    compileOnly(projects.core)

    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.npc.spawns)
    implementation(projects.scripts.item.actions)
    implementation(projects.scripts.player.actions)
    implementation(projects.scripts.`object`.actions)

    implementation(projects.scripts.interfaces)
    implementation(projects.plugins.larranskey)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}
