package org.jesse.game.content.dt2.npc.whisperer

import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.npc.combat.CombatScript

/**
 * @author Khaled Abdeljaber
 */
class WhispererSoulNPC(
    id: Int, tile: Location, facing: Direction
) : NPC(id, tile, facing, 0), CombatScript {

    override fun setFaceEntityCombat(entity: Entity?) {

    }

    override fun attack(target: Entity?): Int {
        return -1
    }

    override fun setRespawnTask() {

    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        remove()
    }
}