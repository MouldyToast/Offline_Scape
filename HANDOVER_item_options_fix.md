# HANDOVER — Fix boot regression from cache/assets removal (align plugins to vanilla)

## TL;DR for the next session
The big `cache/assets/` removal is **done and pushed** (branch
`claude/sub-agents-investigation-tiyebh`, repo `MouldyToast/Offline_Scape`, 10 commits).
But the **server now crashes at boot**: several `ItemPlugin`s throw because the custom
inventory options they bind no longer exist on the item definitions (those options used to
come from `cache/assets/types/` TOML overrides that were deleted or moved).

**Owner's chosen fix strategy: "align plugins to vanilla first."** Prefer changing the
*plugin* to use the vanilla definition over re-adding a cache override. The owner does **not**
want to run `generateCache` for real game items, and does not want real items overridden.

**Your job this session:** `git clone` osrs-dumps (the vanilla rev-228 / build-228 definition
dumps), use it to confirm what each affected item/NPC actually has in vanilla, then apply the
per-plugin fixes below and get the server booting clean. A genuinely-custom remainder needs an
owner decision (see "Open decision").

---

## Hard facts established last session (do NOT re-derive — verified by sub-agents)

1. **The server reads the binary cache `cache/data/cache/` directly at boot.** Item/NPC/object
   *options* come 100% from decoding that cache. `GameLoader.load` → `CacheManager.loadCacheFiles("./cache/data/cache/", true)` (read-only) → `CacheManager.loadDefinitions` → `ItemDefinitions.load()` decodes CONFIGS/ITEM group.
2. **`data/items/ItemDefinitions.json` is NOT the source of options.** It only overrides
   weight/slot/examine/bonuses/weapon-anim/requirements (`ItemDefinitions.apply`), never
   `inventoryOptions`. The `-beforeFuckery.json`/`.bak` siblings are dead debug dumps. Same for
   `data/npcs`/`data/objects` (drops/combat/examine side-inputs only). So there is no stale-JSON
   culprit — the options are simply absent from the cache decode.
3. **`generateCache` is a one-time build step, NOT per-boot.** `runDev`/`runProduction`/etc.
   (`app/build.gradle.kts`, `workingDir = ../` = repo root) do **not** `dependsOn` it. It writes
   the same `cache/data/cache/` (its cwd is the `:cache` module dir, path `data/cache`). So a
   cache override requires exactly one `generateCache` run, then the server just reads it.
4. **js5 to the game client is served from the in-memory `CacheManager.getCache()`** object
   (`NetworkServiceFactory(... CacheManager.getCache())`, `Js5Info.of(cache)`), NOT from a flat
   dir. `generateFlatCache`/`generateWebJs5ResponseDirectory` are standalone tasks off the boot
   path. **Implication:** for a custom left-click option to actually render for players, the
   option must be present in the cache the client downloads — vanilla alignment can't invent a
   custom option; only a cache entry (disk override, or a boot-time mutation of the in-memory
   cache) makes the client show it.
5. **The verification guard that crashes boot:** `ItemPlugin.verifyIfOptionExists`
   (`engine/src/main/java/org/jesse/game/model/item/pluginextensions/ItemPlugin.java:136-150`)
   runs on every `bind(...)`. It passes iff, for some id in `getItems()`, the def
   `containsOption(name)` (inventoryOptions) **or** `containsParamByValue(name)` (a param whose
   *string value* equals name). **Bypass:** `getItems()` returning `new int[0]` skips the check
   entirely (`PharaohSceptre` already does this). Hazard: if every id resolves to null it throws
   an NPE on `def.getParameters()` instead of the clean message.
6. **`NPCPlugin` never verifies at boot.** `NPCPlugin.verifyOptionExists` is a global `false`,
   never flipped anywhere. So **all missing NPC options degrade silently** (lost right-click in
   game) — they do NOT crash boot.
7. **The cache-packing mechanism itself is sound.** `ItemDefinitions.get(id)` returns the shared
   static instance; `setOption` sets `inventoryOptions[index]`; `encode()` writes them under
   opcodes 35-39; `pack()` writes to the in-memory cache; `cache.close()` flushes to disk *only
   because generateCache opens read-write*. Last session's `generateCache` logged "Cache repack
   took ... ms" (reached `close()`), but that does **not** prove the specific options landed —
   **decode the actual cache to confirm** (see "First, confirm reality").

