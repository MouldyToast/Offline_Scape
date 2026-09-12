package org.jesse.game.world.entity.player.privilege;

/**
 * !!! DO NOT RENAME ENUMS IN HERE OR IT WILL BREAK {@link org.jesse.game.content.middleman.MiddleManStaffOption.GsonAdapter}.
 */
public enum Crown {
    NONE(0, CrownType.NONE),
    STANDARD_IRON_MAN(2, CrownType.GAME_MODE),
    ULTIMATE_IRON_MAN(3, CrownType.GAME_MODE),
    HARDCORE_IRON_MAN(10, CrownType.GAME_MODE),

    // Member (donor) crowns have no vanilla sprite; id -1 means "no icon" and getCrownTag() returns an empty string.
    TOPAZ(-1, CrownType.MEMBER),
    SAPPHIRE(-1, CrownType.MEMBER),
    EMERALD(-1, CrownType.MEMBER),
    RUBY(-1, CrownType.MEMBER),
    DIAMOND(-1, CrownType.MEMBER),
    DRAGONSTONE(-1, CrownType.MEMBER),
    ONYX(-1, CrownType.MEMBER),
    ZENYTE(-1, CrownType.MEMBER),
    ENCHANTED(-1, CrownType.MEMBER),
    GOLD(-1, CrownType.MEMBER),
    ETERNAL(-1, CrownType.MEMBER),
    NEBULA(-1, CrownType.MEMBER),
    CATALYTIC(-1, CrownType.MEMBER),

    YOUTUBER(7, CrownType.RANK),
    FORUM_MODERATOR(1, CrownType.RANK),
    SUPPORT(1, CrownType.RANK),
    MODERATOR(1, CrownType.RANK),
    SENIOR_MODERATOR(1, CrownType.RANK),
    ADMINISTRATOR(1, CrownType.RANK),
    DEVELOPER(1, CrownType.RANK),
    TRUE_DEVELOPER(1, CrownType.RANK),

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
        this.tag = id < 0 ? "" : "<img=" + id + ">";
    }

    public int getId() {
        return id;
    }

    public CrownType getType() {
        return type;
    }

    public String getCrownTag() {
        return this == NONE || id < 0 ? "" : tag;
    }
}
