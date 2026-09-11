# PLAN: Remove cache/assets — Custom Packed Assets (v3, verdicts final)

> **Goal:** Delete the entire `cache/assets/` directory and every piece of code
> that reads from it. The pristine rev-228 cache (OpenRS2 ID 2043) becomes the
> only cache data source. No custom-packed assets survive.
>
> **Status:** Investigation COMPLETE. Six sub-agents verified v1's claims
> (v2 changelog at bottom) and then resolved every open task to a final
> verdict — including full binary decodes of all 518 packed blobs and
> byte-diffs against the actual vanilla rev-228 cache pulled from OpenRS2.
> All open checkpoints are now DECIDED (2026-09-10, Jesse):
>
> | Decision | Ruling |
> |---|---|
> | Tournament | **CULL** — module, interfaces, enums, scripts, maps, TOML all go |
> | BH2020 | **CULL** — interface 1722, postPackEdits(), emblem trader, ~2400 LOC entanglement |
> | Login screen | **VANILLA** — delete custom backgrounds + packClientBackground() |
> | Rank crowns / chat icons | **REMAP + DELETE** — remap emitted indices ≥60 onto vanilla frames 0–59, then delete TOML + PNGs |
> | GIM challenges | **CULL** — delete IronmanGroupChallenges, freeing structs/ + params/ |
> | Economy TOMLs | **DELETE ALL** (prices, tradeable_items, stackables, amethyst, nail_beast, halos, tp_scrolls) — full vanilla economy behavior |
>
> **Constraint:** Don't break real existing content (diaries, PvM Arena,
> gameframe, rune pouch, instance objects, spell/teleport keying, Puro Puro).
> Verified survivors are inlined into `KeepSetDefinitionOverrides.kt`.

---

## Final verdict summary (all 758 files)

| Asset group | Verdict |
|---|---|
| `packed/misc/archive_3/{161,164,548}` (290 pane files) | DELETE — after byte-diffing the 6 op-bearing files (161:64/65, 164:57/58, 548:68/69) vs vanilla; see Byte-diff gate |
| `packed/misc/archive_3/{1619,1706,1707,1708}` (82) | DELETE (tournament culled) |
| `packed/misc/archive_2/enum_8` (112) | DELETE 87 outright (bug-fix batch 1974/2500-2512/3981-3986, dead custom, GIM index 10026-10028/10031/10045/10046, tournament 10019/10020/10053-10056); RELOCATE-or-delete-after-diff the 10 live-reader enums (273, 708, 1141, 1753, 1756, 1757, 1952, 1953, 2286); byte-diff gate on ~15 unowned vanilla overwrites (4998/4999, 3077, 2513, 2158, 2169, 2173, 2344, 2389, 2390, 2796, 3194, 3292, 3304, 4097, 4218, 4929, 1002, 1904) |
| `packed/misc/archive_12` (34) | DELETE 31 (24 nrteleports closure — orphaned by 6d285bf2; 5 nrdaily orphans 10501-10503/10628/10565; 10673 together with BaseNightmareNPC.java:392; +10599/10610 with tournament cull); byte-diff gate on 647 + 1074 (vanilla-script overrides with live callers) |
| `types/` (70 TOML) | 33 DELETE (some with paired code edits), 25 REPLACE → KeepSet, 7 economy DELETE (decided), pvm_arena 2 REPLACE, emblem_trader DELETE (BH cull), change_skin_colour DELETE (revert to vanilla makeover), mod_crowns DELETE after remap; 4 files SPLIT (rots, reward_caskets, gauntlet, bank) |
| `structs/` (54) | DELETE all — 5601-5603 are stale teleport leftovers overwriting vanilla; 10301-10351 freed by the GIM-challenges cull |
| `params/` (31) | DELETE all — only reader (param 5011) dies with GIM challenges |
| `sprites/` (48) | DELETE all — crowns/chat icons after the index remap; backgrounds per vanilla-login decision; pointers + background_mobile + mobile_login_button are confirmed orphans |
| `interfaces/1722/` (26) | DELETE (BH2020 culled) with postPackEdits() |
| `map/osnr_tournament/` (4) | DELETE (tournament culled) with TypeParser:873-878 |
| `inv/` (3) | DELETE 134 (vanilla=1, consumer dead) and 620 (vanilla=500; file's 1000 clobbers KeepSet's 2500 — bug B4, deletion is the fix); **inv/169 → KeepSet `size = 4`** (vanilla=3; divine/tournament pouch code hard-requires 4 — item-loss risk otherwise) |
| `osnr/custom_maps/` (2) | DELETE + delete NearRealityCustomMapsPacker — region 6729 EXISTS in vanilla (l/m26_73); custom data is a stale pre-209 conversion that is strictly worse (4 locs dropped, 26 locs downgraded 38848→83, ~1,363 plane-3 tiles lost). Verdict supersedes v1/v2's RELOCATE |
| `diary_info.json` | RELOCATE to `data/` — update BOTH DiaryInfo.java:30 (cwd=cache/, use `../data/...`) and AchievementDiaries.java:42 (cwd=repo root) |
| `npc_options.json` | DELETE — never read (zero callers); also delete NpcActions.java and NPCPlugin.main()/loadUsedNpcOptions() (the generator that writes it) |

