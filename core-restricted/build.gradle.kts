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
    compileOnly(projects.threads)
    compileOnly(projects.cache)
    compileOnly(projects.coreModel)
    compileOnly(projects.core)
    // Temporary until PvM Arena moves out: PvmArenaKingBlackDragon extends
    // KingBlackDragon, now in content/areas/wilderness.
    compileOnly(projects.content.areas.wilderness)

    implementation(projects.scripts.npc.spawns)
    implementation(projects.scripts.npc)
    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.npc.definitions)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.interfaces)

    implementation(projects.scripts.groundItems)

    implementation(projects.scripts.item.actions)
    implementation(projects.scripts.item.equip)
    implementation(projects.scripts.item.definitions)

    implementation(projects.scripts.`object`.actions)

    compileOnly(libs.logback.classic)
    compileOnly(libs.fastutil)
    compileOnly(libs.google.guava)
    compileOnly(libs.kotlinx.coroutines.core)
    compileOnly(libs.kotlinx.datetime)
    compileOnly(libs.classgraph)
    compileOnly(libs.okhttp)
    compileOnly(libs.checker.qual)
    compileOnly(libs.javaparser.core)
    compileOnly(libs.javaparser.symbol.solver.core)

    compileOnly(libs.apache.commons.lang3)
    compileOnly(libs.apache.commons.codec)
    compileOnly(libs.jctools.core)
    compileOnly(libs.jackson.module.afterburner)
    compileOnly(libs.jackson.module.kotlin)
    //compileOnly(libs.logstash.gelf)
    compileOnly(libs.mXparser)

    compileOnly(libs.ktor.client.core)
    compileOnly(libs.ktor.client.okhttp)
    compileOnly(libs.ktor.client.content.negotiation)
    compileOnly(libs.ktor.client.logging)
    compileOnly(libs.ktor.serialization.kotlinx.json)

    compileOnly(libs.kord.core)

    compileOnly(libs.netty.codec.haproxy)

    compileOnly(libs.mockk)
}
