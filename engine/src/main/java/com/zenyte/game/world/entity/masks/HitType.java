package com.zenyte.game.world.entity.masks;

/**
 * @author Kris | 28. march 2018 : 0:37.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 * <p>
 * Mark id must be a positive integer!
 */
public enum HitType {
    MISSED(12, 13),
    //Regular does not invoke special effects such as vengeance.
    REGULAR(16, 17),
    //Used for damage not applied by any direct combat (thieving)
    TYPELESS(16, 17),
    //Default invokes special effects such as vengeance.
    DEFAULT(16, 17),
    MELEE(16, 17),
    MAGIC(16, 17),
    RANGED(16, 17),
    POISON(2),
    YELLOW(3),
    DISEASED(4),
    VENOM(5),
    HEALED(6),
    SHIELD_CHARGE(11),
    PALM_LOWER(15),
    SHIELD(18, 19),
    ARMOUR(20, 21),
    CHARGE(22, 23),
    DISCHARGE(24, 25),
    CORRUPTION(0),
    SHIELD_DOWN(60),
    PRAYER_DRAIN(60),
    BLEED(67),
    SANITY_DRAIN(71),
    SANITY_RESTORE(72),
    DOOM(73),
    BURN(74),
    WARDENS(53, 54);

    private final int id;

    /**
     * The ID of the "tinted" version of this hitsplat. The tinted variant is used when displaying hits on the client
     * side that don't come from you AND don't damage you.
     * <p>
     * In other words, if the hit has nothing to do with you it is displayed as a less significant variant.
     */
    private final int tintedId;

    public static final HitType[] values = values();

    HitType(int id, int tintedId) {
        this.id = id;
        this.tintedId = tintedId;
    }

    HitType(int id) {
        this.id = id;
        this.tintedId = id;
    }

    public int getId() {
        return id;
    }

    public int getTintedId() {
        return tintedId;
    }

    public int getMaxId() {
        return switch (this) {
            case DEFAULT, REGULAR, MELEE, MAGIC, RANGED -> 43;
            case SHIELD -> 44;
            case ARMOUR -> 45;
            default -> id;
        };
    }

}