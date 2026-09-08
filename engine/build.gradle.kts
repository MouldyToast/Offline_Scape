plugins {
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(projects.threads)
    api(projects.cache)
    api(projects.coreModel)

    api(libs.logback.classic)
    api(libs.fastutil)
    api(libs.google.guava)
    api(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    api(libs.kotlinx.datetime)
    api(libs.classgraph)
    api(libs.okhttp)
    api(libs.checker.qual)
    api(libs.javaparser.core)
    api(libs.javaparser.symbol.solver.core)

    api(libs.apache.commons.lang3)
    implementation(libs.apache.commons.codec)
    implementation(libs.jctools.core)
    implementation(libs.jackson.module.afterburner)
    api(libs.jackson.module.kotlin)
    //implementation(libs.logstash.gelf)
    implementation(libs.mXparser)

    implementation(libs.bundles.ktor.server)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.bundles.ktor.client)

    implementation(libs.bundles.exposed)

    implementation(libs.postgresql)

    implementation(libs.kord.core)
    implementation(libs.jda)

    implementation(libs.netty.codec.haproxy)

    implementation(libs.mockk)

    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.serialization.jackson)

    implementation(libs.googleauth)
}
