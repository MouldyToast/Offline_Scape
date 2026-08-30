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
    compileOnly(projects.core)
    implementation(projects.scripts.item.actions)
    implementation(projects.content.minigames.pyramidPlunder)
    implementation(projects.content.skills.agility.pyramid)
    implementation(projects.content.skills.agility.shortcuts)
    implementation(projects.content.areas.taverley)
}
