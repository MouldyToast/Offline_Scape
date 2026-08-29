plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(libs.slf4j.api)
    api(libs.kotlin.inline.logger)
    api(libs.fastutil)
    implementation(libs.apache.commons.text)
    implementation(libs.kotlinx.datetime)
    api(libs.hikari.cp)
    api(libs.mysql.connector.j)
    api(libs.google.gson)
}