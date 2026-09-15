# PROJECT_MAP.md

> Living reference document for the Offline\_Scape (rev-228) codebase.
> Generated from source investigation — not inferred from names.
> Last updated: 2026-09-15, at commit `e5c78ef6`.

---

## 1. Module Overview

Gradle multi-module build. Kotlin 2.2.0, JDK 21, Java with `--enable-preview`. Root project name is `nr-228` (branding remnant, see Landmines).

Module dependency spine: `api ← core-model ← (cache, engine)`; `engine` depends on `threads`, `cache`, `core-model` (all `api` scope). Every `content/*` and `tools/*` module is `compileOnly(projects.engine)` plus whichever `scripts:*` DSL modules it needs. `app` depends only on `engine` plus all auto-discovered content/tools modules as `runtimeOnly`.

### api/

**Purpose:** Database layer for NR's external API (store, votes, hiscores, sanctions, logs, user accounts). Pure Kotlin, standalone.

Key packages: `org.jesse.api` — Exposed ORM tables, DAO extensions, Ktor client, kotlinx.serialization.

Size: 78 Kotlin files, ~6,400 lines. Zero Java.

Depends on: nothing (standalone). `core-model` exposes it via `api(projects.api)`.

Notable: NR infrastructure (credit store, vote shop, sanctions DB) — cull candidate once the in-engine store/vote services go.

### app/

**Purpose:** Application entry point wrapper. Gradle run tasks only; no source.

Key tasks: `runDev`, `runBeta`, `runProduction`, `runPluginScanner`, `runHotswap`. (`generateFlatCache` and `generateWebJs5ResponseDirectory` are dead — see Landmines. `generateCache` is commented out.)

Auto-discovery: `findContentModules()`/`findToolModules()` walk `:content` and `:tools` subprojects and add every one with a `build.gradle.kts` as `runtimeOnly`.

Entry point: `org.jesse.Main` (in `engine`). `applicationName` is `"near-reality-server"` (branding remnant).

### cache/

**Purpose:** Cache loading, definition decoding, XTEA keys, RSProt API surface, event bus. The server serves the vanilla OpenRS2 cache unmodified — there is no cache build pipeline.

Key packages:
- `mgi.types` — Cache definition decoders (items, NPCs, objects, animations, components, enums, varbits, params, structs, sprites, model draw code). 105 files.
- `org.jesse.cache.interfaces.teleports` — teleport category/destination registry + builder DSL. Feeds the runtime teleport menu server-side.
- `org.jesse.cache_tool` — cache utilities (CacheTool, Archive, XTEA, sprite/model dumpers).
- `org.jesse.game.world.region` — `XTEALoader`, `MapUtils`, `Regions`.
- `org.jesse.plugins.PluginManager` — the static event bus lives here, not in engine.
- `org.jesse.CacheManager` (108 lines).
- `org.jesse.ContentConstants` — `SERVER_NAME = "Offline_scape"`, plus `CASTLE_WARS`, `CONSTRUCTION`, `HALLOWEEN`, `SPAWN_MODE` toggles.
- `net.runelite` (3 files — verified extensions of the runelite-cache artifact), `cloud.rsps` (2 files).

Size: 166 Java files (~47,600 lines), 47 Kotlin files (~2,600 lines).

Depends on: `util`, `core-model`. Exposes RuneLite cache lib, RSProt 228 API, Netty, BouncyCastle as `api`.

Tasks: `setupCache`, `downloadCache`, `downloadXTEAs`, `resetCache`. `openrs2CacheId = "2043"` pins the OpenRS2 build.

### engine/

**Purpose:** Server engine — world simulation, player/NPC entities, combat, skills, networking (RSProt), packet handling, plugin system, plus content not yet extracted to `content/`.

