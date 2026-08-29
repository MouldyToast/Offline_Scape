package com.near_reality.game.content.remnantpets

import com.near_reality.game.item.CustomNpcId.DRIFTER
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.Spawnable

class DrifterNPC(id: Int, loc: Location?, dir: Direction?, rad: Int) : NPC(DRIFTER, loc, dir, rad), Spawnable {
    override fun validate(id: Int, name: String?): Boolean {
        return id == DRIFTER
    }

    override fun spawn(): NPC {
        val npc = super.spawn()
        if(location.regionId == 6582) {
            logger.info("Registering Drifter with companion object for force chat")
            instance = this
        }
        return npc
    }

    companion object {
        var instance : DrifterNPC? = null
    }
}