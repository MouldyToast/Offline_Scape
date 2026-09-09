# CACHE_KEEPSET — verified keep-set for the custom cache surface

PERMANENT manifest (does not self-delete). Established at Stage 5b @ branch
claude/new-session-lo0m56; re-verify line numbers before relying on them, the
id-level facts are the contract. Anything listed here must NOT be removed by a
later stage without an explicit decision superseding this file.

## (a) Live teleport menu — interface 1601

**Server stack:** engine/src/main/kotlin/org/jesse/game/world/entity/player/teleports/
(TeleportsManager.kt, DestinationTeleport.kt — DestinationTeleport uses
TeleportType.NEAR_REALITY_PORTAL_TELEPORT → RegularStructure, i.e. vanilla anim
714 / gfx 111; no custom anim dependency), content/interfaces/teleports/
(teleports.kt, TeleportInterfaceDialog.kt), SpellbookInterface.java:118-127
(spell-name → category match, sets varp 261), ChatInterface.java "Report
button" → TELEPORTS, pane op3 previous-teleport handlers
(SidePanelsResizablePaneInterface.java:43-45 and the Fixed/Resizable pane
siblings). GameInterface.TELEPORTS(1601, CENTRAL); WALKABLE_INTERFACES entry
GameInterface.TELEPORTS (InterfaceHandler.java). Persistence:
Player.teleportsManager.favoriteDestinations (list of RELATIVE destination
indices), initialized at LoginManager (`getTeleportsManager().initialize`).

**Cache-side source (16 files):** cache/src/main/kotlin/org/jesse/cache/interfaces/teleports/
— Teleports.kt, TeleportsList.kt, Category.kt, Destination.kt,
builder/{TeleportsBuilder,CategoryBuilder,DefaultDestination}.kt,
categories/{training,cities,skilling,wilderness,bosses,dungeons,minigames,misc}.kt,
packing/TeleportsPacker.kt (invoked at TypeParser `TeleportsPacker.pack()`).

**TeleportsPacker outputs:**
- Enums 10000 (index: category idx → category enum), 10001 training, 10002 cities,
  10003 skilling, 10004 wilderness, 10005 bosses, 10006 dungeons, 10007 minigames,
  10008 (search: flat idx → struct), 10009 misc.
- Structs 10000-10229 — **POSITIONAL**: TeleportsBuilder.nextStructID starts at
  10000 and increments per destination in DSL declaration order (230 destinations:
  training 22, cities 43, skilling 49+Blisterwood, wilderness 14, bosses 36,
  dungeons 44, minigames 13, misc 8). Reordering/inserting/removing a DSL line
  shifts every later struct id AND invalidates every player's persisted favourite
  indices. Append-only, ever.
- Params 5000 (name, string), 5001 (spriteID), 5002 (negated-sprite variant) —
  packed from assets/params/5000-5002 by packParams.

**Sprites:** assets/sprites/spellbook_teleport/ — 80 groups, one file each
(`<group>_<sprite>.png`), packed by TypeParser.packSprites (this dir ONLY):
27 30 33 37 54 55 77 80 83 87 104 105 323 341-348 360 373 391-398 410 544 545
547 549 555 556 557 586 594 595 597 599 605 606 607 636 1752-1754 1756-1759
1761 1832-1834 1836-1839 1841 1908-1915 1938-1945.

**Types tomls (TypeParser.parse of assets/types):**
- spell_items.toml — 8 blocks; params 601/602 on the spellbook dummy items.
  The 601 strings ("Training Teleports", "Skilling Teleports", "Minigames
  Teleports", "Wilderness Teleports", "Bosses Teleports", "Dungeons Teleports",
  "Cities Teleports", "Misc Teleports") must stay BYTE-IDENTICAL to the DSL
  category names — SpellbookInterface matches
  `TeleportsList.getCategories().get(spellName.toLowerCase())`.
- component/spellbook_secondary_home_teleports.toml (218:4/99/143 op2+clickmask).
- component/interfaces/open_teleports.toml — op3="*" clickmask=14 on 161:64,
  164:57, 548:68. NOTE: possible 161:64-vs-65 off-by-one ambiguity (unresolved;
  verify against the live pane blob before ever editing).
- item/tp_scrolls.toml — vanilla price edits (12938/13249/21802), NOT teleport-menu
  related but keep (vanilla-edit class).

**Packed blobs (GenericDataPacker packAll of assets/packed/):**
archive_3/1601 (28 component files 0-27), panes archive_3/161 (98), 164 (97),
548 (95); also vanilla overrides 465/476/621/629 in the same tree (see (e)).