---

## Byte-diff gate (the one unblocker for every ⚠ item)

No vanilla cache is on disk (`cache/data` is downloaded on demand,
`cache/build.gradle.kts:63`). Run `./gradlew :cache:setupCache`, then diff
these ~30 payloads against vanilla before their deletion commit:

1. **Pane op files:** 161:64, 161:65 (op3 = teleport-previous-destination,
   mask=14), 164:57, 164:58, 548:68, 548:69. If vanilla already carries the
   same op counts → delete all 290 pane files with zero code change (precedent:
   commit bedaf333 deleted archive_3/629 the same way). If not, port the op
   arrays into a KeepSet component edit for those files only.
2. **enum_8 unowned vanilla overwrites** (list above) — CS2-side lookups;
   4998/4999 are a 60-flag mass-disable of unknown ownership.
3. **enum_8 live-reader enums** (273, 1952, 1953, 2286, 1753, 1756, 1757) —
   if identical to vanilla, delete; if NR edits, inline into KeepSet. 708
   contains NR items 30102-30104 → deleting is correct once those items die.
   1141 (tab names; index 8 "Game Noticeboard") resolves with the panes.
4. **archive_12/647 (poh_menagerie_petlist) and 1074 (shop_main_init)** —
   live callers `PetList.java:31`, `ShopInterface.java:45`, `GameShop.java:39`;
   1074 visibly drops vanilla's "Value" op. Diff + arg-signature check;
   deleting a signature-changed script errors every shop open. Highest crash
   risk in the set.
5. **TOML inconclusives:** item 6199 op1 "Open" (mystery_box), bank.toml
   op1/op2 booth parity (its 47345 `sizex=2` fix is real → KeepSet), NPC 13677
   vanilla name (vefari).

---

## KeepSet inlining list (REPLACE verdicts → `KeepSetDefinitionOverrides.kt`)

Pattern already in the file: `InventoryDefinitions.get(id).apply{...}.pack()`,
`TypeParser.cloneObject`, `setOption` (see lines 32-74; object 35015 was
already migrated this way — same treatment).

- **inv 169 → size 4** (rune pouch; see verdict table).
- **Top-level:** `magical_wheat_object` (object 25016 `types=[]`/`cliptype=1`;
  Puro Puro spawns it as type 22 — PuroPuroArea.java:164,213);
  `spell_items` (param-601 spell-name keys that SpellDefinitions.java:48,70
  and the teleport-1700 system rely on — deleting silently re-keys spells).
