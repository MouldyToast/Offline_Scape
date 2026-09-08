package org.jesse.game.world.entity.player.privilege;

/**
 * !!! DO NOT RENAME ENUMS IN HERE OR IT WILL BREAK {@link org.jesse.game.content.middleman.MiddleManStaffOption.GsonAdapter}.
 */
public enum Crown {
    NONE(0, CrownType.NONE),
    STANDARD_IRON_MAN(2, CrownType.GAME_MODE),
    ULTIMATE_IRON_MAN(3, CrownType.GAME_MODE),
    HARDCORE_IRON_MAN(10, CrownType.GAME_MODE),

    TOPAZ(78, CrownType.MEMBER),
    SAPPHIRE(71, CrownType.MEMBER),
    EMERALD(72, CrownType.MEMBER),
    RUBY(73, CrownType.MEMBER),
    DIAMOND(74, CrownType.MEMBER),
    DRAGONSTONE(75, CrownType.MEMBER),
    ONYX(76, CrownType.MEMBER),
    ZENYTE(77, CrownType.MEMBER),
    ENCHANTED(79, CrownType.MEMBER),
    GOLD(80, CrownType.MEMBER),
    ETERNAL(83, CrownType.MEMBER),
    NEBULA(81, CrownType.MEMBER),
    CATALYTIC(82, CrownType.MEMBER),

    YOUTUBER(7, CrownType.RANK),
    FORUM_MODERATOR(6, CrownType.RANK),
    SUPPORT(4, CrownType.RANK),
    MODERATOR(0, CrownType.RANK),
    SENIOR_MODERATOR(0, CrownType.RANK),
    ADMINISTRATOR(1, CrownType.RANK),
    DEVELOPER(5, CrownType.RANK),
    TRUE_DEVELOPER(69, CrownType.RANK),

    GROUP_IRON_MAN(41, CrownType.GAME_MODE),
    HARDCORE_GROUP_IRON_MAN(42, CrownType.GAME_MODE),
    REALIST_REGULAR(43, CrownType.GAME_MODE),
    REALIST_STANDARD_IRON_MAN(44, CrownType.GAME_MODE),
    REALIST_HARDCORE_IRON_MAN(45, CrownType.GAME_MODE),
    REALIST_ULTIMATE_IRON_MAN(46, CrownType.GAME_MODE),

    ;
    private final int id;
    private final CrownType type;
    private final String tag;

    Crown(int id, CrownType type) {
        this.id = id;
        this.type = type;
        this.tag = "<img=" + id + ">";
    }

    public int getId() {
        return id;
    }

    public CrownType getType() {
        return type;
    }

    public String getCrownTag() {
        return this == NONE ? "" : tag;
    }
}
