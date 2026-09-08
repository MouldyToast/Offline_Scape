package org.jesse.game.content.gauntlet.npc

import org.jesse.game.content.gauntlet.gauntletStrongMonsterKills
import org.jesse.game.content.gauntlet.gauntletWeakMonsterKills
import org.jesse.game.content.gauntlet.map.GauntletMap
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player

class GauntletNPC(id: Int, tile: Location?, facing: Direction?, radius: Int, private val map: GauntletMap) :
    NPC(id, tile, facing, radius) {

    init {
        isSpawned = true
        supplyCache = false
    }

    override fun canMove(fromX: Int, fromY: Int, direction: Int): Boolean {
        val next = Location(fromX + Utils.DIRECTION_DELTA_X[direction], fromY + Utils.DIRECTION_DELTA_Y[direction], plane)
        return !map.inSafeRoom(next) && super.canMove(fromX, fromY, direction)
    }

    override fun onMovement() {
        super.onMovement()
        if (getCombat().underCombat() && map.inSafeRoom(location))
            getCombat().removeTarget()
    }

    override fun onFinish(source: Entity?) {
        reset()
        finish()
    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)

        if (source is Player) {
            val demiBoss = GauntletMonsterType.values().find { id == it.npcId || id == it.corruptedNpcId }
            if (demiBoss != null) {
                when (demiBoss) {
                    GauntletMonsterType.RAT,
                    GauntletMonsterType.SPIDER,
                    GauntletMonsterType.BAT -> source.gauntletWeakMonsterKills++
                    GauntletMonsterType.UNICORN,
                    GauntletMonsterType.SCORPION,
                    GauntletMonsterType.WOLF -> source.gauntletStrongMonsterKills++
                    else -> {}
                }
            }
        }
        drop(middleLocation)
    }

}
