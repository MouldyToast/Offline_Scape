plugins { id("org.jetbrains.kotlin.jvm") }
dependencies {
    compileOnly(projects.engine)
    implementation(projects.content.other.rewards)
}
