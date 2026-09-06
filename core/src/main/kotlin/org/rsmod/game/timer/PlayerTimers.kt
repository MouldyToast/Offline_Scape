package org.rsmod.game.timer

/**
 * Pre-RSCM soft-timer id registry. Ids are stable and append-only; the
 * RSCM phase replaces them with symbolic timer names. GRAVESTONE and
 * AVAS_DEVICE are reserved for the T2-a tick-driver moves.
 */
public object PlayerTimers {
    public const val FARMING: Short = 1
    public const val HUNTER: Short = 2
    public const val PRAYER_DRAIN: Short = 3
    public const val GRAVESTONE: Short = 4
    public const val AVAS_DEVICE: Short = 5
}
