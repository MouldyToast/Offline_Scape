# CACHE_ASSETS_AUDIT — dead & removable content in `cache/assets/`

> **Date:** 2026-09-10 · **Branch:** `claude/investigate-first-nn5bb0` · Companion to `CACHE_KEEPSET.md`
> (which this document corrects in several places — see §8).
>
> **Method:** three parallel audits covering every file under `cache/assets/` (869 files):
> (1) sprites / interfaces / inv / maps, (2) `types/` TOMLs + `params/` + `structs/`,
> (3) `packed/` blobs + top-level JSONs + the TypeParser pipeline. Binary payloads were
> decoded directly (map-pack headers, enum blobs, inv configs, struct/param bytes, and a
> 4-byte-BE call-graph scan of every CS2 blob), so classifications below are evidence-based,
> not name-based. Every file is classified:
>
> - **DEAD** — delete now, zero code change
> - **EASY** — delete with a small, named code change (given per item)
> - **KEEP** — verified live consumer (named per item)
> - **UNCERTAIN** — resolution path given per item
>
> **The single highest-value unlock:** this checkout has no vanilla cache on disk. Run
> `./gradlew :cache:setupCache` (downloads OpenRS2 cache 2043 + xteas), then **byte-diff every
> vanilla-id payload against the pristine group**. Anything identical is DEAD with zero code
> change. This one mechanical pass resolves nearly all UNCERTAIN rows (§7).

---

## 1. How the pipeline consumes `cache/assets/` (read this first)

Entry point: `cache/build.gradle.kts` `generateCache` → `mgi.tools.parser.TypeParser.main`.
Line numbers below are `cache/src/main/java/mgi/tools/parser/TypeParser.java` as of this audit.

| TypeParser line | step | reads |
|---|---|---|
| :156-157 | `parse(new File("assets/types"))` | **every** `*.toml` under `types/` (recursive; skips only paths ending `exclude`) |
| :158, :162-166 | `pack(...)` per definitions class | packs everything `parse` collected |
| :159 | `packDynamicConfigs()` | `assets/diary_info.json` (enums 2501-2512) + builds BH enum 1974 from code |
| :160 | `KeepSetDefinitionOverrides.pack()` | (code-defined; incl. inv 620 size=2500) |
| :161 | `removeCATasks()` | (code-defined; enums 3981-3986) |
| :167 | `packClientBackground()` | `sprites/background/background_desktop.png`, `background_logo.png` |
| :168 | `packSprites()` | `sprites/spellbook_teleport/` **only** |
| :169 | `packClientScripts()` | **nothing — empty method** (see §5.6) |
| :170 | `packInvs()` | every file in `inv/` |
| :171 | `packInterfaces()` | every numeric dir in `interfaces/` (currently only `1722/`) |
| :174 | `packMaps()` | `map/osnr_tournament/` (4 files) + inline injections |
| :175 | `TeleportsPacker.pack()` | (code-defined; enums 10000-10009, structs 10000-10229, params 5000-5002) |
| :178-183 | `NearRealityCustomMapsPacker.pack()` (gated `ENABLED_MAP_PACKING = true`, :97) | `osnr/custom_maps/` (pvm_arena, region 6729) |
| :183 | `GenericDataPacker.packAll("assets/packed/")` | every blob under `packed/misc/archive_{2,3,12}` |
| :185 | `KeepSetDefinitionOverrides.packEnums()` | (code-defined; enum 10024 — **after** packAll) |
| :192→:206-214 | `postPackEdits()` | dereferences components 1722:3 / 1722:21 (hard NPE coupling to `interfaces/1722/`) |
| :500-537 | `packStructs()` / `packParams()` | every numeric file in `structs/` and `params/` — **silently overwrites vanilla ids** |

**Two structural facts every future cleanup must know:**

1. **Wholesale scans.** `types/`, `inv/`, `interfaces/`, `params/`, `structs/`, and `packed/`
   are directory scans — deleting any *file* there needs **zero code change**. "EASY" in this
   document means code that dies with (or breaks without) the file, never a packer registry edit.