Key packages (all `org.jesse` unless noted):
- `game` — `GameConstants` (REVISION = 228, REGISTRATION_LOCATION = Tutorial Island 3093,3107,0), `GameInterface`, `GameLoader`; `GameToggles.java` (4 flags: `BARROWS_OLD_DISABLED`, `NIGHTMARE_SHURA_PREREQ`, `NEW_PLAYER_BROADCAST_DISABLED`, `COX_MASSES_ENABLED`).
- `game.world.entity.player` — `Player.java` (5,377 lines), login, skills, containers, `GameCommands.java` (2,622 lines).
- `game.world.entity.npc` — `NPC.java` (2,421 lines), combat scripts, spawn loader, combat defs.
- `game.world.region` — `Region.java` (715), `DynamicRegion.java` (329), `GlobalAreaManager`.
- `game.content.skills` — all skill implementations (still in engine, not `content/`).
- `game.content.commands` — Developer (665) / Administrator (570) / Player (120) commands.
- `game.net.packet` / `game.packet` — `PacketDispatcher.java` (681), `GameMessageConsumers.kt`, `PacketSender.kt` (4,165).
- `game.model.ui` — `InterfaceHandler.java` (495) + 91 production interface implementations in `testinterfaces/` (name is legacy; NOT test code).
- `plugins` — `PluginScanner.kt` (164), `PluginLoader.kt` (90).
- `network` — `NetworkServiceFactory`, `BootstrapFactory`, `Js5Info`.
- `api` — store/vote/sanction/user/item API services (NR infrastructure — 22 files).
- `cloud.rsps` (46 files, rename deferred by decision) — `rsprot/` integration layer (Session, connection handler, login blocks, auth, ping, reconnect), hiscores + worlds Ktor servers, HAProxy, `PluginRoot` scanner marker, misc util.
- `game.content` NR systems still present: `donation` (3 files), `killstreak` (3), `lootkeys` (6), `breaches` (25), `vote` (1), `preset` (4), `imbue/DisimbueItemHandler.kt`, `scoreboard` (5 files), `xamphur/PhantomHandCorruption.kt`.

Size: 3,168 Java files (~305,600 lines), 421 Kotlin files (~33,600 lines). By far the largest module.

Depends on: `threads`, `cache`, `core-model` (api scope).

### core-model/

**Purpose:** Shared data models + the three ID constant files. 100% Kotlin.

Key packages: `org.jesse.game.item.ids.ItemId` (14,115 lines), `org.jesse.game.npc.ids.NpcId` (11,751), `org.jesse.game.obj.ids.ObjectId` (26,110) — top-level `const val` with `@file:JvmName` for Java interop. Also container/item/location models.

Size: 24 Kotlin files, ~53,900 lines (the three ID files are ~52,000 of that).

Depends on: `api` (api scope).

### threads/

**Purpose:** Main game thread definition, CPU affinity. 2 Kotlin files, 136 lines.

### util/

**Purpose:** Shared utilities — logging, string utils, GSON helpers, DB pooling.

Key packages: `org.jesse.utils`, `org.jesse.util`, `com.runespawn` (2 files — NR cull target).

Size: 17 Java (~2,300 lines), 17 Kotlin (~760 lines).

### scripts/

**Purpose:** Kotlin DSL base classes for content — NPC spawns/drops/definitions, shops, item actions/definitions/equip, interfaces, object actions, player actions, ground items.

Submodules (18): `common`, `ground-items`, `interfaces`(+`:user`), `item`(+`actions`,`definitions`,`equip`), `npc`(+`actions`,`definitions`,`drops`,`spawns`), `object`(+`actions`), `player`(+`actions`), `shops`.

Size: 99 Kotlin files, ~3,500 lines. Packages `org.jesse.scripts.*`. Content modules consume these DSLs; the DSL modules themselves are `compileOnly(projects.engine)`.

### content/

**Purpose:** Game content as auto-discovered Gradle modules. Any directory under `content/` with a `build.gradle.kts` becomes a module automatically — no settings edits needed.

**74 modules.** Standard build file: `compileOnly(projects.engine)` plus needed `scripts:*` DSLs.

