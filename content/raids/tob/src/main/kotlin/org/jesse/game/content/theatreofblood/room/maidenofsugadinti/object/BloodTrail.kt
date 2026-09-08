package org.jesse.game.content.theatreofblood.room.maidenofsugadinti.`object`

import org.jesse.game.content.theatreofblood.room.maidenofsugadinti.npc.MaidenOfSugadinti
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Jire
 * @author Tommeh
 */
internal class BloodTrail(
    private val maiden: MaidenOfSugadinti,
    private val tile: Location,
    private val initialTicks: Int = 1 + (if (maiden.room.raid.hardMode) 45 else 30)
) : WorldObject(32984, DEFAULT_TYPE, Utils.random(3), tile) {

    private var ticks = initialTicks

    fun process() = if (maiden.dead()) false
    else when (--ticks) {
        initialTicks - 1 -> {
            maiden.addSplat(tile)
            World.spawnObject(this)
            true
        }
        0 -> {
            remove()
            false
        }
        else -> true
    }

    fun remove() {
        maiden.removeSplat(tile)
        World.removeObject(this)
    }

    fun resetTimer() {
        ticks = initialTicks
    }

}