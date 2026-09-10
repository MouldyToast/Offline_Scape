# PLAN: Remove cache/assets — Custom Packed Assets (v2, verified)

> **Goal:** Delete the entire `cache/assets/` directory and every piece of code
> that reads from it. The pristine rev-228 cache (from OpenRS2 ID 2043) should
> be the only cache data source. No custom-packed assets survive.
>
> **Constraint:** Don't break real existing content. Some assets feed real OSRS
> content that works today (achievement diaries, NPC option overrides, PvM Arena
> map data). Those data sources must be replaced or the content code must be
> adapted, not just deleted.
>
> **Scope:** 758 files across 9 subdirectories + 2 top-level JSON files.
>
> **Verification status:** v2. Every claim in v1 was verified against the tree
> by three independent investigation agents (2026-09-10). All inventory counts,
> consumer paths, line numbers, and interface IDs checked out; the corrections
> found are integrated below and summarized in the changelog at the bottom.

---

## Current Inventory

| Path | Files | What it is | How it enters the cache |
|---|---|---|---|
| `packed/misc/` | 518 | Pre-packed binary blobs injected by archive/group/file ID. Archive 3 = interfaces (groups 161, 164, 548, 1619, 1706, 1707, 1708). Archive 2 = configs (enum_8, 112 enum files, many in vanilla ID range). Archive 12 = clientscripts (34 flat files: 647, 1074, 10501–10507, 10510–10526, 10528, 10529, 10565, 10599, 10610, 10628, 10673, 50520). | `GenericDataPacker.packAll()` (TypeParser.java:180) reads `assets/packed/`, walks archive→group→file structure, writes raw bytes into the cache. |
| `types/` | 70 | TOML definition overrides — items (29), NPCs (12), objects (12), components (5 + 1 in `component/interfaces/`), sprites (2), PvM Arena defs (2), plus 7 top-level TOMLs (`disable_default_player_icons`, `drop_tables`, `farming`, `magical_wheat_object`, `pets`, `spell_items`, `tournament`). | `TypeParser.parse(new File("assets/types"))` (line 156) — the TOML reader pipeline applies `inherit=<vanilla_id>` + field overrides, then `pack()` writes modified defs into the cache. Also read by the (dead) generator `NPCPlugin.main()` at `engine/.../NPCPlugin.java:64`. |
| `structs/` | 54 | Raw binary struct definitions: **5601–5603** (teleport leftovers overwriting vanilla IDs) **+ 10301–10351** (Group Ironman challenge metadata). | `TypeParser.packStructs()` (line 476) — reads each file by numeric name, packs as a struct config. |
| `sprites/` | 48 | PNG images: `FinalGameCrowns/` (28, NR rank icons), `chat_icons/` (12, NR chat sprites), `background/` (3, login screen — only 2 are read), `pointers/` (4, **orphaned, zero references**), `mobile_login_button.png` (1). | Two paths: (1) `TypeParser.packClientBackground()` (line 363) reads only `background_desktop.png` + `background_logo.png` (`background_mobile.png` is unreferenced). (2) `SpriteReader` (SpriteReader.java:60) reads `sprites/` paths referenced from `types/sprite/*.toml`. |
| `params/` | 31 | Raw binary param definitions (IDs 689, 690, 5000–5028). Only **5011** has a server-side reader (`Challenge.kt` maxCount). | `TypeParser.packParams()` (line 496) — reads each file by numeric name. |
| `interfaces/1722/` | 26 | Raw binary interface 1722 components (BH custom overlay). | `TypeParser.packInterfaces()` (line 600) — walks `assets/interfaces/` directories (1722 is the only one). |
| `map/osnr_tournament/` | 4 | Custom map data (`final_*` = region 13426, `tourney_*` = region 13428; the latter injects WorldObjects 35006/35007). | `TypeParser.packMaps()` lines 873–878 — hardcoded `packMapPre209()` calls. (Lines 863–872 also pack regions 11567/11595/13109 with in-code `Regions.inject` and **no asset files** — not part of this removal.) |
| `inv/` | 3 | Raw binary inventory definitions: 134 = duel stakes (size 28), 169 = rune pouch (size 4), 620 = collection log (size 1000 — **clobbers** KeepSet's deliberate 2500, bug B4). | `TypeParser.packInvs()` (line 462) — reads each file by numeric name. Runs at :170, AFTER `KeepSetDefinitionOverrides.pack()` at :160. |
| `osnr/custom_maps/` | 2 | PvM Arena map data (`pvm_arena_landscape.dat`, `pvm_arena_objects.dat`). | `NearRealityCustomMapsPacker.pack()` — packs region 6729 (sole call; comment "Not really custom but converted for post rev 209"). Called from TypeParser line 176 behind `ENABLED_MAP_PACKING`. |
| `diary_info.json` | 1 | Achievement diary task metadata (area, complexity, auto-complete flags). | **Two readers:** `DiaryInfo.load()` (cache module, default `assets/diary_info.json` at line 30) is called from `TypeParser.java:399` during **cache packing**; `AchievementDiaries.java:42` (engine) reads hardcoded `cache/assets/diary_info.json` at **server boot**. |
| `npc_options.json` | 1 | NPC right-click option map, 20,567 entries, 65.6% all-null. **Dead artifact:** written by the standalone generator `NPCPlugin.main()` (`NPCPlugin.java:75`), read by nothing — `NpcActions.loadUsedNpcOptions` has zero callers. |

---

## Consumers — Code That Reads From assets/

### Cache packing pipeline (runs during `generateCache`, cwd = `cache/`)

| Consumer | File | What it reads | Method |
|---|---|---|---|
| **TypeParser** | `cache/src/main/java/mgi/tools/parser/TypeParser.java` | `assets/types`, `assets/sprites/background`, `assets/inv`, `assets/structs`, `assets/params`, `assets/interfaces`, `assets/map/osnr_tournament` | `parse()` :156, `packClientBackground()` :363, `packInvs()` :462, `packStructs()` :476, `packParams()` :496, `packInterfaces()` :600, `packMaps()` :862, `postPackEdits()` :203 (1722:3 and 1722:21 x/y tweaks) |
| **GenericDataPacker** | `cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/GenericDataPacker.kt` | `assets/packed/` (all subdirs; bucket dir name is arbitrary) | `packAll()` — called from TypeParser:180. ⚠ No null check on `listFiles()` — deleting `packed/` while the call remains = NPE. |
| **NearRealityCustomMapsPacker** | `cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomMapsPacker.kt` | `assets/osnr/custom_maps/` | `pack()` — called from TypeParser:176 |
| **SpriteReader** | `cache/src/main/java/mgi/tools/parser/readers/SpriteReader.java` | `./assets/sprites/*` (paths from `types/sprite/*.toml` `[sprite.images]`) | `read()` :60 |

### Runtime / other consumers (cwd = repo root for app tasks)

| Consumer | File | What it reads | Status |
|---|---|---|---|
| **AchievementDiaries** | `engine/src/main/java/org/jesse/game/content/achievementdiary/AchievementDiaries.java:42` | `cache/assets/diary_info.json` | **LIVE — server boot.** v1 missed this. |
| **DiaryInfo** | `cache/src/main/java/org/jesse/game/content/achievementdiary/DiaryInfo.java:30` | `assets/diary_info.json` | LIVE — but during cache packing (TypeParser:399), not server boot. |
| **NpcActions** | `cache/src/main/java/org/jesse/game/world/entity/npc/actions/NpcActions.java:19` | `assets/npc_options.json` | **DEAD** — `loadUsedNpcOptions` has zero callers. |
| **NPCPlugin.main()** | `engine/src/main/java/org/jesse/game/world/entity/npc/actions/NPCPlugin.java:64,:75` | reads `cache/assets/types`, writes `cache/assets/npc_options.json` | **DEAD generator** — no gradle task, exits after writing a file nothing reads. |
| **Cache2018Dump** | `cache/src/main/kotlin/org/jesse/cache_tool/dumping/Cache2018Dump.kt:54` | absolute machine-specific path to `.../assets/osnr/easter_2024` (dir doesn't exist) | DEAD — cleanup candidate. |

⚠ **Silent-failure hazard:** `TypeParser.parse` catches all exceptions, logs
"Something went wrong in …", and calls `System.exit(0)`. A missing `assets/`
subdirectory makes `generateCache` "succeed" (exit 0) with a half-packed cache.
Verification must scan output for that log line, not trust the exit code —
or fix the exit code as part of this work.

---

## Investigation Tasks

Verdict vocabulary: **DELETE** (remove asset + all code that reads it),
**RELOCATE** (asset serves real content — move to `data/` and update the
reader), **REPLACE** (inline into `KeepSetDefinitionOverrides.kt`),
**KEEP_WITH_FEATURE** (tied to an open content decision — flag for Jesse).

### Task 1: `packed/misc/` — Binary Interface & Config Overrides

**Investigate:** For each archive/group, determine what the packed data
overrides in the vanilla cache and whether any surviving server content
depends on the override.

**Procedure:**
1. Groups (verified): `archive_3/{161(98f),164(97f),548(95f),1619(9f),1706(13f),1707(49f),1708(11f)}`,
   `archive_2/enum_8` (112 files), `archive_12` (34 flat files, IDs listed in
   the inventory table). Nothing else exists under `packed/`.
2. For archive_3 groups:
   - 161 = `RESIZABLE_PANE`, 164 = `SIDE_PANELS_RESIZABLE_PANE`,
     548 = `FIXED_PANE` (GameInterface.java:204–207). These are the gameframe
     root interfaces. **Check:** do the packed files add components that server
     code references (`sendComponentSettings`, `sendComponentText`, etc.)?
     Search for component references exceeding the vanilla component count.
   - 1619 = `TOURNAMENT_SHOP`, 1706 = `TOURNAMENT_OVERLAY`,
     1707 = `TOURNAMENT_PRESETS`, 1708 = `TOURNAMENT_VIEWER`
     (GameInterface.java:44–51). Tied to the tournament decision (Task 6).
3. For archive_2/enum_8: 112 enum overrides with IDs from 273 upward — many in
   vanilla range, so removal can silently change vanilla lookups (Risk 2).
   Check which server code reads those enum IDs via `EnumDefinitions.get()`.
4. For archive_12: file names are CS2 script IDs (`packClientScripts()` in
   TypeParser is empty — this is the sole CS2 entry path). Check
   `sendClientScript()` call sites and osrs-dumps `script/` to classify
   vanilla-replacement vs custom. Note the 10501–10673 band pairs with the
   struct 10301+ band (GIM challenges UI) — cross-check with Task 3.

**Expected verdict per group:** Most are NR-custom → DELETE. Gameframe
overrides (161/164/548) need careful checking — they may add the BH overlay
slot or other NR UI elements that still render. Tournament groups →
KEEP_WITH_FEATURE.

### Task 2: `types/` — TOML Definition Overrides

**Investigate:** For each TOML file, determine whether the definitions it
creates or modifies are referenced by surviving server content.

**Procedure:** as v1 (parse IDs, grep references, classify), with corrected
known facts:

- `types/npc/custom.toml` — NR custom NPCs: Angel of Death 16041 + 16044,
  Stone Angels 16042/16043 (models 60164/60165), pet NPC 10960, 16065 (Hans),
  16076 (Brother Jacmob), 8 toapets entries, **plus** object 29300 (RDI
  Bonfire) and item 25104 — not purely NPCs. Almost certainly DELETE.
- `types/pets.toml` (**top level**, not `types/item/pets.toml`) — item 30003
  "Jal-ImRek", item 30005 "Wyrmy", NPCs 16016/16018. DELETE.
- `types/pvm_arena/` — `team_portal.toml` (objects inheriting 43765/43767) and
  `sir_eldric.toml` (NPC inheriting 3516). Consumed by PvmArenaModule /
  PvmArenaSirEldric*. PvM Arena is kept → RELOCATE or REPLACE.
- `types/tournament.toml` — item 30006 "Tournament Rune Pouch" **plus NPCs
  16011/16012 (Tournament Guard) plus objects 35005/35006/35007**.
  KEEP_WITH_FEATURE (tournament decision).
- `types/sprite/` — `mod_crowns.toml` overrides sprite group 423, slots
  **4–7, 13, 21, 22, 43–89** (14–20 commented out);
  `mobile_login_button.toml` overrides group 2134. DELETE (NR staff system) —
  but confirm no surviving code references those slot indices.
- `types/component/` — 5 override TOMLs: bank deposit box title (192:1), skin
  colour (205:*), pre-EoC keybinds button (121:104–106), spellbook secondary
  home teleports (218:4/99/143), XP multiplier option (160:5). Check each
  individually. **Plus** `types/component/interfaces/easter_noticeboard.toml`,
  which **creates new interface 1713** (clones 220:0–18) — check for code
  referencing interface 1713; missed in v1.

### Task 3: `structs/` — Struct Definitions 5601–5603, 10301–10351

**Resolved (v1 hypothesis was wrong — not store/donation tiers):**

- 10301–10351 are **Group Ironman challenge** metadata (names like "First
  Group to have all 5 members maxed").
- **Live dependency:** `content/other/group-ironman/.../IronmanGroupChallenges.kt`
  registers structs 10305, 10306, 10308–10315, 10319, 10320; each registration
  runs `StructDefinitions.get(structId).getParamAsInt(5011)`
  (`engine/.../challenges/Challenge.kt`) — **a missing struct is an NPE at
  plugin init**. The GIM-challenges dependency must be resolved (cull the
  challenge system, or inline these structs + param 5011 into
  `KeepSetDefinitionOverrides`) BEFORE deleting `structs/`.
- 10301–10304, 10321–10351 have no server-side reader (likely CS2-side via
  archive_12 — cross-check Task 1) → DELETE with the corresponding scripts.
- 5601–5603 ("Teleport"/"Teleport2"/"Teleport3") **overwrite vanilla struct
  IDs** — leftovers of the deleted teleport-1601 system → DELETE.

### Task 4: `params/` — Param Definitions 689, 690, 5000–5028

**Mostly resolved:** only **5011** has a server-side reader (`Challenge.kt`
maxCount; binary default 0x7FFFFFFF matches). 689/690 and the rest of
5000–5028 have zero code references — remaining check is CS2-side usage via
archive_12 (Task 1). Fate is coupled to Task 3's GIM-challenges resolution;
everything except (possibly) 5011 → DELETE.

### Task 5: `interfaces/1722/` — Bounty Hunter Custom Interface

**Investigate:** Confirmed NR BH overlay: `BOUNTY_HUNTER_CUSTOM(1722,
BH_OVERLAY)` (GameInterface.java:122), `BountyHunterVars.kt:36 I_PARENT=1722`.

**Corrected scope:** `GameToggles.BH2020_ENABLED` (defaults **true**) gates
only `BountyHunterController.process()` — it is NOT a blanket gate. ~2400 LOC
across 23 files run unconditionally, with call sites threaded through
`Player.java`, `PlayerVariables.java`, `TickVariable.java`, `WorldThread.java`,
all four `*Combat.java` classes, `SpellbookTeleport.java`, `PlayerExt.kt`,
`CombatUtility.kt`, etc. If BH2020 is culled, that whole entanglement goes with
the interface data and `postPackEdits()` (Task 12). Delete both or neither
(Risk 4). BH2020 is listed as awaiting cull in PROJECT_MAP → KEEP_WITH_FEATURE
pending final confirm.

### Task 6: `map/osnr_tournament/` — Tournament Map Data

**Investigate:** Tournament keep/cull is an open checkpoint (PROJECT_MAP:298).

**Corrected facts:** `content/minigames/tournament/` is **active** — 33 files,
3502 LOC, auto-registered via `ServerLaunchEvent` (`TournamentModule.kt:33`).
Regions 13426/13428 are referenced only by the packer (TypeParser:873–878),
but the lobby constant `Location(3358, 7460, 0)`
(`TournamentLobbyArea.kt:151`) lands in region 13428 implicitly. A cull must
cover: maps + TypeParser:873–878 + content module + `types/tournament.toml` +
`packed/misc/archive_3/{1619,1706,1707,1708}` + enum 10024 in
`KeepSetDefinitionOverrides.packEnums()` + the related GameInterface entries
(incl. TOURNAMENT_SPECTATING 154, TOURNAMENT_SPECTATING_INVENTORY 374,
TOURNAMENT_SUPPLIES 100). Note: the KDoc on `packEnums()` says "Tournament
content is kept", contradicting PROJECT_MAP's "undecided" — Jesse to resolve.
KEEP_WITH_FEATURE.

### Task 7: `inv/` — Inventory Overrides 134, 169, 620

**Resolved:** 134 = DUEL_STAKE + OPPONENT_STAKE (two ContainerType entries
share inv id 134), 169 = RUNE_POUCH, 620 = COLLECTION_LOG.

- **620 → DELETE and nothing else:** the KeepSet override (size 2500) already
  exists, and `inv/620` (size 1000) currently **clobbers** it because
  `packInvs()` (:170) runs after `KeepSetDefinitionOverrides.pack()` (:160)
  — audit bug B4. Deleting the file fixes the bug. Do NOT "move to KeepSet".
- 134/169: diff against vanilla rev-228 values; if identical → DELETE, if a
  real fix → REPLACE into KeepSet.

### Task 8: `osnr/custom_maps/` — PvM Arena Map Data

**Investigate:** PvM Arena is kept by decision (4577 LOC,
`content/minigames/pvm-arena/`). `NearRealityCustomMapsPacker` packs region
6729 (sole call, `old = false` → routes through `TypeParser.packMap`).

**Procedure:** unchanged from v1 — determine whether region 6729 exists in
vanilla rev-228 (format conversion) or is post-228 content. Either way the
packer serves kept content → RELOCATE map files to `data/` and keep the packer
(renamed), updating `AssetsBase.kt` path resolution.

### Task 9: `sprites/` — Login Screen, Crowns, Chat Icons, Pointers

1. `sprites/background/` — `background_desktop.png` + `background_logo.png`
   are live (TypeParser:364,373). **`background_mobile.png` is orphaned
   (zero references) → DELETE without investigation.** Custom-vs-vanilla
   login screen remains Jesse's call for the two live files.
2. `sprites/FinalGameCrowns/` + `chat_icons/` — feed `mod_crowns.toml`
   (sprite group 423, slots 4–7/13/21–22/43–89). DELETE with the TOML;
   check no surviving code references those slot indices.
3. `sprites/pointers/` — **orphaned: zero references in any TOML, packer, or
   code** (the large siblings died in c2f2460e; these `_small` variants
   survived on filename mismatch). DELETE without investigation.
4. `sprites/mobile_login_button.png` — referenced only from
   `mobile_login_button.toml` (group 2134). DELETE with the TOML.

### Task 10: `diary_info.json` — PRE-DECIDED: Relocate (Phase 0)

**Not an investigation — but v1 missed a reader.** Two consumers, two working
directories:

- `DiaryInfo.java:30` (cache module) — default `assets/diary_info.json`,
  invoked during `generateCache` (cwd = `cache/`).
- `AchievementDiaries.java:42` (engine) — hardcoded
  `cache/assets/diary_info.json`, server boot (cwd = repo root, per
  `app/build.gradle.kts` `workingDir = ../`).

Move the file to repo-root `data/diary_info.json` and update **both** paths
respecting each cwd: DiaryInfo → `../data/diary_info.json` (or resolve via an
absolute helper like `AssetsBase.kt`), AchievementDiaries →
`data/diary_info.json`. Verify both `generateCache` and server boot afterwards.

### Task 11: `npc_options.json` — Dead Generator Artifact

**Resolved (v1 premise was wrong):** nothing reads this file at runtime or
otherwise. `NpcActions.loadUsedNpcOptions` has **zero callers**
(CACHE_ASSETS_AUDIT.md:116 concurs). The file is *written* by the standalone
generator `NPCPlugin.main()` (`NPCPlugin.java:55–86`; write at :75; also reads
`cache/assets/types` at :64), which has no gradle task. The live NPC option
map is rebuilt from `NPCDefinitions` + registered plugin handlers
(`NPCPlugin.filter()`), never from the JSON.

**Verdict: DELETE** — the JSON, `NpcActions.java` (whole class), and
`NPCPlugin.main()` + its private `loadUsedNpcOptions()` (keep the rest of
NPCPlugin — it is the live plugin base class). No loader removal exists to do.

### Task 12: `postPackEdits()` — Interface 1722 Component Tweaks

Confirmed at TypeParser:203–213 (1722:3 and 1722:21 → x=215, y=30, in a second
cache open/close pass). Fate follows Task 5 exactly: BH2020 culled → delete;
kept → keep (and relocate the binary data out of assets/).

---

## Execution Order

```
Phase 0 — Relocate runtime data files FIRST
  ├── Move diary_info.json → data/diary_info.json (repo root)
  │     Update DiaryInfo.java:30  → "../data/diary_info.json"  (cwd = cache/)
  │     Update AchievementDiaries.java:42 → "data/diary_info.json" (cwd = repo root)
  └── Verify generateCache AND server boot; check diary tab
  npc_options.json is NOT pre-moved — it is dead (Task 11) and goes in Phase 2.

Phase 1 — Remaining investigations (parallel)
  └── Tasks 1, 2 (component/enum/script cross-refs), 6 & 5 decisions, 7 (134/169 diff),
      8 (region 6729 vanilla check), 9 (slot-index refs).
      Tasks 3, 4, 10, 11 are already resolved above.

Phase 1.5 — Resolve the GIM-challenges dependency (blocker for structs/params)
  └── Cull IronmanGroupChallenges OR inline structs 10305–10320 + param 5011
      into KeepSetDefinitionOverrides. Without this, deleting structs/ NPEs
      plugin init.

Phase 2 — Delete pure-NR assets (no surviving references)
  ├── Immediate orphans: sprites/pointers/, background_mobile.png,
  │     npc_options.json + NpcActions.java + NPCPlugin.main()/loadUsedNpcOptions()
  ├── Delete assets with "DELETE" verdict
  ├── Remove packing code in TypeParser that reads deleted assets IN THE SAME
  │     COMMIT as the assets (parse() exits 0 on missing dirs — silent failure;
  │     GenericDataPacker.packAll NPEs on missing packed/)
  ├── Remove GenericDataPacker if packed/ is fully deleted
  ├── Remove NearRealityCustomMapsPacker only if PvM Arena maps handled (Phase 3)
  ├── Remove SpriteReader TOML entries for deleted sprites
  └── Delete Cache2018Dump.kt easter_2024 dead path (or the whole dumper if unused)

Phase 3 — Relocate remaining kept assets
  ├── Surviving definition overrides → KeepSetDefinitionOverrides.kt
  ├── PvM Arena maps → data/maps/ (update NearRealityCustomMapsPacker/AssetsBase path)
  └── Login screen sprites (if kept) → data/sprites/ (update packClientBackground)

Phase 4 — Clean up packing pipeline
  ├── Remove empty method bodies in TypeParser (packInvs, packStructs, …)
  ├── Remove packClientBackground() if login sprites are deleted
  ├── Remove postPackEdits() if BH2020 is deleted
  ├── Remove GenericDataPacker.kt if nothing remains in packed/
  └── Delete cache/assets/ directory

Phase 5 — Verify
  ├── ./gradlew clean compileJava compileKotlin
  ├── ./gradlew :cache:resetCache
  ├── ./gradlew :cache:generateCache — exit code is NOT sufficient:
  │     TypeParser.parse System.exit(0)s on error. Grep the output for
  │     "Something went wrong in" (or fix the exit code as part of this work).
  ├── ./gradlew :app:runPluginScanner (runs PluginScanner, no assets dependency)
  └── Boot server: login screen, walk around, diary tab, AND Group Ironman
      challenges init (catches the Task 3 NPE class)
```

---

## Known Decisions (from PROJECT_MAP + prior sessions)

- **PvM Arena:** kept by decision → its assets survive (relocated).
- **Tournament:** keep/cull undecided → flag, don't delete yet. ⚠ Note:
  `KeepSetDefinitionOverrides.packEnums()` KDoc asserts "Tournament content is
  kept", contradicting PROJECT_MAP — resolve which is authoritative.
- **BH2020:** NR system awaiting cull → delete (pending final confirm). Cull
  is larger than the toggle suggests (see Task 5).
- **Teleport interface 1700:** permanent keep; TeleportsPacker already deleted
  (commit 6d285bf2). No `assets/` dependency. (Structs 5601–5603 are stale
  leftovers of the old 1601 system — Task 3.)
- **Origins bosses:** `cache/assets/origins/` already deleted (commit 3a9a3dd3).
  No `assets/` dependency remaining.
- **Login screen:** Jesse's call — custom or vanilla.

## Reference-material caveat

`CUSTOM_ITEM_IDS.txt` corroborates the 30000+ item / 60000+ model custom bands
but is a self-described pre-Stage-5c **tombstone index** — many paths it cites
no longer exist. Use it as history, not as a description of the current tree.
(Similarly stale: PROJECT_MAP mentions `UNIVERSAL_SHOP_FLOODGATE`, which is
gone from GameToggles; CACHE_ASSETS_AUDIT references `archive_3/1601`, already
deleted.)

---

## Risk Areas

1. **Gameframe interfaces (161/164/548):** root display panes. If packed
   overrides add component slots that server code writes to, deleting them
   causes client errors. **Verify component counts before deleting.**
2. **Enum overrides (archive_2/enum_8):** 112 enums, many in vanilla ID range —
   removal silently changes vanilla lookups (returns -1 default).
3. **Struct/param overrides:** confirmed concrete instance — GIM challenges NPE
   at plugin init if structs 10305–10320 / param 5011 vanish (Phase 1.5).
4. **`postPackEdits()` / interface 1722 coupling:** delete both or neither
   (and note the BH2020 code entanglement in Task 5).
5. **Silent packing failure:** `TypeParser.parse` exits 0 on error; assets and
   their reading code must be removed atomically, and generateCache output must
   be scanned, not just its exit code.
6. **inv packing order:** `packInvs()` runs after KeepSet overrides and can
   clobber them (bug B4 today). Any surviving inv override must move to
   KeepSet, not stay as a binary file.

---

## v2 Verification Changelog (what changed from v1)

1. `npc_options.json` is a dead build artifact (written by `NPCPlugin.main()`,
   read by nothing) — Task 11 rewritten; verdict certain DELETE.
2. `diary_info.json` has a second, missed reader (`AchievementDiaries.java:42`)
   and a dual working-directory trap — Task 10 / Phase 0 corrected.
3. Structs are GIM-challenge metadata (5601–5603 + 10301–10351, not
   "10301–10354+"); deleting them NPEs plugin init — Task 3 rewritten,
   Phase 1.5 added.
4. Params: only 5011 is code-read — Task 4 mostly resolved.
5. Missed asset: `types/component/interfaces/easter_noticeboard.toml` creates
   interface 1713 — added to Task 2.
6. `inv/620` currently clobbers KeepSet's 2500 (bug B4); deleting it is the
   fix — Task 7 corrected.
7. Hazards added: GenericDataPacker NPE on missing `packed/`;
   `TypeParser.parse` System.exit(0) silent failure — Phases 2/5 hardened.
8. Consumers table extended: NPCPlugin.main (:64/:75), AchievementDiaries
   (:42), Cache2018Dump (:54).
9. Tournament dependency map broadened (active module, tournament.toml NPCs/
   objects, enum 10024 KDoc contradiction) — Tasks 2/6 corrected.
10. Touch-ups: `types/pets.toml` path; custom.toml full contents; sprite-423
    slot list; orphaned `pointers/` + `background_mobile.png`;
    CUSTOM_ITEM_IDS.txt staleness caveat.

All other v1 claims verified correct: inventory counts (758), all TypeParser
line references, packer behaviors, packed/misc archive contents, all 8
GameInterface IDs, KeepSet collection-log override, PROJECT_MAP decision
quotes, TeleportsPacker/origins already-deleted status.
