# PLAN: Stage 3 — custom world & maps

**Roadmap:** ROADMAP_custom_cache_removal, Stage 3.
**Verified against:** branch `claude/new-session-lo0m56` @ `4ee1c573` (Stages 1–2 executed). Every FIND block below was read from source and verified unique by the dry-run in Part D on this tree (run: CLEAN).
**Removal order enforced (rule 1):** server content code (Part A) → TypeParser invocations (Part B1) → packer classes (Part B2–B5) → asset files (Part C).

---

## 0. Scope decisions & corrections

### 0.1 User decisions
- **Tournament minigame: KEEP** (roadmap checkpoint 2 RESOLVED). Keep-set: pack calls TypeParser:1429-1430 (region 13426) + :1433-1436 (13428), `assets/map/osnr_tournament/` (4 files), `types/tournament.toml` + `types/object/tournament_portal.toml`, models 60456/60517 in shared rebirth dirs, enum 10024, `content/minigames/tournament/`, `::tourny`. Instancing blast radius: regions 13426–13433 (512×512 copy window from (3328,7296)) — nothing may ever be repacked there.
- **Legacy arenas REMOVE:** `map/dmm_tourny/` (14477/14478/14733/14734) + `map/tournament/` (15245/15248) + 7 orphaned spawn scripts.
- **Island 9517 REMOVE** → the whole world-map layer dies with it (org.jire.wmpacker, NearRealityCustomWorldMapPacker, BuilderExt label helpers).
- **Staff zone 8314 REMOVE** (user chose removal): map, pack call, `::staffzone`/`::sz`/`::si`.
- Plus: runespawn wholesale, dangling donator-zone code (maps pre-deleted in `069c688f`), orphan barrows 13909, primal zone 6582, dead commented map blocks.

### 0.2 Verification-driven corrections
1. **`RDIArea.java` is NOT dead — KEEP.** `PluginType.kt:100` classgraph-scans every `RegionArea` subclass; RDIArea's polygon covers the KEPT elemental regions 11604/11605 (copyMapRegion + live region11605.kt spawns). Deleting it drops multiway + cannon restriction there. Rename belongs to 5b/6.
2. `GameCommands.java:1044-1090` is the `::si` SUPPORT staff-teleport block — dies with the staff zone alongside `::staffzone`/`::sz` (PlayerCommands.kt:55-61). NPC id 8314 in region14486.kt is an unrelated id namespace — untouched.
3. **Primal zone second hook:** `MapChanges.java:17-46` `case 6582` injects/strips objects on the packed landscape — dies with the pack call. Cases 13139/13395/12132 are live keeps.
4. The Stage-2 wild-mole label removal left `CustomWorldMapAreas.changeMainArea`'s `addMapElement(2500,…)` packing a dangling map-element def — removing the world-map layer closes it.
5. **Scope additions (same evidence pattern):** `region11374.kt` (custom barrows brothers 16052-16057 ×3 + Strange old man 16058 — all void terrain), `StrangeOldManRDI.java` (warps into void crypts), `GethinNPC/SigmundNPC/TarikNPC.java` (openShop targets that DO NOT EXIST — `Shop.get` throws; only spawns were in dying scripts), toml defs 16031/16032/16033/16051/16058/16059/16060. **KEPT:** brother defs 16052-16057 + NPCSpawnLoader drop-viewer entries + wight validate() arms (separable slice → 5c).
6. Legacy-arena spawn scripts all verified inside custom-packed or never-mapped regions (incl. region14732/15246 — same `deadman_final_*` NPC family, open-sea band with no vanilla map). No region15245.kt exists.
7. Removing UDI Kylie is safe: vanilla Kylie 7728 spawns at (2614,3446) in region10293.kt; vanilla minnow spots match westernPath/easternPath.
8. **Login-position guard ADDED (A-12):** no static-region relocation existed (only `fixLocationIfInstanceDC` for instances). Saves logged out in removed zones now relocate to home (3087,3490). Pre-builds what Stage 4 expects.
9. `assets/runespawn` = 5 files; `assets/runespawn/interfaces` never existed (TypeParser:940 dead-if-true). `GameConstants.RUNESPAWN` never read/written. `com.runespawn.util` imports in pvm-arena/tournament are an external LIBRARY — excluded from greps.
10. `WorldMapElementExtractor.java` zero call sites. KEEP `mgi/types/worldmap/*` (Definitions.java:98 registers MapElementDefinitions).
11. `dmm_tourny` holds 12 files (4 never-referenced `*59_141` leftovers included) — all die.
12. `BarrowsStaircase` x==2852 warp was already unreachable-in-practice (never-packed crypt band); deletion IS the vanilla restore. `ChaosAltar.LEGENDARY_TILE` sat on the pre-deleted quad island.
13. After B1-t, `packMaps(ExecutorService)`'s parameter is unused — leave; Stage 6 tidy.

---

## Part A — server code

- **A-1** staff commands: delete `::staffzone`/`::sz` block (PlayerCommands.kt) and the 47-line `::si` block (GameCommands.java:1044-1090).
- **A-2** donator access: delete `::ndi` command + `SpellbookTeleport.DONATOR_ISLANDS` enum line.
- **A-3** `Ladder.java`: delete 4 UDI_LADDER entries + PRIMAL_AREA entry.
- **A-4** `MinnowFishingSpot.java`: delete UZONE/UZONE2 arrays; replace the dZone branch with the plain western/eastern path lookup (vanilla platform preserved — its 7730-7733 spawns match those paths).
- **A-5** `BarrowsStaircase.java`: drop the x==2852 → (1624,2614) warp + now-unused Location import.
- **A-6** `ChaosAltar.java`: drop LEGENDARY_TILE and its `matches` clause.
- **A-7** `GlobalImplings.java`: delete donorImplings field, its scheduleCreation block, the log suffix, and the DonatorImplingSpawn nested class.
- **A-8** `NPC.java`: delete ONLY `case 13430, 13431` (9456 / 9369+9370 / 11604+11605 arms KEEP — comments there are stale).
- **A-9** `MageOfZamorak.java`: strip the 16051/RDI branch (ABYSS_LOCATION_CENTER, rdi flag, id array entry).
- **A-10** file deletions: GethinNPC, SigmundNPC, TarikNPC, StrangeOldManRDI, WorldMapElementExtractor + 15 spawn scripts (region11374, 13430, 13433, 13436, 13439, 13441, 13550, 13552, 14477, 14478, 14732, 14733, 14734, 15246, 15248). NOT RDIArea.java, NOT KylieMinnow.
- **A-11** `GameConstants.java`: delete the orphan `RUNESPAWN` field.
- **A-12** login guard: append `removedCustomRegions` + `fixLocationIfRemovedRegion` to `AreaManagerExt.kt`; wrap the LoginManager location line with it (composes after `fixLocationIfInstanceDC`).

