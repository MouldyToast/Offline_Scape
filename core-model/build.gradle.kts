plugins {
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(projects.api)
    api(libs.fastutil)

    compileOnly(libs.google.gson)
    compileOnly(libs.slf4j.api)
    compileOnly(libs.netty.buffer)

    compileOnly(libs.kotlinx.serialization.core)
    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.kotlinx.serialization.protobuf)

    compileOnly(libs.kotlinx.datetime)

    compileOnly(libs.bundles.exposed)

    compileOnly(libs.jackson.core)
    compileOnly(libs.jackson.databind)
    compileOnly(libs.jackson.datatype.jsr310)
    compileOnly(libs.jackson.module.afterburner)
    compileOnly(libs.jackson.module.kotlin)

    compileOnly(libs.apache.commons.lang3)
    compileOnly(libs.apache.commons.io)
}
