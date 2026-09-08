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
    api(projects.scripts.shops)
    implementation(kotlin("script-runtime"))
    compileOnly(projects.engine)
}
