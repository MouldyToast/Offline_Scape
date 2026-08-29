package com.zenyte.game.content.skills.mining;

import com.zenyte.game.content.stars.ShootingStarLevel;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.utils.Ordinal;
import mgi.types.config.items.ItemDefinitions;

import java.util.EnumSet;
import java.util.Set;

@Ordinal
public enum OreDefinitions {
    /**
     * Ores
     */
    TIN(741600, 438, 1, 1, 2, 17.5, 2, 1, true, ObjectId.ROCKS_11360, ObjectId.ROCKS_11361, ObjectId.ROCKS_10080),
    COPPER(741600, 436, 1, 1, 2, 17.5, 2, 1, true, ObjectId.ROCKS_10943, ObjectId.ROCKS_11161, ObjectId.ROCKS_10079),
    CLAY(741600, 434, 1, 1, 2, 5, 2, 1, true, ObjectId.ROCKS_11362, ObjectId.ROCKS_11363),
    SOFT_CLAY(741600, ItemId.SOFT_CLAY, 70, 1, 67, 5, 2, 1, true, ObjectId.ROCKS_36210),
    BLURITE(741600, 668, 10, 10, 47, 17.5, 5, 1, true, ObjectId.ROCKS_11378, ObjectId.ROCKS_11379),
    IRON(741600, 440, 15, 15, 3, 35, 6, 1, true, ObjectId.ROCKS_11364, ObjectId.ROCKS_11365),
    SILVER(741600, 442, 20, 25, 17, 40, 8, 1, true, ObjectId.ROCKS_11368, ObjectId.ROCKS_11369),
    COAL(296640, 453, 30, 35, 12, 50, 6, 1, true, ObjectId.ROCKS_11366, ObjectId.ROCKS_11367),
    GOLD(296640, 444, 40, 45, 17, 65, 9, 1, true, ObjectId.ROCKS_11370, ObjectId.ROCKS_11371),
    MITHRIL(148320, 447, 55, 65, 33, 80, 12, 1, true, ObjectId.ROCKS_11372, ObjectId.ROCKS_11373),
    LOVAKITE(245562, 13356, 65, 100, 50, 10, 0, 1, true, ObjectId.ROCKS_28596, ObjectId.ROCKS_28597),
    ADAMANTITE(59328, 449, 70, 80, 67, 95, 15, 1, true, ObjectId.ROCKS_11374, ObjectId.ROCKS_11375),
    RUNITE(42377, 451, 85, 99, 150, 125, 18, 1, false, ObjectId.ROCKS_11376, ObjectId.ROCKS_11377),
    /**
     * Motherlode mine
     */
    PAYDIRT(-1, 12011, 30, 50, 150, 60, 0, 0, true, 26661, 26662, 26663, 26664),

    /**
     * Unique
     */
    SANDSTONE(741600, -1, 35, 25, 8, -1, 0, 1, true, ObjectId.ROCKS_11386),
    GRANITE(741600, -1, 45, 35, 8, -1, 0, 1, true, ObjectId.ROCKS_11387),
    GEM(211886, -1, 40, 60, 175, 65, 0, 1, false, ObjectId.ROCKS_11380, ObjectId.ROCKS_11381),
    RUNITE_GOLEM_ROCKS(42377, 451, 85, 99, -1, 125, 0, 1, false),
    ROCKSLIDE(-1, -1, 99, 15, 20, 0, 0, 1, false, 27062),
    ROCKFALL(-1, -1, 30, 20, 30, 10, 0, 1, false, 26679, 26680),
    URT_SALT(-1, 22597, 72, 70, 3, 5, 0, 11, false, 33254),
    EFH_SALT(-1, 22595, 72, 70, 15, 5, 0, 11, false, 33255),
    TE_SALT(-1, 22593, 72, 70, 15, 5, 0, 11, false, 33256),
    SALAX_SALT(-1, 28349, 72, 70, -1, 3, 0, 0, false, 47522),

