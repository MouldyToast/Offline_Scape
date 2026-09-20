package org.jesse.game.content.skills.construction;

import org.jesse.game.content.skills.construction.constants.Furniture;
import org.jesse.game.content.skills.construction.constants.FurnitureSpace;
import org.jesse.game.content.skills.construction.constants.RoomType;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.dynamicregion.CoordinateUtilities;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * House viewer interface (interface 422).
 * <p>
 * Rev-240 rewrote the viewer's cs2 to use dbtable lookups instead of cache
 * enums. The server packs per-hotspot furniture tiers into three ints that
 * the client unpacks via {@code poh_viewer_setroom} (script 1376).
 * <p>
 * See {@code investigation_house_viewer_rev240.md} for the full bit layout
 * derivation and live-game validation.
 */
public final class HouseViewer {

    private static final int INTERFACE_ID = 422;
    private static final int ROOM_SCRIPT = 1376;
    private static final int GRID_SCRIPT = 1382;

    /**
     * Grid base values, from 5x5 to 9x9.
     * Still used by handleInterface for room-selection index calculations.
     */
    private static final int[][] GRIDS = new int[][] {
            new int[] { 32770, 65539, 9 },
            new int[] { 16385, 65539, -1 },
            new int[] { 16385, 81925, -1 },
            new int[] { 16385, 98310, -1 },
            new int[] { 32770, 131072, -1 },
    };

    // ── Hotspot slot mapping ────────────────────────────────────────────
    // Maps (RoomType, FurnitureSpace) → slot index in the rev-240 poh_room
    // dbtable's hotspot list. Built from osrs-dumps-rev240/config/dump.dbrow.
    //
    // FurnitureSpaces with no entry here (e.g. spice rack, league hall items)
    // are not yet buildable in the server and default to tier 0 (empty).
    private static final Map<RoomType, Map<FurnitureSpace, Integer>> HOTSPOT_SLOTS = new EnumMap<>(RoomType.class);

