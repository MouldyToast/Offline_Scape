package mgi.types.config.enums;

import java.util.function.Supplier;

/**
 * @author Kris | 20/11/2018 20:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 *
 * <p>An interface used for storing constants of enums, as well as helper methods.</p>
 * <p>Type is interface as opposed to a class to prevent unnecessary cluttering of "public static final"...
 * per every constant, as well as to ensure that the server runs flawlessly in the scenario in which
 * one of the enums provided here changes in the cache, making it throw an exception. Lazy class initialization
 * could otherwise create issues.</p>
 */
public interface Enums {

    Supplier<RuntimeException> runtimeExceptionSupplier = RuntimeException::new;

    static Supplier<RuntimeException> exception() {
        return runtimeExceptionSupplier;
    }

    IntEnum ITEM_RETRIEVAL_SERVICE = EnumDefinitions.getIntEnumOrEmpty(1757);
    IntEnum RAIDS_ONLY_ITEMS = EnumDefinitions.getIntEnumOrEmpty(1666);
    IntEnum ITEM_SETS = EnumDefinitions.getIntEnumOrEmpty(1034);
    IntEnum FARMING_WATERING_CANS = EnumDefinitions.getIntEnumOrEmpty(136);
    StringEnum MINIGAMES_LIST = EnumDefinitions.getStringEnumOrEmpty(848);
    IntEnum BANK_EQUIPMENT_TAB_SLOT_MAP = EnumDefinitions.getIntEnumOrEmpty(2777);
    IntEnum DEPOSIT_BOX_EQUIPMENT_TAB_SLOT_MAP = EnumDefinitions.getIntEnumOrEmpty(2737);
    IntEnum CL_BOSSES = EnumDefinitions.getIntEnumOrEmpty(2103);
    StringEnum SKILL_DIALOGUE_STRING = EnumDefinitions.getStringEnumOrEmpty(1809);

    StringEnum EXPERIENCE_TRACKER_COLOURS = EnumDefinitions.getStringEnumOrEmpty(1168);
    StringEnum EXPERIENCE_TRACKER_DURATION = EnumDefinitions.getStringEnumOrEmpty(1166);
    StringEnum EXPERIENCE_TRACKER_SIZE = EnumDefinitions.getStringEnumOrEmpty(1165);
    StringEnum EXPERIENCE_TRACKER_POSITION = EnumDefinitions.getStringEnumOrEmpty(1164);
    StringEnum EXPERIENCE_TRACKER_GROUP = EnumDefinitions.getStringEnumOrEmpty(1170);
    StringEnum EXPERIENCE_TRACKER_SPEED = EnumDefinitions.getStringEnumOrEmpty(1140);

    IntEnum PUZZLE_BOX_ENUMS = EnumDefinitions.getIntEnumOrEmpty(1864);

    IntEnum STASH_UNIT_BUILD_STAGES_VARS = EnumDefinitions.getIntEnumOrEmpty(1440);
    IntEnum STASH_UNIT_BUILD_STAGES_CONTAINER = EnumDefinitions.getIntEnumOrEmpty(1525);

    // IntEnum ITEMS_ALWAYS_LOST_ON_DEATH = EnumDefinitions.getIntEnumOrEmpty(879);

    IntEnum DIANGO_ITEM_RETRIEVAL = EnumDefinitions.getIntEnumOrEmpty(708);

    IntEnum FAKE_XP_DROPS = EnumDefinitions.getIntEnumOrEmpty(681);

    IntEnum TASK_EXTENSION_ENUM = EnumDefinitions.getIntEnumOrEmpty(273);
    IntEnum TASK_COST_ENUM = EnumDefinitions.getIntEnumOrEmpty(836);
    IntEnum TASK_DISABLE_ENUM = EnumDefinitions.getIntEnumOrEmpty(854);
    StringEnumLC SLAYER_PERK_REWARD_NAMES = EnumDefinitions.getStringEnumLowercaseOrEmpty(834);
    IntEnum SLAYER_ITEM_REWARDS_ENUM = EnumDefinitions.getIntEnumOrEmpty(840);
    IntEnum SLAYER_REWARDS_COST = EnumDefinitions.getIntEnumOrEmpty(842);

    // StringEnum COSTUME_STORAGE_UNIT_ENUM = EnumDefinitions.getStringEnumOrEmpty(380);

    StringEnum EMOTES_ENUM = EnumDefinitions.getStringEnumOrEmpty(1000);