    BASALT(-1, 22603, 72, 85, 15, 5, 0, 11, false, 33257),
    ESSENCE(-1, 7936, 1, 1, -1, 5, 0, 0, false, ObjectId.RUNE_ESSENCE_34773),
    DAEYALT_ESSENCE(-1, 24706, 60, 1, -1, 5, 0, 0, false, DaeyaltEssence.ESSENSE),

    VOLCANIC_ASH(741600, 21622, 22, 5, 50, 10, 0, 11, false, 30985),
    AMETHYST(46350, 21347, 92, 100, 42, 240, 0, 11, false, ObjectId.CRYSTALS, ObjectId.CRYSTALS_11389),
    ANCIENT_ESSENCE(-1, ItemId.ANCIENT_ESSENCE, 75, 15, 25, 13.5, 0, 15, false, 46701),
    /**
     * Castle wars
     */
    CWARS_ROCKS(-1, -1, 1, 35, 100, 0, 0, 1, false, 4437, 4438),
    CWARS_WALL(-1, -1, 1, 35, 100, 0, 0, 1, false, 4448),

    /**
     * Scar Essence Mine
     */
    SMALL_AMALGAMATION(-1, 28591, 64, 1, 5, 0, 0, 11, true, 49912),
    MEDIUM_AMALGAMATION(-1, 28591, 64, 1, 5, 0, 0, 11, true, 49913),
    LARGE_AMALGAMATION(-1, 28591, 64, 1, 5, 0, 0, 11, true, 49914),
    /**
     * End of Scar Essence Mine
     */

    ROCK_FORMATION(-1, 23905, 30, 99, 150, 35.2, 0, 0, true, 36193),
    GLOWING_ROCK_FORMATION(-1, 23905, 30, 20, 30, 35.2, 0, 0, true, 36192),

    /* Shooting stars */
    SHOOTING_STAR_LEVEL_ONE(-1, ItemId.STARDUST, 1, 35, 150, 1, 0, 0, false, ShootingStarLevel.ONE.getObjectId()),
    SHOOTING_STAR_LEVEL_TWO(-1, ItemId.STARDUST, 1, 45, 150, 1, 0, 0, false, ShootingStarLevel.TWO.getObjectId()),
    SHOOTING_STAR_LEVEL_THREE(-1, ItemId.STARDUST, 1, 55, 150, 1, 0, 0, false, ShootingStarLevel.THREE.getObjectId()),
    SHOOTING_STAR_LEVEL_FOUR(-1, ItemId.STARDUST, 1, 65, 150, 1, 0, 0, false, ShootingStarLevel.FOUR.getObjectId()),
    SHOOTING_STAR_LEVEL_FIVE(-1, ItemId.STARDUST, 1, 75, 150, 1, 0, 0, false, ShootingStarLevel.FIVE.getObjectId()),
    SHOOTING_STAR_LEVEL_SIX(-1, ItemId.STARDUST, 1, 85, 150, 1, 0, 0, false, ShootingStarLevel.SIX.getObjectId()),
    SHOOTING_STAR_LEVEL_SEVEN(-1, ItemId.STARDUST, 1, 95, 150, 1, 0, 0, false, ShootingStarLevel.SEVEN.getObjectId()),
    SHOOTING_STAR_LEVEL_EIGHT(-1, ItemId.STARDUST, 1, 110, 150, 1, 0, 0, false, ShootingStarLevel.EIGHT.getObjectId()),
    SHOOTING_STAR_LEVEL_NINE(-1, ItemId.STARDUST, 1, 120, 150, 1, 0, 0, false, ShootingStarLevel.NINE.getObjectId()),
//        SHOOTING_STAR_LEVEL_ONE(-1, ItemId.STARDUST, 10, 2, 150, 12, 0, 0, false, ShootingStarLevel.ONE.getObjectId()),
//        SHOOTING_STAR_LEVEL_TWO(-1, ItemId.STARDUST, 20, 2, 150, 22, 0, 0, false, ShootingStarLevel.TWO.getObjectId()),
//        SHOOTING_STAR_LEVEL_THREE(-1, ItemId.STARDUST, 30, 2, 150, 26, 0, 0, false, ShootingStarLevel.THREE.getObjectId()),
//        SHOOTING_STAR_LEVEL_FOUR(-1, ItemId.STARDUST, 40, 2, 150, 31, 0, 0, false, ShootingStarLevel.FOUR.getObjectId()),
//        SHOOTING_STAR_LEVEL_FIVE(-1, ItemId.STARDUST, 50, 2, 150, 48, 0, 0, false, ShootingStarLevel.FIVE.getObjectId()),
//        SHOOTING_STAR_LEVEL_SIX(-1, ItemId.STARDUST, 60, 2, 150, 74, 0, 0, false, ShootingStarLevel.SIX.getObjectId()),
//        SHOOTING_STAR_LEVEL_SEVEN(-1, ItemId.STARDUST, 70, 2, 150, 123, 0, 0, false, ShootingStarLevel.SEVEN.getObjectId()),
//        SHOOTING_STAR_LEVEL_EIGHT(-1, ItemId.STARDUST, 80, 2, 150, 162, 0, 0, false, ShootingStarLevel.EIGHT.getObjectId()),
//        SHOOTING_STAR_LEVEL_NINE(-1, ItemId.STARDUST, 90, 2, 150, 244, 0, 0, false, ShootingStarLevel.NINE.getObjectId()),
    ;