---

## The boot errors (owner-reported; the pasted log was truncated, expect a few more)
`RuntimeException: None of the items enlisted in <Plugin> contains option <X>`:
- `RottenPotatoItem` — "Utility"
- `ScrollBox` — "Open"
- `RingOfDuelingItem` — "Duel Arena"  (error dump shows the item already has param `451=>Emir's Arena`)
- `RoyalSeedPod` — "Configure"
- `Silverlight` — "Check"

## Affected-plugin audit (from a full read-only sweep — trust but verify against osrs-dumps)

Only **ItemPlugins crash**. The complete crashing/at-risk set:

| Plugin (file) | items | offending option | classification & fix |
|---|---|---|---|
| `RingOfDuelingItem` (engine/.../plugins/item/RingOfDuelingItem.java:37) | 2552-2566 | "Duel Arena" | **VANILLA-RENAME** → rebind plugin to **"Emir's Arena"** (vanilla param 451). Confirm the modern rev-228 name via osrs-dumps. No override. |
| `ScrollBox` (engine/.../treasuretrails/plugins/ScrollBox.java:16) | 2803,2805,2807,2809,2811,2813 (NR-repurposed) | "Open" | **VANILLA-ALIGN** → point plugin (and its drop-table sources / `ClueItem`) at the real OSRS scroll-box ids **24361-24366**, which already have "Open". Confirm those ids + "Open" in osrs-dumps. Bigger change (drop tables). |
| `SledPlugin` (content/generic/.../SledPlugin.kt:13) | 4083,4084,25282 | "Ride" | **LIKELY VANILLA** — vanilla sled siblings 4084/25282 already carry "Ride" so verify passes; just drop the inlined 4083 override. Confirm 4084/25282 have "Ride". |
| `NewMaxCapes` (engine/.../plugins/item/capes/NewMaxCapes.java:45) | 13342,13329,... max capes | "Features" | **LIKELY VANILLA** — confirm a vanilla max cape has "Features"; if so drop override. If not → custom. |
| `RottenPotatoItem` (engine/.../rottenpotato/plugin/RottenPotatoItem.java:25) | 5733 | "Utility","Punishment" | **CUSTOM-NECESSARY** (staff tools). No vanilla equivalent → Open decision. |
| `RoyalSeedPod` (engine/.../plugins/item/RoyalSeedPod.java:27) | 19564 | "Configure" | **CUSTOM-NECESSARY** (vanilla has "Commune" only) → Open decision. |
| `Silverlight` (engine/.../plugins/item/Silverlight.java:12) | 2402 | "Check" | **CUSTOM-NECESSARY** (vanilla has "Wield" only; kill-counter) → Open decision. |
| `PharaohSceptre` (content/.../PharaohSceptre.java) | `new int[0]` | "Enakhra's Temple" | Already **bypassed** (empty getItems). If re-enabled, "Enakhra's Temple" is a VANILLA-RENAME of pyramid param → **"Jaldraocht"**. Leave as-is. |

**NPC overrides that were lost but do NOT crash** (verify off) — restoring the *feature* needs a
cache entry, so they fall under the Open decision too: Ashuelot Reis 11289 (Bank/Collect),
Captain Errdo 6088 (Glider), Captain Rimor 7595 (Layouts), TzHaar-Ket-Keh 7690 (Practice Mode),
dying knight 16023 / Hagavik 16024 (Collect), armoured zombies (Attack = default, fine), rots
brothers. These currently just lose right-click options in-game.

**Not actually affected** (no binding plugin, or bind only vanilla/default options — confirmed):
ice_gloves, pet_mystery_box (Quick-Open unbound), tome_of_experience (30215 unbound),
smouldering_demon (Follower, not ItemPlugin), reward_caskets 7956, graceful_dye, emote_scroll,
clue_progresser, queen_secateurs, snake_weed, wildy_loots (culled), imbue_scroll, starter_weaponry,
mystery_box (no bind), pets, farming.

## Two confirmed deletion MISTAKES from last session (regressions to repair)
1. **`dueling_rings.toml` deleted as "dead"** — WRONG. It set `[item.parameters] 451="Duel Arena"`
   on rings 2552-2566, and `RingOfDuelingItem` binds "Duel Arena", satisfied via
   `containsParamByValue`. The reader *is* the param-value check. Fix by vanilla-rename (above).