    StringEnum SKILL_GUIDES_ENUM = EnumDefinitions.getStringEnumOrEmpty(108);

    StringEnum FAIRY_RING_CODES = EnumDefinitions.getStringEnumOrEmpty(823);
    IntEnum FAIRY_RING_VARBIT_CODES = EnumDefinitions.getIntEnumOrEmpty(824);

    IntEnum REGULAR_SPELLS_ENUM = EnumDefinitions.getIntEnumOrEmpty(1982);
    IntEnum ENCHANT_SIDEBAR_ENUM = EnumDefinitions.getIntEnumOrEmpty(5285);
    IntEnum ANCIENT_SPELLS_ENUM = EnumDefinitions.getIntEnumOrEmpty(1983);
    IntEnum LUNAR_SPELLS_ENUM = EnumDefinitions.getIntEnumOrEmpty(1984);
    IntEnum ARCEUUS_SPELLS_ENUM = EnumDefinitions.getIntEnumOrEmpty(1985);
    IntEnum AUTOCASTABLE_SPELLS_ENUM = EnumDefinitions.getIntEnumOrEmpty(1986);

    StringEnum PEST_CONTROL_REWARDS_ENUM = EnumDefinitions.getStringEnumOrEmpty(2285);
    IntEnum PEST_CONTROL_POINTS_ENUM = EnumDefinitions.getIntEnumOrEmpty(2286);
    IntEnum PEST_CONTROL_REWARDS_VOID_ELEMENTS_ENUM = EnumDefinitions.getIntEnumOrEmpty(2287);
    IntEnum PEST_CONTROL_REWARDS_PACKS_STATS_ENUM = EnumDefinitions.getIntEnumOrEmpty(2288);
    StringEnum SKILL_NAMES_ENUM = EnumDefinitions.getStringEnumOrEmpty(680);

    IntEnum TOURNAMENT_ITEMS_ENUM = EnumDefinitions.getIntEnumOrEmpty(10024);
    IntEnum TOURNAMENT_REWARDS = EnumDefinitions.getIntEnumOrEmpty(10053);
    IntEnum TOURNAMENT_REWARDS_NUM = EnumDefinitions.getIntEnumOrEmpty(10054);
    IntEnum TOURNAMENT_REWARDS_COST = EnumDefinitions.getIntEnumOrEmpty(10055);
    IntEnum TOURNAMENT_REWARDS_IRONMAN = EnumDefinitions.getIntEnumOrEmpty(10056);

    IntEnum OPHELD_TO_IFBUTTON = EnumDefinitions.getIntEnumOrEmpty(4303);

    IntEnum NON_SEARCHABLE_SETTINGS_CATEGORIES = EnumDefinitions.getIntEnumOrEmpty(423);
    IntEnum SEARCHABLE_SETTINGS_CATEGORIES = EnumDefinitions.getIntEnumOrEmpty(422);

    IntEnum KEYBINDS = EnumDefinitions.getIntEnumOrEmpty(1161);

    IntEnum GIM_STORAGE_REQS = EnumDefinitions.getIntEnumOrEmpty(4216);

    IntEnum TOB_SUPPLIES_SLOT_TO_ITEM = EnumDefinitions.getIntEnumOrEmpty(1952);
    IntEnum TOB_SUPPLIES_ITEM_TO_COST = EnumDefinitions.getIntEnumOrEmpty(1953);

    IntEnum TOURNAMENT_SUPPLIES_BY_CHILD = EnumDefinitions.getIntEnumOrEmpty(1124);

    IntEnum HAIR_STYLES_DB_POINTER = EnumDefinitions.getIntEnumOrEmpty(496);
    IntEnum FACIAL_HAIR_STYLES_DB_POINTER = EnumDefinitions.getIntEnumOrEmpty(2630);
    IntEnum TORSO_STYLES_DB_POINTER = EnumDefinitions.getIntEnumOrEmpty(5495);
    IntEnum SLEEVE_STYLES_DB_POINTER = EnumDefinitions.getIntEnumOrEmpty(5496);
    IntEnum LEGGING_STYLES_DB_POINTER = EnumDefinitions.getIntEnumOrEmpty(5497);
    IntEnum SHOE_STYLES_DB_POINTER = EnumDefinitions.getIntEnumOrEmpty(5498);
    IntEnum HAND_STYLES_DB_POINTER = EnumDefinitions.getIntEnumOrEmpty(5499);

}
