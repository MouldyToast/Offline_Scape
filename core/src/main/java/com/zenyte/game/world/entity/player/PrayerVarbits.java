package com.zenyte.game.world.entity.player;

/**
 * Prayer varbit ids, mirrored from the Prayer enum so engine hot paths can
 * read prayer state without importing content types. Post-T3.1a these bits
 * always reflect true effect (see docs/player-field-extraction/
 * HANDOVER_after_G3.md §7). Raw ids until the RSCM symbolic-name phase.
 *
 * Provenance (verified 2026-09-06 against osrs-dumps @ f54d6da7,
 * rev-228 / OpenRS2 #2043):
 * - config/dump.varbit: all eight are single bits of varplayer_83 —
 *   4112=bit8, 4116/4117/4118=bits12/13/14, 4119/4120/4121=bits15/16/17,
 *   4129=bit26 — matching the enum's prayer-list ordering.
 * - script/[proc,prayer_updatebutton].cs2: every prayer button re-renders
 *   on varplayer_83 transmits, i.e. varp 83 is the client's prayer
 *   display state.
 * - Read≡write holds by construction regardless: these constants mirror
 *   the same enum PrayerManager.sendBit(prayer.getVarbit()) writes with.
 */
public final class PrayerVarbits {
    public static final int PROTECT_ITEM = 4112;          // Prayer.PROTECT_ITEM
    public static final int PROTECT_FROM_MAGIC = 4116;    // Prayer.PROTECT_FROM_MAGIC
    public static final int PROTECT_FROM_MISSILES = 4117; // Prayer.PROTECT_FROM_MISSILES
    public static final int PROTECT_FROM_MELEE = 4118;    // Prayer.PROTECT_FROM_MELEE
    public static final int RETRIBUTION = 4119;           // Prayer.RETRIBUTION
    public static final int REDEMPTION = 4120;            // Prayer.REDEMPTION
    public static final int SMITE = 4121;                 // Prayer.SMITE
    public static final int PIETY = 4129;                 // Prayer.PIETY

    private PrayerVarbits() {}
}