2. **`pharaoh_sceptre.toml` deleted** — set op4 "Enakhra's Temple" on sceptres; `PharaohSceptre`
   currently survives only via the empty-`getItems()` bypass. Low priority (feature already
   rework-pending) but note it.

Both were misclassified because the Task-2 audit reasoned "no reader for param X"; the "reader" is
`verifyIfOptionExists`'s `containsParamByValue`. Re-scan any other deleted `[item.parameters]`
TOMLs for the same trap while you have osrs-dumps open.

---

## First, confirm reality (before changing anything)
1. `./gradlew :cache:setupCache` is already done this session (pristine rev-228 at
   `cache/data/cache/`). Decode it (or use osrs-dumps) to answer, per item: does the *vanilla*
   def already contain the option the plugin binds? This turns every "LIKELY VANILLA" above into a
   yes/no and confirms the 4 CUSTOM-NECESSARY ones truly have no vanilla equivalent.
2. Optionally decode item 5733/2402/19564/2803 from the *generated* cache to see whether last
   session's inlined `KeepSetDefinitionOverrides` item options actually persisted (diagnostic only;
   the strategy removes most of them anyway).

## Open decision (owner — resolve with osrs-dumps evidence in hand)
For the genuinely-custom remainder (RottenPotato Utility/Punishment, Silverlight Check,
RoyalSeedPod Configure, and the custom NPC QoL options: Ashuelot bank, CoX layouts, TzHaar
practice, etc.) — the client can only show these if they're in the cache. Options the owner was
weighing:
- **Boot-time in-memory injection** — a small engine-boot injector that calls
  `ItemDefinitions.get(id).setOption(...)` / NPC equivalent on the in-memory cache (which also
  feeds js5 to clients). Disk cache stays pristine, no `generateCache`, features kept. This is the
  "keep it but no generateCache" answer. Must run after `loadDefinitions` and before plugin
  registration (so `verifyIfOptionExists` passes) — find the exact hook in `GameLoader.load` /
  `Main.kt` boot order and confirm js5 group versioning makes clients refetch.
- **Drop them (pure vanilla)** — remove the custom bindings; lose those conveniences.
- **Per-feature** — owner picks keep/drop for each.
The owner deferred this pending the osrs-dumps check — surface each item with its vanilla-vs-custom
finding and let them choose.

## Execution order once decided
1. Vanilla-align the clean cases: RingOfDueling ("Emir's Arena"), ScrollBox (→24361-24366 +
   drop-table/ClueItem repoint), Sled/MaxCapes (drop overrides after confirming siblings). Remove
   the corresponding now-dead entries from `KeepSetDefinitionOverrides.kt`.
2. Handle the custom remainder per the owner's decision (injector, drop, or mixed).
3. Prune `KeepSetDefinitionOverrides.kt` of every item/NPC override that vanilla alignment made
   unnecessary — goal is the cache pipeline stops overriding real items.
4. Rebuild + boot: `./gradlew :cache:generateCache` (grep output for "Something went wrong in";
   exit code is unreliable — `TypeParser.parse` can `System.exit(0)` on error), then
   `./gradlew compileJava compileKotlin`, then boot the server (`runDev`) and confirm **zero**
   `verifyIfOptionExists` throws. `runPluginScanner` for good measure.
5. Commit per-logical-change and push to `claude/sub-agents-investigation-tiyebh`.

## Reference: how the old TOMLs mapped (git history)
`git show HEAD~10:cache/assets/types/item/<name>.toml` (and npc/object/) shows exactly what each
override set (op1..op5 → `setOption(N-1)`, `[item.parameters] K="V"` → param K=value). Use it to
know precisely which option/param each plugin expects. Last session inlined the "REPLACE" set into
`cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/KeepSetDefinitionOverrides.kt`
(methods `packItemOverrides`/`packNpcOverrides`/`packComponents`/`packSpellbookTeleportKeys`) — that
file is where cache-side overrides currently live and is what you'll prune.

## Git state
- Branch `claude/sub-agents-investigation-tiyebh`, all work pushed to `origin`.
- `cache/assets/` no longer exists. Surviving overrides are programmatic in
  `KeepSetDefinitionOverrides.kt`. `diary_info.json` moved to repo-root `data/`.
- Last commit: `8dea602a` (final assets teardown). generateCache + runPluginScanner were green
  last session, but that predates discovering the boot regression.
