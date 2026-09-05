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
    compileOnly(projects.core)
    implementation(projects.scripts.interfaces)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.item.actions)
    implementation(libs.mockk)
}