- **Items:** ice_gloves (+30030), pet_mystery_box (30031/30032),
  tome_of_experience (30215/30216 — NewCrystalChestLoot.kt:67),
  smouldering_demon (33250 + npc 13602 — BossPet.java:160), scroll_boxes
  (ClueItem.java:18-23 + ~10 drop tables; or repoint code to vanilla
  24361-24366 and drop entirely), royal_seed_pod, rotten_potato, sled,
  silverlight (drop the dead param-451 line), max_capes (op4/op5 only —
  op2 "Teleports" is dead), reward_caskets' 7956 "Slayer casket"
  `stackable=1` only (recolours die).
- **NPCs:** armoured_zombies (14113-14122 — region11169 spawns + drop table),
  ashuelot_reis (Bank/Collect live — audit:128 wrong; drop dead op4),
  captain_errdo, captain_rimor, dying_knight (16023 —
  InstancePortal.java:48), hagavik (16024), rots brothers 16035-16040 only
  (pets 16045-16050 die — dangling models), tzhaar_ket_keh.
- **Objects:** godwars_dungeon_instance_objects (drop examine-only 35019),
  dagannoth_kings_instance_objects, instance_objects (mole hill 12202),
  thermonuclear_boss_entrance, cox_steps, gauntlet 36084 only (36080
  Quick-Pass unhandled), catacombs_instance_objects ("Enter-Paid" IS handled
  — KourendStatueObject.java:36; audit:190/266 wrong), vardovis_rock (48741),
  raids_hammer (35020 — injected by MapChanges.java:14-18; audit:130 wrong),
  bank.toml op1/op2 + 47345 sizex=2 (pending parity diff; drop Presets ops).
- **Components:** spellbook_secondary_home_teleports (218:4/99/143 —
  SpellbookTeleport.java:280-292), xp_multiplier_option (160:5 —
  OrbsInterface.java:38 + option-3 branch).

## DELETEs that need a paired code edit (same commit as the asset)

- `drop_tables.toml` (2689/2690, dangling model): fix
  `RareDropTableProcessor.java:29-41` DisplayedDrop icons (audit:184 wrong —
  processor is live).
- `beta_zoo.toml`: remove spawns region10291.kt:83-95, region10547.kt:13-18,
  NpcId.kt:11756-11759.
- `easter_noticeboard.toml`: remove `GameInterface.java:215
  EASTER_NOTICEBOARD(1713)`.
- `archive_12/10673`: remove `BaseNightmareNPC.java:392 sendClientScript(10673)`.
- `mod_crowns.toml` + crown/chat-icon PNGs (decided REMAP+DELETE): first remap
  every emitted index ≥60 onto vanilla frames 0-59 (~30 literals):
  donor crowns 71-83 (`MemberRank.java:16-29` → Crown entries), TRUE_DEVELOPER
  69 → 5, broadcast icon 68 → 13 (`BroadcastType`, `RevenantMaledictus.kt:326`),
  tournament icons 60-67 die with the tournament cull. Delete
  `disable_default_player_icons.toml` (enum 1894) in the same commit.
- `change_skin_colour.toml` DELETE: un-hides vanilla makeover gender buttons;
  no code break (MakeOverInterface binds survive). The rank-gated SkinColour
  feature degrades cosmetically — acceptable under the crowns decision.
- `inv/134` optional cleanup: ContainerType.kt:54-55 (DUEL_STAKE/OPPONENT_STAKE).
- Free cleanups: `mgi/types/component/custom/` (14 classes, zero callers),
  `Cache2018Dump.kt:54` easter_2024 dead path, `AssetsBase.kt`/`Asset.kt`
  (orphaned once NearRealityCustomMapsPacker goes), `SpriteReader.java` +
  its TypeReader registration (no inputs remain once both sprite TOMLs go).

## Cull work packages (decided)

1. **Tournament** — `content/minigames/tournament/` (33 files, 3502 LOC,
   ServerLaunchEvent-registered), `types/tournament.toml`, `map/osnr_tournament/`
   + TypeParser:873-878, archive_3/{1619,1706,1707,1708}, enum_8
   {10019,10020,10053-10056}, archive_12 {10599,10610} + their
   sendClientScript call sites, enum 10024 + KDoc in
   `KeepSetDefinitionOverrides.packEnums()`, GameInterface entries
   (1619/1706/1707/1708/154/374/100), rune pouch 30006 branch in
   `RunePouch.java:45-49`, TournamentPreset announcement icons.
