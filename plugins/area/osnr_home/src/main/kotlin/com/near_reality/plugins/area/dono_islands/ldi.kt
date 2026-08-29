package com.near_reality.plugins.area.dono_islands

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Ldi : NPCSpawnsScript() {

    init {
        BLACK_SALAMANDER(1689, 2617, 0, SOUTH, 4)
        BLACK_SALAMANDER(1689, 2610, 0, SOUTH, 4)
        BLACK_SALAMANDER(1692, 2609, 0, SOUTH, 4)
        BLACK_SALAMANDER(1691, 2615, 0, SOUTH, 4)
        BLACK_SALAMANDER(1694, 2612, 0, SOUTH, 4)
        BLACK_SALAMANDER(1693, 2610, 0, SOUTH, 4)


        TOOL_LEPRECHAUN(1716, 2614, 0, SOUTH, 1)
        MASTER_FARMER(1702, 2604, 0, SOUTH, 2)
    }
}