    static {
        // ── Parlour (type 1) ────────────────────────────────────────────
        // slot 0-2: armchair, 3: rug, 4: bookcase, 5: fireplace, 6: curtains
        Map<FurnitureSpace, Integer> parlour = new EnumMap<>(FurnitureSpace.class);
        parlour.put(FurnitureSpace.CHAIR_SPACE1, 0);
        parlour.put(FurnitureSpace.CHAIR_SPACE2, 1);
        parlour.put(FurnitureSpace.CHAIR_SPACE3, 2);
        parlour.put(FurnitureSpace.RUG_SPACE, 3);
        parlour.put(FurnitureSpace.BOOKCASE_SPACE, 4);
        parlour.put(FurnitureSpace.FIREPLACE_SPACE, 5);
        parlour.put(FurnitureSpace.CURTAIN_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.PARLOUR, parlour);

        // ── Garden (type 2) ─────────────────────────────────────────────
        // slot 0: centerpiece, 1-2: tree, 3-4: big plant, 5-6: small plant, 7: tip jar
        Map<FurnitureSpace, Integer> garden = new EnumMap<>(FurnitureSpace.class);
        garden.put(FurnitureSpace.CENTREPIECE_SPACE, 0);
        garden.put(FurnitureSpace.TREE_SPACE, 1);
        garden.put(FurnitureSpace.BIG_TREE_SPACE, 2);
        garden.put(FurnitureSpace.BIG_PLANT_SPACE_1, 3);
        garden.put(FurnitureSpace.BIG_PLANT_SPACE_2, 4);
        garden.put(FurnitureSpace.SMALL_PLANT_SPACE_1, 5);
        garden.put(FurnitureSpace.SMALL_PLANT_SPACE_2, 6);
        garden.put(FurnitureSpace.TIP_JAR_SPACE, 7);
        HOTSPOT_SLOTS.put(RoomType.GARDEN, garden);

        // ── Kitchen (type 3) ────────────────────────────────────────────
        // slot 0: stove, 1: shelf, 2: barrel, 3: cat basket, 4: larder, 5: sink, 6: table, 7: spice rack (no FurnitureSpace)
        Map<FurnitureSpace, Integer> kitchen = new EnumMap<>(FurnitureSpace.class);
        kitchen.put(FurnitureSpace.STOVE_SPACE, 0);
        kitchen.put(FurnitureSpace.SHELF_SPACE, 1);
        kitchen.put(FurnitureSpace.SHELF_SPACE2, 1);  // both shelf objects → same viewer slot
        kitchen.put(FurnitureSpace.BARREL_SPACE, 2);
        kitchen.put(FurnitureSpace.CAT_BASKET_SPACE, 3);
        kitchen.put(FurnitureSpace.LARDER_SPACE, 4);
        kitchen.put(FurnitureSpace.SINK_SPACE, 5);
        kitchen.put(FurnitureSpace.TABLE_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.KITCHEN, kitchen);

        // ── Dining room (type 4) ────────────────────────────────────────
        // slot 0: dining table, 1-2: chairs, 3: fireplace, 4: curtains, 5: wall crest, 6: bellpull
        Map<FurnitureSpace, Integer> dining = new EnumMap<>(FurnitureSpace.class);
        dining.put(FurnitureSpace.DINING_TABLE_SPACE, 0);
        dining.put(FurnitureSpace.SEATING_SPACE, 1);
        dining.put(FurnitureSpace.SEATING_SPACE2, 2);
        dining.put(FurnitureSpace.DINING_FIREPLACE_SPACE, 3);
        dining.put(FurnitureSpace.DINING_CURTAIN_SPACE, 4);
        dining.put(FurnitureSpace.DINING_DECORATION_SPACE, 5);
        dining.put(FurnitureSpace.BELL_PULL_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.DINING_ROOM, dining);

        // ── Bedroom (type 5) ────────────────────────────────────────────
        // slot 0: bed, 1: wardrobe, 2: mirror, 3: curtains, 4: rug, 5: fireplace, 6: clock
        Map<FurnitureSpace, Integer> bedroom = new EnumMap<>(FurnitureSpace.class);
        bedroom.put(FurnitureSpace.BED_SPACE, 0);
        bedroom.put(FurnitureSpace.WARDROBE_SPACE, 1);
        bedroom.put(FurnitureSpace.DRESSER_SPACE, 2);
        bedroom.put(FurnitureSpace.BEDROOM_CURTAIN_SPACE, 3);
        bedroom.put(FurnitureSpace.BEDROOM_RUG_SPACE, 4);
        bedroom.put(FurnitureSpace.BEDROOM_FIREPLACE_SPACE, 5);
        bedroom.put(FurnitureSpace.CORNER_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.BEDROOM, bedroom);

        // ── Games room (type 6) ─────────────────────────────────────────
        // slot 0: null, 1: party game, 2: null, 3: prize chest, 4: attack stone, 5: balance, 6: ranging
        Map<FurnitureSpace, Integer> games = new EnumMap<>(FurnitureSpace.class);
        games.put(FurnitureSpace.GAME_SPACE, 1);
        games.put(FurnitureSpace.PRIZE_CHEST_SPACE, 3);
        games.put(FurnitureSpace.STONE_SPACE, 4);
        games.put(FurnitureSpace.ELEMENTAL_BALANCE_SPACE, 5);
        games.put(FurnitureSpace.RANGING_GAME_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.GAMES_ROOM, games);

        // ── Skill hall (type 7) & downstairs variant (type 8) ───────────
        // slot 0: stair, 1: monster trophy, 2: null, 3: fish trophy, 4: armour, 5: cw armour, 6: rune case
        Map<FurnitureSpace, Integer> skillHall = new EnumMap<>(FurnitureSpace.class);
        skillHall.put(FurnitureSpace.SKILL_HALL_STAIRCASE, 0);
        skillHall.put(FurnitureSpace.HEAD_TROPHY_SPACE, 1);
        skillHall.put(FurnitureSpace.FISHING_TROPHY_SPACE, 3);
        skillHall.put(FurnitureSpace.ARMOUR_SPACE, 4);
        skillHall.put(FurnitureSpace.CW_ARMOUR_SPACE, 5);
        skillHall.put(FurnitureSpace.RUNE_CASE_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.SKILL_HALL, skillHall);
        Map<FurnitureSpace, Integer> skillHallDs = new EnumMap<>(FurnitureSpace.class);
        skillHallDs.put(FurnitureSpace.SKILL_HALL_STAIRCASE_DS, 0);
        skillHallDs.put(FurnitureSpace.HEAD_TROPHY_SPACE, 1);
        skillHallDs.put(FurnitureSpace.FISHING_TROPHY_SPACE, 3);
        skillHallDs.put(FurnitureSpace.ARMOUR_SPACE, 4);
        skillHallDs.put(FurnitureSpace.CW_ARMOUR_SPACE, 5);
        skillHallDs.put(FurnitureSpace.RUNE_CASE_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.SKILL_HALL_DS, skillHallDs);

        // ── Quest hall (type 9) & downstairs variant (type 10) ──────────
        // slot 0: stair, 1: portrait, 2: landscape, 3: quest trophy, 4: sword, 5: map, 6: bookcase
        Map<FurnitureSpace, Integer> questHall = new EnumMap<>(FurnitureSpace.class);
        questHall.put(FurnitureSpace.QUEST_HALL_STAIRCASE, 0);
        questHall.put(FurnitureSpace.PORTRAIT_SPACE, 1);
        questHall.put(FurnitureSpace.LANDSCAPE_SPACE, 2);
        questHall.put(FurnitureSpace.GUILD_TROPHY_SPACE, 3);
        questHall.put(FurnitureSpace.SWORD_SPACE, 4);
        questHall.put(FurnitureSpace.MAP_SPACE, 5);
        questHall.put(FurnitureSpace.QUEST_BOOKCASE_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.QUEST_HALL, questHall);
        Map<FurnitureSpace, Integer> questHallDs = new EnumMap<>(FurnitureSpace.class);
        questHallDs.put(FurnitureSpace.QUEST_HALL_STAIRCASE_DS, 0);
        questHallDs.put(FurnitureSpace.PORTRAIT_SPACE, 1);
        questHallDs.put(FurnitureSpace.LANDSCAPE_SPACE, 2);
        questHallDs.put(FurnitureSpace.GUILD_TROPHY_SPACE, 3);
        questHallDs.put(FurnitureSpace.SWORD_SPACE, 4);
        questHallDs.put(FurnitureSpace.MAP_SPACE, 5);
        questHallDs.put(FurnitureSpace.QUEST_BOOKCASE_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.QUEST_HALL_DS, questHallDs);

        // ── Chapel (type 11) ────────────────────────────────────────────
        // slot 0: icon, 1: altar, 2: incense burner, 3: window, 4: rug, 5: statue, 6: instrument
        Map<FurnitureSpace, Integer> chapel = new EnumMap<>(FurnitureSpace.class);
        chapel.put(FurnitureSpace.ICON_SPACE, 0);
        chapel.put(FurnitureSpace.ALTAR_SPACE, 1);
        chapel.put(FurnitureSpace.LAMP_SPACE, 2);
        chapel.put(FurnitureSpace.CHAPEL_WINDOW_SPACE, 3);
        chapel.put(FurnitureSpace.CHAPEL_RUG_SPACE, 4);
        chapel.put(FurnitureSpace.STATUES_SPACE, 5);
        chapel.put(FurnitureSpace.MUSICAL_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.CHAPEL, chapel);

        // ── Workshop (type 12) ──────────────────────────────────────────
        // slot 0: workbench, 1: crafting table, 2: tool store, 3: repair, 4: heraldry
        Map<FurnitureSpace, Integer> workshop = new EnumMap<>(FurnitureSpace.class);
        workshop.put(FurnitureSpace.WORKBENCH_SPACE, 0);
        workshop.put(FurnitureSpace.CLOCKMAKING_SPACE, 1);
        workshop.put(FurnitureSpace.TOOL_SPACE1, 2);
        workshop.put(FurnitureSpace.TOOL_SPACE2, 2);
        workshop.put(FurnitureSpace.TOOL_SPACE3, 2);
        workshop.put(FurnitureSpace.TOOL_SPACE4, 2);
        workshop.put(FurnitureSpace.TOOL_SPACE5, 2);
        workshop.put(FurnitureSpace.REPAIR_SPACE, 3);
        workshop.put(FurnitureSpace.HERALDRY_SPACE, 4);
        HOTSPOT_SLOTS.put(RoomType.WORKSHOP, workshop);

        // ── Study (type 13) ─────────────────────────────────────────────
        // slot 0: lectern, 1: globe, 2: null, 3: crystal ball, 4: wall chart, 5: telescope, 6: bookcase
        Map<FurnitureSpace, Integer> study = new EnumMap<>(FurnitureSpace.class);
        study.put(FurnitureSpace.LECTERN_SPACE, 0);
        study.put(FurnitureSpace.GLOBE_SPACE, 1);
        study.put(FurnitureSpace.CRYSTAL_BALL_SPACE, 3);
        study.put(FurnitureSpace.WALL_CHART_SPACE, 4);
        study.put(FurnitureSpace.TELESCOPE_SPACE, 5);
        study.put(FurnitureSpace.STUDY_BOOKCASE_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.STUDY, study);

        // ── Portal chamber (type 14) ────────────────────────────────────
        // slot 0-2: null (portal destination metadata), 3-5: portal frame, 6: portal focus
        Map<FurnitureSpace, Integer> portal = new EnumMap<>(FurnitureSpace.class);
        portal.put(FurnitureSpace.PORTAL_SPACE1, 3);
        portal.put(FurnitureSpace.PORTAL_SPACE2, 4);
        portal.put(FurnitureSpace.PORTAL_SPACE3, 5);
        portal.put(FurnitureSpace.CENTERPIECE_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.PORTAL_CHAMBER, portal);

        // ── Throne room (type 15) ───────────────────────────────────────
        // slot 0: throne, 1: cage/floor, 2: wall crest, 3: lever, 4-5: chairs, 6: trapdoor
        Map<FurnitureSpace, Integer> throne = new EnumMap<>(FurnitureSpace.class);
        throne.put(FurnitureSpace.THRONE_SPACE, 0);
        throne.put(FurnitureSpace.THRONE_FLOOR, 1);
        throne.put(FurnitureSpace.THRONE_DECORATION, 2);
        throne.put(FurnitureSpace.LEVER, 3);
        throne.put(FurnitureSpace.THRONE_SEATING, 4);
        throne.put(FurnitureSpace.THRONE_SEATING2, 5);
        throne.put(FurnitureSpace.TRAPDOOR, 6);
        HOTSPOT_SLOTS.put(RoomType.THRONE_ROOM, throne);

        // ── Oubliette (type 16) ─────────────────────────────────────────
        // slot 0: trap, 1: cage, 2: guard, 3: lighting, 4: ladder, 5: null, 6: decor
        Map<FurnitureSpace, Integer> oubliette = new EnumMap<>(FurnitureSpace.class);
        oubliette.put(FurnitureSpace.FLOOR_SPACE, 0);
        oubliette.put(FurnitureSpace.PRISON_SPACE, 1);
        oubliette.put(FurnitureSpace.GUARD_SPACE1, 2);
        oubliette.put(FurnitureSpace.LIGHTING_SPACE, 3);
        oubliette.put(FurnitureSpace.LADDER, 4);
        oubliette.put(FurnitureSpace.DECORATION_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.OUBLIETTE, oubliette);

        // ── Dungeon corridor (type 17) ──────────────────────────────────
        // slot 0: guard, 1-2: trap, 3-4: door, 5: lighting, 6: decor
        Map<FurnitureSpace, Integer> dungCorr = new EnumMap<>(FurnitureSpace.class);
        dungCorr.put(FurnitureSpace.GUARD_SPACE1, 0);
        dungCorr.put(FurnitureSpace.GUARD_SPACE2, 0);
        dungCorr.put(FurnitureSpace.TRAP_SPACE, 1);
        dungCorr.put(FurnitureSpace.TRAP_SPACE2, 2);
        dungCorr.put(FurnitureSpace.DOOR_SPACE, 3);
        dungCorr.put(FurnitureSpace.DOOR_SPACE2, 4);
        dungCorr.put(FurnitureSpace.LIGHTING_SPACE, 5);
        dungCorr.put(FurnitureSpace.LIGHTING_SPACE2, 5);
        dungCorr.put(FurnitureSpace.DECORATION_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.DUNGEON_CORRIDOR, dungCorr);

        // ── Dungeon junction (type 18) — same layout as corridor ────────
        HOTSPOT_SLOTS.put(RoomType.DUNGEON_JUNCTION, dungCorr);

        // ── Dungeon stairs (type 19) ────────────────────────────────────
        // slot 0: stair, 1-2: guard, 3-4: door, 5: lighting, 6: decor
        Map<FurnitureSpace, Integer> dungStairs = new EnumMap<>(FurnitureSpace.class);
        dungStairs.put(FurnitureSpace.SKILL_HALL_STAIRCASE, 0);  // dungeon stairs reuse staircase space
        dungStairs.put(FurnitureSpace.GUARD_SPACE3, 1);
        dungStairs.put(FurnitureSpace.GUARD_SPACE4, 2);
        dungStairs.put(FurnitureSpace.DOOR_SPACE, 3);
        dungStairs.put(FurnitureSpace.DOOR_SPACE2, 4);
        dungStairs.put(FurnitureSpace.LIGHTING_SPACE, 5);
        dungStairs.put(FurnitureSpace.LIGHTING_SPACE2, 5);
        dungStairs.put(FurnitureSpace.DECORATION_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.DUNGEON_STAIRS_ROOM, dungStairs);

        // ── Treasure room (type 20) ─────────────────────────────────────
        // slot 0: treasure, 1: elite guard, 2: null, 3: door, 4: wall crest, 5: lighting, 6: decor
        Map<FurnitureSpace, Integer> treasure = new EnumMap<>(FurnitureSpace.class);
        treasure.put(FurnitureSpace.TREASURE_SPACE, 0);
        treasure.put(FurnitureSpace.MONSTER_SPACE, 1);
        treasure.put(FurnitureSpace.DOOR_SPACE, 3);
        treasure.put(FurnitureSpace.TREASURE_DECORATION_SPACE, 4);
        treasure.put(FurnitureSpace.LIGHTING_SPACE, 5);
        treasure.put(FurnitureSpace.DECORATION_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.TREASURE_ROOM, treasure);

        // ── Formal garden (type 21) ─────────────────────────────────────
        // slot 0: centerpiece, 1: fencing, 2: hedge, 3-4: flower, 5-6: flower (no extras), 7: tip jar
        Map<FurnitureSpace, Integer> formalGarden = new EnumMap<>(FurnitureSpace.class);
        formalGarden.put(FurnitureSpace.FORMAL_CENTERPIECE, 0);
        formalGarden.put(FurnitureSpace.FORMAL_FENCING, 1);
        formalGarden.put(FurnitureSpace.FORMAL_HEDGE, 2);
        formalGarden.put(FurnitureSpace.FORMAL_BIG_PLANT, 3);
        formalGarden.put(FurnitureSpace.FORMAL_BIG_PLANT_2, 4);
        formalGarden.put(FurnitureSpace.FORMAL_PLANT_1, 5);
        formalGarden.put(FurnitureSpace.FORMAL_PLANT_2, 6);
        formalGarden.put(FurnitureSpace.TIP_JAR_SPACE, 7);
        HOTSPOT_SLOTS.put(RoomType.FORMAL_GARDEN, formalGarden);

        // ── Combat room (type 22) ───────────────────────────────────────
        // slot 0: ring, 1: null, 2: null, 3: weapon rack, 4: wall crest, 5: dummy
        Map<FurnitureSpace, Integer> combat = new EnumMap<>(FurnitureSpace.class);
        combat.put(FurnitureSpace.COMBAT_RING_SPACE, 0);
        combat.put(FurnitureSpace.STORAGE_SPACE, 3);
        combat.put(FurnitureSpace.COMBAT_DECORATION_SPACE, 4);
        combat.put(FurnitureSpace.COMBAT_DUMMY_SPACE, 5);
        HOTSPOT_SLOTS.put(RoomType.COMBAT_ROOM, combat);

        // ── Costume room (type 23) ──────────────────────────────────────
        // slot 0: cape rack, 1: magic wardrobe, 2: null, 3: toy box, 4: treasure chest, 5: fancy dress, 6: armour case
        Map<FurnitureSpace, Integer> costume = new EnumMap<>(FurnitureSpace.class);
        costume.put(FurnitureSpace.CAPE_RACK_SPACE, 0);
        costume.put(FurnitureSpace.MAGICAL_WARDROBE_SPACE, 1);
        costume.put(FurnitureSpace.TOY_BOX_SPACE, 3);
        costume.put(FurnitureSpace.TREASURE_CHEST_SPACE, 4);
        costume.put(FurnitureSpace.COSTUME_BOX_SPACE, 5);
        costume.put(FurnitureSpace.ARMOUR_CASE_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.COSTUME_ROOM, costume);

        // ── Menagerie indoors (type 24) ─────────────────────────────────
        // slot 0: pet house, 1: null, 2: null, 3: scratching post, 4: arena, 5: pet list, 6: feeder
        Map<FurnitureSpace, Integer> menagerieIn = new EnumMap<>(FurnitureSpace.class);
        menagerieIn.put(FurnitureSpace.PET_HOUSE_SPACE, 0);
        menagerieIn.put(FurnitureSpace.SCRATCHING_POST_SPACE, 3);
        menagerieIn.put(FurnitureSpace.ARENA_SPACE, 4);
        menagerieIn.put(FurnitureSpace.PET_LIST_SPACE, 5);
        menagerieIn.put(FurnitureSpace.PET_FEEDER_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.MENAGERIE_INDOORS, menagerieIn);

        // ── Menagerie outdoors (type 25) ────────────────────────────────
        // slot 0: pet house, 1: null, 2: habitat, 3: scratching post, 4: arena, 5: pet list, 6: feeder
        Map<FurnitureSpace, Integer> menagerieOut = new EnumMap<>(FurnitureSpace.class);
        menagerieOut.put(FurnitureSpace.PET_HOUSE_SPACE, 0);
        menagerieOut.put(FurnitureSpace.HABITAT_SPACE, 2);
        menagerieOut.put(FurnitureSpace.SCRATCHING_POST_SPACE, 3);
        menagerieOut.put(FurnitureSpace.ARENA_SPACE, 4);
        menagerieOut.put(FurnitureSpace.PET_LIST_SPACE, 5);
        menagerieOut.put(FurnitureSpace.PET_FEEDER_SPACE, 6);
        HOTSPOT_SLOTS.put(RoomType.MENAGERIE_OUTDOORS, menagerieOut);

        // ── Superior garden (type 26) ───────────────────────────────────
        // slot 0: teleport, 1: topiary, 2: pool, 3: theme, 4: fencing, 5-6: bench
        Map<FurnitureSpace, Integer> superiorGarden = new EnumMap<>(FurnitureSpace.class);
        superiorGarden.put(FurnitureSpace.TELEPORT_SPACE, 0);
        superiorGarden.put(FurnitureSpace.TOPIARY_SPACE, 1);
        superiorGarden.put(FurnitureSpace.POOL_SPACE, 2);
        superiorGarden.put(FurnitureSpace.THEME_SPACE, 3);
        superiorGarden.put(FurnitureSpace.FENCING_SPACE, 4);
        superiorGarden.put(FurnitureSpace.SUPERIOR_SEATING_SPACE, 5);
        superiorGarden.put(FurnitureSpace.SUPERIOR_SEATING_SPACE2, 6);
        HOTSPOT_SLOTS.put(RoomType.SUPERIOR_GARDEN, superiorGarden);

        // ── Achievement gallery (type 27) ───────────────────────────────
        // slot 0: spell altar, 1: adventure log, 2: jewellery box, 3: boss lair, 4: display, 5: quest list
        Map<FurnitureSpace, Integer> achieveGallery = new EnumMap<>(FurnitureSpace.class);
        achieveGallery.put(FurnitureSpace.ACHIEVEMENT_ALTAR_SPACE, 0);
        achieveGallery.put(FurnitureSpace.ADVENTURE_LOG_SPACE, 1);
        achieveGallery.put(FurnitureSpace.JEWELLERY_BOX, 2);
        achieveGallery.put(FurnitureSpace.BOSS_LAIR_SPACE, 3);
        achieveGallery.put(FurnitureSpace.DISPLAY_SPACE, 4);
        achieveGallery.put(FurnitureSpace.QUEST_LIST_SPACE, 5);
        HOTSPOT_SLOTS.put(RoomType.ACHIEVEMENT_GALLERY, achieveGallery);

        // ── Portal nexus (type 28) ──────────────────────────────────────
        // slot 0: nexus portal, 1: rug, 2: curtains, 3-4: amulet
        Map<FurnitureSpace, Integer> nexus = new EnumMap<>(FurnitureSpace.class);
        nexus.put(FurnitureSpace.PORTAL_NEXUS_SPACE, 0);
        nexus.put(FurnitureSpace.NEXUS_RUG_SPACE, 1);
        nexus.put(FurnitureSpace.NEXUS_CURTAIN_SPACE, 2);
        nexus.put(FurnitureSpace.NEXUS_AMULET_SPACE_1, 3);
        nexus.put(FurnitureSpace.NEXUS_AMULET_SPACE_2, 4);
        HOTSPOT_SLOTS.put(RoomType.PORTAL_NEXUS, nexus);

        // League hall (type 29) — no FurnitureSpace entries exist in server yet
    }