Exact FIND text for every tag lives in the embedded dry-run (Part D), byte-identical to source (tabs included for Ladder/SpellbookTeleport/GameConstants).

## Part B — TypeParser / packers / cache-side

- **B-1** TypeParser.java, tags B1-a..u: imports (world-map packer, CustomWorldMapAreas), RUNESPAWN field + args parsing + 3 guarded blocks + commented RuneSpawnMigration block, world-map pack call :211, pack calls for 9517 / dmm_tourny ×4 / legacy tournament ×2 / primal 6582 / staff 8314, dead commented blocks (GWD 1291-1423 range-delete, gamble/world_boss/tutorial, 11601, NR317 donator lines, nr_dzone, quad_dono_island), and the changeGodwarsArea/changeMainArea pair. **KEEP:** tournament 13426/13428 calls, home comments :1439-1440, vanilla-edit packs, copyMaps, Effigy apply, duelArena, ENABLED_MAP_PACKING.
- **B-2** `MapChanges.java`: delete `case 6582` arm (lines 17-46).
- **B-3** `NearRealityCustomMapsPacker.kt`: delete `packMap(13909, "barrows")` (keeps 12145 middleman → 5d, 6729 pvm-arena → live).
- **B-4** `BuilderExt.kt`: delete the 3 map-label helpers (L161-180) + MapElementDefinitions import (L8). JagexColor/toHSL stay.
- **B-5** class deletions: NearRealityCustomWorldMapPacker.kt, `org.jire.wmpacker/` (2 files), RuneSpawnMigration.java. Plus `types/npc/custom.toml` trims: blocks 16031/16032/16033 (B5-a), 16051 (B5-b), 16058/16059/16060 (B5-c). KEEP 16052-16057, 16034 Primula (→5c), RDI-bonfire object 29300 edit (needs own check), dying_knight.toml.

## Part C — asset deletions (31 files)

`git rm -r`: `map/dmm_tourny/` (12), `map/tournament/` (4), `map/primal_zone/` (2), `assets/runespawn/` (5). `git rm`: island_l/m_regular + island_l/m_christmas (4), staff_landscape/objects (2), custom_maps/barrows_landscape/objects (2).
**NOT deleted:** osnr_tournament/ (keep-set), all 15 home assets (Stage 4), mm_*/pvm_arena_* (live), vanilla-edit packs, yanille/, old_duel_arena/, packed branding blob (Stage 6).

## Part D — dry-run gate (validated CLEAN on this tree)

Save as `dryrun_stage3.py`, run before any edit; delete in the execution commit. Asserts: every FIND unique; B1-u range anchors + all-comment + 16 godwars fragments; kept home comment lines present and in no FIND; deletion targets exist with dir counts; keep-guards (tournament keep-set, home assets, mm/pvm assets, RDIArea.java, dying_knight.toml present and not deleted; kept pack calls present and in no FIND; 16052-16057 present and in no FIND).

