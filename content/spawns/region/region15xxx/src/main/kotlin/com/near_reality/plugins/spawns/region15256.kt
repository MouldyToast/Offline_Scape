package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region15256 : NPCSpawnsScript() {

    init {
        LITHIL(3790, 9760, 1, SOUTH, 5)
        OATHBREAKER_EPIWS(3806, 9734, 1, SOUTH, 5)
        9459(3805, 9745, 1, EAST, 0)
        OATHBREAKER_BATS(3807, 9732, 1, SOUTH, 5)
        SISTER_ASERET(3808, 9728, 1, SOUTH, 5)
        OATH_LORD_DROWS(3809, 9735, 1, SOUTH, 5)
        OATHBREAKER_MALS(3810, 9733, 1, SOUTH, 5)
        9473(3811, 9777, 1, WEST, 0)
        LUMIERE(3826, 9760, 1, SOUTH, 5)
        DAER_KRAND(3821, 9778, 2, SOUTH, 5)
    }
}
