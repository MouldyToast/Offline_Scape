# PLAN — Replace `mgi.types` with OR2 + OpenRune codecs

Synthesises FINDINGS_turn1 (OR2 decode verification), FINDINGS_turn2 (type mapping +
server pipeline), FINDINGS_turnA (RSCM/gamevals), FINDINGS_turnB (builder
coexistence). All §6 counts grep-verified on current HEAD (Java **and** Kotlin — see
correction note in §6). Pins: cache = OpenRS2 #2043 = osrs-dumps `f54d6da7`
(rev-228); gameval name source = osrs-dumps master `95304496` (2026-09-02-rev240);
OR2 = `dev.or2:all:2.4.17`.

---

## §1 Verified: OR2 decodes rev-228

- OR2 2.4.17 decodes the rev-228 cache with **zero decode errors** — 14,162 NPCs /
  30,646 items — verified by field-level diff against `mgi.types` output on **both**
  the stock OpenRS2 #2043 cache and the NR-packed production cache (Turn 1).
- Revision is passed explicitly: `OsrsCacheProvider(cache, 228)` (stock cache has no
  `version.dat` for auto-detect). The codec revision branches (thresholds 220/232
  bracket 228) behave correctly in practice, not just by inspection.
- Known mgi decode gaps at rev-228 (Turn 2 presence probes): npc op122
  (lowPriorityFollowerOps, 217 defs) and op123 (isFollower, 240 packed) are silently
  ignored; npc height op (14 defs) ignored; everything else mgi drops occurs **zero**
  times at rev-228. OR2 decodes all of these.

## §2 Type mapping (mgi → OR2/OpenRune)

| mgi.types class | OR2 client type | OpenRune server type | Note |
|---|---|---|---|
| `config.items.ItemDefinitions` | `ItemType` | `ItemServerType` | client portion field-equivalent (Turn 1); server fields → §3 |
| `config.npcs.NPCDefinitions` | `NpcType` | `NpcServerType` | NpcType carries combat stats/height at 228 |
| `config.ObjectDefinitions` | `ObjectType` | `ObjectServerType` | pure swap at 228 |
| `config.enums.EnumDefinitions` (+ `Enums`/`IntEnum`/`StringEnum` wrappers) | `EnumType` | — | wrapper layer needs thin equivalent (Tier 2) |
| `config.StructDefinitions` | `StructType` | — | opcode-identical; **Tier 1** |
| `config.AnimationDefinitions` | `SequenceType` | `SequenceServerType` | |
| `config.SpotAnimationDefinition` | `SpotAnimType` | — | |
| `component.ComponentDefinitions` | `widget.ComponentType` | — | NR `component.custom/*` builders migrate/die with their features |
| `config.VarbitDefinitions` / `ParamDefinitions` / `InventoryDefinitions` / `HitbarDefinitions` / `DBRow/DBTable` / idk / overlay / underlay | `VarBitType` / `ParamType` / `InventoryType` / `HealthBarType` / `DBRowType/DBTableType` / `IdentityKitType` / `OverlayType/UnderlayType` | various or-cache extras | verified equal where diffed (Turn 1) |
| `worldmap.*` | decode: `WorldMapAreaType`; **packing: no OR2/OpenRune equivalent** | — | retain mgi wmpacker (§5) |
| `clientscript.ClientScriptDefinitions`, `draw.*`, `skeleton.*` | `PackCs2` / n-a | — | builder-pipeline concerns only |

Runtime access target: OR2 ships **`dev.openrune.cache.CacheManager`** (typed maps,
`init(CacheStore)`, `getStruct/getEnum/getNpc/…` + `OrDefault` variants + bulk
getters) — the same object OpenRune content uses for client-cache data. Server-config
data eventually goes through `ServerCacheManager` (or-cache port, later phase).

## §3 Server-side fields (summary of Turn 2 — governs Tiers 4–5)

- OpenRune keeps a clean split: client `NpcType` + **standalone** `NpcServerType`
  (separate enriched object, decoded from a second SERVER cache built by
  `PackServerConfig` from RSCM-id TOML).