```python
#!/usr/bin/env python3
"""Stage 3 dry-run gate. Run from the repo root: python3 dryrun_stage3.py
Asserts every FIND block matches its file exactly once, every deletion target
exists with expected counts, the big commented ranges have exact boundaries,
and the tournament/home keep-guards hold. Exit 0 = CLEAN."""
import pathlib, sys

fail = 0
def err(tag, msg):
    global fail
    print(f"[{tag}] {msg}"); fail += 1

TP = "cache/src/main/java/mgi/tools/parser/TypeParser.java"
GC = "engine/src/main/java/org/jesse/game/world/entity/player/GameCommands.java"

FINDS = [
    # ---------------- A1: staff zone commands ----------------
    ("engine/src/main/kotlin/org/jesse/game/content/commands/PlayerCommands.kt",
     '        Command(PlayerPrivilege.SUPPORT, arrayOf("staffzone", "sz"), "Teleport to staff zone.") { p, _ ->\n'
     '            if (p.isLocked)\n'
     '                return@Command\n'
     '            val teleport = RegularTeleport(Location(2080, 7844, 0))\n'
     '            p.sendMessage("You teleport to the Staff Zone")\n'
     '            teleport.teleport(p)\n'
     '        }\n', "A1-a"),
    (GC,
     '        new Command(PlayerPrivilege.SUPPORT, "si", "Teleport to the staff area.", (p, args) -> {\n'
     '            if (p.isLocked()) {\n'
     '                return;\n'
     '            }\n'
     '            final Teleport teleport = new Teleport() {\n'
     '                @Override\n'
     '                public TeleportType getType() {\n'
     '                    return TeleportType.REGULAR_TELEPORT;\n'
     '                }\n'
     '\n'
     '                @Override\n'
     '                public Location destination() {\n'
     '                    return new Location(2078, 7840, 0);\n'
     '                }\n'
     '\n'
     '                @Override\n'
     '                public int getLevel() {\n'
     '                    return 0;\n'
     '                }\n'
     '\n'
     '                @Override\n'
     '                public double getExperience() {\n'
     '                    return 0;\n'
     '                }\n'
     '\n'
     '                @Override\n'
     '                public int getRandomizationDistance() {\n'
     '                    return 3;\n'
     '                }\n'
     '\n'
     '                @Override\n'
     '                public Item[] getRunes() {\n'
     '                    return null;\n'
     '                }\n'
     '\n'
     '                @Override\n'
     '                public int getWildernessLevel() {\n'
     '                    return WILDERNESS_LEVEL;\n'
     '                }\n'
     '\n'
     '                @Override\n'
     '                public boolean isCombatRestricted() {\n'
     '                    return UNRESTRICTED;\n'
     '                }\n'
     '            };\n'
     '            teleport.teleport(p);\n'
     '        });\n', "A1-b"),
    # ---------------- A2: donator island access ----------------
    (GC,
     '        new Command(PlayerPrivilege.PLAYER, "ndi", "Teleport to Donator Islands.", (p, args) -> {\n'
     '            if (p.isLocked()) return;\n'
     '            SpellbookTeleport.DONATOR_ISLANDS.teleport(p);\n'
     '        });\n', "A2-a"),
    ("engine/src/main/java/org/jesse/game/content/skills/magic/spells/teleports/SpellbookTeleport.java",
     '\tDONATOR_ISLANDS(NORMAL, ARCEUUS_TELEPORT, 0, new Location(1663, 2622, 0)),\n', "A2-b"),
    # ---------------- A3: Ladder UDI + primal entries ----------------
    ("engine/src/main/java/org/jesse/game/world/object/Ladder.java",
     '\tUDI_LADDER_WEST_FLOOR_0(new LadderObject(33550, new Location(1659, 2566, 0)), new Location(1660, 2568, 1)),\n'
     '\tUDI_LADDER_EAST_FLOOR_0(new LadderObject(33550, new Location(1666, 2566, 0)), new Location(1666, 2568, 1)),\n'
     '\n'
     '\tUDI_LADDER_WEST_FLOOR_1(new LadderObject(33552, new Location(1659, 2566, 0)), new Location(1660, 2568, 0)),\n'
     '\tUDI_LADDER_EAST_FLOOR_1(new LadderObject(33552, new Location(1666, 2566, 0)), new Location(1666, 2568, 0)),\n'
     '\n'
     '\tPRIMAL_AREA(new LadderObject(17385, new Location(1651, 11679, 0)), new Location(3099, 3500, 0)),\n', "A3-a"),
    # ---------------- A4: minnow dZone ----------------
    ("engine/src/main/java/org/jesse/game/world/entity/npc/impl/MinnowFishingSpot.java",
     '    private static final Location[] UZONE2 = new Location[] {\n'
     '        new Location(1699, 2571, 0),\n'
     '        new Location(1702, 2571, 0),\n'
     '        new Location(1699, 2569, 0),\n'
     '        new Location(1702, 2569)\n'
     '    };\n'
     '\n'
     '    private static final Location[] UZONE = new Location[] {\n'
     '        new Location(1693, 2571, 0),\n'
     '        new Location(1690, 2571, 0),\n'
     '        new Location(1693, 2569, 0),\n'
     '        new Location(1690, 2569)\n'
     '    };\n'
     '\n', "A4-a"),
    ("engine/src/main/java/org/jesse/game/world/entity/npc/impl/MinnowFishingSpot.java",
     '        boolean dZone = tile.getY() < 3000;\n'
     '        Location[] path = dZone ? UZONE : westernPath;\n'
     '        Location matching = CollectionUtils.findMatching(path, tile::matches);\n'
     '        if (matching == null) {\n'
     '            matching = CollectionUtils.findMatching(path = dZone ? UZONE2 : easternPath, tile::matches);\n'
     '        }\n', "A4-b"),
    # ---------------- A5: barrows staircase warp ----------------
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/BarrowsStaircase.java",
     '        if (option.equals("Climb-up")) {\n'
     '\n'
     '            if(object.getLocation().getX() == 2852) {\n'
     '                player.setLocation(new Location(1624, 2614, 0));\n'
     '                return;\n'
     '            }\n'
     '\n', "A5-a"),
    ("engine/src/main/java/org/jesse/game/content/minigame/barrows/BarrowsStaircase.java",
     'import org.jesse.game.world.entity.Location;\n', "A5-b"),
    # ---------------- A6: chaos altar legendary tile ----------------
    ("engine/src/main/java/org/jesse/plugins/object/ChaosAltar.java",
     '    private static final Location LEGENDARY_TILE = new Location(1709, 2604, 0);\n', "A6-a"),
    ("engine/src/main/java/org/jesse/plugins/object/ChaosAltar.java",
     '        if (!object.matches(tile) && !object.matches(LEGENDARY_TILE)) {\n', "A6-b"),
    # ---------------- A7: donator implings ----------------
    ("engine/src/main/java/org/jesse/game/content/skills/hunter/GlobalImplings.java",
     '    private static final ObjectList<DonatorImplingSpawn> donorImplings = new ObjectArrayList<>(new DonatorImplingSpawn[]{\n'
     '            new DonatorImplingSpawn(Impling.ECLECTIC, new Location(3346, 8162, 2)),\n'
     '            new DonatorImplingSpawn(Impling.NATURE, new Location(3355, 8146, 2)),\n'
     '            new DonatorImplingSpawn(Impling.NATURE, new Location(3368, 8164, 2)),\n'
     '            new DonatorImplingSpawn(Impling.MAGPIE, new Location(3366, 8279, 2)),\n'
     '            new DonatorImplingSpawn(Impling.NINJA, new Location(3390, 7984, 0)),\n'
     '            new DonatorImplingSpawn(Impling.CRYSTAL, new Location(3370, 7605, 0)),\n'
     '            new DonatorImplingSpawn(Impling.DRAGON, new Location(3414, 7593, 0)),\n'
     '            new DonatorImplingSpawn(Impling.LUCKY, new Location(3385, 7821, 0)),\n'
     '\n'
     '    });\n', "A7-a"),
    ("engine/src/main/java/org/jesse/game/content/skills/hunter/GlobalImplings.java",
     '        WorldTasksManager.scheduleCreation(() -> {\n'
     '            for (DonatorImplingSpawn impling : donorImplings) {\n'
     '                ImplingNPC implingNPC = new ImplingNPC(impling.getImpling().getNpcId(), impling.getLocation(), Direction.SOUTH)\n'
     '                        .setOnFinished(npc -> {\n'
     '                            npc.setRespawnTime(Utils.random(2000, 6000));\n'
     '                            npc.setRespawnTask();\n'
     '                        });\n'
     '                implingNPC.spawn();\n'
     '                implingNPC.setRadius(10);\n'
     '            }\n'
     '        });\n', "A7-b"),
    ("engine/src/main/java/org/jesse/game/content/skills/hunter/GlobalImplings.java",
     '        log.debug(spawnedImplingCount + " global implings spawned; " + rareImplingCount + " rare, " + invisibleImplingCount + " invisible, "+ donorImplings.size()+" donator islands");\n', "A7-c"),
    ("engine/src/main/java/org/jesse/game/content/skills/hunter/GlobalImplings.java",
     '    public static class DonatorImplingSpawn {\n'
     '        private Impling impling;\n'
     '        private Location location;\n'
     '\n'
     '        public Impling getImpling() {\n'
     '            return impling;\n'
     '        }\n'
     '\n'
     '        public Location getLocation() {\n'
     '            return location;\n'
     '        }\n'
     '\n'
     '        public DonatorImplingSpawn(Impling impling, Location location) {\n'
     '            this.impling = impling;\n'
     '            this.location = location;\n'
     '        }\n'
     '    }\n', "A7-d"),
    # ---------------- A8: NPC aggression exclusion ----------------
    ("engine/src/main/java/org/jesse/game/world/entity/npc/NPC.java",
     '            case 13430, 13431: return false;    /* udi */\n', "A8-a"),
    # ---------------- A9: Mage of Zamorak RDI branch ----------------
    ("engine/src/main/java/org/jesse/plugins/renewednpc/MageOfZamorak.java",
     '    private static final Location ABYSS_LOCATION_CENTER = new Location(3039, 4836, 0);\n', "A9-a"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/MageOfZamorak.java",
     '            boolean rdi = npc.getId() == 16051;\n'
     '\n'
     '\n', "A9-b"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/MageOfZamorak.java",
     '                player.setLocation(rdi ? ABYSS_LOCATION_CENTER : ABYSS_LOCATION);\n', "A9-c"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/MageOfZamorak.java",
     '        return new int[] { NpcId.MAGE_OF_ZAMORAK_2581, 16051};\n', "A9-d"),
    # ---------------- A11: GameConstants.RUNESPAWN ----------------
    ("engine/src/main/java/org/jesse/game/GameConstants.java",
     '\tpublic static boolean RUNESPAWN;\n', "A11-a"),
    # ---------------- A12: login guard anchors ----------------
    ("engine/src/main/kotlin/org/jesse/game/world/entity/player/AreaManagerExt.kt",
     'fun fixLocationIfInstanceDC(player: Player, location: Location): Location {\n'
     '    val onEnterLocation = player.areaManager.onEnterLocation.takeIf { it != 0 }?.let { Location(it) }\n'
     '    if (onEnterLocation != null)\n'
     '        player.areaManager.onEnterLocation = 0\n'
     '    return onEnterLocation?:location\n'
     '}\n', "A12-a"),
    ("engine/src/main/java/org/jesse/game/world/entity/player/login/LoginManager.java",
     '        Location location = AreaManagerExtKt.fixLocationIfInstanceDC(parser, parser.getLocation());\n', "A12-b"),
    # ---------------- B1: TypeParser ----------------
    (TP, 'import org.jesse.cache_tool.packing.custom.NearRealityCustomWorldMapPacker;\n', "B1-a"),
    (TP, 'import org.jire.wmpacker.CustomWorldMapAreas;\n', "B1-b"),
    (TP, '    public static boolean RUNESPAWN = false;\n\n', "B1-c"),
    (TP, '        if (args.length > 1) {\n'
         '            RUNESPAWN = Boolean.parseBoolean(args[1]);\n'
         '        }\n', "B1-d"),
    (TP, '//        try {\n'
         '//            Cache runespawn_cache = Cache.openCache("data/cache-runespawn");\n'
         '//            RuneSpawnMigration runespawn = new RuneSpawnMigration(cache, runespawn_cache);\n'
         '//            runespawn.run();\n'
         '//        } catch (Exception e) {\n'
         '//            e.printStackTrace(System.err);\n'
         '//        }\n', "B1-e"),
    (TP, '        if (RUNESPAWN) {\n'
         '            parse(new File("assets/runespawn/types"));\n'
         '        }\n', "B1-f"),
    (TP, '        NearRealityCustomWorldMapPacker.pack();\n', "B1-g"),
    (TP, '        if (RUNESPAWN) {\n'
         '            packInterfacesInner(cache, Paths.get("assets/runespawn/interfaces").toFile().listFiles());\n'
         '        }\n', "B1-h"),
    (TP, '        packMapPre209(9517, java.nio.file.Files.readAllBytes(Paths.get("assets/map/island_l_regular.dat")),\n'
         '                Regions.inject(\n'
         '                        java.nio.file.Files.readAllBytes(Paths.get("assets/map/island_m_regular.dat")), o -> {\n'
         '                            if (o.getId() == 46087) {\n'
         '                                o.setId(46089);\n'
         '                            }\n'
         '                            return false;\n'
         '                        }));\n', "B1-i"),
    (TP, '        packMapPre209(14477, java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/m56_141.dat")),\n'
         '                java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/l56_141.dat")));\n'
         '        packMapPre209(14478, java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/m56_142.dat")),\n'
         '                java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/l56_142.dat")));\n'
         '        packMapPre209(14733, java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/m57_141.dat")),\n'
         '                java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/l57_141.dat")));\n'
         '        packMapPre209(14734, java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/m57_142.dat")),\n'
         '                java.nio.file.Files.readAllBytes(Paths.get("assets/map/dmm_tourny/l57_142.dat")));\n', "B1-j"),
    (TP, '        packMapPre209(15245, java.nio.file.Files.readAllBytes(Paths.get("assets/map/tournament/2.dat")),\n'
         '                java.nio.file.Files.readAllBytes(Paths.get("assets/map/tournament/3.dat")));\n'
         '        packMapPre209(15248, java.nio.file.Files.readAllBytes(Paths.get("assets/map/tournament/0.dat")),\n'
         '                Regions.inject(java.nio.file.Files.readAllBytes(Paths.get("assets/map/tournament/1.dat")),\n'
         '                        null,\n'
         '                        new WorldObject(35005, 10, 3, new Location(3806, 9245, 0)),\n'
         '                        new WorldObject(35006, 10, 1, new Location(3813, 9256, 0)),\n'
         '                        new WorldObject(35007, 10, 0, new Location(3799, 9256, 0))));\n', "B1-k"),
    (TP, '        packMapPre209(6582, java.nio.file.Files.readAllBytes(Paths.get("assets/map/primal_zone/primal_dungeon_l.dat")),\n'
         '                java.nio.file.Files.readAllBytes(Paths.get("assets/map/primal_zone/primal_dungeon_m.dat")));\n', "B1-l"),
    (TP, '//    packMapPre209(13420, "assets/map/gamble/gamble_0.dat", "assets/map/gamble/gameble_1.dat.dat");\n'
         '//        packMapPre209(13422, "assets/map/world_boss/worldboss_landscape.dat",\n'
         '//                "assets/map/world_boss/worldboss_objects.dat");\n'
         '//        packMapPre209(13424, "assets/map/tutorial_island/tutorial_landscape.dat",\n'
         '//                "assets/map/tutorial_island/tutorial_objects.dat");\n', "B1-m"),
    (TP, '        packMapPre209(8314, "assets/map/staff_landscape.dat",\n'
         '                "assets/map/staff_objects.dat");\n', "B1-n"),
    (TP, '//        packMapPre209(11601, null,\n'
         '//                Regions.inject(11601, null, new WorldObject(50083, 10, 1, new Location(2906, 5206, 0))));\n', "B1-o"),
    (TP, '//        packMapsRSPSi(13430, "assets/map/donator_zones/LDI.pack");\n'
         '//        packMapsRSPSi(13433, "assets/map/donator_zones/UDI.pack");\n'
         '//        packMapsRSPSi(13550, "assets/map/donator_zones/rev_dungeon.pack");\n'
         '//        packMapsRSPSi(13552, "assets/map/donator_zones/rev_dungeon.pack");\n'
         '//        packMapsRSPSi(11374, "assets/map/donator_zones/barrows.pack");\n'
         '//        packMapsRSPSi(11375, "assets/map/donator_zones/barrows.pack");\n'
         '//        packMapsRSPSi(11376, "assets/map/donator_zones/barrows.pack");\n'
         '//        packMapsRSPSi(11377, "assets/map/donator_zones/barrows.pack");\n'
         '//        packMapsRSPSi(11378, "assets/map/donator_zones/barrows.pack");\n'
         '//        packMapsRSPSi(11379, "assets/map/donator_zones/barrows.pack");\n'
         '//\n'
         '//        packMapsRSPSi(13436, "assets/map/donator_zones/RDI.pack");\n'
         '//        packMapsRSPSi(13439, "assets/map/donator_zones/DI.pack");\n'
         '//        packMapsRSPSi(13441, "assets/map/donator_zones/DIE.pack");\n', "B1-p"),
    (TP, '//        packMapsRSPSi(13443, "assets/map/donator_zones/nr_dzone.pack");\n', "B1-q"),
    (TP, '//        packMapPre209(6441,\n'
         '//                "assets/map/quad_dono_island/top_left_landscape.dat",\n'
         '//                "assets/map/quad_dono_island/top_left_objects.dat");\n'
         '//        packMapPre209(6697,\n'
         '//                "assets/map/quad_dono_island/top_right_landscape.dat",\n'
         '//                "assets/map/quad_dono_island/top_right_objects.dat");\n'
         '//        packMapPre209(6440,\n'
         '//                "assets/map/quad_dono_island/bottom_left_landscape.dat",\n'
         '//                "assets/map/quad_dono_island/bottom_left_objects.dat");\n'
         '//        packMapPre209(6696,\n'
         '//                "assets/map/quad_dono_island/bottom_right_landscape.dat",\n'
         '//                "assets/map/quad_dono_island/bottom_right_objects.dat");\n'
         '\n'
         '//        packMapsRSPSi(6440, "assets/map/quad_dono_island/dZone.pack");\n'
         '\n'
         '//        packMapPre209(6469,\n'
         '//                "assets/map/quad_dono_island/chin_dungeon/chin_dung_landscape.dat",\n'
         '//                "assets/map/quad_dono_island/chin_dungeon/chin_dung_objects.dat");\n', "B1-r"),
    (TP, '        if (RUNESPAWN) {\n'
         '            packMapPre209(12342, "assets/runespawn/edgarrock_landscape.dat",\n'
         '                    "assets/runespawn/edgarrock_objects.dat");\n'
         '            packMapPre209(13382, "assets/runespawn/darkedge_landscape.dat",\n'
         '                    "assets/runespawn/darkedge_objects.dat");\n'
         '        }\n', "B1-s"),
    (TP, '//        CustomWorldMapAreas.changeGodwarsArea(service);\n'
         '        CustomWorldMapAreas.changeMainArea(service);\n', "B1-t"),
    # ---------------- B2: MapChanges primal arm ----------------
    ("cache/src/main/java/mgi/tools/parser/MapChanges.java",
     '            case 6582 -> {\n'
     '                newData = addObjects(inputData,\n'
     '                    new WorldObject(36594, 10, 0, new Location(1650, 11680, 0)),\n'
     '                    new WorldObject(60501, 10, 0, new Location(1655, 11679, 0)),\n'
     '                    new WorldObject(34856, 10, 0, new Location(1654, 11681, 0)),\n'
     '                    new WorldObject(34856, 10, 0, new Location(1654, 11678, 0)),\n'
     '                    new WorldObject(34856, 10, 0, new Location(1657, 11678, 0)),\n'
     '                    new WorldObject(34856, 10, 0, new Location(1657, 11681, 0)),\n'
     '                    new WorldObject(60502, 10, 0, new Location(1654, 11683, 0)));\n'
     '\n'
     '                newData = editObjects(newData,\n'
     '                        o -> {\n'
     '                            if(o.getId() == 33318)\n'
     '                                return true;\n'
     '                            if(o.getId() == 660)\n'
     '                                return true;\n'
     '                            if(o.getId() == 661)\n'
     '                                return true;\n'
     '                            if(o.getId() == 1457)\n'
     '                                return true;\n'
     '//                            if(o.getId() == 197 && !o.matches(new Location(1650, 11680, 0))){\n'
     '//                                o.setType(10);\n'
     '//                                o.setId(34856);\n'
     '//                            }\n'
     '                            if(o.getId() == 197) {\n'
     '                                return true;\n'
     '                            }\n'
     '                            return false;\n'
     '                        });\n'
     '            }\n', "B2-a"),
    # ---------------- B3: custom maps packer barrows entry ----------------
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomMapsPacker.kt",
     '            packMap(13909, "barrows")\n', "B3-a"),
    # ---------------- B4: BuilderExt map labels ----------------
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/BuilderExt.kt",
     'import mgi.types.worldmap.MapElementDefinitions\n', "B4-a"),
    ("cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/BuilderExt.kt",
     'fun Int.createSmallMapLabel(label: String) = MapElementDefinitions.get(444)!!.toBuilder()\n'
     '    .id(this)\n'
     '    .text(label)\n'
     '    .textSize(0)\n'
     '    .build()\n'
     '    .pack()\n'
     '\n'
     'fun Int.createMediumMapLabel(label: String) = MapElementDefinitions.get(444)!!.toBuilder()\n'
     '    .id(this)\n'
     '    .text(label)\n'
     '    .textSize(1)\n'
     '    .build()\n'
     '    .pack()\n'
     '\n'
     'fun Int.createLargeMapLabel(label: String) = MapElementDefinitions.get(143)!!.toBuilder()\n'
     '    .id(this)\n'
     '    .text(label)\n'
     '    .textSize(2)\n'
     '    .build()\n'
     '    .pack()\n', "B4-b"),
    # ---------------- B5: custom.toml shop NPC defs ----------------
    ("cache/assets/types/npc/custom.toml",
     '# Gethin\n[[npc]]\nid=16031\ninherit=1482\nop1=""\nop3="Trade"\n\n'
     '# Sigmund\n[[npc]]\nid=16032\ninherit=3894\nop1=""\nop3="Trade"\n\n'
     '# Tarik\n[[npc]]\nid=16033\ninherit=1781\nop1=""\nop3="Trade"\n\n', "B5-a"),
    ("cache/assets/types/npc/custom.toml",
     '# RDI mage of Zamorak\n[[npc]]\nid=16051\ninherit=2581\n\n', "B5-b"),
    ("cache/assets/types/npc/custom.toml",
     '#Strange old man\n[[npc]]\nid=16058\ninherit=1671\n\n'
     '# Tarik\n[[npc]]\nid=16059\ninherit=1781\nop1=""\nop3="Trade"\n\n'
     '# Squire\n[[npc]]\nid=16060\ninherit=4737\nname="Teleport Manager"\n\n', "B5-c"),
]

for path, text, tag in FINDS:
    p = pathlib.Path(path)
    if not p.exists():
        err(tag, f"missing file {path}"); continue
    n = p.read_text().count(text)
    if n != 1:
        err(tag, f"expected 1 match in {path}, found {n}")

# ---------------- big commented GWD block: anchored range check ----------------
tp_lines = pathlib.Path(TP).read_text().splitlines()
def line(i): return tp_lines[i-1]
if line(1291) != '//        packMapPre209(4674,':
    err("B1-u", f"GWD block start anchor mismatch at 1291: {line(1291)!r}")
if line(1423) != '//                }));':
    err("B1-u", f"GWD block end anchor mismatch at 1423: {line(1423)!r}")
bad = [i for i in range(1291, 1424) if not line(i).lstrip().startswith('//')]
if bad:
    err("B1-u", f"non-comment lines inside 1291-1423: {bad}")
if sum('godwars-instances' in line(i) for i in range(1291, 1424)) != 16:
    err("B1-u", "expected 16 godwars-instances asset paths in 1291-1423")
# home comment lines must be KEPT and must sit outside every removal region
for keep in ('        //packMapPre209(12342, "assets/map/osnr_home/624.dat", "assets/map/osnr_home/625.dat");',
             '        //packMapsRSPSi(13382, "assets/osnr/custom_maps/NR_home.pack");'):
    if keep not in tp_lines:
        err("KEEP-home", f"missing kept home line: {keep!r}")

# ---------------- deletion manifest ----------------
DELETIONS = {
    # server code
    "engine/src/main/java/org/jesse/plugins/renewednpc/GethinNPC.java": None,
    "engine/src/main/java/org/jesse/plugins/renewednpc/SigmundNPC.java": None,
    "engine/src/main/java/org/jesse/plugins/renewednpc/TarikNPC.java": None,
    "engine/src/main/java/org/jesse/plugins/renewednpc/StrangeOldManRDI.java": None,
    # spawn scripts
    "content/spawns/region/region13xxx/src/main/kotlin/org/jesse/plugins/spawns/region11374.kt": None,
    "content/spawns/region/region13xxx/src/main/kotlin/org/jesse/plugins/spawns/region13430.kt": None,
    "content/spawns/region/region13xxx/src/main/kotlin/org/jesse/plugins/spawns/region13433.kt": None,
    "content/spawns/region/region13xxx/src/main/kotlin/org/jesse/plugins/spawns/region13436.kt": None,
    "content/spawns/region/region13xxx/src/main/kotlin/org/jesse/plugins/spawns/region13439.kt": None,
    "content/spawns/region/region13xxx/src/main/kotlin/org/jesse/plugins/spawns/region13441.kt": None,
    "content/spawns/region/region13xxx/src/main/kotlin/org/jesse/plugins/spawns/region13550.kt": None,
    "content/spawns/region/region13xxx/src/main/kotlin/org/jesse/plugins/spawns/region13552.kt": None,
    "content/spawns/region/region14xxx/src/main/kotlin/org/jesse/plugins/spawns/region14477.kt": None,
    "content/spawns/region/region14xxx/src/main/kotlin/org/jesse/plugins/spawns/region14478.kt": None,
    "content/spawns/region/region14xxx/src/main/kotlin/org/jesse/plugins/spawns/region14732.kt": None,
    "content/spawns/region/region14xxx/src/main/kotlin/org/jesse/plugins/spawns/region14733.kt": None,
    "content/spawns/region/region14xxx/src/main/kotlin/org/jesse/plugins/spawns/region14734.kt": None,
    "content/spawns/region/region15xxx/src/main/kotlin/org/jesse/plugins/spawns/region15246.kt": None,
    "content/spawns/region/region15xxx/src/main/kotlin/org/jesse/plugins/spawns/region15248.kt": None,
    # cache pipeline classes
    "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomWorldMapPacker.kt": None,
    "cache/src/main/kotlin/org/jire/wmpacker/WorldMapAreas.kt": None,
    "cache/src/main/kotlin/org/jire/wmpacker/CustomWorldMapAreas.kt": None,
    "cache/src/main/java/mgi/tools/parser/RuneSpawnMigration.java": None,
    "engine/src/main/java/org/jesse/tools/WorldMapElementExtractor.java": None,
    # assets (dirs carry expected file counts)
    "cache/assets/map/dmm_tourny": 12,
    "cache/assets/map/tournament": 4,
    "cache/assets/map/primal_zone": 2,
    "cache/assets/runespawn": 5,
    "cache/assets/map/island_l_regular.dat": None,
    "cache/assets/map/island_m_regular.dat": None,
    "cache/assets/map/island_l_christmas.dat": None,
    "cache/assets/map/island_m_christmas.dat": None,
    "cache/assets/map/staff_landscape.dat": None,
    "cache/assets/map/staff_objects.dat": None,
    "cache/assets/osnr/custom_maps/barrows_landscape.dat": None,
    "cache/assets/osnr/custom_maps/barrows_objects.dat": None,
}
for path, count in DELETIONS.items():
    p = pathlib.Path(path)
    if not p.exists():
        err("DEL", f"deletion target missing: {path}"); continue
    if count is not None:
        actual = sum(1 for f in p.rglob("*") if f.is_file())
        if actual != count:
            err("DEL", f"{path}: expected {count} files, found {actual}")

# ---------------- keep-guards ----------------
KEEP_FILES = [
    # tournament minigame keep-set
    "cache/assets/map/osnr_tournament/final_landscape.dat",
    "cache/assets/map/osnr_tournament/final_objects.dat",
    "cache/assets/map/osnr_tournament/tourney_landscape.dat",
    "cache/assets/map/osnr_tournament/tourney_objects.dat",
    "cache/assets/types/tournament.toml",
    "cache/assets/types/object/tournament_portal.toml",
    "content/minigames/tournament/build.gradle.kts",
    # home material (Stage 4)
    "cache/assets/map/home23_l.dat", "cache/assets/map/home23_m.dat",
    "cache/assets/map/home24_l.dat", "cache/assets/map/home24_m.dat",
    "cache/assets/map/home25_l.dat", "cache/assets/map/home25_m.dat",
    "cache/assets/map/home26_l.dat", "cache/assets/map/home26_m.dat",
    "cache/assets/map/home27_l.dat", "cache/assets/map/home27_m.dat",
    "cache/assets/map/christmas_home29_l.dat", "cache/assets/map/christmas_home29_m.dat",
    "cache/assets/osnr/custom_maps/NR_home.pack",
    "cache/assets/osnr/custom_maps/home_landscape.dat",
    "cache/assets/osnr/custom_maps/home_objects.dat",
    # surviving custom maps
    "cache/assets/osnr/custom_maps/mm_landscape.dat",
    "cache/assets/osnr/custom_maps/mm_objects.dat",
    "cache/assets/osnr/custom_maps/pvm_arena_landscape.dat",
    "cache/assets/osnr/custom_maps/pvm_arena_objects.dat",
    # NOT-dead area class (correction: auto-registered by classgraph AREA scan)
    "engine/src/main/java/org/jesse/game/world/region/area/RDIArea.java",
    # dying-knight def (GWD content, spawned in kept regions)
    "cache/assets/types/npc/dying_knight.toml",
]
for path in KEEP_FILES:
    if not pathlib.Path(path).exists():
        err("KEEP", f"keep-set file missing (verify before executing!): {path}")
for path in KEEP_FILES:
    if path in DELETIONS:
        err("KEEP", f"keep-set file is in the deletion manifest: {path}")

# keep-set pack calls must be present in TypeParser and OUTSIDE every FIND block
tp_text = pathlib.Path(TP).read_text()
for keep_call, tag in [
    ('packMapPre209(13426, "assets/map/osnr_tournament/final_landscape.dat"', "KEEP-13426"),
    ('packMapPre209(13428, java.nio.file.Files.readAllBytes(Paths.get("assets/map/osnr_tournament/tourney_landscape.dat"))', "KEEP-13428"),
    ('packMapsRSPSi(14388, "assets/map/Meiyerditch.pack");', "KEEP-14388"),
    ('packMapsRSPSi(6457, "assets/map/kourend_castle.pack");', "KEEP-6457"),
    ('packMapsRSPSi(10803, "assets/map/witchaven.pack");', "KEEP-10803"),
    ('packMapsRSPSi(12854, "assets/map/varrock_topr.pack");', "KEEP-12854"),
    ('NearRealityEffigyMapEdits.apply();', "KEEP-effigy"),
    ('copyMapRegion(11605, 11604);', "KEEP-rdicopy"),
]:
    if keep_call not in tp_text:
        err(tag, f"kept pack call not found: {keep_call}")
    for _, text, ftag in FINDS:
        if keep_call in text:
            err(tag, f"kept pack call sits inside removal block {ftag}!")

# tournament keep-set tokens must not appear in any FIND block
for token in ("osnr_tournament", "tournament.toml", "tournament_portal", "13426", "13428"):
    for path, text, ftag in FINDS:
        if token in text and ftag not in ("A8-a",):  # A8-a mentions 13430/13431 only
            err("KEEP-tourney", f"token {token!r} found inside FIND {ftag}")

# barrows-brother defs 16052-16057 stay (dropViewer + wight validators reference them)
ct = pathlib.Path("cache/assets/types/npc/custom.toml").read_text()
for i in range(16052, 16058):
    if f"id={i}\n" not in ct:
        err("KEEP-wights", f"custom.toml def id={i} missing")
    for path, text, ftag in FINDS:
        if f"id={i}\n" in text:
            err("KEEP-wights", f"def id={i} inside FIND {ftag} — wight defs must stay this stage")

print("CLEAN" if fail == 0 else f"{fail} FAILURES")
sys.exit(1 if fail else 0)
```

