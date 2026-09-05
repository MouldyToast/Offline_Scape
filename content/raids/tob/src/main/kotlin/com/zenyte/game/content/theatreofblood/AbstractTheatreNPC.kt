package com.zenyte.game.content.theatreofblood

import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC

/**
 * Base class for Theatre of Blood NPCs. Relocated from the ToA package
 * (com.zenyte.game.content.tombsofamascut), where it had squatted since the
 * NR codebase; its only subclass is ToB's TheatreNPC. Converted to Kotlin
 * per the touch-it-convert-it policy (tob's sourceset is Kotlin-only).
 * NOTE: tile is nullable — TheatreNPC passes Location? through.
 */
abstract class AbstractTheatreNPC(id: Int, tile: Location?, facing: Direction, radius: Int) :
    NPC(id, tile, facing, radius) {

    override fun isRaidNpcForTwistedBow(): Boolean {
        return true
    }

    override fun isVampyric(): Boolean {
        return true
    }
}