2. **BH2020** — `interfaces/1722/` + `postPackEdits()` (TypeParser:203-213),
   `types/npc/emblem_trader.toml`, `GameToggles.BH2020_ENABLED`,
   `engine/.../bountyhunter/` (kotlin + java, 23 files ~2400 LOC),
   `content/interfaces/bounty-hunter/`, and the threaded call sites in
   Player.java, PlayerVariables.java, TickVariable.java, WorldThread.java,
   PlayerExt.kt, PlayerAttributes.kt, CombatUtility.kt, CombatInfixes.kt,
   four `*Combat.java` classes, SpellbookTeleport.java,
   BountyHunterStoreInterface.java, SpellbookInterface.java,
   GameInterface BOUNTY_HUNTER_CUSTOM/BOUNTY_HUNTER_STORE.
3. **GIM challenges** — `content/other/group-ironman/.../challenges/`
   (IronmanGroupChallenges.kt + registrations), engine `Challenge.kt` usage;
   frees `structs/` + `params/` + enum_8 {10026-10028,10031,10045,10046}.
   GIM groups/storage untouched (IronmanGroupTasks.kt:79 reads a different
   struct band from the vanilla cache — verify before deleting Challenge.kt).

---

## Execution Order

```
Phase 0 — Relocate diary data FIRST
  ├── Move cache/assets/diary_info.json → data/diary_info.json (repo root)
  ├── DiaryInfo.java:30 → "../data/diary_info.json" (cwd = cache/)
  ├── AchievementDiaries.java:42 → "data/diary_info.json" (cwd = repo root)
  └── Verify generateCache AND server boot; check diary tab

Phase 1 — Byte-diff gate
  ├── ./gradlew :cache:setupCache
  └── Diff the ~30 flagged payloads (list above); record outcomes in this doc

Phase 2 — Dead deletes (no decisions, no diffs needed)
  ├── npc_options.json + NpcActions.java + NPCPlugin.main()/loadUsedNpcOptions()
  ├── sprites/pointers/, background_mobile.png, mobile_login_button.{png,toml}
  ├── archive_12 nrteleports closure (24) + nrdaily orphans (5)
  ├── enum_8 dead-custom (57) + bug-fix batch (20)
  ├── inv/134, inv/620
  ├── structs 5601-5603
  ├── dead TOMLs (clue_progresser, emote_scroll, graceful_dye, wildy_loots,
  │     starter_weaponry [fixes bug B6], snake_weed, queen_secateurs,
  │     dueling_rings, pharaoh_sceptre, imbue_scroll rename, farming
  │     [currently clobbers vanilla 50062-50072], hide_tanner,
  │     anti_dragon_breath_shield, bank_deposit_box_title,
  │     pre-eoc_keybinds_button, easter_noticeboard*, beta_zoo*, drop_tables*,
  │     mystery_box op2, economy TOMLs ×7 [decided])
  │     (* = with paired code edit)
  └── mgi/types/component/custom/ (14 classes), Cache2018Dump easter path

Phase 3 — KeepSet inlining (REPLACE list above), then delete the source TOMLs
  └── Includes inv/169 → size 4, and the SPLIT files' surviving halves

Phase 4 — Decided culls
  ├── GIM challenges → then delete structs/ + params/ + GIM enums
  ├── Tournament work package
  ├── BH2020 work package (delete interfaces/1722 + postPackEdits together)
  ├── Crown index remap → delete mod_crowns.toml + FinalGameCrowns/ +
  │     chat_icons/ + disable_default_player_icons.toml + change_skin_colour
  └── Login vanilla: delete background_{desktop,logo}.png + packClientBackground()

Phase 5 — Pipeline cleanup
  ├── Remove pane/enum/script blob dirs per Phase 1 outcomes
  ├── Remove emptied TypeParser methods (packInvs, packStructs, packParams,
  │     packInterfaces, packClientBackground, postPackEdits, map arms 873-878)
  │     — assets and their reading code ALWAYS in the same commit:
  │     parse() System.exit(0)s on a missing dir (silent failure);
  │     GenericDataPacker.packAll NPEs on missing packed/
  ├── Remove GenericDataPacker.kt, NearRealityCustomMapsPacker.kt,
  │     AssetsBase.kt/Asset.kt, SpriteReader.java (+ TypeReader registration)
  └── Delete cache/assets/ directory

Phase 6 — Verify
  ├── ./gradlew clean compileJava compileKotlin
  ├── ./gradlew :cache:resetCache && ./gradlew :cache:generateCache
  │     — exit code is NOT sufficient: grep output for "Something went wrong in"
  ├── ./gradlew :app:runPluginScanner
  └── Boot server: login screen (vanilla), walk around, diary tab, rune pouch
      (divine 4-slot), PvM Arena perimeter walk (26 reverted scenery tiles —
      confirm no new blocking tile crosses the fight polygons), GIM boot clean
```