2. **Last writer wins.** `GenericDataPacker.packAll` at :183 runs **after** almost every code
   packer, and `Group.addFile` overwrites in place (`mgi/tools/jagcached/cache/Group.java:420-430`).
   The blobs therefore silently clobber the code-defined output of :159, :160, :161, :162-166.
   The only code that beats the blobs is what runs after :183 (`packEnums()` at :185).
   `CACHE_KEEPSET.md:130-132`'s ordering warning has the direction incomplete — the hazard runs
   both ways, and today the blobs are winning (and shipping stale data — §2).

---

## 2. Bugs found — stale payloads shipping wrong data TODAY

These are not just cleanups; each fixes live behavior. All are zero- or one-file changes.

| # | bug | fix |
|---|---|---|
| B1 | `packed/misc/archive_2/enum_8/1974` (68 entries) clobbers `packDynamicConfigs()`'s BH reward enum built from `BountyHunterRewardType` (67 rewards, TypeParser :412-422). The blob has an extra key 66 = obj 20716 (uncharged tome of fire), shifting every later index — the BH shop shows a phantom reward and indices past 65 are off by one. | delete blob `enum_8/1974` |
| B2 | `enum_8/2503` is `[9,10,8,4]` but `diary_info.json` (the live source, keep-set (d)) yields `[8,10,8,4]` for Falador — the diary UI over-reports Falador easy tasks by one. The other 11 of 2501-2512 match today but are equally clobber-prone. | delete blobs `enum_8/2500`-`2512` (14 files incl. 2500) |
| B3 | `packed/misc/archive_3/{161,164,548}` (290 pane component files) repack the three vanilla gameframe panes and run **after** the component TOMLs — destroying `types/component/interfaces/open_teleports.toml`'s `op3="*" clickmask=14` on 161:64 / 164:57 / 548:68. **The teleport-tab right-click op is broken in the built cache right now.** String-scan proof: the 290 blobs contain zero op/text strings (geometry-only), so they carry nothing the TOML needs. | delete the 3 dirs (verify against vanilla first — §7); the TOML then takes effect. (Alternative — moving packAll before :157 — re-orders every blob vs. every code writer; don't.) |
| B4 | `inv/620` (4 bytes: size=1000) clobbers `KeepSetDefinitionOverrides.pack()`'s deliberate `size = 2500` collection log (`KeepSetDefinitionOverrides.kt:32-38`, `ContainerType.COLLECTION_LOG(620)`), because `packInvs()` at :170 runs after :160. | delete `inv/620` |
| B5 | `types/npc/custom.toml`'s `id=10960` block ("Pet Angel of Death") overwrites npc 10960, which live code uses as `BossPet.PET_REAVER` (`engine/.../follower/impl/BossPet.java:84`). | delete the block (whole file is DEAD — §3) |
| B6 | `types/item/starter_weaponry.toml` re-models vanilla items 22331/22333/22335 with models 60015-60020 that were **deleted** with `cache/assets/models/` in `c2f2460e` — starter weapons render broken. | delete the TOML (restores vanilla models) |
| B7 | `enum_8/3981-3986` clobber `removeCATasks()`'s filtered combat-achievement enums (TypeParser :161, :906-1164) — the entire 258-line method's output is discarded every build. | pick one side: delete the 6 blobs (filter becomes live) **or** delete `removeCATasks()` + its call (behaviour-neutral, keeps blobs). Decide which behavior is wanted; today the blobs define reality. |

---

## 3. DEAD — delete now, zero code change (~150 files)

### 3a. `packed/misc/archive_12/` (CS2 scripts) — 8 of 36 files

Call-graph proven (every component listener stores script ids as plain `readInt` —
`ComponentDefinitions.java:696-712` — so the 4-byte scan is exact). Live roots are only:
10504/10511 (components 1601/1, 1601/8), 10527 (`teleports.kt:41`), 10592 (`TeleportsManager.kt:34`).