| Group | Modules |
|---|---|
| `areas/` | city/prifddinas, ferox-enclave, kebos, stronghold-of-security, taverley, waterbirth-island, wilderness |
| `bosses/` | abyssal-sire, bryophyta, dt2, gauntlet, mage-arena-ii, nex, nightmare, obor, skotizo, thermonuclear-smoke-devil, tormented-demon, vorkath, zalcano, zulrah |
| `raids/` | colosseum (active dev), cox, toa, tob |
| `minigames/` | inferno, party-room, pvm-arena, pyramid-plunder, tears-of-guthix |
| `interfaces/` | character-design, collection-log, death, slayer, teleports, world-switcher |
| `skills/` | agility/courses, agility/prifddinas-rooftop, agility/pyramid, agility/shortcuts |
| `drops/` | processors, tables |
| `generic/` | one merged module + item-plugins, npc-plugins, object-plugins, floor-item-plugins |
| `spawns/` | region/region{4..17}xxx (911 spawn DSL files), plus `other/spawns/misc` |
| `other/` | death-mechanics, group-ironman, items/{avernic-defender, elemental-tiara, muddy-chest, neitiznot-faceguard, staff-of-balance}, larrans-key, migrations, rewards, **shops (275 shop DSL files)** |
| `travel/` | magic carpet, master scroll book, item transportation |
| `events/`, `quest/` | empty placeholders (`.gitkeep` only) |

Size: 949 Java files (~79,000 lines), 1,785 Kotlin files (~92,500 lines).

### tools/

**Purpose:** Server tooling as auto-discovered modules: `analyzer`, `backups`, `updater` (3 modules, 19 Kotlin files, ~1,400 lines). `discord/` has source but no build file — disabled.

### data/ (runtime data, not a Gradle module)

See Section 4.

---

## 2. Key File Locations

### Engine & Boot

| File | Lines | Purpose |
|---|---|---|
| `engine/src/main/kotlin/org/jesse/Main.kt` | 282 | entry point, boot orchestrator |
| `engine/src/main/java/org/jesse/game/GameLoader.java` | 25 | loads cache + defs via CacheManager |
| `cache/src/main/java/org/jesse/CacheManager.java` | 108 | cache init |
| `cache/src/main/java/mgi/types/Definitions.java` | 125 | definition load order |
| `engine/src/main/java/org/jesse/game/GameConstants.java` | — | REVISION=228, REGISTRATION_LOCATION |
| `engine/src/main/java/org/jesse/GameToggles.java` | — | 4 feature flags |
| `cache/src/main/java/org/jesse/ContentConstants.java` | — | SERVER_NAME, feature toggles |
| `engine/.../world/World.java` | 1,886 | world simulation |
| `engine/.../world/entity/player/Player.java` | 5,377 | player entity (god class) |
| `engine/.../world/entity/npc/NPC.java` | 2,421 | NPC entity |

### Login, Commands, Combat

| File | Lines | Purpose |
|---|---|---|
| `engine/.../player/login/LoginManager.java` | 778 | login |
| `engine/.../player/GameCommands.java` | 2,622 | command dispatcher |
| `engine/.../commands/DeveloperCommands.kt` | 665 | dev commands |
| `engine/.../commands/AdministratorCommands.kt` | 570 | admin commands |
| `engine/.../commands/PlayerCommands.kt` | 120 | player commands |
| `engine/.../combat/PlayerCombat.java` | 1,422 | player combat |
| `engine/.../combat/SpecialAttack.java` | 1,559 | special attacks |
| `engine/.../npc/combat/CombatScript.java` | — | NPC combat base |

### Definitions & Cache

| File | Lines | Purpose |
|---|---|---|
| `cache/.../config/items/ItemDefinitions.java` | 2,361 | item defs |
| `cache/.../config/npcs/NPCDefinitions.java` | 1,484 | NPC defs |
| `cache/.../config/ObjectDefinitions.java` | — | object defs |
| `cache/.../component/ComponentDefinitions.java` | 1,881 | interface component defs |
| `cache/.../world/region/XTEALoader.java` | — | XTEA key loading |

### ID Constants

| File | Lines | Notes |
|---|---|---|
| `core-model/.../obj/ids/ObjectId.kt` | 26,110 | generated, don't hand-edit |
| `core-model/.../item/ids/ItemId.kt` | 14,115 | generated |
| `core-model/.../npc/ids/NpcId.kt` | 11,751 | generated |

### Networking & Packets