    private final int baseClueGeodeChance;
    private final int ore;
    private final int level;
    private final int speed;
    private final int time;
    private final int incinerationExperience;
    private final double xp;
    private final int depletionRate;
    private final int[] rocks;
    private final boolean extraOre;

    OreDefinitions(final int baseClueGeodeChance, int ore, int level, final int speed, int time, double xp,
                   int incinerationExperience, int depletionRate, boolean extraOre, int... rocks) {
        this.baseClueGeodeChance = baseClueGeodeChance;
        this.incinerationExperience = incinerationExperience;
        this.ore = ore;
        this.level = level;
        this.speed = speed;
        this.time = time;
        this.xp = xp;
        this.depletionRate = depletionRate;
        this.extraOre = extraOre;
        this.rocks = rocks;
    }

    public static OreDefinitions getDef(int id) {
        return MiningDefinitions.ores.get(id);
    }

    public String getName() {
        return ItemDefinitions.getOrThrow(this.ore).getName().toLowerCase().replace(" ore", "");
    }

    public boolean isSmeltable() {
        return this.equals(BLURITE) || this.equals(IRON) || this.equals(SILVER) || this.equals(GOLD) || this.equals(MITHRIL) || this.equals(ADAMANTITE) || this.equals(RUNITE);
    }

    public boolean isShootingStar() {
        final Set<OreDefinitions> stars = EnumSet.of(
                SHOOTING_STAR_LEVEL_ONE,
                SHOOTING_STAR_LEVEL_TWO,
                SHOOTING_STAR_LEVEL_THREE,
                SHOOTING_STAR_LEVEL_FOUR,
                SHOOTING_STAR_LEVEL_FIVE,
                SHOOTING_STAR_LEVEL_SIX,
                SHOOTING_STAR_LEVEL_SEVEN,
                SHOOTING_STAR_LEVEL_EIGHT,
                SHOOTING_STAR_LEVEL_NINE
        );
        return stars.contains(this);
    }

    public boolean isAmalgamation() {
        final Set<OreDefinitions> amalgamations = EnumSet.of(
                SMALL_AMALGAMATION,
                MEDIUM_AMALGAMATION,
                LARGE_AMALGAMATION
        );
        return amalgamations.contains(this);
    }

    ;

    public int getBaseClueGeodeChance() {
        return baseClueGeodeChance;
    }

    public int getOre() {
        return ore;
    }

    public int getLevel() {
        return level;
    }

    public int getSpeed() {
        return speed;
    }

    public int getTime() {
        return time;
    }

    public int getIncinerationExperience() {
        return incinerationExperience;
    }

    public double getXp() {
        return xp;
    }

    public int getDepletionRate() {
        return depletionRate;
    }

    public int[] getRocks() {
        return rocks;
    }

    public boolean isExtraOre() {
        return extraOre;
    }

}
