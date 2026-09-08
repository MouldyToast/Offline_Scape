plugins { id("org.jetbrains.kotlin.jvm") }

kotlin {
    sourceSets.main {
        kotlin {
            setSrcDirs(listOf("src/main/kotlin", "src/main/java"))
            include("**/*.kt", "**/*.java")
        }
    }
}

dependencies {
    compileOnly(projects.engine)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.groundItems)
}
