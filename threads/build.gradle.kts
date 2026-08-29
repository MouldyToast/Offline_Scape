plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(libs.openhft.affinity)
    api(libs.openhft.chronicle.threads)

    implementation(libs.slf4j.api)
}