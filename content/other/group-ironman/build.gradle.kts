plugins {
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.kotlin.serialization)
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
