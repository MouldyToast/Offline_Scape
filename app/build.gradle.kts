plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

fun findContentModules(): List<Project> =
    project(":content").subprojects.filter { it.buildFile.exists() }

fun findToolModules(): List<Project> =
    project(":tools").subprojects.filter { it.buildFile.exists() }

dependencies {
    runtimeOnly(libs.hotswap.agent.core)

    runtimeOnly(libs.slf4j.api)
    runtimeOnly(libs.logback.classic)

    runtimeOnly(projects.core)

    runtimeOnly(projects.coreRestricted)



    runtimeOnly(projects.plugins.interfaces.teleports)
    runtimeOnly(projects.plugins.item.actions.deathItems)
    runtimeOnly(projects.plugins.item.actions)

    runtimeOnly(projects.plugins.rewards)
    runtimeOnly(projects.plugins.shops)
    runtimeOnly(projects.plugins.spawns.custom)
    runtimeOnly(projects.plugins.spawns.region10xxx)
    runtimeOnly(projects.plugins.spawns.region11xxx)
    runtimeOnly(projects.plugins.spawns.region12xxx)
    runtimeOnly(projects.plugins.spawns.region13xxx)
    runtimeOnly(projects.plugins.spawns.region14xxx)
    runtimeOnly(projects.plugins.spawns.region15xxx)
    runtimeOnly(projects.plugins.spawns.region16xxx)
    runtimeOnly(projects.plugins.spawns.region17xxx)
    runtimeOnly(projects.plugins.spawns.region4xxx)
    runtimeOnly(projects.plugins.spawns.region5xxx)
    runtimeOnly(projects.plugins.spawns.region6xxx)
    runtimeOnly(projects.plugins.spawns.region7xxx)
    runtimeOnly(projects.plugins.spawns.region8xxx)
    runtimeOnly(projects.plugins.spawns.region9xxx)

    // Auto-discovered content and tools modules
    findContentModules().forEach { runtimeOnly(it) }
    findToolModules().forEach { runtimeOnly(it) }
}

val defaultMainClass = "com.zenyte.Main"

application {
    applicationName = "near-reality-server"
    mainClass = defaultMainClass
}

val defaultJvmArgs = arrayOf(
    "--enable-preview",
    "--enable-native-access=ALL-UNNAMED",

    "--add-exports=java.base/jdk.internal.ref=ALL-UNNAMED",
    "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
    "--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED",
    "--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
    "--add-opens=jdk.compiler/com.sun.tools.javac=ALL-UNNAMED",
    "--add-opens=java.base/java.lang=ALL-UNNAMED",
    "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
    "--add-opens=java.base/java.io=ALL-UNNAMED",
    "--add-opens=java.base/java.util=ALL-UNNAMED",

    "-Xms1g",
    "-XX:AutoBoxCacheMax=65535",
    "--add-opens=java.base/jdk.internal.vm=ALL-UNNAMED",
    "-XX:TieredStopAtLevel=1",
    "-XX:CompileThreshold=1500",
    "-Dslf4j.internal.verbosity=warn",

    "-XX:-OmitStackTraceInFastThrow",

    "-Dio.netty.tryReflectionSetAccessible=true",

    "--add-opens=java.base/java.time=ALL-UNNAMED",
    "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
    "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
    "--add-opens=java.base/java.io=ALL-UNNAMED",
    "--add-opens=jdk.unsupported/sun.misc=ALL-UNNAMED",
    "--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED",

    "--add-opens=java.base/java.nio=ALL-UNNAMED",
    "--add-opens=java.base/java.security=ALL-UNNAMED",
    "--add-opens=java.base/sun.security.action=ALL-UNNAMED",
    "--add-opens=jdk.naming.rmi/com.sun.jndi.rmi.registry=ALL-UNNAMED",
    "--add-opens=java.base/sun.net=ALL-UNNAMED",
)

tasks.register<JavaExec>("runPluginScanner") {
    group = "_nr_data"
    mainClass.set("com.zenyte.plugins.PluginScanner")
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = layout.projectDirectory.dir("../").asFile
    jvmArgs(*defaultJvmArgs)
}

tasks.register<JavaExec>("generateFlatCache") {
    group = "_nr_data"
    mainClass.set("org.jire.runecache.GenerateFlatCache")
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = layout.projectDirectory.dir("../").asFile
    jvmArgs(*defaultJvmArgs)
}

tasks.register<JavaExec>("generateWebJs5ResponseDirectory") {
    group = "_nr_data"
    mainClass.set("org.jire.runecache.GenerateWebJs5ResponseDirectory")
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = layout.projectDirectory.dir("../").asFile
    jvmArgs(*defaultJvmArgs)
}

tasks.register<JavaExec>("runDev") {
    group = "_nr_dev"
    mainClass.set(defaultMainClass)
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("localhost")
    workingDir = layout.projectDirectory.dir("../").asFile
    jvmArgs(*defaultJvmArgs)
}

tasks.register<JavaExec>("runBeta") {
    group = "_nr"
    mainClass.set(defaultMainClass)
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("beta")
    workingDir = layout.projectDirectory.dir("../").asFile
    jvmArgs(*defaultJvmArgs)
}

tasks.register<JavaExec>("runProduction") {
    group = "_nr"
    mainClass.set(defaultMainClass)
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("main")
    workingDir = layout.projectDirectory.dir("../").asFile
    jvmArgs(*defaultJvmArgs)
}


//tasks.register<JavaExec>("generateCache") {
//    group = "_nr_data"
//    mainClass.set("mgi.tools.parser.TypeParser")
//    classpath = sourceSets["main"].runtimeClasspath
//    args = listOf("--unzip", "false")
//    workingDir = layout.projectDirectory.dir("../").asFile
//    jvmArgs(*defaultJvmArgs)
//}

tasks.register<JavaExec>("runHotswap") {
    group = "application"
    description = "Run server with HotSwap Agent for live code reload"
    mainClass.set(defaultMainClass)
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = layout.projectDirectory.dir("../").asFile
    jvmArgs(defaultJvmArgs.toList())
    jvmArgs("-XX:HotswapAgent=fatjar")
}

tasks.named<Zip>("distZip").configure {
    enabled = false
}

tasks.named<Tar>("distTar").configure {
    enabled = false
}