- **Rev-228 cache params already carry equipment bonuses (params 0–11), str/prayer
  (10/11) and attack speed (14)** — verified against wiki values. Offline_Scape's
  osrsbox `ItemDefinitions.json` overlay (bonuses/speed/slot/weight) is redundant and
  a stale-data risk: most of the item "merge" gets **deleted**, not ported.
- NPC combat JSON: hp/att/def/str/ranged/magic duplicate cache NpcType at 228 (drop);
  wander/aggro/respawn map to `NpcServerType` fields; maxHit/anims/projectiles are
  content-level combat config in OpenRune, not server-type fields.

## §4 RSCM mechanism (Turn A — done, artifacts exist)

- The rev-228 cache **has no gamevals archive** (no idx24; files 0–15,17–21,255), so
  OpenRune's `GamevalDumper`/`freshCache` path is impossible; and f54d6da7 symbols are
  placeholders for every config table (real names didn't publicly exist in Feb 2025).
- Working mechanism (built + verified): **backport Jagex gameval names from osrs-dumps
  master (rev-240) onto rev-228's ID universe**, with mechanical repurpose guards
  (normalized display-name diff for npc/obj/loc: 21/60/462 unsafe → placeholder;
  varbit structural diff: 550 unsafe) and `prefix_id` placeholder fallback. Coverage:
  99%+ npc/obj/loc/seq/spotanim/inv/component; varbit 9,942/17,262; varp 2,171/4,661;
  enum/struct/param have no real names anywhere (Jagex gamevals exclude them) —
  placeholders, hand-nameable later.
- Artifacts: `make_rscm_228.py` (regenerates in ~5s) → 21 `.rscm` text tables +
  `gamevals.dat`/`gamevals_generated.dat` (byte-verified against `GameValDat`
  format; component/dbcol packed `(parent<<16)|child`). Layering rule: Jagex-group
  tables → dat; enum/struct/param/etc → `.rscm` only (preserves `GameValProvider`'s
  `maxBaseID` guard semantics so vanilla enums stay hand-nameable).
- Consumption today needs only the published OR2 artifact:
  `ConstantProvider.load(dir)` (RSCMProvider/SymProvider). `RSCM.kt` (~110 lines,
  or-cache) is copyable when the `"npc.x".asRSCM()` idiom is wanted.
- **Not a Tier-1 dependency** — numeric IDs stay through Tiers 1–3. NR custom names:
  custom layer, `nr_` prefix, only for IDs above vanilla max (verify NR ID ranges
  before emitting — open task, execution session).

## §5 Builder strategy (Turn B — decision)

**TypeParser keeps packing; OR2 takes over reading. Indefinitely.** Decode/build are
fully decoupled: OR2 already decodes the NR-packed cache (Turn 1), `pack(Class…)`
re-encodes only TOML-touched defs, and the builder runs in its own JVM. The true
boundary: `mgi.types` lives on **inside the cache module** as the encoder until each
packer is ported or (mostly) deleted by NR cleanup — the packer fleet is 34 Kotlin
files / ~5,200 lines in `com/near_reality/cache_tool/packing/custom/`, largely
NR-deletion candidates; port survivors to OR2 tasks (`PackConfig` after asset-TOML
schema conversion, `PackIfType`, `PackSprites`, raw `Cache.write` for byte packers,
`PackCs2` when cs2 work resumes — the NR Neptune compiler is already commented out).
Worldmap repacking (`CustomWorldMapAreas.changeMainArea`, TypeParser:1534 — the 3g
heap) has **no OR2/OpenRune equivalent**: retain mgi wmpacker; revisit after NR
cleanup empties it. OpenRune's `MapPackers` is a SERVER-cache task, unrelated.

**Immediate standalone commits (independent of migration):**
1. `ItemDefinitions.encode()`: write op3 (examine) + op43 (subops) — fixes the 8
   dueling rings on next cache gen.
2. Same commit or sibling: NPC op122/123 decode+encode (mgi currently ignores both and
   emits legacy op111 on encode, NPCDefinitions.java:679/:327) — un-deadens
   `isFollower` for 217 vanilla pets and stops silent flag loss on touched defs.
3. `generateCache` task: add `maxHeapSize = "3g"` (confirmed absent at HEAD,
   cache/build.gradle.kts:53).

## §6 Migration order (all counts grep-verified on current HEAD, Java+Kotlin)

**Correction to FINDINGS_turn2:** its "re-verified" import counts matched
Java-only grep (`;`-terminated) and undercounted Kotlin files. True per-class file
counts (files importing the class; core/cache/content split shown; `core-restricted`
no longer exists; 482 files total import `mgi.types`):

| class | total | core | cache | content | other |
|---|---|---|---|---|---|
| ItemDefinitions | 205 | 154 | 17 | 28 | 6 |
| NPCDefinitions | 63 | 42 | 11 | 9 | 1 |
| EnumDefinitions | 49 | 38 | 10 | 1 | 0 |
| ObjectDefinitions | 48 | 29 | 13 | 6 | 0 |
| ComponentDefinitions | 33 | 8 | 25 | 0 | 0 |
| AnimationDefinitions | 25 | 16 | 9 | 0 | 0 |
| StructDefinitions | 24 | 12 | 8 | 4 | 0 |
| SpotAnimationDefinition | 10 | 2 | 8 | 0 | 0 |
| Enums wrapper | 48 | 24 | 0 | 24 | 0 |
| IntEnum / StringEnum | 16 / 6 | 16 / 6 | 0 | 0 | 0 |
| VarbitDefinitions / InventoryDefinitions | 5 / 7 | 2 / 4 | 3 / 3 | 0 | 0 |

Only 2 wildcard `mgi.types` imports exist, both in cache-module NR component builders
(stay put). **Cache-module imports never migrate in Tiers 1–3** — they are the encoder
side (§5).

**Tiers** (server modules only: core + content + other; each tier = swap reads to
`dev.openrune.cache.CacheManager`, leave mgi loading in place until the tier's final
cleanup step):

- **Tier 1 — Struct (16 files: 12 core + 4 content).** No server-side extensions, no
  wrappers, opcode-identical codec. Fully specified below.
- **Tier 2 — Enum (39 files: 38 core + 1 content) + wrappers (Enums 48, IntEnum 16,
  StringEnum 6).** Blocker to resolve first: design the typed-wrapper equivalent
  (`EnumType.getInt/getString` + a ported `Enums` object holding well-known enum ids;
  the wrapper is Offline_Scape convenience, not mgi decoding — it can be re-pointed at
  `CacheManager.getEnum` wholesale, likely making Tier 2 mostly a 1-file change plus
  the 39 direct-import files). Settings trio (already struct-swapped in Tier 1) picks
  up its enum edits here.
- **Tier 3 — SpotAnim (2 core) + Varbit (2 core) + Inventory (4 core) + Animation
  (16 core) + Object (35).** Straight swaps; Object is the volume item. Blocker:
  audit `AnimationDefinitions` speed/priority field usage (mgi legacy ops 13–15 vs
  OR2's op18) at the 16 sites.
- **Tier 4 — NPC (52 server files).** Blockers: NPC combat JSON dedup decision (§3);
  entity classes holding `NPCDefinitions` fields; coordinate with Phase-5B–5F boss
  work.
- **Tier 5 — Item (188 server files).** Hardest. Blockers: osrsbox JSON deletion
  (§3), equipment/bonus reads move to cache params, `Player.java` field-extraction
  interplay, and the 154 core files. Sub-tier by package.
- **Tier 6 — Component (8 core files) + cleanup.** After NR interface deletions.
  Endgame: remove types from `Definitions.serverLowPriorityDefinitions` as each
  becomes read-free; mgi.types survives only in `cache/`.

---

## §6.1 TIER 1 — EXECUTABLE SPEC (Struct swap)

Everything below is verified at current HEAD. No open questions. Standard build check:
`./gradlew clean compileJava compileKotlin`.

### Step 0 — Dependency wiring (3 edits)

**(a) `settings.gradle.kts`** — inside the existing
`dependencyResolutionManagement { repositories { … } }` block (lines 14–24), after
`mavenCentral()` add:

```kotlin
        maven(url = "https://raw.githubusercontent.com/OpenRune/hosting/master")
        maven(url = "https://jitpack.io")
```

**(b) `gradle/libs.versions.toml`** — in the libraries section add:

```toml
or2 = { module = "dev.or2:all", version = "2.4.17" }
```

**(c) `core/build.gradle.kts`** — in `dependencies {}` (after `api(projects.cache)`):

```kotlin
    api(libs.or2)
```

(`api` so content modules see it via their `compileOnly(projects.core)`.)

### Step 1 — Boot wiring (1 edit)

`core/src/main/java/com/zenyte/game/GameLoader.java` — after
`CacheManager.loadDefinitions(pool, false);` (line 21) add:

```java
        Or2Defs.init("./cache/data/cache/");
```

(import `com.zenyte.game.cache.Or2Defs`). mgi loading stays untouched — both systems
read the same files read-only; NPCPlugin.java's detached-load fallback path is
unaffected.

### Step 2 — New file (the only new code, ~40 lines)

`core/src/main/kotlin/com/zenyte/game/cache/Or2Defs.kt`:

```kotlin
package com.zenyte.game.cache

import dev.openrune.OsrsCacheProvider
import dev.openrune.cache.CacheManager
import dev.openrune.definition.type.ParamType
import dev.openrune.definition.type.StructType
import dev.openrune.filesystem.Cache
import java.nio.file.Path

/** Boot-time loader for OR2 definitions + mgi-parity param-default helpers. */
object Or2Defs {

    lateinit var params: Map<Int, ParamType>
        private set

    @JvmStatic
    fun init(cachePath: String) {
        val cache = Cache.load(Path.of(cachePath))
        CacheManager.init(OsrsCacheProvider(cache, 228))
        val map = mutableMapOf<Int, ParamType>()
        OsrsCacheProvider.ParamDecoder(228).load(cache, map)
        params = map
    }

    /** mgi StructDefinitions.get parity: throws if id has no struct. */
    @JvmStatic
    fun struct(id: Int): StructType =
        requireNotNull(CacheManager.getStruct(id)) { "No struct $id" }

    @JvmStatic
    fun structOrNull(id: Int): StructType? = CacheManager.getStruct(id)
}

/* mgi getParamAs* parity: absent key falls back to the PARAM DEFINITION's default
 * (mgi StructDefinitions.java:90–131), not a flat -1/"" like StructType.getInt. */
fun StructType.paramInt(key: Int): Int =
    params?.get(key) as? Int ?: requireNotNull(Or2Defs.params[key]) { "No param $key" }.defaultInt

fun StructType.paramBool(key: Int): Boolean = paramInt(key) == 1

fun StructType.paramString(key: Int): String =
    params?.get(key) as? String
        ?: requireNotNull(Or2Defs.params[key]) { "No param $key" }.defaultString ?: ""
```

Java callers use `Or2Defs.struct(id)` and
`Or2DefsKt.paramInt(struct, key)` / `Or2DefsKt.paramBool(...)` /
`Or2DefsKt.paramString(...)`. (Do **not** map `getParamAsInt` to `StructType.getInt`
— different default semantics, see comment above.)

### Step 3 — Callsite edits (16 files; full line inventory in
`APPENDIX_tier1_struct_sites.txt`)

Mechanical patterns:
- `StructDefinitions.get(id)` → `Or2Defs.struct(id)` (Java) / `Or2Defs.struct(id)`
  (Kotlin). Sites already wrapped in `Objects.requireNonNull(...)` drop the wrapper
  (Or2Defs.struct throws).
- Type `StructDefinitions` in fields/params/locals → `StructType`
  (`dev.openrune.definition.type.StructType`).
- `.getParamAsInt(k)` → `paramInt(k)` ext (Kotlin) / `Or2DefsKt.paramInt(s, k)`
  (Java); same for Boolean/String.
- Remove the `import mgi.types.config.StructDefinitions` line; add the OR2/Or2Defs
  imports.

File-by-file (line numbers at HEAD; only struct-related edits — files also using
EnumDefinitions/Enums keep those mgi imports until Tier 2):

1. **core …/loyaltytitles/LoyaltyTitleShop.kt** (:14,94–95,145–153,232,241) — type
   swaps incl. the `temporaryAttributes` cast at :95 (`as StructType?`), list at
   :145–153 → `Or2Defs.struct(id++)`.
2. **core …/challenges/Challenge.kt** (:3,:10) — one-liner.
3. **core …/advancedsettings/SettingCategory.java** (:3,22–25,32) — ctor param type,
   3 param getters; enum line :25 untouched.
4. **core …/advancedsettings/Settings.java** (:6,:49).
5. **core …/advancedsettings/Setting.java** (:7,130–143) — 13 param getters.
6. **core …/collectionlog/CollectionLogInterface.java** (:22,78,137,141,153,177,185,
   275) — includes helper signature :141/:177.
7. **core …/collectionlog/CollectionLog.java** (:20,42,50).
8. **core …/collectionlog/CollectionLogRewardHandler.java** (:8,:67).
9. **core …/calog/CALogBossInterface.java** (:11,:33).
10. **core …/player/Player.java** (:223,4559–4567) — `getTitle()` return type →
    `StructType` (external consumers: none — verified; only LoyaltyTitleShop helpers,
    which swap in the same commit).
11. **core …/advent/AdventDay.java** (:4,27–29).
12. **core /tools/StructExtractor.java** (:4,:21) — array iteration → 
    `for (var t : dev.openrune.cache.CacheManager.INSTANCE.getStructs().values())`
    (fully qualify: this file may sit near `com.zenyte.CacheManager` usages; check
    imports per file — Kotlin files can `import dev.openrune.cache.CacheManager`
    unless the zenyte one is also imported).
13. **content/other/group-ironman …/IronmanGroupTasks.kt** (:8,:79).
14. **content/other/group-ironman …/IronmanGroupChallengesInterface.kt** (:11,25–27).
15. **content/other/group-ironman …/IronmanGroupChallenges.kt** (:11,:138).
16. **content/raids/toa …/InvocationType.java** (:3,68–71).

### Step 4 — Verification

1. `./gradlew clean compileJava compileKotlin`.
2. Parity check (throwaway, delete after): a `main` in core that, post-`Or2Defs.init`
   + mgi load, iterates `StructDefinitions.definitions` and asserts
   `params == mgi.getParameters()` (as plain maps) for every non-null id, and that
   every `Or2Defs.params[k].defaultInt/defaultString` matches
   `ParamDefinitions.get(k)`. Expected: zero mismatches (struct opcode sets are
   identical; Turn-1 method).
3. Boot the server; open the settings interface, collection log, and loyalty titles
   (the three heaviest struct consumers) — behavioral smoke.
4. Do **not** remove `StructDefinitions` from `Definitions.serverLowPriorityDefinitions`
   (mgi/types/Definitions.java:119) in this commit — that's the tier's closing step
   once a follow-up grep confirms zero remaining server-module readers.

### Non-goals for Tier 1
No RSCM usage, no TypeParser/cache-module changes, no enum edits (even in mixed
files), no removal of mgi struct loading.

---

## Open items ledger (carried forward)

- NR custom ID ranges — RESOLVED (measured on the packed cache via OR2 decode):
  every type's NR customs sit strictly above vanilla maxima — npc 115 @ 14163–16135,
  obj 749 @ 31300–60501, loc 7 @ 56078–60505, enum 107 @ 5716–23001, struct 923 @
  5828–30174, seq 73 @ 15000–30006, varbit 2,767 @ 17262–25055. GameValProvider's
  guard is satisfied; NR names may be emitted. Exclude varp's 15,339 above-max
  entries (empty 20,000-padding, not content).
- Packed cache LOSES vanilla definitions vs base: 5 npcs, 2 locs, 16 enums, **403
  structs** absent after generateCache. Same encoder-fidelity family as the op43 bug.
  Audit which packer owns each (struct packer prime suspect) during the §5 standalone
  commits or Tier-1 verification; diff base-vs-packed key sets to get the exact ids.
- `NPCDefinitions.isFollower` reader audit — resolved by §5 patch 2 (decode restores
  real data; readers then work).
- `changeMainArea` content audit after each NR deletion phase (§5) — may retire the
  3g heap entirely.
- Cache-module game classes (`DiaryInfo`, `Regions`, `WorldObject`,
  `BountyHunterRewardType` — canonical in `cache/`, used by core) — future extraction;
  OpenRune equivalent is content-plugin pack-config TOML (Turn B §H).
- Enum wrapper design decision at Tier-2 start (§6).
- Asset-TOML schema conversion (mgi readers → OR2 rsconfig) before any `PackConfig`
  adoption (§5).
