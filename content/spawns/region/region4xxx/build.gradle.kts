plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    sourceSets.main {
        kotlin {
            setSrcDirs(listOf("src/main/kotlin"))
            include("**/*.kt")
        }
    }
}

dependencies {
    implementation(projects.scripts.npc.spawns)
    compileOnly(projects.engine)
}
