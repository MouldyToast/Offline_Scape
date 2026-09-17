import java.net.URI

plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(projects.util)

    api(projects.coreModel)

    implementation(projects.util)
    implementation(libs.google.guava)
    implementation(libs.apache.commons.io)
    implementation(libs.zip4j)
    implementation(libs.apache.commons.compress)
    implementation(libs.apache.commons.lang3)
    implementation(libs.jsoup)
    implementation(libs.apache.ant)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.module.afterburner)
    implementation(libs.apache.commons.cli)
    implementation("pl.allegro.finance:tradukisto:4.3.3")

    implementation(libs.netty.transport.native.iouring)
    implementation(libs.netty.transport.native.epoll)
    implementation(libs.netty.transport.native.kqueue)

    api(libs.netty.handler)
    api(libs.netty.codec.http2)

    api(libs.runelite.api)
    api(libs.runelite.cache)

    api(libs.openhft.chronicle.core)

    api(libs.rsprot.osrs240.api)

    api(libs.bouncycastle.pkix)
    api(libs.bouncycastle.provider)

    api(libs.jctools.core)
    api(libs.xz)

    implementation(libs.sqlite)

    runtimeClasspath(libs.slf4j.simple)
}

// OpenRS2 Archive — rev 240 cache (2026-09-16, ID 2710)
// Browse available caches at: https://archive.openrs2.org/caches
val openrs2CacheId = "2710"
val openrs2Scope = "runescape"
val cacheDir = file("data/cache")
val xteaFile = file("data/objects/xteas.json")
val cacheZip = file("data/cache-240-openrs2.zip")

tasks.register("downloadCache") {
    group = "_cache"
    description = "Download OSRS rev 240 cache from OpenRS2 Archive"
    outputs.upToDateWhen { cacheDir.exists() && cacheDir.list()?.any { it.startsWith("main_file_cache") } == true }
    doLast {
        if (cacheDir.exists() && cacheDir.list()?.any { it.startsWith("main_file_cache") } == true) {
            println("Cache already exists at ${cacheDir.absolutePath}, skipping. Delete cache dir or run resetCache to re-extract.")
            return@doLast
        }
        if (cacheZip.exists()) {
            println("Zip already on disk, extracting from local copy...")
        } else {
            println("Downloading rev 240 cache from OpenRS2 Archive (ID $openrs2CacheId)...")
            URI("https://archive.openrs2.org/caches/$openrs2Scope/$openrs2CacheId/disk.zip").toURL().openStream().use { input ->
                cacheZip.outputStream().use { output -> input.copyTo(output) }
            }
        }
        println("Extracting to ${cacheDir.absolutePath}...")
        // disk.zip contains a cache/ subdirectory — extract to data/ so it becomes data/cache/
        copy {
            from(zipTree(cacheZip))
            into(file("data"))
        }
        println("Cache ready (${cacheDir.listFiles()?.size ?: 0} files). Zip kept at ${cacheZip.name} for fast resets.")
    }
}

tasks.register("resetCache") {
    group = "_cache"
    description = "Wipe cache dir and re-extract from the local zip (no download)"
    doLast {
        if (!cacheZip.exists()) {
            throw GradleException("No local zip at ${cacheZip.absolutePath}. Run setupCache first to download it.")
        }
        if (cacheDir.exists()) {
            println("Deleting ${cacheDir.absolutePath}...")
            cacheDir.deleteRecursively()
        }
        println("Extracting fresh cache from ${cacheZip.name}...")
        copy {
            from(zipTree(cacheZip))
            into(file("data"))
        }
        println("Cache reset (${cacheDir.listFiles()?.size ?: 0} files).")
    }
}

tasks.register("downloadXTEAs") {
    group = "_cache"
    description = "Download XTEA keys for rev 240 from OpenRS2 Archive"
    outputs.upToDateWhen { xteaFile.exists() && xteaFile.length() > 2 }
    doLast {
        if (xteaFile.exists() && xteaFile.length() > 2) {
            println("XTEAs already exist at ${xteaFile.absolutePath}, skipping. Delete to re-download.")
            return@doLast
        }
        xteaFile.parentFile.mkdirs()
        println("Downloading XTEA keys from OpenRS2 Archive (ID $openrs2CacheId)...")
        URI("https://archive.openrs2.org/caches/$openrs2Scope/$openrs2CacheId/keys.json").toURL().openStream().use { input ->
            xteaFile.outputStream().use { output -> input.copyTo(output) }
        }
        println("XTEA keys saved (${xteaFile.length() / 1024} KB).")
    }
}

tasks.register("setupCache") {
    group = "_cache"
    description = "Download cache + XTEAs from OpenRS2 Archive in one step"
    dependsOn("downloadCache", "downloadXTEAs")
}