| File | Lines | Purpose |
|---|---|---|
| `engine/.../net/packet/PacketDispatcher.java` | 681 | packet dispatch |
| `engine/.../packet/PacketSender.kt` | 4,165 | outgoing packets |
| `engine/.../net/packet/GameMessageConsumers.kt` | — | incoming handler registration |
| `engine/.../cloud/rsps/rsprot/*.kt` | 7 files | RSProt session/auth/login layer |
| `engine/.../network/NetworkServiceFactory.kt` | — | network bootstrap |

### Plugin System

| File | Lines | Purpose |
|---|---|---|
| `engine/.../plugins/PluginScanner.kt` | 164 | writes `data/plugins.dat` |
| `engine/.../plugins/PluginLoader.kt` | 90 | Class.forName loading |
| `cache/.../plugins/PluginManager.java` | — | static event bus |

### Teleports, Shops, Spawns

| File | Count/Lines | Purpose |
|---|---|---|
| `cache/.../teleports/` | 8 category files + TeleportsList + builder DSL | teleport destination registry (runtime, no packer) |
| `content/interfaces/teleports/TeleportMenu.kt` | — | teleport dialog/interaction |
| `scripts/shops/.../ShopScript.kt` | 53 | shop DSL |
| `content/other/shops/` | 275 files | shop definitions |
| `engine/.../npc/spawns/NPCSpawnLoader.java` | 219 | NPC spawn loading |
| `content/spawns/region/region{N}xxx/` | 911 files | per-region spawn DSL |
| `cache/data/npcs/combat/*.npc.json` | 3,039 | per-NPC combat defs |
| `cache/data/npcs/drops/*.drops.json` | 1,798 | per-NPC drop tables |

### Colosseum (active development)

`content/raids/colosseum/` — 28 source files. ColosseumInstance, wave system, SolHeredit, MinimusNpc, combat scripts (Manticore, FremennikWarband, JavelinColossus), intermission/reward-chest/scoreboard interfaces, cutscene, statistics.

---

## 3. Language & Package Audit

### Java vs Kotlin split

| Module | Java files | Java lines | Kotlin files | Kotlin lines |
|---|---|---|---|---|
| api | 0 | 0 | 78 | 6,404 |
| cache | 166 | 47,630 | 47 | 2,599 |
| content | 949 | 79,000 | 1,785 | 92,485 |
| core-model | 0 | 0 | 24 | 53,867 |
| engine | 3,168 | 305,626 | 421 | 33,634 |
| scripts | 0 | 0 | 99 | 3,497 |
| threads | 0 | 0 | 2 | 136 |
| tools | 0 | 0 | 19 | 1,400 |
| util | 17 | 2,298 | 17 | 764 |
| **Total** | **4,300** | **~435K** | **2,492** | **~195K** |

### Package namespaces

| Namespace | Files | Location | Status |
|---|---|---|---|
| `org.jesse` | ~4,628 | everywhere | Unified root |
| `cloud.rsps` | 50 | engine (46), cache (2), content (2) | Rename deferred. RSProt layer, hiscores/worlds servers, `PluginRoot` marker |
| `mgi` | 105 | cache only | Deferred behind OR2 adoption decision (likely replaced, not renamed) |
| `net.runelite` | 3 | cache only | Keep — verified extensions of the runelite-cache artifact |
| `com.runespawn` | 2 | util only | NR cull target |

No `com.zenyte` or `com.near_reality` strings remain in source.

---

## 4. Configuration & Data Files

### Repo root
| File | Purpose |
|---|---|
| `worlds.json` | World config — localhost world 101, port 43594, `verifyPasswords: false`. Contains a `nearRealityGuild` key (branding remnant). |
| `PROVENANCE_nr.txt` | Pre-collapse `com.near_reality` file listing — NR-cull identification map. |
| `CUSTOM_ITEM_IDS.txt` | Manifest of the custom item layer (1,398 lines). |
| `.data/gamevals/*.rscm` | Rev-228 RSCM symbolic-name tables (21 files). |
| `.data/gamevals-binary/` | `gamevals.dat` + `gamevals_generated.dat` — binary RSCM tables. |
| `.run/` | IntelliJ run configurations. |

