package org.jesse.game.world.entity.player.privilege;

import org.jesse.game.util.TimeUtils;
import org.jesse.utils.Ordinal;
import org.jesse.utils.TimeUnit;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 5-4-2019 | 16:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@Ordinal
public enum MemberRank implements IPrivilege {

    NONE(       0,      0,          Crown.NONE,         "000000",   60,        -1),
    TOPAZ(      1,      0.0D,       Crown.TOPAZ,        "FF5785",   60,        12),
    SAPPHIRE(   2,      0.02D,      Crown.SAPPHIRE,     "6E9EEB",   50,        10),
    EMERALD(    3,      0.03D,      Crown.EMERALD,      "93C47D",   40,        8),
    RUBY(       4,      0.04D,      Crown.RUBY,         "CB2E00",   30,        6),
    DIAMOND(    5,      0.05D,      Crown.DIAMOND,      "F3F3F3",   20,        4),
    DRAGONSTONE(6,      0.06D,      Crown.DRAGONSTONE,  "B4A7D6",   10,        2),
    ONYX(       7,      0.08D,       Crown.ONYX,         "666666",  0,            0),
    ZENYTE(     8,      0.10D,      Crown.ZENYTE,       "F5B26B",   0,            0),
    ENCHANTED(  9,      0.12D,       Crown.ENCHANTED,    "C9DAF8",  0,            0),
    GOLD(       10,     0.14D,      Crown.GOLD,         "FFE599",   0,            0),
    ETERNAL(    11,     0.16D,      Crown.ETERNAL,      "6FA8DC",   0,            0),
    NEBULA(     12,     0.18D,       Crown.NEBULA,       "D5A6BD",  0,            0),
    CATALYTIC(  13,     0.20D,      Crown.CATALYTIC,    "999999",   0,            0),
    MYTHICAL(   14,     0.20D,      Crown.CATALYTIC,    "000000",   0,            0),
    ;

    public static final MemberRank[] values = values();
    private final int id;
    private final Crown crown;
    private final double dropRate;
    private final String yellColor;
    private final int yellDelay;
    private final int togglesChance;

    MemberRank(int id, double dropRate, Crown crown, String yellColor, int yellDelaySeconds, final int togglesChance) {
        this.id = id;
        this.dropRate = dropRate;
        this.crown = crown;
        this.yellColor = yellColor;
        this.yellDelay = (int) TimeUnit.SECONDS.toTicks(yellDelaySeconds);
        this.togglesChance = togglesChance;
    }

    public static MemberRank fromId(int memberRank) {
        return MemberRank.values[memberRank];
    }

    public boolean equalToOrGreaterThan(final MemberRank member) {
        return getId() >= member.getId();
    }

    @Override
    public String toString() {
        return StringFormatUtil.formatString(name().toLowerCase().replace("_", " "));
    }

    public int getId() {
        return id;
    }

    public String getYellColor() {
        return yellColor;
    }

    public int getYellDelay() {
        return yellDelay;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Crown crown() {
        return crown;
    }

    public double getDR() {
        return dropRate;
    }

    public int getTogglesChance() {
        return togglesChance;
    }

    public double getNotedResourcePercentChance() {
        return switch (this) {
            case TOPAZ -> 0.1;
            case SAPPHIRE, EMERALD, RUBY -> 0.15;
            case DIAMOND, DRAGONSTONE, ONYX -> 0.2;
            case ZENYTE -> 0.25;
            case ENCHANTED -> 0.3;
            case GOLD -> 0.4;
            case ETERNAL -> 0.6;
            case NEBULA -> 0.75;
            case CATALYTIC -> 1.0;
            default -> 0.0;
        };
    }
}
