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
    implementation(projects.content.other.rewards)
}
