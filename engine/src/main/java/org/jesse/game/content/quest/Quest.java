package org.jesse.game.content.quest;

/**
 * The rev-228 quest list, generated from the client's quest database
 * (dbtable 0) and its quest_progress_get script, which is the
 * authoritative dbrow -> progress varp/varbit mapping.
 *
 * counted marks entries that appear in the quest list's quest count:
 * miniquests and subquests (e.g. the Recipe for Disaster parts) are
 * tracked but not counted.
 */
public enum Quest {
    ANIMAL_MAGNETISM(3185, 0, true, true),
    ANOTHER_SLICE_OF_H_A_M(3550, 1, true, true),
    THE_ASCENT_OF_ARCEUUS(7856, 3, true, true),
    ALFRED_GRIMHAND_S_BARCRAWL(13714, 4, true, false), // miniquest
    BEAR_YOUR_SOUL(5078, 5, true, false), // miniquest
    BELOW_ICE_MOUNTAIN(12063, 6, true, true),
    BETWEEN_A_ROCK(299, 7, true, true),
    BIG_CHOMPY_BIRD_HUNTING(293, 8, false, true),
    BIOHAZARD(68, 9, false, true),
    BLACK_KNIGHTS_FORTRESS(130, 10, false, true),
    BONE_VOYAGE(5795, 11, true, true),
    CABIN_FEVER(655, 12, false, true),
    CLIENT_OF_KOUREND(5619, 13, true, true),
    CLOCK_TOWER(10, 14, false, true),
    COLD_WAR(3293, 15, true, true),
    CONTACT(3274, 16, true, true),
    COOK_S_ASSISTANT(29, 17, false, true),
    THE_CORSAIR_CURSE(6071, 18, true, true),
    CREATURE_OF_FENKENSTRAIN(13715, 19, true, true),
    CURSE_OF_THE_EMPTY_LORD(13713, 20, true, false), // miniquest
    DADDY_S_HOME(10570, 21, true, false), // miniquest
    DARKNESS_OF_HALLOWVALE(2573, 22, true, true),
    DEATH_PLATEAU(314, 23, false, true),
    DEATH_TO_THE_DORGESHUUN(2258, 24, true, true),
    DEMON_SLAYER(2561, 25, true, true),
    THE_DEPTHS_OF_DESPAIR(6027, 26, true, true),
    DESERT_TREASURE_I(358, 27, true, true),
    DEVIOUS_MINDS(1465, 28, true, true),
    THE_DIG_SITE(131, 29, false, true),
    DORIC_S_QUEST(31, 30, false, true),
    DRAGON_SLAYER_I(176, 31, false, true),
    DRAGON_SLAYER_II(6104, 32, true, true),
    DREAM_MENTOR(3618, 33, true, true),
    DRUIDIC_RITUAL(80, 34, false, true),
    DWARF_CANNON(0, 35, false, true),
    EADGAR_S_RUSE(335, 36, false, true),
    EAGLES_PEAK(2780, 37, true, true),
    ELEMENTAL_WORKSHOP_I(13718, 38, true, true),
    ELEMENTAL_WORKSHOP_II(2639, 39, true, true),
    ENAKHRA_S_LAMENT(1560, 40, true, true),
    THE_ENCHANTED_KEY(13717, 41, true, false), // miniquest
    ENLIGHTENED_JOURNEY(2866, 42, true, true),
    ENTER_THE_ABYSS(492, 43, false, false), // miniquest
    ERNEST_THE_CHICKEN(32, 44, false, true),
    THE_EYES_OF_GLOUPHRIE(2497, 45, true, true),
    FAIRYTALE_I_GROWING_PAINS(1803, 46, true, true),
    FAIRYTALE_II_CURE_A_QUEEN(2326, 47, true, true),
    FAMILY_CREST(148, 48, false, true),
    FAMILY_PEST(5347, 49, true, false), // miniquest
    THE_FEUD(334, 50, true, true),
    FIGHT_ARENA(17, 51, false, true),
    FISHING_CONTEST(11, 52, false, true),
    FORGETTABLE_TALE(822, 53, true, true),
    THE_FORSAKEN_TOWER(7796, 54, true, true),
    THE_FREMENNIK_EXILES(9459, 55, true, true),
    THE_FREMENNIK_ISLES(3311, 56, true, true),
    THE_FREMENNIK_TRIALS(347, 57, false, true),
    GARDEN_OF_TRANQUILLITY(961, 58, true, true),
    THE_GENERAL_S_SHADOW(3330, 59, true, false), // miniquest
    GERTRUDE_S_CAT(180, 60, false, true),
    GETTING_AHEAD(693, 61, true, true),
    GHOSTS_AHOY(217, 62, true, true),
    THE_GIANT_DWARF(571, 63, true, true),
    GOBLIN_DIPLOMACY(2378, 64, true, true),
    THE_GOLEM(346, 65, true, true),
    THE_GRAND_TREE(150, 66, false, true),
    THE_GREAT_BRAIN_ROBBERY(980, 67, false, true),
    GRIM_TALES(2783, 68, true, true),
    THE_HAND_IN_THE_SAND(1527, 69, true, true),
    HAUNTED_MINE(382, 70, false, true),
    HAZEEL_CULT(223, 71, false, true),
    HEROES_QUEST(188, 72, false, true),
    HOLY_GRAIL(5, 73, false, true),
    HORROR_FROM_THE_DEEP(34, 74, true, true),
    ICTHLARIN_S_LITTLE_HELPER(418, 75, true, true),
    IMP_CATCHER(160, 76, false, true),
    IN_AID_OF_THE_MYREQUE(1990, 77, true, true),
    IN_SEARCH_OF_KNOWLEDGE(8403, 78, true, false), // miniquest
    IN_SEARCH_OF_THE_MYREQUE(387, 79, false, true),
    JUNGLE_POTION(175, 80, false, true),
    A_KINGDOM_DIVIDED(12296, 81, true, true),
    KING_S_RANSOM(3888, 82, true, true),
    THE_KNIGHT_S_SWORD(122, 83, false, true),
    LAIR_OF_TARN_RAZORLOR(3290, 84, true, false), // miniquest
    LEGENDS_QUEST(139, 85, false, true),
    LOST_CITY(147, 86, false, true),
    THE_LOST_TRIBE(532, 87, true, true),
    LUNAR_DIPLOMACY(2448, 88, true, true),
    MAGE_ARENA_I(267, 89, false, false), // miniquest
    MAGE_ARENA_II(6067, 90, true, false), // miniquest
    MAKING_FRIENDS_WITH_MY_ARM(6528, 91, true, true),
    MAKING_HISTORY(1383, 92, true, true),
    MERLIN_S_CRYSTAL(14, 93, false, true),
    MISTHALIN_MYSTERY(3468, 94, true, true),
    MONKEY_MADNESS_I(365, 95, false, true),
    MONKEY_MADNESS_II(5027, 96, true, true),
    MONK_S_FRIEND(30, 97, false, true),
    MOUNTAIN_DAUGHTER(260, 98, true, true),
    MOURNING_S_END_PART_I(517, 99, false, true),
    MOURNING_S_END_PART_II(1103, 100, true, true),
    MURDER_MYSTERY(192, 101, false, true),
    MY_ARM_S_BIG_ADVENTURE(2790, 102, true, true),
    NATURE_SPIRIT(307, 103, false, true),
    A_NIGHT_AT_THE_THEATRE(12276, 104, true, true),
    OBSERVATORY_QUEST(112, 105, false, true),
    OLAF_S_QUEST(3534, 106, true, true),
    ONE_SMALL_FAVOUR(416, 107, false, true),
    PIRATE_S_TREASURE(71, 108, false, true),
    PLAGUE_CITY(165, 109, false, true),
    A_PORCINE_OF_INTEREST(10582, 110, true, true),
    PRIEST_IN_PERIL(302, 111, false, true),
    PRINCE_ALI_RESCUE(273, 112, false, true),
    THE_QUEEN_OF_THIEVES(6037, 113, true, true),
    RAG_AND_BONE_MAN_I(714, 114, false, true),
    RAG_AND_BONE_MAN_II(714, 115, false, true),
    RATCATCHERS(1404, 116, true, true),
    RECIPE_FOR_DISASTER(1850, 117, true, true),
    RECRUITMENT_DRIVE(657, 118, true, true),
    REGICIDE(328, 119, false, true),
    THE_RESTLESS_GHOST(107, 120, false, true),
    ROMEO_JULIET(144, 121, false, true),
    ROVING_ELVES(402, 122, false, true),
    ROYAL_TROUBLE(2140, 123, true, true),
    RUM_DEAL(600, 124, false, true),
    RUNE_MYSTERIES(63, 125, false, true),
    SCORPION_CATCHER(76, 126, false, true),
    SEA_SLUG(159, 127, false, true),
    SHADES_OF_MORT_TON(339, 128, false, true),
    SHADOW_OF_THE_STORM(1372, 129, true, true),
    SHEEP_HERDER(60, 130, false, true),
    SHEEP_SHEARER(179, 131, false, true),
    SHIELD_OF_ARRAV(13716, 132, true, true),
    SHILO_VILLAGE(116, 133, false, true),
    SINS_OF_THE_FATHER(7255, 134, true, true),
    SKIPPY_AND_THE_MOGRES(1344, 135, true, false), // miniquest
    THE_SLUG_MENACE(2610, 136, true, true),
    SONG_OF_THE_ELVES(9016, 137, true, true),
    A_SOUL_S_BANE(2011, 138, true, true),
    SPIRITS_OF_THE_ELID(1444, 139, true, true),
    SWAN_SONG(2098, 140, true, true),
    TAI_BWO_WANNAI_TRIO(320, 141, false, true),
    A_TAIL_OF_TWO_CATS(1028, 142, true, true),
    TALE_OF_THE_RIGHTEOUS(6358, 143, true, true),
    A_TASTE_OF_HOPE(6396, 144, true, true),
    TEARS_OF_GUTHIX(451, 145, true, true),
    TEMPLE_OF_IKOV(26, 146, false, true),
    THRONE_OF_MISCELLANIA(359, 147, false, true),
    THE_TOURIST_TRAP(197, 148, false, true),
    TOWER_OF_LIFE(3337, 149, true, true),
    TREE_GNOME_VILLAGE(111, 150, false, true),
    TRIBAL_TOTEM(200, 151, false, true),
    TROLL_ROMANCE(385, 152, false, true),
    TROLL_STRONGHOLD(317, 153, false, true),
    UNDERGROUND_PASS(161, 154, false, true),
    VAMPYRE_SLAYER(178, 155, false, true),
    WANTED(1051, 156, true, true),
    WATCHTOWER(212, 157, false, true),
    WATERFALL_QUEST(65, 158, false, true),
    WHAT_LIES_BELOW(3523, 159, true, true),
    WITCH_S_HOUSE(226, 160, false, true),
    WITCH_S_POTION(67, 161, false, true),
    X_MARKS_THE_SPOT(8063, 162, true, true),
    ZOGRE_FLESH_EATERS(487, 163, true, true),
    THE_FROZEN_DOOR(13175, 164, true, false), // miniquest
    LAND_OF_THE_GOBLINS(13599, 165, true, true),
    HOPESPEAR_S_WILL(13619, 166, true, false), // miniquest
    TEMPLE_OF_THE_EYE(13738, 167, true, true),
    BENEATH_CURSED_SANDS(13841, 168, true, true),
    SLEEPING_GIANTS(13902, 169, true, true),
    THE_GARDEN_OF_DEATH(14609, 180, true, true),
    INTO_THE_TOMBS(13836, 2306, true, false), // miniquest
    RECIPE_FOR_DISASTER_ANOTHER_COOK_S_QUEST(1850, 2307, true, false), // subquest
    RECIPE_FOR_DISASTER_DWARF(1892, 2308, true, false), // subquest
    RECIPE_FOR_DISASTER_GOBLINS(1867, 2309, true, false), // subquest
    RECIPE_FOR_DISASTER_PIRATE(1895, 2310, true, false), // subquest
    RECIPE_FOR_DISASTER_LUMBRIDGE_GUIDE(1896, 2311, true, false), // subquest
    RECIPE_FOR_DISASTER_DAVE(1878, 2312, true, false), // subquest
    RECIPE_FOR_DISASTER_UGLOGWEE(1904, 2313, true, false), // subquest
    RECIPE_FOR_DISASTER_AMIK_VARZE(1910, 2314, true, false), // subquest
    RECIPE_FOR_DISASTER_AWOWOGEI(1914, 2315, true, false), // subquest
    RECIPE_FOR_DISASTER_CULINAROMANCER(1850, 2316, true, false), // subquest
    SECRETS_OF_THE_NORTH(14722, 2338, true, true),
    DESERT_TREASURE_II_THE_FALLEN_EMPIRE(14862, 2343, true, true),
    HIS_FAITHFUL_SERVANTS(14973, 3250, true, false), // miniquest
    THE_PATH_OF_GLOUPHRIE(15288, 3425, true, true),
    CHILDREN_OF_THE_SUN(9632, 3450, true, true),
    BARBARIAN_TRAINING(9613, 3451, true, false), // miniquest
    DEFENDER_OF_VARROCK(9655, 3466, true, true),
    WHILE_GUTHIX_SLEEPS(9653, 3467, true, true),
    TWILIGHT_S_PROMISE(9649, 3512, true, true),
    AT_FIRST_LIGHT(9835, 3513, true, true),
    PERLIOUS_MOONS(9819, 3514, true, true),
    THE_RIBBITING_TALE_OF_A_LILY_PAD_LABOUR_DISPUTE(9844, 3515, true, true),
    THE_HEART_OF_DARKNESS(11117, 3710, true, true),
    DEATH_ON_THE_ISLE(11210, 3711, true, true),
    MEAT_AND_GREET(11182, 3712, true, true),
    ETHICALLY_ACQUIRED_ANTIQUITIES(11193, 3713, true, true),
    THE_CURSE_OF_ARRAV(11479, 3937, true, true),
    FINAL_DAWN(16663, 5189, true, true),
    SHADOWS_OF_CUSTODIA(16632, 5190, true, true),
    SCRAMBLED(16758, 5191, true, true),
    EXISTENTIAL_CRISIS(16661, 5192, true, true),
    IMPENDING_CHAOS(16662, 5193, true, true),
    PANDEMONIUM(18314, 7103, true, true),
    PRYING_TIMES(18317, 7104, true, true),
    CURRENT_AFFAIRS(18282, 7105, true, true),
    TROUBLED_TORTUGANS(18321, 7106, true, true),
    THE_RED_REEF(18335, 7107, true, true),
    BURIAL_AT_SEA(18274, 7108, true, true),
    FALLEN_FROM_GRACE(15759, 7133, true, true),
    LEARNING_THE_ROPES(281, 9643, false, true),
    THE_IDES_OF_MILK(20106, 9645, true, true),
    THE_BLOOD_MOON_RISES(15464, 16414, true, true),
    A_RUFF_SITUATION(15889, 16971, true, true),
    CRAB_QUEST(15842, 16972, true, true),
    VALE_TOTEMS(4758, 5194, false, false),
    ;

    public static final Quest[] values = values();
    private final int variable, dbTableIndex;
    private final boolean isVarbit, counted;

    Quest(final int variable, final int dbTableIndex, final boolean isVarbit, final boolean counted) {
        this.variable = variable;
        this.dbTableIndex = dbTableIndex;
        this.isVarbit = isVarbit;
        this.counted = counted;
    }

    public int getVariable() {
        return variable;
    }

    public int getDbTableIndex() {
        return dbTableIndex;
    }

    public boolean isVarbit() {
        return isVarbit;
    }

    public boolean isCounted() {
        return counted;
    }

}
