# Offline\_Scape

OSRS private server based on the Near Reality / Zenyte rev-228 codebase, targeting a clean vanilla OSRS experience.

Active cleanup is underway to remove NR custom content (donator systems, custom weapons, seasonal events, etc.) and move toward a revision-agnostic architecture. See `PROJECT_MAP.md` for structural details.

## Requirements

- **JDK 21** (JetBrains Runtime recommended for hotswap support)
- **IntelliJ IDEA** (recommended)

## Setup

1. Open the project in IntelliJ IDEA and set JDK 21 as the project SDK (`File → Project Structure`).

2. Download the base rev-228 game cache and XTEA keys from the [OpenRS2 Archive](https://archive.openrs2.org/caches) (archive ID 2043):
   ```
   ./gradlew :cache:setupCache
   ```

3. Pack custom content from `cache/assets/` into the base cache:
   ```
   ./gradlew :cache:generateCache
   ```

4. Generate the plugin class index:
   ```
   ./gradlew :app:runPluginScanner
   ```

5. Start the server:
   ```
   ./gradlew :app:runDev
   ```

## Connecting

Use [RSProx](https://github.com/blurite/rsprox) to launch a rev-228 client. Add this entry to your `proxy-targets.yaml`:

```yaml
  - name: Offline_Scape
    jav_config_url: https://client.blurite.io/jav_local_228.ws
    varp_count: 20000
    revision: 228
    game_server_port: 43594
    modulus: 933160a3e3e3369fef59748ce74ab1ea4f21a6a523558b6a1b7b395998d96f3ab62827af37c4855220303edae8365f968eb7cc0794f82ba4117d029e2a9d54adc2d60d8d2f79a6cc37c74bc225432291b02c0c92571ff91d393698f54413af6fe20eb71dbbedd87b6233e6597bfb295a21f2d085d9f4c7a02374e32719c4dce1
```

Select "Offline\_Scape" in RSProx and connect. World config is in `worlds.json` (localhost, port 43594, world 101).

## Gradle Tasks

| Task | Description |
|---|---|
| `./gradlew :cache:setupCache` | Download base cache + XTEAs from OpenRS2 (one-time) |
| `./gradlew :cache:resetCache` | Wipe and re-extract cache from the local zip (no download) |
| `./gradlew :cache:generateCache` | Run TypeParser — pack custom content into the base cache |
| `./gradlew :app:runPluginScanner` | Scan for plugin classes, write `data/plugins.dat` |
| `./gradlew :app:runDev` | Start server (localhost, dev mode) |
| `./gradlew :app:runHotswap` | Start with HotSwap Agent for live code reload |
| `./gradlew classes` | Compile all modules |

### When to re-run what

| You changed… | Re-run |
|---|---|
| TOML definitions or cache assets | `generateCache` |
| Added/removed a plugin class | `runPluginScanner` |
| Code only (no new plugin classes) | Just rebuild and run |

## Cache Pipeline

1. **`setupCache`** downloads a vanilla OSRS rev-228 cache from the [OpenRS2 Archive](https://archive.openrs2.org/caches) (ID 2043) into `cache/data/cache/`, and XTEA keys into `cache/data/objects/xteas.json`. Change the `openrs2CacheId` variable in `cache/build.gradle.kts` to use a different build.

2. **`generateCache`** runs `TypeParser`, which loads the vanilla cache, applies TOML definition overrides from `cache/assets/types/`, runs custom content packers, and writes the modified cache back to `cache/data/cache/`.

3. At server startup, `CacheManager` loads the final cache and `PluginLoader` reads `data/plugins.dat` to discover plugin classes.

## Project Structure

| Module | Purpose |
|---|---|
| `core/` | Game engine — world simulation, player/NPC entities, combat, skills, networking (RSProt 228), plugin system |
| `core-restricted/` | Content with bidirectional dependencies on core and scripts (ToA, Colosseum, DT2, PvM Arena, migrations) |
| `core-model/` | Shared data models (player models, item models, ID constants) |
| `cache/` | Cache reading, definition decoders, TypeParser cache builder, XTEA keys |
| `api/` | Database layer for external API (store, votes, hiscores — NR custom, standalone) |
| `threads/` | Thread management, CPU affinity |
| `util/` | Shared utilities (logging, string formatting, DB pooling) |
| `scripts/` | DSL definitions — NPC spawns, drops, shops, item/NPC/object definitions |
| `plugins/` | Remaining game content not yet moved to `content/` — spawns, shops, interfaces, items, areas (being consolidated) |
| `content/` | Vanilla content modules — bosses, raids, skills, areas, minigames, drops, generic plugins, items, travel (auto-discovered by Gradle) |
| `tools/` | Server tooling — backups, updater, analyzer, discord bot (auto-discovered by Gradle) |
| `app/` | Application entry point wrapper, Gradle task definitions (no source code) |
| `data/` | Runtime data (characters, GE offers, server config, combat defs, drop tables) |
| `cache/assets/` | Custom content source data packed into the cache by TypeParser |

## Origin

Forked from the Near Reality rev-228 server (Zenyte-based). Ongoing cleanup is removing NR custom content (donator islands, custom weapons, seasonal events, wilderness vault, etc.) to produce a clean vanilla base suitable for revision upgrades. The boon/perk/remnant system, Wilderness Vault, and Well of Goodwill have already been removed. The `cloud.rsps` opaque dependencies have been replaced with local implementations and the CDN to the OpenRS2 Archive.