    // ── Tier resolution ─────────────────────────────────────────────────

    /**
     * Returns the 1-based tier of a built furniture within its hotspot's
     * build progression. Tier 0 means nothing built (or unrecognised).
     */
    private static int getFurnitureTier(FurnitureSpace space, Furniture built) {
        final Furniture[] options = space.getFurnitures();
        for (int i = 0; i < options.length; i++) {
            if (options[i] == built) {
                return i + 1;
            }
        }
        return 0;
    }

    // ── Bitmask packing ─────────────────────────────────────────────────
    //
    // The rev-240 cs2 poh_viewer_setroom unpacks per-hotspot furniture tiers
    // from bits 15–31 of int3, bits 0–27 of int4, and bits 0–10 of int5.
    //
    // Bit budget per slot:
    //   Slot 0: 8 bits (int3[15-17] + int4[0-1] + int5[0-2])
    //   Slot 1: 8 bits (int3[18-20] + int4[2-3] + int5[3-5])
    //   Slot 2: 8 bits (int3[21-23] + int4[4-5] + int5[6-8])
    //   Slot 3: 7 bits (int3[24-25] + int4[6-8] + int5[9-10])
    //   Slot 4: 5 bits (int3[26-27] + int4[9-11])
    //   Slot 5: 5 bits (int3[28-29] + int4[12-14])
    //   Slot 6: 5 bits (int3[30-31†] + int4[15-17])  † via sign-bit workaround
    //   Slot 7: 5 bits (int4[18-22])
    //   Slot 8: 5 bits (int4[23-27])

