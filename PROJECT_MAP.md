# PROJECT_MAP.md

> Living reference document for the Offline\_Scape (rev-228) codebase.
> Generated from source investigation — not inferred from names.
> Last updated: 2026-09-08, at commit `f1914af3` (merge of PR #26, the org.jesse package collapse).

---

## 0. What Changed Since the Last Map (2026-08-29)

The repo has been through a 167-commit cleanup campaign since the Main reset. The previous map is structurally obsolete. Headline changes:

1. **`plugins/` is gone.** The entire module tree (including `excluded/`) was broken up and moved into auto-discovered `content/` and `tools/` modules. The `plugins/` directory was deleted (`132ce7f3`).
2. **`core-restricted/` is gone.** Colosseum + ToA → `content/raids/`, DT2/Origins/Tormented Demon → `content/bosses/`, PvM Arena/Tournament/Chaos Key → `content/minigames/`, wilderness → `content/areas/`, migrations → `content/other/`. Empty module removed (`00a4de8a`).
3. **`core/` → `engine/`** (`45af1863`, PR #25).
4. **The org.jesse package collapse executed** (`a7a87330`, PR #26). `com.zenyte` and `com.near_reality` no longer exist in source — everything is `org.jesse.*`. `PROVENANCE_nr.txt` at repo root records every pre-collapse `com.near_reality` file path (the NR-cull map).
5. **ID constants migrated.** `ItemId`/`NpcId`/`ObjectId` are now Kotlin top-level `const val` files in `core-model` under `org.jesse.game.{item,npc,obj}.ids` (wildcard-importable via `@file:JvmName`). All `Custom*Id` files merged in and deleted. `core-model` is now 100% Kotlin.
6. **Major NR systems removed:** boon/perk/remnant system, Well of Goodwill, Wilderness Vault, NR custom home area (spawn moved to real Tutorial Island; Edgeville/GE spawns restored), donator islands + NR317 donator zones, Xamphur world boss + world boss arena, custom tutorial island, god bows/chaotics/polypore/custom whips/slayer helm effects, Korasi's sword, gambling (dice bag + flower poker), Magic Storage Unit, SlayerStatues (RS3), NR anticheat, donation store interface, solo launch contests, most custom cache packers and their asset directories (christmas/, easter/, pets/, korasi/, dice bag/, halloween/, retired/, exports/ all deleted from `cache/assets/`).
7. **Active new content development:** Fortis Colosseum (`content/raids/colosseum/`) — wave system, Sol Heredit, Manticore/Fremennik Warband/Javelin Colossus combat, intermission loot interface, quiver/ammo logic.
8. **OR2/RSCM prep landed:** `.data/gamevals/` holds rev-228 RSCM symbolic-name tables (`*.rscm`) plus `REPORT.tsv` mapping stats; generated during the OR2 migration prep (`0c17c893`). The old `docs/or2-migration/` directory was later deleted from the repo.

---

## 1. Module Overview

Gradle multi-module build. Kotlin 2.2.0, JDK 21, Java with `--enable-preview`. Root project name is still `nr-228` (branding remnant, see Landmines).

Module dependency spine: `api ← core-model ← (cache, engine)`; `engine` depends on `threads`, `cache`, `core-model` (all `api` scope). Every `content/*` and `tools/*` module is `compileOnly(projects.engine)` plus whichever `scripts:*` DSL modules it needs. `app` depends only on `engine` plus all auto-discovered content/tools modules as `runtimeOnly`.

### api/

**Purpose:** Database layer for NR's external API (store, votes, hiscores, sanctions, logs, user accounts). Pure Kotlin, standalone.

Key packages: `org.jesse.api` — Exposed ORM tables, DAO extensions, Ktor client, kotlinx.serialization.

Size: 78 Kotlin files, ~6,400 lines. Zero Java.

Depends on: nothing (standalone). `core-model` exposes it via `api(projects.api)`.

Notable: This whole module is NR infrastructure (credit store, vote shop, sanctions DB) — a cull candidate once the in-engine store/vote services go.

### app/

**Purpose:** Application entry point wrapper. Gradle run tasks only; no source.

Key tasks: `runDev`, `runBeta`, `runProduction`, `runPluginScanner`, `runHotswap`, `generateFlatCache`, `generateWebJs5ResponseDirectory`. (`generateBoonData` is gone with the boon system; `generateCache` here is commented out — the real one lives in `cache/`.)

Auto-discovery: `findContentModules()`/`findToolModules()` walk `:content` and `:tools` subprojects and add every one with a `build.gradle.kts` as `runtimeOnly`.

Entry point: `org.jesse.Main` (in `engine`). `applicationName` is still `"near-reality-server"` (branding remnant).

### cache/

**Purpose:** Cache loading, definition decoding, cache packing pipeline (TypeParser), XTEA keys, teleport interface packing, RSProt API surface, event bus.

Key packages:
- `mgi.types` — All cache definition decoders (items, NPCs, objects, animations, components, enums, varbits, params, structs, sprites, model draw code). 147 files.
- `mgi.tools.parser` — `TypeParser.java` (1,804 lines), the cache builder pipeline.
- `mgi.custom` — Down from 23 files to 4 (AnimationBase, CustomTeleport, FramePacker, halloween/).
- `org.jesse.cache_tool.packing.custom` — remaining custom packers (post-cull survivors, incl. `TeleportsPacker` chain and `UniversalShopPacker`).
- `org.jesse.cache.interfaces.teleports` — teleport category/destination registry + packer (**permanent keep** — interface 1700 stays by decision).
- `org.jesse.game.world.region` — `XTEALoader`, `MapUtils`, `Regions`.
- `org.jesse.plugins.PluginManager` — the static event bus lives here, not in engine.
- `org.jesse.CacheManager` (108 lines), `org.jesse.ContentConstants` (SERVER_NAME = `"Offline_scape"`).
- `net.runelite` (3 files — verified extensions of the runelite-cache artifact), `org.jire.wmpacker` (2 files), `cloud.rsps` (2 files).

Size: 213 Java files (~54,700 lines), 90 Kotlin files (~9,200 lines).

Depends on: `util`, `core-model`. Exposes RuneLite cache lib, RSProt 228 API, Netty, BouncyCastle as `api`.

Tasks: `generateCache`, `setupCache`, `downloadCache`, `downloadXTEAs`, `resetCache`. `openrs2CacheId = "2043"` in `cache/build.gradle.kts` — the single place the OpenRS2 build is pinned.

### engine/ (formerly core/)

**Purpose:** The server engine — world simulation, player/NPC entities, combat, skills, networking (RSProt), packet handling, plugin system, plus a large amount of content that has not yet been extracted to `content/`.

Key packages (all `org.jesse` unless noted):
- `game` — `GameConstants` (REVISION = 228, REGISTRATION_LOCATION = Tutorial Island 3093,3107,0), `GameInterface`, `GameLoader`; root also holds `GameToggles.java` (feature flags incl. `UNIVERSAL_SHOP_FLOODGATE`, `BH2020_ENABLED`).
- `game.world.entity.player` — `Player.java` (5,629 lines), login, skills, containers, `GameCommands.java` (2,819 lines).
- `game.world.entity.npc` — `NPC.java` (2,424 lines), combat scripts, spawn loader, combat defs.
- `game.world.region` — `Region.java` (715), `DynamicRegion.java` (329), `GlobalAreaManager`.
- `game.content.skills` — all skill implementations (still in engine, not `content/`).
- `game.content.commands` — Developer (748) / Administrator (619) / Player (152) / Custom (113) commands.
- `game.net.packet` / `game.packet` — `PacketDispatcher.java` (682), `GameMessageConsumers.kt`, `PacketSender.kt` (4,165).
- `game.model.ui` — `InterfaceHandler.java` (543) + 86 production interface implementations in `testinterfaces/` (name is legacy; NOT test code).
- `plugins` — `PluginScanner.kt` (164), `PluginLoader.kt` (90), plugin scan types.
- `network` — `NetworkServiceFactory`, `BootstrapFactory`, `Js5Info` (formerly `com.near_reality.network`).
- `api` — store/vote/sanction/user API services (NR infrastructure, still live).
- `cloud.rsps` (50 files, **not** collapsed — deferred by decision) — `rsprot/` integration layer (Session, connection handler, login blocks, auth, ping, reconnect), hiscores + worlds Ktor servers, HAProxy, `PluginRoot` scanner marker, misc util.
- `game.content` also still hosts NR systems awaiting cull: `universalshop`, `middleman`, `storebundle`, `challenges`, `scoreboard`, `imbue`, `donation`, `advent`, `breaches`, `bountyhunter` (BH2020), `killstreak`, `lootkeys`, plus `referral`, presets, and one `xamphur` leftover file (`PhantomHandCorruption.kt`).

Size: 3,460 Java files (~330,900 lines), 488 Kotlin files (~41,600 lines). By far the largest module.

Depends on: `threads`, `cache`, `core-model` (api scope).

### core-model/

**Purpose:** Shared data models + the three ID constant files. Now 100% Kotlin (all Java converted, `89b8941f`).

Key packages: `org.jesse.game.item.ids.ItemId` (14,915 lines), `org.jesse.game.npc.ids.NpcId` (12,026), `org.jesse.game.obj.ids.ObjectId` (26,114) — top-level `const val` with `@file:JvmName` for Java interop; `Custom*Id` counterparts merged in and deleted. Also container/item/location models, `org.jesse.game.content.universalshop` model remnants.

Size: 27 Kotlin files, ~55,600 lines (the three ID files are 53,055 of that).

Depends on: `api` (api scope).

### threads/

**Purpose:** Main game thread definition, CPU affinity. 2 Kotlin files, 136 lines. Unchanged.

### util/

**Purpose:** Shared utilities — logging, string utils, GSON helpers, DB pooling.

Key packages: `org.jesse.utils`, `org.jesse.util`, `com.runespawn` (2 files — dies with the NR cull).

Size: 17 Java (~2,300 lines), 17 Kotlin (~760 lines).

### scripts/

**Purpose:** Kotlin DSL base classes for content — NPC spawns/drops/definitions, shops, item actions/definitions/equip, interfaces, object actions, player actions, ground items.

Submodules (18): `common`, `ground-items`, `interfaces`(+`:user`), `item`(+`actions`,`definitions`,`equip`), `npc`(+`actions`,`definitions`,`drops`,`spawns`), `object`(+`actions`), `player`(+`actions`), `shops`.

Size: 99 Kotlin files, ~3,600 lines. Packages now `org.jesse.scripts.*`. Content modules consume these DSLs; the DSL modules themselves are `compileOnly(projects.engine)`.

### content/ (NEW — replaces plugins/ and core-restricted/)

**Purpose:** Game content as auto-discovered Gradle modules. Any directory under `content/` with a `build.gradle.kts` becomes a module automatically (`settings.gradle.kts` walks the tree) — no settings edits needed to add one.

**79 modules.** Standard build file is one line: `compileOnly(projects.engine)` plus needed `scripts:*` DSLs.

| Group | Modules |
|---|---|
| `areas/` | city/prifddinas, ferox-enclave, kebos, stronghold-of-security, taverley, waterbirth-island, wilderness |
| `bosses/` | abyssal-sire, bryophyta, dt2, gauntlet, mage-arena-ii, nex, nightmare, obor, origins (NR custom, kept for now), skotizo, thermonuclear-smoke-devil, tormented-demon, vorkath, zalcano, zulrah |
| `raids/` | colosseum (active dev), cox, toa, tob |
| `minigames/` | chaoskey (NR), inferno, party-room, pvm-arena (kept by decision), pyramid-plunder, tears-of-guthix, tournament (NR, keep/cull undecided) |
| `interfaces/` | bounty-hunter, character-design, collection-log, death, slayer, teleports, world-switcher |
| `skills/` | agility/courses, agility/prifddinas-rooftop, agility/pyramid, agility/shortcuts |
| `drops/` | processors, tables |
| `generic/` | one merged module + item-plugins, npc-plugins, object-plugins, floor-item-plugins submodules |
| `spawns/` | region/region{4..17}xxx (926 spawn DSL files), plus `other/spawns/misc` |
| `other/` | death-mechanics, group-ironman, items/{avernic-defender, elemental-tiara, muddy-chest, neitiznot-faceguard, staff-of-balance}, larrans-key, migrations, rewards, **shops (275 shop DSL files)**, unused (parking lot: CollectionLogRewardSet, universalshop leftovers) |
| `travel/` | magic carpet, master scroll book, item transportation |
| `events/`, `quest/` | empty placeholders (`.gitkeep` only) |

Size: 954 Java files (~79,600 lines), 1,903 Kotlin files (~102,800 lines).

### tools/ (NEW)

**Purpose:** Server tooling as auto-discovered modules: `analyzer`, `backups`, `updater` (3 modules, 19 Kotlin files, ~1,400 lines). `discord/` has source but **no build file** — still disabled.

### data/ (runtime data, not a Gradle module)

See Section 5.

---

## 2. Key File Locations

### Engine & Boot

| File | Lines | Module |
|---|---|---|
| `engine/src/main/kotlin/org/jesse/Main.kt` | 286 | engine — entry point, boot orchestrator |
| `engine/src/main/java/org/jesse/game/GameLoader.java` | 25 | engine — loads cache + defs via CacheManager |
| `cache/src/main/java/org/jesse/CacheManager.java` | 108 | cache |
| `cache/src/main/java/mgi/types/Definitions.java` | 125 | cache — definition load order |
| `engine/src/main/java/org/jesse/game/GameConstants.java` | — | engine — REVISION=228, REGISTRATION_LOCATION |
| `engine/src/main/java/org/jesse/GameToggles.java` | — | engine — feature flags |
| `cache/src/main/java/org/jesse/ContentConstants.java` | — | cache — SERVER_NAME="Offline_scape" |
| `engine/src/main/java/org/jesse/game/world/World.java` | 1,914 | engine |
| `engine/.../world/entity/player/Player.java` | 5,629 | engine — god class |
| `engine/.../world/entity/npc/NPC.java` | 2,424 | engine |

### Login, Commands, Combat

| File | Lines | Module |
|---|---|---|
| `engine/.../player/login/LoginManager.java` | 784 | engine |
| `engine/.../player/GameCommands.java` | 2,819 | engine — command dispatcher |
| `engine/src/main/kotlin/org/jesse/game/content/commands/{Developer,Administrator,Player,Custom}Commands.kt` | 748/619/152/113 | engine |
| `engine/.../player/action/combat/PlayerCombat.java` | 1,450 | engine |
| `engine/.../player/action/combat/SpecialAttack.java` | 1,560 | engine |
| `engine/.../npc/combat/CombatScript.java` | — | engine — NPC combat base |

### Definitions & Cache

| File | Lines | Module |
|---|---|---|
| `cache/src/main/java/mgi/types/config/items/ItemDefinitions.java` | 2,365 | cache |
| `cache/src/main/java/mgi/types/config/npcs/NPCDefinitions.java` | 1,494 | cache |
| `cache/src/main/java/mgi/types/config/ObjectDefinitions.java` | 1,302 | cache |
| `cache/src/main/java/mgi/types/component/ComponentDefinitions.java` | 1,881 | cache |
| `cache/src/main/java/mgi/tools/parser/TypeParser.java` | 1,804 | cache — cache builder |
| `cache/src/main/java/org/jesse/game/world/region/XTEALoader.java` | — | cache |
| `cache/src/main/java/org/jesse/game/world/region/XTEALoaderPorted.java` | — | **still dead code** |

### ID Constants (all migrated)

| File | Lines | Module |
|---|---|---|
| `core-model/src/main/kotlin/org/jesse/game/obj/ids/ObjectId.kt` | 26,114 | core-model — top-level const val |
| `core-model/src/main/kotlin/org/jesse/game/item/ids/ItemId.kt` | 14,915 | core-model — Custom items merged in |
| `core-model/src/main/kotlin/org/jesse/game/npc/ids/NpcId.kt` | 12,026 | core-model — Custom NPCs merged in |

### Networking & Packets

| File | Lines | Module |
|---|---|---|
| `engine/.../game/net/packet/PacketDispatcher.java` | 682 | engine |
| `engine/src/main/kotlin/org/jesse/game/packet/PacketSender.kt` | 4,165 | engine |
| `engine/src/main/kotlin/org/jesse/game/net/packet/GameMessageConsumers.kt` | — | engine — incoming handler registration |
| `engine/src/main/kotlin/cloud/rsps/rsprot/*.kt` | 7 files | engine — RSProt session/auth/login layer |
| `engine/src/main/kotlin/org/jesse/network/NetworkServiceFactory.kt` | — | engine |

### Plugin System

| File | Lines | Module |
|---|---|---|
| `engine/src/main/kotlin/org/jesse/plugins/PluginScanner.kt` | 164 | engine — writes `data/plugins.dat` |
| `engine/src/main/kotlin/org/jesse/plugins/PluginLoader.kt` | 90 | engine — Class.forName loading |
| `cache/src/main/java/org/jesse/plugins/PluginManager.java` | — | cache — static event bus |

### Teleports, Shops, Spawns

| File | Count/Lines | Module |
|---|---|---|
| `cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/` | 8 category files + TeleportsList + packing/TeleportsPacker.kt | cache — **permanent keep** (interface 1700) |
| `content/interfaces/teleports/` | — | content — teleport dialog/interaction |
| `scripts/shops/.../ShopScript.kt` | 53 | scripts — shop DSL |
| `content/other/shops/` | 275 files | content — shop definitions (many filenames contain spaces/apostrophes) |
| `engine/.../npc/spawns/NPCSpawnLoader.java` | 287 | engine |
| `content/spawns/region/region{N}xxx/` | 926 files | content — per-region spawn DSL |
| `cache/data/npcs/combat/*.npc.json` | 3,046 | per-NPC combat defs |
| `cache/data/npcs/drops/*.drops.json` | 1,798 | per-NPC drop tables |

### Colosseum (active development)

`content/raids/colosseum/src/main/*/org/jesse/game/content/colosseum/` — ColosseumInstance, wave system (WaveData, ColosseumWaveNpc, ColosseumWaveLoot), SolHeredit, MinimusNpc, combat scripts (Manticore, FremennikWarband, JavelinColossus), intermission/reward-chest/scoreboard interfaces, cutscene, statistics.

---

## 3. Language & Package Audit

### Java vs Kotlin split (current)

| Module | Java files | Java lines | Kotlin files | Kotlin lines |
|---|---|---|---|---|
| api | 0 | 0 | 78 | 6,404 |
| cache | 213 | 54,677 | 90 | 9,193 |
| content | 954 | 79,588 | 1,903 | 102,809 |
| core-model | 0 | 0 | 27 | 55,630 |
| engine | 3,460 | 330,854 | 488 | 41,600 |
| scripts | 0 | 0 | 99 | 3,564 |
| threads | 0 | 0 | 2 | 136 |
| tools | 0 | 0 | 19 | 1,400 |
| util | 17 | 2,298 | 17 | 764 |
| **Total** | **4,644** | **~467K** | **2,723** | **~221K** |

(Previous map's line counts were unreliable; these are `wc -l` over `src/` per module. core-model's Kotlin lines are dominated by the three ID files.)

### Package namespaces (post-collapse)

| Namespace | Files | Location | Status |
|---|---|---|---|
| `org.jesse` | ~7,200 | everywhere | The unified root. Done. |
| `cloud.rsps` | 50 | engine (46), cache (2), content (2) | Rename deferred by decision. RSProt layer, hiscores/worlds servers, `PluginRoot` marker. |
| `mgi` | 147 | cache only | Deferred behind the OR2 adoption decision (likely replaced, not renamed). |
| `net.runelite` | 3 | cache only | Keep — verified extensions of the runelite-cache artifact. |
| `org.jire.wmpacker` | 2 | cache only | Rename candidate. |
| `com.runespawn` | 2 | util only | Dies with the NR cull. |

The only remaining `com.zenyte`/`com.near_reality` strings in source are the two `.replace()` calls in `ControllerManager.java` — the **deliberate save-compatibility shim** that rewrites legacy controller FQCNs in old player saves to `org.jesse.` on load. Do not remove until the serialization migration phase.

---

## 4. NR Custom Content Inventory (REMAINING)

What's left to cull, now that the big removals have happened. `PROVENANCE_nr.txt` (repo root) lists every file that was `com.near_reality` pre-collapse — use it to identify NR-origin code now that package names no longer tell you.

### Still in engine (org.jesse.game.content.*)
- **Universal shop** — `universalshop/` + `UniversalShop.kt` (engine), model remnants in core-model and `content/other/unused/`. Kill plan exists (PLAN_remove_universal_shop.md): hardcoded catalog in UniversalShopTable, dead imports in ShopScript files, Emblem Trader tabs replaced by vanilla BH/Blood Money shops. Gated by `GameToggles.UNIVERSAL_SHOP_FLOODGATE`.
- **Store / donation** — `donation/`, `storebundle/`, `org.jesse.api.service.store/`, plus the `api` module's store tables and `data/middleman/`-style runtime dirs.
- **Vote** — `org.jesse.api.service.vote/`.
- **Middleman** — `middleman/` (player-to-player trade escrow).
- **Challenges / Scoreboard** — `challenges/`, `scoreboard/`.
- **Imbue** — `imbue/`.
- **Bounty Hunter 2020** — `bountyhunter/` (`BH2020_ENABLED` toggle); interface already extracted to `content/interfaces/bounty-hunter`.
- **Killstreaks / Loot keys** — `killstreak/`, `lootkeys/` (wilderness NR mechanics).
- **Advent calendar** — `advent/` (Christmas event system; `ContentConstants.CHRISTMAS` toggle).
- **Breaches** — `breaches/` (NR world event).
- **Referrals** — `referral/` packages + `data/referrals/`.
- **Xamphur leftover** — single file `xamphur/PhantomHandCorruption.kt` survived the Xamphur removal.

### In content/ (moved, fate per-feature)
- `bosses/origins/` — NR "Origins" bosses (moved out of core-restricted, not yet culled; `cache/assets/origins/` is 5,951 files).
- `minigames/tournament/` — keep/cull undecided (open checkpoint).
- `minigames/pvm-arena/` — **kept by decision** (real OSRS 2015 Clan Cup activity), slated for rework.
- `minigames/chaoskey/` — NR custom.
- `other/migrations/` — NR player-save migration system (ClassGraph-based).
- `other/unused/` — parking lot for orphaned classes.

### Cache assets remaining (`cache/assets/`)

| Directory | Files | Notes |
|---|---|---|
| `origins/` | 5,951 | Origins cosmetic/boss line — largest remaining custom asset block |
| `packed/` | 3,423 | Pre-packed binary cache data |
| `osnr/` | 2,174 | Mixed NR assets incl. store interface CS2 |
| `sprites/` | 269 | |
| `cs2/` | 245 | |
| `animations/` | 189 | |
| `teleportation/` | 118 | Feeds the teleport menu — part of the permanent keep-set |
| `types/` | 112 TOMLs | Definition overrides (home.toml deleted with NR home) |
| `rebirth/` | 106 | Rebirth cosmetics (partially cleaned) |
| `structs/` 55, `map/` 50, `params/` 31, `interfaces/` 26, `models/` 19, `runespawn/` 5, `inv/` 3 | | |

Deleted since last map: `christmas/`, `easter/`, `pets/`, `korasi/`, `dice bag/`, `halloween/`, `retired/`, `exports/`, `scripts/`.

Custom packers: `mgi.custom` down to 4 files; surviving NR packers live in `org.jesse.cache_tool.packing.custom/`. Removal order rule per feature: server content code → packer invocation → packer class → assets; verify each stage by regenerating from the pristine zip (ROADMAP_custom_cache_removal_v2.md). The teleport menu (interface 1700, TeleportsPacker) is the sole confirmed permanent keep.

---

## 5. Configuration & Data Files

### Repo root
| File | Purpose |
|---|---|
| `worlds.json` | World config — localhost world 101, port 43594, `verifyPasswords: false`. Still contains a `nearRealityGuild` key (branding remnant). |
| `PROVENANCE_nr.txt` | Pre-collapse `com.near_reality` file listing — the NR-cull identification map. |
| `.data/gamevals/*.rscm` + `.data/REPORT.tsv` | Rev-228 RSCM symbolic-name tables generated during OR2 prep (npc: 14,162 names, obj: 30,646; REPORT.tsv tracks fallback quality). |
| `.run/` | IntelliJ run configurations. |

### `data/` directory (largely unchanged)
`animations.json`, `components.json`, `enums.json`, `structs.json`, `music.json`, `item_variations.json`, `items/` (runtime item overrides + requirements), `examines/`, `areatypes/` (binary combat-zone maps), `osrsbox-db/`, `rewards/`, `referrals/`, `show_objects/`, `cs2_raws/`, `private.key` (RSA), `REWARD_SCHEMA.json`, stronghold questions. `data/map/` and `data/raids/` remain empty. `data/plugins.dat` is generated and gitignored.

### `cache/data/`
Gitignored cache at `cache/data/cache/` (from OpenRS2 ID 2043 via `setupCache`), `objects/xteas.json`, door definitions, 3,046 combat JSONs, 1,798 drop JSONs.

### Content format reference

| Content type | Format | Location |
|---|---|---|
| NPC spawns | Kotlin DSL extending `NPCSpawnsScript` | `content/spawns/region/region{N}xxx/*.kt` |
| Shops | Kotlin DSL extending `ShopScript` | `content/other/shops/*.kt` |
| Drop tables | JSON per-NPC | `cache/data/npcs/drops/` |
| NPC combat defs | JSON per-NPC | `cache/data/npcs/combat/` |
| Custom defs | TOML (`inherit=<vanilla_id>`) | `cache/assets/types/**` |
| Teleport destinations | Kotlin | `cache/src/.../teleports/categories/*.kt` |

---

## 6. Build & Run

| Task | Purpose |
|---|---|
| `./gradlew :cache:setupCache` | Download rev-228 cache + XTEAs from OpenRS2 (ID 2043) — one-time |
| `./gradlew :cache:resetCache` | Wipe and re-extract from local zip (no download) |
| `./gradlew :cache:generateCache` | Run TypeParser — pack custom content into base cache |
| `./gradlew :app:runPluginScanner` | Scan for plugin classes → `data/plugins.dat` |
| `./gradlew :app:runDev` | Start server (localhost, world 101, no password verify) |
| `./gradlew :app:runHotswap` | Run with HotSwap Agent |
| `./gradlew clean compileJava compileKotlin` | Compile check (Jesse's standard verification) |

Pipeline: `setupCache` → `generateCache` → `runPluginScanner` → `runDev`. Re-run `generateCache` after TOML/asset changes; `runPluginScanner` after adding/removing plugin classes.

Client: RSProx with `jav_local_228.ws` config (see README for the proxy-targets.yaml entry).

**Adding a content module:** create `content/<group>/<name>/build.gradle.kts` with `compileOnly(projects.engine)` — settings.gradle.kts auto-discovers it. Then `runPluginScanner` so its plugins register.

---

## 7. Architecture Patterns

### Plugin discovery (unchanged mechanics, new roots)
`PluginScanner` scans three package roots via marker classes: `Main::class` (org.jesse), `NearReality::class` (**also org.jesse now** — redundant with Main post-collapse, kept as a marker object), `PluginRoot::class` (cloud.rsps). Writes `data/plugins.dat`; `PluginLoader` reads it and loads via `Class.forName`. Because all content collapsed into org.jesse, everything under content/ is scanned via the first root.

### Event bus
`PluginManager` (cache module, `org.jesse.plugins`) — static enum-singleton bus. `@Subscribe` static methods discovered at scan time; `PluginManager.post(event)` at runtime.

### Content module pattern
`compileOnly(projects.engine)` means content compiles against engine but is only wired in at runtime through `app`'s runtimeOnly aggregation + plugin scanning. Content never appears on engine's compile classpath — the dependency cycle that `core-restricted` existed to break is now solved structurally.

### Interfaces, combat scripts, definitions
Unchanged in mechanics from the previous map: `GameInterface` enum + `InterfaceHandler` + implementations in `testinterfaces/`; NPC combat scripts extend `CombatScript` registered via `CombatScriptsHandler`; cache decoders in `mgi.types` with TOML overrides applied by TypeParser and runtime JSON overrides from `data/items/`.

### Typed player attributes
Use the existing `AttributesExt.kt` delegate system (`persistentAttribute()`/`attribute()`) over the `Map<String, Object>` storage. The OpenRune AttributeMap/AttributeKey port was reverted — do not reintroduce it.

---

## 8. Known Landmines

### Deliberate shims — do not "fix"
- **`ControllerManager.java` legacy-prefix rewrite** — `.replace("com.zenyte.", "org.jesse.")` / `.replace("com.near_reality.", ...)` on `lastController` load. Required for old player saves (the only persisted-FQCN site in the codebase). Stays until the serialization migration phase.
- **Persisted-class embargo** — never convert Gson-persisted classes (Player + ~20 content classes) to Kotlin until serialization migration. Gson's Unsafe allocation ignores Kotlin null-safety → runtime NPEs on old saves with no compile error.

### Dead code that looks alive
- `XTEALoaderPorted.java` — still never called.
- `app` tasks `generateFlatCache` / `generateWebJs5ResponseDirectory` — reference `org.jire.runecache.*` classes that exist nowhere in the repo. Dead tasks, delete candidates.
- `generateCache` in `app/build.gradle.kts` — commented out; the real one is `:cache:generateCache`.
- `tools/discord/` — source present, no build file, never compiled.
- `content/events/`, `content/quest/` — empty `.gitkeep` placeholders.

### Collapse leftovers (mechanical debris)
- Two directories literally named with dots instead of nested paths: `content/bosses/nex/src/main/kotlin/com.near_reality.plugins.spawns.nex/` and `content/other/larrans-key/src/main/java/com.zenyte.game.content.larranskey/`. The files inside declare correct `org.jesse.*` packages and compile (Gradle passes explicit file lists), but the dirs should be renamed/merged to real paths.
- `NearReality.kt` marker object in engine — now redundant with `Main` as a scan root (same package). Removable once PluginScanner's root list is simplified.

### Branding remnants (Tier 1, separate later plan)
- `rootProject.name = "nr-228"` (settings.gradle.kts)
- `applicationName = "near-reality-server"` (app/build.gradle.kts)
- `worlds.json` `nearRealityGuild` field
- `NearRealityLogger` and the 5 in-game "Zenyte" branding classes (ZenyteGuide/ZenyteTeleporter/ZenytePortal + 2 structures). **Never bulk-replace the word "Zenyte"** — it's also the real OSRS gem.

### Misleading names
- `testinterfaces/` — 86 production interface implementations, not tests.
- `content/other/unused/` — intentional parking lot, not garbage; classes there are awaiting a decision with their owning feature.
- `plugins.dat` — generated binary, never hand-edited.

### Reflection / string-based loading that breaks on rename
1. `PluginLoader` — `Class.forName` from `plugins.dat`; re-run `runPluginScanner` after any package/class moves.
2. `ControllerManager` — `Class.forName` on persisted controller names (plus the shim above).
3. `GodwarsBossDoorObject` / `GodwarsInstancePortal` — `Class.forName` boss instance creation.
4. `AbstractTOAManager` — reflective encounter loading (now in `content/raids/toa`).
5. `PlayerMigrationManager` — ClassGraph discovery (now in `content/other/migrations`).
6. ~60 FQCN strings in `scripts/*Compilation.kt` `defaultImports` feed the .kts compiler — fail at **runtime**, not build. These were updated in the collapse; keep them in sync on any future moves.

### Shell-hostile filenames
275 shop files in `content/other/shops/` include spaces and apostrophes (`Quartermaster's Stores.kt`). Any batch tooling must be null-safe Python, not bash loops.

### Standing gate quirks (false positives in greps)
`grep -c isActive` always ≥1 (session.isActive); `PlayerProcessEvent` always shows 2 comment mentions; `getCurrentHouse` shows 1 (javadoc).

### J2K gotcha
Static `@Subscribe` methods becoming companion members without `@JvmStatic` silently unregister handlers — any batch conversion needs `runPluginScanner` + boot-log diff per batch.

### Largest files (current)

| File | Lines |
|---|---|
| `core-model/.../obj/ids/ObjectId.kt` | 26,114 — generated, don't hand-edit |
| `core-model/.../item/ids/ItemId.kt` | 14,915 — generated |
| `core-model/.../npc/ids/NpcId.kt` | 12,026 — generated |
| `engine/.../player/Player.java` | 5,629 — god class |
| `engine/.../packet/PacketSender.kt` | 4,165 |
| `cache/.../utils/MapLocations.java` | 3,758 |
| `cache/mgi/types/draw/model/ModelData.java` | 2,919 |
| `engine/.../player/GameCommands.java` | 2,819 |
| `cache/mgi/types/draw/Rasterizer3D.java` | 2,456 |
| `engine/.../npc/NPC.java` | 2,424 |
| `cache/mgi/.../ItemDefinitions.java` | 2,365 |
| `engine/.../World.java` | 1,914 |
| `cache/mgi/.../ComponentDefinitions.java` | 1,881 |
| `content/bosses/dt2/.../WhispererNPC.kt` | 1,866 |
| `engine/.../slayer/RegularTask.kt` | 1,842 |
| `cache/mgi/tools/parser/TypeParser.java` | 1,804 |
| `engine/.../clues/EmoteClue.java` | 1,699 |
| `engine/.../combat/SpecialAttack.java` | 1,560 |
| `content/bosses/nex/.../NexNPC.java` | 1,503 |
| `content/bosses/nightmare/.../BaseNightmareNPC.java` | 1,470 |

---

*Update this document as cleanup progresses. The README's own "Project Structure" table is currently stale (still lists core/, core-restricted/, plugins/) and should be synced with Section 1 of this map.*
