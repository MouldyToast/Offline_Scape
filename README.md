# Offline_Scape

OSRS private server based on the NR-228 / Zenyte codebase, targeting a vanilla OSRS experience.

## Requirements

- **JDK 21** (JetBrains Runtime recommended for hotswap support)
- **IntelliJ IDEA** (recommended)

## Setup

1. Open the project in IntelliJ IDEA and set JDK 21 as the project SDK (`File → Project Structure`).
2. Download the base game cache and XTEA keys from the [OpenRS2 Archive](https://archive.openrs2.org/caches):
   ```
   gradlew :cache:setupCache
   ```
3. Generate boon/remnant exchange data:
   ```
   ./gradlew :app:generateBoonData
   ```
4. Generate the cache (packs custom content from `cache/assets/` into the base cache):
   ```
   ./gradlew :cache:generateCache
   ```
5. Generate the plugin class list:
   ```
   ./gradlew :app:runPluginScanner
   ```
6. Start the server:
   ```
   ./gradlew :app:runDev
   ```

## Connecting

Use [RSProx](https://github.com/blurite/rsprox) to launch the client. Add this entry to your `proxy-targets.yaml`:

```yaml
  - name: Offline_Scape
    jav_config_url: https://client.blurite.io/jav_local_228.ws
    varp_count: 20000
    revision: 228
    game_server_port: 43594
    modulus: 933160a3e3e3369fef59748ce74ab1ea4f21a6a523558b6a1b7b395998d96f3ab62827af37c4855220303edae8365f968eb7cc0794f82ba4117d029e2a9d54adc2d60d8d2f79a6cc37c74bc225432291b02c0c92571ff91d393698f54413af6fe20eb71dbbedd87b6233e6597bfb295a21f2d085d9f4c7a02374e32719c4dce1
```

Select "Offline_Scape" in RSProx and connect. Default world config is in `worlds.json` (localhost, port 43594).

## Gradle Tasks

| Task | Description |
|---|---|
| `./gradlew :cache:setupCache` | Download base cache + XTEAs from OpenRS2 |
| `./gradlew :app:generateBoonData` | Generate boon/remnant exchange data |
| `./gradlew :cache:generateCache` | Pack custom content into the base cache |
| `./gradlew :app:runPluginScanner` | Generate plugin class list (required before first run) |
| `./gradlew :app:runDev` | Start server (localhost, dev mode) |
| `./gradlew :app:runHotswap` | Start server with HotSwap Agent for live code reload |
| `./gradlew classes` | Compile all modules |

## Cache Pipeline

1. `:cache:setupCache` downloads a vanilla OSRS rev 228 cache from the [OpenRS2 Archive](https://archive.openrs2.org/caches) (ID 2043) into `cache/data/cache/`, and XTEA keys into `cache/data/objects/xteas.json`.
2. `:app:generateBoonData` generates `cache/data/dynamic/perk_data.json` and remnant exchange values.
3. `:app:generateCache` runs TypeParser, which extracts `data/cache-228.zip` into `data/cache/`, loads it, then packs custom content from `cache/assets/` (items, NPCs, sprites, models, maps, client scripts, interfaces) into the cache files.
4. At runtime, `GameLoader` reads the final cache from `cache/data/cache/`.

To use a different build of rev 228, change the `openrs2CacheId` variable in `cache/build.gradle.kts`.

## Project Structure

- `core/` — game engine, networking (RSProt 228), world/entity systems
- `core-restricted/` — content that depends on core (ToA, migrations, anticheat)
- `cache/` — cache reading library, TypeParser cache builder, cache tools
- `cache/assets/` — custom content source data packed into the cache by TypeParser
- `plugins/` — game content plugins (areas, bosses, items, spawns, interfaces)
- `scripts/` — script definitions (NPC, item, object definitions and actions)
- `data/` — runtime data (characters, GE offers, server config)

## Origin

Forked from the Near Reality rev 228 server (Zenyte-based). The `cloud.rsps` opaque dependencies have been replaced with local implementations, and the base cache source has been switched from Jire's CDN to the OpenRS2 Archive.