    /**
     * Packs hotspot furniture tiers for one room into the three bitmask ints
     * expected by cs2 1376.
     *
     * @return {int3_furniture_bits, int4, int5} — int3 contains ONLY the
     *         furniture bits (15–31); the caller ORs in the position bits (0–14).
     */
    private int[] packHotspotTiers(RoomReference ref) {
        final int[] tiers = new int[9];

        final Map<FurnitureSpace, Integer> slotMap = HOTSPOT_SLOTS.get(ref.getRoom());
        if (slotMap != null) {
            for (final FurnitureData furn : ref.getFurnitureData()) {
                final Integer slot = slotMap.get(furn.getSpace());
                if (slot != null && slot >= 0 && slot < 9) {
                    tiers[slot] = getFurnitureTier(furn.getSpace(), furn.getFurniture());
                }
            }
        }

        int int3 = 0, int4 = 0, int5 = 0;

        // Slot 0 (8 bits)
        int3 |= (tiers[0] & 0x7) << 15;
        int4 |= (tiers[0] >> 3) & 0x3;
        int5 |= (tiers[0] >> 5) & 0x7;

        // Slot 1 (8 bits)
        int3 |= (tiers[1] & 0x7) << 18;
        int4 |= ((tiers[1] >> 3) & 0x3) << 2;
        int5 |= ((tiers[1] >> 5) & 0x7) << 3;

        // Slot 2 (8 bits)
        int3 |= (tiers[2] & 0x7) << 21;
        int4 |= ((tiers[2] >> 3) & 0x3) << 4;
        int5 |= ((tiers[2] >> 5) & 0x7) << 6;

        // Slot 3 (7 bits)
        int3 |= (tiers[3] & 0x3) << 24;
        int4 |= ((tiers[3] >> 2) & 0x7) << 6;
        int5 |= ((tiers[3] >> 5) & 0x3) << 9;

        // Slot 4 (5 bits)
        int3 |= (tiers[4] & 0x3) << 26;
        int4 |= ((tiers[4] >> 2) & 0x7) << 9;

        // Slot 5 (5 bits)
        int3 |= (tiers[5] & 0x3) << 28;
        int4 |= ((tiers[5] >> 2) & 0x7) << 12;

        // Slot 6 (5 bits) — bit 31 needs sign-bit workaround
        int3 |= (tiers[6] & 0x3) << 30;
        int4 |= ((tiers[6] >> 2) & 0x7) << 15;

        // Slot 7 (5 bits)
        int4 |= (tiers[7] & 0x1F) << 18;

        // Slot 8 (5 bits)
        int4 |= (tiers[8] & 0x1F) << 23;

        // Sign-bit workaround: the server can't send a negative int3, so
        // move bit 31 of int3 into bit 30 of int4 — the client moves it back.
        if ((int3 & (1 << 31)) != 0) {
            int3 &= ~(1 << 31);
            int4 |= (1 << 30);
        }

        return new int[] { int3, int4, int5 };
    }

