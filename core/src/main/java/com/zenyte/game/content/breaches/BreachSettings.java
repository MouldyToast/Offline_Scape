package com.zenyte.game.content.breaches;

import com.zenyte.game.item.ids.ItemId;
import com.zenyte.game.npc.ids.NpcId;

public class BreachSettings {

    /*
    Final settings for the scheduler
     */
    public static final int BREACH_INTERVAL = 60; // breach interval in minutes
    public static final int BREACH_DELAY = 120; // delay start of breaches in minutes

    /*
    Changeable from the control panel
     */
    public static boolean BREACHES_ACTIVE = true;
    public static int BOSS_AMOUNT = 3; // the amount of bosses to spawn upon breach creation
    public static int BOSS_QUANTITY = 50; // the amount of bosses a breach will spawn in total
    public static int BOSS_RADIUS = 10; // the radius in which the bosses can wander
                                                // - also affects the radius of their spawns
    public static final int CREDIT_KILLER_AMOUNT = 15; // the amount of players who can get a resource drop

    /*
    VISUALS
     */
    public static final int BREACH_PORTAL_OBJECT_ID = 49561;
    public static final int BREACH_SPAWN_BOSS_PORTAL_OBJECT_ID = 49562;

    /*
    MVP killer LOOT
    Credited killer LOOT
     */
    //            [itemId][minAm][maxAm][dropWeight](dropWeightRandom == 1) TODO
    public static int[][] MVP_REWARD_ITEMS = {
            {ItemId.TRINKET_OF_ADVANCED_WEAPONRY, 1, 1, 10}
    };
    public static int[][] CREDIT_REWARD_ITEMS = {
            {ItemId.LOBSTER+1, 50, 80, 4},
            {ItemId.SUPERANTIPOISON4 + 1, 8, 16, 4},
            {ItemId.CHAOS_RUNE, 400, 600, 4},
            {ItemId.IRIT_LEAF+1, 30, 40, 4},
            {ItemId.UNICORN_HORN+1, 36, 40, 4},

            {ItemId.SWORDFISH+1, 50, 80, 6},
            {ItemId.SUPER_STRENGTH4+1, 8, 16, 6},
            {ItemId.COSMIC_RUNE, 400, 600, 6},
            {ItemId.KWUARM+1, 30, 40, 6},
            {ItemId.LIMPWURT_ROOT+1, 36, 40, 6},

            {ItemId.MONKFISH+1, 50, 80, 8},
            {ItemId.SUPER_ENERGY4+1, 8, 16, 8},
            {ItemId.LAW_RUNE, 400, 600, 8},
            {ItemId.AVANTOE+1, 30, 40, 8},
            {ItemId.MORT_MYRE_FUNGUS+1, 36, 40, 8},

            {ItemId.SHARK+1, 50, 80, 12},
            {ItemId.PRAYER_POTION4+1, 8, 16, 12},
            {ItemId.NATURE_RUNE, 400, 600, 12},
            {ItemId.RANARR_WEED+1, 30, 40, 12},
            {ItemId.SNAPE_GRASS+1, 36, 40, 12},

            {ItemId.SHARK+1, 100, 150, 14},
            {ItemId.SUPER_DEFENCE4+1, 8, 16, 14},
            {ItemId.MUD_RUNE, 400, 600, 14},
            {ItemId.CADANTINE+1, 30, 40, 14},
            {ItemId.WHITE_BERRIES+1, 36, 40, 14},

            {ItemId.COOKED_KARAMBWAN+1, 50, 80, 14},
            {ItemId.ANTIFIRE_POTION4+1, 8, 16, 14},
            {ItemId.MIST_RUNE, 400, 600, 14},
            {ItemId.LANTADYME+1, 30, 40, 14},
            {ItemId.BLUE_DRAGON_SCALE+1, 36, 40, 14},

            {ItemId.COOKED_KARAMBWAN+1, 50, 100, 16},
            {ItemId.RANGING_POTION4+1, 8, 16, 16},
            {ItemId.STEAM_RUNE, 400, 600, 16},
            {ItemId.DWARF_WEED+1, 30, 40, 16},
            {ItemId.WINE_OF_ZAMORAK+1, 36, 40, 16},

            {ItemId.SEA_TURTLE+1, 50, 80, 16},
            {ItemId.SARADOMIN_BREW4+1, 8, 16, 16},
            {ItemId.LAVA_RUNE, 400, 600, 16},
            {ItemId.TOADFLAX+1, 30, 40, 16},
            {ItemId.CRUSHED_NEST+1, 36, 40, 16},

            {ItemId.SEA_TURTLE+1, 100, 150, 18},
            {ItemId.SUPER_RESTORE4+1, 8, 16, 18},
            {ItemId.DEATH_RUNE, 400, 600, 18},
            {ItemId.SNAPDRAGON+1, 30, 40, 18},
            {ItemId.RED_SPIDERS_EGGS+1, 36, 40, 18},

            {ItemId.MANTA_RAY+1, 50, 80, 20},
            {ItemId.SUPER_COMBAT_POTION4+1, 8, 16, 20},
            {ItemId.BLOOD_RUNE, 400, 600, 20},
            {ItemId.TORSTOL+1, 30, 40, 20},
            {ItemId.POTATO_CACTUS+1, 36, 40, 20},

            {ItemId.MANTA_RAY+1, 100, 150, 22},
            {ItemId.ANTIVENOM4+1, 8, 16, 22},
            {ItemId.WRATH_RUNE, 400, 600, 22},
            {ItemId.SOUL_RUNE, 400, 600, 22}
    };

    /*
    BREACH NPCS
     */
    public static final int[] BREACH_NPCS = {
            NpcId.GENERAL_GRAARDOR_12444,
            NpcId.KRIL_TSUTSAROTH_12446,
            NpcId.COMMANDER_ZILYANA_12445,
            NpcId.KREEARRA_12443,
            NpcId.CAVE_ABOMINATION_12454,
            NpcId.DAGANNOTH_PRIME_12442,
            NpcId.DAGANNOTH_REX_12439,
            NpcId.DAGANNOTH_SUPREME_12441,
            NpcId.DERWEN_12450,
            NpcId.DHAROK_THE_WRETCHED_12447,
            NpcId.GREATER_ABYSSAL_DEMON_12451,
            NpcId.JALIMKOT_12455,
            NpcId.JUSTICIAR_ZACHARIAH_12449,
            NpcId.KING_BLACK_DRAGON_12440,
            NpcId.MALEVOLENT_MAGE_12456,
            NpcId.NIGHT_BEAST_12459,
            NpcId.SULPHUR_LIZARD_12458,
//            NpcId.DURIAL_321,
            NpcId.VITREOUS_WARPED_JELLY_12457
    };
}
