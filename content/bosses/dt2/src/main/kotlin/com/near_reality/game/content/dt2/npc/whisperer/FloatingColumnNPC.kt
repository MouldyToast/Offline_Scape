package com.near_reality.game.content.dt2.npc.whisperer

import com.near_reality.game.content.dt2.npc.findObject
import com.near_reality.game.content.dt2.npc.remove
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.combat.CombatScript

/**
 * @author Khaled Abdeljaber
 */
class FloatingColumnNPC(
    id: Int, tile: Location, facing: Direction, var whisperer: WhispererNPC?
) : NPC(id, tile, facing, 0), CombatScript {

    override fun setFaceEntityCombat(entity: Entity?) {

    }

    override fun attack(target: Entity?): Int {
        return -1
    }

    override fun sendDeath() {
        super.sendDeath()

        val obj = position.findObject { type == 10 }
        if (obj != null) {
            whisperer?.removeObjects(obj)

            obj.remove()
        }

        whisperer = null
    }

    override fun setRespawnTask() {

    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        remove()
    }

    override fun isCycleHealable(): Boolean {
        return false
    }
}