## Part E — execution order & gates

1. Dry-run CLEAN. 2. Part A → `./gradlew compileJava compileKotlin`. 3. Part B → compile. 4. Part C → compile. 5. Post-greps (zero unless stated): staffzone/sz/2080,7844/2078,7840 · DONATOR_ISLANDS/UDI_LADDER/PRIMAL_AREA/LEGENDARY_TILE/UZONE/DonatorImplingSpawn/donorImplings · RUNESPAWN/RuneSpawnMigration · runespawn (only com.runespawn.util library imports allowed) · wmpacker/CustomWorldMapAreas/WorldMapAreas/NearRealityCustomWorldMapPacker/WORLDMAP_ORANGE/create*MapLabel/WorldMapElementExtractor · dmm_tourny/primal_zone/island_[lm]_/staff_landscape/staff_objects/barrows_landscape/barrows_objects/donator_zones/quad_dono_island · 13909|6582 in cache/src · 16031/16032/16033/16051/16058/16059/16060 (only unrelated-namespace constants allowed) · ABYSS_LOCATION_CENTER/StrangeOldManRDI · "case 13430". Keep-asserts: packMapPre209(13426 ×1, osnr_tournament present, RDIArea.java present.
6. **Local: cache regen from pristine** — expect Packed map[13426]/[13428], NO packed lines for 9517/14477/14478/14733/14734/15245/15248/6582/8314/13909, no file-not-found.
7. **Local: `./gradlew clean` + `:app:runPluginScanner`** — 15 spawn scripts + 4 NPC plugins are in stale plugins.dat.
8. **Local boot:** no ClassNotFoundException, no GlobalAreaManager registration errors.
9. **Local in-game:** ::staffzone/::sz/::si/::ndi unknown; ::tourny works; world map has no 9517 island; tournament joins + instances build (13426-13433 window partially blank = intended); barrows staircases vanilla; chaos altar vanilla; minnow platform + Kylie 7728 work; Mage of Zamorak 2581 vanilla abyss; impling log has no "donator islands"; drop viewer still shows brothers 16052-16057; save-guard test: doctored saves at (2080,7844)/(3370,7605)/(3403,8000)/(1651,11679)/(3806,9245) → home 3087,3490; tournament-lobby save untouched.
10. Commit (plan + dry-run deleted in same commit), push.

## Part F — save impact
- Players logged out in removed zones (incl. the guard-less pre-deleted 069c688f zones): relocated to home by A-12. Staff at 8314 included.
- No item defs removed this stage — no bank/inventory render impact.
- Per-name killcounts inert. Instance-DC relocation still wins (guard composes after it).

## Part G — deliberately left
Home maps + comments (Stage 4) · middleman 12145 (5d) · pvm-arena 6729 (live) · RDI/elemental copy + RDIArea rename (keep-set pass) · "RuneSpawn Credit Packages" packed string (Stage 6) · custom barrows-brothers slice: defs 16052-16057 + NPCSpawnLoader:164-169 + six wight validate() arms (5c decision) · PrimulaNPC + 16034 (5c) · RDI-bonfire object 29300 toml edit (own check) · packMaps unused param (Stage 6).

## Part H — roadmap bookkeeping
Stage 3 exit gate met → Stage 4 (home; gate = location decision; A-12 guard exists — just append home regions). Tournament checkpoint RESOLVED KEEP; staff zone removed by user decision; donator maps pre-deleted in 069c688f (this stage removed surviving code); corrections: RDIArea live-keep (classgraph), MapChanges 6582 second hook, dmm count 12. Forward: barrows-brothers slice + Primula + bonfire → 5c; branding blob + hiscores rows + packMaps tidy → Stage 6; middleman → 5d. Stage 2 addendum: dangling map-label 2500 closed here.