**CS2 closure:** server invokes 10527 (search toggle) and 10592 (previous-teleport
name). The 1601/pane scripts live inside archive_12's contiguous custom block
10500-10718 (shared with other custom UIs). CAVEAT: the closure was
byte-scan-derived from component blobs — do not prune individual ids inside
10500-10718; the block lives or dies whole (Stage 6 decision).

**Varps:** 261 = selected-category scratch — SHARED (also makeover mage, xp lamps,
slayer counts, lecterns...). Favourites bitmask, in updateFavorites order:
263, 264, 265, 266, 262, 3808, 3809 (7 × 32 = 224 bits).

**Known bugs (documented, not licenses to refactor):**
- Favourites capacity 224 < 230 destinations; TeleportsManager.updateFavorites
  CLEARS ALL favourites if any favourite index ≥ 224 (indices 224-229 are
  un-favouritable landmines).
- Positional struct-id fragility vs persisted favourite indices (above).
- Destination.wikiURL is dead weight (never packed, never read).

## (b) Tournament keep-set (Stage 3 §0.1, post-Stage-4 state)

Pack calls for regions 13426 (final_*) and 13428 (tourney_*) — currently
TypeParser:1180-1186; assets/map/osnr_tournament/ (4 files);
types/tournament.toml (types/object/tournament_portal.toml died in Stage 4 with
the portal — entry is via Tournament Guard / ::tourny); models 60456
(rebirth_obj_tourny_supplies) and 60476 (rare_drop_table) in
NearRealityCustomModelMap; enum 10024 (NearRealityCustomEnumsPacker:152);
content/minigames/tournament/. Instancing blast radius: regions 13426-13433
(512×512 copy window) — nothing may ever be repacked there.

## (c) Interface 1722 — Bounty Hunter overlay

GameInterface.BOUNTY_HUNTER_CUSTOM(1722, BH_OVERLAY) + its handler
(content/interfaces/bounty-hunter/); assets/interfaces/1722 (source-built, not
a packed blob); TypeParser postPackEdits() nudges components 1722:3 and 1722:21
to x=215,y=30 — the nudge and the interface live together.

## (d) Permanent-keep pipeline pieces

- AnimationBase entries PLAYER(5000), PLAYER_ALT(5005) + holiday bases
  TRICK_HALLOWEEN_EMOTE(5002), THANKSGIVING_TURKEY(5003), THANKSGIVING_POOF(5004)
  (+ assets/animations/bases/*.dat), and the pack(int,byte[]) overload.
- FramePacker + PackerExt.kt (origins defaultSkins/defaultSkeletons reset/add/write).
- packHighRevision() retained as FramePacker.write() + AnimationBase.pack().
- packDynamicConfigs: diary enums (enum 1974 + DiaryInfo loop) — live diary UI.
- increaseVarclientAmount() — varc capacity for custom varcs.
- Vanilla-edit class that survives 5d: copyMaps region clones, Effigy rows,
  duelArena(cache), tp_scrolls.toml-style price edits.

## (e) Stage-6 pointer table — unowned flags from the roster audit

Verified to exist, owner not yet established; do not delete without a Stage-6 claim:
- assets/structs 10301-10351 (51 files) + 5601-5603.
- assets/params 5003-5028 (contiguous block after the teleport params 5000-5002).
- Vanilla interface override blobs archive_3/161, 164, 465, 476, 548, 621, 629
  (161/164/548 partially owned by (a) — the teleport op3s ride inside them).
- archive_12 CS2 block 10500-10718 (see (a) caveat) + the other custom bands
  (10034-10048, 10102-10121, 10200-10202, 10400-10405, 10800-10810,
  10900-10912, 12540-12586, 13511-13516, 20050-20606, 30031-31984,
  32000-32157, 33500-34038, 41025, 44035, 45568-45999, 50520-50711).
- TypeParser.removeCATasks() and packClientBackground() (login branding).
- Objects 47567/47568 edits (NearRealityCustomObjectsPacker).
- packMap(6729, "pvm_arena") — "not really custom, converted post-rev-209".
- copyMaps() clones; modifyRegions regionID==8036 arm.
- npc_options.json; assets/inv/134, 169, 620.
- assets/osnr/custom_cs2/ (its packer was deleted in 5b; dir unowned).
- Ordering hazard: enum packAll runs BEFORE the Kotlin custom packers — later
  packers can silently overwrite earlier group writes; preserve invocation order
  in TypeParser when touching anything above.
