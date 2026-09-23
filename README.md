# Offline\_Scape

OSRS private server on revision 240, targeting a clean vanilla OSRS experience. Originally forked from the Near Reality / Zenyte rev-228 codebase; the lineage packages have been collapsed into a single `org.jesse` root and the revision upgraded to 240.

Active cleanup continues: removing NR custom content and custom cache assets, extracting content out of the engine into auto-discovered `content/` modules, and migrating Java content to Kotlin. See `PROJECT_MAP.md` for the structural map.

## Requirements

- **JDK 21** (JetBrains Runtime recommended for hotswap support)
- **IntelliJ IDEA** (recommended)

## Setup

1. Open the project in IntelliJ IDEA and set JDK 21 as the project SDK (`File → Project Structure`).

2. Download the rev-240 game cache and XTEA keys from the [OpenRS2 Archive](https://archive.openrs2.org/caches) (archive ID 2710):
   ```
   ./gradlew :cache:setupCache
   ```

3. Start the server:
   ```
   ./gradlew :app:runDev
   ```

That's the whole pipeline. The cache is served and loaded **unmodified** — there is no cache build step — and plugin classes are discovered by a classpath scan at boot, so there is no plugin index to generate. Add a class, restart, it's found.

## Connecting

Use [RSProx](https://github.com/blurite/rsprox) to launch a rev-240 client. RSProx supplies the client-side game cache and XTEA keys (rev 240 no longer sends XTEA keys in rebuild packets — the proxy owns that responsibility; the server's local cache is for definition loading only). Add this entry to your `proxy-targets.yaml`:

```yaml
  - name: Offline_Scape
    jav_config_url: https://client.blurite.io/jav_local_240.ws
    varp_count: 20000
    revision: 240
    game_server_port: 43594
    modulus: 933160a3e3e3369fef59748ce74ab1ea4f21a6a523558b6a1b7b395998d96f3ab62827af37c4855220303edae8365f968eb7cc0794f82ba4117d029e2a9d54adc2d60d8d2f79a6cc37c74bc225432291b02c0c92571ff91d393698f54413af6fe20eb71dbbedd87b6233e6597bfb295a21f2d085d9f4c7a02374e32719c4dce1
```

(The modulus corresponds to the server's RSA key at `data/private.key` — unchanged across the revision upgrade.)

Select "Offline\_Scape" in RSProx and connect. World config is in `worlds.json` (localhost, port 43594, world 101, password verification disabled for dev).

## Gradle Tasks

| Task | Description |
|---|---|
| `./gradlew :cache:setupCache` | Download rev-240 cache + XTEA keys from OpenRS2 ID 2710 (one-time) |
| `./gradlew :cache:resetCache` | Wipe and re-extract the cache from the local zip (no re-download) |
| `./gradlew :app:runDev` | Start server (localhost, dev mode) |
| `./gradlew :app:runBeta` / `runProduction` | Start with beta / production world config |
| `./gradlew :app:runHotswap` | Start with HotSwap Agent for live code reload |
| `./gradlew clean compileJava compileKotlin` | Compile check across all modules |

To use a different cache build, change `openrs2CacheId` in `cache/build.gradle.kts`.

## Project Structure

Gradle multi-module build. Kotlin 2.2.0, JDK 21 with `--enable-preview`.

| Module | Purpose |
|---|---|
| `engine/` | Game engine — world simulation, player/NPC entities, combat, skills, networking (RSProt), packet handling, plugin discovery, plus content not yet extracted to `content/` |
| `cache/` | Cache loading, definition decoders, XTEA keys, event bus |
| `core-model/` | Shared data models and the ID constant files (`ItemId`, `NpcId`, `ObjectId`) |
| `api/` | Database layer for the external API (store, votes, hiscores — NR infrastructure, standalone, cull candidate) |
| `threads/` | Main game thread definition, CPU affinity |
| `util/` | Shared utilities — logging, string utils, DB pooling |
| `scripts/` | Kotlin DSL base classes for content — spawns, drops, shops, item/NPC/object actions and definitions |
| `content/` | Game content as auto-discovered modules — bosses, raids, skills, areas, minigames, interfaces, spawns, shops, travel. Any directory with a `build.gradle.kts` becomes a module automatically |
| `tools/` | Server tooling — analyzer, backups, updater (auto-discovered) |
| `app/` | Application entry point wrapper and run tasks (no source) |
| `data/` | Runtime data — RSA key, item overrides, examines, area maps, diary info |
| `cache/data/` | The downloaded cache, XTEAs, per-NPC combat definitions (JSON) and drop tables (JSON) |

### Adding a content module

Create `content/<group>/<name>/build.gradle.kts` containing:

```kotlin
plugins { id("org.jetbrains.kotlin.jvm") }
dependencies { compileOnly(projects.engine) }
```

Add `implementation(projects.scripts.<dsl>)` entries as needed. The module is picked up by the settings walk on the next Gradle sync; its plugin classes register via the boot-time scan — no further wiring.

### Content formats

| Content type | Format | Location |
|---|---|---|
| NPC spawns | Kotlin DSL (`NPCSpawnsScript`) | `content/spawns/region/region{N}xxx/` |
| Shops | Kotlin DSL (`ShopScript`) | `content/other/shops/` |
| Drop tables | JSON per-NPC | `cache/data/npcs/drops/` |
| NPC combat defs | JSON per-NPC | `cache/data/npcs/combat/` |
| Teleport destinations | Kotlin DSL | `cache/.../teleports/categories/` |

## Origin

Forked from the Near Reality rev-228 server (itself Zenyte-based). Since the fork: the mixed `com.zenyte` / `com.near_reality` package roots have been collapsed into `org.jesse`, the `core/` module was renamed to `engine/`, the custom cache pipeline (TypeParser and packed assets) was removed in favour of the pristine OpenRS2 cache, the `plugins.dat` scanner pipeline was replaced with boot-time ClassGraph discovery, and the server was upgraded from revision 228 to 240. Removal of the remaining NR systems (donator/store/vote infrastructure, custom items, seasonal content) proceeds in numbered stages with exit gates.