---

## Corrections this investigation made to CACHE_ASSETS_AUDIT.md

Apply before trusting the audit for anything else: line 116 ✓ (confirmed);
**wrong/stale:** 128 (ashuelot_reis HAS a live handler), 129 (emblem_trader
12113 is BH2020's registered trader), 130 (raids_hammer 35020 injected by
MapChanges), 184 (drop_tables has a live processor), 190/266
(catacombs "Enter-Paid" IS handled), B3 ("geometry-only" false — pane blobs
carry op arrays + onLoad hooks; the open_teleports.toml it cites no longer
exists), §5(a) (teleport-script keep-set stale post-6d285bf2), §3a
(10599/10610/10673 all have live invokers), §3b (enum 10024 blob doesn't
exist), 260-261 (inv 134/169 vanilla sizes now known: 1 and 3), §8.4 (PvM
map keep-set entry unnecessary — region 6729 is vanilla). Also stale docs:
PROJECT_MAP's UNIVERSAL_SHOP_FLOODGATE and archive_3/1601 references,
CUSTOM_ITEM_IDS.txt (self-described pre-Stage-5c tombstone).

## Risk Areas (unchanged from v2, all confirmed real)

1. Pane op arrays (6 files) — byte-diff gate. 2. enum_8 vanilla-ID overwrites
— byte-diff gate. 3. GIM structs NPE — resolved by decided cull. 4.
postPackEdits/1722 coupling — resolved by decided cull. 5. parse()
System.exit(0) silent failure — atomic commits + output grep. 6. packInvs
runs after KeepSet — resolved by inv deletion + KeepSet inlining.

---

## v2 Verification Changelog (corrections to the original v1 document)

1. npc_options.json is a dead build artifact (written by NPCPlugin.main(),
   read by nothing) — Task 11 rewritten; verdict certain DELETE.
2. diary_info.json has a second, missed reader (AchievementDiaries.java:42)
   and a dual working-directory trap — Phase 0 corrected.
3. Structs are GIM-challenge metadata (5601–5603 + 10301–10351, not
   "10301–10354+"); deleting them NPEs plugin init.
4. Params: only 5011 is code-read.
5. Missed asset: easter_noticeboard.toml creates interface 1713.
6. inv/620 currently clobbers KeepSet's 2500 (bug B4); deleting it is the fix.
7. Hazards: GenericDataPacker NPE on missing packed/; TypeParser.parse
   System.exit(0) silent failure.
8. Consumers table extended: NPCPlugin.main (:64/:75), AchievementDiaries
   (:42), Cache2018Dump (:54).
9. Tournament dependency map broadened (active module, tournament.toml
   NPCs/objects, enum 10024 KDoc contradiction).
10. Touch-ups: types/pets.toml path; custom.toml full contents; sprite-423
    slot list; orphaned pointers/ + background_mobile.png; CUSTOM_ITEM_IDS.txt
    staleness caveat.