    // ── Instance fields (unchanged) ─────────────────────────────────────

    public HouseViewer(final Player player) {
        this.player = player;
    }

    private final transient Player player;
    private transient RoomReference room;
    private transient int roomComponent;
    private transient int rotation;
    private transient int plane;
    private transient int size;
    private transient int index;
    private transient boolean displayDungeon;
    private transient boolean displayUpperFloor;

    // ── Opening the viewer ──────────────────────────────────────────────

    public void openHouseViewer() {
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        plane = player.getPlane();
        size = 9;
        for (final RoomReference room : player.getConstruction().getReferences()) {
            if (room.getPlane() == 0) {
                displayDungeon = true;
            } else if (room.getPlane() == 2) {
                displayUpperFloor = true;
            }
        }
        displayDungeon = true;
        displayUpperFloor = true;
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE_ID);

        // ── Per-room: script 1376 (poh_viewer_setroom) ──────────────────
        final int roomCount = player.getConstruction().getReferences().size();
        int count = 1;
        int minX = Integer.MAX_VALUE, maxX = 0;
        int minY = Integer.MAX_VALUE, maxY = 0;
        boolean hasDungeon = false, hasUpperFloor = false;

        for (final RoomReference ref : player.getConstruction().getReferences()) {
            // Track bounding box for the grid script
            minX = Math.min(minX, ref.getX());
            maxX = Math.max(maxX, ref.getX());
            minY = Math.min(minY, ref.getY());
            maxY = Math.max(maxY, ref.getY());
            if (ref.getPlane() == 0) hasDungeon = true;
            if (ref.getPlane() == 2) hasUpperFloor = true;

            // Pack position + room type into bits 0–14 of int3
            int int3 = (ref.getX() & 0x7)
                    | ((ref.getY() & 0x7) << 3)
                    | ((ref.getPlane() & 0x3) << 6)
                    | ((ref.getRotation() & 0x3) << 8)
                    | ((ref.getRoom().getInterfaceSlot() & 0x1F) << 10);

            // Pack hotspot furniture tiers into bits 15+ of int3 and int4/int5
            final int[] tierBits = packHotspotTiers(ref);
            int3 |= tierBits[0];

            player.getPacketDispatcher().sendClientScript(ROOM_SCRIPT,
                    count++,
                    ref.getRoom().getPohRoomDbrow(),
                    ref.getRoom().getDoorMask(),
                    int3,
                    tierBits[1],
                    tierBits[2]
            );
        }

