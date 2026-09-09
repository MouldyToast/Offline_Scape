# PLAN: Stage 4 — custom home area

**Roadmap:** ROADMAP_custom_cache_removal, Stage 4.
**Verified against:** `claude/new-session-lo0m56` @ `87003018` (Stages 1–3 executed). 63 tagged FIND blocks verified byte-exact and unique by the embedded dry-run (run: CLEAN).
**Removal order (rule 1):** server code (A) → TypeParser (B1) → packers/model map (B2–B4) → assets (C).

## 0. Scope & corrections

### 0.1 Gate: ALREADY SATISFIED
Home is vanilla Edgeville (3087,3490). No live pack covers region 12342 (home pack lines commented; `modifyRegions(12342)` reachable only from packMapsRSPSi whose live packs are Meiyerditch/kourend/witchaven/varrock_topr; `packMapsRSPSiModern` has zero callers). This stage removes machinery and leftovers, not a live map. New-player flow (register island → ZenyteGuide.SPAWN_LOCATION (3090,3497)), RespawnPoint.EDGEVILLE, and all (3087,348x-349x) teleports are vanilla-safe keeps.

### 0.2 User decisions
- **Tournament portal: REMOVE** — tournament_portal.toml (obj 60446), model 60517, TournamentPortalObject.kt, ObjectId.TOURNAMENT_PORTAL_60446, broadcast reworded. Entry survives via Guard 16012 + ::tourny (re-pointed to the guard's tile).
- **AFK zone: REMOVE** — ::afk, Boundary.AFK_ZONE, World online-count exclusion; ::slayer (dead hub) in the same sweep.

### 0.3 Verification-driven corrections
1. **Smelting bonus is the vanilla Varrock-armour perk** (items 13104-13107) — only the tile was custom. A-8 re-anchors it to `object.getRegionId() == 12342` (the only furnace there is Edgeville's) instead of deleting vanilla behavior.
2. `getOnlineActivePlayerCount()` has zero callers — simplified, not deleted (Stage 6 candidate).
3. `LOCATION_IN_FRONT_OF_PORTAL` (3105,3487) is live exit plumbing (lobby leave/login, fight exits) on a proven vanilla tile — hoisted to `TOURNAMENT_EXIT_LOCATION` in TournamentPlayerExt.kt; `moveToTournamentPortal` → `moveToTournamentExit` (lobby reaches it via its existing wildcard import).
4. Guard NPC (3097,3505) is adjacent to, not atop, the Emblem Trader (3096,3505) — LEFT in place; `::tourny` re-pointed to (3097,3504), the tile the guard faces.
5. ChristmasCupboard home branch dead (cupboard existed only on the unreferenced christmas_home29 map); land-of-snow RETURN branch is live — collapsed to the unconditional return; event stays seasonal-usable.
6. `HOME_ZENYTE_GUIDE` had one live consumer (EvilBobIsland train-island return) — inlined as `new Location(3089, 3496, 0)`; the always-true guard in ZenyteGuide simplified.
7. `MakeoverMage.HOME_MAKE_OVER_MAGE` referenced only inside a fully-commented block — both die.
8. POH gilded altars untouched (chapel Altar.java); AltarOPlugin's 27501 special-case + orphaned OfferingAction + 4 imports die; bones on named vanilla altars now correctly no-op.
9. Pottery safe: vanilla PotteryFiringObjectAction exists; CookingObjectAction's (3105,3497) branch + pottery-material bindings + crafting imports die.
10. NR_home.pack parsed: regions 13382/13383/13638/13639 → appended to `removedCustomRegions`.
11. Model-map pairing rule enforced: every deleted .dat loses its map entry in the same commit (incl. the rebirth_afk_rightGate/rightgate 60520 case-duplicate). ge_orb_pillarb (10517), dzone_stallb (37907), zenyte_portal (38000) are vanilla-model overrides — deletion restores vanilla on regen.
12. WorldBroadcasts: dead home-guide/tablet tips deleted; Turael tip reworded to Burthorpe; Zahur tip reworded to Nardah (vanilla spawn verified, plugin live).
13. XTEALoaderPorted: only the stale home javadoc + commented 12342 case cleaned; `getXTEAs` stays.
14. "Wilderness Statistics" plugin was name-bound to the gone custom hiscore board — file deletion safe.

## Part A — server code (tags A1-a…A18-a; exact FINDs in the dry-run)
A-1 ::slayer+::afk deleted; ::tourny → (3097,3504) "Tournament Guard." · A-2 Boundary.AFK_ZONE · A-3 World.java Boundary import + count simplification · A-4 git rm: WildernessStatisticsObject.java, content/generic HomeBoxOfRestorationObject.java (TickVariable.BOX_OF_RESTORATION LEFT — ordinal risk, Part G), HomeLecternObject.java, tournament loc/TournamentPortalObject.kt · A-5 SpiralStaircase homeException + warp · A-6 AltarOPlugin constants/handler/OfferingAction/4 imports · A-7 CookingObjectAction pottery branch + material bindings + 2 imports · A-8 Smelting EDGEVILLE_FURNACE + Location import; region-gate replacement · A-9 ChristmasCupboard collapse + Utils import; ChristmasConstants home location + Location import · A-10 InstantMovementObjects HOME_MANHOLE dup (vanilla Trapdoor route survives) · A-11 ZenyteGuide constant + guard; EvilBobIsland inline + import · A-12 MakeoverMage constant + import + commented block · A-13 TeleportManager.WIZARD_LOCATION · A-14 WorldBroadcasts 2 deletions + 2 rewords · A-15 tournament portal unplumbing (controller broadcast; PlayerExt hoist TOURNAMENT_EXIT_LOCATION/moveToTournamentExit; Tournament.kt/TournamentArea.kt/TournamentLobbyArea.kt call-site renames) · A-16 ObjectId.TOURNAMENT_PORTAL_60446 (vanilla TOURNAMENT_PORTAL=12355 untouched) · A-17 login guard += 13382/13383/13638/13639 · A-18 BankPolygons Edgeville polygon RESTORED (vanilla fix: firemaking blocked in bank again).

## Part B — cache side
B-1 TypeParser: editObjects()/addCustomStalls() calls + stubs; packMapsRSPSiModern (both overloads, zero callers); dead modifyRegions 12342 branch (8036 branch KEEP); commented home pack lines :1250-1251 · B-2 RebirthPacker: packHome2025 call + function (misc_models call stays — dir survives trimmed; red-mist 50145 keep-guarded) · B-3 XTEALoaderPorted javadoc/commented-case cleanup · B-4 model map: 17 entries die paired with Part C files (header rewrite keeps 2450 + 29615; blocks for 60404/60428/60463/60483/60514-60522/60532); KEEP tourny supplies 60456 + rare_drop_table 60476.

## Part C — assets (34 files)
git rm -r rebirth/home_2025/ (13) · git rm 4 of 6 misc_models (healing_fountain, magicstallb, dzone_stallb, zenyte_portal — KEEP tourny supplies + rare_drop_table) · 12 map/home2x + christmas_home29 · 3 custom_maps home files (NR_home.pack, home_landscape/objects) · 2 tomls (tournament_portal, universal_chest). NOT deleted: osnr_tournament/, mm_/pvm_arena_, rebirth customs_*/master_comp_cape/legacy_models, tournament.toml.

## Part D — dry-run gate (validated CLEAN @ 87003018)
Save as `dryrun_stage4.py`, run before any edit; delete in the execution commit. 63 FINDs byte-exact-unique; 34-file deletion manifest (home_2025 count == 13); keep-guards: tournament module minus portal file, guard spawn tile, supplies+rare_drop models/entries/toml refs, casket/rax overrides, six kept rebirth dirs, region12342.kt Emblem Trader, ::tourny, TypeParser 8036/13426/Meiyerditch, ZenyteGuide.SPAWN_LOCATION, vanilla TOURNAMENT_PORTAL=12355, chapel ALTAR_13199, UniversalShopInterface 12342 gate — each present and in no FIND.

```python
#!/usr/bin/env python3
"""Stage 4 dry-run gate. Run from the repo root: python3 dryrun_stage4.py
Asserts every FIND block matches its file exactly once, every deletion target
exists with expected counts, and the tournament/vanilla keep-guards hold.
Exit 0 = CLEAN."""
import pathlib, sys

fail = 0
def err(tag, msg):
    global fail
    print(f"[{tag}] {msg}"); fail += 1

TP  = "cache/src/main/java/mgi/tools/parser/TypeParser.java"
MM  = "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityCustomModelMap.kt"
RP  = "cache/src/main/kotlin/org/jesse/cache_tool/packing/custom/NearRealityRebirthPacker.kt"
XT  = "cache/src/main/java/org/jesse/game/world/region/XTEALoaderPorted.java"
PC  = "engine/src/main/kotlin/org/jesse/game/content/commands/PlayerCommands.kt"
WJ  = "engine/src/main/java/org/jesse/game/world/World.java"
BK  = "engine/src/main/kotlin/org/jesse/game/world/Boundary.kt"
TD  = "content/minigames/tournament/src/main/kotlin/org/jesse/game/content/tournament"

FINDS = [
    # ---------------- A-1: ::slayer + ::afk commands ----------------
    (PC,
     '        Command(PlayerPrivilege.PLAYER, "slayer", "Teleport to slayer masters.") { p, _ ->\n'
     '            if (p.isLocked)\n'
     '                return@Command\n'
     '            val teleport = RegularTeleport(Location(3077, 3490, 0))\n'
     '            p.sendMessage("Your words manage to teleport you to the Slayer Masters.")\n'
     '            teleport.teleport(p)\n'
     '        }\n'
     '\n'
     '        Command(PlayerPrivilege.PLAYER, "afk", "Teleport to the AFK Area.") { p, _ ->\n'
     '            if (p.isLocked)\n'
     '                return@Command\n'
     '            val teleport = RegularTeleport(Location(3124, 3482,0))\n'
     '            p.sendMessage("Your words manage to teleport you to the AFK Area.")\n'
     '            teleport.teleport(p)\n'
     '        }\n'
     '\n', "A1-a"),
    # ---------------- A-1b: repoint ::tourny at the Tournament Guard ----------------
    (PC,
     '            val teleport = RegularTeleport(Location(3104, 3486, 0))\n'
     '            p.sendMessage("You teleport to the Tournament Area.")\n', "A1-b"),
    # ---------------- A-2: Boundary.AFK_ZONE ----------------
    (BK,
     '        @JvmField\n'
     '        val AFK_ZONE = Boundary(3096, 3469, 3113, 3483)\n'
     '\n', "A2-a"),
    # ---------------- A-3: World.getOnlineActivePlayerCount ----------------
    (WJ, 'import org.jesse.game.world.Boundary;\n', "A3-a"),
    (WJ,
     '    public static int getOnlineActivePlayerCount() {\n'
     '        return (int) getPlayers().stream()\n'
     '                // remove nulls\n'
     '                .filter(Objects::nonNull)\n'
     '                // Make sure their not in the AFK Zone\n'
     '                .filter(player -> !Boundary.AFK_ZONE.isIn(player))\n'
     '                // Ensure they are not idle\n'
     '//            .filter(player -> !player.isIdle)\n'
     '                // count\n'
     '                .count();\n'
     '    }\n', "A3-b"),
    # ---------------- A-5: SpiralStaircaseObject homeException ----------------
    ("engine/src/main/java/org/jesse/plugins/object/SpiralStaircaseObject.java",
     '    private static final Location homeException = new Location(3118, 3484, 0);\n'
     '\n', "A5-a"),
    ("engine/src/main/java/org/jesse/plugins/object/SpiralStaircaseObject.java",
     '        } else if (option.equals("Climb-up")) {\n'
     '            if (object.matches(homeException)) {\n'
     '                player.setLocation(new Location(3119, 3482, 1));\n'
     '                return;\n'
     '            }\n'
     '            final int[] offsets = getUpOffsets(object.getRotation());\n', "A5-b"),
    # ---------------- A-6: AltarOPlugin home gilded altar ----------------
    ("engine/src/main/java/org/jesse/plugins/object/AltarOPlugin.java",
     '    public static final Animation PRAY_ANIM = new Animation(645);\n'
     '\n'
     '    private static final int HOME_ALTAR_OBJ = 27501;\n'
     '\n'
     '    private static final float HOME_ALTAR_PRAYER_XP_MOD = 3.5f;\n', "A6-a"),
    ("engine/src/main/java/org/jesse/plugins/object/AltarOPlugin.java",
     '    @Override\n'
     '    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {\n'
     '        /* only home altar can be used as gilded altar */\n'
     '        if (object.getId() != HOME_ALTAR_OBJ) {\n'
     '            player.sendMessage("Nothing interesting happens.");\n'
     '            return;\n'
     '        }\n'
     '        Bones bone = Bones.getBone(item.getId());\n'
     '        if (bone == null) {\n'
     '            player.sendMessage("You can only offer bones to the gods.");\n'
     '            return;\n'
     '        }\n'
     '        player.getActionManager().setAction(new OfferingAction(bone, item, object, HOME_ALTAR_PRAYER_XP_MOD));\n'
     '    }\n', "A6-b"),
    ("engine/src/main/java/org/jesse/plugins/object/AltarOPlugin.java",
     'import org.jesse.game.world.World;\n', "A6-c"),
    ("engine/src/main/java/org/jesse/plugins/object/AltarOPlugin.java",
     'import org.jesse.game.world.entity.masks.Graphics;\n', "A6-d"),
    ("engine/src/main/java/org/jesse/plugins/object/AltarOPlugin.java",
     'import org.jesse.game.world.entity.player.Action;\n', "A6-e"),
    ("engine/src/main/java/org/jesse/plugins/object/AltarOPlugin.java",
     'import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;\n', "A6-f"),
    # ---------------- A-7: CookingObjectAction pottery tile ----------------
    ("engine/src/main/java/org/jesse/plugins/itemonobject/CookingObjectAction.java",
     '\t\tif (object.getLocation().equals(3105, 3497, 0)) {\n'
     '            final CraftingDefinitions.PotteryFiringData data = CraftingDefinitions.PotteryFiringData.getData(item);\n'
     '            if (data != null) {\n'
     '                player.getActionManager().setAction(new PotteryFiringCrafting(data, 28));\n'
     '            }\n'
     '        }\n'
     '\n', "A7-a"),
    ("engine/src/main/java/org/jesse/plugins/itemonobject/CookingObjectAction.java",
     '        for (final CraftingDefinitions.PotteryFiringData data : CraftingDefinitions.PotteryFiringData.VALUES_ARR) {\n'
     '            list.add(data.getMaterial().getId());\n'
     '        }\n', "A7-b"),
    ("engine/src/main/java/org/jesse/plugins/itemonobject/CookingObjectAction.java",
     'import org.jesse.game.content.skills.crafting.CraftingDefinitions;\n'
     'import org.jesse.game.content.skills.crafting.actions.PotteryFiringCrafting;\n', "A7-c"),
    # ---------------- A-8: Smelting Varrock-armour tile -> region gate ----------------
    ("engine/src/main/java/org/jesse/game/content/skills/smithing/Smelting.java",
     '\tprivate static final Location EDGEVILLE_FURNACE = new Location(3101, 3493, 0);\n', "A8-a"),
    ("engine/src/main/java/org/jesse/game/content/skills/smithing/Smelting.java",
     '\t\tif (object.getPositionHash() == EDGEVILLE_FURNACE.getPositionHash() && body >= 13104 && body <= 13107) {\n', "A8-b"),
    ("engine/src/main/java/org/jesse/game/content/skills/smithing/Smelting.java",
     'import org.jesse.game.world.entity.Location;\n', "A8-c"),
    # ---------------- A-9: ChristmasCupboard home branch ----------------
    ("engine/src/main/java/org/jesse/game/content/event/christmas2019/ChristmasCupboard.java",
     '        int x, y;\n'
     '    \n'
     '        if (object.getPositionHash() == ChristmasConstants.homeChristmasCupboardLocation.getPositionHash()) {\n'
     '            if (player.getFollower() != null) {\n'
     '                player.sendMessage("The Queen of Snow has forbidden all pets in her domain. You\'ll have to pick up your follower if you want to travel to the Land of Snow.");\n'
     '                return;\n'
     '            }\n'
     '            // home cupboard\n'
     '            x = 2070 + Utils.random(0, 1);\n'
     '            y = 5401 + Utils.random(0, 2);\n'
     '        } else {\n'
     '            // land of snow cupboard\n'
     '            x = 3094;\n'
     '            y = 3487;\n'
     '        }\n'
     '    \n'
     '        new FadeScreen(player, () -> {\n'
     '            player.setLocation(new Location(x, y));\n', "A9-a"),
    ("engine/src/main/java/org/jesse/game/content/event/christmas2019/ChristmasCupboard.java",
     'import org.jesse.game.util.Utils;\n', "A9-b"),
    ("cache/src/main/java/org/jesse/game/content/event/christmas2019/ChristmasConstants.java",
     '    public static final Location homeChristmasCupboardLocation = new Location(3094, 3485);\n', "A9-c"),
    ("cache/src/main/java/org/jesse/game/content/event/christmas2019/ChristmasConstants.java",
     'import org.jesse.game.world.entity.Location;\n', "A9-d"),
    # ---------------- A-10: InstantMovementObjects home manhole dup ----------------
    ("engine/src/main/java/org/jesse/game/world/object/InstantMovementObjects.java",
     '\tHOME_MANHOLE_ENTRY_TOP(100, new Location(3095, 3480), new Location(3096, 9867), LadderOA.CLIMB_DOWN),\n'
     '\n', "A10-a"),
    # ---------------- A-11: ZenyteGuide HOME_ZENYTE_GUIDE ----------------
    ("engine/src/main/java/org/jesse/plugins/renewednpc/ZenyteGuide.java",
     '    public static final Location HOME_ZENYTE_GUIDE = new Location(3089, 3496);\n', "A11-a"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/ZenyteGuide.java",
     '                handle(player, npc);\n'
     '                if (npc.getLocation().getPositionHash() != HOME_ZENYTE_GUIDE.getPositionHash()) {\n'
     '                    npc.setInteractingWith(player);\n'
     '                }\n', "A11-b"),
    ("engine/src/main/java/org/jesse/game/world/region/area/EvilBobIsland.java",
     '                        return ZenyteGuide.HOME_ZENYTE_GUIDE;\n', "A11-c"),
    ("engine/src/main/java/org/jesse/game/world/region/area/EvilBobIsland.java",
     'import org.jesse.plugins.renewednpc.ZenyteGuide;\n', "A11-d"),
    # ---------------- A-12: MakeoverMage dead guard ----------------
    ("engine/src/main/java/org/jesse/plugins/renewednpc/MakeoverMage.java",
     '    private static final Location HOME_MAKE_OVER_MAGE = new Location(3095, 3505, 0);\n'
     '\n', "A12-a"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/MakeoverMage.java",
     'import org.jesse.game.world.entity.Location;\n', "A12-b"),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/MakeoverMage.java",
     '        /*bind("Skin Colour", new OptionHandler() {\n'
     '            @Override\n'
     '            public void handle(Player player, NPC npc) {\n'
     '                player.stopAll();\n'
     '                player.faceEntity(npc);\n'
     '                GameInterface.MAKEOVER.open(player);\n'
     '            }\n'
     '\n'
     '            @Override\n'
     '            public void execute(final Player player, final NPC npc) {\n'
     '                player.stopAll();\n'
     '                player.setFaceEntity(npc);\n'
     '                handle(player, npc);\n'
     '                if (npc.getLocation().getPositionHash() != HOME_MAKE_OVER_MAGE.getPositionHash()) {\n'
     '                    npc.setInteractingWith(player);\n'
     '                }\n'
     '            }\n'
     '        });*/\n', "A12-c"),
    # ---------------- A-13: TeleportManager.WIZARD_LOCATION ----------------
    ("engine/src/main/java/org/jesse/game/world/entity/player/teleportsystem/TeleportManager.java",
     '    private static final Location WIZARD_LOCATION = new Location(3088, 3505, 0);\n', "A13-a"),
    # ---------------- A-14: WorldBroadcasts stale home tips ----------------
    ("engine/src/main/java/org/jesse/game/world/broadcasts/WorldBroadcasts.java",
     '            "Did you know: You can rewatch the " + GameConstants.SERVER_NAME + " tutorial by talking to the " + GameConstants.SERVER_NAME + " guide near the Grand Exchange at home.",\n', "A14-a"),
    ("engine/src/main/java/org/jesse/game/world/broadcasts/WorldBroadcasts.java",
     '            "Did you know: The Magic shop sells " + GameConstants.SERVER_NAME + " home teleport tablets that allow you to instantly teleport to the home area.",\n', "A14-b"),
    ("engine/src/main/java/org/jesse/game/world/broadcasts/WorldBroadcasts.java",
     '            "Did you know: You can start Slayer South-West of the Grand Exchange with Turael. Higher level slayer masters can also be found here.",\n', "A14-c"),
    ("engine/src/main/java/org/jesse/game/world/broadcasts/WorldBroadcasts.java",
     '            "Did you know: Zahur at Home can add herbs to vials of water and crush secondary ingredients for you.",\n', "A14-d"),
    # ---------------- A-15: tournament portal removal (module keeps) ----------------
    (f"{TD}/TournamentController.kt",
     '                    append(" tournament starts in ${it.formattedString}, enter via red portal east of home!")\n', "A15-a"),
    (f"{TD}/TournamentPlayerExt.kt",
     'import org.jesse.game.content.tournament.area.TournamentArea\n'
     'import org.jesse.game.content.tournament.loc.TournamentPortalObject\n', "A15-b"),
    (f"{TD}/TournamentPlayerExt.kt",
     'internal fun Player.moveToTournamentPortal() {\n'
     '    setLocation(TournamentPortalObject.LOCATION_IN_FRONT_OF_PORTAL.random(2))\n'
     '    restoreStatePostFight()\n'
     '}\n', "A15-c"),
    (f"{TD}/Tournament.kt",
     '            player.moveToTournamentPortal()\n', "A15-d"),
    (f"{TD}/area/TournamentArea.kt",
     'import org.jesse.game.content.tournament.moveToTournamentPortal\n', "A15-e"),
    (f"{TD}/area/TournamentArea.kt",
     '            players.toSet().forEach { it.moveToTournamentPortal() }\n', "A15-f"),
    (f"{TD}/area/TournamentLobbyArea.kt",
     'import org.jesse.game.content.tournament.loc.TournamentPortalObject\n', "A15-g"),
    (f"{TD}/area/TournamentLobbyArea.kt",
     '                player.forceLocation(TournamentPortalObject.LOCATION_IN_FRONT_OF_PORTAL.copy())\n', "A15-h"),
    (f"{TD}/area/TournamentLobbyArea.kt",
     '        RandomLocation.random(TournamentPortalObject.LOCATION_IN_FRONT_OF_PORTAL, 1)\n', "A15-i"),
    # ---------------- A-16: ObjectId custom portal constant ----------------
    ("core-model/src/main/kotlin/org/jesse/game/obj/ids/ObjectId.kt",
     'const val TOURNAMENT_PORTAL_60446 = 60446\n', "A16-a"),
    # ---------------- A-17: login-guard extension ----------------
    ("engine/src/main/kotlin/org/jesse/game/world/entity/player/AreaManagerExt.kt",
     '    // custom donator barrows crypts (never packed) + orphan barrows 13909\n'
     '    11374, 11375, 11376, 11377, 11378, 11379, 13909,\n', "A17-a"),
    # ---------------- A-18: RESTORE the Edgeville bank polygon (vanilla restore) ----------------
    ("engine/src/main/java/org/jesse/game/world/entity/player/container/impl/bank/BankPolygons.java",
     '    /* new int[][] { { 3091, 3500 }, { 3091, 3498 }, { 3090, 3497 }, { 3090, 3494 }, { 3091, 3493 }, { 3091,\n'
     '                    3488 }, { 3099, 3488 },\n'
     '                    { 3099, 3500 } },*/\n'
     '    /** Edgeville bank. */\n'
     '    /** Shilo Village bank. */\n', "A18-a"),
    # ---------------- B-1: TypeParser ----------------
    (TP,
     '\n'
     '        editObjects();\n'
     '        addCustomStalls();\n', "B1-a"),
    (TP,
     '    public static void addCustomStalls() {\n'
     '    }\n'
     '\n'
     '    public static void editObjects() {\n'
     '    }\n'
     '\n', "B1-b"),
    (TP,
     '    public static void packMapsRSPSiModern(int baseRegionID, String packFilePath) throws IOException {\n'
     '        packMapsRSPSiModern(CacheManager.getCache(), baseRegionID, packFilePath);\n'
     '    }\n'
     '\n'
     '    public static void packMapsRSPSiModern(Cache cache, int baseRegionID, String packFilePath)\n'
     '            throws IOException {\n'
     '        byte[] packBytes = java.nio.file.Files.readAllBytes(Path.of(packFilePath));\n'
     '        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(packBytes);\n'
     '\n'
     '        int baseRegionX = (baseRegionID >> 8) & 0xFF;\n'
     '        int baseRegionY = baseRegionID & 0xFF;\n'
     '\n'
     '        int mapSquareCount = buffer.getInt();\n'
     '\n'
     '        for (int i = 0; i < mapSquareCount; i++) {\n'
     '            buffer.getInt(); // locGroupID\n'
     '            buffer.getInt(); // mapGroupID\n'
     '\n'
     '            int localMapSqGridX = buffer.getInt();\n'
     '            int localMapSqGridZ = buffer.getInt();\n'
     '\n'
     '            int locsBlockLength = buffer.getInt();\n'
     '            byte[] locsBlock = new byte[locsBlockLength];\n'
     '            buffer.get(locsBlock);\n'
     '\n'
     '            int mapBlockLength = buffer.getInt();\n'
     '            byte[] mapBlock = new byte[mapBlockLength];\n'
     '            buffer.get(mapBlock);\n'
     '\n'
     '            int regionX = baseRegionX + localMapSqGridX;\n'
     '            int regionY = baseRegionY + localMapSqGridZ;\n'
     '\n'
     '            int regionID = (regionX << 8) | regionY;\n'
     '            locsBlock = modifyRegions(regionID, locsBlock);\n'
     '            packMap(cache, regionID, mapBlock, locsBlock);\n'
     '        }\n'
     '    }\n'
     '\n', "B1-c"),
    (TP,
     '        if (regionID == 12342) {\n'
     '            return Regions.inject(locsBlock,\n'
     '                    o -> o.getId() == 76 || o.getId() == 29165 || o.getId() == 40448 || o.getId() == 2133 || o.getId() == 7439 || o.getId() == 6267 || o.getId() == 41705,\n'
     '                    //FIX GE booths to have bank near banks\n'
     '                    new WorldObject(10060, 0, 1, new Location(3094, 3490, 1)),\n'
     '                    new WorldObject(10060, 0, 1, new Location(3095, 3490, 1)),\n'
     '                    new WorldObject(10060, 0, 3, new Location(3094, 3495, 1)),\n'
     '                    new WorldObject(10060, 0, 3, new Location(3095, 3495, 1)),\n'
     '                    //Fix pottery wheel\n'
     '                    new WorldObject(4310, 10, 1, new Location(3104, 3497, 0)),\n'
     '                    new WorldObject(2031, 10, 3, new Location(3108, 3494, 0))\n'
     '            );\n'
     '        }\n', "B1-d"),
    (TP,
     '        //packMapPre209(12342, "assets/map/osnr_home/624.dat", "assets/map/osnr_home/625.dat");\n'
     '        //packMapsRSPSi(13382, "assets/osnr/custom_maps/NR_home.pack");\n', "B1-e"),
    # ---------------- B-2: RebirthPacker split ----------------
    (RP, '        packHome2025()\n', "B2-a"),
    (RP,
     '    @JvmStatic fun packHome2025() {\n'
     '        assetsBase("assets/rebirth/home_2025/") {\n'
     '            defaultModels()\n'
     '        }\n'
     '    }\n'
     '\n', "B2-b"),
    # ---------------- B-3: XTEALoaderPorted stale home special case ----------------
    (XT,
     '    /**\n'
     '     * Gets the default xtea keys of {0, 0, 0, 0} or\n'
     '     * the correct keys for the home area (egdeville)\n'
     '     * as we repack the maps upon cache update.\n'
     '     * @param region\n'
     '     * @return\n'
     '     */\n'
     '    public static int[] getXTEAs(final int region) {\n'
     '//\t\tif (region == 12342) {\n'
     '//\t\t\treturn getXTEAKeys(region);\n'
     '//\t\t}\n'
     '        return defaultKeys;\n'
     '//\t\treturn getXTEAKeys(region);\n'
     '    }\n', "B3-a"),
    # ---------------- B-4: model map trims ----------------
    (MM,
     '    "reward_casket_inv" to CustomDefinition.Model(2450),\n'
     '\n'
     '    "rebirth_ge_orb_pillarb" to CustomDefinition.Model(10517),\n'
     '\n'
     '    "rebirth_item_rax" to CustomDefinition.Model(29615),\n'
     '\n'
     '    "rebirth_dzone_stallb" to CustomDefinition.Model(modelId = 37907),\n'
     '\n'
     '    "rebirth_zenyte_portal" to CustomDefinition.Model(38000),\n', "B4-a"),
    (MM, '    "rebirth_universal_chest" to CustomDefinition.Model(modelId = 60404),\n', "B4-b"),
    (MM, '    "rebirth_hiscore_board" to CustomDefinition.Model(modelId = 60428),\n', "B4-c"),
    (MM, '    "rebirth_healing_fountain" to CustomDefinition.Model(modelId = 60463), // was 50001 (no usages found)\n', "B4-d"),
    (MM, '    "rebirth_magicstallb" to CustomDefinition.Model(modelId = 60483),\n', "B4-e"),
    (MM,
     '    "rebirth_well_cyan" to CustomDefinition.Model(modelId = 60514),\n'
     '    "rebirth_well_off_blue" to CustomDefinition.Model(modelId = 60515),\n'
     '    "rebirth_well_canopy" to CustomDefinition.Model(modelId = 60516),\n'
     '    "rebirth_tournament_portal" to CustomDefinition.Model(modelId = 60517),\n'
     '    "rebirth_afk_fence" to CustomDefinition.Model(modelId = 60518),\n'
     '    "rebirth_afk_leftgate" to CustomDefinition.Model(modelId = 60519),\n'
     '    "rebirth_afk_rightGate" to CustomDefinition.Model(modelId = 60520),\n'
     '    "rebirth_afk_rightgate" to CustomDefinition.Model(modelId = 60520),\n'
     '    "rebirth_afk_diagonal_fence" to CustomDefinition.Model(modelId = 60521),\n'
     '    "rebirth_skull_fence" to CustomDefinition.Model(modelId = 60522),\n', "B4-f"),
    (MM, '    "rebirth_money_bag_statue" to CustomDefinition.Model(modelId = 60532),\n', "B4-g"),
]

# ---- 1. every FIND matches exactly once ----
texts = {}
for path, find, tag in FINDS:
    p = pathlib.Path(path)
    if not p.exists():
        err(tag, f"file missing: {path}"); continue
    if path not in texts:
        texts[path] = p.read_text()
    n = texts[path].count(find)
    if n != 1:
        err(tag, f"expected 1 match in {path}, found {n}")

# ---- 2. deletion manifest ----
DELETED_FILES = [
    # A-4 dead home-furniture plugins + portal object
    "engine/src/main/java/org/jesse/plugins/object/WildernessStatisticsObject.java",
    "content/generic/src/main/java/org/jesse/plugins/object/HomeBoxOfRestorationObject.java",
    "engine/src/main/java/org/jesse/game/content/skills/magic/lecterns/HomeLecternObject.java",
    "content/minigames/tournament/src/main/kotlin/org/jesse/game/content/tournament/loc/TournamentPortalObject.kt",
    # C-2 misc_models trims
    "cache/assets/rebirth/misc_models/models/rebirth_healing_fountain.dat",
    "cache/assets/rebirth/misc_models/models/rebirth_magicstallb.dat",
    "cache/assets/rebirth/misc_models/models/rebirth_dzone_stallb.dat",
    "cache/assets/rebirth/misc_models/models/rebirth_zenyte_portal.dat",
    # C-4 osnr custom maps
    "cache/assets/osnr/custom_maps/NR_home.pack",
    "cache/assets/osnr/custom_maps/home_landscape.dat",
    "cache/assets/osnr/custom_maps/home_objects.dat",
    # C-5 toml defs
    "cache/assets/types/object/tournament_portal.toml",
    "cache/assets/types/object/universal_chest.toml",
] + [f"cache/assets/map/home{r}_{s}.dat" for r in (23, 24, 25, 26, 27) for s in ("l", "m")] \
  + ["cache/assets/map/christmas_home29_l.dat", "cache/assets/map/christmas_home29_m.dat"]
for f in DELETED_FILES:
    if not pathlib.Path(f).exists():
        err("DEL", f"deletion target missing: {f}")

# C-1 home_2025 dir: exactly the 13 known models
home2025 = sorted(p.name for p in pathlib.Path("cache/assets/rebirth/home_2025/models").glob("*"))
if len(home2025) != 13:
    err("DEL", f"home_2025/models expected 13 files, found {len(home2025)}: {home2025}")

# ---- 3. keep-guards ----
def keep(path, needle, tag):
    p = pathlib.Path(path)
    if not p.exists():
        err(tag, f"keep-file missing: {path}"); return
    if needle not in p.read_text(errors="replace"):
        err(tag, f"keep-needle missing in {path}: {needle!r}")

# tournament module survives minus the portal file
for f in ["TournamentModule.kt", "TournamentManager.kt", "TournamentController.kt",
          "npc/TournamentGuardHomeNpc.kt", "npc/TournamentGuardHomePlugin.kt",
          "loc/TournamentSupplies.kt", "loc/TournamentBarrier.kt",
          "area/TournamentLobbyArea.kt", "area/TournamentFightArea.kt"]:
    if not pathlib.Path(f"{TD}/{f}").exists():
        err("KEEP", f"tournament keep-file missing: {f}")
keep(f"{TD}/npc/TournamentGuardHomeNpc.kt", "Location(3097, 3505, 0)", "KEEP-guard")
# tourny supplies model + its toml/model-map refs
keep("cache/assets/rebirth/misc_models/models/rebirth_obj_tourny_supplies.dat", "", "KEEP-supplies") \
    if pathlib.Path("cache/assets/rebirth/misc_models/models/rebirth_obj_tourny_supplies.dat").exists() \
    else err("KEEP-supplies", "rebirth_obj_tourny_supplies.dat missing")
if not pathlib.Path("cache/assets/rebirth/misc_models/models/rare_drop_table.dat").exists():
    err("KEEP-rdt", "rare_drop_table.dat missing")
keep(MM, '"rebirth_obj_tourny_supplies" to CustomDefinition.Model(modelId = 60456),', "KEEP-mm-supplies")
keep(MM, '"rare_drop_table" to CustomDefinition.Model(modelId = 60476),', "KEEP-mm-rdt")
keep(MM, '"reward_casket_inv" to CustomDefinition.Model(2450),', "KEEP-mm-casket")
keep(MM, '"rebirth_item_rax" to CustomDefinition.Model(29615),', "KEEP-mm-rax")
keep("cache/assets/types/tournament.toml", "models=[60456]", "KEEP-toml-supplies")
# rebirth keep-dirs untouched
for d in ["customs_2023", "customs_2024", "customs_2025", "legacy_models", "master_comp_cape", "misc_models"]:
    if not pathlib.Path(f"cache/assets/rebirth/{d}").is_dir():
        err("KEEP-dir", f"rebirth/{d} missing")
keep(RP, 'assetsBase("assets/rebirth/misc_models/")', "KEEP-misc-pack")
keep(RP, '.modelList("armoured_zombie_red_mist".model())', "KEEP-redmist")
# region12xxx spawn files untouched (incl. the flagged Krystilia duplicate)
keep("content/spawns/region/region12xxx/src/main/kotlin/org/jesse/plugins/spawns/region12342.kt",
     "EMBLEM_TRADER(3096, 3505, 0, SOUTH, 0)", "KEEP-spawns")
# vanilla keeps that must be in no FIND
KEEP_NEEDLES = [
    (PC, 'arrayOf("tourny", "tourney")'),
    (WJ, "public static EntityList<Player> getPlayers()"),
    (TP, "        if (regionID == 8036) {"),
    (TP, 'packMapPre209(13426, "assets/map/osnr_tournament/final_landscape.dat"'),
    (TP, 'packMapsRSPSi(14388, "assets/map/Meiyerditch.pack");'),
    ("engine/src/main/java/org/jesse/plugins/renewednpc/ZenyteGuide.java",
     "public static final Location SPAWN_LOCATION = new Location(3090, 3497);"),
    ("core-model/src/main/kotlin/org/jesse/game/obj/ids/ObjectId.kt", "const val TOURNAMENT_PORTAL = 12355"),
    ("engine/src/main/java/org/jesse/game/content/skills/construction/objects/chapel/Altar.java",
     "ObjectId.ALTAR_13199"),
    ("engine/src/main/kotlin/org/jesse/game/content/universalshop/UniversalShopInterface.kt",
     "player.location.regionId != 12342"),
]
for path, needle in KEEP_NEEDLES:
    keep(path, needle, "KEEP")
    for fpath, find, tag in FINDS:
        if fpath == path and needle in find:
            err("KEEP", f"kept needle {needle!r} appears inside FIND {tag}")

if not pathlib.Path("cache/assets/rebirth/customs_2023/models/reward_casket_inv.dat").exists():
    err("KEEP-casket", "reward_casket_inv.dat missing from customs_2023")
if not pathlib.Path("cache/assets/rebirth/customs_2025/models/rebirth_item_rax.dat").exists():
    err("KEEP-rax", "rebirth_item_rax.dat missing from customs_2025")

print("CLEAN" if fail == 0 else f"FAILED with {fail} error(s)")
sys.exit(1 if fail else 0)
```

## Part E — gates
1 dry-run CLEAN · 2 Part A → compile · 3 Part B → compile · 4 Part C → compile · 5 post-greps: packHome2025/home_2025 · home2x/christmas_home29/NR_home/home_landscape/home_objects/osnr_home (allowed: kept osnr_home monastery package) · packMapsRSPSiModern · AFK_ZONE · TOURNAMENT_PORTAL_60446/TournamentPortalObject/LOCATION_IN_FRONT_OF_PORTAL/moveToTournamentPortal/"red portal" · universal_chest/60404 · dead model ids in cache/src · homeException/HOME_ALTAR_OBJ/EDGEVILLE_FURNACE/homeChristmasCupboardLocation/HOME_MANHOLE/HOME_ZENYTE_GUIDE/HOME_MAKE_OVER_MAGE/WIZARD_LOCATION · 12342 (allowed: UniversalShopInterface gate → 5a; NEW Smelting region gate; TILE_HIGHLIGHTING_VARBIT_ID). Keep-asserts: TOURNAMENT_EXIT_LOCATION ×4, moveToTournamentExit ×3, BankPolygons Edgeville line.
6-9 local: cache regen (no dead-model pack lines, no `no id mapping found`, 13426/13428 packed) · clean + plugin scanner (4 deleted plugin classes stale) · boot · in-game: ::afk/::slayer unknown; ::tourny → guard; tournament full loop via guard, exits to (3105,3487); broadcast names the Guard; firemaking blocked in Edgeville bank, allowed outside; Varrock-armour double-bar at Edgeville furnace works; pottery at vanilla site; POH gilded altar 2.5x, named vanilla altars no-op; Edgeville trapdoor; christmas cupboard return; Bob portal return beside bank; clue caskets render; Zemouregal red mist; save-guard: doctored save in region 13382/13639 → home; tournament-lobby save untouched.
10 commit (plan + dry-run deleted), push.

## Part F — save impact
None expected: no item defs removed (both tomls are objects; portal/lectern/box/chest were world objects). TickVariable.BOX_OF_RESTORATION deliberately LEFT (ordinal persistence). A-17 relocates upstream saves stranded on the never-live NR_home island. "box of restoration delay" attribute inert.

## Part G — deliberately left
2450/29615 vanilla model overrides in kept dirs (5c/6, needs render verify) · orphan models in kept rebirth dirs (6) · SlayerMaster.SUMONA dead-master slice (5c/5d) · TickVariable.BOX_OF_RESTORATION (6, with migration look) · unreferenced ObjectId constants (6) · getOnlineActivePlayerCount zero-caller (6) · WorldBroadcasts occult/vote/store tips (5a/6) · Analytics.java home_stalls/afk_skilling SQL columns (6, schema-coupled) · aldarin "Home for now temp" live mapping (content decision) · UniversalShopInterface 12342 gate (5a) · Krystilia duplicate spawn (pre-existing vanilla-data bug, flagged) · tournament guard lacks the portal's empty-equipment check (pre-existing, keep-set pass) · Land of Snow seasonal content (live keep).

## Part H — roadmap bookkeeping
Stage 4 gate RESOLVED as already-satisfied; portal checkpoint RESOLVED REMOVE; AFK+::slayer removed; two vanilla restores (bank polygon, Varrock-armour perk re-anchor). Corrections: Smelting perk was vanilla; LOCATION_IN_FRONT_OF_PORTAL live plumbing; HOME_ZENYTE_GUIDE live consumer; cupboard return branch live; guard adjacent-not-atop. Login guard covers the NR_home island. Next: 5a (universal shop — plan ready; 12342 gate + ::shop die there) then 5b/5c/5d per sequencing. Stage 6 backlog: 2450/29615, TickVariable, ObjectId tidy, zero-caller World method, Analytics columns, packMaps unused param.