| file | name | why dead |
|---|---|---|
| `10501` | `nrdaily_overview_open` | no invoker anywhere; its interface died in `2a3d4bfe`. **Corrects commit `f28cd69d`**, which restored 10502/10503/10565 believing 10501 was server-invoked — it is not. |
| `10502`, `10503` | `nrdaily_*` procs | referenced only by 10501 |
| `10565` | `string_to_int_nr` | referenced only by 10501 and 10628 |
| `10628` | (challenge-entries script) | no invoker; calls 10633 which was purged in `acc119fc` — already broken |
| `10673` | orphan | no invoker; calls nonexistent 10674 |
| `10610` | tournament-viewer draw | no invoker; `TournamentViewerInterface.kt` fills 1708 server-side |
| `10599` | `nrtourny_presets_open` | no invoker; calls already-deleted 10600/10602. Tournament-adjacent — note it in the commit message, but it is broken today. |

### 3b. `packed/misc/archive_2/enum_8/` — 72 of 112 files

- `10024` — rewritten by `packEnums()` at :185 *after* packAll; the blob never reaches the output cache.
- `1974`, `2500`-`2512` — the B1/B2 bug fixes (15 files).
- **57 orphaned custom enums** whose consumer UIs are already deleted (all ids ≥ 9600, no server
  reference, no surviving CS2/component reference; decoded contents attribute each to its dead owner):
  `9600-9602` (point at structs 10410-10419 which don't exist); `10010-10016`, `10035-10039`
  (game-mode/XP-rate picker → moved to vanilla 890 in `8fbe4b29`); `10017` (iface 1606), `10018`,
  `10048` (1999), `10019`, `10020` (dead tournament script 10599's lists), `10040` (1614), `10044`
  (1615), `10047` (2000), `12053` (2011), `12520` (1711); `10021`, `10032-10034`, `10041-10043`
  (hiscores UI, deleted `2a3d4bfe`); `10022`, `10023` (NR store/claim lists); `10029`,
  `10049-10052` (cape customizer, deleted `2a3d4bfe`); `11000-11014` (donation store — labels,
  perk text, `"$25"…"$5,000"`, `"PayPal"/"Coinbase"`).

### 3c. Top-level and sprites

| item | evidence |
|---|---|
| `npc_options.json` (1.37 MB) | only reader is `NpcActions.loadUsedNpcOptions` (`cache/.../npc/actions/NpcActions.java:22-32`) which has **zero callers**; the live option map is rebuilt from defs in `NPCPlugin.java:89`. Also delete `NpcActions.java` and fix the generator path literal at `NPCPlugin.java:75` (2-line edit) — or leave the code and just note the file regenerates via `NPCPlugin.main`. |
| `sprites/background/background_mobile.png` (361 KB) | zero references (only `_desktop`/`_logo` are packed, TypeParser :367/:376); mobile siblings already deleted in `a45225ce` |
| `sprites/pointers/` (4 files) | no TOML, no packer, no code; the 4 large siblings died in `c2f2460e`, these `_small` variants survived on filename mismatch only |
| `inv/620` | bug B4 above |

### 3d. `types/` TOMLs (whole-file deletions, zero code change)

| file | evidence |
|---|---|
| `types/pets.toml` | items 30003/30005, npcs 16016/16018 (Discord competition pets) — zero references, no Id constants |
| `types/farming.toml` | objects 50062-50072 (deleted NR home/donator zones) — zero references; also collides with dumped `ObjectId` names 50063-50066/50072, so deletion is strictly safer |
| `types/npc/custom.toml` | 16041-16044/16065/16076 unreferenced (models 60164/60165 dangling); `toapets` "Transform" ops have no handler; the 10960 block is bug B5; obj 29300 op rename is name-only (`BonfireObject.java:16-31` handles any option) |
| `types/npc/ashuelot_reis.toml` | npc 11289 Bank/Presets/Collect — no handler, no spawn, zero references |
| `types/npc/emblem_trader.toml` | defines npc 12113; live emblem traders are 7943/308 (`EmblemTrader.java:171`, `EdgevilleEmblemTrader.java:75`) |
| `types/object/raids_hammer.toml` | object 35020 — zero references |
| `types/component/pre-eoc_keybinds_button.toml` | adds a "Pre-EOC Layout" button (121:113-115) with no handler anywhere — a button that does nothing |
| `types/component/bank_deposit_box_title.toml` | pure NR title branding on 192:1; deleting reverts to vanilla text |
| `types/item/wildy_loots.toml` | items 32366/32367 — feature removed in Stage 2, zero references |
| `types/item/starter_weaponry.toml` | bug B6 |
| `types/item/snake_weed.toml`, `queen_secateurs.toml`, `dueling_rings.toml` (duel arena gone), `pharaoh_sceptre.toml`, `silverlight.toml`, `reward_caskets.toml` | cosmetic/param edits with no handler for their added ops (each verified: 0 hits for the op strings / param reads) |

### 3e. `params/` and `structs/`

| item | evidence |
|---|---|
| `params/5010`, `5013`, `5017`, `5023` | referenced by **no** struct in `assets/structs` and no code — leftovers of deleted structs |
| `structs/10307`, `10316`, `10317`, `10318` | their `register(...)` calls are commented out (`IronmanGroupChallenges.kt:36-53, 83-93`) |

### 3f. Adjacent (outside `cache/assets/`, found incidentally)

- `data/enums.json` — no reader anywhere (only `data/components.json` is consumed, by
  `ComponentUpdater.java:267,293`); also duplicates `enum_8/2286` with *different* data.
- `data/items/ItemDefinitions.json.bak`, `ItemDefinitions-beforeFuckery.json` — stale sibling
  copies, not read by the loader.

---

## 4. EASY — delete with a small, named change

### 4a. Sprite frames in `types/sprite/mod_crowns.toml` (chat `<img=N>` badges)

The TOML maps PNGs onto frames of vanilla sprite group 423; consumers are only `<img=N>` chat tags
(complete consumer set: `Crown.java:7-40`, `BroadcastType.java:13-40`, `TournamentPreset.kt`,
plus literals in `DailyChallengeManager.java:69`, `VoteHandler.java:20`, `ShootingStar.java:104`,
`RevenantMaledictus.kt:326`, `Killstreaks.java:174-186`; no `img=` bytes exist in any packed CS2 blob).
History note: `c2f2460e` deleted several of these files and `a9a9aadc` restored 9 of them *because
the TOML still referenced them* — a reference-driven restore, not usage-driven. The genuinely dead
frames (delete the TOML line, then the file):

| frame | file | TOML line |
|---|---|---|
| 21 | (duplicate announcement mapping — line only) | `mod_crowns.toml:18` |
| 22 | `chat_icons/xamphur.png` (Xamphur removed `58a3747a`; XAMPHUR broadcast uses icon 13) | `:19` |
| 47 | `chat_icons/wildy.png` | `:24` |
| 49 | `chat_icons/well.png` | `:26` |
| 52 | `FinalGameCrowns/Donator/Orange.png` (Donator block already commented out at `:11-17`) | `:29` |
| 70 | `FinalGameCrowns/GemRanks/GemRank1.png` (gem crowns start at 71, `Crown.SAPPHIRE(71)`) | `:47` |
| 84-89 | `FinalGameCrowns/Socials_Support/Socials1-6.png` | `:61-66` |

Residual risk: a CS2 script could compute a frame index arithmetically; impact is a missing chat
badge, never a crash.

### 4b. Small paired deletions

| item | change |
|---|---|
| `sprites/mobile_login_button.png` + `types/sprite/mobile_login_button.toml` | delete both — sprite group 2134 frame 0 reverts to vanilla; no code involved. (`c2f2460e` kept it deliberately; branding-policy call.) |
| Interface 1713 (easter noticeboard) | delete `types/component/interfaces/easter_noticeboard.toml` + `GameInterface.java:215` (`EASTER_NOTICEBOARD(1713)` — never opened anywhere) |
| Drop-table icon items 2689/2690 | delete `types/drop_tables.toml` + `ItemId.kt:1599-1600` (`RARE_DROP_TABLE`/`GEM_DROP_TABLE`, zero users; drop viewer removed in `3ad12968`/`225857cb`; model 60476 dangling) |
| Sherlock's notes 30210-30217 | delete `types/item/clue_progresser_item.toml` + `ItemId.kt:12400` (models 60450-60454 dangling) |
| Emote scroll / graceful dye | delete `types/item/emote_scroll.toml` + `ItemId.kt:1590`; `types/item/graceful_dye.toml` + `ItemId.kt:1620` |
| Beta zoo | delete `types/npc/beta_zoo.toml` + `NpcId.kt:11756-11759` (`BETA_*`) + the spawn lines `region10291.kt:83-95` and `region10547.kt:13-18` (~12 lines); also restores vanilla zoo NPC names |
| `types/npc/rots.toml` split | keep brothers 16035-16040 (`AhrimTheBlightedRots.java:24` etc.); delete the pet blocks 16045-16050 ("… the Bobbled" — zero references, models 60181-60186 dangling) |
| Scroll boxes → vanilla ids | `ClueItem.java:18-23` uses NR-repurposed vanilla ids 2803-2813; vanilla scroll boxes exist (`ItemId.kt:11291-11296`, `SCROLL_BOX_*_24361`+). Repoint the six args, then delete `types/item/scroll_boxes.toml` + aliases `ItemId.kt:1689-1699`. (Until then the TOML is load-bearing — KEEP.) |
| Line-level dead ops (edit lines, keep files) | `object/bank.toml` op3 "Presets"/op4 "Last-preset" (no handler; presets UI gone); `item/max_capes.toml` op2 "Teleports" (bind commented out `NewMaxCapes.java:155`); `item/mystery_box.toml` op2 "Quick-Open"; `object/gauntlet.toml` 36080 "Quick-Pass"; `object/catacombs_instance_objects.toml` op2 "Enter-Paid" |

### 4c. Dead pipeline code in `TypeParser.java` (~600 lines) — delete with calls

| code | evidence |
|---|---|
| `packClientScripts()` :540-541 (empty body) + call :169; cascade: `packCs2FromDirectory` (both overloads) :543-579, `packRustyScripts` :581-592, `packClientScriptsRecursive` :594-606, `packClientScript` :608-611, `packClientScriptNamed` :613-616 | all CS2 now enters via archive_12 blobs only |
| `packMapsRSPSi` (both overloads) :681-716 + `modifyRegions` :718-728 | zero callers since the map-pack removal (`79f7d289`); the region-8036 arm `CACHE_KEEPSET.md:126-127` flags as unowned is **unreachable dead code** — no pack ever contained 8036 (pack headers decoded in this session) |
| `portMaps(Cache)` :218-223 + `copyMapRegionFromTargetCache` :810+ | private, no callers (`DataMigration.java` has its own copy) |
| `packSound` :481-484; `packInterface(File,int)` :659-679 | no callers (`packInterfaces()`/`packInterfacesInner` are the live path) |
| `regionsChanged` :1167 + `regionChanged` :1169-1170 | only caller commented out (`DataMigration.java:256`) |
| `ENABLED_MAP_PACKING` :97 + else-branch :180-182 + guard :840-843 | constant is `true`; not a tournament switch — **every** map write funnels through the :840 guard, so flipping it false kills all map packing. Inline it: delete constant, else, guard. |
| `removeCATasks()` :161, :906-1164 | bug B7 — delete method **or** the 6 blobs, not neither |

### 4d. Larger optional bundles (record the decision before cutting)

| bundle | contents |
|---|---|
| **GIM challenge system** (unlocks 22 structs + ~19 params) | delete `content/other/group-ironman/.../challenges/IronmanGroupChallenges.kt` + engine scaffolding `engine/.../content/challenges/{Challenge,ChallengeProgress,ChallengeRegistry,ChallengeType,ChallengesManager}.kt` (no other references); then `structs/10301-10322`, `params/5003-5017`, `params/689,690` all become deletable. Also removes the boot-time NPE coupling (§6). |
| **PvM Arena** (only if the minigame is retired — it is live today) | delete `cache/assets/osnr/` + `NearRealityCustomMapsPacker.kt` + import `TypeParser.java:14` + block :178-183 + orphaned `AssetsBase.kt`/`Asset.kt` (sole consumers verified) + `types/pvm_arena/` |
| **Crown/rank system** (if ever de-customized) | `mod_crowns.toml` + all `FinalGameCrowns/`+`chat_icons/` files + `types/disable_default_player_icons.toml` (companion that blanks vanilla icon-name enum 1894) + `Crown`/`MemberRank`/`GameMode.crownRealist` |

---

## 5. KEEP — verified live (do not re-litigate without new evidence)

| item | owner / evidence |
|---|---|
| **Teleport system closure**: archive_12 scripts 10504-10529 + 10592 + `50520` (26 files), `archive_3/1601` (28), `sprites/spellbook_teleport/` (80), `params/5000-5002`, `types/spell_items.toml`, `types/component/interfaces/open_teleports.toml`, `spellbook_secondary_home_teleports.toml`, `TeleportsPacker` | keep-set (a); call graph traced from live roots. **Note: `50520` sits inside the closure — keep-set (e)'s "50520-50711 band" is NOT wholesale-disposable.** |
| **Tournament set**: `map/osnr_tournament/` (regions 13426/13428), `archive_3/{1619,1706,1707,1708}`, enums 10053-10056, `types/tournament.toml` | live minigame (234 files), `::tourny` command, `TournamentLobbyArea.kt:151`, `TournamentFightArea.kt:38`; enums read by `Enums.java` TOURNAMENT_* |
| **PvM Arena set**: `osnr/custom_maps/` (region 6729 = both fight arenas, `PvmArenaFightArea.kt:23,28`), `types/pvm_arena/{team_portal,sir_eldric}.toml` | live minigame; **missing from CACHE_KEEPSET — add it** |
| **`interfaces/1722/`** (bounty hunter) | `GameInterface.BOUNTY_HUNTER_CUSTOM(1722)`, `BountyHunterVars.kt:36`, `WildyExt.kt:64-97`; `postPackEdits()` NPEs without it |
| **`inv/169`** (rune pouch, 4 slots) | `ContainerType.RUNE_POUCH(169)`, `RunePouch.java:20-40` — custom in origin, live in effect |
| inv 620 *semantics* | the 2500-slot collection log lives in `KeepSetDefinitionOverrides.kt:32-38` (code), which is why the 1000-slot *file* is deletable (B4) |
| **`diary_info.json`** | source of truth for diary counts (`DiaryInfo.load`, TypeParser :423); currently clobbered by the B2 blobs — keep JSON, delete blobs |
| **Crowns/chat icons (live frames)** | announcement(13,54-67), star(51), lottery(50), Clan_icon(53), clan_skull(68), Death(57), vote(48), forum_mod(6), youtuber(7), Staff(4,5,69), Realist(43-46), GemRank2-14(71-83) — all mapped to `Crown`/`MemberRank`/`BroadcastType`/content literals |
| **`background_desktop.png` / `background_logo.png`** | client login background/logo (TypeParser :366-384) |
| **GIM challenge structs 10305-10320 (the 12 registered ones)** | **load-bearing at boot**: `IronmanGroupChallenges.kt:26-98` → `Challenge.kt:10` dereferences the struct — missing struct = NPE during plugin init. Params `5011` likewise |
| **Def-edit TOMLs with live handlers** | `royal_seed_pod` (`RoyalSeedPod.java:27`), `ice_gloves` (30030 in `BlastFurnaceObjectAction.java`), `smouldering_demon` (`BossPet:160`), `tome_of_experience` (`NewCrystalChestLoot.kt:67`), `pet_mystery_box` (`MysteryBox.java:91`), `imbue_scroll`, `rotten_potato`, `max_capes` (op4/5), `sled`, `mystery_box` (op1), `magical_wheat_object` (25016 = `PuroPuroArea:164`), `tzhaar_ket_keh`, `captain_errdo`, `captain_rimor`, `vefari`, `dying_knight`, `hagavik`, `armoured_zombies` (creates npcs 14113-14122 used by combat/drops/spawns/slayer), `rots` brothers, `xp_multiplier_option` (`OrbsInterface.java:67-95`), instance-object TOMLs (godwars/dks/mole/thermo/gauntlet/cox_steps/vardovis_rock), `bank.toml` op1/2 + sizex fix, `tp_scrolls` (keep-set) |
| enum_8 `1141` | sidebar tab-name enum with entry 2 = `"*"` — likely the other half of the teleport-tab wiring; treat as KEEP alongside `open_teleports.toml` |

---

## 6. Booby traps for future sessions

1. **`postPackEdits()` NPEs** if `interfaces/1722/` is deleted (TypeParser :206-214).
2. **Boot NPE** if structs 10305/10306/10308-10315/10319/10320 are deleted while
   `IronmanGroupChallenges.kt` still registers them (`Challenge.kt:10`).
3. **`ENABLED_MAP_PACKING` is not a feature flag** — it gates *all* map writes (:840), including
   the vanilla injections and tournament maps.
4. **packAll ordering** — anything added under `packed/misc/` after this audit will clobber
   code packers again (§1.2). Prefer code packers (`KeepSetDefinitionOverrides`) over blobs.
5. After deleting any `types/npc/*.toml`, regenerate `npc_options.json` via `NPCPlugin.main` —
   or delete that pipeline first (§3c) and skip the ritual forever.
6. `packStructs()`/`packParams()` silently overwrite vanilla ids — `params/689,690` and
   `structs/5601-5603` sit in the vanilla band (see §7).

---

## 7. UNCERTAIN — and exactly how to resolve each

**Step 1 (resolves most rows): `./gradlew :cache:setupCache`, then byte-diff each payload against
the pristine vanilla group. Identical ⇒ DEAD, delete with zero code change. Different ⇒ needs a
named owner before deletion.**

| item | question | resolution |
|---|---|---|
| `archive_12/647` (`poh_menagerie_petlist`), `1074` (`shop_main_init`) | vanilla-script overrides with live server callers (`PetList.java:31`, `ShopInterface.java:45`) — do the blobs differ from vanilla? | byte-diff; if identical delete; if different, check arg-signature compatibility before reverting |
| `archive_3/{161,164,548}` (290 files) | vanilla pane overwrites (bug B3) | byte-diff; delete either way after confirming the TOML replaces the intended bits |
| 46 vanilla enum overwrites in `enum_8` (`273 708 1002 1141 1753 1756 1757 1904 1952 1953 2158 2169 2173 2286 2344 2389 2390 2513 2796 3077 3194 3292 3304 4097 4218 4929 4998 4999`) | several are read by live code (`Enums.java`); `708` adds custom item 30102; `4998/4999` mass-disable 60 booleans | byte-diff each; identical ⇒ DEAD; different ⇒ record owner. **Do not bulk-delete.** |
| `inv/134` (duel stake, size 28) | consumer dead (`ContainerType.DUEL_STAKE` unreferenced; duel arena removed) but vanilla size of inv 134 unknown | byte-diff; deleting the file is zero-code-change either way; the two `ContainerType.kt:47-48` lines are a separate 2-line cleanup |
| `inv/169` | is size=4 a deviation? | byte-diff for the record; KEEP regardless (rune pouch) |
| `params/689,690`, `structs/5601-5603` | vanilla-band ids being silently overwritten — possible NR mistake | byte-diff vanilla PARAMS/STRUCT groups; they die with structs 10321/10322 (GIM bundle) either way |
| GIM client-side leftovers: enums `10026-10028,10031,10045,10046`, structs `10301-10304,10321-10326,10327-10351`, params `5003-5028` | server owner exists only for structs 10305-10320/param 5011; the rest may be read by CS2 | byte-scan the archive_12 10500-10718 band for these ids (same method used for the teleport closure); or take the §4d GIM bundle and delete the lot |
| `types/component/change_skin_colour.toml` | resizes the makeover swatch panel for NR donator-gated colours (`MakeOverInterface.java:24-78`) | delete only together with the rank-gated `SkinColour` block — visual break otherwise |
| `types/disable_default_player_icons.toml` | companion of mod_crowns (suppresses vanilla icon names in enum 1894) | dies with the crown system (§4d), not before |
| `anti_dragon_breath_shield.toml`, `hide_tanner.toml`, `catacombs_instance_objects.toml` op2 | suppression-only / unhandled-op edits | safe to delete on the next pass; listed here only because behaviour is cosmetic-visible |
| Price/economy TOMLs: `prices.toml`, `amethyst_items.toml`, `nail_beast_nails.toml`, `halos.toml`, `tradeable_items.toml`, `stackables.toml` | zero-code-change deletable, but they change economy semantics (GE tradeability of ~90 items, Destroy→Drop swaps, herb-box stacking) | explicit policy decision, not a dead-code call — same class the keep-set blesses for `tp_scrolls` |
| pvm_arena `.dat`s byte-identical to vanilla 6729? | packer comment says "not really custom, converted post rev 209" | diff after setupCache; if identical the whole packer is redundant |

---

## 8. Corrections to `CACHE_KEEPSET.md`

1. **Stale line refs**: tournament pack calls are at `TypeParser.java:897-903`, not ":1180-1186".
2. **`NearRealityCustomModelMap` does not exist** — §(b)'s claim that models 60456/60476 are kept
   there is wrong; `cache/assets/models/` was deleted in `c2f2460e` and no model packer remains.
   All `60xxx` model refs are dangling: `starter_weaponry` (60015-60020), `scroll_boxes`
   (60477-60482), `clue_progresser_item` (60450-60454), `drop_tables` (60476), `tournament`
   (60456), `npc/custom` (60164/60165), `npc/rots` pets (60181-60186). Tournament Supplies
   (obj 35006) currently packs with a broken model — needs a decision (restore a model packer
   for 60456, or re-model onto a vanilla id).
3. **`modifyRegions` region-8036 arm** (§(e) unowned flag): proven unreachable — no pack ever
   contained region 8036; safe to delete with `packMapsRSPSi` (§4c).
4. **PvM Arena is missing from the keep-set** — region 6729 + `types/pvm_arena/` + the packer are
   live (§5); add as a keep-set entry.
5. **`copyMaps()` self-contradiction** (permanent-keep in (d), unowned in (e)): partial owner
   found — `copyMapRegion(10058, 9546)` serves `EvilBobIsland.java` (region 9546). The
   Catacombs clone block (12 calls, 6299-7082) and `copyMapRegion(11605, 11604)` remain unowned.
6. **`assets/inv/620` flag resolved**: the file is DEAD (and harmful, B4); the semantics live in
   `KeepSetDefinitionOverrides`. `134` resolved to DEAD-leaning (§7); `169` resolved to KEEP.
7. **`npc_options.json` flag resolved**: DEAD (§3c).
8. **Structs/params §(e) owners assigned**: 10305-10320 + param 5011 → Group Ironman challenges
   (KEEP, boot-NPE coupling); 10301-10304 / 10321-10326 / 10327-10351 / 5601-5603 / remaining
   params → no server owner found (§7).
9. **CS2 band 50520-50711**: `50520` is part of the live teleport closure — the band cannot be
   dropped wholesale.
10. Commit `f28cd69d`'s restore of 10502/10503/10565 was based on a wrong premise (§3a).

---

## 9. Verification playbook (per cleanup batch)

1. `./gradlew :cache:compileJava :cache:compileKotlin` (add `compileJava compileKotlin` at root
   when engine/content code is touched).
2. `resetCache` + `generateCache` — must complete without new errors; `packDynamicConfigs` and
   `postPackEdits` are the steps most likely to throw if a keep was miscut.
3. Boot the server: zero new WARNs from `ItemDefinitions`, `NPCDefinitions`, plugin init
   (watch for `ChallengeRegistry`/struct NPEs).
4. In-game spot checks tied to the batch:
   - B1: Bounty Hunter shop shows 67 rewards, no uncharged tome of fire, correct prices past slot 65.
   - B2: Falador diary easy-task count matches `diary_info.json` (8).
   - B3: teleport sidebar tab right-click shows the `*` op on all three gameframe modes.
   - B4: collection log opens with 2500 capacity.
   - Sprite frames: send a broadcast / check crowns in chat for missing badges.
   - After npc TOML deletions: affected NPCs' right-click menus in-game.
5. For any vanilla-id payload deleted after a byte-diff showed *difference*: record the diff and
   the named owner in this file before deleting.
