package org.jesse.game.content.gauntlet.npc

import org.jesse.game.npc.ids.*

/**
 * @author Andys1814.
 * @since 1/21/2022.
 */
enum class GauntletMonsterType(val npcId: Int, val corruptedNpcId: Int) {

    RAT(CRYSTALLINE_RAT, CORRUPTED_RAT),
    SPIDER(CRYSTALLINE_SPIDER, CORRUPTED_SPIDER),
    BAT(CRYSTALLINE_BAT, CORRUPTED_BAT),
    UNICORN(CRYSTALLINE_UNICORN, CORRUPTED_UNICORN),
    SCORPION(CRYSTALLINE_SCORPION, CORRUPTED_SCORPION),
    WOLF(CRYSTALLINE_WOLF, CORRUPTED_WOLF),
    BEAR(CRYSTALLINE_BEAR, CORRUPTED_BEAR),
    DRAGON(CRYSTALLINE_DRAGON, CORRUPTED_DRAGON),
    DARK_BEAST(CRYSTALLINE_DARK_BEAST, CORRUPTED_DARK_BEAST);

    companion object {
        @JvmField val TIER_ONE = arrayOf(RAT, SPIDER, BAT)
        @JvmField val TIER_TWO = arrayOf(UNICORN, SCORPION, WOLF)
        @JvmField val DEMI_BOSS = arrayOf(BEAR, DRAGON, DARK_BEAST)
    }
}