        // ── Grid: script 1382 ───────────────────────────────────────────
        // Rev-240 takes (count, coordLowerLeft, coordUpperRight, viewPlane).
        // Coord packing: level << 28 | x << 14 | z
        final int lowerLevel = hasDungeon ? 0 : 1;
        final int upperLevel = hasUpperFloor ? 2 : 1;
        // If no rooms were added, use safe defaults
        if (minX == Integer.MAX_VALUE) {
            minX = 1; maxX = 1; minY = 1; maxY = 1;
        }
        final int coordLower = (lowerLevel << 28) | (minX << 14) | minY;
        final int coordUpper = (upperLevel << 28) | (maxX << 14) | maxY;

        player.getVarManager().sendBit(5330, 0);
        player.getVarManager().sendBit(5329, roomComponent);
        player.getVarManager().sendBit(5333, 0);
        if (room != null) {
            player.getVarManager().sendBit(5333, room.getRoom().getInterfaceSlot());
        }
        player.getPacketDispatcher().sendClientScript(GRID_SCRIPT, roomCount, coordLower, coordUpper, plane);
        player.getPacketDispatcher().sendComponentSettings(422, 5, 0, 242, AccessMask.CLICK_OP1, AccessMask.CLICK_OP6);
    }

    // ── Interface handling ──────────────────────────────────────────────
    // Rev-240 added 5 room slots (components 39-43), shifting all button
    // component IDs by +5 compared to rev-228:
    //   63=move, 64=rotate, 65/66=cw/ccw, 67=delete, 68=cancel, 69=done

    public void handleInterface(final int interfaceId, final int componentId, final int slotId, final int itemId, final int option) {
        if (componentId > 5 && componentId < 44) {
            player.getTemporaryAttributes().remove("houseviewerMove");
            room = player.getConstruction().getReferences().get(componentId - 6);
            index = GRIDS[size - 5][2] + (82 * room.getPlane()) + (room.getY() * 9) + room.getX() + (2 - room.getPlane());
            player.getVarManager().sendBit(5330, 0);
            player.getVarManager().sendBit(5329, roomComponent = (componentId - 5));
            player.getVarManager().sendBit(5332, 0);
            player.getVarManager().sendBit(5333, room.getRoom().getInterfaceSlot());
        } else if (componentId == 63) {//move
            player.getTemporaryAttributes().put("houseviewerMove", true);
        } else if (componentId == 64) {//rotate
            rotation = room.getRotation();
            player.getVarManager().sendBit(5330, index);
            player.getVarManager().sendBit(5332, 1);
            player.getVarManager().sendBit(5331, rotation);
        } else if (componentId == 65 || componentId == 66) {
            rotate(componentId == 65);
        } else if (componentId == 67) {//delete
            player.getConstruction().getReferences().remove(room);
            for (int i = 5329; i < 5335; i++) {
                player.getVarManager().sendBit(i, 0);
            }
            reopen();
        } else if (componentId == 68) {//cancel
            player.getVarManager().sendBit(5332, 0);
            player.getVarManager().sendBit(5330, 0);
        } else if (componentId == 69) {//done (confirm rotation)
            player.getVarManager().sendBit(5332, 0);
            if (room.getRotation() == rotation) {
                player.getVarManager().sendBit(5330, 0);
                return;
            }
            final int roomRot = room.getRotation();
            int rot = 3;
            if (roomRot == 0) {
                rot = rotation;
            } else if (roomRot == 1) {
                if (rotation != 0) {
                    rot = 1;
                }
            } else if (roomRot == 2) {
                rot = (rotation + 2) & 0x3;
            } else if (roomRot == 3) {
                if (rotation == 0) {
                    rot = 1;
                }
            }
            final int rota = rot;
            room.setRotation(rotation);
            room.getFurnitureData().forEach(r -> {
                final int[] coords = CoordinateUtilities.translate(r.getLocation().getX(), r.getLocation().getY(), rota);
                r.getLocation().setLocation(coords[0], coords[1], r.getLocation().getPlane());
            });
            reopen();
        } else if (componentId == 5) {
            int slot = slotId + 1;
            final int plane = slot > 170 ? 2 : slot > 80 ? 1 : 0;
            int x = 0;
            int y = 0;
            slot -= (size == 5 ? 9 : 0) + (plane * 80);
            y = (slot - 1) / size;
            x = slot - (y * size) - plane;
            y+= player.getConstruction().getYardOffset();
            x+= player.getConstruction().getYardOffset();
            final Object moving = player.getTemporaryAttributes().remove("houseviewerMove");
            if (x == 0 || y == 0 || x == 8 || y == 8) {
                player.sendMessage("You cannot " + (moving == null ? "build" : "move") + " rooms on the edge of the map.");
                return;
            }
            if (moving != null) {
                room.setX(x);
                room.setY(y);
                index = GRIDS[size - 5][2] + (82 * room.getPlane()) + (room.getY() * 9) + room.getX() + 1;

                reopen();
                return;
            }
            player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 212);
            player.getTemporaryAttributes().put("houseviewerRoomAdd", slotId);
        }
    }

    private void reopen() {
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        player.getConstruction().enterHouse(true, player.getConstruction().getRelationalSpawnTile());
        WorldTasksManager.schedule(() -> openHouseViewer(), 3);
    }

    private void rotate(final boolean clockwise) {
        if (clockwise) {
            if (++rotation == 4) {
                rotation = 0;
            }
        } else {
            if (--rotation == -1) {
                rotation = 3;
            }
        }
    }

    public void addRoom(final int componentId, final int slot) {
        final int slotId = (int) player.getTemporaryAttributes().get("houseviewerRoomAdd");
        RoomType room = RoomType.getRoomBySlot(slot);
        final int plane = slotId > 170 ? 2 : slotId > 80 ? 1 : 0;
        if (plane == 0) {
            if (!(room == RoomType.DUNGEON_CORRIDOR || room == RoomType.DUNGEON_JUNCTION || room == RoomType.OUBLIETTE || room == RoomType.DUNGEON_STAIRS_ROOM)) {
                openHouseViewer();
                player.sendMessage("You cannot build a " + room.toString().toLowerCase() + " in dungeon.");
                return;
            }
        } else if (plane == 1) {
            if (room == RoomType.DUNGEON_CORRIDOR || room == RoomType.DUNGEON_JUNCTION || room == RoomType.OUBLIETTE || room == RoomType.DUNGEON_STAIRS_ROOM) {
                openHouseViewer();
                player.sendMessage("You cannot build a " + room.toString().toLowerCase() + " on the surface.");
                return;
            }
        } else if (plane == 2) {
            if (room == RoomType.DUNGEON_CORRIDOR || room == RoomType.DUNGEON_JUNCTION || room == RoomType.OUBLIETTE || room == RoomType.DUNGEON_STAIRS_ROOM) {
                openHouseViewer();
                player.sendMessage("You cannot build a " + room.toString().toLowerCase() + " on the surface.");
                return;
            }
            if (room == RoomType.SKILL_HALL) {
                room = RoomType.SKILL_HALL_DS;
            } else if (room == RoomType.QUEST_HALL) {
                room = RoomType.QUEST_HALL_DS;
            }
        }
        int coordinateSlot = slotId + 1;
        int x = 0;
        int y = 0;
        coordinateSlot -= (size == 5 ? 9 : 0) + (plane * 80);
        y = (coordinateSlot - 1) / size;
        x = coordinateSlot - (y * size) - plane;
        y+= player.getConstruction().getYardOffset();
        x+= player.getConstruction().getYardOffset();
        player.getConstruction().getReferences().add(new RoomReference(room, x, y, plane, 0));
        this.plane = plane;
        reopen();
    }
}