### `data/` directory
`animations.json`, `components.json`, `enums.json`, `structs.json`, `music.json`, `item_variations.json`, `items/` (runtime item overrides + requirements), `examines/`, `areatypes/` (binary combat-zone maps), `osrsbox-db/`, `rewards/`, `referrals/` (orphaned — engine code deleted), `show_objects/`, `cs2_raws/`, `private.key` (RSA), `REWARD_SCHEMA.json`, stronghold questions, `diary_info.json`, `docs/INCOMPLETE_COMBAT_ACHIEVEMENTS.md`. `data/map/` and `data/raids/` are empty. `data/plugins.dat` is generated and gitignored.

### `cache/data/`
Gitignored cache at `cache/data/cache/` (from OpenRS2 ID 2043 via `setupCache`), `objects/xteas.json`, door definitions, 3,039 combat JSONs, 1,798 drop JSONs.

### Content format reference

| Content type | Format | Location |
|---|---|---|
| NPC spawns | Kotlin DSL extending `NPCSpawnsScript` | `content/spawns/region/region{N}xxx/*.kt` |
| Shops | Kotlin DSL extending `ShopScript` | `content/other/shops/*.kt` |
| Drop tables | JSON per-NPC | `cache/data/npcs/drops/` |
| NPC combat defs | JSON per-NPC | `cache/data/npcs/combat/` |
| Teleport destinations | Kotlin DSL | `cache/src/.../teleports/categories/*.kt` |

---

## 5. Build & Run

| Task | Purpose |
|---|---|
| `./gradlew :cache:setupCache` | Download rev-228 cache + XTEAs from OpenRS2 (ID 2043) — one-time |
| `./gradlew :cache:resetCache` | Wipe and re-extract from local zip (no download) |
| `./gradlew :app:runPluginScanner` | Scan for plugin classes → `data/plugins.dat` |
| `./gradlew :app:runDev` | Start server (localhost, world 101, no password verify) |
| `./gradlew :app:runHotswap` | Run with HotSwap Agent |
| `./gradlew clean compileJava compileKotlin` | Compile check |

Pipeline: `setupCache` → `runPluginScanner` → `runDev`. The vanilla cache is served unmodified.

Re-run `runPluginScanner` after adding/removing plugin classes.

Client: RSProx with `jav_local_228.ws` config.

**Adding a content module:** create `content/<group>/<name>/build.gradle.kts` with `compileOnly(projects.engine)` — auto-discovered. Then `runPluginScanner`.

---

## 6. Architecture Patterns

### Plugin discovery
`PluginScanner` scans three package roots via marker classes: `Main::class` (org.jesse), `NearReality::class` (also org.jesse — redundant, kept as marker), `PluginRoot::class` (cloud.rsps). Writes `data/plugins.dat`; `PluginLoader` reads it and loads via `Class.forName`.

### Event bus
`PluginManager` (cache module, `org.jesse.plugins`) — static enum-singleton bus. `@Subscribe` static methods discovered at scan time; `PluginManager.post(event)` at runtime.

### Content module pattern
`compileOnly(projects.engine)` means content compiles against engine but is only wired in at runtime through `app`'s runtimeOnly aggregation + plugin scanning. Content never appears on engine's compile classpath.

### Interfaces, combat scripts, definitions
`GameInterface` enum + `InterfaceHandler` (495 lines) + 91 implementations in `testinterfaces/`; NPC combat scripts extend `CombatScript` registered via `CombatScriptsHandler`; cache decoders in `mgi.types` with runtime JSON overrides from `data/items/`.

### Typed player attributes
Use the existing `AttributesExt.kt` delegate system (`persistentAttribute()`/`attribute()`) over the `Map<String, Object>` storage. Do not introduce OpenRune's `AttributeMap`/`AttributeKey`.

---

## 7. Known Landmines

### Deliberate shims — do not "fix"
- **Persisted-class embargo** — never convert Gson-persisted classes (Player + ~20 content classes) to Kotlin until serialization migration. Gson's Unsafe allocation ignores Kotlin null-safety → runtime NPEs on old saves with no compile error.

