enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven(url = "https://maven.rsps.cloud/")
        maven(url = "https://repo.runelite.net/") {
            content {
                includeGroup("net.runelite")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"

    id("org.jetbrains.kotlin.jvm") version "2.2.0" apply false
}

include(":api")

include(
    ":threads",
    ":util",
    ":core-model",
    ":cache",
    ":core",
)

include(
    ":scripts:common",

    ":scripts:ground-items",

    ":scripts:interfaces",
    ":scripts:interfaces:user",

    ":scripts:item",
    ":scripts:item:actions",
    ":scripts:item:definitions",
    ":scripts:item:equip",

    ":scripts:npc",
    ":scripts:npc:actions",
    ":scripts:npc:definitions",
    ":scripts:npc:drops",
    ":scripts:npc:spawns",

    ":scripts:object",
    ":scripts:object:actions",

    ":scripts:player",
    ":scripts:player:actions",

    ":scripts:shops",
)

include(
    ":core-restricted",
)

include(
    ":plugins:area:ferox_enclave",
    ":plugins:area:osnr_home:npc",
    ":plugins:area:osnr_home:obj",
    ":plugins:area:osnr_home",
    ":plugins:boss:zalcano",
    ":plugins:elven",
    ":plugins:ground-items",
    ":plugins:interfaces:characterdesign",
    ":plugins:interfaces:death",
    ":plugins:interfaces:slayer",
    ":plugins:interfaces:teleports",
    ":plugins:interfaces:worldswitcher",
    ":plugins:item:actions:death-items",
    ":plugins:item:actions",
    ":plugins:item:cosmetics",
    ":plugins:item:customs",
    ":plugins:item:staff-of-balance",
    ":plugins:larranskey",
    ":plugins:npc:drops",
    ":plugins:object",
    ":plugins:rewards",
    ":plugins:shops",
    ":plugins:spawns:custom",
    ":plugins:spawns:nex",
    ":plugins:spawns:region10xxx",
    ":plugins:spawns:region11xxx",
    ":plugins:spawns:region12xxx",
    ":plugins:spawns:region13xxx",
    ":plugins:spawns:region14xxx",
    ":plugins:spawns:region15xxx",
    ":plugins:spawns:region16xxx",
    ":plugins:spawns:region17xxx",
    ":plugins:spawns:region4xxx",
    ":plugins:spawns:region5xxx",
    ":plugins:spawns:region6xxx",
    ":plugins:spawns:region7xxx",
    ":plugins:spawns:region8xxx",
    ":plugins:spawns:region9xxx",
)

// excluded
include(
    ":plugins:excluded",
    ":plugins:excluded:boss:abyssalsire",
    ":plugins:excluded:boss:nex",
    ":plugins:excluded:boss:nightmare",
    ":plugins:excluded:gauntlet",
    ":plugins:excluded:group-ironman",
    ":plugins:excluded:itemonitem:impl",
    ":plugins:excluded:itemonitem:neitiznot_faceguard",
    ":plugins:excluded:itemonobject:elemental_tiara",
    ":plugins:excluded:muddychest",
    ":plugins:excluded:skills:agility:priffdinasrooftop",
    ":plugins:excluded:theatreofblood",
    ":plugins:excluded:tools:analyzer",
    ":plugins:excluded:tools:backups",
    // ":plugins:excluded:tools:discord", // Discord base classes stripped, bot disabled
    ":plugins:excluded:tools:updater",
)

include(":app")

rootProject.name = "nr-228"