### Dead code
- `app` tasks `generateFlatCache` / `generateWebJs5ResponseDirectory` — reference `org.jire.runecache.*` classes that exist nowhere. Dead, delete candidates.
- `tools/discord/` — source present, no build file, never compiled.
- `content/events/`, `content/quest/` — empty `.gitkeep` placeholders.
- `data/referrals/` — orphaned runtime directory, engine code deleted.

### Mechanical debris
- Two directories named with dots instead of nested paths: `content/bosses/nex/src/main/kotlin/com.near_reality.plugins.spawns.nex/` and `content/other/larrans-key/src/main/java/com.zenyte.game.content.larranskey/`. Files inside declare correct `org.jesse.*` packages and compile, but the dirs should be renamed.
- `NearReality.kt` marker object — redundant with `Main` as a scan root (same package). Removable once PluginScanner's root list is simplified.

### Branding remnants
- `rootProject.name = "nr-228"` (settings.gradle.kts)
- `applicationName = "near-reality-server"` (app/build.gradle.kts)
- `worlds.json` `nearRealityGuild` field
- `NearRealityLogger` (used in 155 files)
- 5 in-game "Zenyte" branding classes (ZenyteGuide/ZenyteTeleporter/ZenytePortal + 2 structures). **Never bulk-replace "Zenyte"** — it's also the real OSRS gem.

### Misleading names
- `testinterfaces/` — 91 production interface implementations, not tests.
- `plugins.dat` — generated binary, never hand-edited.

### Reflection / string-based loading that breaks on rename
1. `PluginLoader` — `Class.forName` from `plugins.dat`; re-run `runPluginScanner` after any class moves.
2. `ControllerManager` — `Class.forName` on persisted controller names (plus the shim above).
3. `GodwarsBossDoorObject` / `GodwarsInstancePortal` — `Class.forName` boss instance creation.
4. `AbstractTOAManager` — reflective encounter loading (`content/raids/toa`).
5. `PlayerMigrationManager` — ClassGraph discovery (`content/other/migrations`).
6. ~14 `*Compilation.kt` files in `scripts/` feed FQCN strings to the `.kts` compiler `defaultImports` — fail at **runtime**, not build. Keep in sync on any class moves.

### Shell-hostile filenames
275 shop files in `content/other/shops/` include spaces and apostrophes. Batch tooling must be null-safe Python, not bash loops.

### J2K gotcha
Static `@Subscribe` methods becoming companion members without `@JvmStatic` silently unregister handlers — any batch conversion needs `runPluginScanner` + boot-log diff.

### Largest files

| File | Lines |
|---|---|
| `core-model/.../obj/ids/ObjectId.kt` | 26,110 |
| `core-model/.../item/ids/ItemId.kt` | 14,115 |
| `core-model/.../npc/ids/NpcId.kt` | 11,751 |
| `engine/.../player/Player.java` | 5,377 |
| `engine/.../packet/PacketSender.kt` | 4,165 |
| `cache/.../utils/MapLocations.java` | 3,758 |
| `cache/mgi/.../model/ModelData.java` | 2,919 |
| `engine/.../player/GameCommands.java` | 2,622 |
| `cache/mgi/.../Rasterizer3D.java` | 2,456 |
| `engine/.../npc/NPC.java` | 2,421 |
| `cache/mgi/.../ItemDefinitions.java` | 2,361 |
| `engine/.../World.java` | 1,886 |
| `cache/mgi/.../ComponentDefinitions.java` | 1,881 |
| `content/.../dt2/.../WhispererNPC.kt` | 1,866 |
| `engine/.../slayer/RegularTask.kt` | 1,827 |
| `api/.../util/BCrypt.kt` | 1,746 |
| `engine/.../clues/EmoteClue.java` | 1,699 |
| `engine/.../combat/SpecialAttack.java` | 1,559 |
| `content/.../nex/.../NexNPC.java` | 1,503 |
| `cache/.../efficientarea/Vector.java` | 1,471 |
| `content/.../nightmare/.../BaseNightmareNPC.java` | 1,469 |
| `engine/.../combat/PlayerCombat.java` | 1,422 |
